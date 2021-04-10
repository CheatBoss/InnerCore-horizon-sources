package org.mozilla.javascript.tools.debugger;

import java.util.*;
import java.awt.event.*;
import org.mozilla.javascript.tools.debugger.treetable.*;
import javax.swing.table.*;
import java.awt.*;
import javax.swing.tree.*;
import javax.swing.*;

class MyTreeTable extends JTreeTable
{
    private static final long serialVersionUID = 3457265548184453049L;
    
    public MyTreeTable(final VariableModel variableModel) {
        super(variableModel);
    }
    
    public boolean isCellEditable(final EventObject eventObject) {
        if (eventObject instanceof MouseEvent) {
            final MouseEvent mouseEvent = (MouseEvent)eventObject;
            if (mouseEvent.getModifiers() == 0 || ((mouseEvent.getModifiers() & 0x410) != 0x0 && (mouseEvent.getModifiers() & 0x1ACF) == 0x0)) {
                final int rowAtPoint = this.rowAtPoint(mouseEvent.getPoint());
                for (int i = this.getColumnCount() - 1; i >= 0; --i) {
                    if (TreeTableModel.class == this.getColumnClass(i)) {
                        this.tree.dispatchEvent(new MouseEvent(this.tree, mouseEvent.getID(), mouseEvent.getWhen(), mouseEvent.getModifiers(), mouseEvent.getX() - this.getCellRect(rowAtPoint, i, true).x, mouseEvent.getY(), mouseEvent.getClickCount(), mouseEvent.isPopupTrigger()));
                        break;
                    }
                }
            }
            return mouseEvent.getClickCount() >= 3;
        }
        return eventObject == null;
    }
    
    public JTree resetTree(final TreeTableModel treeTableModel) {
        this.tree = new TreeTableCellRenderer(treeTableModel);
        super.setModel(new TreeTableModelAdapter(treeTableModel, this.tree));
        final ListToTreeSelectionModelWrapper selectionModel = new ListToTreeSelectionModelWrapper();
        this.tree.setSelectionModel(selectionModel);
        this.setSelectionModel(selectionModel.getListSelectionModel());
        if (this.tree.getRowHeight() < 1) {
            this.setRowHeight(18);
        }
        this.setDefaultRenderer(TreeTableModel.class, this.tree);
        this.setDefaultEditor(TreeTableModel.class, new TreeTableCellEditor());
        this.setShowGrid(true);
        this.setIntercellSpacing(new Dimension(1, 1));
        this.tree.setRootVisible(false);
        this.tree.setShowsRootHandles(true);
        final DefaultTreeCellRenderer defaultTreeCellRenderer = (DefaultTreeCellRenderer)this.tree.getCellRenderer();
        defaultTreeCellRenderer.setOpenIcon(null);
        defaultTreeCellRenderer.setClosedIcon(null);
        defaultTreeCellRenderer.setLeafIcon(null);
        return this.tree;
    }
}
