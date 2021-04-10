package org.mozilla.javascript.ast;

import java.util.*;

public class LabeledStatement extends AstNode
{
    private List<Label> labels;
    private AstNode statement;
    
    public LabeledStatement() {
        this.labels = new ArrayList<Label>();
        this.type = 133;
    }
    
    public LabeledStatement(final int n) {
        super(n);
        this.labels = new ArrayList<Label>();
        this.type = 133;
    }
    
    public LabeledStatement(final int n, final int n2) {
        super(n, n2);
        this.labels = new ArrayList<Label>();
        this.type = 133;
    }
    
    public void addLabel(final Label label) {
        this.assertNotNull(label);
        this.labels.add(label);
        label.setParent(this);
    }
    
    public Label getFirstLabel() {
        return this.labels.get(0);
    }
    
    public Label getLabelByName(final String s) {
        for (final Label label : this.labels) {
            if (s.equals(label.getName())) {
                return label;
            }
        }
        return null;
    }
    
    public List<Label> getLabels() {
        return this.labels;
    }
    
    public AstNode getStatement() {
        return this.statement;
    }
    
    @Override
    public boolean hasSideEffects() {
        return true;
    }
    
    public void setLabels(final List<Label> list) {
        this.assertNotNull(list);
        if (this.labels != null) {
            this.labels.clear();
        }
        final Iterator<Label> iterator = list.iterator();
        while (iterator.hasNext()) {
            this.addLabel(iterator.next());
        }
    }
    
    public void setStatement(final AstNode statement) {
        this.assertNotNull(statement);
        (this.statement = statement).setParent(this);
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        final Iterator<Label> iterator = this.labels.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next().toSource(n));
        }
        sb.append(this.statement.toSource(n + 1));
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this)) {
            final Iterator<Label> iterator = this.labels.iterator();
            while (iterator.hasNext()) {
                iterator.next().visit(nodeVisitor);
            }
            this.statement.visit(nodeVisitor);
        }
    }
}
