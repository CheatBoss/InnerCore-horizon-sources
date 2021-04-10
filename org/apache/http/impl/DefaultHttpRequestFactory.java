package org.apache.http.impl;

import org.apache.http.*;

@Deprecated
public class DefaultHttpRequestFactory implements HttpRequestFactory
{
    public DefaultHttpRequestFactory() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HttpRequest newHttpRequest(final String s, final String s2) throws MethodNotSupportedException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HttpRequest newHttpRequest(final RequestLine requestLine) throws MethodNotSupportedException {
        throw new RuntimeException("Stub!");
    }
}
