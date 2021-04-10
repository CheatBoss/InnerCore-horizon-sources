package org.apache.http.client.protocol;

import org.apache.http.protocol.*;
import org.apache.http.*;
import java.io.*;

@Deprecated
public class ResponseProcessCookies implements HttpResponseInterceptor
{
    public ResponseProcessCookies() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void process(final HttpResponse httpResponse, final HttpContext httpContext) throws HttpException, IOException {
        throw new RuntimeException("Stub!");
    }
}
