package org.apache.http.conn.routing;

@Deprecated
public class BasicRouteDirector implements HttpRouteDirector
{
    public BasicRouteDirector() {
        throw new RuntimeException("Stub!");
    }
    
    protected int directStep(final RouteInfo routeInfo, final RouteInfo routeInfo2) {
        throw new RuntimeException("Stub!");
    }
    
    protected int firstStep(final RouteInfo routeInfo) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public int nextStep(final RouteInfo routeInfo, final RouteInfo routeInfo2) {
        throw new RuntimeException("Stub!");
    }
    
    protected int proxiedStep(final RouteInfo routeInfo, final RouteInfo routeInfo2) {
        throw new RuntimeException("Stub!");
    }
}
