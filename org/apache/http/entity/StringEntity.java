package org.apache.http.entity;

import java.io.*;

@Deprecated
public class StringEntity extends AbstractHttpEntity
{
    protected final byte[] content;
    
    public StringEntity(final String s) throws UnsupportedEncodingException {
        this.content = null;
        throw new RuntimeException("Stub!");
    }
    
    public StringEntity(final String s, final String s2) throws UnsupportedEncodingException {
        this.content = null;
        throw new RuntimeException("Stub!");
    }
    
    public Object clone() throws CloneNotSupportedException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public InputStream getContent() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public long getContentLength() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean isRepeatable() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean isStreaming() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void writeTo(final OutputStream outputStream) throws IOException {
        throw new RuntimeException("Stub!");
    }
}
