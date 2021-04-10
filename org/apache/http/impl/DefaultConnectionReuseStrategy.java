package org.apache.http.impl;

import org.apache.http.*;
import org.apache.http.protocol.*;

@Deprecated
public class DefaultConnectionReuseStrategy implements ConnectionReuseStrategy
{
    public DefaultConnectionReuseStrategy() {
        throw new RuntimeException("Stub!");
    }
    
    protected TokenIterator createTokenIterator(final HeaderIterator headerIterator) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean keepAlive(final HttpResponse httpResponse, final HttpContext httpContext) {
        throw new RuntimeException("Stub!");
    }
}
