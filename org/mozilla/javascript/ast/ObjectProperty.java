package org.mozilla.javascript.ast;

public class ObjectProperty extends InfixExpression
{
    public ObjectProperty() {
        this.type = 103;
    }
    
    public ObjectProperty(final int n) {
        super(n);
        this.type = 103;
    }
    
    public ObjectProperty(final int n, final int n2) {
        super(n, n2);
        this.type = 103;
    }
    
    public boolean isGetterMethod() {
        return this.type == 151;
    }
    
    public boolean isMethod() {
        return this.isGetterMethod() || this.isSetterMethod() || this.isNormalMethod();
    }
    
    public boolean isNormalMethod() {
        return this.type == 163;
    }
    
    public boolean isSetterMethod() {
        return this.type == 152;
    }
    
    public void setIsGetterMethod() {
        this.type = 151;
    }
    
    public void setIsNormalMethod() {
        this.type = 163;
    }
    
    public void setIsSetterMethod() {
        this.type = 152;
    }
    
    public void setNodeType(final int type) {
        if (type != 103 && type != 151 && type != 152 && type != 163) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invalid node type: ");
            sb.append(type);
            throw new IllegalArgumentException(sb.toString());
        }
        this.setType(type);
    }
    
    @Override
    public String toSource(int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(this.makeIndent(n + 1));
        if (this.isGetterMethod()) {
            sb.append("get ");
        }
        else if (this.isSetterMethod()) {
            sb.append("set ");
        }
        final AstNode left = this.left;
        final int type = this.getType();
        final int n2 = 0;
        int n3;
        if (type == 103) {
            n3 = 0;
        }
        else {
            n3 = n;
        }
        sb.append(left.toSource(n3));
        if (this.type == 103) {
            sb.append(": ");
        }
        final AstNode right = this.right;
        if (this.getType() == 103) {
            n = n2;
        }
        else {
            ++n;
        }
        sb.append(right.toSource(n));
        return sb.toString();
    }
}
