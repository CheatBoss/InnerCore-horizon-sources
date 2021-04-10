package org.mozilla.javascript.tools.debugger;

import javax.swing.filechooser.*;
import org.mozilla.javascript.*;
import java.io.*;
import javax.swing.text.*;
import java.lang.reflect.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;

public class SwingGui extends JFrame implements GuiCallback
{
    private static final long serialVersionUID = -8217029773456711621L;
    private EventQueue awtEventQueue;
    private JSInternalConsole console;
    private ContextWindow context;
    private FileWindow currentWindow;
    private JDesktopPane desk;
    Dim dim;
    JFileChooser dlg;
    private Runnable exitAction;
    private final Map<String, FileWindow> fileWindows;
    private Menubar menubar;
    private JSplitPane split1;
    private JLabel statusBar;
    private JToolBar toolBar;
    private final Map<String, JFrame> toplevels;
    
    public SwingGui(final Dim dim, final String s) {
        super(s);
        this.toplevels = Collections.synchronizedMap(new HashMap<String, JFrame>());
        this.fileWindows = Collections.synchronizedMap(new HashMap<String, FileWindow>());
        this.dim = dim;
        this.init();
        dim.setGuiCallback(this);
    }
    
    private String chooseFile(String canonicalPath) {
        this.dlg.setDialogTitle(canonicalPath);
        File currentDirectory = null;
        final String systemProperty = SecurityUtilities.getSystemProperty("user.dir");
        if (systemProperty != null) {
            currentDirectory = new File(systemProperty);
        }
        if (currentDirectory != null) {
            this.dlg.setCurrentDirectory(currentDirectory);
        }
        if (this.dlg.showOpenDialog(this) == 0) {
            try {
                canonicalPath = this.dlg.getSelectedFile().getCanonicalPath();
                final File parentFile = this.dlg.getSelectedFile().getParentFile();
                final Properties properties = System.getProperties();
                ((Hashtable<String, String>)properties).put("user.dir", parentFile.getPath());
                System.setProperties(properties);
                return canonicalPath;
            }
            catch (SecurityException ex) {}
            catch (IOException ex2) {}
        }
        return null;
    }
    
    private void exit() {
        if (this.exitAction != null) {
            SwingUtilities.invokeLater(this.exitAction);
        }
        this.dim.setReturnValue(5);
    }
    
    private JInternalFrame getSelectedFrame() {
        final JInternalFrame[] allFrames = this.desk.getAllFrames();
        for (int i = 0; i < allFrames.length; ++i) {
            if (allFrames[i].isShowing()) {
                return allFrames[i];
            }
        }
        return allFrames[allFrames.length - 1];
    }
    
    static String getShortName(final String s) {
        int n;
        if ((n = s.lastIndexOf(47)) < 0) {
            n = s.lastIndexOf(92);
        }
        String substring = s;
        if (n >= 0) {
            substring = s;
            if (n + 1 < s.length()) {
                substring = s.substring(n + 1);
            }
        }
        return substring;
    }
    
    private JMenu getWindowMenu() {
        return this.menubar.getMenu(3);
    }
    
