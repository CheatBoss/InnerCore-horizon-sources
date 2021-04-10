package org.mozilla.javascript;

public interface ConstProperties
{
    void defineConst(final String p0, final Scriptable p1);
    
    boolean isConst(final String p0);
    
    void putConst(final String p0, final Scriptable p1, final Object p2);
}
