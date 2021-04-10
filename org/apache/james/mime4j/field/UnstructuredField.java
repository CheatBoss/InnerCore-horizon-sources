package org.apache.james.mime4j.field;

import org.apache.james.mime4j.util.*;
import org.apache.james.mime4j.codec.*;

public class UnstructuredField extends AbstractField
{
    static final FieldParser PARSER;
    private boolean parsed;
    private String value;
    
    static {
        PARSER = new FieldParser() {
            @Override
            public ParsedField parse(final String s, final String s2, final ByteSequence byteSequence) {
                return new UnstructuredField(s, s2, byteSequence);
            }
        };
    }
    
    UnstructuredField(final String s, final String s2, final ByteSequence byteSequence) {
        super(s, s2, byteSequence);
        this.parsed = false;
    }
    
    private void parse() {
        this.value = DecoderUtil.decodeEncodedWords(this.getBody());
        this.parsed = true;
    }
    
    public String getValue() {
        if (!this.parsed) {
            this.parse();
        }
        return this.value;
    }
}
