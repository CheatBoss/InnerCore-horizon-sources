package org.apache.http.impl.client;

import org.apache.http.client.*;
import java.io.*;
import org.apache.http.protocol.*;

@Deprecated
public class DefaultHttpRequestRetryHandler implements HttpRequestRetryHandler
{
    public DefaultHttpRequestRetryHandler() {
        throw new RuntimeException("Stub!");
    }
    
    public DefaultHttpRequestRetryHandler(final int n, final boolean b) {
        throw new RuntimeException("Stub!");
    }
    
    public int getRetryCount() {
        throw new RuntimeException("Stub!");
    }
    
    public boolean isRequestSentRetryEnabled() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean retryRequest(final IOException ex, final int n, final HttpContext httpContext) {
        throw new RuntimeException("Stub!");
    }
}
