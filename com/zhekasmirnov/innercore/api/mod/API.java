package com.zhekasmirnov.innercore.api.mod;

import java.lang.reflect.*;
import com.zhekasmirnov.innercore.api.log.*;
import com.zhekasmirnov.innercore.api.annotations.*;
import org.mozilla.javascript.annotations.*;
import java.util.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.adaptedscript.*;
import com.zhekasmirnov.innercore.api.mod.coreengine.*;
import com.zhekasmirnov.innercore.api.mod.preloader.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import com.zhekasmirnov.innercore.api.runtime.*;
import com.zhekasmirnov.innercore.api.mod.util.*;
import com.zhekasmirnov.innercore.api.constants.*;
import com.zhekasmirnov.innercore.mod.executable.*;
import com.zhekasmirnov.innercore.mod.build.*;

public abstract class API extends ScriptableObject
{
    private static ArrayList<API> APIInstanceList;
    public static final String LOGGER_TAG = "INNERCORE-API";
    private static ScriptableObject currentScopeToInject;
    protected ArrayList<Executable> executables;
    protected boolean isLoaded;
    
    static {
        API.currentScopeToInject = null;
        API.APIInstanceList = new ArrayList<API>();
    }
    
    public API() {
        this.isLoaded = false;
        this.executables = new ArrayList<Executable>();
    }
    
    public static ArrayList<Function> collectAllCallbacks(final String s) {
        final ArrayList<Function> list = new ArrayList<Function>();
        final Iterator<API> iterator = API.APIInstanceList.iterator();
        while (iterator.hasNext()) {
            final Iterator<Executable> iterator2 = iterator.next().executables.iterator();
            while (iterator2.hasNext()) {
                final Function function = iterator2.next().getFunction(s);
                if (function != null) {
                    list.add(function);
                }
            }
        }
        return list;
    }
    
    private String createDumpString(final Class clazz, final String s, String s2) {
        final Method[] methods = clazz.getMethods();
        final int length = methods.length;
        final int n = 0;
        int i = 0;
        String s3 = s2;
        while (i < length) {
            final Method method = methods[i];
            s2 = s3;
            if (method.getAnnotation(JSStaticFunction.class) != null) {
                final StringBuilder sb = new StringBuilder();
                sb.append(s3);
                sb.append(s);
                sb.append(this.dumpMethod(method));
                s2 = sb.toString();
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(s2);
                sb2.append("\n");
                final String string = sb2.toString();
                final Class<?> returnType = method.getReturnType();
                s2 = string;
                if (returnType != null) {
                    s2 = string;
                    if (!returnType.isPrimitive()) {
                        final Method[] methods2 = returnType.getMethods();
                        final int length2 = methods2.length;
                        s2 = string;
                        String string2;
                        for (int j = 0; j < length2; ++j, s2 = string2) {
                            final Method method2 = methods2[j];
                            string2 = s2;
                            if ((method2.getModifiers() & 0x8) == 0x0) {
                                string2 = s2;
                                if (!method2.getName().equals("getClassName")) {
                                    string2 = s2;
                                    if (method2.getDeclaringClass().getPackage().toString().contains("zhekasmirnov")) {
                                        final StringBuilder sb3 = new StringBuilder();
                                        sb3.append(s2);
                                        sb3.append("\t");
                                        sb3.append(this.dumpMethod(method2));
                                        sb3.append("\n");
                                        string2 = sb3.toString();
                                    }
                                }
                            }
                        }
                    }
                }
            }
            ++i;
            s3 = s2;
        }
        final StringBuilder sb4 = new StringBuilder();
        sb4.append(s3);
        sb4.append("\n");
        s2 = sb4.toString();
        final Class[] classes = clazz.getClasses();
        final int length3 = classes.length;
        String s4 = s2;
        for (int k = n; k < length3; ++k, s4 = s2) {
            final Class clazz2 = classes[k];
            s2 = s4;
            if (clazz2.getAnnotation(APIStaticModule.class) != null) {
                final StringBuilder sb5 = new StringBuilder();
                sb5.append(s);
                sb5.append(clazz2.getSimpleName());
                sb5.append(".");
                s2 = this.createDumpString(clazz2, sb5.toString(), s4);
            }
        }
        return s4;
    }
    
