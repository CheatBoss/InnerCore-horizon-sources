package org.mozilla.javascript.ast;

public class ContinueStatement extends Jump
{
    private Name label;
    private Loop target;
    
    public ContinueStatement() {
        this.type = 121;
    }
    
    public ContinueStatement(final int n) {
        this(n, -1);
    }
    
    public ContinueStatement(final int position, final int length) {
        this.type = 121;
        this.position = position;
        this.length = length;
    }
    
    public ContinueStatement(final int n, final int n2, final Name label) {
        this(n, n2);
        this.setLabel(label);
    }
    
    public ContinueStatement(final int n, final Name label) {
        this(n);
        this.setLabel(label);
    }
    
    public ContinueStatement(final Name label) {
        this.type = 121;
        this.setLabel(label);
    }
    
    public Name getLabel() {
        return this.label;
    }
    
    public Loop getTarget() {
        return this.target;
    }
    
    public void setLabel(final Name label) {
        this.label = label;
        if (label != null) {
            label.setParent(this);
        }
    }
    
    public void setTarget(final Loop target) {
        this.assertNotNull(target);
        this.setJumpStatement(this.target = target);
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(n));
        sb.append("continue");
        if (this.label != null) {
            sb.append(" ");
            sb.append(this.label.toSource(0));
        }
        sb.append(";\n");
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this) && this.label != null) {
            this.label.visit(nodeVisitor);
        }
    }
}
