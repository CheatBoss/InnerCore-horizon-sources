package org.apache.http.entity.mime;

import org.apache.http.entity.mime.content.*;
import org.apache.james.mime4j.message.*;
import org.apache.james.mime4j.descriptor.*;
import org.apache.james.mime4j.parser.*;

public class FormBodyPart extends BodyPart
{
    private final String name;
    
    public FormBodyPart(final String name, final ContentBody body) {
        if (name == null) {
            throw new IllegalArgumentException("Name may not be null");
        }
        if (body != null) {
            this.name = name;
            this.setHeader(new Header());
            this.setBody(body);
            this.generateContentDisp(body);
            this.generateContentType(body);
            this.generateTransferEncoding(body);
            return;
        }
        throw new IllegalArgumentException("Body may not be null");
    }
    
    private void addField(final String s, final String s2) {
        this.getHeader().addField(new MinimalField(s, s2));
    }
    
    protected void generateContentDisp(final ContentBody contentBody) {
        final StringBuilder sb = new StringBuilder();
        sb.append("form-data; name=\"");
        sb.append(this.getName());
        sb.append("\"");
        if (contentBody.getFilename() != null) {
            sb.append("; filename=\"");
            sb.append(contentBody.getFilename());
            sb.append("\"");
        }
        this.addField("Content-Disposition", sb.toString());
    }
    
    protected void generateContentType(final ContentDescriptor contentDescriptor) {
        if (contentDescriptor.getMimeType() != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(contentDescriptor.getMimeType());
            if (contentDescriptor.getCharset() != null) {
                sb.append("; charset=");
                sb.append(contentDescriptor.getCharset());
            }
            this.addField("Content-Type", sb.toString());
        }
    }
    
    protected void generateTransferEncoding(final ContentDescriptor contentDescriptor) {
        if (contentDescriptor.getTransferEncoding() != null) {
            this.addField("Content-Transfer-Encoding", contentDescriptor.getTransferEncoding());
        }
    }
    
    public String getName() {
        return this.name;
    }
}
