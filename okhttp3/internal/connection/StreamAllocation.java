package okhttp3.internal.connection;

import okhttp3.internal.http.*;
import java.net.*;
import okhttp3.internal.*;
import java.io.*;
import okhttp3.*;
import okhttp3.internal.http2.*;
import java.lang.ref.*;
import java.util.*;

public final class StreamAllocation
{
    public final Address address;
    public final Call call;
    private final Object callStackTrace;
    private boolean canceled;
    private HttpCodec codec;
    private RealConnection connection;
    private final ConnectionPool connectionPool;
    public final EventListener eventListener;
    private int refusedStreamCount;
    private boolean released;
    private boolean reportedAcquired;
    private Route route;
    private RouteSelector.Selection routeSelection;
    private final RouteSelector routeSelector;
    
    public StreamAllocation(final ConnectionPool connectionPool, final Address address, final Call call, final EventListener eventListener, final Object callStackTrace) {
        this.connectionPool = connectionPool;
        this.address = address;
        this.call = call;
        this.eventListener = eventListener;
        this.routeSelector = new RouteSelector(address, this.routeDatabase(), call, eventListener);
        this.callStackTrace = callStackTrace;
    }
    
    private Socket deallocate(final boolean b, final boolean b2, final boolean b3) {
        if (b3) {
            this.codec = null;
        }
        if (b2) {
            this.released = true;
        }
        final RealConnection connection = this.connection;
        if (connection != null) {
            if (b) {
                connection.noNewStreams = true;
            }
            if (this.codec == null && (this.released || this.connection.noNewStreams)) {
                this.release(this.connection);
                Socket socket = null;
                Label_0128: {
                    if (this.connection.allocations.isEmpty()) {
                        this.connection.idleAtNanos = System.nanoTime();
                        if (Internal.instance.connectionBecameIdle(this.connectionPool, this.connection)) {
                            socket = this.connection.socket();
                            break Label_0128;
                        }
                    }
                    socket = null;
                }
                this.connection = null;
                return socket;
            }
        }
        return null;
    }
    
