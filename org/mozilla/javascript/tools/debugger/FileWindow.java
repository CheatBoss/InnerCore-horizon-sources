package org.mozilla.javascript.tools.debugger;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.text.*;

class FileWindow extends JInternalFrame implements ActionListener
{
    private static final long serialVersionUID = -6212382604952082370L;
    int currentPos;
    private SwingGui debugGui;
    private FileHeader fileHeader;
    private JScrollPane p;
    private Dim.SourceInfo sourceInfo;
    FileTextArea textArea;
    
    public FileWindow(final SwingGui debugGui, final Dim.SourceInfo sourceInfo) {
        super(SwingGui.getShortName(sourceInfo.url()), true, true, true, true);
        this.debugGui = debugGui;
        this.sourceInfo = sourceInfo;
        this.updateToolTip();
        this.currentPos = -1;
        (this.textArea = new FileTextArea(this)).setRows(24);
        this.textArea.setColumns(80);
        this.p = new JScrollPane();
        this.fileHeader = new FileHeader(this);
        this.p.setViewportView(this.textArea);
        this.p.setRowHeaderView(this.fileHeader);
        this.setContentPane(this.p);
        this.pack();
        this.updateText(sourceInfo);
        this.textArea.select(0);
    }
    
    private void updateToolTip() {
        final int n = this.getComponentCount() - 1;
        int n2;
        if (n > 1) {
            n2 = 1;
        }
        else if ((n2 = n) < 0) {
            return;
        }
        final Component component = this.getComponent(n2);
        if (component != null && component instanceof JComponent) {
            ((JComponent)component).setToolTipText(this.getUrl());
        }
    }
    
    @Override
    public void actionPerformed(final ActionEvent actionEvent) {
        final String actionCommand = actionEvent.getActionCommand();
        if (actionCommand.equals("Cut")) {
            return;
        }
        if (actionCommand.equals("Copy")) {
            this.textArea.copy();
            return;
        }
        actionCommand.equals("Paste");
    }
    
    public void clearBreakPoint(final int n) {
        if (this.sourceInfo.breakableLine(n) && this.sourceInfo.breakpoint(n, false)) {
            this.fileHeader.repaint();
        }
    }
    
    @Override
    public void dispose() {
        this.debugGui.removeWindow(this);
        super.dispose();
    }
    
    public int getPosition(int lineStartOffset) {
        try {
            lineStartOffset = this.textArea.getLineStartOffset(lineStartOffset);
            return lineStartOffset;
        }
        catch (BadLocationException ex) {
            return -1;
        }
    }
    
    public String getUrl() {
        return this.sourceInfo.url();
    }
    
    public boolean isBreakPoint(final int n) {
        return this.sourceInfo.breakableLine(n) && this.sourceInfo.breakpoint(n);
    }
    
    void load() {
        final String url = this.getUrl();
        if (url != null) {
            final RunProxy runProxy = new RunProxy(this.debugGui, 2);
            runProxy.fileName = url;
            runProxy.text = this.sourceInfo.source();
            new Thread(runProxy).start();
        }
    }
    
    public void select(final int n, final int n2) {
        final int length = this.textArea.getDocument().getLength();
        this.textArea.select(length, length);
        this.textArea.select(n, n2);
    }
    
    public void setBreakPoint(final int n) {
        if (this.sourceInfo.breakableLine(n) && this.sourceInfo.breakpoint(n, true)) {
            this.fileHeader.repaint();
        }
    }
    
    public void setPosition(final int currentPos) {
        this.textArea.select(currentPos);
        this.currentPos = currentPos;
        this.fileHeader.repaint();
    }
    
    public void toggleBreakPoint(final int breakPoint) {
        if (!this.isBreakPoint(breakPoint)) {
            this.setBreakPoint(breakPoint);
            return;
        }
        this.clearBreakPoint(breakPoint);
    }
    
    public void updateText(final Dim.SourceInfo sourceInfo) {
        this.sourceInfo = sourceInfo;
        final String source = sourceInfo.source();
        if (!this.textArea.getText().equals(source)) {
            this.textArea.setText(source);
            int currentPos = 0;
            if (this.currentPos != -1) {
                currentPos = this.currentPos;
            }
            this.textArea.select(currentPos);
        }
        this.fileHeader.update();
        this.fileHeader.repaint();
    }
}
