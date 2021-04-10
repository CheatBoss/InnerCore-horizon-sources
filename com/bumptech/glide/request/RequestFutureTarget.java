package com.bumptech.glide.request;

import android.os.*;
import com.bumptech.glide.util.*;
import java.util.concurrent.*;
import com.bumptech.glide.request.target.*;
import android.graphics.drawable.*;
import com.bumptech.glide.request.animation.*;

public class RequestFutureTarget<T, R> implements FutureTarget<R>, Runnable
{
    private static final Waiter DEFAULT_WAITER;
    private final boolean assertBackgroundThread;
    private Exception exception;
    private boolean exceptionReceived;
    private final int height;
    private boolean isCancelled;
    private final Handler mainHandler;
    private Request request;
    private R resource;
    private boolean resultReceived;
    private final Waiter waiter;
    private final int width;
    
    static {
        DEFAULT_WAITER = new Waiter();
    }
    
    public RequestFutureTarget(final Handler handler, final int n, final int n2) {
        this(handler, n, n2, true, RequestFutureTarget.DEFAULT_WAITER);
    }
    
    RequestFutureTarget(final Handler mainHandler, final int width, final int height, final boolean assertBackgroundThread, final Waiter waiter) {
        this.mainHandler = mainHandler;
        this.width = width;
        this.height = height;
        this.assertBackgroundThread = assertBackgroundThread;
        this.waiter = waiter;
    }
    
    private R doGet(final Long n) throws ExecutionException, InterruptedException, TimeoutException {
        synchronized (this) {
            if (this.assertBackgroundThread) {
                Util.assertBackgroundThread();
            }
            if (this.isCancelled) {
                throw new CancellationException();
            }
            if (this.exceptionReceived) {
                throw new ExecutionException(this.exception);
            }
            if (this.resultReceived) {
                return this.resource;
            }
            if (n == null) {
                this.waiter.waitForTimeout(this, 0L);
            }
            else if (n > 0L) {
                this.waiter.waitForTimeout(this, n);
            }
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            if (this.exceptionReceived) {
                throw new ExecutionException(this.exception);
            }
            if (this.isCancelled) {
                throw new CancellationException();
            }
            if (!this.resultReceived) {
                throw new TimeoutException();
            }
            return this.resource;
        }
    }
    
    @Override
    public boolean cancel(final boolean b) {
        synchronized (this) {
            if (this.isCancelled) {
                return true;
            }
            final boolean b2 = this.isDone() ^ true;
            if (b2) {
                this.isCancelled = true;
                if (b) {
                    this.clear();
                }
                this.waiter.notifyAll(this);
            }
            return b2;
        }
    }
    
    @Override
    public void clear() {
        this.mainHandler.post((Runnable)this);
    }
    
    @Override
    public R get() throws InterruptedException, ExecutionException {
        try {
            return this.doGet(null);
        }
        catch (TimeoutException ex) {
            throw new AssertionError((Object)ex);
        }
    }
    
    @Override
    public R get(final long n, final TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.doGet(timeUnit.toMillis(n));
    }
    
    @Override
    public Request getRequest() {
        return this.request;
    }
    
    @Override
    public void getSize(final SizeReadyCallback sizeReadyCallback) {
        sizeReadyCallback.onSizeReady(this.width, this.height);
    }
    
    @Override
    public boolean isCancelled() {
        synchronized (this) {
            return this.isCancelled;
        }
    }
    
    @Override
    public boolean isDone() {
        synchronized (this) {
            return this.isCancelled || this.resultReceived;
        }
    }
    
    @Override
    public void onDestroy() {
    }
    
    @Override
    public void onLoadCleared(final Drawable drawable) {
    }
    
    @Override
    public void onLoadFailed(final Exception exception, final Drawable drawable) {
        synchronized (this) {
            this.exceptionReceived = true;
            this.exception = exception;
            this.waiter.notifyAll(this);
        }
    }
    
    @Override
    public void onLoadStarted(final Drawable drawable) {
    }
    
    @Override
    public void onResourceReady(final R resource, final GlideAnimation<? super R> glideAnimation) {
        synchronized (this) {
            this.resultReceived = true;
            this.resource = resource;
            this.waiter.notifyAll(this);
        }
    }
    
    @Override
    public void onStart() {
    }
    
    @Override
    public void onStop() {
    }
    
    @Override
    public void run() {
        if (this.request != null) {
            this.request.clear();
            this.cancel(false);
        }
    }
    
    @Override
    public void setRequest(final Request request) {
        this.request = request;
    }
    
    static class Waiter
    {
        public void notifyAll(final Object o) {
            o.notifyAll();
        }
        
        public void waitForTimeout(final Object o, final long n) throws InterruptedException {
            o.wait(n);
        }
    }
}
