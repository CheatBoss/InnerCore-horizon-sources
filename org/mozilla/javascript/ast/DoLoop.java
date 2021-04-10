package org.mozilla.javascript.ast;

public class DoLoop extends Loop
{
    private AstNode condition;
    private int whilePosition;
    
    public DoLoop() {
        this.whilePosition = -1;
        this.type = 118;
    }
    
    public DoLoop(final int n) {
        super(n);
        this.whilePosition = -1;
        this.type = 118;
    }
    
    public DoLoop(final int n, final int n2) {
        super(n, n2);
        this.whilePosition = -1;
        this.type = 118;
    }
    
    public AstNode getCondition() {
        return this.condition;
    }
    
    public int getWhilePosition() {
        return this.whilePosition;
    }
    
    public void setCondition(final AstNode condition) {
        this.assertNotNull(condition);
        (this.condition = condition).setParent(this);
    }
    
    public void setWhilePosition(final int whilePosition) {
        this.whilePosition = whilePosition;
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(n));
        sb.append("do ");
        sb.append(this.body.toSource(n).trim());
        sb.append(" while (");
        sb.append(this.condition.toSource(0));
        sb.append(");\n");
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this)) {
            this.body.visit(nodeVisitor);
            this.condition.visit(nodeVisitor);
        }
    }
}
