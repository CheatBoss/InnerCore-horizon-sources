package org.mozilla.javascript.ast;

public class RegExpLiteral extends AstNode
{
    private String flags;
    private String value;
    
    public RegExpLiteral() {
        this.type = 48;
    }
    
    public RegExpLiteral(final int n) {
        super(n);
        this.type = 48;
    }
    
    public RegExpLiteral(final int n, final int n2) {
        super(n, n2);
        this.type = 48;
    }
    
    public String getFlags() {
        return this.flags;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public void setFlags(final String flags) {
        this.flags = flags;
    }
    
    public void setValue(final String value) {
        this.assertNotNull(value);
        this.value = value;
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(n));
        sb.append("/");
        sb.append(this.value);
        sb.append("/");
        String flags;
        if (this.flags == null) {
            flags = "";
        }
        else {
            flags = this.flags;
        }
        sb.append(flags);
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        nodeVisitor.visit(this);
    }
}
