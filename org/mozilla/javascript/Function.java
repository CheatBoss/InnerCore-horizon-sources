package org.mozilla.javascript;

public interface Function extends Scriptable, Callable
{
    Object call(final Context p0, final Scriptable p1, final Scriptable p2, final Object[] p3);
    
    Scriptable construct(final Context p0, final Scriptable p1, final Object[] p2);
}
