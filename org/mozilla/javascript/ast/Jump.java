package org.mozilla.javascript.ast;

import org.mozilla.javascript.*;

public class Jump extends AstNode
{
    private Jump jumpNode;
    public Node target;
    private Node target2;
    
    public Jump() {
        this.type = -1;
    }
    
    public Jump(final int type) {
        this.type = type;
    }
    
    public Jump(final int n, final int lineno) {
        this(n);
        this.setLineno(lineno);
    }
    
    public Jump(final int n, final Node node) {
        this(n);
        this.addChildToBack(node);
    }
    
    public Jump(final int n, final Node node, final int lineno) {
        this(n, node);
        this.setLineno(lineno);
    }
    
    public Node getContinue() {
        if (this.type != 132) {
            codeBug();
        }
        return this.target2;
    }
    
    public Node getDefault() {
        if (this.type != 114) {
            codeBug();
        }
        return this.target2;
    }
    
    public Node getFinally() {
        if (this.type != 81) {
            codeBug();
        }
        return this.target2;
    }
    
    public Jump getJumpStatement() {
        if (this.type != 120 && this.type != 121) {
            codeBug();
        }
        return this.jumpNode;
    }
    
    public Jump getLoop() {
        if (this.type != 130) {
            codeBug();
        }
        return this.jumpNode;
    }
    
    public void setContinue(final Node target2) {
        if (this.type != 132) {
            codeBug();
        }
        if (target2.getType() != 131) {
            codeBug();
        }
        if (this.target2 != null) {
            codeBug();
        }
        this.target2 = target2;
    }
    
    public void setDefault(final Node target2) {
        if (this.type != 114) {
            codeBug();
        }
        if (target2.getType() != 131) {
            codeBug();
        }
        if (this.target2 != null) {
            codeBug();
        }
        this.target2 = target2;
    }
    
    public void setFinally(final Node target2) {
        if (this.type != 81) {
            codeBug();
        }
        if (target2.getType() != 131) {
            codeBug();
        }
        if (this.target2 != null) {
            codeBug();
        }
        this.target2 = target2;
    }
    
    public void setJumpStatement(final Jump jumpNode) {
        if (this.type != 120 && this.type != 121) {
            codeBug();
        }
        if (jumpNode == null) {
            codeBug();
        }
        if (this.jumpNode != null) {
            codeBug();
        }
        this.jumpNode = jumpNode;
    }
    
    public void setLoop(final Jump jumpNode) {
        if (this.type != 130) {
            codeBug();
        }
        if (jumpNode == null) {
            codeBug();
        }
        if (this.jumpNode != null) {
            codeBug();
        }
        this.jumpNode = jumpNode;
    }
    
    @Override
    public String toSource(final int n) {
        throw new UnsupportedOperationException(this.toString());
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        throw new UnsupportedOperationException(this.toString());
    }
}
