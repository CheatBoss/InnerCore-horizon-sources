package org.apache.http.entity;

import org.apache.http.*;
import java.io.*;

@Deprecated
public class BufferedHttpEntity extends HttpEntityWrapper
{
    public BufferedHttpEntity(final HttpEntity httpEntity) throws IOException {
        super(null);
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
