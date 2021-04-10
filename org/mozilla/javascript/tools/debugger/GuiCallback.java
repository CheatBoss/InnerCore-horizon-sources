package org.mozilla.javascript.tools.debugger;

public interface GuiCallback
{
    void dispatchNextGuiEvent() throws InterruptedException;
    
    void enterInterrupt(final Dim.StackFrame p0, final String p1, final String p2);
    
    boolean isGuiEventThread();
    
    void updateSourceText(final Dim.SourceInfo p0);
}
