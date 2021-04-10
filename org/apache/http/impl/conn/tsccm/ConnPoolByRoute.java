package org.apache.http.impl.conn.tsccm;

import java.util.*;
import org.apache.http.conn.routing.*;
import org.apache.http.params.*;
import java.util.concurrent.*;
import org.apache.http.conn.*;
import java.util.concurrent.locks.*;

@Deprecated
public class ConnPoolByRoute extends AbstractConnPool
{
    protected Queue<BasicPoolEntry> freeConnections;
    protected final int maxTotalConnections;
    protected final ClientConnectionOperator operator;
    protected final Map<HttpRoute, RouteSpecificPool> routeToPool;
    protected Queue<WaitingThread> waitingThreads;
    
    public ConnPoolByRoute(final ClientConnectionOperator clientConnectionOperator, final HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }
    
    protected BasicPoolEntry createEntry(final RouteSpecificPool routeSpecificPool, final ClientConnectionOperator clientConnectionOperator) {
        throw new RuntimeException("Stub!");
    }
    
    protected Queue<BasicPoolEntry> createFreeConnQueue() {
        throw new RuntimeException("Stub!");
    }
    
    protected Map<HttpRoute, RouteSpecificPool> createRouteToPoolMap() {
        throw new RuntimeException("Stub!");
    }
    
    protected Queue<WaitingThread> createWaitingThreadQueue() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void deleteClosedConnections() {
        throw new RuntimeException("Stub!");
    }
    
    protected void deleteEntry(final BasicPoolEntry basicPoolEntry) {
        throw new RuntimeException("Stub!");
    }
    
    protected void deleteLeastUsedEntry() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void freeEntry(final BasicPoolEntry basicPoolEntry, final boolean b, final long n, final TimeUnit timeUnit) {
        throw new RuntimeException("Stub!");
    }
    
    public int getConnectionsInPool(final HttpRoute httpRoute) {
        throw new RuntimeException("Stub!");
    }
    
    protected BasicPoolEntry getEntryBlocking(final HttpRoute httpRoute, final Object o, final long n, final TimeUnit timeUnit, final WaitingThreadAborter waitingThreadAborter) throws ConnectionPoolTimeoutException, InterruptedException {
        throw new RuntimeException("Stub!");
    }
    
    protected BasicPoolEntry getFreeEntry(final RouteSpecificPool routeSpecificPool, final Object o) {
        throw new RuntimeException("Stub!");
    }
    
    protected RouteSpecificPool getRoutePool(final HttpRoute httpRoute, final boolean b) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected void handleLostEntry(final HttpRoute httpRoute) {
        throw new RuntimeException("Stub!");
    }
    
    protected RouteSpecificPool newRouteSpecificPool(final HttpRoute httpRoute) {
        throw new RuntimeException("Stub!");
    }
    
    protected WaitingThread newWaitingThread(final Condition condition, final RouteSpecificPool routeSpecificPool) {
        throw new RuntimeException("Stub!");
    }
    
    protected void notifyWaitingThread(final RouteSpecificPool routeSpecificPool) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public PoolEntryRequest requestPoolEntry(final HttpRoute httpRoute, final Object o) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void shutdown() {
        throw new RuntimeException("Stub!");
    }
}
