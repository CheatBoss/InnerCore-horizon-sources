package org.mozilla.javascript.ast;

import org.mozilla.javascript.*;

public class InfixExpression extends AstNode
{
    protected AstNode left;
    protected int operatorPosition;
    protected AstNode right;
    
    public InfixExpression() {
        this.operatorPosition = -1;
    }
    
    public InfixExpression(final int n) {
        super(n);
        this.operatorPosition = -1;
    }
    
    public InfixExpression(final int n, final int n2) {
        super(n, n2);
        this.operatorPosition = -1;
    }
    
    public InfixExpression(final int n, final int n2, final AstNode left, final AstNode right) {
        super(n, n2);
        this.operatorPosition = -1;
        this.setLeft(left);
        this.setRight(right);
    }
    
    public InfixExpression(final int type, final AstNode astNode, final AstNode astNode2, final int n) {
        this.operatorPosition = -1;
        this.setType(type);
        this.setOperatorPosition(n - astNode.getPosition());
        this.setLeftAndRight(astNode, astNode2);
    }
    
    public InfixExpression(final AstNode astNode, final AstNode astNode2) {
        this.operatorPosition = -1;
        this.setLeftAndRight(astNode, astNode2);
    }
    
    public AstNode getLeft() {
        return this.left;
    }
    
    public int getOperator() {
        return this.getType();
    }
    
    public int getOperatorPosition() {
        return this.operatorPosition;
    }
    
    public AstNode getRight() {
        return this.right;
    }
    
    @Override
    public boolean hasSideEffects() {
        final int type = this.getType();
        final boolean b = false;
        final boolean b2 = false;
        if (type == 89) {
            boolean b3 = b;
            if (this.right != null) {
                b3 = b;
                if (this.right.hasSideEffects()) {
                    b3 = true;
                }
            }
            return b3;
        }
        switch (type) {
            default: {
                return super.hasSideEffects();
            }
            case 104:
            case 105: {
                if (this.left == null || !this.left.hasSideEffects()) {
                    boolean b4 = b2;
                    if (this.right == null) {
                        return b4;
                    }
                    b4 = b2;
                    if (!this.right.hasSideEffects()) {
                        return b4;
                    }
                }
                return true;
            }
        }
    }
    
    public void setLeft(final AstNode left) {
        this.assertNotNull(left);
        this.left = left;
        this.setLineno(left.getLineno());
        left.setParent(this);
    }
    
    public void setLeftAndRight(final AstNode left, final AstNode right) {
        this.assertNotNull(left);
        this.assertNotNull(right);
        this.setBounds(left.getPosition(), right.getPosition() + right.getLength());
        this.setLeft(left);
        this.setRight(right);
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
    
    public void setOperatorPosition(final int operatorPosition) {
        this.operatorPosition = operatorPosition;
    }
    
    public void setRight(final AstNode right) {
        this.assertNotNull(right);
        (this.right = right).setParent(this);
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(n));
        sb.append(this.left.toSource());
        sb.append(" ");
        sb.append(AstNode.operatorToString(this.getType()));
        sb.append(" ");
        sb.append(this.right.toSource());
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this)) {
            this.left.visit(nodeVisitor);
            this.right.visit(nodeVisitor);
        }
    }
}
