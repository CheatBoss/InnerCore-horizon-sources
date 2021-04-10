package com.bumptech.glide;

import com.bumptech.glide.load.model.*;
import android.graphics.*;
import com.bumptech.glide.load.engine.bitmap_recycle.*;
import android.os.*;
import com.bumptech.glide.provider.*;
import android.view.animation.*;
import com.bumptech.glide.request.animation.*;
import java.io.*;
import com.bumptech.glide.load.engine.*;
import android.graphics.drawable.*;
import com.bumptech.glide.load.resource.bitmap.*;
import com.bumptech.glide.load.resource.file.*;
import android.widget.*;
import com.bumptech.glide.request.target.*;
import com.bumptech.glide.request.*;
import com.bumptech.glide.load.resource.transcode.*;
import com.bumptech.glide.load.*;

public class BitmapRequestBuilder<ModelType, TranscodeType> extends GenericRequestBuilder<ModelType, ImageVideoWrapper, Bitmap, TranscodeType> implements BitmapOptions
{
    private final BitmapPool bitmapPool;
    private DecodeFormat decodeFormat;
    private Downsampler downsampler;
    private ResourceDecoder<InputStream, Bitmap> imageDecoder;
    private ResourceDecoder<ParcelFileDescriptor, Bitmap> videoDecoder;
    
    BitmapRequestBuilder(final LoadProvider<ModelType, ImageVideoWrapper, Bitmap, TranscodeType> loadProvider, final Class<TranscodeType> clazz, final GenericRequestBuilder<ModelType, ?, ?, ?> genericRequestBuilder) {
        super(loadProvider, clazz, genericRequestBuilder);
        this.downsampler = Downsampler.AT_LEAST;
        this.bitmapPool = genericRequestBuilder.glide.getBitmapPool();
        this.decodeFormat = genericRequestBuilder.glide.getDecodeFormat();
        this.imageDecoder = new StreamBitmapDecoder(this.bitmapPool, this.decodeFormat);
        this.videoDecoder = new FileDescriptorBitmapDecoder(this.bitmapPool, this.decodeFormat);
    }
    
    private BitmapRequestBuilder<ModelType, TranscodeType> downsample(final Downsampler downsampler) {
        this.downsampler = downsampler;
        this.imageDecoder = new StreamBitmapDecoder(downsampler, this.bitmapPool, this.decodeFormat);
        super.decoder(new ImageVideoBitmapDecoder(this.imageDecoder, this.videoDecoder));
        return this;
    }
    
    @Override
    public BitmapRequestBuilder<ModelType, TranscodeType> animate(final int n) {
        super.animate(n);
        return this;
    }
    
    @Deprecated
    @Override
    public BitmapRequestBuilder<ModelType, TranscodeType> animate(final Animation animation) {
        super.animate(animation);
        return this;
    }
    
