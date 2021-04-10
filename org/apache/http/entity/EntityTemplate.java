package org.apache.http.entity;

import java.io.*;

@Deprecated
public class EntityTemplate extends AbstractHttpEntity
{
    public EntityTemplate(final ContentProducer contentProducer) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void consumeContent() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public InputStream getContent() {
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
