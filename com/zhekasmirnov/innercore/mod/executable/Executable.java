package com.zhekasmirnov.innercore.mod.executable;

import java.util.*;
import com.zhekasmirnov.innercore.mod.build.*;
import com.zhekasmirnov.innercore.api.mod.*;
import com.zhekasmirnov.innercore.api.unlimited.*;
import com.zhekasmirnov.innercore.api.mod.util.*;
import com.zhekasmirnov.innercore.mod.executable.library.*;
import com.zhekasmirnov.innercore.api.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.innercore.modpack.*;
import com.zhekasmirnov.innercore.api.log.*;

public class Executable implements Runnable
{
    private static final HashMap<String, Scriptable> javaWrapCache;
    public API apiInstance;
    public CompilerConfig compilerConfig;
    private boolean isApiAdded;
    public boolean isLoadedFromDex;
    protected boolean isRunning;
    protected Throwable lastRunException;
    public String name;
    public Context parentContext;
    private Mod parentMod;
    public Script script;
    public ScriptableObject scriptScope;
    
    static {
        javaWrapCache = new HashMap<String, Scriptable>();
    }
    
    public Executable(final Context parentContext, final Script script, final ScriptableObject scriptScope, final CompilerConfig compilerConfig, final API apiInstance) {
        this.parentMod = null;
        this.isLoadedFromDex = false;
        this.isRunning = false;
        this.lastRunException = null;
        this.isApiAdded = false;
        this.parentContext = parentContext;
        this.script = script;
        this.scriptScope = scriptScope;
        this.apiInstance = apiInstance;
        this.compilerConfig = compilerConfig;
        this.name = compilerConfig.getName();
    }
    
    public Executable(final Context parentContext, final ScriptableObject scriptScope, final CompilerConfig compilerConfig, final API apiInstance) {
        this.parentMod = null;
        this.isLoadedFromDex = false;
        this.isRunning = false;
        this.lastRunException = null;
        this.isApiAdded = false;
        this.parentContext = parentContext;
        this.script = null;
        this.scriptScope = scriptScope;
        this.apiInstance = apiInstance;
        this.compilerConfig = compilerConfig;
        this.name = compilerConfig.getName();
    }
    
    public void addToScope(final ScriptableObject scriptableObject) {
        if (scriptableObject == null) {
            return;
        }
        final Object[] allIds = scriptableObject.getAllIds();
        for (int i = 0; i < allIds.length; ++i) {
            final Object o = allIds[i];
            if (o instanceof String) {
                this.scriptScope.put((String)o, (Scriptable)this.scriptScope, scriptableObject.get(o));
            }
        }
    }
    
    public Object callFunction(final String s, final Object[] array) {
        final Object property = ScriptableObjectHelper.getProperty(this.scriptScope, s, null);
        if (property != null && property instanceof Function) {
            return ((Function)property).call(this.parentContext, (Scriptable)this.scriptScope, (Scriptable)this.scriptScope, array);
        }
        return null;
    }
    
    public Object evaluateStringInScope(final String s) {
        return this.parentContext.evaluateString((Scriptable)this.scriptScope, s, this.name, 0, (Object)null);
    }
    
    public Function getFunction(final String s) {
        final Object property = ScriptableObjectHelper.getProperty(this.scriptScope, s, null);
        if (property != null && property instanceof Function) {
            return (Function)property;
        }
        return null;
    }
    
    public Throwable getLastRunException() {
        return this.lastRunException;
    }
    
    public Mod getParentMod() {
        return this.parentMod;
    }
    
    public ScriptableObject getScope() {
        return this.scriptScope;
    }
    
    protected void injectAPI() {
        if (!this.isApiAdded) {
            this.isApiAdded = true;
            this.injectStaticAPIs();
            if (this.apiInstance != null) {
                this.apiInstance.prepareExecutable(this);
            }
        }
    }
    
