package org.apache.http.impl.client;

import org.apache.http.conn.*;
import org.apache.http.*;
import org.apache.http.protocol.*;

@Deprecated
public class DefaultConnectionKeepAliveStrategy implements ConnectionKeepAliveStrategy
{
    public DefaultConnectionKeepAliveStrategy() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public long getKeepAliveDuration(final HttpResponse httpResponse, final HttpContext httpContext) {
        throw new RuntimeException("Stub!");
    }
}
