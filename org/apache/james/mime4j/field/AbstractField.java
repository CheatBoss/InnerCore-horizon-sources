package org.apache.james.mime4j.field;

import org.apache.james.mime4j.*;
import org.apache.james.mime4j.util.*;
import java.util.regex.*;

public abstract class AbstractField implements ParsedField
{
    private static final Pattern FIELD_NAME_PATTERN;
    private static final DefaultFieldParser parser;
    private final String body;
    private final String name;
    private final ByteSequence raw;
    
    static {
        FIELD_NAME_PATTERN = Pattern.compile("^([\\x21-\\x39\\x3b-\\x7e]+):");
        parser = new DefaultFieldParser();
    }
    
    protected AbstractField(final String name, final String body, final ByteSequence raw) {
        this.name = name;
        this.body = body;
        this.raw = raw;
    }
    
    public static DefaultFieldParser getParser() {
        return AbstractField.parser;
    }
    
    public static ParsedField parse(final String s) throws MimeException {
        return parse(ContentUtil.encode(s), s);
    }
    
    public static ParsedField parse(final ByteSequence byteSequence) throws MimeException {
        return parse(byteSequence, ContentUtil.decode(byteSequence));
    }
    
    private static ParsedField parse(final ByteSequence byteSequence, String s) throws MimeException {
        s = MimeUtil.unfold(s);
        final Matcher matcher = AbstractField.FIELD_NAME_PATTERN.matcher(s);
        if (matcher.find()) {
            final String group = matcher.group(1);
            final String s2 = s = s.substring(matcher.end());
            if (s2.length() > 0) {
                s = s2;
                if (s2.charAt(0) == ' ') {
                    s = s2.substring(1);
                }
            }
            return AbstractField.parser.parse(group, s, byteSequence);
        }
        throw new MimeException("Invalid field in string");
    }
    
    @Override
    public String getBody() {
        return this.body;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public ParseException getParseException() {
        return null;
    }
    
    @Override
    public ByteSequence getRaw() {
        return this.raw;
    }
    
    @Override
    public boolean isValidField() {
        return this.getParseException() == null;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        sb.append(": ");
        sb.append(this.body);
        return sb.toString();
    }
}
