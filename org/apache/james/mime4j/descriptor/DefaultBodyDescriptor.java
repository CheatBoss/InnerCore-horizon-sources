package org.apache.james.mime4j.descriptor;

import org.apache.commons.logging.*;
import java.util.*;
import org.apache.james.mime4j.util.*;
import org.apache.james.mime4j.parser.*;

public class DefaultBodyDescriptor implements MutableBodyDescriptor
{
    private static final String DEFAULT_MEDIA_TYPE = "text";
    private static final String DEFAULT_MIME_TYPE = "text/plain";
    private static final String DEFAULT_SUB_TYPE = "plain";
    private static final String EMAIL_MESSAGE_MIME_TYPE = "message/rfc822";
    private static final String MEDIA_TYPE_MESSAGE = "message";
    private static final String MEDIA_TYPE_TEXT = "text";
    private static final String SUB_TYPE_EMAIL = "rfc822";
    private static final String US_ASCII = "us-ascii";
    private static Log log;
    private String boundary;
    private String charset;
    private long contentLength;
    private boolean contentTransferEncSet;
    private boolean contentTypeSet;
    private String mediaType;
    private String mimeType;
    private Map<String, String> parameters;
    private String subType;
    private String transferEncoding;
    
    static {
        DefaultBodyDescriptor.log = LogFactory.getLog((Class)DefaultBodyDescriptor.class);
    }
    
    public DefaultBodyDescriptor() {
        this(null);
    }
    
    public DefaultBodyDescriptor(final BodyDescriptor bodyDescriptor) {
        this.mediaType = "text";
        this.subType = "plain";
        this.mimeType = "text/plain";
        this.boundary = null;
        this.charset = "us-ascii";
        this.transferEncoding = "7bit";
        this.parameters = new HashMap<String, String>();
        this.contentLength = -1L;
        String mediaType;
        if (bodyDescriptor != null && MimeUtil.isSameMimeType("multipart/digest", bodyDescriptor.getMimeType())) {
            this.mimeType = "message/rfc822";
            this.subType = "rfc822";
            mediaType = "message";
        }
        else {
            this.mimeType = "text/plain";
            this.subType = "plain";
            mediaType = "text";
        }
        this.mediaType = mediaType;
    }
    
    private void parseContentType(String trim) {
        boolean b = true;
        this.contentTypeSet = true;
        final Map<String, String> headerParams = MimeUtil.getHeaderParams(trim);
        final String s = trim = headerParams.get("");
        String mimeType = null;
        String trim3 = null;
        String mediaType = null;
        Label_0185: {
            if (s != null) {
                mimeType = s.toLowerCase().trim();
                final int index = mimeType.indexOf(47);
                Label_0166: {
                    if (index != -1) {
                        final String trim2 = mimeType.substring(0, index).trim();
                        final String s2 = trim3 = mimeType.substring(index + 1).trim();
                        trim = trim2;
                        if (trim2.length() > 0) {
                            trim3 = s2;
                            trim = trim2;
                            if (s2.length() > 0) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append(trim2);
                                sb.append("/");
                                sb.append(s2);
                                mimeType = sb.toString();
                                trim3 = s2;
                                trim = trim2;
                                break Label_0166;
                            }
                        }
                    }
                    else {
                        trim3 = (trim = null);
                    }
                    b = false;
                }
                mediaType = trim;
                if (b) {
                    break Label_0185;
                }
                trim = null;
            }
            trim3 = (mediaType = null);
            mimeType = trim;
        }
        trim = headerParams.get("boundary");
        if (mimeType != null && ((mimeType.startsWith("multipart/") && trim != null) || !mimeType.startsWith("multipart/"))) {
            this.mimeType = mimeType;
            this.subType = trim3;
            this.mediaType = mediaType;
        }
        if (MimeUtil.isMultipart(this.mimeType)) {
            this.boundary = trim;
        }
        trim = headerParams.get("charset");
        this.charset = null;
        if (trim != null) {
            trim = trim.trim();
            if (trim.length() > 0) {
                this.charset = trim.toLowerCase();
            }
        }
        if (this.charset == null && "text".equals(this.mediaType)) {
            this.charset = "us-ascii";
        }
        this.parameters.putAll(headerParams);
        this.parameters.remove("");
        this.parameters.remove("boundary");
        this.parameters.remove("charset");
    }
    
    @Override
    public void addField(Field body) {
        final String name = body.getName();
        body = (Field)body.getBody();
        final String lowerCase = name.trim().toLowerCase();
        if (lowerCase.equals("content-transfer-encoding") && !this.contentTransferEncSet) {
            this.contentTransferEncSet = true;
            final String lowerCase2 = ((String)body).trim().toLowerCase();
            if (lowerCase2.length() > 0) {
                this.transferEncoding = lowerCase2;
            }
        }
        else {
            if (lowerCase.equals("content-length") && this.contentLength == -1L) {
                try {
                    this.contentLength = Long.parseLong(((String)body).trim());
                    return;
                }
                catch (NumberFormatException ex) {
                    final Log log = DefaultBodyDescriptor.log;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Invalid content-length: ");
                    sb.append((String)body);
                    log.error((Object)sb.toString());
                    return;
                }
            }
            if (lowerCase.equals("content-type") && !this.contentTypeSet) {
                this.parseContentType((String)body);
            }
        }
    }
    
    @Override
    public String getBoundary() {
        return this.boundary;
    }
    
    @Override
    public String getCharset() {
        return this.charset;
    }
    
    @Override
    public long getContentLength() {
        return this.contentLength;
    }
    
    @Override
    public Map<String, String> getContentTypeParameters() {
        return this.parameters;
    }
    
    @Override
    public String getMediaType() {
        return this.mediaType;
    }
    
    @Override
    public String getMimeType() {
        return this.mimeType;
    }
    
    @Override
    public String getSubType() {
        return this.subType;
    }
    
    @Override
    public String getTransferEncoding() {
        return this.transferEncoding;
    }
    
    @Override
    public String toString() {
        return this.mimeType;
    }
}
