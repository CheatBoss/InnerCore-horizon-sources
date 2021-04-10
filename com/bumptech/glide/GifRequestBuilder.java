package com.bumptech.glide;

import com.bumptech.glide.provider.*;
import android.graphics.*;
import com.bumptech.glide.load.resource.gif.*;
import android.view.animation.*;
import java.io.*;
import com.bumptech.glide.load.resource.bitmap.*;
import com.bumptech.glide.request.animation.*;
import com.bumptech.glide.load.engine.*;
import android.graphics.drawable.*;
import com.bumptech.glide.request.*;
import com.bumptech.glide.load.*;
import com.bumptech.glide.load.resource.transcode.*;

public class GifRequestBuilder<ModelType> extends GenericRequestBuilder<ModelType, InputStream, GifDrawable, GifDrawable> implements BitmapOptions, DrawableOptions
{
    GifRequestBuilder(final LoadProvider<ModelType, InputStream, GifDrawable, GifDrawable> loadProvider, final Class<GifDrawable> clazz, final GenericRequestBuilder<ModelType, ?, ?, ?> genericRequestBuilder) {
        super(loadProvider, clazz, genericRequestBuilder);
    }
    
    private GifDrawableTransformation[] toGifTransformations(final Transformation<Bitmap>[] array) {
        final GifDrawableTransformation[] array2 = new GifDrawableTransformation[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = new GifDrawableTransformation(array[i], this.glide.getBitmapPool());
        }
        return array2;
    }
    
    @Override
    public GifRequestBuilder<ModelType> animate(final int n) {
        super.animate(n);
        return this;
    }
    
    @Deprecated
    @Override
    public GifRequestBuilder<ModelType> animate(final Animation animation) {
        super.animate(animation);
        return this;
    }
    
    @Override
    public GifRequestBuilder<ModelType> animate(final ViewPropertyAnimation.Animator animator) {
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
    
    @Override
    public GifRequestBuilder<ModelType> cacheDecoder(final ResourceDecoder<File, GifDrawable> resourceDecoder) {
        super.cacheDecoder(resourceDecoder);
        return this;
    }
    
    @Override
    public GifRequestBuilder<ModelType> centerCrop() {
        return this.transformFrame(this.glide.getBitmapCenterCrop());
    }
    
    @Override
    public GifRequestBuilder<ModelType> clone() {
        return (GifRequestBuilder)super.clone();
    }
    
    @Override
    public GifRequestBuilder<ModelType> crossFade() {
        super.animate(new DrawableCrossFadeFactory<GifDrawable>());
        return this;
    }
    
    @Override
    public GifRequestBuilder<ModelType> crossFade(final int n) {
        super.animate(new DrawableCrossFadeFactory<GifDrawable>(n));
        return this;
    }
    
    @Override
    public GifRequestBuilder<ModelType> crossFade(final int n, final int n2) {
        super.animate(new DrawableCrossFadeFactory<GifDrawable>(this.context, n, n2));
        return this;
    }
    
    @Deprecated
    @Override
    public GifRequestBuilder<ModelType> crossFade(final Animation animation, final int n) {
        super.animate(new DrawableCrossFadeFactory<GifDrawable>(animation, n));
        return this;
    }
    
    @Override
    public GifRequestBuilder<ModelType> decoder(final ResourceDecoder<InputStream, GifDrawable> resourceDecoder) {
        super.decoder(resourceDecoder);
        return this;
    }
    
    @Override
    public GifRequestBuilder<ModelType> diskCacheStrategy(final DiskCacheStrategy diskCacheStrategy) {
        super.diskCacheStrategy(diskCacheStrategy);
        return this;
    }
    
    @Override
    public GifRequestBuilder<ModelType> dontAnimate() {
        super.dontAnimate();
        return this;
    }
    
    @Override
    public GifRequestBuilder<ModelType> dontTransform() {
        super.dontTransform();
        return this;
    }
    
    @Override
    public GifRequestBuilder<ModelType> encoder(final ResourceEncoder<GifDrawable> resourceEncoder) {
        super.encoder(resourceEncoder);
        return this;
    }
    
    @Override
    public GifRequestBuilder<ModelType> error(final int n) {
        super.error(n);
        return this;
    }
    
    @Override
    public GifRequestBuilder<ModelType> error(final Drawable drawable) {
        super.error(drawable);
        return this;
    }
    
    @Override
    public GifRequestBuilder<ModelType> fallback(final int n) {
        super.fallback(n);
        return this;
    }
    
    @Override
    public GifRequestBuilder<ModelType> fallback(final Drawable drawable) {
        super.fallback(drawable);
        return this;
    }
    
    @Override
    public GifRequestBuilder<ModelType> fitCenter() {
        return this.transformFrame(this.glide.getBitmapFitCenter());
    }
    
    @Override
    public GifRequestBuilder<ModelType> listener(final RequestListener<? super ModelType, GifDrawable> requestListener) {
        super.listener((RequestListener<? super Object, GifDrawable>)requestListener);
        return this;
    }
    
    @Override
    public GifRequestBuilder<ModelType> load(final ModelType modelType) {
        super.load(modelType);
        return this;
    }
    
    @Override
    public GifRequestBuilder<ModelType> override(final int n, final int n2) {
        super.override(n, n2);
        return this;
    }
    
    @Override
    public GifRequestBuilder<ModelType> placeholder(final int n) {
        super.placeholder(n);
        return this;
    }
    
    @Override
    public GifRequestBuilder<ModelType> placeholder(final Drawable drawable) {
        super.placeholder(drawable);
        return this;
    }
    
    @Override
    public GifRequestBuilder<ModelType> priority(final Priority priority) {
        super.priority(priority);
        return this;
    }
    
    @Override
    public GifRequestBuilder<ModelType> signature(final Key key) {
        super.signature(key);
        return this;
    }
    
    @Override
    public GifRequestBuilder<ModelType> sizeMultiplier(final float n) {
        super.sizeMultiplier(n);
        return this;
    }
    
    @Override
    public GifRequestBuilder<ModelType> skipMemoryCache(final boolean b) {
        super.skipMemoryCache(b);
        return this;
    }
    
    @Override
    public GifRequestBuilder<ModelType> sourceEncoder(final Encoder<InputStream> encoder) {
        super.sourceEncoder(encoder);
        return this;
    }
    
    @Override
    public GifRequestBuilder<ModelType> thumbnail(final float n) {
        super.thumbnail(n);
        return this;
    }
    
    @Override
    public GifRequestBuilder<ModelType> thumbnail(final GenericRequestBuilder<?, ?, ?, GifDrawable> genericRequestBuilder) {
        super.thumbnail(genericRequestBuilder);
        return this;
    }
    
    public GifRequestBuilder<ModelType> thumbnail(final GifRequestBuilder<?> gifRequestBuilder) {
        super.thumbnail(gifRequestBuilder);
        return this;
    }
    
    @Override
    public GifRequestBuilder<ModelType> transcoder(final ResourceTranscoder<GifDrawable, GifDrawable> resourceTranscoder) {
        super.transcoder(resourceTranscoder);
        return this;
    }
    
    @Override
    public GifRequestBuilder<ModelType> transform(final Transformation<GifDrawable>... array) {
        super.transform(array);
        return this;
    }
    
    public GifRequestBuilder<ModelType> transformFrame(final Transformation<Bitmap>... array) {
        return this.transform((Transformation<GifDrawable>[])this.toGifTransformations(array));
    }
    
    public GifRequestBuilder<ModelType> transformFrame(final BitmapTransformation... array) {
        return this.transform((Transformation<GifDrawable>[])this.toGifTransformations(array));
    }
}
