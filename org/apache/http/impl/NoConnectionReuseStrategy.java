package org.apache.http.impl;

import org.apache.http.*;
import org.apache.http.protocol.*;

@Deprecated
public class NoConnectionReuseStrategy implements ConnectionReuseStrategy
{
    public NoConnectionReuseStrategy() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean keepAlive(final HttpResponse httpResponse, final HttpContext httpContext) {
        throw new RuntimeException("Stub!");
    }
}
