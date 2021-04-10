package org.apache.james.mime4j.field.structured.parser;

public class Token
{
    public int beginColumn;
    public int beginLine;
    public int endColumn;
    public int endLine;
    public String image;
    public int kind;
    public Token next;
    public Token specialToken;
    
    public static final Token newToken(final int n) {
        return new Token();
    }
    
    @Override
    public String toString() {
        return this.image;
    }
}
