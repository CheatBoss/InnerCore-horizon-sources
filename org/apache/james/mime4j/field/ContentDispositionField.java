package org.apache.james.mime4j.field;

import org.apache.commons.logging.*;
import org.apache.james.mime4j.util.*;
import java.io.*;
import org.apache.james.mime4j.field.contentdisposition.parser.*;
import org.apache.james.mime4j.field.datetime.parser.*;
import java.util.*;

public class ContentDispositionField extends AbstractField
{
    public static final String DISPOSITION_TYPE_ATTACHMENT = "attachment";
    public static final String DISPOSITION_TYPE_INLINE = "inline";
    public static final String PARAM_CREATION_DATE = "creation-date";
    public static final String PARAM_FILENAME = "filename";
    public static final String PARAM_MODIFICATION_DATE = "modification-date";
    public static final String PARAM_READ_DATE = "read-date";
    public static final String PARAM_SIZE = "size";
    static final FieldParser PARSER;
    private static Log log;
    private Date creationDate;
    private boolean creationDateParsed;
    private String dispositionType;
    private Date modificationDate;
    private boolean modificationDateParsed;
    private Map<String, String> parameters;
    private ParseException parseException;
    private boolean parsed;
    private Date readDate;
    private boolean readDateParsed;
    
    static {
        ContentDispositionField.log = LogFactory.getLog((Class)ContentDispositionField.class);
        PARSER = new FieldParser() {
            @Override
            public ParsedField parse(final String s, final String s2, final ByteSequence byteSequence) {
                return new ContentDispositionField(s, s2, byteSequence);
            }
        };
    }
    
    ContentDispositionField(final String s, final String s2, final ByteSequence byteSequence) {
        super(s, s2, byteSequence);
        this.parsed = false;
        this.dispositionType = "";
        this.parameters = new HashMap<String, String>();
    }
    
    private void parse() {
        final String body = this.getBody();
        final ContentDispositionParser contentDispositionParser = new ContentDispositionParser(new StringReader(body));
        try {
            contentDispositionParser.parseAll();
        }
        catch (TokenMgrError tokenMgrError) {
            if (ContentDispositionField.log.isDebugEnabled()) {
                final Log log = ContentDispositionField.log;
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
            if (ContentDispositionField.log.isDebugEnabled()) {
                final Log log2 = ContentDispositionField.log;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Parsing value '");
                sb2.append(body);
                sb2.append("': ");
                sb2.append(parseException.getMessage());
                log2.debug((Object)sb2.toString());
            }
            this.parseException = parseException;
        }
        final String dispositionType = contentDispositionParser.getDispositionType();
        if (dispositionType != null) {
            this.dispositionType = dispositionType.toLowerCase(Locale.US);
            final List<String> paramNames = contentDispositionParser.getParamNames();
            final List<String> paramValues = contentDispositionParser.getParamValues();
            if (paramNames != null && paramValues != null) {
                for (int min = Math.min(paramNames.size(), paramValues.size()), i = 0; i < min; ++i) {
                    this.parameters.put(paramNames.get(i).toLowerCase(Locale.US), paramValues.get(i));
                }
            }
        }
        this.parsed = true;
    }
    
    private Date parseDate(final String s) {
        final String parameter = this.getParameter(s);
        if (parameter == null) {
            if (ContentDispositionField.log.isDebugEnabled()) {
                final Log log = ContentDispositionField.log;
                final StringBuilder sb = new StringBuilder();
                sb.append("Parsing ");
                sb.append(s);
                sb.append(" null");
                log.debug((Object)sb.toString());
            }
            return null;
        }
        try {
            return new DateTimeParser(new StringReader(parameter)).parseAll().getDate();
        }
        catch (org.apache.james.mime4j.field.datetime.parser.TokenMgrError tokenMgrError) {
            if (ContentDispositionField.log.isDebugEnabled()) {
                final Log log2 = ContentDispositionField.log;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Parsing ");
                sb2.append(s);
                sb2.append(" '");
                sb2.append(parameter);
                sb2.append("': ");
                sb2.append(tokenMgrError.getMessage());
                log2.debug((Object)sb2.toString());
            }
            return null;
        }
        catch (ParseException ex) {
            if (ContentDispositionField.log.isDebugEnabled()) {
                final Log log3 = ContentDispositionField.log;
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Parsing ");
                sb3.append(s);
                sb3.append(" '");
                sb3.append(parameter);
                sb3.append("': ");
                sb3.append(ex.getMessage());
                log3.debug((Object)sb3.toString());
            }
            return null;
        }
    }
    
    public Date getCreationDate() {
        if (!this.creationDateParsed) {
            this.creationDate = this.parseDate("creation-date");
            this.creationDateParsed = true;
        }
        return this.creationDate;
    }
    
    public String getDispositionType() {
        if (!this.parsed) {
            this.parse();
        }
        return this.dispositionType;
    }
    
    public String getFilename() {
        return this.getParameter("filename");
    }
    
    public Date getModificationDate() {
        if (!this.modificationDateParsed) {
            this.modificationDate = this.parseDate("modification-date");
            this.modificationDateParsed = true;
        }
        return this.modificationDate;
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
    
    public Date getReadDate() {
        if (!this.readDateParsed) {
            this.readDate = this.parseDate("read-date");
            this.readDateParsed = true;
        }
        return this.readDate;
    }
    
    public long getSize() {
        final String parameter = this.getParameter("size");
        if (parameter == null) {
            return -1L;
        }
        try {
            final long long1 = Long.parseLong(parameter);
            if (long1 < 0L) {
                return -1L;
            }
            return long1;
        }
        catch (NumberFormatException ex) {
            return -1L;
        }
    }
    
    public boolean isAttachment() {
        if (!this.parsed) {
            this.parse();
        }
        return this.dispositionType.equals("attachment");
    }
    
    public boolean isDispositionType(final String s) {
        if (!this.parsed) {
            this.parse();
        }
        return this.dispositionType.equalsIgnoreCase(s);
    }
    
    public boolean isInline() {
        if (!this.parsed) {
            this.parse();
        }
        return this.dispositionType.equals("inline");
    }
}
