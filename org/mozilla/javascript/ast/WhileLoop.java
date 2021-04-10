package org.mozilla.javascript.ast;

public class WhileLoop extends Loop
{
    private AstNode condition;
    
    public WhileLoop() {
        this.type = 117;
    }
    
    public WhileLoop(final int n) {
        super(n);
        this.type = 117;
    }
    
    public WhileLoop(final int n, final int n2) {
        super(n, n2);
        this.type = 117;
    }
    
    public AstNode getCondition() {
        return this.condition;
    }
    
    public void setCondition(final AstNode condition) {
        this.assertNotNull(condition);
        (this.condition = condition).setParent(this);
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(n));
        sb.append("while (");
        sb.append(this.condition.toSource(0));
        sb.append(") ");
        if (this.body.getType() == 129) {
            sb.append(this.body.toSource(n).trim());
            sb.append("\n");
        }
        else {
            sb.append("\n");
            sb.append(this.body.toSource(n + 1));
        }
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this)) {
            this.condition.visit(nodeVisitor);
            this.body.visit(nodeVisitor);
        }
    }
}
