package org.mozilla.javascript.debug;

import org.mozilla.javascript.*;

public interface Debugger
{
    DebugFrame getFrame(final Context p0, final DebuggableScript p1);
    
    void handleCompilationDone(final Context p0, final DebuggableScript p1, final String p2);
}
