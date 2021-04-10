package org.mozilla.javascript.debug;

import org.mozilla.javascript.*;

public interface DebugFrame
{
    void onDebuggerStatement(final Context p0);
    
    void onEnter(final Context p0, final Scriptable p1, final Scriptable p2, final Object[] p3);
    
    void onExceptionThrown(final Context p0, final Throwable p1);
    
    void onExit(final Context p0, final boolean p1, final Object p2);
    
    void onLineChange(final Context p0, final int p1);
}
