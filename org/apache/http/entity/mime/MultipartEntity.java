package org.apache.http.entity.mime;

import org.apache.http.*;
import java.nio.charset.*;
import org.apache.http.message.*;
import org.apache.james.mime4j.field.*;
import org.apache.james.mime4j.parser.*;
import org.apache.http.entity.mime.content.*;
import org.apache.james.mime4j.message.*;
import java.util.*;
import java.io.*;

public class MultipartEntity implements HttpEntity
{
    private static final char[] MULTIPART_CHARS;
    private final Header contentType;
    private volatile boolean dirty;
    private long length;
    private final Message message;
    private final HttpMultipart multipart;
    
    static {
        MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    }
    
    public MultipartEntity() {
        this(HttpMultipartMode.STRICT, null, null);
    }
    
    public MultipartEntity(final HttpMultipartMode httpMultipartMode) {
        this(httpMultipartMode, null, null);
    }
    
    public MultipartEntity(final HttpMultipartMode httpMultipartMode, final String s, final Charset charset) {
        this.multipart = new HttpMultipart("form-data");
        this.contentType = new BasicHeader("Content-Type", this.generateContentType(s, charset));
        this.dirty = true;
        (this.message = new Message()).setHeader(new org.apache.james.mime4j.message.Header());
        this.multipart.setParent(this.message);
        HttpMultipartMode strict = httpMultipartMode;
        if (httpMultipartMode == null) {
            strict = HttpMultipartMode.STRICT;
        }
        this.multipart.setMode(strict);
        this.message.getHeader().addField(Fields.contentType(this.contentType.getValue()));
    }
    
    public void addPart(final String s, final ContentBody contentBody) {
        this.multipart.addBodyPart(new FormBodyPart(s, contentBody));
        this.dirty = true;
    }
    
    @Override
    public void consumeContent() throws IOException, UnsupportedOperationException {
        if (!this.isStreaming()) {
            return;
        }
        throw new UnsupportedOperationException("Streaming entity does not implement #consumeContent()");
    }
    
    protected String generateContentType(final String s, final Charset charset) {
        final StringBuilder sb = new StringBuilder();
        sb.append("multipart/form-data; boundary=");
        if (s != null) {
            sb.append(s);
        }
        else {
            final Random random = new Random();
            for (int nextInt = random.nextInt(11), i = 0; i < nextInt + 30; ++i) {
                final char[] multipart_CHARS = MultipartEntity.MULTIPART_CHARS;
                sb.append(multipart_CHARS[random.nextInt(multipart_CHARS.length)]);
            }
        }
        if (charset != null) {
            sb.append("; charset=");
            sb.append(charset.name());
        }
        return sb.toString();
    }
    
    @Override
    public InputStream getContent() throws IOException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Multipart form entity does not implement #getContent()");
    }
    
    @Override
    public Header getContentEncoding() {
        return null;
    }
    
    @Override
    public long getContentLength() {
        if (this.dirty) {
            this.length = this.multipart.getTotalLength();
            this.dirty = false;
        }
        return this.length;
    }
    
    @Override
    public Header getContentType() {
        return this.contentType;
    }
    
    @Override
    public boolean isChunked() {
        return this.isRepeatable() ^ true;
    }
    
    @Override
    public boolean isRepeatable() {
        final Iterator<FormBodyPart> iterator = this.multipart.getBodyParts().iterator();
        while (iterator.hasNext()) {
            if (((ContentBody)iterator.next().getBody()).getContentLength() < 0L) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean isStreaming() {
        return this.isRepeatable() ^ true;
    }
    
    @Override
    public void writeTo(final OutputStream outputStream) throws IOException {
        this.multipart.writeTo(outputStream);
    }
}
