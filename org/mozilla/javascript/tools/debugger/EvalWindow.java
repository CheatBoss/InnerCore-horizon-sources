package org.mozilla.javascript.tools.debugger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class EvalWindow extends JInternalFrame implements ActionListener
{
    private static final long serialVersionUID = -2860585845212160176L;
    private EvalTextArea evalTextArea;
    
    public EvalWindow(final String s, final SwingGui swingGui) {
        super(s, true, false, true, true);
        (this.evalTextArea = new EvalTextArea(swingGui)).setRows(24);
        this.evalTextArea.setColumns(80);
        this.setContentPane(new JScrollPane(this.evalTextArea));
        this.pack();
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(final ActionEvent actionEvent) {
        final String actionCommand = actionEvent.getActionCommand();
        if (actionCommand.equals("Cut")) {
            this.evalTextArea.cut();
            return;
        }
        if (actionCommand.equals("Copy")) {
            this.evalTextArea.copy();
            return;
        }
        if (actionCommand.equals("Paste")) {
            this.evalTextArea.paste();
        }
    }
    
    @Override
    public void setEnabled(final boolean b) {
        super.setEnabled(b);
        this.evalTextArea.setEnabled(b);
    }
}
