package org.apache.http;

@Deprecated
public interface HttpConnectionMetrics
{
    Object getMetric(final String p0);
    
    long getReceivedBytesCount();
    
    long getRequestCount();
    
    long getResponseCount();
    
    long getSentBytesCount();
    
    void reset();
}
