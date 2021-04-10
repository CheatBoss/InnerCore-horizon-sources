package org.apache.james.mime4j.field;

import java.util.regex.*;
import org.apache.james.mime4j.codec.*;
import java.util.*;
import org.apache.james.mime4j.field.address.*;
import org.apache.james.mime4j.parser.*;
import org.apache.james.mime4j.util.*;

public class Fields
{
    private static final Pattern FIELD_NAME_PATTERN;
    
    static {
        FIELD_NAME_PATTERN = Pattern.compile("[\\x21-\\x39\\x3b-\\x7e]+");
    }
    
    private Fields() {
    }
    
    public static AddressListField addressList(final String s, final Iterable<Address> iterable) {
        checkValidFieldName(s);
        return addressList0(s, iterable);
    }
    
    private static AddressListField addressList0(final String s, final Iterable<Address> iterable) {
        return parse(AddressListField.PARSER, s, encodeAddresses(iterable));
    }
    
    public static AddressListField bcc(final Iterable<Address> iterable) {
        return addressList0("Bcc", iterable);
    }
    
    public static AddressListField bcc(final Address address) {
        return addressList0("Bcc", Collections.singleton(address));
    }
    
    public static AddressListField bcc(final Address... array) {
        return addressList0("Bcc", Arrays.asList(array));
    }
    
    public static AddressListField cc(final Iterable<Address> iterable) {
        return addressList0("Cc", iterable);
    }
    
    public static AddressListField cc(final Address address) {
        return addressList0("Cc", Collections.singleton(address));
    }
    
    public static AddressListField cc(final Address... array) {
        return addressList0("Cc", Arrays.asList(array));
    }
    
    private static void checkValidFieldName(final String s) {
        if (Fields.FIELD_NAME_PATTERN.matcher(s).matches()) {
            return;
        }
        throw new IllegalArgumentException("Invalid field name");
    }
    
    public static ContentDispositionField contentDisposition(final String s) {
        return parse(ContentDispositionField.PARSER, "Content-Disposition", s);
    }
    
    public static ContentDispositionField contentDisposition(final String s, final String s2) {
        return contentDisposition(s, s2, -1L, null, null, null);
    }
    
    public static ContentDispositionField contentDisposition(final String s, final String s2, final long n) {
        return contentDisposition(s, s2, n, null, null, null);
    }
    
