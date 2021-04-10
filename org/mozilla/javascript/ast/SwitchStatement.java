package org.mozilla.javascript.ast;

import java.util.*;

public class SwitchStatement extends Jump
{
    private static final List<SwitchCase> NO_CASES;
    private List<SwitchCase> cases;
    private AstNode expression;
    private int lp;
    private int rp;
    
    static {
        NO_CASES = Collections.unmodifiableList((List<? extends SwitchCase>)new ArrayList<SwitchCase>());
    }
    
    public SwitchStatement() {
        this.lp = -1;
        this.rp = -1;
        this.type = 114;
    }
    
    public SwitchStatement(final int position) {
        this.lp = -1;
        this.rp = -1;
        this.type = 114;
        this.position = position;
    }
    
    public SwitchStatement(final int position, final int length) {
        this.lp = -1;
        this.rp = -1;
        this.type = 114;
        this.position = position;
        this.length = length;
    }
    
    public void addCase(final SwitchCase switchCase) {
        this.assertNotNull(switchCase);
        if (this.cases == null) {
            this.cases = new ArrayList<SwitchCase>();
        }
        this.cases.add(switchCase);
        switchCase.setParent(this);
    }
    
    public List<SwitchCase> getCases() {
        if (this.cases != null) {
            return this.cases;
        }
        return SwitchStatement.NO_CASES;
    }
    
    public AstNode getExpression() {
        return this.expression;
    }
    
    public int getLp() {
        return this.lp;
    }
    
    public int getRp() {
        return this.rp;
    }
    
    public void setCases(final List<SwitchCase> list) {
        if (list == null) {
            this.cases = null;
            return;
        }
        if (this.cases != null) {
            this.cases.clear();
        }
        final Iterator<SwitchCase> iterator = list.iterator();
        while (iterator.hasNext()) {
            this.addCase(iterator.next());
        }
    }
    
    public void setExpression(final AstNode expression) {
        this.assertNotNull(expression);
        (this.expression = expression).setParent(this);
    }
    
    public void setLp(final int lp) {
        this.lp = lp;
    }
    
    public void setParens(final int lp, final int rp) {
        this.lp = lp;
        this.rp = rp;
    }
    
    public void setRp(final int rp) {
        this.rp = rp;
    }
    
    @Override
    public String toSource(final int n) {
        final String indent = this.makeIndent(n);
        final StringBuilder sb = new StringBuilder();
        sb.append(indent);
        sb.append("switch (");
        sb.append(this.expression.toSource(0));
        sb.append(") {\n");
        if (this.cases != null) {
            final Iterator<SwitchCase> iterator = this.cases.iterator();
            while (iterator.hasNext()) {
                sb.append(iterator.next().toSource(n + 1));
            }
        }
        sb.append(indent);
        sb.append("}\n");
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this)) {
            this.expression.visit(nodeVisitor);
            final Iterator<SwitchCase> iterator = this.getCases().iterator();
            while (iterator.hasNext()) {
                iterator.next().visit(nodeVisitor);
            }
        }
    }
}
