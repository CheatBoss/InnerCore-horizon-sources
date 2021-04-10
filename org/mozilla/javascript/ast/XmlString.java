package org.mozilla.javascript.ast;

public class XmlString extends XmlFragment
{
    private String xml;
    
    public XmlString() {
    }
    
    public XmlString(final int n) {
        super(n);
    }
    
    public XmlString(final int n, final String xml) {
        super(n);
        this.setXml(xml);
    }
    
    public String getXml() {
        return this.xml;
    }
    
    public void setXml(final String xml) {
        this.assertNotNull(xml);
        this.xml = xml;
        this.setLength(xml.length());
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(n));
        sb.append(this.xml);
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        nodeVisitor.visit(this);
    }
}
