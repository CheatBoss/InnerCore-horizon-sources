package com.bumptech.glide;

import android.content.*;
import com.bumptech.glide.load.engine.*;
import android.graphics.drawable.*;
import com.bumptech.glide.provider.*;
import com.bumptech.glide.signature.*;
import com.bumptech.glide.load.resource.*;
import com.bumptech.glide.util.*;
import android.view.animation.*;
import com.bumptech.glide.request.animation.*;
import java.io.*;
import com.bumptech.glide.request.*;
import android.widget.*;
import com.bumptech.glide.manager.*;
import com.bumptech.glide.request.target.*;
import com.bumptech.glide.load.resource.transcode.*;
import com.bumptech.glide.load.*;

public class GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> implements Cloneable
{
    private GlideAnimationFactory<TranscodeType> animationFactory;
    protected final Context context;
    private DiskCacheStrategy diskCacheStrategy;
    private int errorId;
    private Drawable errorPlaceholder;
    private Drawable fallbackDrawable;
    private int fallbackResource;
    protected final Glide glide;
    private boolean isCacheable;
    private boolean isModelSet;
    private boolean isThumbnailBuilt;
    private boolean isTransformationSet;
    protected final Lifecycle lifecycle;
    private ChildLoadProvider<ModelType, DataType, ResourceType, TranscodeType> loadProvider;
    private ModelType model;
    protected final Class<ModelType> modelClass;
    private int overrideHeight;
    private int overrideWidth;
    private Drawable placeholderDrawable;
    private int placeholderId;
    private Priority priority;
    private RequestListener<? super ModelType, TranscodeType> requestListener;
    protected final RequestTracker requestTracker;
    private Key signature;
    private Float sizeMultiplier;
    private Float thumbSizeMultiplier;
    private GenericRequestBuilder<?, ?, ?, TranscodeType> thumbnailRequestBuilder;
    protected final Class<TranscodeType> transcodeClass;
    private Transformation<ResourceType> transformation;
    
    GenericRequestBuilder(final Context context, final Class<ModelType> modelClass, final LoadProvider<ModelType, DataType, ResourceType, TranscodeType> loadProvider, final Class<TranscodeType> transcodeClass, final Glide glide, final RequestTracker requestTracker, final Lifecycle lifecycle) {
        this.signature = EmptySignature.obtain();
        this.sizeMultiplier = 1.0f;
        final ChildLoadProvider<ModelType, DataType, ResourceType, TranscodeType> childLoadProvider = null;
        this.priority = null;
        this.isCacheable = true;
        this.animationFactory = NoAnimation.getFactory();
        this.overrideHeight = -1;
        this.overrideWidth = -1;
        this.diskCacheStrategy = DiskCacheStrategy.RESULT;
        this.transformation = (Transformation<ResourceType>)UnitTransformation.get();
        this.context = context;
        this.modelClass = modelClass;
        this.transcodeClass = transcodeClass;
        this.glide = glide;
        this.requestTracker = requestTracker;
        this.lifecycle = lifecycle;
        ChildLoadProvider<ModelType, DataType, ResourceType, TranscodeType> loadProvider2 = childLoadProvider;
        if (loadProvider != null) {
            loadProvider2 = new ChildLoadProvider<ModelType, DataType, ResourceType, TranscodeType>(loadProvider);
        }
        this.loadProvider = loadProvider2;
        if (context == null) {
            throw new NullPointerException("Context can't be null");
        }
        if (modelClass != null && loadProvider == null) {
            throw new NullPointerException("LoadProvider must not be null");
        }
    }
    
