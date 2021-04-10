package org.apache.http.protocol;

import org.apache.http.*;
import java.io.*;

@Deprecated
public class ResponseConnControl implements HttpResponseInterceptor
{
    public ResponseConnControl() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void process(final HttpResponse httpResponse, final HttpContext httpContext) throws HttpException, IOException {
        throw new RuntimeException("Stub!");
    }
}
