package org.apache.james.mime4j.field;

import org.apache.james.mime4j.field.address.parser.*;
import org.apache.commons.logging.*;
import org.apache.james.mime4j.util.*;
import org.apache.james.mime4j.field.address.*;

public class MailboxField extends AbstractField
{
    static final FieldParser PARSER;
    private static Log log;
    private Mailbox mailbox;
    private ParseException parseException;
    private boolean parsed;
    
    static {
        MailboxField.log = LogFactory.getLog((Class)MailboxField.class);
        PARSER = new FieldParser() {
            @Override
            public ParsedField parse(final String s, final String s2, final ByteSequence byteSequence) {
                return new MailboxField(s, s2, byteSequence);
            }
        };
    }
    
    MailboxField(final String s, final String s2, final ByteSequence byteSequence) {
        super(s, s2, byteSequence);
        this.parsed = false;
    }
    
    private void parse() {
        final String body = this.getBody();
        try {
            final MailboxList flatten = AddressList.parse(body).flatten();
            if (flatten.size() > 0) {
                this.mailbox = flatten.get(0);
            }
        }
        catch (ParseException parseException) {
            if (MailboxField.log.isDebugEnabled()) {
                final Log log = MailboxField.log;
                final StringBuilder sb = new StringBuilder();
                sb.append("Parsing value '");
                sb.append(body);
                sb.append("': ");
                sb.append(parseException.getMessage());
                log.debug((Object)sb.toString());
            }
            this.parseException = parseException;
        }
        this.parsed = true;
    }
    
    public Mailbox getMailbox() {
        if (!this.parsed) {
            this.parse();
        }
        return this.mailbox;
    }
    
    @Override
    public ParseException getParseException() {
        if (!this.parsed) {
            this.parse();
        }
        return this.parseException;
    }
}
