package org.apache.http.client.protocol;

import org.apache.http.protocol.*;
import org.apache.http.*;
import java.io.*;

@Deprecated
public class RequestAddCookies implements HttpRequestInterceptor
{
    public RequestAddCookies() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void process(final HttpRequest httpRequest, final HttpContext httpContext) throws HttpException, IOException {
        throw new RuntimeException("Stub!");
    }
}
