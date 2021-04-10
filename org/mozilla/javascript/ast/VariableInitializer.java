package org.mozilla.javascript.ast;

public class VariableInitializer extends AstNode
{
    private AstNode initializer;
    private AstNode target;
    
    public VariableInitializer() {
        this.type = 122;
    }
    
    public VariableInitializer(final int n) {
        super(n);
        this.type = 122;
    }
    
    public VariableInitializer(final int n, final int n2) {
        super(n, n2);
        this.type = 122;
    }
    
    public AstNode getInitializer() {
        return this.initializer;
    }
    
    public AstNode getTarget() {
        return this.target;
    }
    
    public boolean isDestructuring() {
        return !(this.target instanceof Name);
    }
    
    public void setInitializer(final AstNode initializer) {
        this.initializer = initializer;
        if (initializer != null) {
            initializer.setParent(this);
        }
    }
    
    public void setNodeType(final int type) {
        if (type != 122 && type != 154 && type != 153) {
            throw new IllegalArgumentException("invalid node type");
        }
        this.setType(type);
    }
    
    public void setTarget(final AstNode target) {
        if (target == null) {
            throw new IllegalArgumentException("invalid target arg");
        }
        (this.target = target).setParent(this);
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(n));
        sb.append(this.target.toSource(0));
        if (this.initializer != null) {
            sb.append(" = ");
            sb.append(this.initializer.toSource(0));
        }
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this)) {
            this.target.visit(nodeVisitor);
            if (this.initializer != null) {
                this.initializer.visit(nodeVisitor);
            }
        }
    }
}
