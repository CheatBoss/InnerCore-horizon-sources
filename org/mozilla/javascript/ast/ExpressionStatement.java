package org.mozilla.javascript.ast;

public class ExpressionStatement extends AstNode
{
    private AstNode expr;
    
    public ExpressionStatement() {
        this.type = 133;
    }
    
    public ExpressionStatement(final int n, final int n2) {
        super(n, n2);
        this.type = 133;
    }
    
    public ExpressionStatement(final int n, final int n2, final AstNode expression) {
        super(n, n2);
        this.type = 133;
        this.setExpression(expression);
    }
    
    public ExpressionStatement(final AstNode astNode) {
        this(astNode.getPosition(), astNode.getLength(), astNode);
    }
    
    public ExpressionStatement(final AstNode astNode, final boolean b) {
        this(astNode);
        if (b) {
            this.setHasResult();
        }
    }
    
    public AstNode getExpression() {
        return this.expr;
    }
    
    @Override
    public boolean hasSideEffects() {
        return this.type == 134 || this.expr.hasSideEffects();
    }
    
    public void setExpression(final AstNode expr) {
        this.assertNotNull(expr);
        (this.expr = expr).setParent(this);
        this.setLineno(expr.getLineno());
    }
    
    public void setHasResult() {
        this.type = 134;
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.expr.toSource(n));
        sb.append(";\n");
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this)) {
            this.expr.visit(nodeVisitor);
        }
    }
}
