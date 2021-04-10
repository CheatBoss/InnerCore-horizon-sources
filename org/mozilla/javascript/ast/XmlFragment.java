package org.mozilla.javascript.ast;

public abstract class XmlFragment extends AstNode
{
    public XmlFragment() {
        this.type = 145;
    }
    
    public XmlFragment(final int n) {
        super(n);
        this.type = 145;
    }
    
    public XmlFragment(final int n, final int n2) {
        super(n, n2);
        this.type = 145;
    }
}