    @Override
    public BitmapRequestBuilder<ModelType, TranscodeType> animate(final ViewPropertyAnimation.Animator animator) {
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
    
    public BitmapRequestBuilder<ModelType, TranscodeType> approximate() {
        return this.downsample(Downsampler.AT_LEAST);
    }
    
    public BitmapRequestBuilder<ModelType, TranscodeType> asIs() {
        return this.downsample(Downsampler.NONE);
    }
    
    public BitmapRequestBuilder<ModelType, TranscodeType> atMost() {
        return this.downsample(Downsampler.AT_MOST);
    }
    
    @Override
    public BitmapRequestBuilder<ModelType, TranscodeType> cacheDecoder(final ResourceDecoder<File, Bitmap> resourceDecoder) {
        super.cacheDecoder(resourceDecoder);
        return this;
    }
    
    @Override
    public BitmapRequestBuilder<ModelType, TranscodeType> centerCrop() {
        return this.transform(this.glide.getBitmapCenterCrop());
    }
    
    @Override
    public BitmapRequestBuilder<ModelType, TranscodeType> clone() {
        return (BitmapRequestBuilder)super.clone();
    }
    
    @Override
    public BitmapRequestBuilder<ModelType, TranscodeType> decoder(final ResourceDecoder<ImageVideoWrapper, Bitmap> resourceDecoder) {
        super.decoder(resourceDecoder);
        return this;
    }
    
    @Override
    public BitmapRequestBuilder<ModelType, TranscodeType> diskCacheStrategy(final DiskCacheStrategy diskCacheStrategy) {
        super.diskCacheStrategy(diskCacheStrategy);
        return this;
    }
    
    @Override
    public BitmapRequestBuilder<ModelType, TranscodeType> dontAnimate() {
        super.dontAnimate();
        return this;
    }
    
    @Override
    public BitmapRequestBuilder<ModelType, TranscodeType> dontTransform() {
        super.dontTransform();
        return this;
    }
    
    @Override
    public BitmapRequestBuilder<ModelType, TranscodeType> encoder(final ResourceEncoder<Bitmap> resourceEncoder) {
        super.encoder(resourceEncoder);
        return this;
    }
    
    @Override
    public BitmapRequestBuilder<ModelType, TranscodeType> error(final int n) {
        super.error(n);
        return this;
    }
    
    @Override
    public BitmapRequestBuilder<ModelType, TranscodeType> error(final Drawable drawable) {
        super.error(drawable);
        return this;
    }
    
    @Override
    public BitmapRequestBuilder<ModelType, TranscodeType> fallback(final int n) {
        super.fallback(n);
        return this;
    }
    
    @Override
    public BitmapRequestBuilder<ModelType, TranscodeType> fallback(final Drawable drawable) {
        super.fallback(drawable);
        return this;
    }
    
    @Override
    public BitmapRequestBuilder<ModelType, TranscodeType> fitCenter() {
        return this.transform(this.glide.getBitmapFitCenter());
    }
    
    public BitmapRequestBuilder<ModelType, TranscodeType> format(final DecodeFormat decodeFormat) {
        this.decodeFormat = decodeFormat;
        this.imageDecoder = new StreamBitmapDecoder(this.downsampler, this.bitmapPool, decodeFormat);
        this.videoDecoder = new FileDescriptorBitmapDecoder(new VideoBitmapDecoder(), this.bitmapPool, decodeFormat);
        super.cacheDecoder(new FileToStreamDecoder<Bitmap>(new StreamBitmapDecoder(this.downsampler, this.bitmapPool, decodeFormat)));
        super.decoder(new ImageVideoBitmapDecoder(this.imageDecoder, this.videoDecoder));
        return this;
    }
    
    public BitmapRequestBuilder<ModelType, TranscodeType> imageDecoder(final ResourceDecoder<InputStream, Bitmap> imageDecoder) {
        this.imageDecoder = imageDecoder;
        super.decoder(new ImageVideoBitmapDecoder(imageDecoder, this.videoDecoder));
        return this;
    }
    
    @Override
    public Target<TranscodeType> into(final ImageView imageView) {
        return super.into(imageView);
    }
    
    @Override
    public BitmapRequestBuilder<ModelType, TranscodeType> listener(final RequestListener<? super ModelType, TranscodeType> requestListener) {
        super.listener((RequestListener<? super Object, TranscodeType>)requestListener);
        return this;
    }
    
    @Override
    public BitmapRequestBuilder<ModelType, TranscodeType> load(final ModelType modelType) {
        super.load(modelType);
        return this;
    }
    
    @Override
    public BitmapRequestBuilder<ModelType, TranscodeType> override(final int n, final int n2) {
        super.override(n, n2);
        return this;
    }
    
    @Override
    public BitmapRequestBuilder<ModelType, TranscodeType> placeholder(final int n) {
        super.placeholder(n);
        return this;
    }
    
    @Override
    public BitmapRequestBuilder<ModelType, TranscodeType> placeholder(final Drawable drawable) {
        super.placeholder(drawable);
        return this;
    }
    
    @Override
    public BitmapRequestBuilder<ModelType, TranscodeType> priority(final Priority priority) {
        super.priority(priority);
        return this;
    }
    
    @Override
    public BitmapRequestBuilder<ModelType, TranscodeType> signature(final Key key) {
        super.signature(key);
        return this;
    }
    
    @Override
    public BitmapRequestBuilder<ModelType, TranscodeType> sizeMultiplier(final float n) {
        super.sizeMultiplier(n);
        return this;
    }
    
    @Override
    public BitmapRequestBuilder<ModelType, TranscodeType> skipMemoryCache(final boolean b) {
        super.skipMemoryCache(b);
        return this;
    }
    
    @Override
    public BitmapRequestBuilder<ModelType, TranscodeType> sourceEncoder(final Encoder<ImageVideoWrapper> encoder) {
        super.sourceEncoder(encoder);
        return this;
    }
    
    @Override
    public BitmapRequestBuilder<ModelType, TranscodeType> thumbnail(final float n) {
        super.thumbnail(n);
        return this;
    }
    
    public BitmapRequestBuilder<ModelType, TranscodeType> thumbnail(final BitmapRequestBuilder<?, TranscodeType> bitmapRequestBuilder) {
        super.thumbnail(bitmapRequestBuilder);
        return this;
    }
    
    @Override
    public BitmapRequestBuilder<ModelType, TranscodeType> thumbnail(final GenericRequestBuilder<?, ?, ?, TranscodeType> genericRequestBuilder) {
        super.thumbnail(genericRequestBuilder);
        return this;
    }
    
    @Override
    public BitmapRequestBuilder<ModelType, TranscodeType> transcoder(final ResourceTranscoder<Bitmap, TranscodeType> resourceTranscoder) {
        super.transcoder(resourceTranscoder);
        return this;
    }
    
    @Override
    public BitmapRequestBuilder<ModelType, TranscodeType> transform(final Transformation<Bitmap>... array) {
        super.transform(array);
        return this;
    }
    
    public BitmapRequestBuilder<ModelType, TranscodeType> transform(final BitmapTransformation... array) {
        super.transform((Transformation<Bitmap>[])array);
        return this;
    }
    
    public BitmapRequestBuilder<ModelType, TranscodeType> videoDecoder(final ResourceDecoder<ParcelFileDescriptor, Bitmap> videoDecoder) {
        this.videoDecoder = videoDecoder;
        super.decoder(new ImageVideoBitmapDecoder(this.imageDecoder, videoDecoder));
        return this;
    }
}
