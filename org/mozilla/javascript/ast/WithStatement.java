package org.mozilla.javascript.ast;

public class WithStatement extends AstNode
{
    private AstNode expression;
    private int lp;
    private int rp;
    private AstNode statement;
    
    public WithStatement() {
        this.lp = -1;
        this.rp = -1;
        this.type = 123;
    }
    
    public WithStatement(final int n) {
        super(n);
        this.lp = -1;
        this.rp = -1;
        this.type = 123;
    }
    
    public WithStatement(final int n, final int n2) {
        super(n, n2);
        this.lp = -1;
        this.rp = -1;
        this.type = 123;
    }
    
    public AstNode getExpression() {
        return this.expression;
    }
    
    public int getLp() {
        return this.lp;
    }
    
    public int getRp() {
        return this.rp;
    }
    
    public AstNode getStatement() {
        return this.statement;
    }
    
    public void setExpression(final AstNode expression) {
        this.assertNotNull(expression);
        (this.expression = expression).setParent(this);
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
    
    public void setStatement(final AstNode statement) {
        this.assertNotNull(statement);
        (this.statement = statement).setParent(this);
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(n));
        sb.append("with (");
        sb.append(this.expression.toSource(0));
        sb.append(") ");
        if (this.statement.getType() == 129) {
            sb.append(this.statement.toSource(n).trim());
            sb.append("\n");
        }
        else {
            sb.append("\n");
            sb.append(this.statement.toSource(n + 1));
        }
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this)) {
            this.expression.visit(nodeVisitor);
            this.statement.visit(nodeVisitor);
        }
    }
}
