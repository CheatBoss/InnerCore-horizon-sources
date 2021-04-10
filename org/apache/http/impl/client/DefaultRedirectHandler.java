package org.apache.http.impl.client;

import org.apache.http.client.*;
import org.apache.http.protocol.*;
import java.net.*;
import org.apache.http.*;

@Deprecated
public class DefaultRedirectHandler implements RedirectHandler
{
    public DefaultRedirectHandler() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public URI getLocationURI(final HttpResponse httpResponse, final HttpContext httpContext) throws ProtocolException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean isRedirectRequested(final HttpResponse httpResponse, final HttpContext httpContext) {
        throw new RuntimeException("Stub!");
    }
}
