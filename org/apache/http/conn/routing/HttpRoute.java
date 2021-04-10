package org.apache.http.conn.routing;

import org.apache.http.*;
import java.net.*;

@Deprecated
public final class HttpRoute implements RouteInfo
{
    public HttpRoute(final HttpHost httpHost) {
        throw new RuntimeException("Stub!");
    }
    
    public HttpRoute(final HttpHost httpHost, final InetAddress inetAddress, final HttpHost httpHost2, final boolean b) {
        throw new RuntimeException("Stub!");
    }
    
    public HttpRoute(final HttpHost httpHost, final InetAddress inetAddress, final HttpHost httpHost2, final boolean b, final TunnelType tunnelType, final LayerType layerType) {
        throw new RuntimeException("Stub!");
    }
    
    public HttpRoute(final HttpHost httpHost, final InetAddress inetAddress, final boolean b) {
        throw new RuntimeException("Stub!");
    }
    
    public HttpRoute(final HttpHost httpHost, final InetAddress inetAddress, final HttpHost[] array, final boolean b, final TunnelType tunnelType, final LayerType layerType) {
        throw new RuntimeException("Stub!");
    }
    
    public Object clone() throws CloneNotSupportedException {
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
    
    @Override
    public final String toString() {
        throw new RuntimeException("Stub!");
    }
}
