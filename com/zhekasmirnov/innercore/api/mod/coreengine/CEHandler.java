package com.zhekasmirnov.innercore.api.mod.coreengine;

import com.zhekasmirnov.innercore.mod.executable.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.log.*;

public class CEHandler
{
    public static final Object CALL_FAILED;
    public static final Object GET_FAILED;
    public final Executable coreEngine;
    private boolean isLoaded;
    private ScriptableObject scope;
    
    static {
        CALL_FAILED = new Object();
        GET_FAILED = new Object();
    }
    
    public CEHandler(final Executable coreEngine) {
        this.scope = null;
        this.isLoaded = false;
        this.coreEngine = coreEngine;
    }
    
    public Object callMethod(final String s, final Object... array) {
        if (!this.isLoaded || this.scope == null) {
            return CEHandler.CALL_FAILED;
        }
        final Object value = this.scope.get((Object)s);
        if (value instanceof Function) {
            return ((Function)value).call(Compiler.assureContextForCurrentThread(), (Scriptable)this.scope, (Scriptable)this.scope, array);
        }
        return CEHandler.CALL_FAILED;
    }
    
    public Object getValue(final String s) {
        if (this.isLoaded && this.scope != null) {
            return this.scope.get((Object)s);
        }
        return CEHandler.CALL_FAILED;
    }
    
    public void injectCoreAPI(final ScriptableObject scriptableObject) {
        if (this.callMethod("injectCoreAPI", scriptableObject) == CEHandler.CALL_FAILED) {
            ICLog.e("CORE-ENGINE", "failed to inject CoreAPI: method call failed", new RuntimeException());
        }
    }
    
    public void load() {
        if (!this.isLoaded) {
            this.isLoaded = true;
            this.scope = this.coreEngine.getScope();
            this.coreEngine.run();
        }
    }
    
    public Object requireGlobal(final String s) {
        if (this.isLoaded && this.scope != null) {
            return this.coreEngine.evaluateStringInScope(s);
        }
        return CEHandler.CALL_FAILED;
    }
}
