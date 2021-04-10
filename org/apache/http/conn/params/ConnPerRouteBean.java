package org.apache.http.conn.params;

import org.apache.http.conn.routing.*;
import java.util.*;

@Deprecated
public final class ConnPerRouteBean implements ConnPerRoute
{
    public static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 2;
    
    public ConnPerRouteBean() {
        throw new RuntimeException("Stub!");
    }
    
    public ConnPerRouteBean(final int n) {
        throw new RuntimeException("Stub!");
    }
    
    public int getDefaultMax() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public int getMaxForRoute(final HttpRoute httpRoute) {
        throw new RuntimeException("Stub!");
    }
    
    public void setDefaultMaxPerRoute(final int n) {
        throw new RuntimeException("Stub!");
    }
    
    public void setMaxForRoute(final HttpRoute httpRoute, final int n) {
        throw new RuntimeException("Stub!");
    }
    
    public void setMaxForRoutes(final Map<HttpRoute, Integer> map) {
        throw new RuntimeException("Stub!");
    }
}
