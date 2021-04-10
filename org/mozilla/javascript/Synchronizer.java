package org.mozilla.javascript;

public class Synchronizer extends Delegator
{
    private Object syncObject;
    
    public Synchronizer(final Scriptable scriptable) {
        super(scriptable);
    }
    
    public Synchronizer(final Scriptable scriptable, final Object syncObject) {
        super(scriptable);
        this.syncObject = syncObject;
    }
    
    @Override
    public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        Object o;
        if (this.syncObject != null) {
            o = this.syncObject;
        }
        else {
            o = scriptable2;
        }
        if (o instanceof Wrapper) {
            o = ((Wrapper)o).unwrap();
        }
        synchronized (o) {
            return ((Function)this.obj).call(context, scriptable, scriptable2, array);
        }
    }
}
