package org.apache.http.impl.client;

import org.apache.http.conn.*;
import org.apache.http.params.*;
import org.apache.http.client.*;
import org.apache.http.conn.routing.*;
import org.apache.http.protocol.*;
import java.io.*;
import org.apache.http.*;

@Deprecated
public class DefaultRequestDirector implements RequestDirector
{
    protected final ClientConnectionManager connManager;
    protected final HttpProcessor httpProcessor;
    protected final ConnectionKeepAliveStrategy keepAliveStrategy;
    protected ManagedClientConnection managedConn;
    protected final HttpParams params;
    protected final RedirectHandler redirectHandler;
    protected final HttpRequestExecutor requestExec;
    protected final HttpRequestRetryHandler retryHandler;
    protected final ConnectionReuseStrategy reuseStrategy;
    protected final HttpRoutePlanner routePlanner;
    
    public DefaultRequestDirector(final HttpRequestExecutor httpRequestExecutor, final ClientConnectionManager clientConnectionManager, final ConnectionReuseStrategy connectionReuseStrategy, final ConnectionKeepAliveStrategy connectionKeepAliveStrategy, final HttpRoutePlanner httpRoutePlanner, final HttpProcessor httpProcessor, final HttpRequestRetryHandler httpRequestRetryHandler, final RedirectHandler redirectHandler, final AuthenticationHandler authenticationHandler, final AuthenticationHandler authenticationHandler2, final UserTokenHandler userTokenHandler, final HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }
    
    protected HttpRequest createConnectRequest(final HttpRoute httpRoute, final HttpContext httpContext) {
        throw new RuntimeException("Stub!");
    }
    
    protected boolean createTunnelToProxy(final HttpRoute httpRoute, final int n, final HttpContext httpContext) throws HttpException, IOException {
        throw new RuntimeException("Stub!");
    }
    
    protected boolean createTunnelToTarget(final HttpRoute httpRoute, final HttpContext httpContext) throws HttpException, IOException {
        throw new RuntimeException("Stub!");
    }
    
    protected HttpRoute determineRoute(final HttpHost httpHost, final HttpRequest httpRequest, final HttpContext httpContext) throws HttpException {
        throw new RuntimeException("Stub!");
    }
    
    protected void establishRoute(final HttpRoute httpRoute, final HttpContext httpContext) throws HttpException, IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HttpResponse execute(final HttpHost httpHost, final HttpRequest httpRequest, final HttpContext httpContext) throws HttpException, IOException {
        throw new RuntimeException("Stub!");
    }
    
    protected RoutedRequest handleResponse(final RoutedRequest routedRequest, final HttpResponse httpResponse, final HttpContext httpContext) throws HttpException, IOException {
        throw new RuntimeException("Stub!");
    }
    
    protected void releaseConnection() {
        throw new RuntimeException("Stub!");
    }
    
    protected void rewriteRequestURI(final RequestWrapper requestWrapper, final HttpRoute httpRoute) throws ProtocolException {
        throw new RuntimeException("Stub!");
    }
}
