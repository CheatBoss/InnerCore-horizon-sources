package org.mozilla.javascript.ast;

import java.util.*;

public class ObjectLiteral extends AstNode implements DestructuringForm
{
    private static final List<ObjectProperty> NO_ELEMS;
    private List<ObjectProperty> elements;
    boolean isDestructuring;
    
    static {
        NO_ELEMS = Collections.unmodifiableList((List<? extends ObjectProperty>)new ArrayList<ObjectProperty>());
    }
    
    public ObjectLiteral() {
        this.type = 66;
    }
    
    public ObjectLiteral(final int n) {
        super(n);
        this.type = 66;
    }
    
    public ObjectLiteral(final int n, final int n2) {
        super(n, n2);
        this.type = 66;
    }
    
    public void addElement(final ObjectProperty objectProperty) {
        this.assertNotNull(objectProperty);
        if (this.elements == null) {
            this.elements = new ArrayList<ObjectProperty>();
        }
        this.elements.add(objectProperty);
        objectProperty.setParent(this);
    }
    
    public List<ObjectProperty> getElements() {
        if (this.elements != null) {
            return this.elements;
        }
        return ObjectLiteral.NO_ELEMS;
    }
    
    @Override
    public boolean isDestructuring() {
        return this.isDestructuring;
    }
    
    public void setElements(final List<ObjectProperty> list) {
        if (list == null) {
            this.elements = null;
            return;
        }
        if (this.elements != null) {
            this.elements.clear();
        }
        final Iterator<ObjectProperty> iterator = list.iterator();
        while (iterator.hasNext()) {
            this.addElement(iterator.next());
        }
    }
    
    @Override
    public void setIsDestructuring(final boolean isDestructuring) {
        this.isDestructuring = isDestructuring;
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(n));
        sb.append("{");
        if (this.elements != null) {
            this.printList(this.elements, sb);
        }
        sb.append("}");
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this)) {
            final Iterator<ObjectProperty> iterator = this.getElements().iterator();
            while (iterator.hasNext()) {
                iterator.next().visit(nodeVisitor);
            }
        }
    }
}
