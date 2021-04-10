package org.apache.james.mime4j.field.address;

import java.io.*;
import org.apache.james.mime4j.field.address.parser.*;
import java.util.*;
import org.apache.james.mime4j.codec.*;

public class Mailbox extends Address
{
    private static final DomainList EMPTY_ROUTE_LIST;
    private static final long serialVersionUID = 1L;
    private final String domain;
    private final String localPart;
    private final String name;
    private final DomainList route;
    
    static {
        EMPTY_ROUTE_LIST = new DomainList(Collections.emptyList(), true);
    }
    
    public Mailbox(final String s, final String s2) {
        this(null, null, s, s2);
    }
    
    public Mailbox(final String s, final String s2, final String s3) {
        this(s, null, s2, s3);
    }
    
    public Mailbox(String domain, final DomainList list, final String localPart, final String s) {
        if (localPart != null && localPart.length() != 0) {
            String name = null;
            Label_0032: {
                if (domain != null) {
                    name = domain;
                    if (domain.length() != 0) {
                        break Label_0032;
                    }
                }
                name = null;
            }
            this.name = name;
            DomainList empty_ROUTE_LIST;
            if ((empty_ROUTE_LIST = list) == null) {
                empty_ROUTE_LIST = Mailbox.EMPTY_ROUTE_LIST;
            }
            this.route = empty_ROUTE_LIST;
            this.localPart = localPart;
            Label_0076: {
                if (s != null) {
                    domain = s;
                    if (s.length() != 0) {
                        break Label_0076;
                    }
                }
                domain = null;
            }
            this.domain = domain;
            return;
        }
        throw new IllegalArgumentException();
    }
    
    Mailbox(final String s, final Mailbox mailbox) {
        this(s, mailbox.getRoute(), mailbox.getLocalPart(), mailbox.getDomain());
    }
    
    public Mailbox(final DomainList list, final String s, final String s2) {
        this(null, list, s, s2);
    }
    
    private Object getCanonicalizedAddress() {
        if (this.domain == null) {
            return this.localPart;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(this.localPart);
        sb.append('@');
        sb.append(this.domain.toLowerCase(Locale.US));
        return sb.toString();
    }
    
    public static Mailbox parse(final String s) {
        final AddressListParser addressListParser = new AddressListParser(new StringReader(s));
        try {
            return Builder.getInstance().buildMailbox(addressListParser.parseMailbox());
        }
        catch (ParseException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
    
    @Override
    protected final void doAddMailboxesTo(final List<Mailbox> list) {
        list.add(this);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof Mailbox && this.getCanonicalizedAddress().equals(((Mailbox)o).getCanonicalizedAddress()));
    }
    
    public String getAddress() {
        if (this.domain == null) {
            return this.localPart;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(this.localPart);
        sb.append('@');
        sb.append(this.domain);
        return sb.toString();
    }
    
    @Override
    public String getDisplayString(final boolean b) {
        final DomainList route = this.route;
        final boolean b2 = false;
        final boolean b3 = b & route != null;
        boolean b4 = false;
        Label_0041: {
            if (this.name == null) {
                b4 = b2;
                if (!b3) {
                    break Label_0041;
                }
            }
            b4 = true;
        }
        final StringBuilder sb = new StringBuilder();
        final String name = this.name;
        if (name != null) {
            sb.append(name);
            sb.append(' ');
        }
        if (b4) {
            sb.append('<');
        }
        if (b3) {
            sb.append(this.route.toRouteString());
            sb.append(':');
        }
        sb.append(this.localPart);
        if (this.domain != null) {
            sb.append('@');
            sb.append(this.domain);
        }
        if (b4) {
            sb.append('>');
        }
        return sb.toString();
    }
    
    public String getDomain() {
        return this.domain;
    }
    
    @Override
    public String getEncodedString() {
        final StringBuilder sb = new StringBuilder();
        final String name = this.name;
        if (name != null) {
            sb.append(EncoderUtil.encodeAddressDisplayName(name));
            sb.append(" <");
        }
        sb.append(EncoderUtil.encodeAddressLocalPart(this.localPart));
        if (this.domain != null) {
            sb.append('@');
            sb.append(this.domain);
        }
        if (this.name != null) {
            sb.append('>');
        }
        return sb.toString();
    }
    
    public String getLocalPart() {
        return this.localPart;
    }
    
    public String getName() {
        return this.name;
    }
    
    public DomainList getRoute() {
        return this.route;
    }
    
    @Override
    public int hashCode() {
        return this.getCanonicalizedAddress().hashCode();
    }
}
