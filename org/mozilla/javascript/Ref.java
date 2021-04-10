package org.mozilla.javascript;

import java.io.*;

public abstract class Ref implements Serializable
{
    static final long serialVersionUID = 4044540354730911424L;
    
    public boolean delete(final Context context) {
        return false;
    }
    
    public abstract Object get(final Context p0);
    
    public boolean has(final Context context) {
        return true;
    }
    
    @Deprecated
    public abstract Object set(final Context p0, final Object p1);
    
    public Object set(final Context context, final Scriptable scriptable, final Object o) {
        return this.set(context, o);
    }
}
