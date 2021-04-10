package org.mozilla.javascript.ast;

public class ParenthesizedExpression extends AstNode
{
    private AstNode expression;
    
    public ParenthesizedExpression() {
        this.type = 87;
    }
    
    public ParenthesizedExpression(final int n) {
        super(n);
        this.type = 87;
    }
    
    public ParenthesizedExpression(final int n, final int n2) {
        super(n, n2);
        this.type = 87;
    }
    
    public ParenthesizedExpression(final int n, final int n2, final AstNode expression) {
        super(n, n2);
        this.type = 87;
        this.setExpression(expression);
    }
    
    public ParenthesizedExpression(final AstNode astNode) {
        int position;
        if (astNode != null) {
            position = astNode.getPosition();
        }
        else {
            position = 0;
        }
        int length;
        if (astNode != null) {
            length = astNode.getLength();
        }
        else {
            length = 1;
        }
        this(position, length, astNode);
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
        sb.append("(");
        sb.append(this.expression.toSource(0));
        sb.append(")");
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this)) {
            this.expression.visit(nodeVisitor);
        }
    }
}
