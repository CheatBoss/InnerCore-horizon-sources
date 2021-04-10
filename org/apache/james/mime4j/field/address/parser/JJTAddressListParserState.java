package org.apache.james.mime4j.field.address.parser;

import java.util.*;

class JJTAddressListParserState
{
    private Stack<Integer> marks;
    private int mk;
    private boolean node_created;
    private Stack<Node> nodes;
    private int sp;
    
    JJTAddressListParserState() {
        this.nodes = new Stack<Node>();
        this.marks = new Stack<Integer>();
        this.sp = 0;
        this.mk = 0;
    }
    
    void clearNodeScope(final Node node) {
        while (this.sp > this.mk) {
            this.popNode();
        }
        this.mk = this.marks.pop();
    }
    
    void closeNodeScope(final Node node, int n) {
        this.mk = this.marks.pop();
        while (true) {
            final int n2 = n - 1;
            if (n <= 0) {
                break;
            }
            final Node popNode = this.popNode();
            popNode.jjtSetParent(node);
            node.jjtAddChild(popNode, n2);
            n = n2;
        }
        node.jjtClose();
        this.pushNode(node);
        this.node_created = true;
    }
    
    void closeNodeScope(final Node node, final boolean b) {
        boolean node_created;
        if (b) {
            int nodeArity = this.nodeArity();
            this.mk = this.marks.pop();
            while (true) {
                final int n = nodeArity - 1;
                if (nodeArity <= 0) {
                    break;
                }
                final Node popNode = this.popNode();
                popNode.jjtSetParent(node);
                node.jjtAddChild(popNode, n);
                nodeArity = n;
            }
            node.jjtClose();
            this.pushNode(node);
            node_created = true;
        }
        else {
            this.mk = this.marks.pop();
            node_created = false;
        }
        this.node_created = node_created;
    }
    
    int nodeArity() {
        return this.sp - this.mk;
    }
    
    boolean nodeCreated() {
        return this.node_created;
    }
    
    void openNodeScope(final Node node) {
        this.marks.push(new Integer(this.mk));
        this.mk = this.sp;
        node.jjtOpen();
    }
    
    Node peekNode() {
        return this.nodes.peek();
    }
    
    Node popNode() {
        final int sp = this.sp - 1;
        this.sp = sp;
        if (sp < this.mk) {
            this.mk = this.marks.pop();
        }
        return this.nodes.pop();
    }
    
    void pushNode(final Node node) {
        this.nodes.push(node);
        ++this.sp;
    }
    
    void reset() {
        this.nodes.removeAllElements();
        this.marks.removeAllElements();
        this.sp = 0;
        this.mk = 0;
    }
    
    Node rootNode() {
        return this.nodes.elementAt(0);
    }
}
