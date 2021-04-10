package org.apache.james.mime4j.field.address;

import org.apache.james.mime4j.codec.*;
import org.apache.james.mime4j.field.address.parser.*;
import java.util.*;
import java.util.function.*;

class Builder
{
    private static Builder singleton;
    
    static {
        Builder.singleton = new Builder();
    }
    
    private void addSpecials(final StringBuilder sb, final Token token) {
        if (token != null) {
            this.addSpecials(sb, token.specialToken);
            sb.append(token.image);
        }
    }
    
    private Mailbox buildAddrSpec(final DomainList list, final ASTaddr_spec asTaddr_spec) {
        final ChildNodeIterator childNodeIterator = new ChildNodeIterator(asTaddr_spec);
        return new Mailbox(list, this.buildString((SimpleNode)childNodeIterator.next(), true), this.buildString((SimpleNode)childNodeIterator.next(), true));
    }
    
    private Mailbox buildAddrSpec(final ASTaddr_spec asTaddr_spec) {
        return this.buildAddrSpec(null, asTaddr_spec);
    }
    
    private Mailbox buildAngleAddr(final ASTangle_addr asTangle_addr) {
        final ChildNodeIterator childNodeIterator = new ChildNodeIterator(asTangle_addr);
        Node node = childNodeIterator.next();
        DomainList buildRoute;
        if (node instanceof ASTroute) {
            buildRoute = this.buildRoute((ASTroute)node);
            node = childNodeIterator.next();
        }
        else {
            if (!(node instanceof ASTaddr_spec)) {
                throw new IllegalStateException();
            }
            buildRoute = null;
        }
        if (node instanceof ASTaddr_spec) {
            return this.buildAddrSpec(buildRoute, (ASTaddr_spec)node);
        }
        throw new IllegalStateException();
    }
    
    private MailboxList buildGroupBody(final ASTgroup_body asTgroup_body) {
        final ArrayList<Mailbox> list = new ArrayList<Mailbox>();
        final ChildNodeIterator childNodeIterator = new ChildNodeIterator(asTgroup_body);
        while (childNodeIterator.hasNext()) {
            final Node next = childNodeIterator.next();
            if (!(next instanceof ASTmailbox)) {
                throw new IllegalStateException();
            }
            list.add(this.buildMailbox((ASTmailbox)next));
        }
        return new MailboxList(list, true);
    }
    
    private Mailbox buildNameAddr(final ASTname_addr asTname_addr) {
        final ChildNodeIterator childNodeIterator = new ChildNodeIterator(asTname_addr);
        final Node next = childNodeIterator.next();
        if (!(next instanceof ASTphrase)) {
            throw new IllegalStateException();
        }
        final String buildString = this.buildString((SimpleNode)next, false);
        final Node next2 = childNodeIterator.next();
        if (next2 instanceof ASTangle_addr) {
            return new Mailbox(DecoderUtil.decodeEncodedWords(buildString), this.buildAngleAddr((ASTangle_addr)next2));
        }
        throw new IllegalStateException();
    }
    
    private DomainList buildRoute(final ASTroute asTroute) {
        final ArrayList<String> list = new ArrayList<String>(asTroute.jjtGetNumChildren());
        final ChildNodeIterator childNodeIterator = new ChildNodeIterator(asTroute);
        while (childNodeIterator.hasNext()) {
            final Node next = childNodeIterator.next();
            if (!(next instanceof ASTdomain)) {
                throw new IllegalStateException();
            }
            list.add(this.buildString((SimpleNode)next, true));
        }
        return new DomainList(list, true);
    }
    
    private String buildString(final SimpleNode simpleNode, final boolean b) {
        final Token firstToken = simpleNode.firstToken;
        final Token lastToken = simpleNode.lastToken;
        final StringBuilder sb = new StringBuilder();
        Token token;
        for (Token next = firstToken; next != lastToken; next = token) {
            sb.append(next.image);
            token = (next = next.next);
            if (!b) {
                this.addSpecials(sb, token.specialToken);
            }
        }
        sb.append(lastToken.image);
        return sb.toString();
    }
    
    public static Builder getInstance() {
        return Builder.singleton;
    }
    
    public Address buildAddress(final ASTaddress asTaddress) {
        final ChildNodeIterator childNodeIterator = new ChildNodeIterator(asTaddress);
        final Node next = childNodeIterator.next();
        if (next instanceof ASTaddr_spec) {
            return this.buildAddrSpec((ASTaddr_spec)next);
        }
        if (next instanceof ASTangle_addr) {
            return this.buildAngleAddr((ASTangle_addr)next);
        }
        if (!(next instanceof ASTphrase)) {
            throw new IllegalStateException();
        }
        final String buildString = this.buildString((SimpleNode)next, false);
        final Node next2 = childNodeIterator.next();
        if (next2 instanceof ASTgroup_body) {
            return new Group(buildString, this.buildGroupBody((ASTgroup_body)next2));
        }
        if (next2 instanceof ASTangle_addr) {
            return new Mailbox(DecoderUtil.decodeEncodedWords(buildString), this.buildAngleAddr((ASTangle_addr)next2));
        }
        throw new IllegalStateException();
    }
    
    public AddressList buildAddressList(final ASTaddress_list asTaddress_list) {
        final ArrayList<Address> list = new ArrayList<Address>();
        for (int i = 0; i < asTaddress_list.jjtGetNumChildren(); ++i) {
            list.add(this.buildAddress((ASTaddress)asTaddress_list.jjtGetChild(i)));
        }
        return new AddressList(list, true);
    }
    
    public Mailbox buildMailbox(final ASTmailbox asTmailbox) {
        final Node next = new ChildNodeIterator(asTmailbox).next();
        if (next instanceof ASTaddr_spec) {
            return this.buildAddrSpec((ASTaddr_spec)next);
        }
        if (next instanceof ASTangle_addr) {
            return this.buildAngleAddr((ASTangle_addr)next);
        }
        if (next instanceof ASTname_addr) {
            return this.buildNameAddr((ASTname_addr)next);
        }
        throw new IllegalStateException();
    }
    
    private static class ChildNodeIterator implements Iterator<Node>
    {
        private int index;
        private int len;
        private SimpleNode simpleNode;
        
        public ChildNodeIterator(final SimpleNode simpleNode) {
            this.simpleNode = simpleNode;
            this.len = simpleNode.jjtGetNumChildren();
            this.index = 0;
        }
        
        @Override
        public void forEachRemaining(final Consumer<?> p0) {
            // 
            // This method could not be decompiled.
            // 
            // Could not show original bytecode, likely due to the same error.
            // 
            // The error that occurred was:
            // 
            // java.lang.NullPointerException
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        @Override
        public boolean hasNext() {
            return this.index < this.len;
        }
        
        @Override
        public Node next() {
            return this.simpleNode.jjtGetChild(this.index++);
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
