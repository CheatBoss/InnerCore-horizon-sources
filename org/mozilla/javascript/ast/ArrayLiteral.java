package org.mozilla.javascript.ast;

import java.util.*;

public class ArrayLiteral extends AstNode implements DestructuringForm
{
    private static final List<AstNode> NO_ELEMS;
    private int destructuringLength;
    private List<AstNode> elements;
    private boolean isDestructuring;
    private int skipCount;
    
    static {
        NO_ELEMS = Collections.unmodifiableList((List<? extends AstNode>)new ArrayList<AstNode>());
    }
    
    public ArrayLiteral() {
        this.type = 65;
    }
    
    public ArrayLiteral(final int n) {
        super(n);
        this.type = 65;
    }
    
    public ArrayLiteral(final int n, final int n2) {
        super(n, n2);
        this.type = 65;
    }
    
    public void addElement(final AstNode astNode) {
        this.assertNotNull(astNode);
        if (this.elements == null) {
            this.elements = new ArrayList<AstNode>();
        }
        this.elements.add(astNode);
        astNode.setParent(this);
    }
    
    public int getDestructuringLength() {
        return this.destructuringLength;
    }
    
    public AstNode getElement(final int n) {
        if (this.elements == null) {
            throw new IndexOutOfBoundsException("no elements");
        }
        return this.elements.get(n);
    }
    
    public List<AstNode> getElements() {
        if (this.elements != null) {
            return this.elements;
        }
        return ArrayLiteral.NO_ELEMS;
    }
    
    public int getSize() {
        if (this.elements == null) {
            return 0;
        }
        return this.elements.size();
    }
    
    public int getSkipCount() {
        return this.skipCount;
    }
    
    @Override
    public boolean isDestructuring() {
        return this.isDestructuring;
    }
    
    public void setDestructuringLength(final int destructuringLength) {
        this.destructuringLength = destructuringLength;
    }
    
    public void setElements(final List<AstNode> list) {
        if (list == null) {
            this.elements = null;
            return;
        }
        if (this.elements != null) {
            this.elements.clear();
        }
        final Iterator<AstNode> iterator = list.iterator();
        while (iterator.hasNext()) {
            this.addElement(iterator.next());
        }
    }
    
    @Override
    public void setIsDestructuring(final boolean isDestructuring) {
        this.isDestructuring = isDestructuring;
    }
    
    public void setSkipCount(final int skipCount) {
        this.skipCount = skipCount;
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(n));
        sb.append("[");
        if (this.elements != null) {
            this.printList(this.elements, sb);
        }
        sb.append("]");
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this)) {
            final Iterator<AstNode> iterator = this.getElements().iterator();
            while (iterator.hasNext()) {
                iterator.next().visit(nodeVisitor);
            }
        }
    }
}
