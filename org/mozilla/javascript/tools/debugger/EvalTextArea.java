package org.mozilla.javascript.tools.debugger;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.event.*;

class EvalTextArea extends JTextArea implements KeyListener, DocumentListener
{
    private static final long serialVersionUID = -3918033649601064194L;
    private SwingGui debugGui;
    private List<String> history;
    private int historyIndex;
    private int outputMark;
    
    public EvalTextArea(final SwingGui debugGui) {
        this.historyIndex = -1;
        this.debugGui = debugGui;
        this.history = Collections.synchronizedList(new ArrayList<String>());
        final Document document = this.getDocument();
        document.addDocumentListener(this);
        this.addKeyListener(this);
        this.setLineWrap(true);
        this.setFont(new Font("Monospaced", 0, 12));
        this.append("% ");
        this.outputMark = document.getLength();
    }
    
    private void returnPressed() {
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
            final String string = segment.toString();
            if (this.debugGui.dim.stringIsCompilableUnit(string)) {
                if (string.trim().length() > 0) {
                    this.history.add(string);
                    this.historyIndex = this.history.size();
                }
                this.append("\n");
                final String eval = this.debugGui.dim.eval(string);
                if (eval.length() > 0) {
                    this.append(eval);
                    this.append("\n");
                }
                this.append("% ");
                this.outputMark = document.getLength();
            }
            else {
                this.append("\n");
            }
        }
    }
    
    @Override
    public void changedUpdate(final DocumentEvent documentEvent) {
    }
    // monitorenter(this)
    // monitorexit(this)
    
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
    
    @Override
    public void select(final int n, final int n2) {
        super.select(n, n2);
    }
    
    public void write(final String s) {
        synchronized (this) {
            this.insert(s, this.outputMark);
            this.select(this.outputMark += s.length(), this.outputMark);
        }
    }
}
