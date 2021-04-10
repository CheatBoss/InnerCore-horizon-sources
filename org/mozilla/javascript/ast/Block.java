package org.mozilla.javascript.ast;

import org.mozilla.javascript.*;
import java.util.*;

public class Block extends AstNode
{
    public Block() {
        this.type = 129;
    }
    
    public Block(final int n) {
        super(n);
        this.type = 129;
    }
    
    public Block(final int n, final int n2) {
        super(n, n2);
        this.type = 129;
    }
    
    public void addStatement(final AstNode astNode) {
        this.addChild(astNode);
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(n));
        sb.append("{\n");
        final Iterator<Node> iterator = this.iterator();
        while (iterator.hasNext()) {
            sb.append(((AstNode)iterator.next()).toSource(n + 1));
        }
        sb.append(this.makeIndent(n));
        sb.append("}\n");
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this)) {
            final Iterator<Node> iterator = this.iterator();
            while (iterator.hasNext()) {
                ((AstNode)iterator.next()).visit(nodeVisitor);
            }
        }
    }
}
