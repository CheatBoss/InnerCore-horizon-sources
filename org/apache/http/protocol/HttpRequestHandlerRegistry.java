package org.apache.http.protocol;

import java.util.*;

@Deprecated
public class HttpRequestHandlerRegistry implements HttpRequestHandlerResolver
{
    public HttpRequestHandlerRegistry() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HttpRequestHandler lookup(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    @Deprecated
    protected boolean matchUriRequestPattern(final String s, final String s2) {
        throw new RuntimeException("Stub!");
    }
    
    public void register(final String s, final HttpRequestHandler httpRequestHandler) {
        throw new RuntimeException("Stub!");
    }
    
    public void setHandlers(final Map map) {
        throw new RuntimeException("Stub!");
    }
    
    public void unregister(final String s) {
        throw new RuntimeException("Stub!");
    }
}
