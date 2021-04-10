package org.mozilla.javascript.tools.shell;

import java.io.*;
import javax.swing.*;

class ConsoleWriter extends OutputStream
{
    private StringBuffer buffer;
    private ConsoleTextArea textArea;
    
    public ConsoleWriter(final ConsoleTextArea textArea) {
        this.textArea = textArea;
        this.buffer = new StringBuffer();
    }
    
    private void flushBuffer() {
        final String string = this.buffer.toString();
        this.buffer.setLength(0);
        SwingUtilities.invokeLater(new ConsoleWrite(this.textArea, string));
    }
    
    @Override
    public void close() {
        this.flush();
    }
    
    @Override
    public void flush() {
        synchronized (this) {
            if (this.buffer.length() > 0) {
                this.flushBuffer();
            }
        }
    }
    
    @Override
    public void write(final int n) {
        synchronized (this) {
            this.buffer.append((char)n);
            if (n == 10) {
                this.flushBuffer();
            }
        }
    }
    
    public void write(final char[] array, int i, final int n) {
        // monitorenter(this)
        while (i < n) {
            try {
                this.buffer.append(array[i]);
                if (array[i] == '\n') {
                    this.flushBuffer();
                }
                ++i;
                continue;
            }
            finally {
            }
            // monitorexit(this)
            break;
        }
    }
    // monitorexit(this)
}
