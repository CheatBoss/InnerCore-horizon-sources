package org.mozilla.javascript.ast;

import org.mozilla.javascript.*;

public class Comment extends AstNode
{
    private Token.CommentType commentType;
    private String value;
    
    public Comment(final int n, final int n2, final Token.CommentType commentType, final String value) {
        super(n, n2);
        this.type = 161;
        this.commentType = commentType;
        this.value = value;
    }
    
    public Token.CommentType getCommentType() {
        return this.commentType;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public void setCommentType(final Token.CommentType commentType) {
        this.commentType = commentType;
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder(this.getLength() + 10);
        sb.append(this.makeIndent(n));
        sb.append(this.value);
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        nodeVisitor.visit(this);
    }
}
