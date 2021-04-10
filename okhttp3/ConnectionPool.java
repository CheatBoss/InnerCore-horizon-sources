package okhttp3;

import okhttp3.internal.*;
import java.util.concurrent.*;
import java.lang.ref.*;
import okhttp3.internal.connection.*;
import okhttp3.internal.platform.*;
import java.util.*;
import java.net.*;
import javax.annotation.*;

public final class ConnectionPool
{
    private static final Executor executor;
    private final Runnable cleanupRunnable;
    boolean cleanupRunning;
    private final Deque<RealConnection> connections;
    private final long keepAliveDurationNs;
    private final int maxIdleConnections;
    final RouteDatabase routeDatabase;
    
    static {
        executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), Util.threadFactory("OkHttp ConnectionPool", true));
    }
    
    public ConnectionPool() {
        this(5, 5L, TimeUnit.MINUTES);
    }
    
    public ConnectionPool(final int maxIdleConnections, final long n, final TimeUnit timeUnit) {
        this.cleanupRunnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    final long cleanup = ConnectionPool.this.cleanup(System.nanoTime());
                    if (cleanup == -1L) {
                        break;
                    }
                    if (cleanup <= 0L) {
                        continue;
                    }
                    final long n = cleanup / 1000000L;
                    final ConnectionPool this$0 = ConnectionPool.this;
                    // monitorenter(this$0)
                    try {
                        try {
                            ConnectionPool.this.wait(n, (int)(cleanup - 1000000L * n));
                        }
                        finally {
                        }
                        // monitorexit(this$0)
                        // monitorexit(this$0)
                    }
                    catch (InterruptedException ex) {}
                }
            }
        };
        this.connections = new ArrayDeque<RealConnection>();
        this.routeDatabase = new RouteDatabase();
        this.maxIdleConnections = maxIdleConnections;
        this.keepAliveDurationNs = timeUnit.toNanos(n);
        if (n > 0L) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("keepAliveDuration <= 0: ");
        sb.append(n);
        throw new IllegalArgumentException(sb.toString());
    }
    
    private int pruneAndGetAllocationCount(final RealConnection realConnection, final long n) {
        final List<Reference<StreamAllocation>> allocations = realConnection.allocations;
        int i = 0;
        while (i < allocations.size()) {
            final Reference<StreamAllocation> reference = allocations.get(i);
            if (reference.get() != null) {
                ++i;
            }
            else {
                final StreamAllocation.StreamAllocationReference streamAllocationReference = (StreamAllocation.StreamAllocationReference)reference;
                final StringBuilder sb = new StringBuilder();
                sb.append("A connection to ");
                sb.append(realConnection.route().address().url());
                sb.append(" was leaked. Did you forget to close a response body?");
                Platform.get().logCloseableLeak(sb.toString(), streamAllocationReference.callStackTrace);
                allocations.remove(i);
                realConnection.noNewStreams = true;
                if (allocations.isEmpty()) {
                    realConnection.idleAtNanos = n - this.keepAliveDurationNs;
                    return 0;
                }
                continue;
            }
        }
        return allocations.size();
    }
    
    long cleanup(long n) {
        synchronized (this) {
            final Iterator<RealConnection> iterator = this.connections.iterator();
            long n2 = Long.MIN_VALUE;
            RealConnection realConnection = null;
            int n3 = 0;
            int n4 = 0;
            while (iterator.hasNext()) {
                final RealConnection realConnection2 = iterator.next();
                if (this.pruneAndGetAllocationCount(realConnection2, n) > 0) {
                    ++n4;
                }
                else {
                    final int n5 = n3 + 1;
                    final long n6 = n - realConnection2.idleAtNanos;
                    n3 = n5;
                    if (n6 <= n2) {
                        continue;
                    }
                    realConnection = realConnection2;
                    n2 = n6;
                    n3 = n5;
                }
            }
            if (n2 >= this.keepAliveDurationNs || n3 > this.maxIdleConnections) {
                this.connections.remove(realConnection);
                // monitorexit(this)
                Util.closeQuietly(realConnection.socket());
                return 0L;
            }
            if (n3 > 0) {
                n = this.keepAliveDurationNs;
                // monitorexit(this)
                return n - n2;
            }
            if (n4 > 0) {
                n = this.keepAliveDurationNs;
                return n;
            }
            this.cleanupRunning = false;
            return -1L;
        }
    }
    
    boolean connectionBecameIdle(final RealConnection realConnection) {
        if (!realConnection.noNewStreams && this.maxIdleConnections != 0) {
            this.notifyAll();
            return false;
        }
        this.connections.remove(realConnection);
        return true;
    }
    
    @Nullable
    Socket deduplicate(final Address address, final StreamAllocation streamAllocation) {
        for (final RealConnection realConnection : this.connections) {
            if (realConnection.isEligible(address, null) && realConnection.isMultiplexed() && realConnection != streamAllocation.connection()) {
                return streamAllocation.releaseAndAcquire(realConnection);
            }
        }
        return null;
    }
    
    @Nullable
    RealConnection get(final Address address, final StreamAllocation streamAllocation, final Route route) {
        for (final RealConnection realConnection : this.connections) {
            if (realConnection.isEligible(address, route)) {
                streamAllocation.acquire(realConnection, true);
                return realConnection;
            }
        }
        return null;
    }
    
    void put(final RealConnection realConnection) {
        if (!this.cleanupRunning) {
            this.cleanupRunning = true;
            ConnectionPool.executor.execute(this.cleanupRunnable);
        }
        this.connections.add(realConnection);
    }
}
