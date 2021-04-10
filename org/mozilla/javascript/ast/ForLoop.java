package org.mozilla.javascript.ast;

public class ForLoop extends Loop
{
    private AstNode condition;
    private AstNode increment;
    private AstNode initializer;
    
    public ForLoop() {
        this.type = 119;
    }
    
    public ForLoop(final int n) {
        super(n);
        this.type = 119;
    }
    
    public ForLoop(final int n, final int n2) {
        super(n, n2);
        this.type = 119;
    }
    
    public AstNode getCondition() {
        return this.condition;
    }
    
    public AstNode getIncrement() {
        return this.increment;
    }
    
    public AstNode getInitializer() {
        return this.initializer;
    }
    
    public void setCondition(final AstNode condition) {
        this.assertNotNull(condition);
        (this.condition = condition).setParent(this);
    }
    
    public void setIncrement(final AstNode increment) {
        this.assertNotNull(increment);
        (this.increment = increment).setParent(this);
    }
    
    public void setInitializer(final AstNode initializer) {
        this.assertNotNull(initializer);
        (this.initializer = initializer).setParent(this);
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(n));
        sb.append("for (");
        sb.append(this.initializer.toSource(0));
        sb.append("; ");
        sb.append(this.condition.toSource(0));
        sb.append("; ");
        sb.append(this.increment.toSource(0));
        sb.append(") ");
        if (this.body.getType() == 129) {
            sb.append(this.body.toSource(n).trim());
            sb.append("\n");
        }
        else {
            sb.append("\n");
            sb.append(this.body.toSource(n + 1));
        }
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this)) {
            this.initializer.visit(nodeVisitor);
            this.condition.visit(nodeVisitor);
            this.increment.visit(nodeVisitor);
            this.body.visit(nodeVisitor);
        }
    }
}
