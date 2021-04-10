package org.apache.http.impl.client;

import org.apache.http.params.*;
import org.apache.http.auth.*;
import org.apache.http.conn.*;
import org.apache.http.*;
import org.apache.http.cookie.*;
import org.apache.http.conn.routing.*;
import org.apache.http.protocol.*;
import org.apache.http.client.*;

@Deprecated
public class DefaultHttpClient extends AbstractHttpClient
{
    public DefaultHttpClient() {
        super(null, null);
        throw new RuntimeException("Stub!");
    }
    
    public DefaultHttpClient(final ClientConnectionManager clientConnectionManager, final HttpParams httpParams) {
        super(null, null);
        throw new RuntimeException("Stub!");
    }
    
    public DefaultHttpClient(final HttpParams httpParams) {
        super(null, null);
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected AuthSchemeRegistry createAuthSchemeRegistry() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected ClientConnectionManager createClientConnectionManager() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected ConnectionKeepAliveStrategy createConnectionKeepAliveStrategy() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected ConnectionReuseStrategy createConnectionReuseStrategy() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected CookieSpecRegistry createCookieSpecRegistry() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected CookieStore createCookieStore() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected CredentialsProvider createCredentialsProvider() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected HttpContext createHttpContext() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected HttpParams createHttpParams() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected BasicHttpProcessor createHttpProcessor() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected HttpRequestRetryHandler createHttpRequestRetryHandler() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected HttpRoutePlanner createHttpRoutePlanner() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected AuthenticationHandler createProxyAuthenticationHandler() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected RedirectHandler createRedirectHandler() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected HttpRequestExecutor createRequestExecutor() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected AuthenticationHandler createTargetAuthenticationHandler() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected UserTokenHandler createUserTokenHandler() {
        throw new RuntimeException("Stub!");
    }
}
