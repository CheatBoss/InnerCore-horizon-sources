package org.apache.http.impl.conn;

import org.apache.http.conn.scheme.*;
import java.util.*;
import org.apache.http.protocol.*;
import org.apache.http.*;
import org.apache.http.conn.routing.*;
import java.net.*;

@Deprecated
public class ProxySelectorRoutePlanner implements HttpRoutePlanner
{
    protected ProxySelector proxySelector;
    protected SchemeRegistry schemeRegistry;
    
    public ProxySelectorRoutePlanner(final SchemeRegistry schemeRegistry, final ProxySelector proxySelector) {
        throw new RuntimeException("Stub!");
    }
    
    protected Proxy chooseProxy(final List<Proxy> list, final HttpHost httpHost, final HttpRequest httpRequest, final HttpContext httpContext) {
        throw new RuntimeException("Stub!");
    }
    
    protected HttpHost determineProxy(final HttpHost httpHost, final HttpRequest httpRequest, final HttpContext httpContext) throws HttpException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HttpRoute determineRoute(final HttpHost httpHost, final HttpRequest httpRequest, final HttpContext httpContext) throws HttpException {
        throw new RuntimeException("Stub!");
    }
    
    protected String getHost(final InetSocketAddress inetSocketAddress) {
        throw new RuntimeException("Stub!");
    }
    
    public ProxySelector getProxySelector() {
        throw new RuntimeException("Stub!");
    }
    
    public void setProxySelector(final ProxySelector proxySelector) {
        throw new RuntimeException("Stub!");
    }
}
