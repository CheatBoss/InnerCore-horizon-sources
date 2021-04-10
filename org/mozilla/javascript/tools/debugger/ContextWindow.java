package org.mozilla.javascript.tools.debugger;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import org.mozilla.javascript.tools.debugger.treetable.*;

class ContextWindow extends JPanel implements ActionListener
{
    private static final long serialVersionUID = 2306040975490228051L;
    private EvalTextArea cmdLine;
    JComboBox context;
    private SwingGui debugGui;
    private boolean enabled;
    private Evaluator evaluator;
    private MyTreeTable localsTable;
    JSplitPane split;
    private MyTableModel tableModel;
    private JTabbedPane tabs;
    private JTabbedPane tabs2;
    private MyTreeTable thisTable;
    List<String> toolTips;
    
    public ContextWindow(final SwingGui debugGui) {
        this.debugGui = debugGui;
        this.enabled = false;
        final JPanel panel = new JPanel();
        final JToolBar toolBar = new JToolBar();
        toolBar.setName("Variables");
        toolBar.setLayout(new GridLayout());
        toolBar.add(panel);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayout());
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayout());
        panel2.add(toolBar);
        final JLabel label = new JLabel("Context:");
        (this.context = new JComboBox()).setLightWeightPopupEnabled(false);
        this.toolTips = Collections.synchronizedList(new ArrayList<String>());
        label.setBorder(this.context.getBorder());
        this.context.addActionListener(this);
        this.context.setActionCommand("ContextSwitch");
        final GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);
        final GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets.left = 5;
        gridBagConstraints.anchor = 17;
        gridBagConstraints.ipadx = 5;
        layout.setConstraints(label, gridBagConstraints);
        panel.add(label);
        final GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridwidth = 0;
        gridBagConstraints2.fill = 2;
        gridBagConstraints2.anchor = 17;
        layout.setConstraints(this.context, gridBagConstraints2);
        panel.add(this.context);
        (this.tabs = new JTabbedPane(3)).setPreferredSize(new Dimension(500, 300));
        this.thisTable = new MyTreeTable(new VariableModel());
        final JScrollPane scrollPane = new JScrollPane(this.thisTable);
        scrollPane.getViewport().setViewSize(new Dimension(5, 2));
        this.tabs.add("this", scrollPane);
        (this.localsTable = new MyTreeTable(new VariableModel())).setAutoResizeMode(4);
        this.localsTable.setPreferredSize(null);
        this.tabs.add("Locals", new JScrollPane(this.localsTable));
        gridBagConstraints2.weighty = 1.0;
        gridBagConstraints2.weightx = 1.0;
        gridBagConstraints2.gridheight = 0;
        gridBagConstraints2.fill = 1;
        gridBagConstraints2.anchor = 17;
        layout.setConstraints(this.tabs, gridBagConstraints2);
        panel.add(this.tabs);
        this.evaluator = new Evaluator(debugGui);
        this.cmdLine = new EvalTextArea(debugGui);
        this.tableModel = this.evaluator.tableModel;
        final JScrollPane scrollPane2 = new JScrollPane(this.evaluator);
        final JToolBar toolBar2 = new JToolBar();
        toolBar2.setName("Evaluate");
        (this.tabs2 = new JTabbedPane(3)).add("Watch", scrollPane2);
        this.tabs2.add("Evaluate", new JScrollPane(this.cmdLine));
        this.tabs2.setPreferredSize(new Dimension(500, 300));
        toolBar2.setLayout(new GridLayout());
        toolBar2.add(this.tabs2);
        panel3.add(toolBar2);
        this.evaluator.setAutoResizeMode(4);
        (this.split = new JSplitPane(1, panel2, panel3)).setOneTouchExpandable(true);
        SwingGui.setResizeWeight(this.split, 0.5);
        this.setLayout(new BorderLayout());
        this.add(this.split, "Center");
        final JSplitPane split = this.split;
        final ComponentListener componentListener = new ComponentListener() {
            boolean t2Docked = true;
            final /* synthetic */ JPanel val$finalThis;
            
            void check(final Component component) {
                final Container parent = this.val$finalThis.getParent();
                if (parent == null) {
                    return;
                }
                Container container = toolBar.getParent();
                boolean b = true;
                boolean t2Docked = true;
                if (container != null) {
                    if (container != panel2) {
                        while (!(container instanceof JFrame)) {
                            container = container.getParent();
                        }
                        final JFrame frame = (JFrame)container;
                        debugGui.addTopLevel("Variables", frame);
                        if (!frame.isResizable()) {
                            frame.setResizable(true);
                            frame.setDefaultCloseOperation(0);
                            final WindowListener[] array = frame.getListeners(WindowListener.class);
                            frame.removeWindowListener(array[0]);
                            frame.addWindowListener(new WindowAdapter() {
                                @Override
                                public void windowClosing(final WindowEvent windowEvent) {
                                    ContextWindow.this.context.hidePopup();
                                    array[0].windowClosing(windowEvent);
                                }
                            });
                        }
                        b = false;
                    }
                    else {
                        b = true;
                    }
                }
                Container container2 = toolBar2.getParent();
                if (container2 != null) {
                    if (container2 != panel3) {
                        while (!(container2 instanceof JFrame)) {
                            container2 = container2.getParent();
                        }
                        final JFrame frame2 = (JFrame)container2;
                        debugGui.addTopLevel("Evaluate", frame2);
                        frame2.setResizable(true);
                        t2Docked = false;
                    }
                    else {
                        t2Docked = true;
                    }
                }
                if (b && this.t2Docked && t2Docked && this.t2Docked) {
                    return;
                }
                this.t2Docked = t2Docked;
                final JSplitPane splitPane = (JSplitPane)parent;
                if (b) {
                    if (t2Docked) {
                        split.setDividerLocation(0.5);
                    }
                    else {
                        split.setDividerLocation(1.0);
                    }
                    if (false) {
                        splitPane.setDividerLocation(0.66);
                    }
                }
                else {
                    if (t2Docked) {
                        split.setDividerLocation(0.0);
                        splitPane.setDividerLocation(0.66);
                        return;
                    }
                    splitPane.setDividerLocation(1.0);
                }
            }
            
            @Override
            public void componentHidden(final ComponentEvent componentEvent) {
                this.check(componentEvent.getComponent());
            }
            
            @Override
            public void componentMoved(final ComponentEvent componentEvent) {
                this.check(componentEvent.getComponent());
            }
            
            @Override
            public void componentResized(final ComponentEvent componentEvent) {
                this.check(componentEvent.getComponent());
            }
            
            @Override
            public void componentShown(final ComponentEvent componentEvent) {
                this.check(componentEvent.getComponent());
            }
        };
        panel2.addContainerListener(new ContainerListener() {
            final /* synthetic */ JPanel val$finalThis;
            
            @Override
            public void componentAdded(final ContainerEvent containerEvent) {
                final JSplitPane splitPane = (JSplitPane)this.val$finalThis.getParent();
                if (containerEvent.getChild() == toolBar) {
                    if (toolBar2.getParent() == panel3) {
                        split.setDividerLocation(0.5);
                    }
                    else {
                        split.setDividerLocation(1.0);
                    }
                    splitPane.setDividerLocation(0.66);
                }
            }
            
            @Override
            public void componentRemoved(final ContainerEvent containerEvent) {
                final JSplitPane splitPane = (JSplitPane)this.val$finalThis.getParent();
                if (containerEvent.getChild() == toolBar) {
                    if (toolBar2.getParent() == panel3) {
                        split.setDividerLocation(0.0);
                        splitPane.setDividerLocation(0.66);
                        return;
                    }
                    splitPane.setDividerLocation(1.0);
                }
            }
        });
        toolBar.addComponentListener(componentListener);
        toolBar2.addComponentListener(componentListener);
        this.setEnabled(false);
    }
    
    @Override
    public void actionPerformed(final ActionEvent actionEvent) {
        if (!this.enabled) {
            return;
        }
        if (actionEvent.getActionCommand().equals("ContextSwitch")) {
            final Dim.ContextData currentContextData = this.debugGui.dim.currentContextData();
            if (currentContextData == null) {
                return;
            }
            final int selectedIndex = this.context.getSelectedIndex();
            this.context.setToolTipText(this.toolTips.get(selectedIndex));
            if (selectedIndex >= currentContextData.frameCount()) {
                return;
            }
            final Dim.StackFrame frame = currentContextData.getFrame(selectedIndex);
            final Object scope = frame.scope();
            final Object thisObj = frame.thisObj();
            this.thisTable.resetTree(new VariableModel(this.debugGui.dim, thisObj));
            VariableModel variableModel;
            if (scope != thisObj) {
                variableModel = new VariableModel(this.debugGui.dim, scope);
            }
            else {
                variableModel = new VariableModel();
            }
            this.localsTable.resetTree(variableModel);
            this.debugGui.dim.contextSwitch(selectedIndex);
            this.debugGui.showStopLine(frame);
            this.tableModel.updateModel();
        }
    }
    
    public void disableUpdate() {
        this.enabled = false;
    }
    
    public void enableUpdate() {
        this.enabled = true;
    }
    
    @Override
    public void setEnabled(final boolean enabled) {
        this.context.setEnabled(enabled);
        this.thisTable.setEnabled(enabled);
        this.localsTable.setEnabled(enabled);
        this.evaluator.setEnabled(enabled);
        this.cmdLine.setEnabled(enabled);
    }
}
