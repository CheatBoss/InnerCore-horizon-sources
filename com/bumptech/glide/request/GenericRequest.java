package com.bumptech.glide.request;

import java.util.*;
import android.content.*;
import android.graphics.drawable.*;
import com.bumptech.glide.*;
import com.bumptech.glide.load.engine.*;
import com.bumptech.glide.request.target.*;
import com.bumptech.glide.load.*;
import android.util.*;
import com.bumptech.glide.request.animation.*;
import com.bumptech.glide.util.*;
import java.io.*;
import com.bumptech.glide.provider.*;
import com.bumptech.glide.load.data.*;
import com.bumptech.glide.load.resource.transcode.*;

public final class GenericRequest<A, T, Z, R> implements Request, SizeReadyCallback, ResourceCallback
{
    private static final Queue<GenericRequest<?, ?, ?, ?>> REQUEST_POOL;
    private static final String TAG = "GenericRequest";
    private static final double TO_MEGABYTE = 9.5367431640625E-7;
    private GlideAnimationFactory<R> animationFactory;
    private Context context;
    private DiskCacheStrategy diskCacheStrategy;
    private Engine engine;
    private Drawable errorDrawable;
    private int errorResourceId;
    private Drawable fallbackDrawable;
    private int fallbackResourceId;
    private boolean isMemoryCacheable;
    private LoadProvider<A, T, Z, R> loadProvider;
    private Engine.LoadStatus loadStatus;
    private boolean loadedFromMemoryCache;
    private A model;
    private int overrideHeight;
    private int overrideWidth;
    private Drawable placeholderDrawable;
    private int placeholderResourceId;
    private Priority priority;
    private RequestCoordinator requestCoordinator;
    private RequestListener<? super A, R> requestListener;
    private Resource<?> resource;
    private Key signature;
    private float sizeMultiplier;
    private long startTime;
    private Status status;
    private final String tag;
    private Target<R> target;
    private Class<R> transcodeClass;
    private Transformation<Z> transformation;
    
    static {
        REQUEST_POOL = Util.createQueue(0);
    }
    
    private GenericRequest() {
        this.tag = String.valueOf(this.hashCode());
    }
    
    private boolean canNotifyStatusChanged() {
        return this.requestCoordinator == null || this.requestCoordinator.canNotifyStatusChanged(this);
    }
    
    private boolean canSetResource() {
        return this.requestCoordinator == null || this.requestCoordinator.canSetImage(this);
    }
    
    private static void check(final String s, final Object o, final String s2) {
        if (o == null) {
            final StringBuilder sb = new StringBuilder(s);
            sb.append(" must not be null");
            if (s2 != null) {
                sb.append(", ");
                sb.append(s2);
            }
            throw new NullPointerException(sb.toString());
        }
    }
    
    private Drawable getErrorDrawable() {
        if (this.errorDrawable == null && this.errorResourceId > 0) {
            this.errorDrawable = this.context.getResources().getDrawable(this.errorResourceId);
        }
        return this.errorDrawable;
    }
    
    private Drawable getFallbackDrawable() {
        if (this.fallbackDrawable == null && this.fallbackResourceId > 0) {
            this.fallbackDrawable = this.context.getResources().getDrawable(this.fallbackResourceId);
        }
        return this.fallbackDrawable;
    }
    
    private Drawable getPlaceholderDrawable() {
        if (this.placeholderDrawable == null && this.placeholderResourceId > 0) {
            this.placeholderDrawable = this.context.getResources().getDrawable(this.placeholderResourceId);
        }
        return this.placeholderDrawable;
    }
    
