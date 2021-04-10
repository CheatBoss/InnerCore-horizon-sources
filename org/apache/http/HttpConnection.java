package org.apache.http;

import java.io.*;

@Deprecated
public interface HttpConnection
{
    void close() throws IOException;
    
    HttpConnectionMetrics getMetrics();
    
    int getSocketTimeout();
    
    boolean isOpen();
    
    boolean isStale();
    
    void setSocketTimeout(final int p0);
    
    void shutdown() throws IOException;
}
