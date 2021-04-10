package com.bumptech.glide;

import com.bumptech.glide.load.model.*;
import java.io.*;
import com.bumptech.glide.load.resource.gif.*;
import com.bumptech.glide.provider.*;
import com.bumptech.glide.load.resource.transcode.*;

public class GifTypeRequest<ModelType> extends GifRequestBuilder<ModelType>
{
    private final RequestManager.OptionsApplier optionsApplier;
    private final ModelLoader<ModelType, InputStream> streamModelLoader;
    
    GifTypeRequest(final GenericRequestBuilder<ModelType, ?, ?, ?> genericRequestBuilder, final ModelLoader<ModelType, InputStream> streamModelLoader, final RequestManager.OptionsApplier optionsApplier) {
        super(buildProvider(genericRequestBuilder.glide, streamModelLoader, GifDrawable.class, null), GifDrawable.class, genericRequestBuilder);
        this.streamModelLoader = streamModelLoader;
        this.optionsApplier = optionsApplier;
        this.crossFade();
    }
    
    private static <A, R> FixedLoadProvider<A, InputStream, GifDrawable, R> buildProvider(final Glide glide, final ModelLoader<A, InputStream> modelLoader, final Class<R> clazz, final ResourceTranscoder<GifDrawable, R> resourceTranscoder) {
        if (modelLoader == null) {
            return null;
        }
        ResourceTranscoder<GifDrawable, R> buildTranscoder;
        if ((buildTranscoder = resourceTranscoder) == null) {
            buildTranscoder = glide.buildTranscoder(GifDrawable.class, clazz);
        }
        return new FixedLoadProvider<A, InputStream, GifDrawable, R>((ModelLoader<Object, Object>)modelLoader, (ResourceTranscoder<Object, Object>)buildTranscoder, (DataLoadProvider<Object, Object>)glide.buildDataProvider(InputStream.class, GifDrawable.class));
    }
    
    public GenericRequestBuilder<ModelType, InputStream, GifDrawable, byte[]> toBytes() {
        return this.transcode(new GifDrawableBytesTranscoder(), byte[].class);
    }
    
    public <R> GenericRequestBuilder<ModelType, InputStream, GifDrawable, R> transcode(final ResourceTranscoder<GifDrawable, R> resourceTranscoder, final Class<R> clazz) {
        return this.optionsApplier.apply(new GenericRequestBuilder<ModelType, InputStream, GifDrawable, R>(buildProvider(this.glide, this.streamModelLoader, clazz, resourceTranscoder), clazz, (GenericRequestBuilder<ModelType, ?, ?, ?>)this));
    }
}