    private void init(final LoadProvider<A, T, Z, R> loadProvider, final A model, final Key signature, final Context context, final Priority priority, final Target<R> target, final float sizeMultiplier, final Drawable placeholderDrawable, final int placeholderResourceId, final Drawable errorDrawable, final int errorResourceId, final Drawable fallbackDrawable, final int fallbackResourceId, final RequestListener<? super A, R> requestListener, final RequestCoordinator requestCoordinator, final Engine engine, final Transformation<Z> transformation, final Class<R> transcodeClass, final boolean isMemoryCacheable, final GlideAnimationFactory<R> animationFactory, final int overrideWidth, final int overrideHeight, final DiskCacheStrategy diskCacheStrategy) {
        this.loadProvider = loadProvider;
        this.model = model;
        this.signature = signature;
        this.fallbackDrawable = fallbackDrawable;
        this.fallbackResourceId = fallbackResourceId;
        this.context = context.getApplicationContext();
        this.priority = priority;
        this.target = target;
        this.sizeMultiplier = sizeMultiplier;
        this.placeholderDrawable = placeholderDrawable;
        this.placeholderResourceId = placeholderResourceId;
        this.errorDrawable = errorDrawable;
        this.errorResourceId = errorResourceId;
        this.requestListener = requestListener;
        this.requestCoordinator = requestCoordinator;
        this.engine = engine;
        this.transformation = transformation;
        this.transcodeClass = transcodeClass;
        this.isMemoryCacheable = isMemoryCacheable;
        this.animationFactory = animationFactory;
        this.overrideWidth = overrideWidth;
        this.overrideHeight = overrideHeight;
        this.diskCacheStrategy = diskCacheStrategy;
        this.status = Status.PENDING;
        if (model != null) {
            check("ModelLoader", loadProvider.getModelLoader(), "try .using(ModelLoader)");
            check("Transcoder", loadProvider.getTranscoder(), "try .as*(Class).transcode(ResourceTranscoder)");
            check("Transformation", transformation, "try .transform(UnitTransformation.get())");
            if (diskCacheStrategy.cacheSource()) {
                check("SourceEncoder", loadProvider.getSourceEncoder(), "try .sourceEncoder(Encoder) or .diskCacheStrategy(NONE/RESULT)");
            }
            else {
                check("SourceDecoder", loadProvider.getSourceDecoder(), "try .decoder/.imageDecoder/.videoDecoder(ResourceDecoder) or .diskCacheStrategy(ALL/SOURCE)");
            }
            if (diskCacheStrategy.cacheSource() || diskCacheStrategy.cacheResult()) {
                check("CacheDecoder", loadProvider.getCacheDecoder(), "try .cacheDecoder(ResouceDecoder) or .diskCacheStrategy(NONE)");
            }
            if (diskCacheStrategy.cacheResult()) {
                check("Encoder", loadProvider.getEncoder(), "try .encode(ResourceEncoder) or .diskCacheStrategy(NONE/SOURCE)");
            }
        }
    }
    
    private boolean isFirstReadyResource() {
        return this.requestCoordinator == null || !this.requestCoordinator.isAnyResourceSet();
    }
    
    private void logV(final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(" this: ");
        sb.append(this.tag);
        Log.v("GenericRequest", sb.toString());
    }
    
    private void notifyLoadSuccess() {
        if (this.requestCoordinator != null) {
            this.requestCoordinator.onRequestSuccess(this);
        }
    }
    
    public static <A, T, Z, R> GenericRequest<A, T, Z, R> obtain(final LoadProvider<A, T, Z, R> loadProvider, final A a, final Key key, final Context context, final Priority priority, final Target<R> target, final float n, final Drawable drawable, final int n2, final Drawable drawable2, final int n3, final Drawable drawable3, final int n4, final RequestListener<? super A, R> requestListener, final RequestCoordinator requestCoordinator, final Engine engine, final Transformation<Z> transformation, final Class<R> clazz, final boolean b, final GlideAnimationFactory<R> glideAnimationFactory, final int n5, final int n6, final DiskCacheStrategy diskCacheStrategy) {
        GenericRequest<?, ?, ?, ?> genericRequest;
        if ((genericRequest = GenericRequest.REQUEST_POOL.poll()) == null) {
            genericRequest = new GenericRequest<A, T, Z, R>();
        }
        genericRequest.init(loadProvider, a, key, context, priority, target, n, drawable, n2, drawable2, n3, drawable3, n4, requestListener, requestCoordinator, engine, transformation, clazz, b, glideAnimationFactory, n5, n6, diskCacheStrategy);
        return (GenericRequest<A, T, Z, R>)genericRequest;
    }
    