    private void init() {
        this.setJMenuBar(this.menubar = new Menubar(this));
        this.toolBar = new JToolBar();
        final String[] array = { "Break (Pause)", "Go (F5)", "Step Into (F11)", "Step Over (F7)", "Step Out (F8)" };
        final JButton button = new JButton("Break");
        button.setToolTipText("Break");
        button.setActionCommand("Break");
        button.addActionListener(this.menubar);
        button.setEnabled(true);
        final int n = 0 + 1;
        button.setToolTipText(array[0]);
        final JButton button2 = new JButton("Go");
        button2.setToolTipText("Go");
        button2.setActionCommand("Go");
        button2.addActionListener(this.menubar);
        button2.setEnabled(false);
        final int n2 = n + 1;
        button2.setToolTipText(array[n]);
        final JButton button3 = new JButton("Step Into");
        button3.setToolTipText("Step Into");
        button3.setActionCommand("Step Into");
        button3.addActionListener(this.menubar);
        button3.setEnabled(false);
        final int n3 = n2 + 1;
        button3.setToolTipText(array[n2]);
        final JButton button4 = new JButton("Step Over");
        button4.setToolTipText("Step Over");
        button4.setActionCommand("Step Over");
        button4.setEnabled(false);
        button4.addActionListener(this.menubar);
        final int n4 = n3 + 1;
        button4.setToolTipText(array[n3]);
        final JButton button5 = new JButton("Step Out");
        button5.setToolTipText("Step Out");
        button5.setActionCommand("Step Out");
        button5.setEnabled(false);
        button5.addActionListener(this.menubar);
        button5.setToolTipText(array[n4]);
        final Dimension preferredSize = button4.getPreferredSize();
        button.setPreferredSize(preferredSize);
        button.setMinimumSize(preferredSize);
        button.setMaximumSize(preferredSize);
        button.setSize(preferredSize);
        button2.setPreferredSize(preferredSize);
        button2.setMinimumSize(preferredSize);
        button2.setMaximumSize(preferredSize);
        button3.setPreferredSize(preferredSize);
        button3.setMinimumSize(preferredSize);
        button3.setMaximumSize(preferredSize);
        button4.setPreferredSize(preferredSize);
        button4.setMinimumSize(preferredSize);
        button4.setMaximumSize(preferredSize);
        button5.setPreferredSize(preferredSize);
        button5.setMinimumSize(preferredSize);
        button5.setMaximumSize(preferredSize);
        this.toolBar.add(button);
        this.toolBar.add(button2);
        this.toolBar.add(button3);
        this.toolBar.add(button4);
        this.toolBar.add(button5);
        final JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        this.getContentPane().add(this.toolBar, "North");
        this.getContentPane().add(panel, "Center");
        (this.desk = new JDesktopPane()).setPreferredSize(new Dimension(600, 300));
        this.desk.setMinimumSize(new Dimension(150, 50));
        this.desk.add(this.console = new JSInternalConsole("JavaScript Console"));
        (this.context = new ContextWindow(this)).setPreferredSize(new Dimension(600, 120));
        this.context.setMinimumSize(new Dimension(50, 50));
        (this.split1 = new JSplitPane(0, this.desk, this.context)).setOneTouchExpandable(true);
        setResizeWeight(this.split1, 0.66);
        panel.add(this.split1, "Center");
        (this.statusBar = new JLabel()).setText("Thread: ");
        panel.add(this.statusBar, "South");
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
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent windowEvent) {
                SwingGui.this.exit();
            }
        });
    }
    
    private String readFile(final String s) {
        try {
            final FileReader fileReader = new FileReader(s);
            try {
                return Kit.readReader(fileReader);
            }
            finally {
                fileReader.close();
            }
        }
        catch (IOException ex) {
            final String message = ex.getMessage();
            final StringBuilder sb = new StringBuilder();
            sb.append("Error reading ");
            sb.append(s);
            MessageDialogWrapper.showMessageDialog(this, message, sb.toString(), 0);
            return null;
        }
    }
    
    private void setFilePosition(final FileWindow currentWindow, int lineStartOffset) {
        final FileTextArea textArea = currentWindow.textArea;
        Label_0031: {
            if (lineStartOffset != -1) {
                break Label_0031;
            }
        Label_0062_Outer:
            while (true) {
                try {
                    currentWindow.setPosition(-1);
                    if (this.currentWindow == currentWindow) {
                        this.currentWindow = null;
                    }
                    if (true) {
                        if (currentWindow.isIcon()) {
                            this.desk.getDesktopManager().deiconifyFrame(currentWindow);
                        }
                        this.desk.getDesktopManager().activateFrame(currentWindow);
                        try {
                            currentWindow.show();
                            currentWindow.toFront();
                            currentWindow.setSelected(true);
                            return;
                        }
                        catch (Exception ex) {}
                    }
                    return;
                    while (true) {
                        currentWindow.setPosition(lineStartOffset);
                        this.currentWindow = currentWindow;
                        continue Label_0062_Outer;
                        lineStartOffset = textArea.getLineStartOffset(lineStartOffset - 1);
                        this.currentWindow.setPosition(-1);
                        continue;
                    }
                }
                // iftrue(Label_0062:, this.currentWindow == null || this.currentWindow == currentWindow)
                catch (BadLocationException ex2) {
                    continue;
                }
                break;
            }
        }
    }
    
    static void setResizeWeight(final JSplitPane splitPane, final double n) {
        try {
            JSplitPane.class.getMethod("setResizeWeight", Double.TYPE).invoke(splitPane, new Double(n));
        }
        catch (InvocationTargetException ex) {}
        catch (IllegalAccessException ex2) {}
        catch (NoSuchMethodException ex3) {}
    }
    
    private void updateEnabled(final boolean b) {
        ((Menubar)this.getJMenuBar()).updateEnabled(b);
        for (int i = 0; i < this.toolBar.getComponentCount(); ++i) {
            boolean enabled;
            if (i == 0) {
                enabled = (b ^ true);
            }
            else {
                enabled = b;
            }
            this.toolBar.getComponent(i).setEnabled(enabled);
        }
        if (b) {
            this.toolBar.setEnabled(true);
            if (this.getExtendedState() == 1) {
                this.setExtendedState(0);
            }
            this.toFront();
            this.context.setEnabled(true);
            return;
        }
        if (this.currentWindow != null) {
            this.currentWindow.setPosition(-1);
        }
        this.context.setEnabled(false);
    }
    
    public void actionPerformed(final ActionEvent p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   java/awt/event/ActionEvent.getActionCommand:()Ljava/lang/String;
        //     4: astore          12
        //     6: iconst_m1      
        //     7: istore_3       
        //     8: aload           12
        //    10: ldc_w           "Cut"
        //    13: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    16: ifne            936
        //    19: aload           12
        //    21: ldc_w           "Copy"
        //    24: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    27: ifne            936
        //    30: aload           12
        //    32: ldc_w           "Paste"
        //    35: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    38: ifeq            44
        //    41: goto            936
        //    44: aload           12
        //    46: ldc             "Step Over"
        //    48: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    51: ifeq            59
        //    54: iconst_0       
        //    55: istore_2       
        //    56: goto            973
        //    59: aload           12
        //    61: ldc             "Step Into"
        //    63: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    66: ifeq            74
        //    69: iconst_1       
        //    70: istore_2       
        //    71: goto            56
        //    74: aload           12
        //    76: ldc             "Step Out"
        //    78: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    81: ifeq            89
        //    84: iconst_2       
        //    85: istore_2       
        //    86: goto            56
        //    89: aload           12
        //    91: ldc             "Go"
        //    93: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    96: ifeq            104
        //    99: iconst_3       
        //   100: istore_2       
        //   101: goto            56
        //   104: aload           12
        //   106: ldc             "Break"
        //   108: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   111: ifeq            126
        //   114: aload_0        
        //   115: getfield        org/mozilla/javascript/tools/debugger/SwingGui.dim:Lorg/mozilla/javascript/tools/debugger/Dim;
        //   118: invokevirtual   org/mozilla/javascript/tools/debugger/Dim.setBreak:()V
        //   121: iload_3        
        //   122: istore_2       
        //   123: goto            56
        //   126: aload           12
        //   128: ldc_w           "Exit"
        //   131: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   134: ifeq            146
        //   137: aload_0        
        //   138: invokespecial   org/mozilla/javascript/tools/debugger/SwingGui.exit:()V
        //   141: iload_3        
        //   142: istore_2       
        //   143: goto            56
        //   146: aload           12
        //   148: ldc_w           "Open"
        //   151: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   154: ifeq            222
        //   157: aload_0        
        //   158: ldc_w           "Select a file to compile"
        //   161: invokespecial   org/mozilla/javascript/tools/debugger/SwingGui.chooseFile:(Ljava/lang/String;)Ljava/lang/String;
        //   164: astore_1       
        //   165: aload_1        
        //   166: ifnull          217
        //   169: aload_0        
        //   170: aload_1        
        //   171: invokespecial   org/mozilla/javascript/tools/debugger/SwingGui.readFile:(Ljava/lang/String;)Ljava/lang/String;
        //   174: astore          12
        //   176: aload           12
        //   178: ifnull          217
        //   181: new             Lorg/mozilla/javascript/tools/debugger/RunProxy;
        //   184: dup            
        //   185: aload_0        
        //   186: iconst_1       
        //   187: invokespecial   org/mozilla/javascript/tools/debugger/RunProxy.<init>:(Lorg/mozilla/javascript/tools/debugger/SwingGui;I)V
        //   190: astore          13
        //   192: aload           13
        //   194: aload_1        
        //   195: putfield        org/mozilla/javascript/tools/debugger/RunProxy.fileName:Ljava/lang/String;
        //   198: aload           13
        //   200: aload           12
        //   202: putfield        org/mozilla/javascript/tools/debugger/RunProxy.text:Ljava/lang/String;
        //   205: new             Ljava/lang/Thread;
        //   208: dup            
        //   209: aload           13
        //   211: invokespecial   java/lang/Thread.<init>:(Ljava/lang/Runnable;)V
        //   214: invokevirtual   java/lang/Thread.start:()V
        //   217: iload_3        
        //   218: istore_2       
        //   219: goto            56
        //   222: aload           12
        //   224: ldc_w           "Load"
        //   227: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   230: ifeq            298
        //   233: aload_0        
        //   234: ldc_w           "Select a file to execute"
        //   237: invokespecial   org/mozilla/javascript/tools/debugger/SwingGui.chooseFile:(Ljava/lang/String;)Ljava/lang/String;
        //   240: astore_1       
        //   241: aload_1        
        //   242: ifnull          293
        //   245: aload_0        
        //   246: aload_1        
        //   247: invokespecial   org/mozilla/javascript/tools/debugger/SwingGui.readFile:(Ljava/lang/String;)Ljava/lang/String;
        //   250: astore          12
        //   252: aload           12
        //   254: ifnull          293
        //   257: new             Lorg/mozilla/javascript/tools/debugger/RunProxy;
        //   260: dup            
        //   261: aload_0        
        //   262: iconst_2       
        //   263: invokespecial   org/mozilla/javascript/tools/debugger/RunProxy.<init>:(Lorg/mozilla/javascript/tools/debugger/SwingGui;I)V
        //   266: astore          13
        //   268: aload           13
        //   270: aload_1        
        //   271: putfield        org/mozilla/javascript/tools/debugger/RunProxy.fileName:Ljava/lang/String;
        //   274: aload           13
        //   276: aload           12
        //   278: putfield        org/mozilla/javascript/tools/debugger/RunProxy.text:Ljava/lang/String;
        //   281: new             Ljava/lang/Thread;
        //   284: dup            
        //   285: aload           13
        //   287: invokespecial   java/lang/Thread.<init>:(Ljava/lang/Runnable;)V
        //   290: invokevirtual   java/lang/Thread.start:()V
        //   293: iload_3        
        //   294: istore_2       
        //   295: goto            56
        //   298: aload           12
        //   300: ldc_w           "More Windows..."
        //   303: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   306: ifeq            337
        //   309: new             Lorg/mozilla/javascript/tools/debugger/MoreWindows;
        //   312: dup            
        //   313: aload_0        
        //   314: aload_0        
        //   315: getfield        org/mozilla/javascript/tools/debugger/SwingGui.fileWindows:Ljava/util/Map;
        //   318: ldc_w           "Window"
        //   321: ldc_w           "Files"
        //   324: invokespecial   org/mozilla/javascript/tools/debugger/MoreWindows.<init>:(Lorg/mozilla/javascript/tools/debugger/SwingGui;Ljava/util/Map;Ljava/lang/String;Ljava/lang/String;)V
        //   327: aload_0        
        //   328: invokevirtual   org/mozilla/javascript/tools/debugger/MoreWindows.showDialog:(Ljava/awt/Component;)Ljava/lang/String;
        //   331: pop            
        //   332: iload_3        
        //   333: istore_2       
        //   334: goto            56
        //   337: aload           12
        //   339: ldc_w           "Console"
        //   342: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   345: ifeq            412
        //   348: aload_0        
        //   349: getfield        org/mozilla/javascript/tools/debugger/SwingGui.console:Lorg/mozilla/javascript/tools/debugger/JSInternalConsole;
        //   352: invokevirtual   org/mozilla/javascript/tools/debugger/JSInternalConsole.isIcon:()Z
        //   355: ifeq            374
        //   358: aload_0        
        //   359: getfield        org/mozilla/javascript/tools/debugger/SwingGui.desk:Ljavax/swing/JDesktopPane;
        //   362: invokevirtual   javax/swing/JDesktopPane.getDesktopManager:()Ljavax/swing/DesktopManager;
        //   365: aload_0        
        //   366: getfield        org/mozilla/javascript/tools/debugger/SwingGui.console:Lorg/mozilla/javascript/tools/debugger/JSInternalConsole;
        //   369: invokeinterface javax/swing/DesktopManager.deiconifyFrame:(Ljavax/swing/JInternalFrame;)V
        //   374: aload_0        
        //   375: getfield        org/mozilla/javascript/tools/debugger/SwingGui.console:Lorg/mozilla/javascript/tools/debugger/JSInternalConsole;
        //   378: invokevirtual   org/mozilla/javascript/tools/debugger/JSInternalConsole.show:()V
        //   381: aload_0        
        //   382: getfield        org/mozilla/javascript/tools/debugger/SwingGui.desk:Ljavax/swing/JDesktopPane;
        //   385: invokevirtual   javax/swing/JDesktopPane.getDesktopManager:()Ljavax/swing/DesktopManager;
        //   388: aload_0        
        //   389: getfield        org/mozilla/javascript/tools/debugger/SwingGui.console:Lorg/mozilla/javascript/tools/debugger/JSInternalConsole;
        //   392: invokeinterface javax/swing/DesktopManager.activateFrame:(Ljavax/swing/JInternalFrame;)V
        //   397: aload_0        
        //   398: getfield        org/mozilla/javascript/tools/debugger/SwingGui.console:Lorg/mozilla/javascript/tools/debugger/JSInternalConsole;
        //   401: getfield        org/mozilla/javascript/tools/debugger/JSInternalConsole.consoleTextArea:Lorg/mozilla/javascript/tools/shell/ConsoleTextArea;
        //   404: invokevirtual   org/mozilla/javascript/tools/shell/ConsoleTextArea.requestFocus:()V
        //   407: iload_3        
        //   408: istore_2       
        //   409: goto            56
        //   412: aload           12
        //   414: ldc_w           "Cut"
        //   417: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   420: ifeq            428
        //   423: iload_3        
        //   424: istore_2       
        //   425: goto            56
        //   428: aload           12
        //   430: ldc_w           "Copy"
        //   433: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   436: ifeq            442
        //   439: goto            423
        //   442: aload           12
        //   444: ldc_w           "Paste"
        //   447: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   450: ifeq            456
        //   453: goto            423
        //   456: aload           12
        //   458: ldc_w           "Go to function..."
        //   461: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   464: ifeq            491
        //   467: new             Lorg/mozilla/javascript/tools/debugger/FindFunction;
        //   470: dup            
        //   471: aload_0        
        //   472: ldc_w           "Go to function"
        //   475: ldc_w           "Function"
        //   478: invokespecial   org/mozilla/javascript/tools/debugger/FindFunction.<init>:(Lorg/mozilla/javascript/tools/debugger/SwingGui;Ljava/lang/String;Ljava/lang/String;)V
        //   481: aload_0        
        //   482: invokevirtual   org/mozilla/javascript/tools/debugger/FindFunction.showDialog:(Ljava/awt/Component;)Ljava/lang/String;
        //   485: pop            
        //   486: iload_3        
        //   487: istore_2       
        //   488: goto            56
        //   491: aload           12
        //   493: ldc_w           "Tile"
        //   496: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   499: ifeq            724
        //   502: aload_0        
        //   503: getfield        org/mozilla/javascript/tools/debugger/SwingGui.desk:Ljavax/swing/JDesktopPane;
        //   506: invokevirtual   javax/swing/JDesktopPane.getAllFrames:()[Ljavax/swing/JInternalFrame;
        //   509: astore_1       
        //   510: aload_1        
        //   511: arraylength    
        //   512: istore          7
        //   514: iload           7
        //   516: i2d            
        //   517: invokestatic    java/lang/Math.sqrt:(D)D
        //   520: d2i            
        //   521: istore_2       
        //   522: iload_2        
        //   523: istore          6
        //   525: iload_2        
        //   526: istore          5
        //   528: iload           6
        //   530: istore          4
        //   532: iload_2        
        //   533: iload           6
        //   535: imul           
        //   536: iload           7
        //   538: if_icmpge       572
        //   541: iload           6
        //   543: iconst_1       
        //   544: iadd           
        //   545: istore          6
        //   547: iload_2        
        //   548: istore          5
        //   550: iload           6
        //   552: istore          4
        //   554: iload_2        
        //   555: iload           6
        //   557: imul           
        //   558: iload           7
        //   560: if_icmpge       572
        //   563: iload_2        
        //   564: iconst_1       
        //   565: iadd           
        //   566: istore          5
        //   568: iload           6
        //   570: istore          4
        //   572: aload_0        
        //   573: getfield        org/mozilla/javascript/tools/debugger/SwingGui.desk:Ljavax/swing/JDesktopPane;
        //   576: invokevirtual   javax/swing/JDesktopPane.getSize:()Ljava/awt/Dimension;
        //   579: astore          12
        //   581: aload           12
        //   583: getfield        java/awt/Dimension.width:I
        //   586: iload           4
        //   588: idiv           
        //   589: istore          9
        //   591: aload           12
        //   593: getfield        java/awt/Dimension.height:I
        //   596: iload           5
        //   598: idiv           
        //   599: istore          10
        //   601: iconst_0       
        //   602: istore          6
        //   604: iconst_0       
        //   605: istore_2       
        //   606: iload_2        
        //   607: iload           5
        //   609: if_icmpge       719
        //   612: iconst_0       
        //   613: istore          8
        //   615: iconst_0       
        //   616: istore          7
        //   618: iload           7
        //   620: iload           4
        //   622: if_icmpge       705
        //   625: iload_2        
        //   626: iload           4
        //   628: imul           
        //   629: iload           7
        //   631: iadd           
        //   632: istore          11
        //   634: iload           11
        //   636: aload_1        
        //   637: arraylength    
        //   638: if_icmplt       644
        //   641: goto            705
        //   644: aload_1        
        //   645: iload           11
        //   647: aaload         
        //   648: astore          12
        //   650: aload           12
        //   652: iconst_0       
        //   653: invokevirtual   javax/swing/JInternalFrame.setIcon:(Z)V
        //   656: aload           12
        //   658: iconst_0       
        //   659: invokevirtual   javax/swing/JInternalFrame.setMaximum:(Z)V
        //   662: goto            667
        //   665: astore          13
        //   667: aload_0        
        //   668: getfield        org/mozilla/javascript/tools/debugger/SwingGui.desk:Ljavax/swing/JDesktopPane;
        //   671: invokevirtual   javax/swing/JDesktopPane.getDesktopManager:()Ljavax/swing/DesktopManager;
        //   674: aload           12
        //   676: iload           8
        //   678: iload           6
        //   680: iload           9
        //   682: iload           10
        //   684: invokeinterface javax/swing/DesktopManager.setBoundsForFrame:(Ljavax/swing/JComponent;IIII)V
        //   689: iload           8
        //   691: iload           9
        //   693: iadd           
        //   694: istore          8
        //   696: iload           7
        //   698: iconst_1       
        //   699: iadd           
        //   700: istore          7
        //   702: goto            618
        //   705: iload           6
        //   707: iload           10
        //   709: iadd           
        //   710: istore          6
        //   712: iload_2        
        //   713: iconst_1       
        //   714: iadd           
        //   715: istore_2       
        //   716: goto            606
        //   719: iload_3        
        //   720: istore_2       
        //   721: goto            56
        //   724: aload           12
        //   726: ldc_w           "Cascade"
        //   729: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   732: ifeq            881
        //   735: aload_0        
        //   736: getfield        org/mozilla/javascript/tools/debugger/SwingGui.desk:Ljavax/swing/JDesktopPane;
        //   739: invokevirtual   javax/swing/JDesktopPane.getAllFrames:()[Ljavax/swing/JInternalFrame;
        //   742: astore_1       
        //   743: aload_1        
        //   744: arraylength    
        //   745: istore          7
        //   747: iconst_0       
        //   748: istore          4
        //   750: iconst_0       
        //   751: istore          5
        //   753: aload_0        
        //   754: getfield        org/mozilla/javascript/tools/debugger/SwingGui.desk:Ljavax/swing/JDesktopPane;
        //   757: invokevirtual   javax/swing/JDesktopPane.getHeight:()I
        //   760: iload           7
        //   762: idiv           
        //   763: istore          6
        //   765: iload           6
        //   767: istore_2       
        //   768: iload           6
        //   770: bipush          30
        //   772: if_icmple       778
        //   775: bipush          30
        //   777: istore_2       
        //   778: iload           7
        //   780: iconst_1       
        //   781: isub           
        //   782: istore          6
        //   784: iload           6
        //   786: iflt            876
        //   789: aload_1        
        //   790: iload           6
        //   792: aaload         
        //   793: astore          12
        //   795: aload           12
        //   797: iconst_0       
        //   798: invokevirtual   javax/swing/JInternalFrame.setIcon:(Z)V
        //   801: aload           12
        //   803: iconst_0       
        //   804: invokevirtual   javax/swing/JInternalFrame.setMaximum:(Z)V
        //   807: goto            812
        //   810: astore          13
        //   812: aload           12
        //   814: invokevirtual   javax/swing/JInternalFrame.getPreferredSize:()Ljava/awt/Dimension;
        //   817: astore          13
        //   819: aload           13
        //   821: getfield        java/awt/Dimension.width:I
        //   824: istore          7
        //   826: aload           13
        //   828: getfield        java/awt/Dimension.height:I
        //   831: istore          8
        //   833: aload_0        
        //   834: getfield        org/mozilla/javascript/tools/debugger/SwingGui.desk:Ljavax/swing/JDesktopPane;
        //   837: invokevirtual   javax/swing/JDesktopPane.getDesktopManager:()Ljavax/swing/DesktopManager;
        //   840: aload           12
        //   842: iload           5
        //   844: iload           4
        //   846: iload           7
        //   848: iload           8
        //   850: invokeinterface javax/swing/DesktopManager.setBoundsForFrame:(Ljavax/swing/JComponent;IIII)V
        //   855: iload           6
        //   857: iconst_1       
        //   858: isub           
        //   859: istore          6
        //   861: iload           5
        //   863: iload_2        
        //   864: iadd           
        //   865: istore          5
        //   867: iload           4
        //   869: iload_2        
        //   870: iadd           
        //   871: istore          4
        //   873: goto            784
        //   876: iload_3        
        //   877: istore_2       
        //   878: goto            56
        //   881: aload_0        
        //   882: aload           12
        //   884: invokevirtual   org/mozilla/javascript/tools/debugger/SwingGui.getFileWindow:(Ljava/lang/String;)Lorg/mozilla/javascript/tools/debugger/FileWindow;
        //   887: astore_1       
        //   888: iload_3        
        //   889: istore_2       
        //   890: aload_1        
        //   891: ifnull          56
        //   894: aload_1        
        //   895: checkcast       Lorg/mozilla/javascript/tools/debugger/FileWindow;
        //   898: astore_1       
        //   899: aload_1        
        //   900: invokevirtual   org/mozilla/javascript/tools/debugger/FileWindow.isIcon:()Z
        //   903: ifeq            911
        //   906: aload_1        
        //   907: iconst_0       
        //   908: invokevirtual   org/mozilla/javascript/tools/debugger/FileWindow.setIcon:(Z)V
        //   911: aload_1        
        //   912: iconst_1       
        //   913: invokevirtual   org/mozilla/javascript/tools/debugger/FileWindow.setVisible:(Z)V
        //   916: aload_1        
        //   917: invokevirtual   org/mozilla/javascript/tools/debugger/FileWindow.moveToFront:()V
        //   920: aload_1        
        //   921: iconst_1       
        //   922: invokevirtual   org/mozilla/javascript/tools/debugger/FileWindow.setSelected:(Z)V
        //   925: iload_3        
        //   926: istore_2       
        //   927: goto            56
        //   930: astore_1       
        //   931: iload_3        
        //   932: istore_2       
        //   933: goto            56
        //   936: aload_0        
        //   937: invokespecial   org/mozilla/javascript/tools/debugger/SwingGui.getSelectedFrame:()Ljavax/swing/JInternalFrame;
        //   940: astore          12
        //   942: aload           12
        //   944: ifnull          971
        //   947: aload           12
        //   949: instanceof      Ljava/awt/event/ActionListener;
        //   952: ifeq            971
        //   955: aload           12
        //   957: checkcast       Ljava/awt/event/ActionListener;
        //   960: aload_1        
        //   961: invokeinterface java/awt/event/ActionListener.actionPerformed:(Ljava/awt/event/ActionEvent;)V
        //   966: iload_3        
        //   967: istore_2       
        //   968: goto            973
        //   971: iload_3        
        //   972: istore_2       
        //   973: iload_2        
        //   974: iconst_m1      
        //   975: if_icmpeq       991
        //   978: aload_0        
        //   979: iconst_0       
        //   980: invokespecial   org/mozilla/javascript/tools/debugger/SwingGui.updateEnabled:(Z)V
        //   983: aload_0        
        //   984: getfield        org/mozilla/javascript/tools/debugger/SwingGui.dim:Lorg/mozilla/javascript/tools/debugger/Dim;
        //   987: iload_2        
        //   988: invokevirtual   org/mozilla/javascript/tools/debugger/Dim.setReturnValue:(I)V
        //   991: return         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  650    662    665    667    Ljava/lang/Exception;
        //  795    807    810    812    Ljava/lang/Exception;
        //  899    911    930    936    Ljava/lang/Exception;
        //  911    925    930    936    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    void addTopLevel(final String s, final JFrame frame) {
        if (frame != this) {
            this.toplevels.put(s, frame);
        }
    }
    
    protected void createFileWindow(Dim.SourceInfo currentWindow, final int n) {
        final String url = currentWindow.url();
        currentWindow = (Dim.SourceInfo)new FileWindow(this, currentWindow);
        this.fileWindows.put(url, (FileWindow)currentWindow);
        if (n != -1) {
            if (this.currentWindow != null) {
                this.currentWindow.setPosition(-1);
            }
            try {
                ((FileWindow)currentWindow).setPosition(((FileWindow)currentWindow).textArea.getLineStartOffset(n - 1));
            }
            catch (BadLocationException ex) {
                try {
                    ((FileWindow)currentWindow).setPosition(((FileWindow)currentWindow).textArea.getLineStartOffset(0));
                }
                catch (BadLocationException ex2) {
                    ((FileWindow)currentWindow).setPosition(-1);
                }
            }
        }
        this.desk.add((Component)currentWindow);
        if (n != -1) {
            this.currentWindow = (FileWindow)currentWindow;
        }
        this.menubar.addFile(url);
        ((JComponent)currentWindow).setVisible(true);
        if (true) {
            try {
                ((JInternalFrame)currentWindow).setMaximum(true);
                ((JInternalFrame)currentWindow).setSelected(true);
                ((JInternalFrame)currentWindow).moveToFront();
            }
            catch (Exception ex3) {}
        }
    }
    
    @Override
    public void dispatchNextGuiEvent() throws InterruptedException {
        EventQueue awtEventQueue;
        if ((awtEventQueue = this.awtEventQueue) == null) {
            awtEventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
            this.awtEventQueue = awtEventQueue;
        }
        final AWTEvent nextEvent = awtEventQueue.getNextEvent();
        if (nextEvent instanceof ActiveEvent) {
            ((ActiveEvent)nextEvent).dispatch();
            return;
        }
        final Object source = nextEvent.getSource();
        if (source instanceof Component) {
            ((Component)source).dispatchEvent(nextEvent);
            return;
        }
        if (source instanceof MenuComponent) {
            ((MenuComponent)source).dispatchEvent(nextEvent);
        }
    }
    
    @Override
    public void enterInterrupt(final Dim.StackFrame lastFrame, final String threadTitle, final String alertMessage) {
        if (SwingUtilities.isEventDispatchThread()) {
            this.enterInterruptImpl(lastFrame, threadTitle, alertMessage);
            return;
        }
        final RunProxy runProxy = new RunProxy(this, 4);
        runProxy.lastFrame = lastFrame;
        runProxy.threadTitle = threadTitle;
        runProxy.alertMessage = alertMessage;
        SwingUtilities.invokeLater(runProxy);
    }
    
    void enterInterruptImpl(Dim.StackFrame frame, String url, final String s) {
        final JLabel statusBar = this.statusBar;
        final StringBuilder sb = new StringBuilder();
        sb.append("Thread: ");
        sb.append(url);
        statusBar.setText(sb.toString());
        this.showStopLine(frame);
        if (s != null) {
            MessageDialogWrapper.showMessageDialog(this, s, "Exception in Script", 0);
        }
        this.updateEnabled(true);
        final Dim.ContextData contextData = frame.contextData();
        final JComboBox context = this.context.context;
        final List<String> toolTips = this.context.toolTips;
        this.context.disableUpdate();
        final int frameCount = contextData.frameCount();
        context.removeAllItems();
        context.setSelectedItem(null);
        toolTips.clear();
        for (int i = 0; i < frameCount; ++i) {
            frame = contextData.getFrame(i);
            url = frame.getUrl();
            final int lineNumber = frame.getLineNumber();
            String string = url;
            if (url.length() > 20) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("...");
                sb2.append(url.substring(url.length() - 17));
                string = sb2.toString();
            }
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("\"");
            sb3.append(string);
            sb3.append("\", line ");
            sb3.append(lineNumber);
            context.insertItemAt(sb3.toString(), i);
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("\"");
            sb4.append(url);
            sb4.append("\", line ");
            sb4.append(lineNumber);
            toolTips.add(sb4.toString());
        }
        this.context.enableUpdate();
        context.setSelectedIndex(0);
        context.setMinimumSize(new Dimension(50, context.getMinimumSize().height));
    }
    
    public JSInternalConsole getConsole() {
        return this.console;
    }
    
    FileWindow getFileWindow(final String s) {
        if (s != null && !s.equals("<stdin>")) {
            return this.fileWindows.get(s);
        }
        return null;
    }
    
    public Menubar getMenubar() {
        return this.menubar;
    }
    
    @Override
    public boolean isGuiEventThread() {
        return SwingUtilities.isEventDispatchThread();
    }
    
    void removeWindow(final FileWindow fileWindow) {
        this.fileWindows.remove(fileWindow.getUrl());
        final JMenu windowMenu = this.getWindowMenu();
        final int itemCount = windowMenu.getItemCount();
        final JMenuItem item = windowMenu.getItem(itemCount - 1);
        final String shortName = getShortName(fileWindow.getUrl());
        for (int i = 5; i < itemCount; ++i) {
            final JMenuItem item2 = windowMenu.getItem(i);
            if (item2 != null) {
                final String text = item2.getText();
                if (text.substring(text.indexOf(32) + 1).equals(shortName)) {
                    windowMenu.remove(item2);
                    if (itemCount == 6) {
                        windowMenu.remove(4);
                        break;
                    }
                    int n = i - 4;
                    while (i < itemCount - 1) {
                        final JMenuItem item3 = windowMenu.getItem(i);
                        int n2 = n;
                        if (item3 != null) {
                            final String text2 = item3.getText();
                            if (text2.equals("More Windows...")) {
                                break;
                            }
                            final int index = text2.indexOf(32);
                            final StringBuilder sb = new StringBuilder();
                            sb.append((char)(n + 48));
                            sb.append(" ");
                            sb.append(text2.substring(index + 1));
                            item3.setText(sb.toString());
                            item3.setMnemonic(n + 48);
                            n2 = n + 1;
                        }
                        ++i;
                        n = n2;
                    }
                    if (itemCount - 6 == 0 && item != item2 && item.getText().equals("More Windows...")) {
                        windowMenu.remove(item);
                    }
                    break;
                }
            }
        }
        windowMenu.revalidate();
    }
    
    public void setExitAction(final Runnable exitAction) {
        this.exitAction = exitAction;
    }
    
    @Override
    public void setVisible(final boolean visible) {
        super.setVisible(visible);
        if (visible) {
            this.console.consoleTextArea.requestFocus();
            this.context.split.setDividerLocation(0.5);
            try {
                this.console.setMaximum(true);
                this.console.setSelected(true);
                this.console.show();
                this.console.consoleTextArea.requestFocus();
            }
            catch (Exception ex) {}
        }
    }
    
    protected void showFileWindow(final String s, int position) {
        FileWindow fileWindow;
        if ((fileWindow = this.getFileWindow(s)) == null) {
            this.createFileWindow(this.dim.sourceInfo(s), -1);
            fileWindow = this.getFileWindow(s);
        }
        if (position > -1) {
            final int position2 = fileWindow.getPosition(position - 1);
            position = fileWindow.getPosition(position);
            fileWindow.textArea.select(position2);
            fileWindow.textArea.setCaretPosition(position2);
            fileWindow.textArea.moveCaretPosition(position - 1);
        }
        try {
            if (fileWindow.isIcon()) {
                fileWindow.setIcon(false);
            }
            fileWindow.setVisible(true);
            fileWindow.moveToFront();
            fileWindow.setSelected(true);
            this.requestFocus();
            fileWindow.requestFocus();
            fileWindow.textArea.requestFocus();
        }
        catch (Exception ex) {}
    }
    
    void showStopLine(final Dim.StackFrame stackFrame) {
        final String url = stackFrame.getUrl();
        if (url != null && !url.equals("<stdin>")) {
            this.showFileWindow(url, -1);
            final int lineNumber = stackFrame.getLineNumber();
            final FileWindow fileWindow = this.getFileWindow(url);
            if (fileWindow != null) {
                this.setFilePosition(fileWindow, lineNumber);
            }
        }
        else if (this.console.isVisible()) {
            this.console.show();
        }
    }
    
    protected boolean updateFileWindow(final Dim.SourceInfo sourceInfo) {
        final FileWindow fileWindow = this.getFileWindow(sourceInfo.url());
        if (fileWindow != null) {
            fileWindow.updateText(sourceInfo);
            fileWindow.show();
            return true;
        }
        return false;
    }
    
    @Override
    public void updateSourceText(final Dim.SourceInfo sourceInfo) {
        final RunProxy runProxy = new RunProxy(this, 3);
        runProxy.sourceInfo = sourceInfo;
        SwingUtilities.invokeLater(runProxy);
    }
}
