package org.apache.james.mime4j.field;

import java.util.*;
import org.apache.commons.logging.*;
import org.apache.james.mime4j.util.*;
import java.io.*;
import org.apache.james.mime4j.field.datetime.parser.*;

public class DateTimeField extends AbstractField
{
    static final FieldParser PARSER;
    private static Log log;
    private Date date;
    private ParseException parseException;
    private boolean parsed;
    
    static {
        DateTimeField.log = LogFactory.getLog((Class)DateTimeField.class);
        PARSER = new FieldParser() {
            @Override
            public ParsedField parse(final String s, final String s2, final ByteSequence byteSequence) {
                return new DateTimeField(s, s2, byteSequence);
            }
        };
    }
    
    DateTimeField(final String s, final String s2, final ByteSequence byteSequence) {
        super(s, s2, byteSequence);
        this.parsed = false;
    }
    
    private void parse() {
        final String body = this.getBody();
        try {
            this.date = new DateTimeParser(new StringReader(body)).parseAll().getDate();
        }
        catch (TokenMgrError tokenMgrError) {
            if (DateTimeField.log.isDebugEnabled()) {
                final Log log = DateTimeField.log;
                final StringBuilder sb = new StringBuilder();
                sb.append("Parsing value '");
                sb.append(body);
                sb.append("': ");
                sb.append(tokenMgrError.getMessage());
                log.debug((Object)sb.toString());
            }
            this.parseException = new ParseException(tokenMgrError.getMessage());
        }
        catch (ParseException parseException) {
            if (DateTimeField.log.isDebugEnabled()) {
                final Log log2 = DateTimeField.log;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Parsing value '");
                sb2.append(body);
                sb2.append("': ");
                sb2.append(parseException.getMessage());
                log2.debug((Object)sb2.toString());
            }
            this.parseException = parseException;
        }
        this.parsed = true;
    }
    
    public Date getDate() {
        if (!this.parsed) {
            this.parse();
        }
        return this.date;
    }
    
    @Override
    public ParseException getParseException() {
        if (!this.parsed) {
            this.parse();
        }
        return this.parseException;
    }
}
