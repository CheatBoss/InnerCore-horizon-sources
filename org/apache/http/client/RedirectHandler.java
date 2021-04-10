package org.apache.http.client;

import org.apache.http.protocol.*;
import java.net.*;
import org.apache.http.*;

@Deprecated
public interface RedirectHandler
{
    URI getLocationURI(final HttpResponse p0, final HttpContext p1) throws ProtocolException;
    
    boolean isRedirectRequested(final HttpResponse p0, final HttpContext p1);
}
