package org.apache.http;

import java.io.*;

@Deprecated
public interface HttpEntity
{
    void consumeContent() throws IOException;
    
    InputStream getContent() throws IOException, IllegalStateException;
    
    Header getContentEncoding();
    
    long getContentLength();
    
    Header getContentType();
    
    boolean isChunked();
    
    boolean isRepeatable();
    
    boolean isStreaming();
    
    void writeTo(final OutputStream p0) throws IOException;
}
