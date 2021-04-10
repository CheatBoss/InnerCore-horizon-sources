package com.bumptech.glide;

import com.bumptech.glide.load.model.*;
import com.bumptech.glide.load.resource.drawable.*;
import android.content.*;
import com.bumptech.glide.provider.*;
import com.bumptech.glide.manager.*;
import android.view.animation.*;
import android.graphics.*;
import com.bumptech.glide.load.resource.gifbitmap.*;
import java.io.*;
import com.bumptech.glide.request.animation.*;
import com.bumptech.glide.load.engine.*;
import android.graphics.drawable.*;
import android.widget.*;
import com.bumptech.glide.request.target.*;
import com.bumptech.glide.request.*;
import com.bumptech.glide.load.*;
import com.bumptech.glide.load.resource.transcode.*;
import com.bumptech.glide.load.resource.bitmap.*;

public class DrawableRequestBuilder<ModelType> extends GenericRequestBuilder<ModelType, ImageVideoWrapper, GifBitmapWrapper, GlideDrawable> implements BitmapOptions, DrawableOptions
{
    DrawableRequestBuilder(final Context context, final Class<ModelType> clazz, final LoadProvider<ModelType, ImageVideoWrapper, GifBitmapWrapper, GlideDrawable> loadProvider, final Glide glide, final RequestTracker requestTracker, final Lifecycle lifecycle) {
        super(context, clazz, loadProvider, GlideDrawable.class, glide, requestTracker, lifecycle);
        this.crossFade();
    }
    
    @Override
    public DrawableRequestBuilder<ModelType> animate(final int n) {
        super.animate(n);
        return this;
    }
    
    @Deprecated
    @Override
    public DrawableRequestBuilder<ModelType> animate(final Animation animation) {
        super.animate(animation);
        return this;
    }
    
    @Override
    public DrawableRequestBuilder<ModelType> animate(final ViewPropertyAnimation.Animator animator) {
        super.animate(animator);
        return this;
    }
    
    @Override
    void applyCenterCrop() {
        this.centerCrop();
    }
    
    @Override
    void applyFitCenter() {
        this.fitCenter();
    }
    
    public DrawableRequestBuilder<ModelType> bitmapTransform(final Transformation<Bitmap>... array) {
        final GifBitmapWrapperTransformation[] array2 = new GifBitmapWrapperTransformation[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = new GifBitmapWrapperTransformation(this.glide.getBitmapPool(), array[i]);
        }
        return this.transform((Transformation<GifBitmapWrapper>[])array2);
    }
    
    @Override
    public DrawableRequestBuilder<ModelType> cacheDecoder(final ResourceDecoder<File, GifBitmapWrapper> resourceDecoder) {
        super.cacheDecoder(resourceDecoder);
        return this;
    }
    
    @Override
    public DrawableRequestBuilder<ModelType> centerCrop() {
        return this.transform(this.glide.getDrawableCenterCrop());
    }
    
    @Override
    public DrawableRequestBuilder<ModelType> clone() {
        return (DrawableRequestBuilder)super.clone();
    }
    
    @Override
    public final DrawableRequestBuilder<ModelType> crossFade() {
        super.animate(new DrawableCrossFadeFactory<GlideDrawable>());
        return this;
    }
    
    @Override
    public DrawableRequestBuilder<ModelType> crossFade(final int n) {
        super.animate(new DrawableCrossFadeFactory<GlideDrawable>(n));
        return this;
    }
    
    @Override
    public DrawableRequestBuilder<ModelType> crossFade(final int n, final int n2) {
        super.animate(new DrawableCrossFadeFactory<GlideDrawable>(this.context, n, n2));
        return this;
    }
    
    @Deprecated
    @Override
    public DrawableRequestBuilder<ModelType> crossFade(final Animation animation, final int n) {
        super.animate(new DrawableCrossFadeFactory<GlideDrawable>(animation, n));
        return this;
    }
    
    @Override
    public DrawableRequestBuilder<ModelType> decoder(final ResourceDecoder<ImageVideoWrapper, GifBitmapWrapper> resourceDecoder) {
        super.decoder(resourceDecoder);
        return this;
    }
    
    @Override
    public DrawableRequestBuilder<ModelType> diskCacheStrategy(final DiskCacheStrategy diskCacheStrategy) {
        super.diskCacheStrategy(diskCacheStrategy);
        return this;
    }
    
