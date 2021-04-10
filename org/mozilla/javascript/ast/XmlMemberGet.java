package org.mozilla.javascript.ast;

public class XmlMemberGet extends InfixExpression
{
    public XmlMemberGet() {
        this.type = 143;
    }
    
    public XmlMemberGet(final int n) {
        super(n);
        this.type = 143;
    }
    
    public XmlMemberGet(final int n, final int n2) {
        super(n, n2);
        this.type = 143;
    }
    
    public XmlMemberGet(final int n, final int n2, final AstNode astNode, final XmlRef xmlRef) {
        super(n, n2, astNode, xmlRef);
        this.type = 143;
    }
    
    public XmlMemberGet(final AstNode astNode, final XmlRef xmlRef) {
        super(astNode, xmlRef);
        this.type = 143;
    }
    
    public XmlMemberGet(final AstNode astNode, final XmlRef xmlRef, final int n) {
        super(143, astNode, xmlRef, n);
        this.type = 143;
    }
    
    public XmlRef getMemberRef() {
        return (XmlRef)this.getRight();
    }
    
    public AstNode getTarget() {
        return this.getLeft();
    }
    
    public void setProperty(final XmlRef right) {
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
        sb.append(AstNode.operatorToString(this.getType()));
        sb.append(this.getRight().toSource(0));
        return sb.toString();
    }
}
