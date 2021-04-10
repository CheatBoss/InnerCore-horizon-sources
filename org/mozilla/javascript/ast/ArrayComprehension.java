package org.mozilla.javascript.ast;

import java.util.*;

public class ArrayComprehension extends Scope
{
    private AstNode filter;
    private int ifPosition;
    private List<ArrayComprehensionLoop> loops;
    private int lp;
    private AstNode result;
    private int rp;
    
    public ArrayComprehension() {
        this.loops = new ArrayList<ArrayComprehensionLoop>();
        this.ifPosition = -1;
        this.lp = -1;
        this.rp = -1;
        this.type = 157;
    }
    
    public ArrayComprehension(final int n) {
        super(n);
        this.loops = new ArrayList<ArrayComprehensionLoop>();
        this.ifPosition = -1;
        this.lp = -1;
        this.rp = -1;
        this.type = 157;
    }
    
    public ArrayComprehension(final int n, final int n2) {
        super(n, n2);
        this.loops = new ArrayList<ArrayComprehensionLoop>();
        this.ifPosition = -1;
        this.lp = -1;
        this.rp = -1;
        this.type = 157;
    }
    
    public void addLoop(final ArrayComprehensionLoop arrayComprehensionLoop) {
        this.assertNotNull(arrayComprehensionLoop);
        this.loops.add(arrayComprehensionLoop);
        arrayComprehensionLoop.setParent(this);
    }
    
    public AstNode getFilter() {
        return this.filter;
    }
    
    public int getFilterLp() {
        return this.lp;
    }
    
    public int getFilterRp() {
        return this.rp;
    }
    
    public int getIfPosition() {
        return this.ifPosition;
    }
    
    public List<ArrayComprehensionLoop> getLoops() {
        return this.loops;
    }
    
    public AstNode getResult() {
        return this.result;
    }
    
    public void setFilter(final AstNode filter) {
        this.filter = filter;
        if (filter != null) {
            filter.setParent(this);
        }
    }
    
    public void setFilterLp(final int lp) {
        this.lp = lp;
    }
    
    public void setFilterRp(final int rp) {
        this.rp = rp;
    }
    
    public void setIfPosition(final int ifPosition) {
        this.ifPosition = ifPosition;
    }
    
    public void setLoops(final List<ArrayComprehensionLoop> list) {
        this.assertNotNull(list);
        this.loops.clear();
        final Iterator<ArrayComprehensionLoop> iterator = list.iterator();
        while (iterator.hasNext()) {
            this.addLoop(iterator.next());
        }
    }
    
    public void setResult(final AstNode result) {
        this.assertNotNull(result);
        (this.result = result).setParent(this);
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder(250);
        sb.append("[");
        sb.append(this.result.toSource(0));
        final Iterator<ArrayComprehensionLoop> iterator = this.loops.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next().toSource(0));
        }
        if (this.filter != null) {
            sb.append(" if (");
            sb.append(this.filter.toSource(0));
            sb.append(")");
        }
        sb.append("]");
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (!nodeVisitor.visit(this)) {
            return;
        }
        this.result.visit(nodeVisitor);
        final Iterator<ArrayComprehensionLoop> iterator = this.loops.iterator();
        while (iterator.hasNext()) {
            iterator.next().visit(nodeVisitor);
        }
        if (this.filter != null) {
            this.filter.visit(nodeVisitor);
        }
    }
}
