package org.mozilla.javascript.ast;

public class XmlDotQuery extends InfixExpression
{
    private int rp;
    
    public XmlDotQuery() {
        this.rp = -1;
        this.type = 146;
    }
    
    public XmlDotQuery(final int n) {
        super(n);
        this.rp = -1;
        this.type = 146;
    }
    
    public XmlDotQuery(final int n, final int n2) {
        super(n, n2);
        this.rp = -1;
        this.type = 146;
    }
    
    public int getRp() {
        return this.rp;
    }
    
    public void setRp(final int rp) {
        this.rp = rp;
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(n));
        sb.append(this.getLeft().toSource(0));
        sb.append(".(");
        sb.append(this.getRight().toSource(0));
        sb.append(")");
        return sb.toString();
    }
}
