package org.apache.james.mime4j.field.address;

import java.io.*;
import org.apache.james.mime4j.field.address.parser.*;
import java.util.*;

public abstract class Address implements Serializable
{
    private static final long serialVersionUID = 634090661990433426L;
    
    public static Address parse(final String s) {
        final AddressListParser addressListParser = new AddressListParser(new StringReader(s));
        try {
            return Builder.getInstance().buildAddress(addressListParser.parseAddress());
        }
        catch (ParseException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
    
    final void addMailboxesTo(final List<Mailbox> list) {
        this.doAddMailboxesTo(list);
    }
    
    protected abstract void doAddMailboxesTo(final List<Mailbox> p0);
    
    public final String getDisplayString() {
        return this.getDisplayString(false);
    }
    
    public abstract String getDisplayString(final boolean p0);
    
    public abstract String getEncodedString();
    
    @Override
    public String toString() {
        return this.getDisplayString(false);
    }
}
