package org.mozilla.javascript.ast;

public class EmptyExpression extends AstNode
{
    public EmptyExpression() {
        this.type = 128;
    }
    
    public EmptyExpression(final int n) {
        super(n);
        this.type = 128;
    }
    
    public EmptyExpression(final int n, final int n2) {
        super(n, n2);
        this.type = 128;
    }
    
    @Override
    public String toSource(final int n) {
        return this.makeIndent(n);
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        nodeVisitor.visit(this);
    }
}
