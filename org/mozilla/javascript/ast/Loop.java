package org.mozilla.javascript.ast;

public abstract class Loop extends Scope
{
    protected AstNode body;
    protected int lp;
    protected int rp;
    
    public Loop() {
        this.lp = -1;
        this.rp = -1;
    }
    
    public Loop(final int n) {
        super(n);
        this.lp = -1;
        this.rp = -1;
    }
    
    public Loop(final int n, final int n2) {
        super(n, n2);
        this.lp = -1;
        this.rp = -1;
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
    
    public void setBody(final AstNode body) {
        this.body = body;
        this.setLength(body.getPosition() + body.getLength() - this.getPosition());
        body.setParent(this);
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
}
