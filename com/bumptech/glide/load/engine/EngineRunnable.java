package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.engine.executor.*;
import com.bumptech.glide.*;
import android.util.*;
import com.bumptech.glide.request.*;

class EngineRunnable implements Runnable, Prioritized
{
    private static final String TAG = "EngineRunnable";
    private final DecodeJob<?, ?, ?> decodeJob;
    private volatile boolean isCancelled;
    private final EngineRunnableManager manager;
    private final Priority priority;
    private Stage stage;
    
    public EngineRunnable(final EngineRunnableManager manager, final DecodeJob<?, ?, ?> decodeJob, final Priority priority) {
        this.manager = manager;
        this.decodeJob = decodeJob;
        this.stage = Stage.CACHE;
        this.priority = priority;
    }
    
    private Resource<?> decode() throws Exception {
        if (this.isDecodingFromCache()) {
            return this.decodeFromCache();
        }
        return this.decodeFromSource();
    }
    
    private Resource<?> decodeFromCache() throws Exception {
        final Resource<?> resource = null;
        Resource<?> decodeResultFromCache;
        try {
            decodeResultFromCache = this.decodeJob.decodeResultFromCache();
        }
        catch (Exception ex) {
            decodeResultFromCache = resource;
            if (Log.isLoggable("EngineRunnable", 3)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Exception decoding result from cache: ");
                sb.append(ex);
                Log.d("EngineRunnable", sb.toString());
                decodeResultFromCache = resource;
            }
        }
        Resource<?> decodeSourceFromCache;
        if ((decodeSourceFromCache = decodeResultFromCache) == null) {
            decodeSourceFromCache = this.decodeJob.decodeSourceFromCache();
        }
        return decodeSourceFromCache;
    }
    
    private Resource<?> decodeFromSource() throws Exception {
        return this.decodeJob.decodeFromSource();
    }
    
    private boolean isDecodingFromCache() {
        return this.stage == Stage.CACHE;
    }
    
    private void onLoadComplete(final Resource resource) {
        this.manager.onResourceReady(resource);
    }
    
    private void onLoadFailed(final Exception ex) {
        if (this.isDecodingFromCache()) {
            this.stage = Stage.SOURCE;
            this.manager.submitForSource(this);
            return;
        }
        this.manager.onException(ex);
    }
    
    public void cancel() {
        this.isCancelled = true;
        this.decodeJob.cancel();
    }
    
    @Override
    public int getPriority() {
        return this.priority.ordinal();
    }
    
    @Override
    public void run() {
        if (this.isCancelled) {
            return;
        }
        Exception ex = null;
        Resource<?> decode = null;
        try {
            decode = this.decode();
        }
        catch (Exception ex2) {
            if (Log.isLoggable("EngineRunnable", 2)) {
                Log.v("EngineRunnable", "Exception decoding", (Throwable)ex2);
            }
            ex = ex2;
        }
        if (this.isCancelled) {
            if (decode != null) {
                decode.recycle();
            }
            return;
        }
        if (decode == null) {
            this.onLoadFailed(ex);
            return;
        }
        this.onLoadComplete(decode);
    }
    
    interface EngineRunnableManager extends ResourceCallback
    {
        void submitForSource(final EngineRunnable p0);
    }
    
    private enum Stage
    {
        CACHE, 
        SOURCE;
    }
}
