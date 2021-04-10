package org.apache.http.impl.conn.tsccm;

import org.apache.http.conn.scheme.*;
import org.apache.http.params.*;
import java.util.concurrent.*;
import org.apache.http.conn.routing.*;
import org.apache.http.conn.*;

@Deprecated
public class ThreadSafeClientConnManager implements ClientConnectionManager
{
    protected ClientConnectionOperator connOperator;
    protected final AbstractConnPool connectionPool;
    protected SchemeRegistry schemeRegistry;
    
    public ThreadSafeClientConnManager(final HttpParams httpParams, final SchemeRegistry schemeRegistry) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void closeExpiredConnections() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void closeIdleConnections(final long n, final TimeUnit timeUnit) {
        throw new RuntimeException("Stub!");
    }
    
    protected ClientConnectionOperator createConnectionOperator(final SchemeRegistry schemeRegistry) {
        throw new RuntimeException("Stub!");
    }
    
    protected AbstractConnPool createConnectionPool(final HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected void finalize() throws Throwable {
        throw new RuntimeException("Stub!");
    }
    
    public int getConnectionsInPool() {
        throw new RuntimeException("Stub!");
    }
    
    public int getConnectionsInPool(final HttpRoute httpRoute) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public SchemeRegistry getSchemeRegistry() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void releaseConnection(final ManagedClientConnection managedClientConnection, final long n, final TimeUnit timeUnit) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public ClientConnectionRequest requestConnection(final HttpRoute httpRoute, final Object o) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void shutdown() {
        throw new RuntimeException("Stub!");
    }
}
