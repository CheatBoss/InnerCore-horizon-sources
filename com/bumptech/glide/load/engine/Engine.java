package com.bumptech.glide.load.engine;

import java.lang.ref.*;
import java.util.concurrent.*;
import java.util.*;
import android.os.*;
import android.util.*;
import com.bumptech.glide.load.data.*;
import com.bumptech.glide.provider.*;
import com.bumptech.glide.load.resource.transcode.*;
import com.bumptech.glide.*;
import com.bumptech.glide.request.*;
import com.bumptech.glide.util.*;
import com.bumptech.glide.load.*;
import com.bumptech.glide.load.engine.cache.*;

public class Engine implements EngineJobListener, ResourceRemovedListener, ResourceListener
{
    private static final String TAG = "Engine";
    private final Map<Key, WeakReference<EngineResource<?>>> activeResources;
    private final MemoryCache cache;
    private final LazyDiskCacheProvider diskCacheProvider;
    private final EngineJobFactory engineJobFactory;
    private final Map<Key, EngineJob> jobs;
    private final EngineKeyFactory keyFactory;
    private final ResourceRecycler resourceRecycler;
    private ReferenceQueue<EngineResource<?>> resourceReferenceQueue;
    
    public Engine(final MemoryCache memoryCache, final DiskCache.Factory factory, final ExecutorService executorService, final ExecutorService executorService2) {
        this(memoryCache, factory, executorService, executorService2, null, null, null, null, null);
    }
    
    Engine(final MemoryCache cache, final DiskCache.Factory factory, final ExecutorService executorService, final ExecutorService executorService2, final Map<Key, EngineJob> map, final EngineKeyFactory engineKeyFactory, final Map<Key, WeakReference<EngineResource<?>>> map2, final EngineJobFactory engineJobFactory, final ResourceRecycler resourceRecycler) {
        this.cache = cache;
        this.diskCacheProvider = new LazyDiskCacheProvider(factory);
        Map<Key, WeakReference<EngineResource<?>>> activeResources = map2;
        if (map2 == null) {
            activeResources = new HashMap<Key, WeakReference<EngineResource<?>>>();
        }
        this.activeResources = activeResources;
        EngineKeyFactory keyFactory;
        if ((keyFactory = engineKeyFactory) == null) {
            keyFactory = new EngineKeyFactory();
        }
        this.keyFactory = keyFactory;
        Map<Key, EngineJob> jobs;
        if ((jobs = map) == null) {
            jobs = new HashMap<Key, EngineJob>();
        }
        this.jobs = jobs;
        EngineJobFactory engineJobFactory2;
        if ((engineJobFactory2 = engineJobFactory) == null) {
            engineJobFactory2 = new EngineJobFactory(executorService, executorService2, this);
        }
        this.engineJobFactory = engineJobFactory2;
        ResourceRecycler resourceRecycler2;
        if ((resourceRecycler2 = resourceRecycler) == null) {
            resourceRecycler2 = new ResourceRecycler();
        }
        this.resourceRecycler = resourceRecycler2;
        cache.setResourceRemovedListener((MemoryCache.ResourceRemovedListener)this);
    }
    
    private EngineResource<?> getEngineResourceFromCache(final Key key) {
        final Resource<?> remove = this.cache.remove(key);
        if (remove == null) {
            return null;
        }
        if (remove instanceof EngineResource) {
            return (EngineResource<?>)remove;
        }
        return new EngineResource<Object>((Resource<Object>)remove, true);
    }
    
    private ReferenceQueue<EngineResource<?>> getReferenceQueue() {
        if (this.resourceReferenceQueue == null) {
            this.resourceReferenceQueue = new ReferenceQueue<EngineResource<?>>();
            Looper.myQueue().addIdleHandler((MessageQueue$IdleHandler)new RefQueueIdleHandler(this.activeResources, this.resourceReferenceQueue));
        }
        return this.resourceReferenceQueue;
    }
    
