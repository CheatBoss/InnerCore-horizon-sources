package com.bumptech.glide.load.engine;

import com.bumptech.glide.request.*;
import java.util.concurrent.*;
import com.bumptech.glide.load.*;
import java.util.*;
import com.bumptech.glide.util.*;
import android.os.*;

class EngineJob implements EngineRunnableManager
{
    private static final EngineResourceFactory DEFAULT_FACTORY;
    private static final Handler MAIN_THREAD_HANDLER;
    private static final int MSG_COMPLETE = 1;
    private static final int MSG_EXCEPTION = 2;
    private final List<ResourceCallback> cbs;
    private final ExecutorService diskCacheService;
    private EngineResource<?> engineResource;
    private final EngineResourceFactory engineResourceFactory;
    private EngineRunnable engineRunnable;
    private Exception exception;
    private volatile Future<?> future;
    private boolean hasException;
    private boolean hasResource;
    private Set<ResourceCallback> ignoredCallbacks;
    private final boolean isCacheable;
    private boolean isCancelled;
    private final Key key;
    private final EngineJobListener listener;
    private Resource<?> resource;
    private final ExecutorService sourceService;
    
    static {
        DEFAULT_FACTORY = new EngineResourceFactory();
        MAIN_THREAD_HANDLER = new Handler(Looper.getMainLooper(), (Handler$Callback)new MainThreadCallback());
    }
    
    public EngineJob(final Key key, final ExecutorService executorService, final ExecutorService executorService2, final boolean b, final EngineJobListener engineJobListener) {
        this(key, executorService, executorService2, b, engineJobListener, EngineJob.DEFAULT_FACTORY);
    }
    
    public EngineJob(final Key key, final ExecutorService diskCacheService, final ExecutorService sourceService, final boolean isCacheable, final EngineJobListener listener, final EngineResourceFactory engineResourceFactory) {
        this.cbs = new ArrayList<ResourceCallback>();
        this.key = key;
        this.diskCacheService = diskCacheService;
        this.sourceService = sourceService;
        this.isCacheable = isCacheable;
        this.listener = listener;
        this.engineResourceFactory = engineResourceFactory;
    }
    
    private void addIgnoredCallback(final ResourceCallback resourceCallback) {
        if (this.ignoredCallbacks == null) {
            this.ignoredCallbacks = new HashSet<ResourceCallback>();
        }
        this.ignoredCallbacks.add(resourceCallback);
    }
    
    private void handleExceptionOnMainThread() {
        if (this.isCancelled) {
            return;
        }
        if (this.cbs.isEmpty()) {
            throw new IllegalStateException("Received an exception without any callbacks to notify");
        }
        this.hasException = true;
        this.listener.onEngineJobComplete(this.key, null);
        for (final ResourceCallback resourceCallback : this.cbs) {
            if (!this.isInIgnoredCallbacks(resourceCallback)) {
                resourceCallback.onException(this.exception);
            }
        }
    }
    
    private void handleResultOnMainThread() {
        if (this.isCancelled) {
            this.resource.recycle();
            return;
        }
        if (this.cbs.isEmpty()) {
            throw new IllegalStateException("Received a resource without any callbacks to notify");
        }
        this.engineResource = this.engineResourceFactory.build(this.resource, this.isCacheable);
        this.hasResource = true;
        this.engineResource.acquire();
        this.listener.onEngineJobComplete(this.key, this.engineResource);
        for (final ResourceCallback resourceCallback : this.cbs) {
            if (!this.isInIgnoredCallbacks(resourceCallback)) {
                this.engineResource.acquire();
                resourceCallback.onResourceReady(this.engineResource);
            }
        }
        this.engineResource.release();
    }
    
    private boolean isInIgnoredCallbacks(final ResourceCallback resourceCallback) {
        return this.ignoredCallbacks != null && this.ignoredCallbacks.contains(resourceCallback);
    }
    
    public void addCallback(final ResourceCallback resourceCallback) {
        Util.assertMainThread();
        if (this.hasResource) {
            resourceCallback.onResourceReady(this.engineResource);
            return;
        }
        if (this.hasException) {
            resourceCallback.onException(this.exception);
            return;
        }
        this.cbs.add(resourceCallback);
    }
    
    void cancel() {
        if (this.hasException || this.hasResource) {
            return;
        }
        if (this.isCancelled) {
            return;
        }
        this.engineRunnable.cancel();
        final Future<?> future = this.future;
        if (future != null) {
            future.cancel(true);
        }
        this.isCancelled = true;
        this.listener.onEngineJobCancelled(this, this.key);
    }
    
    boolean isCancelled() {
        return this.isCancelled;
    }
    
    @Override
    public void onException(final Exception exception) {
        this.exception = exception;
        EngineJob.MAIN_THREAD_HANDLER.obtainMessage(2, (Object)this).sendToTarget();
    }
    
    @Override
    public void onResourceReady(final Resource<?> resource) {
        this.resource = resource;
        EngineJob.MAIN_THREAD_HANDLER.obtainMessage(1, (Object)this).sendToTarget();
    }
    
    public void removeCallback(final ResourceCallback resourceCallback) {
        Util.assertMainThread();
        if (!this.hasResource && !this.hasException) {
            this.cbs.remove(resourceCallback);
            if (this.cbs.isEmpty()) {
                this.cancel();
            }
        }
        else {
            this.addIgnoredCallback(resourceCallback);
        }
    }
    
    public void start(final EngineRunnable engineRunnable) {
        this.engineRunnable = engineRunnable;
        this.future = this.diskCacheService.submit(engineRunnable);
    }
    
    @Override
    public void submitForSource(final EngineRunnable engineRunnable) {
        this.future = this.sourceService.submit(engineRunnable);
    }
    
    static class EngineResourceFactory
    {
        public <R> EngineResource<R> build(final Resource<R> resource, final boolean b) {
            return new EngineResource<R>(resource, b);
        }
    }
    
    private static class MainThreadCallback implements Handler$Callback
    {
        public boolean handleMessage(final Message message) {
            if (1 != message.what && 2 != message.what) {
                return false;
            }
            final EngineJob engineJob = (EngineJob)message.obj;
            if (1 == message.what) {
                engineJob.handleResultOnMainThread();
                return true;
            }
            engineJob.handleExceptionOnMainThread();
            return true;
        }
    }
}
