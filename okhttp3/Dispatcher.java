package okhttp3;

import javax.annotation.*;
import java.util.*;
import okhttp3.internal.*;
import java.util.concurrent.*;

public final class Dispatcher
{
    @Nullable
    private ExecutorService executorService;
    @Nullable
    private Runnable idleCallback;
    private int maxRequests;
    private int maxRequestsPerHost;
    private final Deque<RealCall.AsyncCall> readyAsyncCalls;
    private final Deque<RealCall.AsyncCall> runningAsyncCalls;
    private final Deque<RealCall> runningSyncCalls;
    
    public Dispatcher() {
        this.maxRequests = 64;
        this.maxRequestsPerHost = 5;
        this.readyAsyncCalls = new ArrayDeque<RealCall.AsyncCall>();
        this.runningAsyncCalls = new ArrayDeque<RealCall.AsyncCall>();
        this.runningSyncCalls = new ArrayDeque<RealCall>();
    }
    
    private <T> void finished(final Deque<T> deque, final T t, final boolean b) {
        synchronized (this) {
            if (deque.remove(t)) {
                if (b) {
                    this.promoteCalls();
                }
                final int runningCallsCount = this.runningCallsCount();
                final Runnable idleCallback = this.idleCallback;
                // monitorexit(this)
                if (runningCallsCount == 0 && idleCallback != null) {
                    idleCallback.run();
                }
                return;
            }
            throw new AssertionError((Object)"Call wasn't in-flight!");
        }
    }
    
    private void promoteCalls() {
        if (this.runningAsyncCalls.size() >= this.maxRequests) {
            return;
        }
        if (this.readyAsyncCalls.isEmpty()) {
            return;
        }
        final Iterator<RealCall.AsyncCall> iterator = this.readyAsyncCalls.iterator();
        while (iterator.hasNext()) {
            final RealCall.AsyncCall asyncCall = iterator.next();
            if (this.runningCallsForHost(asyncCall) < this.maxRequestsPerHost) {
                iterator.remove();
                this.runningAsyncCalls.add(asyncCall);
                this.executorService().execute(asyncCall);
            }
            if (this.runningAsyncCalls.size() >= this.maxRequests) {
                break;
            }
        }
    }
    
    private int runningCallsForHost(final RealCall.AsyncCall asyncCall) {
        final Iterator<RealCall.AsyncCall> iterator = this.runningAsyncCalls.iterator();
        int n = 0;
        while (iterator.hasNext()) {
            final RealCall.AsyncCall asyncCall2 = iterator.next();
            if (asyncCall2.get().forWebSocket) {
                continue;
            }
            if (!asyncCall2.host().equals(asyncCall.host())) {
                continue;
            }
            ++n;
        }
        return n;
    }
    
    void enqueue(final RealCall.AsyncCall asyncCall) {
        synchronized (this) {
            if (this.runningAsyncCalls.size() < this.maxRequests && this.runningCallsForHost(asyncCall) < this.maxRequestsPerHost) {
                this.runningAsyncCalls.add(asyncCall);
                this.executorService().execute(asyncCall);
            }
            else {
                this.readyAsyncCalls.add(asyncCall);
            }
        }
    }
    
    void executed(final RealCall realCall) {
        synchronized (this) {
            this.runningSyncCalls.add(realCall);
        }
    }
    
    public ExecutorService executorService() {
        synchronized (this) {
            if (this.executorService == null) {
                this.executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), Util.threadFactory("OkHttp Dispatcher", false));
            }
            return this.executorService;
        }
    }
    
    void finished(final RealCall.AsyncCall asyncCall) {
        this.finished(this.runningAsyncCalls, asyncCall, true);
    }
    
    void finished(final RealCall realCall) {
        this.finished(this.runningSyncCalls, realCall, false);
    }
    
    public int runningCallsCount() {
        synchronized (this) {
            return this.runningAsyncCalls.size() + this.runningSyncCalls.size();
        }
    }
}
