package org.mozilla.javascript.ast;

public class NumberLiteral extends AstNode
{
    private double number;
    private String value;
    
    public NumberLiteral() {
        this.type = 40;
    }
    
    public NumberLiteral(final double double1) {
        this.type = 40;
        this.setDouble(double1);
        this.setValue(Double.toString(double1));
    }
    
    public NumberLiteral(final int n) {
        super(n);
        this.type = 40;
    }
    
    public NumberLiteral(final int n, final int n2) {
        super(n, n2);
        this.type = 40;
    }
    
    public NumberLiteral(final int n, final String value) {
        super(n);
        this.type = 40;
        this.setValue(value);
        this.setLength(value.length());
    }
    
    public NumberLiteral(final int n, final String s, final double double1) {
        this(n, s);
        this.setDouble(double1);
    }
    
    public double getNumber() {
        return this.number;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public void setNumber(final double number) {
        this.number = number;
    }
    
    public void setValue(final String value) {
        this.assertNotNull(value);
        this.value = value;
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(n));
        String value;
        if (this.value == null) {
            value = "<null>";
        }
        else {
            value = this.value;
        }
        sb.append(value);
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        nodeVisitor.visit(this);
    }
}
