package org.apache.james.mime4j.field;

import org.apache.james.mime4j.field.address.parser.*;
import org.apache.commons.logging.*;
import org.apache.james.mime4j.util.*;
import org.apache.james.mime4j.field.address.*;

public class MailboxListField extends AbstractField
{
    static final FieldParser PARSER;
    private static Log log;
    private MailboxList mailboxList;
    private ParseException parseException;
    private boolean parsed;
    
    static {
        MailboxListField.log = LogFactory.getLog((Class)MailboxListField.class);
        PARSER = new FieldParser() {
            @Override
            public ParsedField parse(final String s, final String s2, final ByteSequence byteSequence) {
                return new MailboxListField(s, s2, byteSequence);
            }
        };
    }
    
    MailboxListField(final String s, final String s2, final ByteSequence byteSequence) {
        super(s, s2, byteSequence);
        this.parsed = false;
    }
    
    private void parse() {
        final String body = this.getBody();
        try {
            this.mailboxList = AddressList.parse(body).flatten();
        }
        catch (ParseException parseException) {
            if (MailboxListField.log.isDebugEnabled()) {
                final Log log = MailboxListField.log;
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
    
    public MailboxList getMailboxList() {
        if (!this.parsed) {
            this.parse();
        }
        return this.mailboxList;
    }
    
    @Override
    public ParseException getParseException() {
        if (!this.parsed) {
            this.parse();
        }
        return this.parseException;
    }
}
