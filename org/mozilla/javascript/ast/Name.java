package org.mozilla.javascript.ast;

public class Name extends AstNode
{
    private String identifier;
    private Scope scope;
    
    public Name() {
        this.type = 39;
    }
    
    public Name(final int n) {
        super(n);
        this.type = 39;
    }
    
    public Name(final int n, final int n2) {
        super(n, n2);
        this.type = 39;
    }
    
    public Name(final int n, final int n2, final String identifier) {
        super(n, n2);
        this.type = 39;
        this.setIdentifier(identifier);
    }
    
    public Name(final int n, final String identifier) {
        super(n);
        this.type = 39;
        this.setIdentifier(identifier);
        this.setLength(identifier.length());
    }
    
    public Scope getDefiningScope() {
        final Scope enclosingScope = this.getEnclosingScope();
        final String identifier = this.getIdentifier();
        if (enclosingScope == null) {
            return null;
        }
        return enclosingScope.getDefiningScope(identifier);
    }
    
    public String getIdentifier() {
        return this.identifier;
    }
    
    @Override
    public Scope getScope() {
        return this.scope;
    }
    
    public boolean isLocalName() {
        final Scope definingScope = this.getDefiningScope();
        return definingScope != null && definingScope.getParentScope() != null;
    }
    
    public int length() {
        if (this.identifier == null) {
            return 0;
        }
        return this.identifier.length();
    }
    
    public void setIdentifier(final String identifier) {
        this.assertNotNull(identifier);
        this.identifier = identifier;
        this.setLength(identifier.length());
    }
    
    @Override
    public void setScope(final Scope scope) {
        this.scope = scope;
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(n));
        String identifier;
        if (this.identifier == null) {
            identifier = "<null>";
        }
        else {
            identifier = this.identifier;
        }
        sb.append(identifier);
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        nodeVisitor.visit(this);
    }
}
