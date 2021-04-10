package org.mozilla.javascript.ast;

import java.util.*;

public class SwitchCase extends AstNode
{
    private AstNode expression;
    private List<AstNode> statements;
    
    public SwitchCase() {
        this.type = 115;
    }
    
    public SwitchCase(final int n) {
        super(n);
        this.type = 115;
    }
    
    public SwitchCase(final int n, final int n2) {
        super(n, n2);
        this.type = 115;
    }
    
    public void addStatement(final AstNode astNode) {
        this.assertNotNull(astNode);
        if (this.statements == null) {
            this.statements = new ArrayList<AstNode>();
        }
        this.setLength(astNode.getPosition() + astNode.getLength() - this.getPosition());
        this.statements.add(astNode);
        astNode.setParent(this);
    }
    
    public AstNode getExpression() {
        return this.expression;
    }
    
    public List<AstNode> getStatements() {
        return this.statements;
    }
    
    public boolean isDefault() {
        return this.expression == null;
    }
    
    public void setExpression(final AstNode expression) {
        this.expression = expression;
        if (expression != null) {
            expression.setParent(this);
        }
    }
    
    public void setStatements(final List<AstNode> list) {
        if (this.statements != null) {
            this.statements.clear();
        }
        final Iterator<AstNode> iterator = list.iterator();
        while (iterator.hasNext()) {
            this.addStatement(iterator.next());
        }
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(n));
        if (this.expression == null) {
            sb.append("default:\n");
        }
        else {
            sb.append("case ");
            sb.append(this.expression.toSource(0));
            sb.append(":\n");
        }
        if (this.statements != null) {
            final Iterator<AstNode> iterator = this.statements.iterator();
            while (iterator.hasNext()) {
                sb.append(iterator.next().toSource(n + 1));
            }
        }
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this)) {
            if (this.expression != null) {
                this.expression.visit(nodeVisitor);
            }
            if (this.statements != null) {
                final Iterator<AstNode> iterator = this.statements.iterator();
                while (iterator.hasNext()) {
                    iterator.next().visit(nodeVisitor);
                }
            }
        }
    }
}
