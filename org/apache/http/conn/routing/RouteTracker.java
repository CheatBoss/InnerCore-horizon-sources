package org.apache.http.conn.routing;

import org.apache.http.*;
import java.net.*;

@Deprecated
public final class RouteTracker implements RouteInfo
{
    public RouteTracker(final HttpHost httpHost, final InetAddress inetAddress) {
        throw new RuntimeException("Stub!");
    }
    
    public RouteTracker(final HttpRoute httpRoute) {
        throw new RuntimeException("Stub!");
    }
    
    public Object clone() throws CloneNotSupportedException {
        throw new RuntimeException("Stub!");
    }
    
    public final void connectProxy(final HttpHost httpHost, final boolean b) {
        throw new RuntimeException("Stub!");
    }
    
    public final void connectTarget(final boolean b) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public final boolean equals(final Object o) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public final int getHopCount() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public final HttpHost getHopTarget(final int n) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public final LayerType getLayerType() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public final InetAddress getLocalAddress() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public final HttpHost getProxyHost() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public final HttpHost getTargetHost() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public final TunnelType getTunnelType() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public final int hashCode() {
        throw new RuntimeException("Stub!");
    }
    
    public final boolean isConnected() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public final boolean isLayered() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public final boolean isSecure() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public final boolean isTunnelled() {
        throw new RuntimeException("Stub!");
    }
    
    public final void layerProtocol(final boolean b) {
        throw new RuntimeException("Stub!");
    }
    
    public final HttpRoute toRoute() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public final String toString() {
        throw new RuntimeException("Stub!");
    }
    
    public final void tunnelProxy(final HttpHost httpHost, final boolean b) {
        throw new RuntimeException("Stub!");
    }
    
    public final void tunnelTarget(final boolean b) {
        throw new RuntimeException("Stub!");
    }
}