    private void onResourceReady(final Resource<?> resource, final R r) {
        final boolean firstReadyResource = this.isFirstReadyResource();
        this.status = Status.COMPLETE;
        this.resource = resource;
        if (this.requestListener == null || !this.requestListener.onResourceReady(r, (Object)this.model, this.target, this.loadedFromMemoryCache, firstReadyResource)) {
            this.target.onResourceReady(r, this.animationFactory.build(this.loadedFromMemoryCache, firstReadyResource));
        }
        this.notifyLoadSuccess();
        if (Log.isLoggable("GenericRequest", 2)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Resource ready in ");
            sb.append(LogTime.getElapsedMillis(this.startTime));
            sb.append(" size: ");
            sb.append(resource.getSize() * 9.5367431640625E-7);
            sb.append(" fromCache: ");
            sb.append(this.loadedFromMemoryCache);
            this.logV(sb.toString());
        }
    }
    
    private void releaseResource(final Resource resource) {
        this.engine.release(resource);
        this.resource = null;
    }
    
    private void setErrorPlaceholder(final Exception ex) {
        if (!this.canNotifyStatusChanged()) {
            return;
        }
        Drawable fallbackDrawable;
        if (this.model == null) {
            fallbackDrawable = this.getFallbackDrawable();
        }
        else {
            fallbackDrawable = null;
        }
        Drawable errorDrawable = fallbackDrawable;
        if (fallbackDrawable == null) {
            errorDrawable = this.getErrorDrawable();
        }
        Drawable placeholderDrawable;
        if ((placeholderDrawable = errorDrawable) == null) {
            placeholderDrawable = this.getPlaceholderDrawable();
        }
        this.target.onLoadFailed(ex, placeholderDrawable);
    }
    
    @Override
    public void begin() {
        this.startTime = LogTime.getLogTime();
        if (this.model == null) {
            this.onException(null);
            return;
        }
        this.status = Status.WAITING_FOR_SIZE;
        if (Util.isValidDimensions(this.overrideWidth, this.overrideHeight)) {
            this.onSizeReady(this.overrideWidth, this.overrideHeight);
        }
        else {
            this.target.getSize(this);
        }
        if (!this.isComplete() && !this.isFailed() && this.canNotifyStatusChanged()) {
            this.target.onLoadStarted(this.getPlaceholderDrawable());
        }
        if (Log.isLoggable("GenericRequest", 2)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("finished run method in ");
            sb.append(LogTime.getElapsedMillis(this.startTime));
            this.logV(sb.toString());
        }
    }
    
    void cancel() {
        this.status = Status.CANCELLED;
        if (this.loadStatus != null) {
            this.loadStatus.cancel();
            this.loadStatus = null;
        }
    }
    
    @Override
    public void clear() {
        Util.assertMainThread();
        if (this.status == Status.CLEARED) {
            return;
        }
        this.cancel();
        if (this.resource != null) {
            this.releaseResource(this.resource);
        }
        if (this.canNotifyStatusChanged()) {
            this.target.onLoadCleared(this.getPlaceholderDrawable());
        }
        this.status = Status.CLEARED;
    }
    
    @Override
    public boolean isCancelled() {
        return this.status == Status.CANCELLED || this.status == Status.CLEARED;
    }
    
    @Override
    public boolean isComplete() {
        return this.status == Status.COMPLETE;
    }
    
    @Override
    public boolean isFailed() {
        return this.status == Status.FAILED;
    }
    
    @Override
    public boolean isPaused() {
        return this.status == Status.PAUSED;
    }
    
    @Override
    public boolean isResourceSet() {
        return this.isComplete();
    }
    
    @Override
    public boolean isRunning() {
        return this.status == Status.RUNNING || this.status == Status.WAITING_FOR_SIZE;
    }
    
