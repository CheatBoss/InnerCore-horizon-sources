package org.mozilla.javascript.ast;

import org.mozilla.javascript.*;

public class KeywordLiteral extends AstNode
{
    public KeywordLiteral() {
    }
    
    public KeywordLiteral(final int n) {
        super(n);
    }
    
    public KeywordLiteral(final int n, final int n2) {
        super(n, n2);
    }
    
    public KeywordLiteral(final int n, final int n2, final int type) {
        super(n, n2);
        this.setType(type);
    }
    
    public boolean isBooleanLiteral() {
        return this.type == 45 || this.type == 44;
    }
    
    @Override
    public KeywordLiteral setType(final int type) {
        if (type != 43 && type != 42 && type != 45 && type != 44 && type != 160) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid node type: ");
            sb.append(type);
            throw new IllegalArgumentException(sb.toString());
        }
        this.type = type;
        return this;
    }
    
    @Override
    public String toSource(int type) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(type));
        type = this.getType();
        if (type != 160) {
            switch (type) {
                default: {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Invalid keyword literal type: ");
                    sb2.append(this.getType());
                    throw new IllegalStateException(sb2.toString());
                }
                case 45: {
                    sb.append("true");
                    break;
                }
                case 44: {
                    sb.append("false");
                    break;
                }
                case 43: {
                    sb.append("this");
                    break;
                }
                case 42: {
                    sb.append("null");
                    break;
                }
            }
        }
        else {
            sb.append("debugger;\n");
        }
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        nodeVisitor.visit(this);
    }
}
