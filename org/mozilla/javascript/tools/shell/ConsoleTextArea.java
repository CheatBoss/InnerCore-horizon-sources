package org.mozilla.javascript.tools.shell;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import javax.swing.event.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.text.*;

public class ConsoleTextArea extends JTextArea implements KeyListener, DocumentListener
{
    static final long serialVersionUID = 8557083244830872961L;
    private ConsoleWriter console1;
    private ConsoleWriter console2;
    private PrintStream err;
    private List<String> history;
    private int historyIndex;
    private PipedInputStream in;
    private PrintWriter inPipe;
    private PrintStream out;
    private int outputMark;
    
    public ConsoleTextArea(final String[] array) {
        this.historyIndex = -1;
        this.outputMark = 0;
        this.history = new ArrayList<String>();
        this.console1 = new ConsoleWriter(this);
        this.console2 = new ConsoleWriter(this);
        this.out = new PrintStream(this.console1, true);
        this.err = new PrintStream(this.console2, true);
        final PipedOutputStream pipedOutputStream = new PipedOutputStream();
        this.inPipe = new PrintWriter(pipedOutputStream);
        this.in = new PipedInputStream();
        try {
            pipedOutputStream.connect(this.in);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        this.getDocument().addDocumentListener(this);
        this.addKeyListener(this);
        this.setLineWrap(true);
        this.setFont(new Font("Monospaced", 0, 12));
    }
    
    @Override
    public void changedUpdate(final DocumentEvent documentEvent) {
    }
    // monitorenter(this)
    // monitorexit(this)
    
    public void eval(final String s) {
        this.inPipe.write(s);
        this.inPipe.write("\n");
        this.inPipe.flush();
        this.console1.flush();
    }
    
    public PrintStream getErr() {
        return this.err;
    }
    
    public InputStream getIn() {
        return this.in;
    }
    
    public PrintStream getOut() {
        return this.out;
    }
    
    @Override
    public void insertUpdate(final DocumentEvent documentEvent) {
        synchronized (this) {
            final int length = documentEvent.getLength();
            if (this.outputMark > documentEvent.getOffset()) {
                this.outputMark += length;
            }
        }
    }
    
    @Override
    public void keyPressed(final KeyEvent keyEvent) {
        final int keyCode = keyEvent.getKeyCode();
        if (keyCode != 8 && keyCode != 37) {
            if (keyCode == 36) {
                final int caretPosition = this.getCaretPosition();
                if (caretPosition == this.outputMark) {
                    keyEvent.consume();
                }
                else if (caretPosition > this.outputMark && !keyEvent.isControlDown()) {
                    if (keyEvent.isShiftDown()) {
                        this.moveCaretPosition(this.outputMark);
                    }
                    else {
                        this.setCaretPosition(this.outputMark);
                    }
                    keyEvent.consume();
                }
                return;
            }
            if (keyCode == 10) {
                this.returnPressed();
                keyEvent.consume();
                return;
            }
            if (keyCode == 38) {
                --this.historyIndex;
                if (this.historyIndex >= 0) {
                    if (this.historyIndex >= this.history.size()) {
                        this.historyIndex = this.history.size() - 1;
                    }
                    if (this.historyIndex >= 0) {
                        final String s = this.history.get(this.historyIndex);
                        this.replaceRange(s, this.outputMark, this.getDocument().getLength());
                        final int n = this.outputMark + s.length();
                        this.select(n, n);
                    }
                    else {
                        ++this.historyIndex;
                    }
                }
                else {
                    ++this.historyIndex;
                }
                keyEvent.consume();
                return;
            }
            if (keyCode == 40) {
                int outputMark;
                final int n2 = outputMark = this.outputMark;
                if (this.history.size() > 0) {
                    ++this.historyIndex;
                    if (this.historyIndex < 0) {
                        this.historyIndex = 0;
                    }
                    final int length = this.getDocument().getLength();
                    if (this.historyIndex < this.history.size()) {
                        final String s2 = this.history.get(this.historyIndex);
                        this.replaceRange(s2, this.outputMark, length);
                        outputMark = this.outputMark + s2.length();
                    }
                    else {
                        this.historyIndex = this.history.size();
                        this.replaceRange("", this.outputMark, length);
                        outputMark = n2;
                    }
                }
                this.select(outputMark, outputMark);
                keyEvent.consume();
            }
        }
        else if (this.outputMark == this.getCaretPosition()) {
            keyEvent.consume();
        }
    }
    
    @Override
    public void keyReleased(final KeyEvent keyEvent) {
    }
    // monitorenter(this)
    // monitorexit(this)
    
    @Override
    public void keyTyped(final KeyEvent keyEvent) {
        if (keyEvent.getKeyChar() == '\b') {
            if (this.outputMark == this.getCaretPosition()) {
                keyEvent.consume();
            }
        }
        else if (this.getCaretPosition() < this.outputMark) {
            this.setCaretPosition(this.outputMark);
        }
    }
    
    public void postUpdateUI() {
        synchronized (this) {
            this.requestFocus();
            this.setCaret(this.getCaret());
            this.select(this.outputMark, this.outputMark);
        }
    }
    
    @Override
    public void removeUpdate(final DocumentEvent documentEvent) {
        synchronized (this) {
            final int length = documentEvent.getLength();
            final int offset = documentEvent.getOffset();
            if (this.outputMark > offset) {
                if (this.outputMark >= offset + length) {
                    this.outputMark -= length;
                }
                else {
                    this.outputMark = offset;
                }
            }
        }
    }
    
    void returnPressed() {
        synchronized (this) {
            final Document document = this.getDocument();
            final int length = document.getLength();
            final Segment segment = new Segment();
            try {
                document.getText(this.outputMark, length - this.outputMark, segment);
            }
            catch (BadLocationException ex) {
                ex.printStackTrace();
            }
            if (segment.count > 0) {
                this.history.add(segment.toString());
            }
            this.historyIndex = this.history.size();
            this.inPipe.write(segment.array, segment.offset, segment.count);
            this.append("\n");
            this.outputMark = document.getLength();
            this.inPipe.write("\n");
            this.inPipe.flush();
            this.console1.flush();
        }
    }
    
    @Override
    public void select(final int n, final int n2) {
        this.requestFocus();
        super.select(n, n2);
    }
    
    public void write(final String s) {
        synchronized (this) {
            this.insert(s, this.outputMark);
            this.select(this.outputMark += s.length(), this.outputMark);
        }
    }
}
