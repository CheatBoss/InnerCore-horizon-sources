package org.apache.http.protocol;

import org.apache.http.*;
import java.io.*;

@Deprecated
public class HttpRequestExecutor
{
    public HttpRequestExecutor() {
        throw new RuntimeException("Stub!");
    }
    
    protected boolean canResponseHaveBody(final HttpRequest httpRequest, final HttpResponse httpResponse) {
        throw new RuntimeException("Stub!");
    }
    
    protected HttpResponse doReceiveResponse(final HttpRequest httpRequest, final HttpClientConnection httpClientConnection, final HttpContext httpContext) throws HttpException, IOException {
        throw new RuntimeException("Stub!");
    }
    
    protected HttpResponse doSendRequest(final HttpRequest httpRequest, final HttpClientConnection httpClientConnection, final HttpContext httpContext) throws IOException, HttpException {
        throw new RuntimeException("Stub!");
    }
    
    public HttpResponse execute(final HttpRequest httpRequest, final HttpClientConnection httpClientConnection, final HttpContext httpContext) throws IOException, HttpException {
        throw new RuntimeException("Stub!");
    }
    
    public void postProcess(final HttpResponse httpResponse, final HttpProcessor httpProcessor, final HttpContext httpContext) throws HttpException, IOException {
        throw new RuntimeException("Stub!");
    }
    
    public void preProcess(final HttpRequest httpRequest, final HttpProcessor httpProcessor, final HttpContext httpContext) throws HttpException, IOException {
        throw new RuntimeException("Stub!");
    }
}
