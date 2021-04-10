package org.mozilla.javascript.tools.debugger;

import org.mozilla.javascript.tools.shell.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.io.*;

class JSInternalConsole extends JInternalFrame implements ActionListener
{
    private static final long serialVersionUID = -5523468828771087292L;
    ConsoleTextArea consoleTextArea;
    
    public JSInternalConsole(final String s) {
        super(s, true, false, true, true);
        (this.consoleTextArea = new ConsoleTextArea((String[])null)).setRows(24);
        this.consoleTextArea.setColumns(80);
        this.setContentPane(new JScrollPane(this.consoleTextArea));
        this.pack();
        this.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameActivated(final InternalFrameEvent internalFrameEvent) {
                if (JSInternalConsole.this.consoleTextArea.hasFocus()) {
                    JSInternalConsole.this.consoleTextArea.getCaret().setVisible(false);
                    JSInternalConsole.this.consoleTextArea.getCaret().setVisible(true);
                }
            }
        });
    }
    
    @Override
    public void actionPerformed(final ActionEvent actionEvent) {
        final String actionCommand = actionEvent.getActionCommand();
        if (actionCommand.equals("Cut")) {
            this.consoleTextArea.cut();
            return;
        }
        if (actionCommand.equals("Copy")) {
            this.consoleTextArea.copy();
            return;
        }
        if (actionCommand.equals("Paste")) {
            this.consoleTextArea.paste();
        }
    }
    
    public PrintStream getErr() {
        return this.consoleTextArea.getErr();
    }
    
    public InputStream getIn() {
        return this.consoleTextArea.getIn();
    }
    
    public PrintStream getOut() {
        return this.consoleTextArea.getOut();
    }
}
