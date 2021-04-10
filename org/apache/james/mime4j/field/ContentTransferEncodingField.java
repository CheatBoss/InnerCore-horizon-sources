package org.apache.james.mime4j.field;

import org.apache.james.mime4j.util.*;

public class ContentTransferEncodingField extends AbstractField
{
    static final FieldParser PARSER;
    private String encoding;
    
    static {
        PARSER = new FieldParser() {
            @Override
            public ParsedField parse(final String s, final String s2, final ByteSequence byteSequence) {
                return new ContentTransferEncodingField(s, s2, byteSequence);
            }
        };
    }
    
    ContentTransferEncodingField(final String s, final String s2, final ByteSequence byteSequence) {
        super(s, s2, byteSequence);
        this.encoding = s2.trim().toLowerCase();
    }
    
    public static String getEncoding(final ContentTransferEncodingField contentTransferEncodingField) {
        if (contentTransferEncodingField != null && contentTransferEncodingField.getEncoding().length() != 0) {
            return contentTransferEncodingField.getEncoding();
        }
        return "7bit";
    }
    
    public String getEncoding() {
        return this.encoding;
    }
}
