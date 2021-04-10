package org.mozilla.javascript.ast;

public abstract class XmlRef extends AstNode
{
    protected int atPos;
    protected int colonPos;
    protected Name namespace;
    
    public XmlRef() {
        this.atPos = -1;
        this.colonPos = -1;
    }
    
    public XmlRef(final int n) {
        super(n);
        this.atPos = -1;
        this.colonPos = -1;
    }
    
    public XmlRef(final int n, final int n2) {
        super(n, n2);
        this.atPos = -1;
        this.colonPos = -1;
    }
    
    public int getAtPos() {
        return this.atPos;
    }
    
    public int getColonPos() {
        return this.colonPos;
    }
    
    public Name getNamespace() {
        return this.namespace;
    }
    
    public boolean isAttributeAccess() {
        return this.atPos >= 0;
    }
    
    public void setAtPos(final int atPos) {
        this.atPos = atPos;
    }
    
    public void setColonPos(final int colonPos) {
        this.colonPos = colonPos;
    }
    
    public void setNamespace(final Name namespace) {
        this.namespace = namespace;
        if (namespace != null) {
            namespace.setParent(this);
        }
    }
}
