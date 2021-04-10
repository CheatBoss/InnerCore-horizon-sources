package org.mozilla.javascript.ast;

public class ForInLoop extends Loop
{
    protected int eachPosition;
    protected int inPosition;
    protected boolean isForEach;
    protected AstNode iteratedObject;
    protected AstNode iterator;
    
    public ForInLoop() {
        this.inPosition = -1;
        this.eachPosition = -1;
        this.type = 119;
    }
    
    public ForInLoop(final int n) {
        super(n);
        this.inPosition = -1;
        this.eachPosition = -1;
        this.type = 119;
    }
    
    public ForInLoop(final int n, final int n2) {
        super(n, n2);
        this.inPosition = -1;
        this.eachPosition = -1;
        this.type = 119;
    }
    
    public int getEachPosition() {
        return this.eachPosition;
    }
    
    public int getInPosition() {
        return this.inPosition;
    }
    
    public AstNode getIteratedObject() {
        return this.iteratedObject;
    }
    
    public AstNode getIterator() {
        return this.iterator;
    }
    
    public boolean isForEach() {
        return this.isForEach;
    }
    
    public void setEachPosition(final int eachPosition) {
        this.eachPosition = eachPosition;
    }
    
    public void setInPosition(final int inPosition) {
        this.inPosition = inPosition;
    }
    
    public void setIsForEach(final boolean isForEach) {
        this.isForEach = isForEach;
    }
    
    public void setIteratedObject(final AstNode iteratedObject) {
        this.assertNotNull(iteratedObject);
        (this.iteratedObject = iteratedObject).setParent(this);
    }
    
    public void setIterator(final AstNode iterator) {
        this.assertNotNull(iterator);
        (this.iterator = iterator).setParent(this);
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(n));
        sb.append("for ");
        if (this.isForEach()) {
            sb.append("each ");
        }
        sb.append("(");
        sb.append(this.iterator.toSource(0));
        sb.append(" in ");
        sb.append(this.iteratedObject.toSource(0));
        sb.append(") ");
        if (this.body.getType() == 129) {
            sb.append(this.body.toSource(n).trim());
            sb.append("\n");
        }
        else {
            sb.append("\n");
            sb.append(this.body.toSource(n + 1));
        }
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this)) {
            this.iterator.visit(nodeVisitor);
            this.iteratedObject.visit(nodeVisitor);
            this.body.visit(nodeVisitor);
        }
    }
}
