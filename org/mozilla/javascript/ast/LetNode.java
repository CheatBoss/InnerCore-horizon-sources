package org.mozilla.javascript.ast;

public class LetNode extends Scope
{
    private AstNode body;
    private int lp;
    private int rp;
    private VariableDeclaration variables;
    
    public LetNode() {
        this.lp = -1;
        this.rp = -1;
        this.type = 158;
    }
    
    public LetNode(final int n) {
        super(n);
        this.lp = -1;
        this.rp = -1;
        this.type = 158;
    }
    
    public LetNode(final int n, final int n2) {
        super(n, n2);
        this.lp = -1;
        this.rp = -1;
        this.type = 158;
    }
    
    public AstNode getBody() {
        return this.body;
    }
    
    public int getLp() {
        return this.lp;
    }
    
    public int getRp() {
        return this.rp;
    }
    
    public VariableDeclaration getVariables() {
        return this.variables;
    }
    
    public void setBody(final AstNode body) {
        this.body = body;
        if (body != null) {
            body.setParent(this);
        }
    }
    
    public void setLp(final int lp) {
        this.lp = lp;
    }
    
    public void setParens(final int lp, final int rp) {
        this.lp = lp;
        this.rp = rp;
    }
    
    public void setRp(final int rp) {
        this.rp = rp;
    }
    
    public void setVariables(final VariableDeclaration variables) {
        this.assertNotNull(variables);
        (this.variables = variables).setParent(this);
    }
    
    @Override
    public String toSource(final int n) {
        final String indent = this.makeIndent(n);
        final StringBuilder sb = new StringBuilder();
        sb.append(indent);
        sb.append("let (");
        this.printList(this.variables.getVariables(), sb);
        sb.append(") ");
        if (this.body != null) {
            sb.append(this.body.toSource(n));
        }
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this)) {
            this.variables.visit(nodeVisitor);
            if (this.body != null) {
                this.body.visit(nodeVisitor);
            }
        }
    }
}
