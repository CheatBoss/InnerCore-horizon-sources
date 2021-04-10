package com.android.internal.http.multipart;

import org.apache.http.entity.*;
import org.apache.http.params.*;
import org.apache.http.*;
import java.io.*;

public class MultipartEntity extends AbstractHttpEntity
{
    public static final String MULTIPART_BOUNDARY = "http.method.multipart.boundary";
    protected Part[] parts;
    
    public MultipartEntity(final Part[] array) {
        this.parts = null;
        throw new RuntimeException("Stub!");
    }
    
    public MultipartEntity(final Part[] array, final HttpParams httpParams) {
        this.parts = null;
        throw new RuntimeException("Stub!");
    }
    
    public InputStream getContent() throws IOException, IllegalStateException {
        throw new RuntimeException("Stub!");
    }
    
    public long getContentLength() {
        throw new RuntimeException("Stub!");
    }
    
    public Header getContentType() {
        throw new RuntimeException("Stub!");
    }
    
    protected byte[] getMultipartBoundary() {
        throw new RuntimeException("Stub!");
    }
    
    public boolean isRepeatable() {
        throw new RuntimeException("Stub!");
    }
    
    public boolean isStreaming() {
        throw new RuntimeException("Stub!");
    }
    
    public void writeTo(final OutputStream outputStream) throws IOException {
        throw new RuntimeException("Stub!");
    }
}
