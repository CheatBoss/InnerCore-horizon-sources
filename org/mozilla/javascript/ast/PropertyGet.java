package org.mozilla.javascript.ast;

public class PropertyGet extends InfixExpression
{
    public PropertyGet() {
        this.type = 33;
    }
    
    public PropertyGet(final int n) {
        super(n);
        this.type = 33;
    }
    
    public PropertyGet(final int n, final int n2) {
        super(n, n2);
        this.type = 33;
    }
    
    public PropertyGet(final int n, final int n2, final AstNode astNode, final Name name) {
        super(n, n2, astNode, name);
        this.type = 33;
    }
    
    public PropertyGet(final AstNode astNode, final Name name) {
        super(astNode, name);
        this.type = 33;
    }
    
    public PropertyGet(final AstNode astNode, final Name name, final int n) {
        super(33, astNode, name, n);
        this.type = 33;
    }
    
    public Name getProperty() {
        return (Name)this.getRight();
    }
    
    public AstNode getTarget() {
        return this.getLeft();
    }
    
    public void setProperty(final Name right) {
        this.setRight(right);
    }
    
    public void setTarget(final AstNode left) {
        this.setLeft(left);
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(n));
        sb.append(this.getLeft().toSource(0));
        sb.append(".");
        sb.append(this.getRight().toSource(0));
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this)) {
            this.getTarget().visit(nodeVisitor);
            this.getProperty().visit(nodeVisitor);
        }
    }
}
