package org.mozilla.javascript.ast;

import org.mozilla.javascript.*;

public class Symbol
{
    private Scope containingTable;
    private int declType;
    private int index;
    private String name;
    private Node node;
    
    public Symbol() {
        this.index = -1;
    }
    
    public Symbol(final int declType, final String name) {
        this.index = -1;
        this.setName(name);
        this.setDeclType(declType);
    }
    
    public Scope getContainingTable() {
        return this.containingTable;
    }
    
    public int getDeclType() {
        return this.declType;
    }
    
    public String getDeclTypeName() {
        return Token.typeToName(this.declType);
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Node getNode() {
        return this.node;
    }
    
    public void setContainingTable(final Scope containingTable) {
        this.containingTable = containingTable;
    }
    
    public void setDeclType(final int declType) {
        if (declType != 109 && declType != 87 && declType != 122 && declType != 153 && declType != 154) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid declType: ");
            sb.append(declType);
            throw new IllegalArgumentException(sb.toString());
        }
        this.declType = declType;
    }
    
    public void setIndex(final int index) {
        this.index = index;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setNode(final Node node) {
        this.node = node;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Symbol (");
        sb.append(this.getDeclTypeName());
        sb.append(") name=");
        sb.append(this.name);
        if (this.node != null) {
            sb.append(" line=");
            sb.append(this.node.getLineno());
        }
        return sb.toString();
    }
}
