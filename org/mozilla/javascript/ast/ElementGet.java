package org.mozilla.javascript.ast;

public class ElementGet extends AstNode
{
    private AstNode element;
    private int lb;
    private int rb;
    private AstNode target;
    
    public ElementGet() {
        this.lb = -1;
        this.rb = -1;
        this.type = 36;
    }
    
    public ElementGet(final int n) {
        super(n);
        this.lb = -1;
        this.rb = -1;
        this.type = 36;
    }
    
    public ElementGet(final int n, final int n2) {
        super(n, n2);
        this.lb = -1;
        this.rb = -1;
        this.type = 36;
    }
    
    public ElementGet(final AstNode target, final AstNode element) {
        this.lb = -1;
        this.rb = -1;
        this.type = 36;
        this.setTarget(target);
        this.setElement(element);
    }
    
    public AstNode getElement() {
        return this.element;
    }
    
    public int getLb() {
        return this.lb;
    }
    
    public int getRb() {
        return this.rb;
    }
    
    public AstNode getTarget() {
        return this.target;
    }
    
    public void setElement(final AstNode element) {
        this.assertNotNull(element);
        (this.element = element).setParent(this);
    }
    
    public void setLb(final int lb) {
        this.lb = lb;
    }
    
    public void setParens(final int lb, final int rb) {
        this.lb = lb;
        this.rb = rb;
    }
    
    public void setRb(final int rb) {
        this.rb = rb;
    }
    
    public void setTarget(final AstNode target) {
        this.assertNotNull(target);
        (this.target = target).setParent(this);
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(n));
        sb.append(this.target.toSource(0));
        sb.append("[");
        sb.append(this.element.toSource(0));
        sb.append("]");
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this)) {
            this.target.visit(nodeVisitor);
            this.element.visit(nodeVisitor);
        }
    }
}
