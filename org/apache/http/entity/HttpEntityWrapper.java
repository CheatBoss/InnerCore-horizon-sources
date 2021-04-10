package org.apache.http.entity;

import org.apache.http.*;
import java.io.*;

@Deprecated
public class HttpEntityWrapper implements HttpEntity
{
    protected HttpEntity wrappedEntity;
    
    public HttpEntityWrapper(final HttpEntity httpEntity) {
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
    public Header getContentEncoding() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public long getContentLength() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Header getContentType() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean isChunked() {
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
