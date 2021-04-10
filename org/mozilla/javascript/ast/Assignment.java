package org.mozilla.javascript.ast;

public class Assignment extends InfixExpression
{
    public Assignment() {
    }
    
    public Assignment(final int n) {
        super(n);
    }
    
    public Assignment(final int n, final int n2) {
        super(n, n2);
    }
    
    public Assignment(final int n, final int n2, final AstNode astNode, final AstNode astNode2) {
        super(n, n2, astNode, astNode2);
    }
    
    public Assignment(final int n, final AstNode astNode, final AstNode astNode2, final int n2) {
        super(n, astNode, astNode2, n2);
    }
    
    public Assignment(final AstNode astNode, final AstNode astNode2) {
        super(astNode, astNode2);
    }
}
