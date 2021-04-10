package org.apache.http.protocol;

import org.apache.http.*;
import java.io.*;

@Deprecated
public class ResponseDate implements HttpResponseInterceptor
{
    public ResponseDate() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void process(final HttpResponse httpResponse, final HttpContext httpContext) throws HttpException, IOException {
        throw new RuntimeException("Stub!");
    }
}
