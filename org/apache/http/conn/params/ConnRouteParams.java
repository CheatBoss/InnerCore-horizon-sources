package org.apache.http.conn.params;

import org.apache.http.*;
import org.apache.http.conn.routing.*;
import org.apache.http.params.*;
import java.net.*;

@Deprecated
public class ConnRouteParams implements ConnRoutePNames
{
    public static final HttpHost NO_HOST;
    public static final HttpRoute NO_ROUTE;
    
    ConnRouteParams() {
        throw new RuntimeException("Stub!");
    }
    
    public static HttpHost getDefaultProxy(final HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }
    
    public static HttpRoute getForcedRoute(final HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }
    
    public static InetAddress getLocalAddress(final HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }
    
    public static void setDefaultProxy(final HttpParams httpParams, final HttpHost httpHost) {
        throw new RuntimeException("Stub!");
    }
    
    public static void setForcedRoute(final HttpParams httpParams, final HttpRoute httpRoute) {
        throw new RuntimeException("Stub!");
    }
    
    public static void setLocalAddress(final HttpParams httpParams, final InetAddress inetAddress) {
        throw new RuntimeException("Stub!");
    }
}
