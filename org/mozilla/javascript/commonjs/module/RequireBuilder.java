package org.mozilla.javascript.commonjs.module;

import java.io.*;
import org.mozilla.javascript.*;

public class RequireBuilder implements Serializable
{
    private static final long serialVersionUID = 1L;
    private ModuleScriptProvider moduleScriptProvider;
    private Script postExec;
    private Script preExec;
    private boolean sandboxed;
    
    public RequireBuilder() {
        this.sandboxed = true;
    }
    
    public Require createRequire(final Context context, final Scriptable scriptable) {
        return new Require(context, scriptable, this.moduleScriptProvider, this.preExec, this.postExec, this.sandboxed);
    }
    
    public RequireBuilder setModuleScriptProvider(final ModuleScriptProvider moduleScriptProvider) {
        this.moduleScriptProvider = moduleScriptProvider;
        return this;
    }
    
    public RequireBuilder setPostExec(final Script postExec) {
        this.postExec = postExec;
        return this;
    }
    
    public RequireBuilder setPreExec(final Script preExec) {
        this.preExec = preExec;
        return this;
    }
    
    public RequireBuilder setSandboxed(final boolean sandboxed) {
        this.sandboxed = sandboxed;
        return this;
    }
}
