package org.mozilla.javascript.ast;

import org.mozilla.javascript.*;

public class UnaryExpression extends AstNode
{
    private boolean isPostfix;
    private AstNode operand;
    
    public UnaryExpression() {
    }
    
    public UnaryExpression(final int n) {
        super(n);
    }
    
    public UnaryExpression(final int n, final int n2) {
        super(n, n2);
    }
    
    public UnaryExpression(final int n, final int n2, final AstNode astNode) {
        this(n, n2, astNode, false);
    }
    
    public UnaryExpression(final int operator, int n, final AstNode operand, final boolean isPostfix) {
        this.assertNotNull(operand);
        int position;
        if (isPostfix) {
            position = operand.getPosition();
        }
        else {
            position = n;
        }
        if (isPostfix) {
            n += 2;
        }
        else {
            n = operand.getPosition() + operand.getLength();
        }
        this.setBounds(position, n);
        this.setOperator(operator);
        this.setOperand(operand);
        this.isPostfix = isPostfix;
    }
    
    public AstNode getOperand() {
        return this.operand;
    }
    
    public int getOperator() {
        return this.type;
    }
    
    public boolean isPostfix() {
        return this.isPostfix;
    }
    
    public boolean isPrefix() {
        return this.isPostfix ^ true;
    }
    
    public void setIsPostfix(final boolean isPostfix) {
        this.isPostfix = isPostfix;
    }
    
    public void setOperand(final AstNode operand) {
        this.assertNotNull(operand);
        (this.operand = operand).setParent(this);
    }
    
    public void setOperator(final int type) {
        if (!Token.isValidToken(type)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid token: ");
            sb.append(type);
            throw new IllegalArgumentException(sb.toString());
        }
        this.setType(type);
    }
    
    @Override
    public String toSource(int type) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(type));
        type = this.getType();
        if (!this.isPostfix) {
            sb.append(AstNode.operatorToString(type));
            if (type == 32 || type == 31 || type == 126) {
                sb.append(" ");
            }
        }
        sb.append(this.operand.toSource());
        if (this.isPostfix) {
            sb.append(AstNode.operatorToString(type));
        }
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this)) {
            this.operand.visit(nodeVisitor);
        }
    }
}
