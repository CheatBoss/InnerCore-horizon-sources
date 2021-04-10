package org.apache.james.mime4j.field.address;

import java.util.*;
import org.apache.james.mime4j.codec.*;

public class Group extends Address
{
    private static final long serialVersionUID = 1L;
    private final MailboxList mailboxList;
    private final String name;
    
    public Group(final String s, final Collection<Mailbox> collection) {
        this(s, new MailboxList(new ArrayList<Mailbox>(collection), true));
    }
    
    public Group(final String name, final MailboxList mailboxList) {
        if (name == null) {
            throw new IllegalArgumentException();
        }
        if (mailboxList != null) {
            this.name = name;
            this.mailboxList = mailboxList;
            return;
        }
        throw new IllegalArgumentException();
    }
    
    public Group(final String s, final Mailbox... array) {
        this(s, new MailboxList(Arrays.asList(array), true));
    }
    
    public static Group parse(final String s) {
        final Address parse = Address.parse(s);
        if (parse instanceof Group) {
            return (Group)parse;
        }
        throw new IllegalArgumentException("Not a group address");
    }
    
    @Override
    protected void doAddMailboxesTo(final List<Mailbox> list) {
        final Iterator<Mailbox> iterator = this.mailboxList.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
    }
    
    @Override
    public String getDisplayString(final boolean b) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        sb.append(':');
        final Iterator<Mailbox> iterator = this.mailboxList.iterator();
        int n = 1;
        while (iterator.hasNext()) {
            final Mailbox mailbox = iterator.next();
            if (n != 0) {
                n = 0;
            }
            else {
                sb.append(',');
            }
            sb.append(' ');
            sb.append(mailbox.getDisplayString(b));
        }
        sb.append(";");
        return sb.toString();
    }
    
    @Override
    public String getEncodedString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(EncoderUtil.encodeAddressDisplayName(this.name));
        sb.append(':');
        final Iterator<Mailbox> iterator = this.mailboxList.iterator();
        int n = 1;
        while (iterator.hasNext()) {
            final Mailbox mailbox = iterator.next();
            if (n != 0) {
                n = 0;
            }
            else {
                sb.append(',');
            }
            sb.append(' ');
            sb.append(mailbox.getEncodedString());
        }
        sb.append(';');
        return sb.toString();
    }
    
    public MailboxList getMailboxes() {
        return this.mailboxList;
    }
    
    public String getName() {
        return this.name;
    }
}
