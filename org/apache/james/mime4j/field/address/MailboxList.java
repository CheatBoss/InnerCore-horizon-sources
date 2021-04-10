package org.apache.james.mime4j.field.address;

import java.io.*;
import java.util.*;

public class MailboxList extends AbstractList<Mailbox> implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final List<Mailbox> mailboxes;
    
    public MailboxList(List<Mailbox> mailboxes, final boolean b) {
        if (mailboxes != null) {
            if (!b) {
                mailboxes = new ArrayList<Mailbox>(mailboxes);
            }
            this.mailboxes = mailboxes;
            return;
        }
        this.mailboxes = Collections.emptyList();
    }
    
    @Override
    public Mailbox get(final int n) {
        return this.mailboxes.get(n);
    }
    
    public void print() {
        for (int i = 0; i < this.size(); ++i) {
            System.out.println(this.get(i).toString());
        }
    }
    
    @Override
    public int size() {
        return this.mailboxes.size();
    }
}
