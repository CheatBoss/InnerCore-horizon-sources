package com.zhekasmirnov.innercore.api.runtime;

import com.zhekasmirnov.innercore.api.runtime.saver.world.*;
import com.zhekasmirnov.innercore.api.log.*;
import com.zhekasmirnov.innercore.api.runtime.saver.*;
import org.mozilla.javascript.*;
import java.util.*;
import com.zhekasmirnov.innercore.api.mod.*;
import com.zhekasmirnov.innercore.mod.executable.*;
import com.zhekasmirnov.apparatus.adapter.innercore.*;

public class Updatable
{
    private static final Object[] EMPTY_ARGS;
    public static final int MODE_COUNT_BASED = 0;
    public static final int MODE_TIME_BASED = 1;
    private static final Updatable clientInstance;
    private static int currentMode;
    private static int maxUpdateCallsPerTick;
    private static int maxUpdateTimePerTick;
    private static final Updatable serverInstance;
    private int currentArrayPosition;
    private Context currentContext;
    private final List<ScriptableObject> disabledDueToError;
    private final boolean isMultithreadingAllowed;
    private final ArrayList<ScriptableObject> postedRemovedUpdatables;
    private int statCurrentCalls;
    private final List<ScriptableObject> updatableObjects;
    
    static {
        Updatable.currentMode = 0;
        Updatable.maxUpdateCallsPerTick = 128;
        Updatable.maxUpdateTimePerTick = 50;
        EMPTY_ARGS = new Object[0];
        serverInstance = new Updatable(true);
        clientInstance = new Updatable(false);
        setPreferences(0, 256);
        WorldDataScopeRegistry.getInstance().addScope("_updatables", (WorldDataScopeRegistry.SaverScope)new ScriptableSaverScope() {
            @Override
            public void read(Object o) {
                final int n = 0;
                int n2 = 0;
                if (o instanceof NativeArray) {
                    final Object[] array = ((NativeArray)o).toArray();
                    for (int length = array.length, i = 0; i < length; ++i) {
                        final Object o2 = array[i];
                        if (o2 instanceof ScriptableObject) {
                            final ScriptableObject scriptableObject = (ScriptableObject)o2;
                            if (scriptableObject.get((Object)"update") instanceof Function) {
                                Updatable.getForServer().addUpdatable(scriptableObject);
                                ++n2;
                                continue;
                            }
                        }
                        final StringBuilder sb = new StringBuilder();
                        sb.append("loaded updatable data is not a scriptable object or it does not have update function, loading failed. obj=");
                        sb.append(o2);
                        ICLog.i("UPDATABLE", sb.toString());
                    }
                }
                else {
                    ICLog.i("UPDATABLE", "assertion failed: updatable scope is not an array, loading failed");
                    n2 = n;
                }
                o = new StringBuilder();
                ((StringBuilder)o).append("successfully loaded updatables: ");
                ((StringBuilder)o).append(n2);
                ICLog.d("UPDATABLE", ((StringBuilder)o).toString());
            }
            
            @Override
            public ScriptableObject save() {
                final ArrayList<ScriptableObject> list = new ArrayList<ScriptableObject>();
                for (final ScriptableObject scriptableObject : Updatable.getForServer().updatableObjects) {
                    if (ObjectSaverRegistry.getSaverFor(scriptableObject) != null) {
                        list.add(scriptableObject);
                    }
                }
                for (final ScriptableObject scriptableObject2 : Updatable.getForServer().disabledDueToError) {
                    if (ObjectSaverRegistry.getSaverFor(scriptableObject2) != null && !shouldBeRemoved((Scriptable)scriptableObject2)) {
                        list.add(scriptableObject2);
                    }
                }
                return (ScriptableObject)new NativeArray(list.toArray());
            }
        });
    }
    
    private Updatable(final boolean isMultithreadingAllowed) {
        this.updatableObjects = new ArrayList<ScriptableObject>();
        this.disabledDueToError = new ArrayList<ScriptableObject>();
        this.currentContext = null;
        this.currentArrayPosition = 0;
        this.postedRemovedUpdatables = new ArrayList<ScriptableObject>();
        this.statCurrentCalls = 0;
        this.isMultithreadingAllowed = isMultithreadingAllowed;
    }
    
    public static void cleanUpAll() {
        getForClient().cleanUp();
        getForServer().cleanUp();
    }
    
    private boolean executeCurrentToNext() {
        if (this.updatableObjects.size() == 0) {
            return true;
        }
        this.currentArrayPosition %= this.updatableObjects.size();
        final boolean executeUpdate = this.executeUpdate(this.updatableObjects.get(this.currentArrayPosition));
        ++this.currentArrayPosition;
        if (executeUpdate) {
            ++this.statCurrentCalls;
        }
        return executeUpdate;
    }
    