    GenericRequestBuilder(final LoadProvider<ModelType, DataType, ResourceType, TranscodeType> loadProvider, final Class<TranscodeType> clazz, final GenericRequestBuilder<ModelType, ?, ?, ?> genericRequestBuilder) {
        this(genericRequestBuilder.context, genericRequestBuilder.modelClass, loadProvider, clazz, genericRequestBuilder.glide, genericRequestBuilder.requestTracker, genericRequestBuilder.lifecycle);
        this.model = genericRequestBuilder.model;
        this.isModelSet = genericRequestBuilder.isModelSet;
        this.signature = genericRequestBuilder.signature;
        this.diskCacheStrategy = genericRequestBuilder.diskCacheStrategy;
        this.isCacheable = genericRequestBuilder.isCacheable;
    }
    
    private Request buildRequest(final Target<TranscodeType> target) {
        if (this.priority == null) {
            this.priority = Priority.NORMAL;
        }
        return this.buildRequestRecursive(target, null);
    }
    
    private Request buildRequestRecursive(final Target<TranscodeType> target, ThumbnailRequestCoordinator thumbnailRequestCoordinator) {
        if (this.thumbnailRequestBuilder != null) {
            if (this.isThumbnailBuilt) {
                throw new IllegalStateException("You cannot use a request as both the main request and a thumbnail, consider using clone() on the request(s) passed to thumbnail()");
            }
            if (this.thumbnailRequestBuilder.animationFactory.equals(NoAnimation.getFactory())) {
                this.thumbnailRequestBuilder.animationFactory = this.animationFactory;
            }
            if (this.thumbnailRequestBuilder.priority == null) {
                this.thumbnailRequestBuilder.priority = this.getThumbnailPriority();
            }
            if (Util.isValidDimensions(this.overrideWidth, this.overrideHeight) && !Util.isValidDimensions(this.thumbnailRequestBuilder.overrideWidth, this.thumbnailRequestBuilder.overrideHeight)) {
                this.thumbnailRequestBuilder.override(this.overrideWidth, this.overrideHeight);
            }
            thumbnailRequestCoordinator = new ThumbnailRequestCoordinator(thumbnailRequestCoordinator);
            final Request obtainRequest = this.obtainRequest(target, this.sizeMultiplier, this.priority, thumbnailRequestCoordinator);
            this.isThumbnailBuilt = true;
            final Request buildRequestRecursive = this.thumbnailRequestBuilder.buildRequestRecursive(target, thumbnailRequestCoordinator);
            this.isThumbnailBuilt = false;
            thumbnailRequestCoordinator.setRequests(obtainRequest, buildRequestRecursive);
            return thumbnailRequestCoordinator;
        }
        else {
            if (this.thumbSizeMultiplier != null) {
                thumbnailRequestCoordinator = new ThumbnailRequestCoordinator(thumbnailRequestCoordinator);
                thumbnailRequestCoordinator.setRequests(this.obtainRequest(target, this.sizeMultiplier, this.priority, thumbnailRequestCoordinator), this.obtainRequest(target, this.thumbSizeMultiplier, this.getThumbnailPriority(), thumbnailRequestCoordinator));
                return thumbnailRequestCoordinator;
            }
            return this.obtainRequest(target, this.sizeMultiplier, this.priority, thumbnailRequestCoordinator);
        }
    }
    
    private Priority getThumbnailPriority() {
        if (this.priority == Priority.LOW) {
            return Priority.NORMAL;
        }
        if (this.priority == Priority.NORMAL) {
            return Priority.HIGH;
        }
        return Priority.IMMEDIATE;
    }
    
    private Request obtainRequest(final Target<TranscodeType> target, final float n, final Priority priority, final RequestCoordinator requestCoordinator) {
        return GenericRequest.obtain(this.loadProvider, this.model, this.signature, this.context, priority, target, n, this.placeholderDrawable, this.placeholderId, this.errorPlaceholder, this.errorId, this.fallbackDrawable, this.fallbackResource, this.requestListener, requestCoordinator, this.glide.getEngine(), this.transformation, this.transcodeClass, this.isCacheable, this.animationFactory, this.overrideWidth, this.overrideHeight, this.diskCacheStrategy);
    }
    
    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> animate(final int n) {
        return this.animate(new ViewAnimationFactory<TranscodeType>(this.context, n));
    }
    
