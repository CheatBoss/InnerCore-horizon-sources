package org.mozilla.javascript.ast;

public class ReturnStatement extends AstNode
{
    private AstNode returnValue;
    
    public ReturnStatement() {
        this.type = 4;
    }
    
    public ReturnStatement(final int n) {
        super(n);
        this.type = 4;
    }
    
    public ReturnStatement(final int n, final int n2) {
        super(n, n2);
        this.type = 4;
    }
    
    public ReturnStatement(final int n, final int n2, final AstNode returnValue) {
        super(n, n2);
        this.type = 4;
        this.setReturnValue(returnValue);
    }
    
    public AstNode getReturnValue() {
        return this.returnValue;
    }
    
    public void setReturnValue(final AstNode returnValue) {
        this.returnValue = returnValue;
        if (returnValue != null) {
            returnValue.setParent(this);
        }
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(n));
        sb.append("return");
        if (this.returnValue != null) {
            sb.append(" ");
            sb.append(this.returnValue.toSource(0));
        }
        sb.append(";\n");
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this) && this.returnValue != null) {
            this.returnValue.visit(nodeVisitor);
        }
    }
}