    @Override
    public void onException(final Exception errorPlaceholder) {
        if (Log.isLoggable("GenericRequest", 3)) {
            Log.d("GenericRequest", "load failed", (Throwable)errorPlaceholder);
        }
        this.status = Status.FAILED;
        if (this.requestListener == null || !this.requestListener.onException(errorPlaceholder, (Object)this.model, this.target, this.isFirstReadyResource())) {
            this.setErrorPlaceholder(errorPlaceholder);
        }
    }
    
    @Override
    public void onResourceReady(final Resource<?> resource) {
        if (resource == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Expected to receive a Resource<R> with an object of ");
            sb.append(this.transcodeClass);
            sb.append(" inside, but instead got null.");
            this.onException(new Exception(sb.toString()));
            return;
        }
        final Object value = resource.get();
        if (value == null || !this.transcodeClass.isAssignableFrom(value.getClass())) {
            this.releaseResource(resource);
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Expected to receive an object of ");
            sb2.append(this.transcodeClass);
            sb2.append(" but instead got ");
            Serializable class1;
            if (value != null) {
                class1 = value.getClass();
            }
            else {
                class1 = "";
            }
            sb2.append(class1);
            sb2.append("{");
            sb2.append(value);
            sb2.append("}");
            sb2.append(" inside Resource{");
            sb2.append(resource);
            sb2.append("}.");
            String s;
            if (value != null) {
                s = "";
            }
            else {
                s = " To indicate failure return a null Resource object, rather than a Resource object containing null data.";
            }
            sb2.append(s);
            this.onException(new Exception(sb2.toString()));
            return;
        }
        if (!this.canSetResource()) {
            this.releaseResource(resource);
            this.status = Status.COMPLETE;
            return;
        }
        this.onResourceReady(resource, (R)value);
    }
    
    @Override
    public void onSizeReady(int round, int round2) {
        if (Log.isLoggable("GenericRequest", 2)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Got onSizeReady in ");
            sb.append(LogTime.getElapsedMillis(this.startTime));
            this.logV(sb.toString());
        }
        if (this.status != Status.WAITING_FOR_SIZE) {
            return;
        }
        this.status = Status.RUNNING;
        round = Math.round(this.sizeMultiplier * round);
        round2 = Math.round(this.sizeMultiplier * round2);
        final DataFetcher<T> resourceFetcher = this.loadProvider.getModelLoader().getResourceFetcher(this.model, round, round2);
        if (resourceFetcher == null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Failed to load model: '");
            sb2.append(this.model);
            sb2.append("'");
            this.onException(new Exception(sb2.toString()));
            return;
        }
        final ResourceTranscoder<Z, R> transcoder = this.loadProvider.getTranscoder();
        if (Log.isLoggable("GenericRequest", 2)) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("finished setup for calling load in ");
            sb3.append(LogTime.getElapsedMillis(this.startTime));
            this.logV(sb3.toString());
        }
        this.loadedFromMemoryCache = true;
        this.loadStatus = this.engine.load(this.signature, round, round2, resourceFetcher, this.loadProvider, this.transformation, transcoder, this.priority, this.isMemoryCacheable, this.diskCacheStrategy, this);
        this.loadedFromMemoryCache = (this.resource != null);
        if (Log.isLoggable("GenericRequest", 2)) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("finished onSizeReady in ");
            sb4.append(LogTime.getElapsedMillis(this.startTime));
            this.logV(sb4.toString());
        }
    }
    
    @Override
    public void pause() {
        this.clear();
        this.status = Status.PAUSED;
    }
    
    @Override
    public void recycle() {
        this.loadProvider = null;
        this.model = null;
        this.context = null;
        this.target = null;
        this.placeholderDrawable = null;
        this.errorDrawable = null;
        this.fallbackDrawable = null;
        this.requestListener = null;
        this.requestCoordinator = null;
        this.transformation = null;
        this.animationFactory = null;
        this.loadedFromMemoryCache = false;
        this.loadStatus = null;
        GenericRequest.REQUEST_POOL.offer(this);
    }
    
    private enum Status
    {
        CANCELLED, 
        CLEARED, 
        COMPLETE, 
        FAILED, 
        PAUSED, 
        PENDING, 
        RUNNING, 
        WAITING_FOR_SIZE;
    }
}
