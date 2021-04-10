package org.apache.http.impl;

import org.apache.http.protocol.*;
import java.util.*;
import org.apache.http.*;

@Deprecated
public class DefaultHttpResponseFactory implements HttpResponseFactory
{
    protected final ReasonPhraseCatalog reasonCatalog;
    
    public DefaultHttpResponseFactory() {
        throw new RuntimeException("Stub!");
    }
    
    public DefaultHttpResponseFactory(final ReasonPhraseCatalog reasonPhraseCatalog) {
        throw new RuntimeException("Stub!");
    }
    
    protected Locale determineLocale(final HttpContext httpContext) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HttpResponse newHttpResponse(final ProtocolVersion protocolVersion, final int n, final HttpContext httpContext) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HttpResponse newHttpResponse(final StatusLine statusLine, final HttpContext httpContext) {
        throw new RuntimeException("Stub!");
    }
}
