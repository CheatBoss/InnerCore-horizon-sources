package org.apache.james.mime4j.field.address;

import java.io.*;
import org.apache.james.mime4j.field.address.parser.*;
import java.util.*;

public class AddressList extends AbstractList<Address> implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final List<? extends Address> addresses;
    
    public AddressList(List<? extends Address> addresses, final boolean b) {
        if (addresses != null) {
            if (!b) {
                addresses = new ArrayList<Address>(addresses);
            }
            this.addresses = addresses;
            return;
        }
        this.addresses = Collections.emptyList();
    }
    
    public static void main(String[] array) throws Exception {
        array = (String[])(Object)new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                while (true) {
                    System.out.print("> ");
                    final String line = ((BufferedReader)(Object)array).readLine();
                    if (line.length() == 0 || line.toLowerCase().equals("exit") || line.toLowerCase().equals("quit")) {
                        break;
                    }
                    parse(line).print();
                }
                System.out.println("Goodbye.");
            }
            catch (Exception ex) {
                ex.printStackTrace();
                Thread.sleep(300L);
                continue;
            }
            break;
        }
    }
    
    public static AddressList parse(final String s) throws ParseException {
        return Builder.getInstance().buildAddressList(new AddressListParser(new StringReader(s)).parseAddressList());
    }
    
    public MailboxList flatten() {
        final Iterator<? extends Address> iterator = this.addresses.iterator();
        while (true) {
            while (iterator.hasNext()) {
                if (!(((Address)iterator.next()) instanceof Mailbox)) {
                    final boolean b = true;
                    if (!b) {
                        return new MailboxList((List<Mailbox>)this.addresses, true);
                    }
                    final ArrayList<Mailbox> list = new ArrayList<Mailbox>();
                    final Iterator<? extends Address> iterator2 = this.addresses.iterator();
                    while (iterator2.hasNext()) {
                        ((Address)iterator2.next()).addMailboxesTo(list);
                    }
                    return new MailboxList(list, false);
                }
            }
            final boolean b = false;
            continue;
        }
    }
    
    @Override
    public Address get(final int n) {
        return (Address)this.addresses.get(n);
    }
    
    public void print() {
        final Iterator<? extends Address> iterator = this.addresses.iterator();
        while (iterator.hasNext()) {
            System.out.println(((Address)iterator.next()).toString());
        }
    }
    
    @Override
    public int size() {
        return this.addresses.size();
    }
}
