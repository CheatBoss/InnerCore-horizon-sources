package org.apache.http.entity;

import org.apache.http.*;
import java.io.*;

@Deprecated
public abstract class AbstractHttpEntity implements HttpEntity
{
    protected boolean chunked;
    protected Header contentEncoding;
    protected Header contentType;
    
    protected AbstractHttpEntity() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void consumeContent() throws IOException, UnsupportedOperationException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Header getContentEncoding() {
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
    
    public void setChunked(final boolean b) {
        throw new RuntimeException("Stub!");
    }
    
    public void setContentEncoding(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public void setContentEncoding(final Header header) {
        throw new RuntimeException("Stub!");
    }
    
    public void setContentType(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public void setContentType(final Header header) {
        throw new RuntimeException("Stub!");
    }
}
