package org.mozilla.javascript.ast;

public class GeneratorExpressionLoop extends ForInLoop
{
    public GeneratorExpressionLoop() {
    }
    
    public GeneratorExpressionLoop(final int n) {
        super(n);
    }
    
    public GeneratorExpressionLoop(final int n, final int n2) {
        super(n, n2);
    }
    
    @Override
    public boolean isForEach() {
        return false;
    }
    
    @Override
    public void setIsForEach(final boolean b) {
        throw new UnsupportedOperationException("this node type does not support for each");
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(n));
        sb.append(" for ");
        String s;
        if (this.isForEach()) {
            s = "each ";
        }
        else {
            s = "";
        }
        sb.append(s);
        sb.append("(");
        sb.append(this.iterator.toSource(0));
        sb.append(" in ");
        sb.append(this.iteratedObject.toSource(0));
        sb.append(")");
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this)) {
            this.iterator.visit(nodeVisitor);
            this.iteratedObject.visit(nodeVisitor);
        }
    }
}
