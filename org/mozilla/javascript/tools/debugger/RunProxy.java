package org.mozilla.javascript.tools.debugger;

import java.awt.*;

class RunProxy implements Runnable
{
    static final int ENTER_INTERRUPT = 4;
    static final int LOAD_FILE = 2;
    static final int OPEN_FILE = 1;
    static final int UPDATE_SOURCE_TEXT = 3;
    String alertMessage;
    private SwingGui debugGui;
    String fileName;
    Dim.StackFrame lastFrame;
    Dim.SourceInfo sourceInfo;
    String text;
    String threadTitle;
    private int type;
    
    public RunProxy(final SwingGui debugGui, final int type) {
        this.debugGui = debugGui;
        this.type = type;
    }
    
    @Override
    public void run() {
        switch (this.type) {
            default: {
                throw new IllegalArgumentException(String.valueOf(this.type));
            }
            case 4: {
                this.debugGui.enterInterruptImpl(this.lastFrame, this.threadTitle, this.alertMessage);
            }
            case 3: {
                final String url = this.sourceInfo.url();
                if (!this.debugGui.updateFileWindow(this.sourceInfo) && !url.equals("<stdin>")) {
                    this.debugGui.createFileWindow(this.sourceInfo, -1);
                }
            }
            case 2: {
                try {
                    this.debugGui.dim.evalScript(this.fileName, this.text);
                }
                catch (RuntimeException ex) {
                    final SwingGui debugGui = this.debugGui;
                    final String message = ex.getMessage();
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Run error for ");
                    sb.append(this.fileName);
                    MessageDialogWrapper.showMessageDialog(debugGui, message, sb.toString(), 0);
                }
            }
            case 1: {
                try {
                    this.debugGui.dim.compileScript(this.fileName, this.text);
                    return;
                }
                catch (RuntimeException ex2) {
                    final SwingGui debugGui2 = this.debugGui;
                    final String message2 = ex2.getMessage();
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Error Compiling ");
                    sb2.append(this.fileName);
                    MessageDialogWrapper.showMessageDialog(debugGui2, message2, sb2.toString(), 0);
                    return;
                }
                break;
            }
        }
    }
}
