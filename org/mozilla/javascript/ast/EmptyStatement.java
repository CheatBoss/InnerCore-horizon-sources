package org.mozilla.javascript.ast;

public class EmptyStatement extends AstNode
{
    public EmptyStatement() {
        this.type = 128;
    }
    
    public EmptyStatement(final int n) {
        super(n);
        this.type = 128;
    }
    
    public EmptyStatement(final int n, final int n2) {
        super(n, n2);
        this.type = 128;
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(n));
        sb.append(";\n");
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        nodeVisitor.visit(this);
    }
}
