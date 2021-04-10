package org.mozilla.javascript.tools.shell;

import org.mozilla.javascript.*;

class Runner implements Runnable, ContextAction
{
    private Object[] args;
    private Function f;
    ContextFactory factory;
    private Script s;
    private Scriptable scope;
    
    Runner(final Scriptable scope, final Function f, final Object[] args) {
        this.scope = scope;
        this.f = f;
        this.args = args;
    }
    
    Runner(final Scriptable scope, final Script s) {
        this.scope = scope;
        this.s = s;
    }
    
    @Override
    public Object run(final Context context) {
        if (this.f != null) {
            return this.f.call(context, this.scope, this.scope, this.args);
        }
        return this.s.exec(context, this.scope);
    }
    
    @Override
    public void run() {
        this.factory.call(this);
    }
}
