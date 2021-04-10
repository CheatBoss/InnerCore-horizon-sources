package org.mozilla.javascript.ast;

public class BreakStatement extends Jump
{
    private Name breakLabel;
    private AstNode target;
    
    public BreakStatement() {
        this.type = 120;
    }
    
    public BreakStatement(final int position) {
        this.type = 120;
        this.position = position;
    }
    
    public BreakStatement(final int position, final int length) {
        this.type = 120;
        this.position = position;
        this.length = length;
    }
    
    public Name getBreakLabel() {
        return this.breakLabel;
    }
    
    public AstNode getBreakTarget() {
        return this.target;
    }
    
    public void setBreakLabel(final Name breakLabel) {
        this.breakLabel = breakLabel;
        if (breakLabel != null) {
            breakLabel.setParent(this);
        }
    }
    
    public void setBreakTarget(final Jump target) {
        this.assertNotNull(target);
        this.setJumpStatement((Jump)(this.target = target));
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(n));
        sb.append("break");
        if (this.breakLabel != null) {
            sb.append(" ");
            sb.append(this.breakLabel.toSource(0));
        }
        sb.append(";\n");
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this) && this.breakLabel != null) {
            this.breakLabel.visit(nodeVisitor);
        }
    }
}
