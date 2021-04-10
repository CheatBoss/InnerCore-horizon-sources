package org.apache.http.protocol;

import org.apache.http.*;
import java.io.*;

@Deprecated
public class RequestUserAgent implements HttpRequestInterceptor
{
    public RequestUserAgent() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void process(final HttpRequest httpRequest, final HttpContext httpContext) throws HttpException, IOException {
        throw new RuntimeException("Stub!");
    }
}
