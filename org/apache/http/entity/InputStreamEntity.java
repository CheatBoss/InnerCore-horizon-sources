package org.apache.http.entity;

import java.io.*;

@Deprecated
public class InputStreamEntity extends AbstractHttpEntity
{
    public InputStreamEntity(final InputStream inputStream, final long n) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void consumeContent() throws IOException {
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
