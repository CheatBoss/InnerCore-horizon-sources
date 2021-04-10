package org.apache.http.impl.conn.tsccm;

import org.apache.http.impl.conn.*;
import java.util.*;
import java.util.concurrent.locks.*;
import java.util.concurrent.*;
import org.apache.http.conn.routing.*;
import org.apache.http.conn.*;
import java.lang.ref.*;

@Deprecated
public abstract class AbstractConnPool implements RefQueueHandler
{
    protected IdleConnectionHandler idleConnHandler;
    protected volatile boolean isShutDown;
    protected Set<BasicPoolEntryRef> issuedConnections;
    protected int numConnections;
    protected final Lock poolLock;
    protected ReferenceQueue<Object> refQueue;
    
    protected AbstractConnPool() {
        throw new RuntimeException("Stub!");
    }
    
    protected void closeConnection(final OperatedClientConnection operatedClientConnection) {
        throw new RuntimeException("Stub!");
    }
    
    public void closeExpiredConnections() {
        throw new RuntimeException("Stub!");
    }
    
    public void closeIdleConnections(final long n, final TimeUnit timeUnit) {
        throw new RuntimeException("Stub!");
    }
    
    public abstract void deleteClosedConnections();
    
    public void enableConnectionGC() throws IllegalStateException {
        throw new RuntimeException("Stub!");
    }
    
    public abstract void freeEntry(final BasicPoolEntry p0, final boolean p1, final long p2, final TimeUnit p3);
    
    public final BasicPoolEntry getEntry(final HttpRoute httpRoute, final Object o, final long n, final TimeUnit timeUnit) throws ConnectionPoolTimeoutException, InterruptedException {
        throw new RuntimeException("Stub!");
    }
    
    protected abstract void handleLostEntry(final HttpRoute p0);
    
    @Override
    public void handleReference(final Reference reference) {
        throw new RuntimeException("Stub!");
    }
    
    public abstract PoolEntryRequest requestPoolEntry(final HttpRoute p0, final Object p1);
    
    public void shutdown() {
        throw new RuntimeException("Stub!");
    }
}