    public void injectStaticAPIs() {
        IDRegistry.injectAPI(this.scriptScope);
        final ScriptableFunctionImpl scriptableFunctionImpl = new ScriptableFunctionImpl() {
            public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
                final String s = (String)array[0];
                String s2;
                if (array.length > 1) {
                    s2 = (String)array[1];
                }
                else {
                    s2 = "*";
                }
                final LibraryDependency libraryDependency = new LibraryDependency(s);
                libraryDependency.setParentMod(Executable.this.getParentMod());
                LibraryRegistry.importLibrary((Scriptable)Executable.this.scriptScope, libraryDependency, s2);
                return null;
            }
        };
        this.scriptScope.put("importLib", (Scriptable)this.scriptScope, (Object)scriptableFunctionImpl);
        this.scriptScope.put("IMPORT", (Scriptable)this.scriptScope, (Object)scriptableFunctionImpl);
        this.scriptScope.put("IMPORT_NATIVE", (Scriptable)this.scriptScope, (Object)new ScriptableFunctionImpl() {
            public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
                return NativeJavaScript.injectNativeModule((String)array[0], Executable.this.scriptScope);
            }
        });
        this.scriptScope.put("WRAP_NATIVE", (Scriptable)this.scriptScope, (Object)new ScriptableFunctionImpl() {
            public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
                return NativeJavaScript.wrapNativeModule((String)array[0]);
            }
        });
        this.scriptScope.put("WRAP_JAVA", (Scriptable)this.scriptScope, (Object)new ScriptableFunctionImpl() {
            public Object call(final Context context, final Scriptable scriptable, Scriptable scriptable2, final Object[] array) {
                scriptable2 = (Scriptable)array[0];
                if (((String)scriptable2).contains("com.zhekasmirnov.horizon.launcher.ads")) {
                    throw new IllegalArgumentException("Unauthorized");
                }
                final Scriptable scriptable3 = Executable.javaWrapCache.get(scriptable2);
                if (scriptable3 != null) {
                    return scriptable3;
                }
                Object o;
                try {
                    o = new NativeJavaClass(scriptable, (Class)Class.forName((String)scriptable2), false);
                }
                catch (ClassNotFoundException ex) {
                    o = new NativeJavaPackage((String)scriptable2);
                }
                Executable.javaWrapCache.put(scriptable2, o);
                return o;
            }
        });
        this.scriptScope.put("__packdir__", (Scriptable)this.scriptScope, (Object)FileTools.DIR_PACK);
        this.scriptScope.put("__modpack__", (Scriptable)this.scriptScope, Context.javaToJS((Object)ModPackContext.getInstance().assureJsAdapter(), (Scriptable)this.scriptScope));
    }
    
    public void injectValueIntoScope(final String s, final Object o) {
        this.scriptScope.put(s, (Scriptable)this.scriptScope, o);
    }
    
    public boolean isRunning() {
        return this.isRunning;
    }
    
    public void reset() {
        this.isRunning = false;
    }
    
    @Override
    public void run() {
        this.runForResult();
    }
    
    public Object runForResult() {
        if (this.isRunning) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Could not run executable '");
            sb.append(this.name);
            sb.append("', it is already running");
            throw new RuntimeException(sb.toString());
        }
        this.isRunning = true;
        try {
            this.injectAPI();
            try {
                return this.runScript();
            }
            catch (Throwable lastRunException) {
                this.lastRunException = lastRunException;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("failed to run executable '");
                sb2.append(this.name);
                sb2.append("', some errors occurred:");
                ICLog.e("INNERCORE-EXEC", sb2.toString(), lastRunException);
                return null;
            }
        }
        catch (Throwable lastRunException2) {
            this.lastRunException = lastRunException2;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("failed to inject API to executable '");
            sb3.append(this.name);
            sb3.append("', some errors occurred:");
            ICLog.e("INNERCORE-EXEC", sb3.toString(), lastRunException2);
            return null;
        }
    }
    
    protected Object runScript() {
        return this.script.exec(Compiler.assureContextForCurrentThread(), (Scriptable)this.scriptScope);
    }
    
    public void setParentMod(final Mod parentMod) {
        this.parentMod = parentMod;
    }
}