    private EngineResource<?> loadFromActiveResources(final Key key, final boolean b) {
        if (!b) {
            return null;
        }
        EngineResource<?> engineResource = null;
        final WeakReference<EngineResource<?>> weakReference = this.activeResources.get(key);
        if (weakReference != null) {
            engineResource = weakReference.get();
            if (engineResource != null) {
                engineResource.acquire();
                return engineResource;
            }
            this.activeResources.remove(key);
        }
        return engineResource;
    }
    
    private EngineResource<?> loadFromCache(final Key key, final boolean b) {
        if (!b) {
            return null;
        }
        final EngineResource<?> engineResourceFromCache = this.getEngineResourceFromCache(key);
        if (engineResourceFromCache != null) {
            engineResourceFromCache.acquire();
            this.activeResources.put(key, new ResourceWeakReference(key, engineResourceFromCache, this.getReferenceQueue()));
        }
        return engineResourceFromCache;
    }
    
    private static void logWithTimeAndKey(final String s, final long n, final Key key) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(" in ");
        sb.append(LogTime.getElapsedMillis(n));
        sb.append("ms, key: ");
        sb.append(key);
        Log.v("Engine", sb.toString());
    }
    
    public void clearDiskCache() {
        this.diskCacheProvider.getDiskCache().clear();
    }
    
    public <T, Z, R> LoadStatus load(final Key key, final int n, final int n2, final DataFetcher<T> dataFetcher, final DataLoadProvider<T, Z> dataLoadProvider, final Transformation<Z> transformation, final ResourceTranscoder<Z, R> resourceTranscoder, final Priority priority, final boolean b, final DiskCacheStrategy diskCacheStrategy, final ResourceCallback resourceCallback) {
        Util.assertMainThread();
        final long logTime = LogTime.getLogTime();
        final EngineKey buildKey = this.keyFactory.buildKey(dataFetcher.getId(), key, n, n2, dataLoadProvider.getCacheDecoder(), dataLoadProvider.getSourceDecoder(), transformation, dataLoadProvider.getEncoder(), resourceTranscoder, dataLoadProvider.getSourceEncoder());
        final EngineResource<?> loadFromCache = this.loadFromCache(buildKey, b);
        if (loadFromCache != null) {
            resourceCallback.onResourceReady(loadFromCache);
            if (Log.isLoggable("Engine", 2)) {
                logWithTimeAndKey("Loaded resource from cache", logTime, buildKey);
            }
            return null;
        }
        final EngineResource<?> loadFromActiveResources = this.loadFromActiveResources(buildKey, b);
        if (loadFromActiveResources != null) {
            resourceCallback.onResourceReady(loadFromActiveResources);
            if (Log.isLoggable("Engine", 2)) {
                logWithTimeAndKey("Loaded resource from active resources", logTime, buildKey);
            }
            return null;
        }
        final EngineJob engineJob = this.jobs.get(buildKey);
        if (engineJob != null) {
            engineJob.addCallback(resourceCallback);
            if (Log.isLoggable("Engine", 2)) {
                logWithTimeAndKey("Added to existing load", logTime, buildKey);
            }
            return new LoadStatus(resourceCallback, engineJob);
        }
        final EngineJob build = this.engineJobFactory.build(buildKey, b);
        final EngineRunnable engineRunnable = new EngineRunnable((EngineRunnable.EngineRunnableManager)build, new DecodeJob<Object, Object, Object>(buildKey, n, n2, dataFetcher, dataLoadProvider, transformation, resourceTranscoder, (DecodeJob.DiskCacheProvider)this.diskCacheProvider, diskCacheStrategy, priority), priority);
        this.jobs.put(buildKey, build);
        build.addCallback(resourceCallback);
        build.start(engineRunnable);
        if (Log.isLoggable("Engine", 2)) {
            logWithTimeAndKey("Started new load", logTime, buildKey);
        }
        return new LoadStatus(resourceCallback, build);
    }
    
    @Override
    public void onEngineJobCancelled(final EngineJob engineJob, final Key key) {
        Util.assertMainThread();
        if (engineJob.equals(this.jobs.get(key))) {
            this.jobs.remove(key);
        }
    }
    
    @Override
    public void onEngineJobComplete(final Key key, final EngineResource<?> engineResource) {
        Util.assertMainThread();
        if (engineResource != null) {
            engineResource.setResourceListener(key, (EngineResource.ResourceListener)this);
            if (engineResource.isCacheable()) {
                this.activeResources.put(key, new ResourceWeakReference(key, engineResource, this.getReferenceQueue()));
            }
        }
        this.jobs.remove(key);
    }
    
    @Override
    public void onResourceReleased(final Key key, final EngineResource engineResource) {
        Util.assertMainThread();
        this.activeResources.remove(key);
        if (engineResource.isCacheable()) {
            this.cache.put(key, engineResource);
            return;
        }
        this.resourceRecycler.recycle(engineResource);
    }
    
    @Override
    public void onResourceRemoved(final Resource<?> resource) {
        Util.assertMainThread();
        this.resourceRecycler.recycle(resource);
    }
    
    public void release(final Resource resource) {
        Util.assertMainThread();
        if (resource instanceof EngineResource) {
            ((EngineResource)resource).release();
            return;
        }
        throw new IllegalArgumentException("Cannot release anything but an EngineResource");
    }
    
    static class EngineJobFactory
    {
        private final ExecutorService diskCacheService;
        private final EngineJobListener listener;
        private final ExecutorService sourceService;
        
        public EngineJobFactory(final ExecutorService diskCacheService, final ExecutorService sourceService, final EngineJobListener listener) {
            this.diskCacheService = diskCacheService;
            this.sourceService = sourceService;
            this.listener = listener;
        }
        
        public EngineJob build(final Key key, final boolean b) {
            return new EngineJob(key, this.diskCacheService, this.sourceService, b, this.listener);
        }
    }
    
    private static class LazyDiskCacheProvider implements DiskCacheProvider
    {
        private volatile DiskCache diskCache;
        private final DiskCache.Factory factory;
        
        public LazyDiskCacheProvider(final DiskCache.Factory factory) {
            this.factory = factory;
        }
        
        @Override
        public DiskCache getDiskCache() {
            if (this.diskCache == null) {
                synchronized (this) {
                    if (this.diskCache == null) {
                        this.diskCache = this.factory.build();
                    }
                    if (this.diskCache == null) {
                        this.diskCache = new DiskCacheAdapter();
                    }
                }
            }
            return this.diskCache;
        }
    }
    
    public static class LoadStatus
    {
        private final ResourceCallback cb;
        private final EngineJob engineJob;
        
        public LoadStatus(final ResourceCallback cb, final EngineJob engineJob) {
            this.cb = cb;
            this.engineJob = engineJob;
        }
        
        public void cancel() {
            this.engineJob.removeCallback(this.cb);
        }
    }
    
    private static class RefQueueIdleHandler implements MessageQueue$IdleHandler
    {
        private final Map<Key, WeakReference<EngineResource<?>>> activeResources;
        private final ReferenceQueue<EngineResource<?>> queue;
        
        public RefQueueIdleHandler(final Map<Key, WeakReference<EngineResource<?>>> activeResources, final ReferenceQueue<EngineResource<?>> queue) {
            this.activeResources = activeResources;
            this.queue = queue;
        }
        
        public boolean queueIdle() {
            final ResourceWeakReference resourceWeakReference = (ResourceWeakReference)this.queue.poll();
            if (resourceWeakReference != null) {
                this.activeResources.remove(resourceWeakReference.key);
            }
            return true;
        }
    }
    
    private static class ResourceWeakReference extends WeakReference<EngineResource<?>>
    {
        private final Key key;
        
        public ResourceWeakReference(final Key key, final EngineResource<?> engineResource, final ReferenceQueue<? super EngineResource<?>> referenceQueue) {
            super(engineResource, referenceQueue);
            this.key = key;
        }
    }
}
