package org.apache.http.impl.client;

import org.apache.http.*;

@Deprecated
public class TunnelRefusedException extends HttpException
{
    public TunnelRefusedException(final String s, final HttpResponse httpResponse) {
        throw new RuntimeException("Stub!");
    }
    
    public HttpResponse getResponse() {
        throw new RuntimeException("Stub!");
    }
}
