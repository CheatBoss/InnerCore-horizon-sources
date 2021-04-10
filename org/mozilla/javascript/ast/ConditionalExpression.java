package org.mozilla.javascript.ast;

public class ConditionalExpression extends AstNode
{
    private int colonPosition;
    private AstNode falseExpression;
    private int questionMarkPosition;
    private AstNode testExpression;
    private AstNode trueExpression;
    
    public ConditionalExpression() {
        this.questionMarkPosition = -1;
        this.colonPosition = -1;
        this.type = 102;
    }
    
    public ConditionalExpression(final int n) {
        super(n);
        this.questionMarkPosition = -1;
        this.colonPosition = -1;
        this.type = 102;
    }
    
    public ConditionalExpression(final int n, final int n2) {
        super(n, n2);
        this.questionMarkPosition = -1;
        this.colonPosition = -1;
        this.type = 102;
    }
    
    public int getColonPosition() {
        return this.colonPosition;
    }
    
    public AstNode getFalseExpression() {
        return this.falseExpression;
    }
    
    public int getQuestionMarkPosition() {
        return this.questionMarkPosition;
    }
    
    public AstNode getTestExpression() {
        return this.testExpression;
    }
    
    public AstNode getTrueExpression() {
        return this.trueExpression;
    }
    
    @Override
    public boolean hasSideEffects() {
        if (this.testExpression == null || this.trueExpression == null || this.falseExpression == null) {
            codeBug();
        }
        return this.trueExpression.hasSideEffects() && this.falseExpression.hasSideEffects();
    }
    
    public void setColonPosition(final int colonPosition) {
        this.colonPosition = colonPosition;
    }
    
    public void setFalseExpression(final AstNode falseExpression) {
        this.assertNotNull(falseExpression);
        (this.falseExpression = falseExpression).setParent(this);
    }
    
    public void setQuestionMarkPosition(final int questionMarkPosition) {
        this.questionMarkPosition = questionMarkPosition;
    }
    
    public void setTestExpression(final AstNode testExpression) {
        this.assertNotNull(testExpression);
        (this.testExpression = testExpression).setParent(this);
    }
    
    public void setTrueExpression(final AstNode trueExpression) {
        this.assertNotNull(trueExpression);
        (this.trueExpression = trueExpression).setParent(this);
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(n));
        sb.append(this.testExpression.toSource(n));
        sb.append(" ? ");
        sb.append(this.trueExpression.toSource(0));
        sb.append(" : ");
        sb.append(this.falseExpression.toSource(0));
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this)) {
            this.testExpression.visit(nodeVisitor);
            this.trueExpression.visit(nodeVisitor);
            this.falseExpression.visit(nodeVisitor);
        }
    }
}
