package org.apache.http.entity;

import java.io.*;

@Deprecated
public class SerializableEntity extends AbstractHttpEntity
{
    public SerializableEntity(final Serializable s, final boolean b) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public InputStream getContent() throws IOException, IllegalStateException {
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
