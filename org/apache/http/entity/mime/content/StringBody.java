package org.apache.http.entity.mime.content;

import java.nio.charset.*;
import java.util.*;
import java.io.*;

public class StringBody extends AbstractContentBody
{
    private final Charset charset;
    private final byte[] content;
    
    public StringBody(final String s) throws UnsupportedEncodingException {
        this(s, "text/plain", null);
    }
    
    public StringBody(final String s, final String s2, final Charset charset) throws UnsupportedEncodingException {
        super(s2);
        if (s != null) {
            Charset defaultCharset;
            if ((defaultCharset = charset) == null) {
                defaultCharset = Charset.defaultCharset();
            }
            this.content = s.getBytes(defaultCharset.name());
            this.charset = defaultCharset;
            return;
        }
        throw new IllegalArgumentException("Text may not be null");
    }
    
    public StringBody(final String s, final Charset charset) throws UnsupportedEncodingException {
        this(s, "text/plain", charset);
    }
    
    @Override
    public String getCharset() {
        return this.charset.name();
    }
    
    @Override
    public long getContentLength() {
        return this.content.length;
    }
    
    @Override
    public Map<String, String> getContentTypeParameters() {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("charset", this.charset.name());
        return hashMap;
    }
    
    @Override
    public String getFilename() {
        return null;
    }
    
    public Reader getReader() {
        return new InputStreamReader(new ByteArrayInputStream(this.content), this.charset);
    }
    
    @Override
    public String getTransferEncoding() {
        return "8bit";
    }
    
    @Override
    public void writeTo(final OutputStream outputStream) throws IOException {
        if (outputStream != null) {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.content);
            final byte[] array = new byte[4096];
            while (true) {
                final int read = byteArrayInputStream.read(array);
                if (read == -1) {
                    break;
                }
                outputStream.write(array, 0, read);
            }
            outputStream.flush();
            return;
        }
        throw new IllegalArgumentException("Output stream may not be null");
    }
    
    @Deprecated
    public void writeTo(final OutputStream outputStream, final int n) throws IOException {
        this.writeTo(outputStream);
    }
}
