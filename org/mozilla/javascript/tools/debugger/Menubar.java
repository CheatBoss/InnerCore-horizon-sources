package org.mozilla.javascript.tools.debugger;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

class Menubar extends JMenuBar implements ActionListener
{
    private static final long serialVersionUID = 3217170497245911461L;
    private JCheckBoxMenuItem breakOnEnter;
    private JCheckBoxMenuItem breakOnExceptions;
    private JCheckBoxMenuItem breakOnReturn;
    private SwingGui debugGui;
    private List<JMenuItem> interruptOnlyItems;
    private List<JMenuItem> runOnlyItems;
    private JMenu windowMenu;
    
    Menubar(final SwingGui debugGui) {
        this.interruptOnlyItems = Collections.synchronizedList(new ArrayList<JMenuItem>());
        this.runOnlyItems = Collections.synchronizedList(new ArrayList<JMenuItem>());
        this.debugGui = debugGui;
        final String[] array = { "Open...", "Run...", "", "Exit" };
        final int[] array3;
        final int[] array2 = array3 = new int[4];
        array3[0] = 79;
        array3[1] = 78;
        array3[2] = 0;
        array3[3] = 81;
        final String[] array4 = { "Cut", "Copy", "Paste", "Go to function..." };
        final String[] array5 = { "Break", "Go", "Step Into", "Step Over", "Step Out" };
        final char[] array7;
        final char[] array6 = array7 = new char[5];
        array7[0] = 'B';
        array7[1] = 'G';
        array7[2] = 'I';
        array7[3] = 'O';
        array7[4] = 'T';
        final String[] array8 = { "Metal", "Windows", "Motif" };
        final int[] array10;
        final int[] array9 = array10 = new int[7];
        array10[0] = 19;
        array10[1] = 116;
        array10[2] = 122;
        array10[3] = 118;
        array10[4] = 119;
        array10[6] = (array10[5] = 0);
        final JMenu menu = new JMenu("File");
        menu.setMnemonic('F');
        final JMenu menu2 = new JMenu("Edit");
        menu2.setMnemonic('E');
        final JMenu menu3 = new JMenu("Platform");
        menu3.setMnemonic('P');
        final JMenu menu4 = new JMenu("Debug");
        menu4.setMnemonic('D');
        (this.windowMenu = new JMenu("Window")).setMnemonic('W');
        for (int i = 0; i < array.length; ++i) {
            if (array[i].length() == 0) {
                menu.addSeparator();
            }
            else {
                final JMenuItem menuItem = new JMenuItem(array[i], (new char[] { '0', 'N', '\0', 'X' })[i]);
                menuItem.setActionCommand((new String[] { "Open", "Load", "", "Exit" })[i]);
                menuItem.addActionListener(this);
                menu.add(menuItem);
                if (array2[i] != 0) {
                    menuItem.setAccelerator(KeyStroke.getKeyStroke(array2[i], 2));
                }
            }
        }
        for (int j = 0; j < array4.length; ++j) {
            final JMenuItem menuItem2 = new JMenuItem(array4[j], (new char[] { 'T', 'C', 'P', 'F' })[j]);
            menuItem2.addActionListener(this);
            menu2.add(menuItem2);
        }
        for (int k = 0; k < array8.length; ++k) {
            final JMenuItem menuItem3 = new JMenuItem(array8[k], (new char[] { 'M', 'W', 'F' })[k]);
            menuItem3.addActionListener(this);
            menu3.add(menuItem3);
        }
        for (int l = 0; l < array5.length; ++l) {
            final JMenuItem menuItem4 = new JMenuItem(array5[l], array6[l]);
            menuItem4.addActionListener(this);
            if (array9[l] != 0) {
                menuItem4.setAccelerator(KeyStroke.getKeyStroke(array9[l], 0));
            }
            if (l != 0) {
                this.interruptOnlyItems.add(menuItem4);
            }
            else {
                this.runOnlyItems.add(menuItem4);
            }
            menu4.add(menuItem4);
        }
        (this.breakOnExceptions = new JCheckBoxMenuItem("Break on Exceptions")).setMnemonic('X');
        this.breakOnExceptions.addActionListener(this);
        this.breakOnExceptions.setSelected(false);
        menu4.add(this.breakOnExceptions);
        (this.breakOnEnter = new JCheckBoxMenuItem("Break on Function Enter")).setMnemonic('E');
        this.breakOnEnter.addActionListener(this);
        this.breakOnEnter.setSelected(false);
        menu4.add(this.breakOnEnter);
        (this.breakOnReturn = new JCheckBoxMenuItem("Break on Function Return")).setMnemonic('R');
        this.breakOnReturn.addActionListener(this);
        this.breakOnReturn.setSelected(false);
        menu4.add(this.breakOnReturn);
        this.add(menu);
        this.add(menu2);
        this.add(menu4);
        final JMenu windowMenu = this.windowMenu;
        final JMenuItem menuItem5 = new JMenuItem("Cascade", 65);
        windowMenu.add(menuItem5);
        menuItem5.addActionListener(this);
        final JMenu windowMenu2 = this.windowMenu;
        final JMenuItem menuItem6 = new JMenuItem("Tile", 84);
        windowMenu2.add(menuItem6);
        menuItem6.addActionListener(this);
        this.windowMenu.addSeparator();
        final JMenu windowMenu3 = this.windowMenu;
        final JMenuItem menuItem7 = new JMenuItem("Console", 67);
        windowMenu3.add(menuItem7);
        menuItem7.addActionListener(this);
        this.add(this.windowMenu);
        this.updateEnabled(false);
    }
    