    @Override
    public DrawableRequestBuilder<ModelType> dontAnimate() {
        super.dontAnimate();
        return this;
    }
    
    @Override
    public DrawableRequestBuilder<ModelType> dontTransform() {
        super.dontTransform();
        return this;
    }
    
    @Override
    public DrawableRequestBuilder<ModelType> encoder(final ResourceEncoder<GifBitmapWrapper> resourceEncoder) {
        super.encoder(resourceEncoder);
        return this;
    }
    
    @Override
    public DrawableRequestBuilder<ModelType> error(final int n) {
        super.error(n);
        return this;
    }
    
    @Override
    public DrawableRequestBuilder<ModelType> error(final Drawable drawable) {
        super.error(drawable);
        return this;
    }
    
    @Override
    public DrawableRequestBuilder<ModelType> fallback(final int n) {
        super.fallback(n);
        return this;
    }
    
    @Override
    public DrawableRequestBuilder<ModelType> fallback(final Drawable drawable) {
        super.fallback(drawable);
        return this;
    }
    
    @Override
    public DrawableRequestBuilder<ModelType> fitCenter() {
        return this.transform(this.glide.getDrawableFitCenter());
    }
    
    @Override
    public Target<GlideDrawable> into(final ImageView imageView) {
        return super.into(imageView);
    }
    
    @Override
    public DrawableRequestBuilder<ModelType> listener(final RequestListener<? super ModelType, GlideDrawable> requestListener) {
        super.listener((RequestListener<? super Object, GlideDrawable>)requestListener);
        return this;
    }
    
    @Override
    public DrawableRequestBuilder<ModelType> load(final ModelType modelType) {
        super.load(modelType);
        return this;
    }
    
    @Override
    public DrawableRequestBuilder<ModelType> override(final int n, final int n2) {
        super.override(n, n2);
        return this;
    }
    
    @Override
    public DrawableRequestBuilder<ModelType> placeholder(final int n) {
        super.placeholder(n);
        return this;
    }
    
    @Override
    public DrawableRequestBuilder<ModelType> placeholder(final Drawable drawable) {
        super.placeholder(drawable);
        return this;
    }
    
    @Override
    public DrawableRequestBuilder<ModelType> priority(final Priority priority) {
        super.priority(priority);
        return this;
    }
    
    @Override
    public DrawableRequestBuilder<ModelType> signature(final Key key) {
        super.signature(key);
        return this;
    }
    
    @Override
    public DrawableRequestBuilder<ModelType> sizeMultiplier(final float n) {
        super.sizeMultiplier(n);
        return this;
    }
    
    @Override
    public DrawableRequestBuilder<ModelType> skipMemoryCache(final boolean b) {
        super.skipMemoryCache(b);
        return this;
    }
    
    @Override
    public DrawableRequestBuilder<ModelType> sourceEncoder(final Encoder<ImageVideoWrapper> encoder) {
        super.sourceEncoder(encoder);
        return this;
    }
    
    @Override
    public DrawableRequestBuilder<ModelType> thumbnail(final float n) {
        super.thumbnail(n);
        return this;
    }
    
    public DrawableRequestBuilder<ModelType> thumbnail(final DrawableRequestBuilder<?> drawableRequestBuilder) {
        super.thumbnail(drawableRequestBuilder);
        return this;
    }
    
    @Override
    public DrawableRequestBuilder<ModelType> thumbnail(final GenericRequestBuilder<?, ?, ?, GlideDrawable> genericRequestBuilder) {
        super.thumbnail(genericRequestBuilder);
        return this;
    }
    
    @Override
    public DrawableRequestBuilder<ModelType> transcoder(final ResourceTranscoder<GifBitmapWrapper, GlideDrawable> resourceTranscoder) {
        super.transcoder(resourceTranscoder);
        return this;
    }
    
    @Override
    public DrawableRequestBuilder<ModelType> transform(final Transformation<GifBitmapWrapper>... array) {
        super.transform(array);
        return this;
    }
    
    public DrawableRequestBuilder<ModelType> transform(final BitmapTransformation... array) {
        return this.bitmapTransform((Transformation<Bitmap>[])array);
    }
}
