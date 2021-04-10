package org.mozilla.javascript.ast;

public class IfStatement extends AstNode
{
    private AstNode condition;
    private AstNode elsePart;
    private int elsePosition;
    private int lp;
    private int rp;
    private AstNode thenPart;
    
    public IfStatement() {
        this.elsePosition = -1;
        this.lp = -1;
        this.rp = -1;
        this.type = 112;
    }
    
    public IfStatement(final int n) {
        super(n);
        this.elsePosition = -1;
        this.lp = -1;
        this.rp = -1;
        this.type = 112;
    }
    
    public IfStatement(final int n, final int n2) {
        super(n, n2);
        this.elsePosition = -1;
        this.lp = -1;
        this.rp = -1;
        this.type = 112;
    }
    
    public AstNode getCondition() {
        return this.condition;
    }
    
    public AstNode getElsePart() {
        return this.elsePart;
    }
    
    public int getElsePosition() {
        return this.elsePosition;
    }
    
    public int getLp() {
        return this.lp;
    }
    
    public int getRp() {
        return this.rp;
    }
    
    public AstNode getThenPart() {
        return this.thenPart;
    }
    
    public void setCondition(final AstNode condition) {
        this.assertNotNull(condition);
        (this.condition = condition).setParent(this);
    }
    
    public void setElsePart(final AstNode elsePart) {
        this.elsePart = elsePart;
        if (elsePart != null) {
            elsePart.setParent(this);
        }
    }
    
    public void setElsePosition(final int elsePosition) {
        this.elsePosition = elsePosition;
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
    
    public void setThenPart(final AstNode thenPart) {
        this.assertNotNull(thenPart);
        (this.thenPart = thenPart).setParent(this);
    }
    
    @Override
    public String toSource(final int n) {
        final String indent = this.makeIndent(n);
        final StringBuilder sb = new StringBuilder(32);
        sb.append(indent);
        sb.append("if (");
        sb.append(this.condition.toSource(0));
        sb.append(") ");
        if (this.thenPart.getType() != 129) {
            sb.append("\n");
            sb.append(this.makeIndent(n + 1));
        }
        sb.append(this.thenPart.toSource(n).trim());
        if (this.elsePart != null) {
            if (this.thenPart.getType() != 129) {
                sb.append("\n");
                sb.append(indent);
                sb.append("else ");
            }
            else {
                sb.append(" else ");
            }
            if (this.elsePart.getType() != 129 && this.elsePart.getType() != 112) {
                sb.append("\n");
                sb.append(this.makeIndent(n + 1));
            }
            sb.append(this.elsePart.toSource(n).trim());
        }
        sb.append("\n");
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this)) {
            this.condition.visit(nodeVisitor);
            this.thenPart.visit(nodeVisitor);
            if (this.elsePart != null) {
                this.elsePart.visit(nodeVisitor);
            }
        }
    }
}
