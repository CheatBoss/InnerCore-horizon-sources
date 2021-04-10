package org.mozilla.javascript.tools.shell;

import org.mozilla.javascript.*;
import java.io.*;

class PipeThread extends Thread
{
    private InputStream from;
    private boolean fromProcess;
    private OutputStream to;
    
    PipeThread(final boolean fromProcess, final InputStream from, final OutputStream to) {
        this.setDaemon(true);
        this.fromProcess = fromProcess;
        this.from = from;
        this.to = to;
    }
    
    @Override
    public void run() {
        try {
            Global.pipe(this.fromProcess, this.from, this.to);
        }
        catch (IOException ex) {
            throw Context.throwAsScriptRuntimeEx(ex);
        }
    }
}
