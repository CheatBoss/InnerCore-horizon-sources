package org.apache.james.mime4j.descriptor;

import org.apache.james.mime4j.field.datetime.*;
import org.apache.james.mime4j.*;
import java.util.*;
import org.apache.james.mime4j.util.*;
import org.apache.james.mime4j.field.datetime.parser.*;
import java.io.*;
import org.apache.james.mime4j.field.language.parser.*;
import org.apache.james.mime4j.field.structured.parser.*;
import org.apache.james.mime4j.field.mimeversion.parser.*;
import org.apache.james.mime4j.parser.*;

public class MaximalBodyDescriptor extends DefaultBodyDescriptor
{
    private static final int DEFAULT_MAJOR_VERSION = 1;
    private static final int DEFAULT_MINOR_VERSION = 0;
    private String contentDescription;
    private DateTime contentDispositionCreationDate;
    private MimeException contentDispositionCreationDateParseException;
    private DateTime contentDispositionModificationDate;
    private MimeException contentDispositionModificationDateParseException;
    private Map<String, String> contentDispositionParameters;
    private DateTime contentDispositionReadDate;
    private MimeException contentDispositionReadDateParseException;
    private long contentDispositionSize;
    private MimeException contentDispositionSizeParseException;
    private String contentDispositionType;
    private String contentId;
    private List<String> contentLanguage;
    private MimeException contentLanguageParseException;
    private String contentLocation;
    private MimeException contentLocationParseException;
    private String contentMD5Raw;
    private boolean isContentDescriptionSet;
    private boolean isContentDispositionSet;
    private boolean isContentIdSet;
    private boolean isContentLanguageSet;
    private boolean isContentLocationSet;
    private boolean isContentMD5Set;
    private boolean isMimeVersionSet;
    private int mimeMajorVersion;
    private int mimeMinorVersion;
    private MimeException mimeVersionException;
    
    protected MaximalBodyDescriptor() {
        this(null);
    }
    
    public MaximalBodyDescriptor(final BodyDescriptor bodyDescriptor) {
        super(bodyDescriptor);
        this.isMimeVersionSet = false;
        this.mimeMajorVersion = 1;
        this.mimeMinorVersion = 0;
        this.contentId = null;
        this.isContentIdSet = false;
        this.contentDescription = null;
        this.isContentDescriptionSet = false;
        this.contentDispositionType = null;
        this.contentDispositionParameters = Collections.emptyMap();
        this.contentDispositionModificationDate = null;
        this.contentDispositionModificationDateParseException = null;
        this.contentDispositionCreationDate = null;
        this.contentDispositionCreationDateParseException = null;
        this.contentDispositionReadDate = null;
        this.contentDispositionReadDateParseException = null;
        this.contentDispositionSize = -1L;
        this.contentDispositionSizeParseException = null;
        this.isContentDispositionSet = false;
        this.contentLanguage = null;
        this.contentLanguageParseException = null;
        this.isContentIdSet = false;
        this.contentLocation = null;
        this.contentLocationParseException = null;
        this.isContentLocationSet = false;
        this.contentMD5Raw = null;
        this.isContentMD5Set = false;
    }
    
    private void parseContentDescription(String trim) {
        if (trim == null) {
            trim = "";
        }
        else {
            trim = trim.trim();
        }
        this.contentDescription = trim;
        this.isContentDescriptionSet = true;
    }
    
    private void parseContentDisposition(String s) {
        this.isContentDispositionSet = true;
        final Map<String, String> headerParams = MimeUtil.getHeaderParams(s);
        this.contentDispositionParameters = headerParams;
        this.contentDispositionType = headerParams.get("");
        s = this.contentDispositionParameters.get("modification-date");
        if (s != null) {
            try {
                this.contentDispositionModificationDate = this.parseDate(s);
            }
            catch (ParseException contentDispositionModificationDateParseException) {
                this.contentDispositionModificationDateParseException = contentDispositionModificationDateParseException;
            }
        }
        s = this.contentDispositionParameters.get("creation-date");
        if (s != null) {
            try {
                this.contentDispositionCreationDate = this.parseDate(s);
            }
            catch (ParseException contentDispositionCreationDateParseException) {
                this.contentDispositionCreationDateParseException = contentDispositionCreationDateParseException;
            }
        }
        s = this.contentDispositionParameters.get("read-date");
        if (s != null) {
            try {
                this.contentDispositionReadDate = this.parseDate(s);
            }
            catch (ParseException contentDispositionReadDateParseException) {
                this.contentDispositionReadDateParseException = contentDispositionReadDateParseException;
            }
        }
        s = this.contentDispositionParameters.get("size");
        if (s != null) {
            try {
                this.contentDispositionSize = Long.parseLong(s);
            }
            catch (NumberFormatException ex) {
                this.contentDispositionSizeParseException = (MimeException)new MimeException(ex.getMessage(), ex).fillInStackTrace();
            }
        }
        this.contentDispositionParameters.remove("");
    }
    
