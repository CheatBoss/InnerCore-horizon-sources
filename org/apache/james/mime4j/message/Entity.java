package org.apache.james.mime4j.message;

import org.apache.james.mime4j.parser.*;
import org.apache.james.mime4j.field.*;
import org.apache.james.mime4j.util.*;
import java.util.*;

public abstract class Entity implements Disposable
{
    private Body body;
    private Header header;
    private Entity parent;
    
    protected Entity() {
        this.header = null;
        this.body = null;
        this.parent = null;
    }
    
    protected Entity(final Entity entity) {
        this.header = null;
        this.body = null;
        this.parent = null;
        if (entity.header != null) {
            this.header = new Header(entity.header);
        }
        final Body body = entity.body;
        if (body != null) {
            this.setBody(BodyCopier.copy(body));
        }
    }
    
    @Override
    public void dispose() {
        final Body body = this.body;
        if (body != null) {
            body.dispose();
        }
    }
    
    public Body getBody() {
        return this.body;
    }
    
    public String getCharset() {
        return ContentTypeField.getCharset((ContentTypeField)this.getHeader().getField("Content-Type"));
    }
    
    public String getContentTransferEncoding() {
        return ContentTransferEncodingField.getEncoding((ContentTransferEncodingField)this.getHeader().getField("Content-Transfer-Encoding"));
    }
    
    public String getDispositionType() {
        final ContentDispositionField contentDispositionField = this.obtainField("Content-Disposition");
        if (contentDispositionField == null) {
            return null;
        }
        return contentDispositionField.getDispositionType();
    }
    
    public String getFilename() {
        final ContentDispositionField contentDispositionField = this.obtainField("Content-Disposition");
        if (contentDispositionField == null) {
            return null;
        }
        return contentDispositionField.getFilename();
    }
    
    public Header getHeader() {
        return this.header;
    }
    
    public String getMimeType() {
        final ContentTypeField contentTypeField = (ContentTypeField)this.getHeader().getField("Content-Type");
        ContentTypeField contentTypeField2;
        if (this.getParent() != null) {
            contentTypeField2 = (ContentTypeField)this.getParent().getHeader().getField("Content-Type");
        }
        else {
            contentTypeField2 = null;
        }
        return ContentTypeField.getMimeType(contentTypeField, contentTypeField2);
    }
    
    public Entity getParent() {
        return this.parent;
    }
    
    public boolean isMimeType(final String s) {
        return this.getMimeType().equalsIgnoreCase(s);
    }
    
    public boolean isMultipart() {
        final ContentTypeField contentTypeField = (ContentTypeField)this.getHeader().getField("Content-Type");
        return contentTypeField != null && contentTypeField.getBoundary() != null && this.getMimeType().startsWith("multipart/");
    }
    
     <F extends Field> F obtainField(final String s) {
        final Header header = this.getHeader();
        if (header == null) {
            return null;
        }
        return (F)header.getField(s);
    }
    
    Header obtainHeader() {
        if (this.header == null) {
            this.header = new Header();
        }
        return this.header;
    }
    
    public Body removeBody() {
        final Body body = this.body;
        if (body == null) {
            return null;
        }
        this.body = null;
        body.setParent(null);
        return body;
    }
    
    public void setBody(final Body body) {
        if (this.body == null) {
            (this.body = body).setParent(this);
            return;
        }
        throw new IllegalStateException("body already set");
    }
    
    public void setBody(final Body body, final String s) {
        this.setBody(body, s, null);
    }
    
    public void setBody(final Body body, final String s, final Map<String, String> map) {
        this.setBody(body);
        this.obtainHeader().setField(Fields.contentType(s, map));
    }
    
    public void setContentDisposition(final String s) {
        this.obtainHeader().setField(Fields.contentDisposition(s, null, -1L, null, null, null));
    }
    
    public void setContentDisposition(final String s, final String s2) {
        this.obtainHeader().setField(Fields.contentDisposition(s, s2, -1L, null, null, null));
    }
    
    public void setContentDisposition(final String s, final String s2, final long n) {
        this.obtainHeader().setField(Fields.contentDisposition(s, s2, n, null, null, null));
    }
    
    public void setContentDisposition(final String s, final String s2, final long n, final Date date, final Date date2, final Date date3) {
        this.obtainHeader().setField(Fields.contentDisposition(s, s2, n, date, date2, date3));
    }
    
    public void setContentTransferEncoding(final String s) {
        this.obtainHeader().setField(Fields.contentTransferEncoding(s));
    }
    
    public void setFilename(final String s) {
        final Header obtainHeader = this.obtainHeader();
        final ContentDispositionField contentDispositionField = (ContentDispositionField)obtainHeader.getField("Content-Disposition");
        if (contentDispositionField == null) {
            if (s != null) {
                obtainHeader.setField(Fields.contentDisposition("attachment", s, -1L, null, null, null));
            }
        }
        else {
            final String dispositionType = contentDispositionField.getDispositionType();
            final HashMap hashMap = new HashMap<Object, Object>(contentDispositionField.getParameters());
            if (s == null) {
                hashMap.remove("filename");
            }
            else {
                hashMap.put("filename", s);
            }
            obtainHeader.setField(Fields.contentDisposition(dispositionType, (Map<String, String>)hashMap));
        }
    }
    
    public void setHeader(final Header header) {
        this.header = header;
    }
    
    public void setMessage(final Message message) {
        this.setBody(message, "message/rfc822", null);
    }
    
    public void setMultipart(final Multipart multipart) {
        final StringBuilder sb = new StringBuilder();
        sb.append("multipart/");
        sb.append(multipart.getSubType());
        this.setBody(multipart, sb.toString(), Collections.singletonMap("boundary", MimeUtil.createUniqueBoundary()));
    }
    
    public void setMultipart(final Multipart multipart, final Map<String, String> map) {
        final StringBuilder sb = new StringBuilder();
        sb.append("multipart/");
        sb.append(multipart.getSubType());
        final String string = sb.toString();
        Map<String, String> map2 = map;
        if (!map.containsKey("boundary")) {
            map2 = new HashMap<String, String>(map);
            map2.put("boundary", MimeUtil.createUniqueBoundary());
        }
        this.setBody(multipart, string, map2);
    }
    
    public void setParent(final Entity parent) {
        this.parent = parent;
    }
    
    public void setText(final TextBody textBody) {
        this.setText(textBody, "plain");
    }
    
    public void setText(final TextBody textBody, String mimeCharset) {
        final StringBuilder sb = new StringBuilder();
        sb.append("text/");
        sb.append(mimeCharset);
        final String string = sb.toString();
        mimeCharset = textBody.getMimeCharset();
        Map<String, String> singletonMap;
        if (mimeCharset != null && !mimeCharset.equalsIgnoreCase("us-ascii")) {
            singletonMap = Collections.singletonMap("charset", mimeCharset);
        }
        else {
            singletonMap = null;
        }
        this.setBody(textBody, string, singletonMap);
    }
}
