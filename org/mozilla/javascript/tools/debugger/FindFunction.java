package org.mozilla.javascript.tools.debugger;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class FindFunction extends JDialog implements ActionListener
{
    private static final long serialVersionUID = 559491015232880916L;
    private JButton cancelButton;
    private SwingGui debugGui;
    private JList list;
    private JButton setButton;
    private String value;
    
    public FindFunction(final SwingGui debugGui, final String s, final String s2) {
        super(debugGui, s, true);
        this.debugGui = debugGui;
        this.cancelButton = new JButton("Cancel");
        this.setButton = new JButton("Select");
        this.cancelButton.addActionListener(this);
        this.setButton.addActionListener(this);
        this.getRootPane().setDefaultButton(this.setButton);
        this.list = new JList((ListModel<E>)new DefaultListModel<Object>());
        final DefaultListModel defaultListModel = (DefaultListModel)this.list.getModel();
        defaultListModel.clear();
        final String[] functionNames = debugGui.dim.functionNames();
        Arrays.sort(functionNames);
        for (int i = 0; i < functionNames.length; ++i) {
            defaultListModel.addElement(functionNames[i]);
        }
        this.list.setSelectedIndex(0);
        this.setButton.setEnabled(functionNames.length > 0);
        this.list.setSelectionMode(1);
        this.list.addMouseListener(new MouseHandler());
        final JScrollPane scrollPane = new JScrollPane(this.list);
        scrollPane.setPreferredSize(new Dimension(320, 240));
        scrollPane.setMinimumSize(new Dimension(250, 80));
        scrollPane.setAlignmentX(0.0f);
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, 1));
        final JLabel label = new JLabel(s2);
        label.setLabelFor(this.list);
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(scrollPane);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, 0));
        panel2.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        panel2.add(Box.createHorizontalGlue());
        panel2.add(this.cancelButton);
        panel2.add(Box.createRigidArea(new Dimension(10, 0)));
        panel2.add(this.setButton);
        final Container contentPane = this.getContentPane();
        contentPane.add(panel, "Center");
        contentPane.add(panel2, "South");
        this.pack();
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == 27) {
                    keyEvent.consume();
                    FindFunction.this.value = null;
                    FindFunction.this.setVisible(false);
                }
            }
        });
    }
    
    @Override
    public void actionPerformed(final ActionEvent actionEvent) {
        final String actionCommand = actionEvent.getActionCommand();
        if (actionCommand.equals("Cancel")) {
            this.setVisible(false);
            this.value = null;
            return;
        }
        if (actionCommand.equals("Select")) {
            if (this.list.getSelectedIndex() < 0) {
                return;
            }
            try {
                this.value = this.list.getSelectedValue();
                this.setVisible(false);
                final Dim.FunctionSource functionSourceByName = this.debugGui.dim.functionSourceByName(this.value);
                if (functionSourceByName != null) {
                    this.debugGui.showFileWindow(functionSourceByName.sourceInfo().url(), functionSourceByName.firstLine());
                }
            }
            catch (ArrayIndexOutOfBoundsException ex) {}
        }
    }
    
    public String showDialog(final Component locationRelativeTo) {
        this.value = null;
        this.setLocationRelativeTo(locationRelativeTo);
        this.setVisible(true);
        return this.value;
    }
    
    class MouseHandler extends MouseAdapter
    {
        @Override
        public void mouseClicked(final MouseEvent mouseEvent) {
            if (mouseEvent.getClickCount() == 2) {
                FindFunction.this.setButton.doClick();
            }
        }
    }
}
