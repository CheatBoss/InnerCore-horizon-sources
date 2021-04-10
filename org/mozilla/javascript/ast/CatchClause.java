package org.mozilla.javascript.ast;

public class CatchClause extends AstNode
{
    private Block body;
    private AstNode catchCondition;
    private int ifPosition;
    private int lp;
    private int rp;
    private Name varName;
    
    public CatchClause() {
        this.ifPosition = -1;
        this.lp = -1;
        this.rp = -1;
        this.type = 124;
    }
    
    public CatchClause(final int n) {
        super(n);
        this.ifPosition = -1;
        this.lp = -1;
        this.rp = -1;
        this.type = 124;
    }
    
    public CatchClause(final int n, final int n2) {
        super(n, n2);
        this.ifPosition = -1;
        this.lp = -1;
        this.rp = -1;
        this.type = 124;
    }
    
    public Block getBody() {
        return this.body;
    }
    
    public AstNode getCatchCondition() {
        return this.catchCondition;
    }
    
    public int getIfPosition() {
        return this.ifPosition;
    }
    
    public int getLp() {
        return this.lp;
    }
    
    public int getRp() {
        return this.rp;
    }
    
    public Name getVarName() {
        return this.varName;
    }
    
    public void setBody(final Block body) {
        this.assertNotNull(body);
        (this.body = body).setParent(this);
    }
    
    public void setCatchCondition(final AstNode catchCondition) {
        this.catchCondition = catchCondition;
        if (catchCondition != null) {
            catchCondition.setParent(this);
        }
    }
    
    public void setIfPosition(final int ifPosition) {
        this.ifPosition = ifPosition;
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
    
    public void setVarName(final Name varName) {
        this.assertNotNull(varName);
        (this.varName = varName).setParent(this);
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(n));
        sb.append("catch (");
        sb.append(this.varName.toSource(0));
        if (this.catchCondition != null) {
            sb.append(" if ");
            sb.append(this.catchCondition.toSource(0));
        }
        sb.append(") ");
        sb.append(this.body.toSource(0));
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this)) {
            this.varName.visit(nodeVisitor);
            if (this.catchCondition != null) {
                this.catchCondition.visit(nodeVisitor);
            }
            this.body.visit(nodeVisitor);
        }
    }
}
