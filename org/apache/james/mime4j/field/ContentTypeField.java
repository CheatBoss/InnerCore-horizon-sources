package org.apache.james.mime4j.field;

import org.apache.commons.logging.*;
import org.apache.james.mime4j.util.*;
import java.io.*;
import org.apache.james.mime4j.field.contenttype.parser.*;
import java.util.*;

public class ContentTypeField extends AbstractField
{
    public static final String PARAM_BOUNDARY = "boundary";
    public static final String PARAM_CHARSET = "charset";
    static final FieldParser PARSER;
    public static final String TYPE_MESSAGE_RFC822 = "message/rfc822";
    public static final String TYPE_MULTIPART_DIGEST = "multipart/digest";
    public static final String TYPE_MULTIPART_PREFIX = "multipart/";
    public static final String TYPE_TEXT_PLAIN = "text/plain";
    private static Log log;
    private String mimeType;
    private Map<String, String> parameters;
    private ParseException parseException;
    private boolean parsed;
    
    static {
        ContentTypeField.log = LogFactory.getLog((Class)ContentTypeField.class);
        PARSER = new FieldParser() {
            @Override
            public ParsedField parse(final String s, final String s2, final ByteSequence byteSequence) {
                return new ContentTypeField(s, s2, byteSequence);
            }
        };
    }
    
    ContentTypeField(final String s, final String s2, final ByteSequence byteSequence) {
        super(s, s2, byteSequence);
        this.parsed = false;
        this.mimeType = "";
        this.parameters = new HashMap<String, String>();
    }
    
    public static String getCharset(final ContentTypeField contentTypeField) {
        if (contentTypeField != null) {
            final String charset = contentTypeField.getCharset();
            if (charset != null && charset.length() > 0) {
                return charset;
            }
        }
        return "us-ascii";
    }
    
    public static String getMimeType(final ContentTypeField contentTypeField, final ContentTypeField contentTypeField2) {
        if (contentTypeField != null && contentTypeField.getMimeType().length() != 0 && (!contentTypeField.isMultipart() || contentTypeField.getBoundary() != null)) {
            return contentTypeField.getMimeType();
        }
        if (contentTypeField2 != null && contentTypeField2.isMimeType("multipart/digest")) {
            return "message/rfc822";
        }
        return "text/plain";
    }
    
    private void parse() {
        final String body = this.getBody();
        final ContentTypeParser contentTypeParser = new ContentTypeParser(new StringReader(body));
        try {
            contentTypeParser.parseAll();
        }
        catch (TokenMgrError tokenMgrError) {
            if (ContentTypeField.log.isDebugEnabled()) {
                final Log log = ContentTypeField.log;
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
            if (ContentTypeField.log.isDebugEnabled()) {
                final Log log2 = ContentTypeField.log;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Parsing value '");
                sb2.append(body);
                sb2.append("': ");
                sb2.append(parseException.getMessage());
                log2.debug((Object)sb2.toString());
            }
            this.parseException = parseException;
        }
        final String type = contentTypeParser.getType();
        final String subType = contentTypeParser.getSubType();
        if (type != null && subType != null) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(type);
            sb3.append("/");
            sb3.append(subType);
            this.mimeType = sb3.toString().toLowerCase();
            final List<String> paramNames = contentTypeParser.getParamNames();
            final List<String> paramValues = contentTypeParser.getParamValues();
            if (paramNames != null && paramValues != null) {
                for (int min = Math.min(paramNames.size(), paramValues.size()), i = 0; i < min; ++i) {
                    this.parameters.put(paramNames.get(i).toLowerCase(), paramValues.get(i));
                }
            }
        }
        this.parsed = true;
    }
    
    public String getBoundary() {
        return this.getParameter("boundary");
    }
    
    public String getCharset() {
        return this.getParameter("charset");
    }
    
    public String getMimeType() {
        if (!this.parsed) {
            this.parse();
        }
        return this.mimeType;
    }
    
    public String getParameter(final String s) {
        if (!this.parsed) {
            this.parse();
        }
        return this.parameters.get(s.toLowerCase());
    }
    
    public Map<String, String> getParameters() {
        if (!this.parsed) {
            this.parse();
        }
        return Collections.unmodifiableMap((Map<? extends String, ? extends String>)this.parameters);
    }
    
    @Override
    public ParseException getParseException() {
        if (!this.parsed) {
            this.parse();
        }
        return this.parseException;
    }
    
    public boolean isMimeType(final String s) {
        if (!this.parsed) {
            this.parse();
        }
        return this.mimeType.equalsIgnoreCase(s);
    }
    
    public boolean isMultipart() {
        if (!this.parsed) {
            this.parse();
        }
        return this.mimeType.startsWith("multipart/");
    }
}
