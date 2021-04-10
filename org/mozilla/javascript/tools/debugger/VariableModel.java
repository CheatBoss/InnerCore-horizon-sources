package org.mozilla.javascript.tools.debugger;

import org.mozilla.javascript.tools.debugger.treetable.*;
import java.util.function.*;
import java.util.*;
import javax.swing.event.*;
import javax.swing.tree.*;

class VariableModel implements TreeTableModel
{
    private static final VariableNode[] CHILDLESS;
    private static final String[] cNames;
    private static final Class<?>[] cTypes;
    private Dim debugger;
    private VariableNode root;
    
    static {
        cNames = new String[] { " Name", " Value" };
        cTypes = new Class[] { TreeTableModel.class, String.class };
        CHILDLESS = new VariableNode[0];
    }
    
    public VariableModel() {
    }
    
    public VariableModel(final Dim debugger, final Object o) {
        this.debugger = debugger;
        this.root = new VariableNode(o, "this");
    }
    
    private VariableNode[] children(final VariableNode variableNode) {
        if (variableNode.children != null) {
            return variableNode.children;
        }
        final Object value = this.getValue(variableNode);
        final Object[] objectIds = this.debugger.getObjectIds(value);
        VariableNode[] childless;
        if (objectIds != null && objectIds.length != 0) {
            Arrays.sort(objectIds, new Comparator<Object>() {
                @Override
                public int compare(final Object o, final Object o2) {
                    if (o instanceof String) {
                        if (o2 instanceof Integer) {
                            return -1;
                        }
                        return ((String)o).compareToIgnoreCase((String)o2);
                    }
                    else {
                        if (o2 instanceof String) {
                            return 1;
                        }
                        return (int)o - (int)o2;
                    }
                }
                
                @Override
                public Comparator<Object> reversed() {
                    return (Comparator<Object>)Comparator-CC.$default$reversed((Comparator)this);
                }
                
                @Override
                public Comparator<Object> thenComparing(final Comparator<?> p0) {
                    // 
                    // This method could not be decompiled.
                    // 
                    // Could not show original bytecode, likely due to the same error.
                    // 
                    // The error that occurred was:
                    // 
                    // java.lang.NullPointerException
                    // 
                    throw new IllegalStateException("An error occurred while decompiling this method.");
                }
                
                @Override
                public <U extends Comparable<? super U>> Comparator<Object> thenComparing(final Function<?, ? extends U> p0) {
                    // 
                    // This method could not be decompiled.
                    // 
                    // Could not show original bytecode, likely due to the same error.
                    // 
                    // The error that occurred was:
                    // 
                    // java.lang.NullPointerException
                    // 
                    throw new IllegalStateException("An error occurred while decompiling this method.");
                }
                
                @Override
                public <U> Comparator<Object> thenComparing(final Function<?, ? extends U> p0, final Comparator<? super U> p1) {
                    // 
                    // This method could not be decompiled.
                    // 
                    // Could not show original bytecode, likely due to the same error.
                    // 
                    // The error that occurred was:
                    // 
                    // java.lang.NullPointerException
                    // 
                    throw new IllegalStateException("An error occurred while decompiling this method.");
                }
                
                @Override
                public Comparator<Object> thenComparingDouble(final ToDoubleFunction<?> p0) {
                    // 
                    // This method could not be decompiled.
                    // 
                    // Could not show original bytecode, likely due to the same error.
                    // 
                    // The error that occurred was:
                    // 
                    // java.lang.NullPointerException
                    // 
                    throw new IllegalStateException("An error occurred while decompiling this method.");
                }
                
                @Override
                public Comparator<Object> thenComparingInt(final ToIntFunction<?> p0) {
                    // 
                    // This method could not be decompiled.
                    // 
                    // Could not show original bytecode, likely due to the same error.
                    // 
                    // The error that occurred was:
                    // 
                    // java.lang.NullPointerException
                    // 
                    throw new IllegalStateException("An error occurred while decompiling this method.");
                }
                
                @Override
                public Comparator<Object> thenComparingLong(final ToLongFunction<?> p0) {
                    // 
                    // This method could not be decompiled.
                    // 
                    // Could not show original bytecode, likely due to the same error.
                    // 
                    // The error that occurred was:
                    // 
                    // java.lang.NullPointerException
                    // 
                    throw new IllegalStateException("An error occurred while decompiling this method.");
                }
            });
            final VariableNode[] array = new VariableNode[objectIds.length];
            int n = 0;
            while (true) {
                childless = array;
                if (n == objectIds.length) {
                    break;
                }
                array[n] = new VariableNode(value, objectIds[n]);
                ++n;
            }
        }
        else {
            childless = VariableModel.CHILDLESS;
        }
        variableNode.children = childless;
        return childless;
    }
    
