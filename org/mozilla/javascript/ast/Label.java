package org.mozilla.javascript.ast;

public class Label extends Jump
{
    private String name;
    
    public Label() {
        this.type = 130;
    }
    
    public Label(final int n) {
        this(n, -1);
    }
    
    public Label(final int position, final int length) {
        this.type = 130;
        this.position = position;
        this.length = length;
    }
    
    public Label(final int n, final int n2, final String name) {
        this(n, n2);
        this.setName(name);
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String trim) {
        if (trim == null) {
            trim = null;
        }
        else {
            trim = trim.trim();
        }
        if (trim != null && !"".equals(trim)) {
            this.name = trim;
            return;
        }
        throw new IllegalArgumentException("invalid label name");
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(n));
        sb.append(this.name);
        sb.append(":\n");
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        nodeVisitor.visit(this);
    }
}
