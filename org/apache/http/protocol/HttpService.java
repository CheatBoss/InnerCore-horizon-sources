package org.apache.http.protocol;

import java.io.*;
import org.apache.http.params.*;
import org.apache.http.*;

@Deprecated
public class HttpService
{
    public HttpService(final HttpProcessor httpProcessor, final ConnectionReuseStrategy connectionReuseStrategy, final HttpResponseFactory httpResponseFactory) {
        throw new RuntimeException("Stub!");
    }
    
    protected void doService(final HttpRequest httpRequest, final HttpResponse httpResponse, final HttpContext httpContext) throws HttpException, IOException {
        throw new RuntimeException("Stub!");
    }
    
    public HttpParams getParams() {
        throw new RuntimeException("Stub!");
    }
    
    protected void handleException(final HttpException ex, final HttpResponse httpResponse) {
        throw new RuntimeException("Stub!");
    }
    
    public void handleRequest(final HttpServerConnection httpServerConnection, final HttpContext httpContext) throws IOException, HttpException {
        throw new RuntimeException("Stub!");
    }
    
    public void setConnReuseStrategy(final ConnectionReuseStrategy connectionReuseStrategy) {
        throw new RuntimeException("Stub!");
    }
    
    public void setExpectationVerifier(final HttpExpectationVerifier httpExpectationVerifier) {
        throw new RuntimeException("Stub!");
    }
    
    public void setHandlerResolver(final HttpRequestHandlerResolver httpRequestHandlerResolver) {
        throw new RuntimeException("Stub!");
    }
    
    public void setHttpProcessor(final HttpProcessor httpProcessor) {
        throw new RuntimeException("Stub!");
    }
    
    public void setParams(final HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }
    
    public void setResponseFactory(final HttpResponseFactory httpResponseFactory) {
        throw new RuntimeException("Stub!");
    }
}
