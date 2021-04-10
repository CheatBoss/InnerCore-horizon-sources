package org.mozilla.javascript.ast;

public class ErrorNode extends AstNode
{
    private String message;
    
    public ErrorNode() {
        this.type = -1;
    }
    
    public ErrorNode(final int n) {
        super(n);
        this.type = -1;
    }
    
    public ErrorNode(final int n, final int n2) {
        super(n, n2);
        this.type = -1;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(final String message) {
        this.message = message;
    }
    
    @Override
    public String toSource(final int n) {
        return "";
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        nodeVisitor.visit(this);
    }
}