    private RealConnection findConnection(final int n, final int n2, final int n3, final int n4, final boolean b) throws IOException {
        Object o;
        Object o2;
        RealConnection connection;
        Route route;
        RealConnection connection2;
        Object o3;
        RealConnection connection3;
        int n5;
        int n6 = 0;
        ConnectionPool connectionPool;
        int n7;
        int size;
        Route route2;
        Object o4;
        Route next;
        Label_0135_Outer:Label_0281_Outer:
        while (true) {
            while (true) {
            Label_0628:
                while (true) {
                    Label_0622: {
                    Label_0619:
                        while (true) {
                            Label_0613: {
                                synchronized (this.connectionPool) {
                                    if (this.released) {
                                        throw new IllegalStateException("released");
                                    }
                                    if (this.codec == null) {
                                        if (!this.canceled) {
                                            o = this.connection;
                                            o2 = this.releaseIfNoNewStreams();
                                            connection = this.connection;
                                            route = null;
                                            if (connection == null) {
                                                break Label_0613;
                                            }
                                            connection2 = this.connection;
                                            o = null;
                                            o3 = o;
                                            if (!this.reportedAcquired) {
                                                o3 = null;
                                            }
                                            if (connection2 != null) {
                                                break Label_0619;
                                            }
                                            Internal.instance.get(this.connectionPool, this.address, this, null);
                                            if (this.connection == null) {
                                                o = this.route;
                                                break Label_0622;
                                            }
                                            connection3 = this.connection;
                                            o = null;
                                            n5 = 1;
                                            // monitorexit(this.connectionPool)
                                            Util.closeQuietly((Socket)o2);
                                            if (o3 != null) {
                                                this.eventListener.connectionReleased(this.call, (Connection)o3);
                                            }
                                            if (n5 != 0) {
                                                this.eventListener.connectionAcquired(this.call, connection3);
                                            }
                                            if (connection3 != null) {
                                                return connection3;
                                            }
                                            Label_0231: {
                                                if (o == null) {
                                                    o3 = this.routeSelection;
                                                    if (o3 == null || !((RouteSelector.Selection)o3).hasNext()) {
                                                        this.routeSelection = this.routeSelector.next();
                                                        n6 = 1;
                                                        break Label_0231;
                                                    }
                                                }
                                                n6 = 0;
                                            }
                                            connectionPool = this.connectionPool;
                                            synchronized (this.connectionPool) {
                                                if (!this.canceled) {
                                                    n7 = n5;
                                                    o3 = connection3;
                                                    if (n6 != 0) {
                                                        o2 = this.routeSelection.getAll();
                                                        size = ((List)o2).size();
                                                        n6 = 0;
                                                        n7 = n5;
                                                        o3 = connection3;
                                                        if (n6 < size) {
                                                            route2 = ((List<Route>)o2).get(n6);
                                                            Internal.instance.get(this.connectionPool, this.address, this, route2);
                                                            if (this.connection == null) {
                                                                break Label_0628;
                                                            }
                                                            o3 = this.connection;
                                                            this.route = route2;
                                                            n7 = 1;
                                                        }
                                                    }
                                                    o4 = o3;
                                                    if (n7 == 0) {
                                                        if ((next = (Route)o) == null) {
                                                            next = this.routeSelection.next();
                                                        }
                                                        this.route = next;
                                                        this.refusedStreamCount = 0;
                                                        o4 = new RealConnection(this.connectionPool, next);
                                                        this.acquire((RealConnection)o4, false);
                                                    }
                                                    // monitorexit(this.connectionPool)
                                                    Label_0421: {
                                                        if (n7 == 0) {
                                                            ((RealConnection)o4).connect(n, n2, n3, n4, b, this.call, this.eventListener);
                                                            this.routeDatabase().connected(((RealConnection)o4).route());
                                                            connectionPool = this.connectionPool;
                                                            synchronized (this.connectionPool) {
                                                                this.reportedAcquired = true;
                                                                Internal.instance.put(this.connectionPool, (RealConnection)o4);
                                                                o3 = route;
                                                                o = o4;
                                                                if (((RealConnection)o4).isMultiplexed()) {
                                                                    o3 = Internal.instance.deduplicate(this.connectionPool, this.address, this);
                                                                    o = this.connection;
                                                                }
                                                                // monitorexit(this.connectionPool)
                                                                Util.closeQuietly((Socket)o3);
                                                                break Label_0421;
                                                            }
                                                            throw new IOException("Canceled");
                                                        }
                                                    }
                                                    this.eventListener.connectionAcquired(this.call, (Connection)o4);
                                                    return (RealConnection)o4;
                                                }
                                                throw new IOException("Canceled");
                                            }
                                        }
                                        throw new IOException("Canceled");
                                    }
                                    throw new IllegalStateException("codec != null");
                                }
                            }
                            connection2 = null;
                            continue Label_0135_Outer;
                        }
                        o = null;
                    }
                    n5 = 0;
                    continue Label_0281_Outer;
                }
                ++n6;
                continue;
            }
        }
    }
    
    private RealConnection findHealthyConnection(final int n, final int n2, final int n3, final int n4, final boolean b, final boolean b2) throws IOException {
        while (true) {
            final RealConnection connection = this.findConnection(n, n2, n3, n4, b);
            synchronized (this.connectionPool) {
                if (connection.successCount == 0) {
                    return connection;
                }
                // monitorexit(this.connectionPool)
                if (connection.isHealthy(b2)) {
                    return connection;
                }
                this.noNewStreams();
            }
        }
    }
    
