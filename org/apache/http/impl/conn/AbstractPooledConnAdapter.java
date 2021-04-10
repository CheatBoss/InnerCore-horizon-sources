package org.apache.http.impl.conn;

import org.apache.http.conn.*;
import java.io.*;
import org.apache.http.conn.routing.*;
import org.apache.http.protocol.*;
import org.apache.http.params.*;
import org.apache.http.*;

@Deprecated
public abstract class AbstractPooledConnAdapter extends AbstractClientConnAdapter
{
    protected volatile AbstractPoolEntry poolEntry;
    
    protected AbstractPooledConnAdapter(final ClientConnectionManager clientConnectionManager, final AbstractPoolEntry abstractPoolEntry) {
        super(null, null);
        throw new RuntimeException("Stub!");
    }
    
    protected final void assertAttached() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void close() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected void detach() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HttpRoute getRoute() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Object getState() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void layerProtocol(final HttpContext httpContext, final HttpParams httpParams) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void open(final HttpRoute httpRoute, final HttpContext httpContext, final HttpParams httpParams) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void setState(final Object o) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void shutdown() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void tunnelProxy(final HttpHost httpHost, final boolean b, final HttpParams httpParams) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void tunnelTarget(final boolean b, final HttpParams httpParams) throws IOException {
        throw new RuntimeException("Stub!");
    }
}
