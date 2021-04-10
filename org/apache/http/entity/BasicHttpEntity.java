package org.apache.http.entity;

import java.io.*;

@Deprecated
public class BasicHttpEntity extends AbstractHttpEntity
{
    public BasicHttpEntity() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void consumeContent() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public InputStream getContent() throws IllegalStateException {
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
    
    public void setContent(final InputStream inputStream) {
        throw new RuntimeException("Stub!");
    }
    
    public void setContentLength(final long n) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void writeTo(final OutputStream outputStream) throws IOException {
        throw new RuntimeException("Stub!");
    }
}
