package org.apache.http.impl.client;

import org.apache.http.params.*;
import org.apache.http.auth.*;
import org.apache.http.conn.*;
import org.apache.http.conn.routing.*;
import org.apache.http.cookie.*;
import org.apache.http.protocol.*;
import java.io.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.*;

@Deprecated
public abstract class AbstractHttpClient implements HttpClient
{
    protected AbstractHttpClient(final ClientConnectionManager clientConnectionManager, final HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }
    
    public void addRequestInterceptor(final HttpRequestInterceptor httpRequestInterceptor) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public void addRequestInterceptor(final HttpRequestInterceptor httpRequestInterceptor, final int n) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public void addResponseInterceptor(final HttpResponseInterceptor httpResponseInterceptor) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public void addResponseInterceptor(final HttpResponseInterceptor httpResponseInterceptor, final int n) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public void clearRequestInterceptors() {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public void clearResponseInterceptors() {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    protected abstract AuthSchemeRegistry createAuthSchemeRegistry();
    
    protected abstract ClientConnectionManager createClientConnectionManager();
    
    protected RequestDirector createClientRequestDirector(final HttpRequestExecutor httpRequestExecutor, final ClientConnectionManager clientConnectionManager, final ConnectionReuseStrategy connectionReuseStrategy, final ConnectionKeepAliveStrategy connectionKeepAliveStrategy, final HttpRoutePlanner httpRoutePlanner, final HttpProcessor httpProcessor, final HttpRequestRetryHandler httpRequestRetryHandler, final RedirectHandler redirectHandler, final AuthenticationHandler authenticationHandler, final AuthenticationHandler authenticationHandler2, final UserTokenHandler userTokenHandler, final HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }
    
    protected abstract ConnectionKeepAliveStrategy createConnectionKeepAliveStrategy();
    
    protected abstract ConnectionReuseStrategy createConnectionReuseStrategy();
    
    protected abstract CookieSpecRegistry createCookieSpecRegistry();
    
    protected abstract CookieStore createCookieStore();
    
    protected abstract CredentialsProvider createCredentialsProvider();
    
    protected abstract HttpContext createHttpContext();
    
    protected abstract HttpParams createHttpParams();
    
    protected abstract BasicHttpProcessor createHttpProcessor();
    
    protected abstract HttpRequestRetryHandler createHttpRequestRetryHandler();
    
    protected abstract HttpRoutePlanner createHttpRoutePlanner();
    
    protected abstract AuthenticationHandler createProxyAuthenticationHandler();
    
    protected abstract RedirectHandler createRedirectHandler();
    
    protected abstract HttpRequestExecutor createRequestExecutor();
    
    protected abstract AuthenticationHandler createTargetAuthenticationHandler();
    
    protected abstract UserTokenHandler createUserTokenHandler();
    
    protected HttpParams determineParams(final HttpRequest httpRequest) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public <T> T execute(final HttpHost httpHost, final HttpRequest httpRequest, final ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public <T> T execute(final HttpHost httpHost, final HttpRequest httpRequest, final ResponseHandler<? extends T> responseHandler, final HttpContext httpContext) throws IOException, ClientProtocolException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public <T> T execute(final HttpUriRequest httpUriRequest, final ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public <T> T execute(final HttpUriRequest httpUriRequest, final ResponseHandler<? extends T> responseHandler, final HttpContext httpContext) throws IOException, ClientProtocolException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public final HttpResponse execute(final HttpHost httpHost, final HttpRequest httpRequest) throws IOException, ClientProtocolException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public final HttpResponse execute(final HttpHost httpHost, final HttpRequest httpRequest, final HttpContext httpContext) throws IOException, ClientProtocolException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public final HttpResponse execute(final HttpUriRequest httpUriRequest) throws IOException, ClientProtocolException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public final HttpResponse execute(final HttpUriRequest httpUriRequest, final HttpContext httpContext) throws IOException, ClientProtocolException {
        throw new RuntimeException("Stub!");
    }
    
    public final AuthSchemeRegistry getAuthSchemes() {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public final ConnectionKeepAliveStrategy getConnectionKeepAliveStrategy() {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    @Override
    public final ClientConnectionManager getConnectionManager() {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public final ConnectionReuseStrategy getConnectionReuseStrategy() {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public final CookieSpecRegistry getCookieSpecs() {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public final CookieStore getCookieStore() {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public final CredentialsProvider getCredentialsProvider() {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    protected final BasicHttpProcessor getHttpProcessor() {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public final HttpRequestRetryHandler getHttpRequestRetryHandler() {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    @Override
    public final HttpParams getParams() {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public final AuthenticationHandler getProxyAuthenticationHandler() {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public final RedirectHandler getRedirectHandler() {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public final HttpRequestExecutor getRequestExecutor() {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public HttpRequestInterceptor getRequestInterceptor(final int n) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public int getRequestInterceptorCount() {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public HttpResponseInterceptor getResponseInterceptor(final int n) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public int getResponseInterceptorCount() {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public final HttpRoutePlanner getRoutePlanner() {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public final AuthenticationHandler getTargetAuthenticationHandler() {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public final UserTokenHandler getUserTokenHandler() {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public void removeRequestInterceptorByClass(final Class<? extends HttpRequestInterceptor> clazz) {
        throw new RuntimeException("Stub!");
    }
    
    public void removeResponseInterceptorByClass(final Class<? extends HttpResponseInterceptor> clazz) {
        throw new RuntimeException("Stub!");
    }
    
    public void setAuthSchemes(final AuthSchemeRegistry authSchemeRegistry) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public void setCookieSpecs(final CookieSpecRegistry cookieSpecRegistry) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public void setCookieStore(final CookieStore cookieStore) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public void setCredentialsProvider(final CredentialsProvider credentialsProvider) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public void setHttpRequestRetryHandler(final HttpRequestRetryHandler httpRequestRetryHandler) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public void setKeepAliveStrategy(final ConnectionKeepAliveStrategy connectionKeepAliveStrategy) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public void setParams(final HttpParams httpParams) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public void setProxyAuthenticationHandler(final AuthenticationHandler authenticationHandler) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public void setRedirectHandler(final RedirectHandler redirectHandler) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public void setReuseStrategy(final ConnectionReuseStrategy connectionReuseStrategy) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public void setRoutePlanner(final HttpRoutePlanner httpRoutePlanner) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public void setTargetAuthenticationHandler(final AuthenticationHandler authenticationHandler) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public void setUserTokenHandler(final UserTokenHandler userTokenHandler) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
}
