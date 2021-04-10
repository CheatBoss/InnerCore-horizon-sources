package org.apache.james.mime4j.field;

import org.apache.james.mime4j.field.address.*;
import org.apache.james.mime4j.field.address.parser.*;
import org.apache.commons.logging.*;
import org.apache.james.mime4j.util.*;

public class AddressListField extends AbstractField
{
    static final FieldParser PARSER;
    private static Log log;
    private AddressList addressList;
    private ParseException parseException;
    private boolean parsed;
    
    static {
        AddressListField.log = LogFactory.getLog((Class)AddressListField.class);
        PARSER = new FieldParser() {
            @Override
            public ParsedField parse(final String s, final String s2, final ByteSequence byteSequence) {
                return new AddressListField(s, s2, byteSequence);
            }
        };
    }
    
    AddressListField(final String s, final String s2, final ByteSequence byteSequence) {
        super(s, s2, byteSequence);
        this.parsed = false;
    }
    
    private void parse() {
        final String body = this.getBody();
        try {
            this.addressList = AddressList.parse(body);
        }
        catch (ParseException parseException) {
            if (AddressListField.log.isDebugEnabled()) {
                final Log log = AddressListField.log;
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
    
    public AddressList getAddressList() {
        if (!this.parsed) {
            this.parse();
        }
        return this.addressList;
    }
    
    @Override
    public ParseException getParseException() {
        if (!this.parsed) {
            this.parse();
        }
        return this.parseException;
    }
}
