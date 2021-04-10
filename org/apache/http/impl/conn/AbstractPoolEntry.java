package org.apache.http.impl.conn;

import org.apache.http.conn.*;
import org.apache.http.conn.routing.*;
import org.apache.http.protocol.*;
import org.apache.http.params.*;
import java.io.*;
import org.apache.http.*;

@Deprecated
public abstract class AbstractPoolEntry
{
    protected final ClientConnectionOperator connOperator;
    protected final OperatedClientConnection connection;
    protected volatile HttpRoute route;
    protected volatile Object state;
    protected volatile RouteTracker tracker;
    
    protected AbstractPoolEntry(final ClientConnectionOperator clientConnectionOperator, final HttpRoute httpRoute) {
        throw new RuntimeException("Stub!");
    }
    
    public Object getState() {
        throw new RuntimeException("Stub!");
    }
    
    public void layerProtocol(final HttpContext httpContext, final HttpParams httpParams) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    public void open(final HttpRoute httpRoute, final HttpContext httpContext, final HttpParams httpParams) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    public void setState(final Object o) {
        throw new RuntimeException("Stub!");
    }
    
    protected void shutdownEntry() {
        throw new RuntimeException("Stub!");
    }
    
    public void tunnelProxy(final HttpHost httpHost, final boolean b, final HttpParams httpParams) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    public void tunnelTarget(final boolean b, final HttpParams httpParams) throws IOException {
        throw new RuntimeException("Stub!");
    }
}