    @Deprecated
    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> animate(final Animation animation) {
        return this.animate(new ViewAnimationFactory<TranscodeType>(animation));
    }
    
    GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> animate(final GlideAnimationFactory<TranscodeType> animationFactory) {
        if (animationFactory == null) {
            throw new NullPointerException("Animation factory must not be null!");
        }
        this.animationFactory = animationFactory;
        return this;
    }
    
    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> animate(final ViewPropertyAnimation.Animator animator) {
        return this.animate(new ViewPropertyAnimationFactory<TranscodeType>(animator));
    }
    
    void applyCenterCrop() {
    }
    
    void applyFitCenter() {
    }
    
    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> cacheDecoder(final ResourceDecoder<File, ResourceType> cacheDecoder) {
        if (this.loadProvider != null) {
            this.loadProvider.setCacheDecoder(cacheDecoder);
        }
        return this;
    }
    
    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> clone() {
        while (true) {
            while (true) {
                try {
                    final GenericRequestBuilder genericRequestBuilder = (GenericRequestBuilder)super.clone();
                    if (this.loadProvider != null) {
                        final ChildLoadProvider<ModelType, DataType, ResourceType, TranscodeType> clone = this.loadProvider.clone();
                        genericRequestBuilder.loadProvider = clone;
                        return genericRequestBuilder;
                    }
                }
                catch (CloneNotSupportedException ex) {
                    throw new RuntimeException(ex);
                }
                final ChildLoadProvider<ModelType, DataType, ResourceType, TranscodeType> clone = null;
                continue;
            }
        }
    }
    
    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> decoder(final ResourceDecoder<DataType, ResourceType> sourceDecoder) {
        if (this.loadProvider != null) {
            this.loadProvider.setSourceDecoder(sourceDecoder);
        }
        return this;
    }
    
    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> diskCacheStrategy(final DiskCacheStrategy diskCacheStrategy) {
        this.diskCacheStrategy = diskCacheStrategy;
        return this;
    }
    
    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> dontAnimate() {
        return this.animate(NoAnimation.getFactory());
    }
    
    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> dontTransform() {
        return this.transform(UnitTransformation.get());
    }
    
    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> encoder(final ResourceEncoder<ResourceType> encoder) {
        if (this.loadProvider != null) {
            this.loadProvider.setEncoder(encoder);
        }
        return this;
    }
    
    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> error(final int errorId) {
        this.errorId = errorId;
        return this;
    }
    
    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> error(final Drawable errorPlaceholder) {
        this.errorPlaceholder = errorPlaceholder;
        return this;
    }
    
    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> fallback(final int fallbackResource) {
        this.fallbackResource = fallbackResource;
        return this;
    }
    
    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> fallback(final Drawable fallbackDrawable) {
        this.fallbackDrawable = fallbackDrawable;
        return this;
    }
    
    public FutureTarget<TranscodeType> into(final int n, final int n2) {
        final RequestFutureTarget<Object, TranscodeType> requestFutureTarget = new RequestFutureTarget<Object, TranscodeType>(this.glide.getMainHandler(), n, n2);
        this.glide.getMainHandler().post((Runnable)new Runnable() {
            @Override
            public void run() {
                if (!requestFutureTarget.isCancelled()) {
                    GenericRequestBuilder.this.into(requestFutureTarget);
                }
            }
        });
        return requestFutureTarget;
    }
    
    public Target<TranscodeType> into(final ImageView imageView) {
        Util.assertMainThread();
        if (imageView == null) {
            throw new IllegalArgumentException("You must pass in a non null View");
        }
        if (!this.isTransformationSet && imageView.getScaleType() != null) {
            switch (imageView.getScaleType()) {
                case FIT_CENTER:
                case FIT_START:
                case FIT_END: {
                    this.applyFitCenter();
                    break;
                }
                case CENTER_CROP: {
                    this.applyCenterCrop();
                    break;
                }
            }
        }
        return this.into(this.glide.buildImageViewTarget(imageView, this.transcodeClass));
    }
    
