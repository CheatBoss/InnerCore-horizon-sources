package org.apache.http.impl;

import org.apache.http.*;
import org.apache.http.io.*;

@Deprecated
public class HttpConnectionMetricsImpl implements HttpConnectionMetrics
{
    public static final String RECEIVED_BYTES_COUNT = "http.received-bytes-count";
    public static final String REQUEST_COUNT = "http.request-count";
    public static final String RESPONSE_COUNT = "http.response-count";
    public static final String SENT_BYTES_COUNT = "http.sent-bytes-count";
    
    public HttpConnectionMetricsImpl(final HttpTransportMetrics httpTransportMetrics, final HttpTransportMetrics httpTransportMetrics2) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Object getMetric(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public long getReceivedBytesCount() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public long getRequestCount() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public long getResponseCount() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public long getSentBytesCount() {
        throw new RuntimeException("Stub!");
    }
    
    public void incrementRequestCount() {
        throw new RuntimeException("Stub!");
    }
    
    public void incrementResponseCount() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void reset() {
        throw new RuntimeException("Stub!");
    }
    
    public void setMetric(final String s, final Object o) {
        throw new RuntimeException("Stub!");
    }
}
