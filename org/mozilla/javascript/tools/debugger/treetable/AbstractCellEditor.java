package org.mozilla.javascript.tools.debugger.treetable;

import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

public class AbstractCellEditor implements CellEditor
{
    protected EventListenerList listenerList;
    
    public AbstractCellEditor() {
        this.listenerList = new EventListenerList();
    }
    
    @Override
    public void addCellEditorListener(final CellEditorListener cellEditorListener) {
        this.listenerList.add(CellEditorListener.class, cellEditorListener);
    }
    
    @Override
    public void cancelCellEditing() {
    }
    
    protected void fireEditingCanceled() {
        final Object[] listenerList = this.listenerList.getListenerList();
        for (int i = listenerList.length - 2; i >= 0; i -= 2) {
            if (listenerList[i] == CellEditorListener.class) {
                ((CellEditorListener)listenerList[i + 1]).editingCanceled(new ChangeEvent(this));
            }
        }
    }
    
    protected void fireEditingStopped() {
        final Object[] listenerList = this.listenerList.getListenerList();
        for (int i = listenerList.length - 2; i >= 0; i -= 2) {
            if (listenerList[i] == CellEditorListener.class) {
                ((CellEditorListener)listenerList[i + 1]).editingStopped(new ChangeEvent(this));
            }
        }
    }
    
    @Override
    public Object getCellEditorValue() {
        return null;
    }
    
    @Override
    public boolean isCellEditable(final EventObject eventObject) {
        return true;
    }
    
    @Override
    public void removeCellEditorListener(final CellEditorListener cellEditorListener) {
        this.listenerList.remove(CellEditorListener.class, cellEditorListener);
    }
    
    @Override
    public boolean shouldSelectCell(final EventObject eventObject) {
        return false;
    }
    
    @Override
    public boolean stopCellEditing() {
        return true;
    }
}