    public static ContentDispositionField contentDisposition(final String s, final String s2, final long n, final Date date, final Date date2, final Date date3) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        if (s2 != null) {
            hashMap.put("filename", s2);
        }
        if (n >= 0L) {
            hashMap.put("size", Long.toString(n));
        }
        if (date != null) {
            hashMap.put("creation-date", MimeUtil.formatDate(date, null));
        }
        if (date2 != null) {
            hashMap.put("modification-date", MimeUtil.formatDate(date2, null));
        }
        if (date3 != null) {
            hashMap.put("read-date", MimeUtil.formatDate(date3, null));
        }
        return contentDisposition(s, hashMap);
    }
    
    public static ContentDispositionField contentDisposition(final String s, final Map<String, String> map) {
        if (!isValidDispositionType(s)) {
            throw new IllegalArgumentException();
        }
        if (map != null && !map.isEmpty()) {
            final StringBuilder sb = new StringBuilder(s);
            for (final Map.Entry<String, String> entry : map.entrySet()) {
                sb.append("; ");
                sb.append(EncoderUtil.encodeHeaderParameter(entry.getKey(), entry.getValue()));
            }
            return contentDisposition(sb.toString());
        }
        return parse(ContentDispositionField.PARSER, "Content-Disposition", s);
    }
    
    public static ContentTransferEncodingField contentTransferEncoding(final String s) {
        return parse(ContentTransferEncodingField.PARSER, "Content-Transfer-Encoding", s);
    }
    
    public static ContentTypeField contentType(final String s) {
        return parse(ContentTypeField.PARSER, "Content-Type", s);
    }
    
    public static ContentTypeField contentType(final String s, final Map<String, String> map) {
        if (!isValidMimeType(s)) {
            throw new IllegalArgumentException();
        }
        if (map != null && !map.isEmpty()) {
            final StringBuilder sb = new StringBuilder(s);
            for (final Map.Entry<String, String> entry : map.entrySet()) {
                sb.append("; ");
                sb.append(EncoderUtil.encodeHeaderParameter(entry.getKey(), entry.getValue()));
            }
            return contentType(sb.toString());
        }
        return parse(ContentTypeField.PARSER, "Content-Type", s);
    }
    
    public static DateTimeField date(final String s, final Date date) {
        checkValidFieldName(s);
        return date0(s, date, null);
    }
    
    public static DateTimeField date(final String s, final Date date, final TimeZone timeZone) {
        checkValidFieldName(s);
        return date0(s, date, timeZone);
    }
    
    public static DateTimeField date(final Date date) {
        return date0("Date", date, null);
    }
    
    private static DateTimeField date0(final String s, final Date date, final TimeZone timeZone) {
        return parse(DateTimeField.PARSER, s, MimeUtil.formatDate(date, timeZone));
    }
    
    private static String encodeAddresses(final Iterable<? extends Address> iterable) {
        final StringBuilder sb = new StringBuilder();
        for (final Address address : iterable) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(address.getEncodedString());
        }
        return sb.toString();
    }
    
    public static MailboxListField from(final Iterable<Mailbox> iterable) {
        return mailboxList0("From", iterable);
    }
    
    public static MailboxListField from(final Mailbox mailbox) {
        return mailboxList0("From", Collections.singleton(mailbox));
    }
    
    public static MailboxListField from(final Mailbox... array) {
        return mailboxList0("From", Arrays.asList(array));
    }
    
    private static boolean isValidDispositionType(final String s) {
        return s != null && EncoderUtil.isToken(s);
    }
    
    private static boolean isValidMimeType(String substring) {
        final boolean b = false;
        if (substring == null) {
            return false;
        }
        final int index = substring.indexOf(47);
        if (index == -1) {
            return false;
        }
        final String substring2 = substring.substring(0, index);
        substring = substring.substring(index + 1);
        boolean b2 = b;
        if (EncoderUtil.isToken(substring2)) {
            b2 = b;
            if (EncoderUtil.isToken(substring)) {
                b2 = true;
            }
        }
        return b2;
    }
    
    public static MailboxField mailbox(final String s, final Mailbox mailbox) {
        checkValidFieldName(s);
        return mailbox0(s, mailbox);
    }
    
    private static MailboxField mailbox0(final String s, final Mailbox mailbox) {
        return parse(MailboxField.PARSER, s, encodeAddresses(Collections.singleton(mailbox)));
    }
    
    public static MailboxListField mailboxList(final String s, final Iterable<Mailbox> iterable) {
        checkValidFieldName(s);
        return mailboxList0(s, iterable);
    }
    
    private static MailboxListField mailboxList0(final String s, final Iterable<Mailbox> iterable) {
        return parse(MailboxListField.PARSER, s, encodeAddresses(iterable));
    }
    
    public static Field messageId(String uniqueMessageId) {
        uniqueMessageId = MimeUtil.createUniqueMessageId(uniqueMessageId);
        return parse(UnstructuredField.PARSER, "Message-ID", uniqueMessageId);
    }
    
    private static <F extends Field> F parse(final FieldParser fieldParser, final String s, final String s2) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(": ");
        sb.append(s2);
        return (F)fieldParser.parse(s, s2, ContentUtil.encode(MimeUtil.fold(sb.toString(), 0)));
    }
    
    public static AddressListField replyTo(final Iterable<Address> iterable) {
        return addressList0("Reply-To", iterable);
    }
    
    public static AddressListField replyTo(final Address address) {
        return addressList0("Reply-To", Collections.singleton(address));
    }
    
    public static AddressListField replyTo(final Address... array) {
        return addressList0("Reply-To", Arrays.asList(array));
    }
    
    public static MailboxField sender(final Mailbox mailbox) {
        return mailbox0("Sender", mailbox);
    }
    
    public static UnstructuredField subject(String encodeIfNecessary) {
        encodeIfNecessary = EncoderUtil.encodeIfNecessary(encodeIfNecessary, EncoderUtil.Usage.TEXT_TOKEN, 9);
        return parse(UnstructuredField.PARSER, "Subject", encodeIfNecessary);
    }
    
    public static AddressListField to(final Iterable<Address> iterable) {
        return addressList0("To", iterable);
    }
    
    public static AddressListField to(final Address address) {
        return addressList0("To", Collections.singleton(address));
    }
    
    public static AddressListField to(final Address... array) {
        return addressList0("To", Arrays.asList(array));
    }
}
