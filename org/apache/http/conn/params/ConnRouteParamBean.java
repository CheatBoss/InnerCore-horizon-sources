package org.apache.http.conn.params;

import org.apache.http.params.*;
import org.apache.http.*;
import org.apache.http.conn.routing.*;
import java.net.*;

@Deprecated
public class ConnRouteParamBean extends HttpAbstractParamBean
{
    public ConnRouteParamBean(final HttpParams httpParams) {
        super(null);
        throw new RuntimeException("Stub!");
    }
    
    public void setDefaultProxy(final HttpHost httpHost) {
        throw new RuntimeException("Stub!");
    }
    
    public void setForcedRoute(final HttpRoute httpRoute) {
        throw new RuntimeException("Stub!");
    }
    
    public void setLocalAddress(final InetAddress inetAddress) {
        throw new RuntimeException("Stub!");
    }
}
