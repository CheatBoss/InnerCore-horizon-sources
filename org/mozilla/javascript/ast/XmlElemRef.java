package org.mozilla.javascript.ast;

public class XmlElemRef extends XmlRef
{
    private AstNode indexExpr;
    private int lb;
    private int rb;
    
    public XmlElemRef() {
        this.lb = -1;
        this.rb = -1;
        this.type = 77;
    }
    
    public XmlElemRef(final int n) {
        super(n);
        this.lb = -1;
        this.rb = -1;
        this.type = 77;
    }
    
    public XmlElemRef(final int n, final int n2) {
        super(n, n2);
        this.lb = -1;
        this.rb = -1;
        this.type = 77;
    }
    
    public AstNode getExpression() {
        return this.indexExpr;
    }
    
    public int getLb() {
        return this.lb;
    }
    
    public int getRb() {
        return this.rb;
    }
    
    public void setBrackets(final int lb, final int rb) {
        this.lb = lb;
        this.rb = rb;
    }
    
    public void setExpression(final AstNode indexExpr) {
        this.assertNotNull(indexExpr);
        (this.indexExpr = indexExpr).setParent(this);
    }
    
    public void setLb(final int lb) {
        this.lb = lb;
    }
    
    public void setRb(final int rb) {
        this.rb = rb;
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(n));
        if (this.isAttributeAccess()) {
            sb.append("@");
        }
        if (this.namespace != null) {
            sb.append(this.namespace.toSource(0));
            sb.append("::");
        }
        sb.append("[");
        sb.append(this.indexExpr.toSource(0));
        sb.append("]");
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this)) {
            if (this.namespace != null) {
                this.namespace.visit(nodeVisitor);
            }
            this.indexExpr.visit(nodeVisitor);
        }
    }
}
