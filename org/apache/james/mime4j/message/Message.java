package org.apache.james.mime4j.message;

import org.apache.james.mime4j.storage.*;
import org.apache.james.mime4j.*;
import org.apache.james.mime4j.field.address.*;
import org.apache.james.mime4j.parser.*;
import org.apache.james.mime4j.field.*;
import java.util.*;
import java.io.*;

public class Message extends Entity implements Body
{
    public Message() {
    }
    
    public Message(final InputStream inputStream) throws IOException, MimeIOException {
        this(inputStream, null, DefaultStorageProvider.getInstance());
    }
    
    public Message(final InputStream inputStream, final MimeEntityConfig mimeEntityConfig) throws IOException, MimeIOException {
        this(inputStream, mimeEntityConfig, DefaultStorageProvider.getInstance());
    }
    
    public Message(final InputStream inputStream, final MimeEntityConfig mimeEntityConfig, final StorageProvider storageProvider) throws IOException, MimeIOException {
        try {
            final MimeStreamParser mimeStreamParser = new MimeStreamParser(mimeEntityConfig);
            mimeStreamParser.setContentHandler(new MessageBuilder(this, storageProvider));
            mimeStreamParser.parse(inputStream);
        }
        catch (MimeException ex) {
            throw new MimeIOException(ex);
        }
    }
    
    public Message(final Message message) {
        super(message);
    }
    
    private AddressList getAddressList(final String s) {
        final AddressListField addressListField = this.obtainField(s);
        if (addressListField == null) {
            return null;
        }
        return addressListField.getAddressList();
    }
    
    private Mailbox getMailbox(final String s) {
        final MailboxField mailboxField = this.obtainField(s);
        if (mailboxField == null) {
            return null;
        }
        return mailboxField.getMailbox();
    }
    
    private MailboxList getMailboxList(final String s) {
        final MailboxListField mailboxListField = this.obtainField(s);
        if (mailboxListField == null) {
            return null;
        }
        return mailboxListField.getMailboxList();
    }
    
    private void setAddressList(final String s, final Collection<Address> collection) {
        final Header obtainHeader = this.obtainHeader();
        if (collection != null && !collection.isEmpty()) {
            obtainHeader.setField(Fields.addressList(s, collection));
            return;
        }
        obtainHeader.removeFields(s);
    }
    
    private void setAddressList(final String s, final Address address) {
        Collection<Address> singleton;
        if (address == null) {
            singleton = null;
        }
        else {
            singleton = Collections.singleton(address);
        }
        this.setAddressList(s, singleton);
    }
    
    private void setAddressList(final String s, final Address... array) {
        Collection<Address> list;
        if (array == null) {
            list = null;
        }
        else {
            list = Arrays.asList(array);
        }
        this.setAddressList(s, list);
    }
    
    private void setMailbox(final String s, final Mailbox mailbox) {
        final Header obtainHeader = this.obtainHeader();
        if (mailbox == null) {
            obtainHeader.removeFields(s);
            return;
        }
        obtainHeader.setField(Fields.mailbox(s, mailbox));
    }
    
    private void setMailboxList(final String s, final Collection<Mailbox> collection) {
        final Header obtainHeader = this.obtainHeader();
        if (collection != null && !collection.isEmpty()) {
            obtainHeader.setField(Fields.mailboxList(s, collection));
            return;
        }
        obtainHeader.removeFields(s);
    }
    
    private void setMailboxList(final String s, final Mailbox mailbox) {
        Collection<Mailbox> singleton;
        if (mailbox == null) {
            singleton = null;
        }
        else {
            singleton = Collections.singleton(mailbox);
        }
        this.setMailboxList(s, singleton);
    }
    
    private void setMailboxList(final String s, final Mailbox... array) {
        Collection<Mailbox> list;
        if (array == null) {
            list = null;
        }
        else {
            list = Arrays.asList(array);
        }
        this.setMailboxList(s, list);
    }
    
    public void createMessageId(final String s) {
        this.obtainHeader().setField(Fields.messageId(s));
    }
    
    public AddressList getBcc() {
        return this.getAddressList("Bcc");
    }
    
    public AddressList getCc() {
        return this.getAddressList("Cc");
    }
    
    public Date getDate() {
        final DateTimeField dateTimeField = this.obtainField("Date");
        if (dateTimeField == null) {
            return null;
        }
        return dateTimeField.getDate();
    }
    
    public MailboxList getFrom() {
        return this.getMailboxList("From");
    }
    
    public String getMessageId() {
        final Field obtainField = this.obtainField("Message-ID");
        if (obtainField == null) {
            return null;
        }
        return obtainField.getBody();
    }
    
    public AddressList getReplyTo() {
        return this.getAddressList("Reply-To");
    }
    
    public Mailbox getSender() {
        return this.getMailbox("Sender");
    }
    
    public String getSubject() {
        final UnstructuredField unstructuredField = this.obtainField("Subject");
        if (unstructuredField == null) {
            return null;
        }
        return unstructuredField.getValue();
    }
    
    public AddressList getTo() {
        return this.getAddressList("To");
    }
    
    public void setBcc(final Collection<Address> collection) {
        this.setAddressList("Bcc", collection);
    }
    
    public void setBcc(final Address address) {
        this.setAddressList("Bcc", address);
    }
    
    public void setBcc(final Address... array) {
        this.setAddressList("Bcc", array);
    }
    
    public void setCc(final Collection<Address> collection) {
        this.setAddressList("Cc", collection);
    }
    
    public void setCc(final Address address) {
        this.setAddressList("Cc", address);
    }
    
    public void setCc(final Address... array) {
        this.setAddressList("Cc", array);
    }
    
    public void setDate(final Date date) {
        this.setDate(date, null);
    }
    
    public void setDate(final Date date, final TimeZone timeZone) {
        final Header obtainHeader = this.obtainHeader();
        if (date == null) {
            obtainHeader.removeFields("Date");
            return;
        }
        obtainHeader.setField(Fields.date("Date", date, timeZone));
    }
    
    public void setFrom(final Collection<Mailbox> collection) {
        this.setMailboxList("From", collection);
    }
    
    public void setFrom(final Mailbox mailbox) {
        this.setMailboxList("From", mailbox);
    }
    
    public void setFrom(final Mailbox... array) {
        this.setMailboxList("From", array);
    }
    
    public void setReplyTo(final Collection<Address> collection) {
        this.setAddressList("Reply-To", collection);
    }
    
    public void setReplyTo(final Address address) {
        this.setAddressList("Reply-To", address);
    }
    
    public void setReplyTo(final Address... array) {
        this.setAddressList("Reply-To", array);
    }
    
    public void setSender(final Mailbox mailbox) {
        this.setMailbox("Sender", mailbox);
    }
    
    public void setSubject(final String s) {
        final Header obtainHeader = this.obtainHeader();
        if (s == null) {
            obtainHeader.removeFields("Subject");
            return;
        }
        obtainHeader.setField(Fields.subject(s));
    }
    
    public void setTo(final Collection<Address> collection) {
        this.setAddressList("To", collection);
    }
    
    public void setTo(final Address address) {
        this.setAddressList("To", address);
    }
    
    public void setTo(final Address... array) {
        this.setAddressList("To", array);
    }
    
    public void writeTo(final OutputStream outputStream) throws IOException {
        MessageWriter.DEFAULT.writeEntity(this, outputStream);
    }
}