    public static void debugLookUpClass(final Class clazz) {
        final StringBuilder sb = new StringBuilder();
        sb.append("starting at ");
        sb.append(clazz);
        ICLog.d("LOOKUP", sb.toString());
        debugLookUpClass(clazz, "");
    }
    
    private static void debugLookUpClass(final Class clazz, final String s) {
        if (clazz == null) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("  ");
        final String string = sb.toString();
        final HashMap<Class, ArrayList<String>> allClassMethods = getAllClassMethods(clazz, null);
        for (final Class clazz2 : allClassMethods.keySet()) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append("methods in class ");
            sb2.append(clazz2.getSimpleName());
            sb2.append(": ");
            ICLog.d("LOOKUP", sb2.toString());
            for (final String s2 : allClassMethods.get(clazz2)) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(string);
                sb3.append(s2);
                sb3.append("(...)");
                ICLog.d("LOOKUP", sb3.toString());
            }
        }
        for (final Class clazz3 : getSubclasses(clazz)) {
            if (clazz3 != null && clazz3.getAnnotation(APIIgnore.class) == null) {
                if (clazz3.getAnnotation(APIStaticModule.class) != null) {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append(s);
                    sb4.append("looking up module ");
                    sb4.append(clazz3.getSimpleName());
                    sb4.append(":");
                    ICLog.d("LOOKUP", sb4.toString());
                }
                else {
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append(s);
                    sb5.append("looking up constructor class ");
                    sb5.append(clazz3.getSimpleName());
                    sb5.append(":");
                    ICLog.d("LOOKUP", sb5.toString());
                }
                debugLookUpClass(clazz3, string);
            }
        }
    }
    
    private String dumpMethod(final Method method) {
        final StringBuilder sb = new StringBuilder();
        sb.append(method.getName());
        sb.append("(");
        final String string = sb.toString();
        final Class<?>[] parameterTypes = method.getParameterTypes();
        int i = 0;
        String string2 = string;
        while (i < parameterTypes.length) {
            String string3 = string2;
            if (i > 0) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(string2);
                sb2.append(", ");
                string3 = sb2.toString();
            }
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(string3);
            sb3.append(parameterTypes[i].getSimpleName());
            string2 = sb3.toString();
            ++i;
        }
        final StringBuilder sb4 = new StringBuilder();
        sb4.append(string2);
        sb4.append(")");
        return sb4.toString();
    }
    
    private static HashMap<Class, ArrayList<String>> getAllClassMethods(Class superclass, final List<String> list) {
        final HashMap hashMap = new HashMap<Class, ArrayList<String>>();
        while (superclass != null && superclass.getName().contains("com.zhekasmirnov.innercore")) {
            final Method[] methods = superclass.getMethods();
            final ArrayList<String> list2 = new ArrayList<String>();
            if (!hashMap.containsKey(superclass)) {
                hashMap.put(superclass, list2);
            }
            for (int length = methods.length, i = 0; i < length; ++i) {
                final Method method = methods[i];
                if (list == null || list.contains(method.getName())) {
                    if ((method.getAnnotation(JSFunction.class) != null || method.getAnnotation(JSStaticFunction.class) != null) && method.getDeclaringClass() == superclass) {
                        list2.add(method.getName());
                    }
                }
            }
            superclass = superclass.getSuperclass();
        }
        return (HashMap<Class, ArrayList<String>>)hashMap;
    }
    
    public static API getInstanceByName(final String s) {
        for (int i = 0; i < API.APIInstanceList.size(); ++i) {
            final API api = API.APIInstanceList.get(i);
            if (api.getName().equals(s)) {
                return api;
            }
        }
        return null;
    }
    
    private static Collection<Class> getSubclasses(Class superclass) {
        final HashSet<Class> set = new HashSet<Class>();
        while (superclass != null && superclass.getName().contains("com.zhekasmirnov.innercore")) {
            final Class[] classes = superclass.getClasses();
            for (int length = classes.length, i = 0; i < length; ++i) {
                set.add(classes[i]);
            }
            superclass = superclass.getSuperclass();
        }
        return set;
    }
    
    protected static void injectIntoScope(Class iterator, final ScriptableObject scriptableObject, final List<String> list) {
        ((Class)iterator).getMethods();
        final HashMap<Class, ArrayList<String>> allClassMethods = getAllClassMethods((Class)iterator, list);
        for (final Class clazz : allClassMethods.keySet()) {
            final ArrayList<String> list2 = allClassMethods.get(clazz);
            final String[] array = new String[list2.size()];
            list2.toArray(array);
            scriptableObject.defineFunctionProperties(array, (Class)clazz, 2);
        }
        iterator = getSubclasses((Class)iterator).iterator();
        while (iterator.hasNext()) {
            final Class clazz2 = iterator.next();
            if (list != null && !list.contains(clazz2.getSimpleName())) {
                continue;
            }
            if (clazz2.getAnnotation(APIStaticModule.class) != null) {
                final ScriptableObject scriptableObject2 = new ScriptableObject() {
                    public String getClassName() {
                        return clazz2.getSimpleName();
                    }
                };
                scriptableObject.defineProperty(clazz2.getSimpleName(), (Object)scriptableObject2, 2);
                injectIntoScope(clazz2, scriptableObject2, null);
            }
            else {
                if (clazz2.getAnnotation(APIIgnore.class) != null) {
                    continue;
                }
                try {
                    scriptableObject.defineProperty(clazz2.getSimpleName(), (Object)new NativeJavaClass((Scriptable)API.currentScopeToInject, (Class)clazz2, false), 2);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    public static void invokeCallback(final String s, final Object... array) {
        final Iterator<API> iterator = API.APIInstanceList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onCallback(s, array);
        }
    }
    
    public static void loadAllAPIs() {
        registerInstance(new AdaptedScriptAPI());
        registerInstance(new PreferencesWindowAPI());
        registerInstance(new CoreEngineAPI());
        registerInstance(new PreloaderAPI());
    }
    
    protected static void registerInstance(final API api) {
        if (!API.APIInstanceList.contains(api)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Register Mod API: ");
            sb.append(api.getName());
            Logger.debug("INNERCORE-API", sb.toString());
            API.APIInstanceList.add(api);
        }
    }
    
    protected void addExecutableCallback(final Executable executable, final String s, final String s2) {
        final Function function = executable.getFunction(s2);
        if (function != null) {
            Callback.addCallback(s, function, 0);
        }
    }
    
    @JSStaticFunction
    public void createDump(final Object o) {
        Class clazz = null;
        if (o instanceof Class) {
            clazz = (Class)o;
        }
        Class<? extends API> class1;
        if ((class1 = (Class<? extends API>)clazz) == null) {
            class1 = this.getClass();
        }
        DebugAPI.dialog(this.createDumpString(class1, "", ""));
    }
    
    public String getClassName() {
        return this.getName();
    }
    
    @JSStaticFunction
    public int getCurrentAPILevel() {
        return this.getLevel();
    }
    
    @JSStaticFunction
    public String getCurrentAPIName() {
        return this.getName();
    }
    
    public abstract int getLevel();
    
    public abstract String getName();
    
    public void injectIntoScope(final ScriptableObject scriptableObject) {
        this.injectIntoScope(scriptableObject, null);
    }
    
    protected void injectIntoScope(final ScriptableObject currentScopeToInject, final List<String> list) {
        API.currentScopeToInject = currentScopeToInject;
        injectIntoScope(this.getClass(), currentScopeToInject, list);
        ConstantRegistry.injectConstants(currentScopeToInject);
    }
    
    public void invokeExecutableCallback(final String s, final Object[] array) {
        Compiler.assureContextForCurrentThread();
        final Iterator<Executable> iterator = this.executables.iterator();
        while (iterator.hasNext()) {
            iterator.next().callFunction(s, array);
        }
    }
    
    public API newInstance() {
        try {
            return (API)this.getClass().getConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public abstract void onCallback(final String p0, final Object[] p1);
    
    public abstract void onLoaded();
    
    public abstract void onModLoaded(final Mod p0);
    
    public void prepareExecutable(final Executable executable) {
        if (!this.isLoaded) {
            final StringBuilder sb = new StringBuilder();
            sb.append("loading API: ");
            sb.append(this.getName());
            ICLog.d("INNERCORE-API", sb.toString());
            this.isLoaded = true;
            this.onLoaded();
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("adding executable: api=");
        sb2.append(this.getName());
        sb2.append(" exec=");
        sb2.append(executable.name);
        ICLog.d("INNERCORE-API", sb2.toString());
        this.executables.add(executable);
        this.setupCallbacks(executable);
    }
    
    public abstract void setupCallbacks(final Executable p0);
}
