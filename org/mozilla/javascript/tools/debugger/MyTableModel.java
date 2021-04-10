package org.mozilla.javascript.tools.debugger;

import javax.swing.table.*;
import java.util.*;

class MyTableModel extends AbstractTableModel
{
    private static final long serialVersionUID = 2971618907207577000L;
    private SwingGui debugGui;
    private List<String> expressions;
    private List<String> values;
    
    public MyTableModel(final SwingGui debugGui) {
        this.debugGui = debugGui;
        this.expressions = Collections.synchronizedList(new ArrayList<String>());
        this.values = Collections.synchronizedList(new ArrayList<String>());
        this.expressions.add("");
        this.values.add("");
    }
    
    @Override
    public int getColumnCount() {
        return 2;
    }
    
    @Override
    public String getColumnName(final int n) {
        switch (n) {
            default: {
                return null;
            }
            case 1: {
                return "Value";
            }
            case 0: {
                return "Expression";
            }
        }
    }
    
    @Override
    public int getRowCount() {
        return this.expressions.size();
    }
    
    @Override
    public Object getValueAt(final int n, final int n2) {
        switch (n2) {
            default: {
                return "";
            }
            case 1: {
                return this.values.get(n);
            }
            case 0: {
                return this.expressions.get(n);
            }
        }
    }
    
    @Override
    public boolean isCellEditable(final int n, final int n2) {
        return true;
    }
    
    @Override
    public void setValueAt(final Object o, final int n, final int n2) {
        switch (n2) {
            default: {}
            case 1: {
                this.fireTableDataChanged();
            }
            case 0: {
                final String string = o.toString();
                this.expressions.set(n, string);
                String eval = "";
                if (string.length() > 0 && (eval = this.debugGui.dim.eval(string)) == null) {
                    eval = "";
                }
                this.values.set(n, eval);
                this.updateModel();
                if (n + 1 == this.expressions.size()) {
                    this.expressions.add("");
                    this.values.add("");
                    this.fireTableRowsInserted(n + 1, n + 1);
                }
            }
        }
    }
    
    void updateModel() {
        for (int i = 0; i < this.expressions.size(); ++i) {
            final String s = this.expressions.get(i);
            String eval;
            if (s.length() > 0) {
                if ((eval = this.debugGui.dim.eval(s)) == null) {
                    eval = "";
                }
            }
            else {
                eval = "";
            }
            this.values.set(i, eval.replace('\n', ' '));
        }
        this.fireTableDataChanged();
    }
}
