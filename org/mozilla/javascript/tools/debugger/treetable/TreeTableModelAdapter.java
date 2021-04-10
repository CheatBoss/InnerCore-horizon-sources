package org.mozilla.javascript.tools.debugger.treetable;

import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.*;

public class TreeTableModelAdapter extends AbstractTableModel
{
    private static final long serialVersionUID = 48741114609209052L;
    JTree tree;
    TreeTableModel treeTableModel;
    
    public TreeTableModelAdapter(final TreeTableModel treeTableModel, final JTree tree) {
        this.tree = tree;
        this.treeTableModel = treeTableModel;
        tree.addTreeExpansionListener(new TreeExpansionListener() {
            @Override
            public void treeCollapsed(final TreeExpansionEvent treeExpansionEvent) {
                TreeTableModelAdapter.this.fireTableDataChanged();
            }
            
            @Override
            public void treeExpanded(final TreeExpansionEvent treeExpansionEvent) {
                TreeTableModelAdapter.this.fireTableDataChanged();
            }
        });
        treeTableModel.addTreeModelListener(new TreeModelListener() {
            @Override
            public void treeNodesChanged(final TreeModelEvent treeModelEvent) {
                TreeTableModelAdapter.this.delayedFireTableDataChanged();
            }
            
            @Override
            public void treeNodesInserted(final TreeModelEvent treeModelEvent) {
                TreeTableModelAdapter.this.delayedFireTableDataChanged();
            }
            
            @Override
            public void treeNodesRemoved(final TreeModelEvent treeModelEvent) {
                TreeTableModelAdapter.this.delayedFireTableDataChanged();
            }
            
            @Override
            public void treeStructureChanged(final TreeModelEvent treeModelEvent) {
                TreeTableModelAdapter.this.delayedFireTableDataChanged();
            }
        });
    }
    
    protected void delayedFireTableDataChanged() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TreeTableModelAdapter.this.fireTableDataChanged();
            }
        });
    }
    
    @Override
    public Class<?> getColumnClass(final int n) {
        return this.treeTableModel.getColumnClass(n);
    }
    
    @Override
    public int getColumnCount() {
        return this.treeTableModel.getColumnCount();
    }
    
    @Override
    public String getColumnName(final int n) {
        return this.treeTableModel.getColumnName(n);
    }
    
    @Override
    public int getRowCount() {
        return this.tree.getRowCount();
    }
    
    @Override
    public Object getValueAt(final int n, final int n2) {
        return this.treeTableModel.getValueAt(this.nodeForRow(n), n2);
    }
    
    @Override
    public boolean isCellEditable(final int n, final int n2) {
        return this.treeTableModel.isCellEditable(this.nodeForRow(n), n2);
    }
    
    protected Object nodeForRow(final int n) {
        return this.tree.getPathForRow(n).getLastPathComponent();
    }
    
    @Override
    public void setValueAt(final Object o, final int n, final int n2) {
        this.treeTableModel.setValueAt(o, this.nodeForRow(n), n2);
    }
}