    private void release(final RealConnection realConnection) {
        for (int size = realConnection.allocations.size(), i = 0; i < size; ++i) {
            if (realConnection.allocations.get(i).get() == this) {
                realConnection.allocations.remove(i);
                return;
            }
        }
        throw new IllegalStateException();
    }
    
    private Socket releaseIfNoNewStreams() {
        final RealConnection connection = this.connection;
        if (connection != null && connection.noNewStreams) {
            return this.deallocate(false, false, true);
        }
        return null;
    }
    
    private RouteDatabase routeDatabase() {
        return Internal.instance.routeDatabase(this.connectionPool);
    }
    
    public void acquire(final RealConnection connection, final boolean reportedAcquired) {
        if (this.connection == null) {
            this.connection = connection;
            this.reportedAcquired = reportedAcquired;
            connection.allocations.add(new StreamAllocationReference(this, this.callStackTrace));
            return;
        }
        throw new IllegalStateException();
    }
    
    public void cancel() {
        synchronized (this.connectionPool) {
            this.canceled = true;
            final HttpCodec codec = this.codec;
            final RealConnection connection = this.connection;
            // monitorexit(this.connectionPool)
            if (codec != null) {
                codec.cancel();
                return;
            }
            if (connection != null) {
                connection.cancel();
            }
        }
    }
    
    public HttpCodec codec() {
        synchronized (this.connectionPool) {
            return this.codec;
        }
    }
    
    public RealConnection connection() {
        synchronized (this) {
            return this.connection;
        }
    }
    
