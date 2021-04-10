package org.mozilla.javascript.tools.debugger.treetable;

import javax.swing.table.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;

public class JTreeTable extends JTable
{
    private static final long serialVersionUID = -2103973006456695515L;
    protected TreeTableCellRenderer tree;
    
    public JTreeTable(final TreeTableModel treeTableModel) {
        this.tree = new TreeTableCellRenderer(treeTableModel);
        super.setModel(new TreeTableModelAdapter(treeTableModel, this.tree));
        final ListToTreeSelectionModelWrapper selectionModel = new ListToTreeSelectionModelWrapper();
        this.tree.setSelectionModel(selectionModel);
        this.setSelectionModel(selectionModel.getListSelectionModel());
        this.setDefaultRenderer(TreeTableModel.class, this.tree);
        this.setDefaultEditor(TreeTableModel.class, new TreeTableCellEditor());
        this.setShowGrid(false);
        this.setIntercellSpacing(new Dimension(0, 0));
        if (this.tree.getRowHeight() < 1) {
            this.setRowHeight(18);
        }
    }
    
    @Override
    public int getEditingRow() {
        if (this.getColumnClass(this.editingColumn) == TreeTableModel.class) {
            return -1;
        }
        return this.editingRow;
    }
    
    public JTree getTree() {
        return this.tree;
    }
    
    @Override
    public void setRowHeight(final int rowHeight) {
        super.setRowHeight(rowHeight);
        if (this.tree != null && this.tree.getRowHeight() != rowHeight) {
            this.tree.setRowHeight(this.getRowHeight());
        }
    }
    
    @Override
    public void updateUI() {
        super.updateUI();
        if (this.tree != null) {
            this.tree.updateUI();
        }
        LookAndFeel.installColorsAndFont(this, "Tree.background", "Tree.foreground", "Tree.font");
    }
    
    public class ListToTreeSelectionModelWrapper extends DefaultTreeSelectionModel
    {
        private static final long serialVersionUID = 8168140829623071131L;
        protected boolean updatingListSelectionModel;
        
        public ListToTreeSelectionModelWrapper() {
            this.getListSelectionModel().addListSelectionListener(this.createListSelectionListener());
        }
        
        protected ListSelectionListener createListSelectionListener() {
            return new ListSelectionHandler();
        }
        
        public ListSelectionModel getListSelectionModel() {
            return this.listSelectionModel;
        }
        
        @Override
        public void resetRowSelection() {
            if (!this.updatingListSelectionModel) {
                this.updatingListSelectionModel = true;
                try {
                    super.resetRowSelection();
                }
                finally {
                    this.updatingListSelectionModel = false;
                }
            }
        }
        
        protected void updateSelectedPathsFromSelectedRows() {
            if (!this.updatingListSelectionModel) {
                this.updatingListSelectionModel = true;
                try {
                    int i = this.listSelectionModel.getMinSelectionIndex();
                    final int maxSelectionIndex = this.listSelectionModel.getMaxSelectionIndex();
                    this.clearSelection();
                    if (i != -1 && maxSelectionIndex != -1) {
                        while (i <= maxSelectionIndex) {
                            if (this.listSelectionModel.isSelectedIndex(i)) {
                                final TreePath pathForRow = JTreeTable.this.tree.getPathForRow(i);
                                if (pathForRow != null) {
                                    this.addSelectionPath(pathForRow);
                                }
                            }
                            ++i;
                        }
                    }
                }
                finally {
                    this.updatingListSelectionModel = false;
                }
            }
        }
        
        class ListSelectionHandler implements ListSelectionListener
        {
            @Override
            public void valueChanged(final ListSelectionEvent listSelectionEvent) {
                ListToTreeSelectionModelWrapper.this.updateSelectedPathsFromSelectedRows();
            }
        }
    }
    
    public class TreeTableCellEditor extends AbstractCellEditor implements TableCellEditor
    {
        @Override
        public Component getTableCellEditorComponent(final JTable table, final Object o, final boolean b, final int n, final int n2) {
            return JTreeTable.this.tree;
        }
        
        @Override
        public boolean isCellEditable(final EventObject eventObject) {
            if (eventObject instanceof MouseEvent) {
                for (int i = JTreeTable.this.getColumnCount() - 1; i >= 0; --i) {
                    if (JTreeTable.this.getColumnClass(i) == TreeTableModel.class) {
                        final MouseEvent mouseEvent = (MouseEvent)eventObject;
                        JTreeTable.this.tree.dispatchEvent(new MouseEvent(JTreeTable.this.tree, mouseEvent.getID(), mouseEvent.getWhen(), mouseEvent.getModifiers(), mouseEvent.getX() - JTreeTable.this.getCellRect(0, i, true).x, mouseEvent.getY(), mouseEvent.getClickCount(), mouseEvent.isPopupTrigger()));
                        return false;
                    }
                }
            }
            return false;
        }
    }
    
    public class TreeTableCellRenderer extends JTree implements TableCellRenderer
    {
        private static final long serialVersionUID = -193867880014600717L;
        protected int visibleRow;
        
        public TreeTableCellRenderer(final TreeModel treeModel) {
            super(treeModel);
        }
        
        @Override
        public Component getTableCellRendererComponent(final JTable table, final Object o, final boolean b, final boolean b2, final int visibleRow, final int n) {
            if (b) {
                this.setBackground(table.getSelectionBackground());
            }
            else {
                this.setBackground(table.getBackground());
            }
            this.visibleRow = visibleRow;
            return this;
        }
        
        @Override
        public void paint(final Graphics graphics) {
            graphics.translate(0, -this.visibleRow * this.getRowHeight());
            super.paint(graphics);
        }
        
        @Override
        public void setBounds(final int n, final int n2, final int n3, final int n4) {
            super.setBounds(n, 0, n3, JTreeTable.this.getHeight());
        }
        
        @Override
        public void setRowHeight(final int rowHeight) {
            if (rowHeight > 0) {
                super.setRowHeight(rowHeight);
                if (JTreeTable.this != null && JTreeTable.this.getRowHeight() != rowHeight) {
                    JTreeTable.this.setRowHeight(this.getRowHeight());
                }
            }
        }
        
        @Override
        public void updateUI() {
            super.updateUI();
            final TreeCellRenderer cellRenderer = this.getCellRenderer();
            if (cellRenderer instanceof DefaultTreeCellRenderer) {
                final DefaultTreeCellRenderer defaultTreeCellRenderer = (DefaultTreeCellRenderer)cellRenderer;
                defaultTreeCellRenderer.setTextSelectionColor(UIManager.getColor("Table.selectionForeground"));
                defaultTreeCellRenderer.setBackgroundSelectionColor(UIManager.getColor("Table.selectionBackground"));
            }
        }
    }
}
