package org.apache.http.impl.conn;

import org.apache.http.conn.scheme.*;
import org.apache.http.params.*;
import java.util.concurrent.*;
import org.apache.http.conn.routing.*;
import org.apache.http.conn.*;
import java.io.*;

@Deprecated
public class SingleClientConnManager implements ClientConnectionManager
{
    public static final String MISUSE_MESSAGE = "Invalid use of SingleClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.";
    protected boolean alwaysShutDown;
    protected ClientConnectionOperator connOperator;
    protected long connectionExpiresTime;
    protected volatile boolean isShutDown;
    protected long lastReleaseTime;
    protected ConnAdapter managedConn;
    protected SchemeRegistry schemeRegistry;
    protected PoolEntry uniquePoolEntry;
    
    public SingleClientConnManager(final HttpParams httpParams, final SchemeRegistry schemeRegistry) {
        throw new RuntimeException("Stub!");
    }
    
    protected final void assertStillUp() throws IllegalStateException {
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
    
    @Override
    protected void finalize() throws Throwable {
        throw new RuntimeException("Stub!");
    }
    
    public ManagedClientConnection getConnection(final HttpRoute httpRoute, final Object o) {
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
    public final ClientConnectionRequest requestConnection(final HttpRoute httpRoute, final Object o) {
        throw new RuntimeException("Stub!");
    }
    
    protected void revokeConnection() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void shutdown() {
        throw new RuntimeException("Stub!");
    }
    
    protected class ConnAdapter extends AbstractPooledConnAdapter
    {
        protected ConnAdapter(final PoolEntry poolEntry, final HttpRoute httpRoute) {
            super(null, (AbstractPoolEntry)null);
            throw new RuntimeException("Stub!");
        }
    }
    
    protected class PoolEntry extends AbstractPoolEntry
    {
        protected PoolEntry() {
            super(null, null);
            throw new RuntimeException("Stub!");
        }
        
        protected void close() throws IOException {
            throw new RuntimeException("Stub!");
        }
        
        protected void shutdown() throws IOException {
            throw new RuntimeException("Stub!");
        }
    }
}
