package org.mozilla.javascript.tools.debugger;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

class MoreWindows extends JDialog implements ActionListener
{
    private static final long serialVersionUID = 5177066296457377546L;
    private JButton cancelButton;
    private JList list;
    private JButton setButton;
    private SwingGui swingGui;
    private String value;
    
    MoreWindows(final SwingGui swingGui, final Map<String, FileWindow> map, final String s, final String s2) {
        super(swingGui, s, true);
        this.swingGui = swingGui;
        this.cancelButton = new JButton("Cancel");
        this.setButton = new JButton("Select");
        this.cancelButton.addActionListener(this);
        this.setButton.addActionListener(this);
        this.getRootPane().setDefaultButton(this.setButton);
        this.list = new JList((ListModel<E>)new DefaultListModel<Object>());
        final DefaultListModel defaultListModel = (DefaultListModel)this.list.getModel();
        defaultListModel.clear();
        final Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            defaultListModel.addElement(iterator.next());
        }
        this.list.setSelectedIndex(0);
        this.setButton.setEnabled(true);
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
                    MoreWindows.this.value = null;
                    MoreWindows.this.setVisible(false);
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
            this.value = this.list.getSelectedValue();
            this.setVisible(false);
            this.swingGui.showFileWindow(this.value, -1);
        }
    }
    
    public String showDialog(final Component locationRelativeTo) {
        this.value = null;
        this.setLocationRelativeTo(locationRelativeTo);
        this.setVisible(true);
        return this.value;
    }
    
    private class MouseHandler extends MouseAdapter
    {
        @Override
        public void mouseClicked(final MouseEvent mouseEvent) {
            if (mouseEvent.getClickCount() == 2) {
                MoreWindows.this.setButton.doClick();
            }
        }
    }
}
