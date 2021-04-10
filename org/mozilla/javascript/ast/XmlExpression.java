package org.mozilla.javascript.ast;

public class XmlExpression extends XmlFragment
{
    private AstNode expression;
    private boolean isXmlAttribute;
    
    public XmlExpression() {
    }
    
    public XmlExpression(final int n) {
        super(n);
    }
    
    public XmlExpression(final int n, final int n2) {
        super(n, n2);
    }
    
    public XmlExpression(final int n, final AstNode expression) {
        super(n);
        this.setExpression(expression);
    }
    
    public AstNode getExpression() {
        return this.expression;
    }
    
    public boolean isXmlAttribute() {
        return this.isXmlAttribute;
    }
    
    public void setExpression(final AstNode expression) {
        this.assertNotNull(expression);
        (this.expression = expression).setParent(this);
    }
    
    public void setIsXmlAttribute(final boolean isXmlAttribute) {
        this.isXmlAttribute = isXmlAttribute;
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(n));
        sb.append("{");
        sb.append(this.expression.toSource(n));
        sb.append("}");
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this)) {
            this.expression.visit(nodeVisitor);
        }
    }
}
