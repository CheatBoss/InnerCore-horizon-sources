package org.mozilla.javascript;

public interface RefCallable extends Callable
{
    Ref refCall(final Context p0, final Scriptable p1, final Object[] p2);
}
