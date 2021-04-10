package org.mozilla.javascript.ast;

public class Yield extends AstNode
{
    private AstNode value;
    
    public Yield() {
        this.type = 72;
    }
    
    public Yield(final int n) {
        super(n);
        this.type = 72;
    }
    
    public Yield(final int n, final int n2) {
        super(n, n2);
        this.type = 72;
    }
    
    public Yield(final int n, final int n2, final AstNode value) {
        super(n, n2);
        this.type = 72;
        this.setValue(value);
    }
    
    public AstNode getValue() {
        return this.value;
    }
    
    public void setValue(final AstNode value) {
        this.value = value;
        if (value != null) {
            value.setParent(this);
        }
    }
    
    @Override
    public String toSource(final int n) {
        if (this.value == null) {
            return "yield";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("yield ");
        sb.append(this.value.toSource(0));
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this) && this.value != null) {
            this.value.visit(nodeVisitor);
        }
    }
}
