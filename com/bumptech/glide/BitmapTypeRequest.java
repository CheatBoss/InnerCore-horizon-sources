package com.bumptech.glide;

import android.os.*;
import java.io.*;
import com.bumptech.glide.load.model.*;
import com.bumptech.glide.provider.*;
import com.bumptech.glide.load.resource.transcode.*;
import android.graphics.*;

public class BitmapTypeRequest<ModelType> extends BitmapRequestBuilder<ModelType, Bitmap>
{
    private final ModelLoader<ModelType, ParcelFileDescriptor> fileDescriptorModelLoader;
    private final Glide glide;
    private final RequestManager.OptionsApplier optionsApplier;
    private final ModelLoader<ModelType, InputStream> streamModelLoader;
    
    BitmapTypeRequest(final GenericRequestBuilder<ModelType, ?, ?, ?> genericRequestBuilder, final ModelLoader<ModelType, InputStream> streamModelLoader, final ModelLoader<ModelType, ParcelFileDescriptor> fileDescriptorModelLoader, final RequestManager.OptionsApplier optionsApplier) {
        super(buildProvider(genericRequestBuilder.glide, streamModelLoader, fileDescriptorModelLoader, Bitmap.class, null), Bitmap.class, genericRequestBuilder);
        this.streamModelLoader = streamModelLoader;
        this.fileDescriptorModelLoader = fileDescriptorModelLoader;
        this.glide = genericRequestBuilder.glide;
        this.optionsApplier = optionsApplier;
    }
    
    private static <A, R> FixedLoadProvider<A, ImageVideoWrapper, Bitmap, R> buildProvider(final Glide glide, final ModelLoader<A, InputStream> modelLoader, final ModelLoader<A, ParcelFileDescriptor> modelLoader2, final Class<R> clazz, final ResourceTranscoder<Bitmap, R> resourceTranscoder) {
        if (modelLoader == null && modelLoader2 == null) {
            return null;
        }
        ResourceTranscoder<Bitmap, R> buildTranscoder;
        if ((buildTranscoder = resourceTranscoder) == null) {
            buildTranscoder = glide.buildTranscoder(Bitmap.class, clazz);
        }
        return new FixedLoadProvider<A, ImageVideoWrapper, Bitmap, R>((ModelLoader<Object, Object>)new ImageVideoModelLoader<A>((ModelLoader<Object, InputStream>)modelLoader, (ModelLoader<Object, ParcelFileDescriptor>)modelLoader2), (ResourceTranscoder<Object, Object>)buildTranscoder, (DataLoadProvider<Object, Object>)glide.buildDataProvider(ImageVideoWrapper.class, Bitmap.class));
    }
    
    public BitmapRequestBuilder<ModelType, byte[]> toBytes() {
        return this.transcode(new BitmapBytesTranscoder(), byte[].class);
    }
    
    public BitmapRequestBuilder<ModelType, byte[]> toBytes(final Bitmap$CompressFormat bitmap$CompressFormat, final int n) {
        return this.transcode(new BitmapBytesTranscoder(bitmap$CompressFormat, n), byte[].class);
    }
    
    public <R> BitmapRequestBuilder<ModelType, R> transcode(final ResourceTranscoder<Bitmap, R> resourceTranscoder, final Class<R> clazz) {
        return this.optionsApplier.apply(new BitmapRequestBuilder<ModelType, R>(buildProvider(this.glide, this.streamModelLoader, this.fileDescriptorModelLoader, clazz, resourceTranscoder), clazz, (GenericRequestBuilder<ModelType, ?, ?, ?>)this));
    }
}
