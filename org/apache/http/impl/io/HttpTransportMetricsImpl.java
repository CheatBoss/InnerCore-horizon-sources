package org.apache.http.impl.io;

import org.apache.http.io.*;

@Deprecated
public class HttpTransportMetricsImpl implements HttpTransportMetrics
{
    public HttpTransportMetricsImpl() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public long getBytesTransferred() {
        throw new RuntimeException("Stub!");
    }
    
    public void incrementBytesTransferred(final long n) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void reset() {
        throw new RuntimeException("Stub!");
    }
    
    public void setBytesTransferred(final long n) {
        throw new RuntimeException("Stub!");
    }
}
