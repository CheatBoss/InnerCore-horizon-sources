package org.mozilla.javascript.tools.shell;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.mozilla.javascript.*;
import javax.swing.filechooser.*;

public class JSConsole extends JFrame implements ActionListener
{
    static final long serialVersionUID = 2551225560631876300L;
    private File CWD;
    private ConsoleTextArea consoleTextArea;
    private JFileChooser dlg;
    
    public JSConsole(final String[] array) {
        super("Rhino JavaScript Console");
        final JMenuBar jMenuBar = new JMenuBar();
        this.createFileChooser();
        final String[] array2 = { "Load...", "Exit" };
        final String[] array3 = { "Cut", "Copy", "Paste" };
        final String[] array4 = { "Metal", "Windows", "Motif" };
        final JMenu menu = new JMenu("File");
        menu.setMnemonic('F');
        final JMenu menu2 = new JMenu("Edit");
        menu2.setMnemonic('E');
        final JMenu menu3 = new JMenu("Platform");
        menu3.setMnemonic('P');
        for (int i = 0; i < array2.length; ++i) {
            final JMenuItem menuItem = new JMenuItem(array2[i], (new char[] { 'L', 'X' })[i]);
            menuItem.setActionCommand((new String[] { "Load", "Exit" })[i]);
            menuItem.addActionListener(this);
            menu.add(menuItem);
        }
        for (int j = 0; j < array3.length; ++j) {
            final JMenuItem menuItem2 = new JMenuItem(array3[j], (new char[] { 'T', 'C', 'P' })[j]);
            menuItem2.addActionListener(this);
            menu2.add(menuItem2);
        }
        final ButtonGroup buttonGroup = new ButtonGroup();
        for (int k = 0; k < array4.length; ++k) {
            final JRadioButtonMenuItem radioButtonMenuItem = new JRadioButtonMenuItem(array4[k], (new boolean[] { true, false, false })[k]);
            buttonGroup.add(radioButtonMenuItem);
            radioButtonMenuItem.addActionListener(this);
            menu3.add(radioButtonMenuItem);
        }
        jMenuBar.add(menu);
        jMenuBar.add(menu2);
        jMenuBar.add(menu3);
        this.setJMenuBar(jMenuBar);
        this.consoleTextArea = new ConsoleTextArea(array);
        this.setContentPane(new JScrollPane(this.consoleTextArea));
        this.consoleTextArea.setRows(24);
        this.consoleTextArea.setColumns(80);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        this.pack();
        this.setVisible(true);
        Main.setIn(this.consoleTextArea.getIn());
        Main.setOut(this.consoleTextArea.getOut());
        Main.setErr(this.consoleTextArea.getErr());
        Main.main(array);
    }
    
    public static void main(final String[] array) {
        new JSConsole(array);
    }
    
    @Override
    public void actionPerformed(final ActionEvent actionEvent) {
        final String actionCommand = actionEvent.getActionCommand();
        String lookAndFeel = null;
        if (actionCommand.equals("Load")) {
            final String chooseFile = this.chooseFile();
            if (chooseFile != null) {
                final String replace = chooseFile.replace('\\', '/');
                final ConsoleTextArea consoleTextArea = this.consoleTextArea;
                final StringBuilder sb = new StringBuilder();
                sb.append("load(\"");
                sb.append(replace);
                sb.append("\");");
                consoleTextArea.eval(sb.toString());
            }
            return;
        }
        if (actionCommand.equals("Exit")) {
            System.exit(0);
            return;
        }
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
            return;
        }
        if (actionCommand.equals("Metal")) {
            lookAndFeel = "javax.swing.plaf.metal.MetalLookAndFeel";
        }
        else if (actionCommand.equals("Windows")) {
            lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
        }
        else if (actionCommand.equals("Motif")) {
            lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
        }
        if (lookAndFeel != null) {
            try {
                UIManager.setLookAndFeel(lookAndFeel);
                SwingUtilities.updateComponentTreeUI(this);
                this.consoleTextArea.postUpdateUI();
                this.createFileChooser();
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Platform", 0);
            }
        }
    }
    
    public String chooseFile() {
        if (this.CWD == null) {
            final String systemProperty = SecurityUtilities.getSystemProperty("user.dir");
            if (systemProperty != null) {
                this.CWD = new File(systemProperty);
            }
        }
        if (this.CWD != null) {
            this.dlg.setCurrentDirectory(this.CWD);
        }
        this.dlg.setDialogTitle("Select a file to load");
        if (this.dlg.showOpenDialog(this) == 0) {
            final String path = this.dlg.getSelectedFile().getPath();
            this.CWD = new File(this.dlg.getSelectedFile().getParent());
            return path;
        }
        return null;
    }
    
    public void createFileChooser() {
        (this.dlg = new JFileChooser()).addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(final File file) {
                if (file.isDirectory()) {
                    return true;
                }
                final String name = file.getName();
                final int lastIndex = name.lastIndexOf(46);
                return lastIndex > 0 && lastIndex < name.length() - 1 && name.substring(lastIndex + 1).toLowerCase().equals("js");
            }
            
            @Override
            public String getDescription() {
                return "JavaScript Files (*.js)";
            }
        });
    }
}
