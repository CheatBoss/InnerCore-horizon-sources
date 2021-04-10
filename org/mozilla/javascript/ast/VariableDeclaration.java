package org.mozilla.javascript.ast;

import org.mozilla.javascript.*;
import java.util.*;

public class VariableDeclaration extends AstNode
{
    private boolean isStatement;
    private List<VariableInitializer> variables;
    
    public VariableDeclaration() {
        this.variables = new ArrayList<VariableInitializer>();
        this.type = 122;
    }
    
    public VariableDeclaration(final int n) {
        super(n);
        this.variables = new ArrayList<VariableInitializer>();
        this.type = 122;
    }
    
    public VariableDeclaration(final int n, final int n2) {
        super(n, n2);
        this.variables = new ArrayList<VariableInitializer>();
        this.type = 122;
    }
    
    private String declTypeName() {
        return Token.typeToName(this.type).toLowerCase();
    }
    
    public void addVariable(final VariableInitializer variableInitializer) {
        this.assertNotNull(variableInitializer);
        this.variables.add(variableInitializer);
        variableInitializer.setParent(this);
    }
    
    public List<VariableInitializer> getVariables() {
        return this.variables;
    }
    
    public boolean isConst() {
        return this.type == 154;
    }
    
    public boolean isLet() {
        return this.type == 153;
    }
    
    public boolean isStatement() {
        return this.isStatement;
    }
    
    public boolean isVar() {
        return this.type == 122;
    }
    
    public void setIsStatement(final boolean isStatement) {
        this.isStatement = isStatement;
    }
    
    @Override
    public Node setType(final int type) {
        if (type != 122 && type != 154 && type != 153) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invalid decl type: ");
            sb.append(type);
            throw new IllegalArgumentException(sb.toString());
        }
        return super.setType(type);
    }
    
    public void setVariables(final List<VariableInitializer> list) {
        this.assertNotNull(list);
        this.variables.clear();
        final Iterator<VariableInitializer> iterator = list.iterator();
        while (iterator.hasNext()) {
            this.addVariable(iterator.next());
        }
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(n));
        sb.append(this.declTypeName());
        sb.append(" ");
        this.printList(this.variables, sb);
        if (this.isStatement()) {
            sb.append(";\n");
        }
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this)) {
            final Iterator<VariableInitializer> iterator = this.variables.iterator();
            while (iterator.hasNext()) {
                iterator.next().visit(nodeVisitor);
            }
        }
    }
}