    @Override
    public void addTreeModelListener(final TreeModelListener treeModelListener) {
    }
    
    @Override
    public Object getChild(final Object o, final int n) {
        if (this.debugger == null) {
            return null;
        }
        return this.children((VariableNode)o)[n];
    }
    
    @Override
    public int getChildCount(final Object o) {
        if (this.debugger == null) {
            return 0;
        }
        return this.children((VariableNode)o).length;
    }
    
    @Override
    public Class<?> getColumnClass(final int n) {
        return VariableModel.cTypes[n];
    }
    
    @Override
    public int getColumnCount() {
        return VariableModel.cNames.length;
    }
    
    @Override
    public String getColumnName(final int n) {
        return VariableModel.cNames[n];
    }
    
    @Override
    public int getIndexOfChild(final Object o, final Object o2) {
        if (this.debugger == null) {
            return -1;
        }
        final VariableNode variableNode = (VariableNode)o;
        final VariableNode variableNode2 = (VariableNode)o2;
        final VariableNode[] children = this.children(variableNode);
        for (int i = 0; i != children.length; ++i) {
            if (children[i] == variableNode2) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public Object getRoot() {
        if (this.debugger == null) {
            return null;
        }
        return this.root;
    }
    
    public Object getValue(final VariableNode variableNode) {
        try {
            return this.debugger.getObjectProperty(variableNode.object, variableNode.id);
        }
        catch (Exception ex) {
            return "undefined";
        }
    }
    
    @Override
    public Object getValueAt(final Object o, int i) {
        if (this.debugger == null) {
            return null;
        }
        final VariableNode variableNode = (VariableNode)o;
        switch (i) {
            default: {
                return null;
            }
            case 1: {
                String s;
                try {
                    s = this.debugger.objectToString(this.getValue(variableNode));
                }
                catch (RuntimeException ex) {
                    s = ex.getMessage();
                }
                final StringBuilder sb = new StringBuilder();
                int length;
                char char1;
                for (length = s.length(), i = 0; i < length; ++i) {
                    if (Character.isISOControl(char1 = s.charAt(i))) {
                        char1 = ' ';
                    }
                    sb.append(char1);
                }
                return sb.toString();
            }
            case 0: {
                return variableNode.toString();
            }
        }
    }
    
    @Override
    public boolean isCellEditable(final Object o, final int n) {
        return n == 0;
    }
    
    @Override
    public boolean isLeaf(final Object o) {
        return this.debugger == null || this.children((VariableNode)o).length == 0;
    }
    
    @Override
    public void removeTreeModelListener(final TreeModelListener treeModelListener) {
    }
    
    @Override
    public void setValueAt(final Object o, final Object o2, final int n) {
    }
    
    @Override
    public void valueForPathChanged(final TreePath treePath, final Object o) {
    }
    
    private static class VariableNode
    {
        private VariableNode[] children;
        private Object id;
        private Object object;
        
        public VariableNode(final Object object, final Object id) {
            this.object = object;
            this.id = id;
        }
        
        @Override
        public String toString() {
            if (this.id instanceof String) {
                return (String)this.id;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("[");
            sb.append((int)this.id);
            sb.append("]");
            return sb.toString();
        }
    }
}