    private boolean executeUpdate(final ScriptableObject scriptableObject) {
        if (ScriptableObjectHelper.getBooleanProperty(scriptableObject, "noupdate", false)) {
            return false;
        }
        final TickExecutor instance = TickExecutor.getInstance();
        if (this.isMultithreadingAllowed && instance.isAvailable()) {
            TickExecutor.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    Updatable.this.executeUpdateWithContext(Compiler.assureContextForCurrentThread(), scriptableObject);
                }
            });
        }
        else {
            this.executeUpdateWithContext(this.currentContext, scriptableObject);
        }
        return true;
    }
    
    private void executeUpdateWithContext(final Context context, final ScriptableObject scriptableObject) {
        final Object value = scriptableObject.get("update", (Scriptable)scriptableObject);
        try {
            final Function function = (Function)value;
            function.call(context, function.getParentScope(), (Scriptable)scriptableObject, Updatable.EMPTY_ARGS);
            if (shouldBeRemoved((Scriptable)scriptableObject)) {
                this.postedRemovedUpdatables.add(scriptableObject);
            }
        }
        catch (Throwable t) {
            this.disabledDueToError.add(scriptableObject);
            this.postedRemovedUpdatables.add(scriptableObject);
            final Object value2 = scriptableObject.get("_handle_error", (Scriptable)scriptableObject);
            if (value2 instanceof Function) {
                try {
                    final Function function2 = (Function)value2;
                    function2.call(context, function2.getParentScope(), (Scriptable)scriptableObject, new Object[] { t });
                }
                catch (Throwable t2) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Error occurred in error handler for ");
                    sb.append(updatableToString(context, (Scriptable)scriptableObject));
                    sb.append(" hash=");
                    sb.append(scriptableObject.hashCode());
                    UserDialog.insignificantError(sb.toString(), t2, true);
                }
                return;
            }
            reportError(t, updatableToString(context, (Scriptable)scriptableObject));
        }
    }
    
    public static Updatable getForClient() {
        return Updatable.clientInstance;
    }
    
    public static Updatable getForServer() {
        return Updatable.serverInstance;
    }
    
    private void onCountBasedTick() {
        final int maxUpdateCallsPerTick = Updatable.maxUpdateCallsPerTick;
        int n = 0;
        int n3;
        for (int n2 = 0; n2 < maxUpdateCallsPerTick && n2 < this.updatableObjects.size(); n2 = n3) {
            if (n >= this.updatableObjects.size()) {
                return;
            }
            n3 = n2;
            if (this.executeCurrentToNext()) {
                n3 = n2 + 1;
            }
            ++n;
        }
    }
    
    private void onTimeBasedTick() {
        final int size = this.updatableObjects.size();
        final long currentTimeMillis = System.currentTimeMillis();
        for (int i = 0; i < size; ++i) {
            this.executeCurrentToNext();
            if (System.currentTimeMillis() - currentTimeMillis > Updatable.maxUpdateTimePerTick) {
                return;
            }
        }
    }
    
    private void removePosted() {
        final Iterator<ScriptableObject> iterator = this.postedRemovedUpdatables.iterator();
        while (iterator.hasNext()) {
            this.updatableObjects.remove(iterator.next());
        }
        this.postedRemovedUpdatables.clear();
    }
    
    public static void reportError(final Throwable t, final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Updatable ");
        sb.append(s);
        sb.append(" was disabled due to error, corresponding object will be disabled. To re-enable it re-enter the world.");
        UserDialog.dialog("UPDATABLE ERROR", sb.toString(), t, true);
    }
    
    public static void setPreferences(final int currentMode, final int n) {
        switch (currentMode) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("invalid updatable engine mode: ");
                sb.append(currentMode);
                throw new IllegalArgumentException(sb.toString());
            }
            case 1: {
                Updatable.maxUpdateTimePerTick = n;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("updatable engine uses time-based mode, maxTime=");
                sb2.append(n);
                ICLog.d("THREADING", sb2.toString());
                break;
            }
            case 0: {
                Updatable.maxUpdateCallsPerTick = n;
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("updatable engine uses count-based mode, maxCount=");
                sb3.append(n);
                ICLog.d("THREADING", sb3.toString());
                break;
            }
        }
        Updatable.currentMode = currentMode;
    }
    
    private static boolean shouldBeRemoved(final Scriptable scriptable) {
        final Object value = scriptable.get("remove", scriptable);
        return value instanceof Boolean && (boolean)value;
    }
    
    private static String updatableToString(final Context context, final Scriptable scriptable) {
        final Object value = scriptable.get("_to_string", scriptable);
        if (value instanceof Function) {
            try {
                final Function function = (Function)value;
                final StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(function.call(context, function.getParentScope(), scriptable, Updatable.EMPTY_ARGS));
                return sb.toString();
            }
            catch (Throwable t) {}
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("");
        sb2.append(scriptable);
        return sb2.toString();
    }
    
    public void addUpdatable(final ScriptableObject scriptableObject) {
        final Object value = scriptableObject.get("update", (Scriptable)scriptableObject);
        if (value instanceof Function) {
            this.updatableObjects.add(scriptableObject);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("cannot add updatable object: <obj>.update must be a function, not ");
        Class<?> class1;
        if (value != null) {
            class1 = value.getClass();
        }
        else {
            class1 = null;
        }
        sb.append(class1);
        sb.append(" ");
        sb.append(value);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public void cleanUp() {
        this.updatableObjects.clear();
        this.disabledDueToError.clear();
        this.postedRemovedUpdatables.clear();
        this.currentArrayPosition = 0;
        this.currentContext = null;
    }
    
    public List<ScriptableObject> getAllUpdatableObjects() {
        return this.updatableObjects;
    }
    
    public void onPostTick() {
        this.removePosted();
    }
    
    public void onTick() {
        if (this.currentContext == null) {
            this.currentContext = Compiler.assureContextForCurrentThread();
        }
        this.statCurrentCalls = 0;
        if (Updatable.currentMode != 0 && (!this.isMultithreadingAllowed || !TickExecutor.getInstance().isAvailable())) {
            if (Updatable.currentMode == 1) {
                this.onTimeBasedTick();
            }
        }
        else {
            this.onCountBasedTick();
        }
    }
    
    public void onTickSingleThreaded() {
        this.onTick();
        this.onPostTick();
    }
}
