package org.apache.http.impl.conn;

import org.apache.http.conn.scheme.*;
import org.apache.http.protocol.*;
import org.apache.http.conn.routing.*;
import org.apache.http.*;

@Deprecated
public class DefaultHttpRoutePlanner implements HttpRoutePlanner
{
    protected SchemeRegistry schemeRegistry;
    
    public DefaultHttpRoutePlanner(final SchemeRegistry schemeRegistry) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HttpRoute determineRoute(final HttpHost httpHost, final HttpRequest httpRequest, final HttpContext httpContext) throws HttpException {
        throw new RuntimeException("Stub!");
    }
}
