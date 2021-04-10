package org.apache.james.mime4j.field.address.parser;

public class SimpleNode extends BaseNode implements Node
{
    protected Node[] children;
    protected int id;
    protected Node parent;
    protected AddressListParser parser;
    
    public SimpleNode(final int id) {
        this.id = id;
    }
    
    public SimpleNode(final AddressListParser parser, final int n) {
        this(n);
        this.parser = parser;
    }
    
    public Object childrenAccept(final AddressListParserVisitor addressListParserVisitor, final Object o) {
        if (this.children != null) {
            int n = 0;
            while (true) {
                final Node[] children = this.children;
                if (n >= children.length) {
                    break;
                }
                children[n].jjtAccept(addressListParserVisitor, o);
                ++n;
            }
        }
        return o;
    }
    
    public void dump(final String s) {
        System.out.println(this.toString(s));
        if (this.children != null) {
            int n = 0;
            while (true) {
                final Node[] children = this.children;
                if (n >= children.length) {
                    break;
                }
                final SimpleNode simpleNode = (SimpleNode)children[n];
                if (simpleNode != null) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(s);
                    sb.append(" ");
                    simpleNode.dump(sb.toString());
                }
                ++n;
            }
        }
    }
    
    @Override
    public Object jjtAccept(final AddressListParserVisitor addressListParserVisitor, final Object o) {
        return addressListParserVisitor.visit(this, o);
    }
    
    @Override
    public void jjtAddChild(final Node node, final int n) {
        final Node[] children = this.children;
        if (children == null) {
            this.children = new Node[n + 1];
        }
        else if (n >= children.length) {
            final Node[] children2 = new Node[n + 1];
            System.arraycopy(children, 0, children2, 0, children.length);
            this.children = children2;
        }
        this.children[n] = node;
    }
    
    @Override
    public void jjtClose() {
    }
    
    @Override
    public Node jjtGetChild(final int n) {
        return this.children[n];
    }
    
    @Override
    public int jjtGetNumChildren() {
        final Node[] children = this.children;
        if (children == null) {
            return 0;
        }
        return children.length;
    }
    
    @Override
    public Node jjtGetParent() {
        return this.parent;
    }
    
    @Override
    public void jjtOpen() {
    }
    
    @Override
    public void jjtSetParent(final Node parent) {
        this.parent = parent;
    }
    
    @Override
    public String toString() {
        return AddressListParserTreeConstants.jjtNodeName[this.id];
    }
    
    public String toString(final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(this.toString());
        return sb.toString();
    }
}
