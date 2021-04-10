package org.mozilla.javascript.tools.shell;

class ConsoleWrite implements Runnable
{
    private String str;
    private ConsoleTextArea textArea;
    
    public ConsoleWrite(final ConsoleTextArea textArea, final String str) {
        this.textArea = textArea;
        this.str = str;
    }
    
    @Override
    public void run() {
        this.textArea.write(this.str);
    }
}
