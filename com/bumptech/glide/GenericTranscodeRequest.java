package com.bumptech.glide;

import com.bumptech.glide.load.model.*;
import android.content.*;
import com.bumptech.glide.manager.*;
import com.bumptech.glide.load.resource.transcode.*;
import java.io.*;
import com.bumptech.glide.provider.*;
import com.bumptech.glide.load.engine.*;
import com.bumptech.glide.request.*;
import com.bumptech.glide.request.target.*;

public class GenericTranscodeRequest<ModelType, DataType, ResourceType> extends GenericRequestBuilder<ModelType, DataType, ResourceType, ResourceType> implements DownloadOptions
{
    private final Class<DataType> dataClass;
    private final ModelLoader<ModelType, DataType> modelLoader;
    private final RequestManager.OptionsApplier optionsApplier;
    private final Class<ResourceType> resourceClass;
    
    GenericTranscodeRequest(final Context context, final Glide glide, final Class<ModelType> clazz, final ModelLoader<ModelType, DataType> modelLoader, final Class<DataType> dataClass, final Class<ResourceType> resourceClass, final RequestTracker requestTracker, final Lifecycle lifecycle, final RequestManager.OptionsApplier optionsApplier) {
        super(context, clazz, build(glide, modelLoader, dataClass, (Class<Object>)resourceClass, UnitTranscoder.get()), (Class<Object>)resourceClass, glide, requestTracker, lifecycle);
        this.modelLoader = modelLoader;
        this.dataClass = dataClass;
        this.resourceClass = resourceClass;
        this.optionsApplier = optionsApplier;
    }
    
    GenericTranscodeRequest(final Class<ResourceType> clazz, final GenericRequestBuilder<ModelType, ?, ?, ?> genericRequestBuilder, final ModelLoader<ModelType, DataType> modelLoader, final Class<DataType> dataClass, final Class<ResourceType> resourceClass, final RequestManager.OptionsApplier optionsApplier) {
        super(build(genericRequestBuilder.glide, modelLoader, dataClass, (Class<Object>)resourceClass, UnitTranscoder.get()), (Class<Object>)clazz, genericRequestBuilder);
        this.modelLoader = modelLoader;
        this.dataClass = dataClass;
        this.resourceClass = resourceClass;
        this.optionsApplier = optionsApplier;
    }
    
    private static <A, T, Z, R> LoadProvider<A, T, Z, R> build(final Glide glide, final ModelLoader<A, T> modelLoader, final Class<T> clazz, final Class<Z> clazz2, final ResourceTranscoder<Z, R> resourceTranscoder) {
        return new FixedLoadProvider<A, T, Z, R>(modelLoader, resourceTranscoder, glide.buildDataProvider(clazz, clazz2));
    }
    
    private GenericRequestBuilder<ModelType, DataType, File, File> getDownloadOnlyRequest() {
        return (GenericRequestBuilder<ModelType, DataType, File, File>)this.optionsApplier.apply(new GenericRequestBuilder<Object, Object, Object, File>(new FixedLoadProvider<Object, Object, Object, File>((ModelLoader<Object, Object>)this.modelLoader, (ResourceTranscoder<Object, File>)UnitTranscoder.get(), (DataLoadProvider<Object, Object>)this.glide.buildDataProvider(this.dataClass, File.class)), File.class, (GenericRequestBuilder<Object, ?, ?, ?>)this)).priority(Priority.LOW).diskCacheStrategy(DiskCacheStrategy.SOURCE).skipMemoryCache(true);
    }
    
    @Override
    public FutureTarget<File> downloadOnly(final int n, final int n2) {
        return this.getDownloadOnlyRequest().into(n, n2);
    }
    
    @Override
    public <Y extends Target<File>> Y downloadOnly(final Y y) {
        return this.getDownloadOnlyRequest().into(y);
    }
    
    public <TranscodeType> GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType> transcode(final ResourceTranscoder<ResourceType, TranscodeType> resourceTranscoder, final Class<TranscodeType> clazz) {
        return this.optionsApplier.apply(new GenericRequestBuilder<ModelType, DataType, ResourceType, TranscodeType>(build(this.glide, this.modelLoader, this.dataClass, this.resourceClass, resourceTranscoder), clazz, this));
    }
}
