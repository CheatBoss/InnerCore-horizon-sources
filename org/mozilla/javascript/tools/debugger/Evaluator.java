package org.mozilla.javascript.tools.debugger;

import javax.swing.*;
import javax.swing.table.*;

class Evaluator extends JTable
{
    private static final long serialVersionUID = 8133672432982594256L;
    MyTableModel tableModel;
    
    public Evaluator(final SwingGui swingGui) {
        super(new MyTableModel(swingGui));
        this.tableModel = (MyTableModel)this.getModel();
    }
}