    @Override
    public void actionPerformed(final ActionEvent actionEvent) {
        final String actionCommand = actionEvent.getActionCommand();
        Label_0071: {
            String lookAndFeel;
            if (actionCommand.equals("Metal")) {
                lookAndFeel = "javax.swing.plaf.metal.MetalLookAndFeel";
            }
            else if (actionCommand.equals("Windows")) {
                lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
            }
            else {
                if (!actionCommand.equals("Motif")) {
                    break Label_0071;
                }
                lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
            }
            try {
                UIManager.setLookAndFeel(lookAndFeel);
                SwingUtilities.updateComponentTreeUI(this.debugGui);
                SwingUtilities.updateComponentTreeUI(this.debugGui.dlg);
                return;
            }
            catch (Exception ex) {
                return;
            }
        }
        final Object source = actionEvent.getSource();
        if (source == this.breakOnExceptions) {
            this.debugGui.dim.setBreakOnExceptions(this.breakOnExceptions.isSelected());
            return;
        }
        if (source == this.breakOnEnter) {
            this.debugGui.dim.setBreakOnEnter(this.breakOnEnter.isSelected());
            return;
        }
        if (source == this.breakOnReturn) {
            this.debugGui.dim.setBreakOnReturn(this.breakOnReturn.isSelected());
            return;
        }
        this.debugGui.actionPerformed(actionEvent);
    }
    
    public void addFile(final String actionCommand) {
        int itemCount;
        final int n = itemCount = this.windowMenu.getItemCount();
        if (n == 4) {
            this.windowMenu.addSeparator();
            itemCount = n + 1;
        }
        final JMenuItem item = this.windowMenu.getItem(itemCount - 1);
        final boolean b = false;
        final int n2 = 5;
        boolean b2 = b;
        int n3 = n2;
        if (item != null) {
            b2 = b;
            n3 = n2;
            if (item.getText().equals("More Windows...")) {
                b2 = true;
                n3 = 5 + 1;
            }
        }
        if (!b2 && itemCount - 4 == 5) {
            final JMenu windowMenu = this.windowMenu;
            final JMenuItem menuItem = new JMenuItem("More Windows...", 77);
            windowMenu.add(menuItem);
            menuItem.setActionCommand("More Windows...");
            menuItem.addActionListener(this);
            return;
        }
        if (itemCount - 4 <= n3) {
            int n4 = itemCount;
            if (b2) {
                n4 = itemCount - 1;
                this.windowMenu.remove(item);
            }
            final String shortName = SwingGui.getShortName(actionCommand);
            final JMenu windowMenu2 = this.windowMenu;
            final StringBuilder sb = new StringBuilder();
            sb.append((char)(n4 - 4 + 48));
            sb.append(" ");
            sb.append(shortName);
            final JMenuItem menuItem2 = new JMenuItem(sb.toString(), n4 - 4 + 48);
            windowMenu2.add(menuItem2);
            if (b2) {
                this.windowMenu.add(item);
            }
            menuItem2.setActionCommand(actionCommand);
            menuItem2.addActionListener(this);
        }
    }
    
    public JCheckBoxMenuItem getBreakOnEnter() {
        return this.breakOnEnter;
    }
    
    public JCheckBoxMenuItem getBreakOnExceptions() {
        return this.breakOnExceptions;
    }
    
    public JCheckBoxMenuItem getBreakOnReturn() {
        return this.breakOnReturn;
    }
    
    public JMenu getDebugMenu() {
        return this.getMenu(2);
    }
    
    public void updateEnabled(final boolean enabled) {
        final int n = 0;
        for (int i = 0; i != this.interruptOnlyItems.size(); ++i) {
            this.interruptOnlyItems.get(i).setEnabled(enabled);
        }
        for (int j = n; j != this.runOnlyItems.size(); ++j) {
            this.runOnlyItems.get(j).setEnabled(enabled ^ true);
        }
    }
}
