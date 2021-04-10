package org.mozilla.javascript.ast;

public class ThrowStatement extends AstNode
{
    private AstNode expression;
    
    public ThrowStatement() {
        this.type = 50;
    }
    
    public ThrowStatement(final int n) {
        super(n);
        this.type = 50;
    }
    
    public ThrowStatement(final int n, final int n2) {
        super(n, n2);
        this.type = 50;
    }
    
    public ThrowStatement(final int n, final int n2, final AstNode expression) {
        super(n, n2);
        this.type = 50;
        this.setExpression(expression);
    }
    
    public ThrowStatement(final int n, final AstNode expression) {
        super(n, expression.getLength());
        this.type = 50;
        this.setExpression(expression);
    }
    
    public ThrowStatement(final AstNode expression) {
        this.type = 50;
        this.setExpression(expression);
    }
    
    public AstNode getExpression() {
        return this.expression;
    }
    
    public void setExpression(final AstNode expression) {
        this.assertNotNull(expression);
        (this.expression = expression).setParent(this);
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(n));
        sb.append("throw");
        sb.append(" ");
        sb.append(this.expression.toSource(0));
        sb.append(";\n");
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this)) {
            this.expression.visit(nodeVisitor);
        }
    }
}
