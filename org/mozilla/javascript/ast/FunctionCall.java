package org.mozilla.javascript.ast;

import java.util.*;

public class FunctionCall extends AstNode
{
    protected static final List<AstNode> NO_ARGS;
    protected List<AstNode> arguments;
    protected int lp;
    protected int rp;
    protected AstNode target;
    
    static {
        NO_ARGS = Collections.unmodifiableList((List<? extends AstNode>)new ArrayList<AstNode>());
    }
    
    public FunctionCall() {
        this.lp = -1;
        this.rp = -1;
        this.type = 38;
    }
    
    public FunctionCall(final int n) {
        super(n);
        this.lp = -1;
        this.rp = -1;
        this.type = 38;
    }
    
    public FunctionCall(final int n, final int n2) {
        super(n, n2);
        this.lp = -1;
        this.rp = -1;
        this.type = 38;
    }
    
    public void addArgument(final AstNode astNode) {
        this.assertNotNull(astNode);
        if (this.arguments == null) {
            this.arguments = new ArrayList<AstNode>();
        }
        this.arguments.add(astNode);
        astNode.setParent(this);
    }
    
    public List<AstNode> getArguments() {
        if (this.arguments != null) {
            return this.arguments;
        }
        return FunctionCall.NO_ARGS;
    }
    
    public int getLp() {
        return this.lp;
    }
    
    public int getRp() {
        return this.rp;
    }
    
    public AstNode getTarget() {
        return this.target;
    }
    
    public void setArguments(final List<AstNode> list) {
        if (list == null) {
            this.arguments = null;
            return;
        }
        if (this.arguments != null) {
            this.arguments.clear();
        }
        final Iterator<AstNode> iterator = list.iterator();
        while (iterator.hasNext()) {
            this.addArgument(iterator.next());
        }
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
    
    public void setTarget(final AstNode target) {
        this.assertNotNull(target);
        (this.target = target).setParent(this);
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(n));
        sb.append(this.target.toSource(0));
        sb.append("(");
        if (this.arguments != null) {
            this.printList(this.arguments, sb);
        }
        sb.append(")");
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
        }
    }
}
