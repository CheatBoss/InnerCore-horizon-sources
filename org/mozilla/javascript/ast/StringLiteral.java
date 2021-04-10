package org.mozilla.javascript.ast;

import org.mozilla.javascript.*;

public class StringLiteral extends AstNode
{
    private char quoteChar;
    private String value;
    
    public StringLiteral() {
        this.type = 41;
    }
    
    public StringLiteral(final int n) {
        super(n);
        this.type = 41;
    }
    
    public StringLiteral(final int n, final int n2) {
        super(n, n2);
        this.type = 41;
    }
    
    public char getQuoteCharacter() {
        return this.quoteChar;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public String getValue(final boolean b) {
        if (!b) {
            return this.value;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(this.quoteChar);
        sb.append(this.value);
        sb.append(this.quoteChar);
        return sb.toString();
    }
    
    public void setQuoteCharacter(final char quoteChar) {
        this.quoteChar = quoteChar;
    }
    
    public void setValue(final String value) {
        this.assertNotNull(value);
        this.value = value;
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder(this.makeIndent(n));
        sb.append(this.quoteChar);
        sb.append(ScriptRuntime.escapeString(this.value, this.quoteChar));
        sb.append(this.quoteChar);
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        nodeVisitor.visit(this);
    }
}
