package org.mozilla.javascript.ast;

import java.util.*;
import org.mozilla.javascript.*;

public class AstRoot extends ScriptNode
{
    private SortedSet<Comment> comments;
    private boolean inStrictMode;
    
    public AstRoot() {
        this.type = 136;
    }
    
    public AstRoot(final int n) {
        super(n);
        this.type = 136;
    }
    
    public void addComment(final Comment comment) {
        this.assertNotNull(comment);
        if (this.comments == null) {
            this.comments = new TreeSet<Comment>(new PositionComparator());
        }
        this.comments.add(comment);
        comment.setParent(this);
    }
    
    public void checkParentLinks() {
        this.visit(new NodeVisitor() {
            @Override
            public boolean visit(final AstNode astNode) {
                if (astNode.getType() == 136) {
                    return true;
                }
                if (astNode.getParent() == null) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("No parent for node: ");
                    sb.append(astNode);
                    sb.append("\n");
                    sb.append(astNode.toSource(0));
                    throw new IllegalStateException(sb.toString());
                }
                return true;
            }
        });
    }
    
    @Override
    public String debugPrint() {
        final DebugPrintVisitor debugPrintVisitor = new DebugPrintVisitor(new StringBuilder(1000));
        this.visitAll(debugPrintVisitor);
        return debugPrintVisitor.toString();
    }
    
    public SortedSet<Comment> getComments() {
        return this.comments;
    }
    
    public boolean isInStrictMode() {
        return this.inStrictMode;
    }
    
    public void setComments(final SortedSet<Comment> set) {
        if (set == null) {
            this.comments = null;
            return;
        }
        if (this.comments != null) {
            this.comments.clear();
        }
        final Iterator<Comment> iterator = set.iterator();
        while (iterator.hasNext()) {
            this.addComment(iterator.next());
        }
    }
    
    public void setInStrictMode(final boolean inStrictMode) {
        this.inStrictMode = inStrictMode;
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        final Iterator<Node> iterator = this.iterator();
        while (iterator.hasNext()) {
            sb.append(((AstNode)iterator.next()).toSource(n));
        }
        return sb.toString();
    }
    
    public void visitAll(final NodeVisitor nodeVisitor) {
        this.visit(nodeVisitor);
        this.visitComments(nodeVisitor);
    }
    
    public void visitComments(final NodeVisitor nodeVisitor) {
        if (this.comments != null) {
            final Iterator<Comment> iterator = this.comments.iterator();
            while (iterator.hasNext()) {
                nodeVisitor.visit(iterator.next());
            }
        }
    }
}
