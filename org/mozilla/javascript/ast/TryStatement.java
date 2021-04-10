package org.mozilla.javascript.ast;

import java.util.*;

public class TryStatement extends AstNode
{
    private static final List<CatchClause> NO_CATCHES;
    private List<CatchClause> catchClauses;
    private AstNode finallyBlock;
    private int finallyPosition;
    private AstNode tryBlock;
    
    static {
        NO_CATCHES = Collections.unmodifiableList((List<? extends CatchClause>)new ArrayList<CatchClause>());
    }
    
    public TryStatement() {
        this.finallyPosition = -1;
        this.type = 81;
    }
    
    public TryStatement(final int n) {
        super(n);
        this.finallyPosition = -1;
        this.type = 81;
    }
    
    public TryStatement(final int n, final int n2) {
        super(n, n2);
        this.finallyPosition = -1;
        this.type = 81;
    }
    
    public void addCatchClause(final CatchClause catchClause) {
        this.assertNotNull(catchClause);
        if (this.catchClauses == null) {
            this.catchClauses = new ArrayList<CatchClause>();
        }
        this.catchClauses.add(catchClause);
        catchClause.setParent(this);
    }
    
    public List<CatchClause> getCatchClauses() {
        if (this.catchClauses != null) {
            return this.catchClauses;
        }
        return TryStatement.NO_CATCHES;
    }
    
    public AstNode getFinallyBlock() {
        return this.finallyBlock;
    }
    
    public int getFinallyPosition() {
        return this.finallyPosition;
    }
    
    public AstNode getTryBlock() {
        return this.tryBlock;
    }
    
    public void setCatchClauses(final List<CatchClause> list) {
        if (list == null) {
            this.catchClauses = null;
            return;
        }
        if (this.catchClauses != null) {
            this.catchClauses.clear();
        }
        final Iterator<CatchClause> iterator = list.iterator();
        while (iterator.hasNext()) {
            this.addCatchClause(iterator.next());
        }
    }
    
    public void setFinallyBlock(final AstNode finallyBlock) {
        this.finallyBlock = finallyBlock;
        if (finallyBlock != null) {
            finallyBlock.setParent(this);
        }
    }
    
    public void setFinallyPosition(final int finallyPosition) {
        this.finallyPosition = finallyPosition;
    }
    
    public void setTryBlock(final AstNode tryBlock) {
        this.assertNotNull(tryBlock);
        (this.tryBlock = tryBlock).setParent(this);
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder(250);
        sb.append(this.makeIndent(n));
        sb.append("try ");
        sb.append(this.tryBlock.toSource(n).trim());
        final Iterator<CatchClause> iterator = this.getCatchClauses().iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next().toSource(n));
        }
        if (this.finallyBlock != null) {
            sb.append(" finally ");
            sb.append(this.finallyBlock.toSource(n));
        }
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this)) {
            this.tryBlock.visit(nodeVisitor);
            final Iterator<CatchClause> iterator = this.getCatchClauses().iterator();
            while (iterator.hasNext()) {
                iterator.next().visit(nodeVisitor);
            }
            if (this.finallyBlock != null) {
                this.finallyBlock.visit(nodeVisitor);
            }
        }
    }
}
