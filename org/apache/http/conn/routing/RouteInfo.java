package org.apache.http.conn.routing;

import org.apache.http.*;
import java.net.*;

@Deprecated
public interface RouteInfo
{
    int getHopCount();
    
    HttpHost getHopTarget(final int p0);
    
    LayerType getLayerType();
    
    InetAddress getLocalAddress();
    
    HttpHost getProxyHost();
    
    HttpHost getTargetHost();
    
    TunnelType getTunnelType();
    
    boolean isLayered();
    
    boolean isSecure();
    
    boolean isTunnelled();
    
    public enum LayerType
    {
        LAYERED, 
        PLAIN;
    }
    
    public enum TunnelType
    {
        PLAIN, 
        TUNNELLED;
    }
}
