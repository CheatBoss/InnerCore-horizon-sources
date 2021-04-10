package org.apache.james.mime4j.field;

import java.util.*;
import org.apache.james.mime4j.util.*;

public class DelegatingFieldParser implements FieldParser
{
    private FieldParser defaultParser;
    private Map<String, FieldParser> parsers;
    
    public DelegatingFieldParser() {
        this.parsers = new HashMap<String, FieldParser>();
        this.defaultParser = UnstructuredField.PARSER;
    }
    
    public FieldParser getParser(final String s) {
        FieldParser defaultParser;
        if ((defaultParser = this.parsers.get(s.toLowerCase())) == null) {
            defaultParser = this.defaultParser;
        }
        return defaultParser;
    }
    
    @Override
    public ParsedField parse(final String s, final String s2, final ByteSequence byteSequence) {
        return this.getParser(s).parse(s, s2, byteSequence);
    }
    
    public void setFieldParser(final String s, final FieldParser fieldParser) {
        this.parsers.put(s.toLowerCase(), fieldParser);
    }
}
