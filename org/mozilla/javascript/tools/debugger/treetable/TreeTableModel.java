package org.mozilla.javascript.tools.debugger.treetable;

import javax.swing.tree.*;

public interface TreeTableModel extends TreeModel
{
    Class<?> getColumnClass(final int p0);
    
    int getColumnCount();
    
    String getColumnName(final int p0);
    
    Object getValueAt(final Object p0, final int p1);
    
    boolean isCellEditable(final Object p0, final int p1);
    
    void setValueAt(final Object p0, final Object p1, final int p2);
}
