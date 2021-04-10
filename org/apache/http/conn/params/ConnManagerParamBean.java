package org.apache.http.conn.params;

import org.apache.http.params.*;

@Deprecated
public class ConnManagerParamBean extends HttpAbstractParamBean
{
    public ConnManagerParamBean(final HttpParams httpParams) {
        super(null);
        throw new RuntimeException("Stub!");
    }
    
    public void setConnectionsPerRoute(final ConnPerRouteBean connPerRouteBean) {
        throw new RuntimeException("Stub!");
    }
    
    public void setMaxTotalConnections(final int n) {
        throw new RuntimeException("Stub!");
    }
    
    public void setTimeout(final long n) {
        throw new RuntimeException("Stub!");
    }
}
