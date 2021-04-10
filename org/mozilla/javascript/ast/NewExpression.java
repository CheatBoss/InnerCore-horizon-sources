package org.mozilla.javascript.ast;

import java.util.*;

public class NewExpression extends FunctionCall
{
    private ObjectLiteral initializer;
    
    public NewExpression() {
        this.type = 30;
    }
    
    public NewExpression(final int n) {
        super(n);
        this.type = 30;
    }
    
    public NewExpression(final int n, final int n2) {
        super(n, n2);
        this.type = 30;
    }
    
    public ObjectLiteral getInitializer() {
        return this.initializer;
    }
    
    public void setInitializer(final ObjectLiteral initializer) {
        this.initializer = initializer;
        if (initializer != null) {
            initializer.setParent(this);
        }
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(n));
        sb.append("new ");
        sb.append(this.target.toSource(0));
        sb.append("(");
        if (this.arguments != null) {
            this.printList(this.arguments, sb);
        }
        sb.append(")");
        if (this.initializer != null) {
            sb.append(" ");
            sb.append(this.initializer.toSource(0));
        }
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this)) {
            this.target.visit(nodeVisitor);
            final Iterator<AstNode> iterator = this.getArguments().iterator();
            while (iterator.hasNext()) {
                iterator.next().visit(nodeVisitor);
            }
            if (this.initializer != null) {
                this.initializer.visit(nodeVisitor);
            }
        }
    }
}
