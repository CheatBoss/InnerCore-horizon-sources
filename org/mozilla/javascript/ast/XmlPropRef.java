package org.mozilla.javascript.ast;

public class XmlPropRef extends XmlRef
{
    private Name propName;
    
    public XmlPropRef() {
        this.type = 79;
    }
    
    public XmlPropRef(final int n) {
        super(n);
        this.type = 79;
    }
    
    public XmlPropRef(final int n, final int n2) {
        super(n, n2);
        this.type = 79;
    }
    
    public Name getPropName() {
        return this.propName;
    }
    
    public void setPropName(final Name propName) {
        this.assertNotNull(propName);
        (this.propName = propName).setParent(this);
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
        sb.append(this.propName.toSource(0));
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this)) {
            if (this.namespace != null) {
                this.namespace.visit(nodeVisitor);
            }
            this.propName.visit(nodeVisitor);
        }
    }
}