    private void parseContentId(String trim) {
        if (trim == null) {
            trim = "";
        }
        else {
            trim = trim.trim();
        }
        this.contentId = trim;
        this.isContentIdSet = true;
    }
    
    private DateTime parseDate(final String s) throws ParseException {
        return new DateTimeParser(new StringReader(s)).date_time();
    }
    
    private void parseLanguage(final String s) {
        this.isContentLanguageSet = true;
        if (s != null) {
            try {
                this.contentLanguage = new ContentLanguageParser(new StringReader(s)).parse();
            }
            catch (MimeException contentLanguageParseException) {
                this.contentLanguageParseException = contentLanguageParseException;
            }
        }
    }
    
    private void parseLocation(final String s) {
        this.isContentLocationSet = true;
        if (s != null) {
            final StructuredFieldParser structuredFieldParser = new StructuredFieldParser(new StringReader(s));
            structuredFieldParser.setFoldingPreserved(false);
            try {
                this.contentLocation = structuredFieldParser.parse();
            }
            catch (MimeException contentLocationParseException) {
                this.contentLocationParseException = contentLocationParseException;
            }
        }
    }
    
    private void parseMD5(final String s) {
        this.isContentMD5Set = true;
        if (s != null) {
            this.contentMD5Raw = s.trim();
        }
    }
    
    private void parseMimeVersion(final String s) {
        final MimeVersionParser mimeVersionParser = new MimeVersionParser(new StringReader(s));
        try {
            mimeVersionParser.parse();
            final int majorVersion = mimeVersionParser.getMajorVersion();
            if (majorVersion != -1) {
                this.mimeMajorVersion = majorVersion;
            }
            final int minorVersion = mimeVersionParser.getMinorVersion();
            if (minorVersion != -1) {
                this.mimeMinorVersion = minorVersion;
            }
        }
        catch (MimeException mimeVersionException) {
            this.mimeVersionException = mimeVersionException;
        }
        this.isMimeVersionSet = true;
    }
    
    @Override
    public void addField(final Field field) {
        final String name = field.getName();
        final String body = field.getBody();
        final String lowerCase = name.trim().toLowerCase();
        if ("mime-version".equals(lowerCase) && !this.isMimeVersionSet) {
            this.parseMimeVersion(body);
            return;
        }
        if ("content-id".equals(lowerCase) && !this.isContentIdSet) {
            this.parseContentId(body);
            return;
        }
        if ("content-description".equals(lowerCase) && !this.isContentDescriptionSet) {
            this.parseContentDescription(body);
            return;
        }
        if ("content-disposition".equals(lowerCase) && !this.isContentDispositionSet) {
            this.parseContentDisposition(body);
            return;
        }
        if ("content-language".equals(lowerCase) && !this.isContentLanguageSet) {
            this.parseLanguage(body);
            return;
        }
        if ("content-location".equals(lowerCase) && !this.isContentLocationSet) {
            this.parseLocation(body);
            return;
        }
        if ("content-md5".equals(lowerCase) && !this.isContentMD5Set) {
            this.parseMD5(body);
            return;
        }
        super.addField(field);
    }
    
    public String getContentDescription() {
        return this.contentDescription;
    }
    
    public DateTime getContentDispositionCreationDate() {
        return this.contentDispositionCreationDate;
    }
    
    public MimeException getContentDispositionCreationDateParseException() {
        return this.contentDispositionCreationDateParseException;
    }
    
    public String getContentDispositionFilename() {
        return this.contentDispositionParameters.get("filename");
    }
    
    public DateTime getContentDispositionModificationDate() {
        return this.contentDispositionModificationDate;
    }
    
    public MimeException getContentDispositionModificationDateParseException() {
        return this.contentDispositionModificationDateParseException;
    }
    
    public Map<String, String> getContentDispositionParameters() {
        return this.contentDispositionParameters;
    }
    
    public DateTime getContentDispositionReadDate() {
        return this.contentDispositionReadDate;
    }
    
    public MimeException getContentDispositionReadDateParseException() {
        return this.contentDispositionReadDateParseException;
    }
    
    public long getContentDispositionSize() {
        return this.contentDispositionSize;
    }
    
    public MimeException getContentDispositionSizeParseException() {
        return this.contentDispositionSizeParseException;
    }
    
    public String getContentDispositionType() {
        return this.contentDispositionType;
    }
    
    public String getContentId() {
        return this.contentId;
    }
    
    public List<String> getContentLanguage() {
        return this.contentLanguage;
    }
    
    public MimeException getContentLanguageParseException() {
        return this.contentLanguageParseException;
    }
    
    public String getContentLocation() {
        return this.contentLocation;
    }
    
    public MimeException getContentLocationParseException() {
        return this.contentLocationParseException;
    }
    
    public String getContentMD5Raw() {
        return this.contentMD5Raw;
    }
    
    public int getMimeMajorVersion() {
        return this.mimeMajorVersion;
    }
    
    public int getMimeMinorVersion() {
        return this.mimeMinorVersion;
    }
    
    public MimeException getMimeVersionParseException() {
        return this.mimeVersionException;
    }
}