    public <Y extends Target<TranscodeType>> Y into(final Y y) {
        Util.assertMainThread();
        if (y == null) {
            throw new IllegalArgumentException("You must pass in a non null Target");
        }
        if (!this.isModelSet) {
            throw new IllegalArgumentException("You must first set a model (try #load())");
        }
        final Request request = y.getRequest();
        if (request != null) {
            request.clear();
            this.requestTracker.removeRequest(request);
            request.recycle();
        }
        final Request buildRequest = this.buildRequest(y);
        y.setRequest(buildRequest);
        this.lifecycle.addListener(y);
        this.requestTracker.runRequest(buildRequest);
        return y;
    }
    
    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> listener(final RequestListener<? super ModelType, TranscodeType> requestListener) {
        this.requestListener = requestListener;
        return this;
    }
    
    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> load(final ModelType model) {
        this.model = model;
        this.isModelSet = true;
        return this;
    }
    
    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> override(final int overrideWidth, final int overrideHeight) {
        if (!Util.isValidDimensions(overrideWidth, overrideHeight)) {
            throw new IllegalArgumentException("Width and height must be Target#SIZE_ORIGINAL or > 0");
        }
        this.overrideWidth = overrideWidth;
        this.overrideHeight = overrideHeight;
        return this;
    }
    
    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> placeholder(final int placeholderId) {
        this.placeholderId = placeholderId;
        return this;
    }
    
    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> placeholder(final Drawable placeholderDrawable) {
        this.placeholderDrawable = placeholderDrawable;
        return this;
    }
    
    public Target<TranscodeType> preload() {
        return this.preload(Integer.MIN_VALUE, Integer.MIN_VALUE);
    }
    
    public Target<TranscodeType> preload(final int n, final int n2) {
        return this.into((Target<TranscodeType>)PreloadTarget.obtain(n, n2));
    }
    
    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> priority(final Priority priority) {
        this.priority = priority;
        return this;
    }
    
    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> signature(final Key signature) {
        if (signature == null) {
            throw new NullPointerException("Signature must not be null");
        }
        this.signature = signature;
        return this;
    }
    
    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> sizeMultiplier(final float n) {
        if (n >= 0.0f && n <= 1.0f) {
            this.sizeMultiplier = n;
            return this;
        }
        throw new IllegalArgumentException("sizeMultiplier must be between 0 and 1");
    }
    
    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> skipMemoryCache(final boolean b) {
        this.isCacheable = (b ^ true);
        return this;
    }
    
    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> sourceEncoder(final Encoder<DataType> sourceEncoder) {
        if (this.loadProvider != null) {
            this.loadProvider.setSourceEncoder(sourceEncoder);
        }
        return this;
    }
    
    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> thumbnail(final float n) {
        if (n >= 0.0f && n <= 1.0f) {
            this.thumbSizeMultiplier = n;
            return this;
        }
        throw new IllegalArgumentException("sizeMultiplier must be between 0 and 1");
    }
    
    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> thumbnail(final GenericRequestBuilder<?, ?, ?, TranscodeType> thumbnailRequestBuilder) {
        if (this.equals(thumbnailRequestBuilder)) {
            throw new IllegalArgumentException("You cannot set a request as a thumbnail for itself. Consider using clone() on the request you are passing to thumbnail()");
        }
        this.thumbnailRequestBuilder = thumbnailRequestBuilder;
        return this;
    }
    
    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> transcoder(final ResourceTranscoder<ResourceType, TranscodeType> transcoder) {
        if (this.loadProvider != null) {
            this.loadProvider.setTranscoder(transcoder);
        }
        return this;
    }
    
    public GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> transform(final Transformation<ResourceType>... array) {
        this.isTransformationSet = true;
        if (array.length == 1) {
            this.transformation = array[0];
            return this;
        }
        this.transformation = new MultiTransformation<ResourceType>(array);
        return this;
    }
}
