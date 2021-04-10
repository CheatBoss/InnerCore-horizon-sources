package org.mozilla.javascript.tools.debugger;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

class FilePopupMenu extends JPopupMenu
{
    private static final long serialVersionUID = 3589525009546013565L;
    int x;
    int y;
    
    public FilePopupMenu(final FileTextArea fileTextArea) {
        final JMenuItem menuItem = new JMenuItem("Set Breakpoint");
        this.add(menuItem);
        menuItem.addActionListener(fileTextArea);
        final JMenuItem menuItem2 = new JMenuItem("Clear Breakpoint");
        this.add(menuItem2);
        menuItem2.addActionListener(fileTextArea);
        final JMenuItem menuItem3 = new JMenuItem("Run");
        this.add(menuItem3);
        menuItem3.addActionListener(fileTextArea);
    }
    
    public void show(final JComponent component, final int x, final int y) {
        super.show(component, this.x = x, this.y = y);
    }
}
