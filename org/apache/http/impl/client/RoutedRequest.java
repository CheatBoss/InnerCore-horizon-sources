package org.apache.http.impl.client;

import org.apache.http.conn.routing.*;

@Deprecated
public class RoutedRequest
{
    protected final RequestWrapper request;
    protected final HttpRoute route;
    
    public RoutedRequest(final RequestWrapper requestWrapper, final HttpRoute httpRoute) {
        throw new RuntimeException("Stub!");
    }
    
    public final RequestWrapper getRequest() {
        throw new RuntimeException("Stub!");
    }
    
    public final HttpRoute getRoute() {
        throw new RuntimeException("Stub!");
    }
}
