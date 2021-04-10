package org.mozilla.javascript.ast;

import java.util.*;

public class XmlLiteral extends AstNode
{
    private List<XmlFragment> fragments;
    
    public XmlLiteral() {
        this.fragments = new ArrayList<XmlFragment>();
        this.type = 145;
    }
    
    public XmlLiteral(final int n) {
        super(n);
        this.fragments = new ArrayList<XmlFragment>();
        this.type = 145;
    }
    
    public XmlLiteral(final int n, final int n2) {
        super(n, n2);
        this.fragments = new ArrayList<XmlFragment>();
        this.type = 145;
    }
    
    public void addFragment(final XmlFragment xmlFragment) {
        this.assertNotNull(xmlFragment);
        this.fragments.add(xmlFragment);
        xmlFragment.setParent(this);
    }
    
    public List<XmlFragment> getFragments() {
        return this.fragments;
    }
    
    public void setFragments(final List<XmlFragment> list) {
        this.assertNotNull(list);
        this.fragments.clear();
        final Iterator<XmlFragment> iterator = list.iterator();
        while (iterator.hasNext()) {
            this.addFragment(iterator.next());
        }
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder(250);
        final Iterator<XmlFragment> iterator = this.fragments.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next().toSource(0));
        }
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this)) {
            final Iterator<XmlFragment> iterator = this.fragments.iterator();
            while (iterator.hasNext()) {
                iterator.next().visit(nodeVisitor);
            }
        }
    }
}