    public boolean hasMoreRoutes() {
        if (this.route == null) {
            final RouteSelector.Selection routeSelection = this.routeSelection;
            if (routeSelection == null || !routeSelection.hasNext()) {
                if (!this.routeSelector.hasNext()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public HttpCodec newStream(final OkHttpClient okHttpClient, final Interceptor.Chain chain, final boolean b) {
        final int connectTimeoutMillis = chain.connectTimeoutMillis();
        final int timeoutMillis = chain.readTimeoutMillis();
        final int writeTimeoutMillis = chain.writeTimeoutMillis();
        final int pingIntervalMillis = okHttpClient.pingIntervalMillis();
        final boolean retryOnConnectionFailure = okHttpClient.retryOnConnectionFailure();
        try {
            final HttpCodec codec = this.findHealthyConnection(connectTimeoutMillis, timeoutMillis, writeTimeoutMillis, pingIntervalMillis, retryOnConnectionFailure, b).newCodec(okHttpClient, chain, this);
            synchronized (this.connectionPool) {
                return this.codec = codec;
            }
        }
        catch (IOException ex) {
            throw new RouteException(ex);
        }
    }
    
    public void noNewStreams() {
        synchronized (this.connectionPool) {
            RealConnection connection = this.connection;
            final Socket deallocate = this.deallocate(true, false, false);
            if (this.connection != null) {
                connection = null;
            }
            // monitorexit(this.connectionPool)
            Util.closeQuietly(deallocate);
            if (connection != null) {
                this.eventListener.connectionReleased(this.call, connection);
            }
        }
    }
    
    public void release() {
        synchronized (this.connectionPool) {
            RealConnection connection = this.connection;
            final Socket deallocate = this.deallocate(false, true, false);
            if (this.connection != null) {
                connection = null;
            }
            // monitorexit(this.connectionPool)
            Util.closeQuietly(deallocate);
            if (connection != null) {
                this.eventListener.connectionReleased(this.call, connection);
            }
        }
    }
    
    public Socket releaseAndAcquire(final RealConnection connection) {
        if (this.codec == null && this.connection.allocations.size() == 1) {
            final Reference<StreamAllocation> reference = this.connection.allocations.get(0);
            final Socket deallocate = this.deallocate(true, false, false);
            this.connection = connection;
            connection.allocations.add(reference);
            return deallocate;
        }
        throw new IllegalStateException();
    }
    
    public Route route() {
        return this.route;
    }
    
    public void streamFailed(final IOException ex) {
    Label_0156_Outer:
        while (true) {
        Label_0125_Outer:
            while (true) {
                while (true) {
                    Label_0190: {
                        Label_0185: {
                            synchronized (this.connectionPool) {
                                if (ex instanceof StreamResetException) {
                                    final StreamResetException ex2 = (StreamResetException)ex;
                                    if (ex2.errorCode == ErrorCode.REFUSED_STREAM) {
                                        ++this.refusedStreamCount;
                                    }
                                    if (ex2.errorCode == ErrorCode.REFUSED_STREAM && this.refusedStreamCount <= 1) {
                                        break Label_0190;
                                    }
                                }
                                else {
                                    if (this.connection == null || (this.connection.isMultiplexed() && !(ex instanceof ConnectionShutdownException))) {
                                        break Label_0190;
                                    }
                                    if (this.connection.successCount != 0) {
                                        break Label_0185;
                                    }
                                    if (this.route != null && ex != null) {
                                        this.routeSelector.connectFailed(this.route, ex);
                                    }
                                }
                                this.route = null;
                                break Label_0185;
                                // monitorexit(this.connectionPool)
                                // iftrue(Label_0179:, connection == null)
                                while (true) {
                                    final RealConnection connection;
                                    this.eventListener.connectionReleased(this.call, connection);
                                    return;
                                    final Socket deallocate;
                                    Util.closeQuietly(deallocate);
                                    continue Label_0156_Outer;
                                }
                                Label_0179: {
                                    return;
                                }
                                final RealConnection connection = this.connection;
                                final boolean b;
                                final Socket deallocate = this.deallocate(b, false, true);
                                // iftrue(Label_0195:, this.connection != null || !this.reportedAcquired)
                                continue Label_0125_Outer;
                            }
                        }
                        final boolean b = true;
                        continue;
                    }
                    final boolean b = false;
                    continue;
                }
                Label_0195: {
                    final RealConnection connection = null;
                }
                continue Label_0125_Outer;
            }
        }
    }
    
    public void streamFinished(final boolean b, final HttpCodec httpCodec, final long n, final IOException ex) {
        this.eventListener.responseBodyEnd(this.call, n);
        final ConnectionPool connectionPool = this.connectionPool;
        // monitorenter(connectionPool)
        Label_0143: {
            if (httpCodec == null) {
                break Label_0143;
            }
            try {
                if (httpCodec != this.codec) {
                    break Label_0143;
                }
                if (!b) {
                    final RealConnection connection = this.connection;
                    ++connection.successCount;
                }
                RealConnection connection2 = this.connection;
                final Socket deallocate = this.deallocate(b, false, true);
                if (this.connection != null) {
                    connection2 = null;
                }
                final boolean released = this.released;
                // monitorexit(connectionPool)
                Util.closeQuietly(deallocate);
                if (connection2 != null) {
                    this.eventListener.connectionReleased(this.call, connection2);
                }
                if (ex != null) {
                    this.eventListener.callFailed(this.call, ex);
                    return;
                }
                if (released) {
                    this.eventListener.callEnd(this.call);
                }
            }
            finally {
                // monitorexit(connectionPool)
                final StringBuilder sb = new StringBuilder();
                sb.append("expected ");
                sb.append(this.codec);
                sb.append(" but was ");
                sb.append(httpCodec);
                throw new IllegalStateException(sb.toString());
            }
        }
    }
    
    @Override
    public String toString() {
        final RealConnection connection = this.connection();
        if (connection != null) {
            return connection.toString();
        }
        return this.address.toString();
    }
    
    public static final class StreamAllocationReference extends WeakReference<StreamAllocation>
    {
        public final Object callStackTrace;
        
        StreamAllocationReference(final StreamAllocation streamAllocation, final Object callStackTrace) {
            super(streamAllocation);
            this.callStackTrace = callStackTrace;
        }
    }
}
