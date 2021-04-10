package com.bumptech.glide.provider;

import com.bumptech.glide.load.model.*;
import com.bumptech.glide.load.resource.transcode.*;
import java.io.*;
import com.bumptech.glide.load.*;

public class FixedLoadProvider<A, T, Z, R> implements LoadProvider<A, T, Z, R>
{
    private final DataLoadProvider<T, Z> dataLoadProvider;
    private final ModelLoader<A, T> modelLoader;
    private final ResourceTranscoder<Z, R> transcoder;
    
    public FixedLoadProvider(final ModelLoader<A, T> modelLoader, final ResourceTranscoder<Z, R> transcoder, final DataLoadProvider<T, Z> dataLoadProvider) {
        if (modelLoader == null) {
            throw new NullPointerException("ModelLoader must not be null");
        }
        this.modelLoader = modelLoader;
        if (transcoder == null) {
            throw new NullPointerException("Transcoder must not be null");
        }
        this.transcoder = transcoder;
        if (dataLoadProvider == null) {
            throw new NullPointerException("DataLoadProvider must not be null");
        }
        this.dataLoadProvider = dataLoadProvider;
    }
    
    @Override
    public ResourceDecoder<File, Z> getCacheDecoder() {
        return this.dataLoadProvider.getCacheDecoder();
    }
    
    @Override
    public ResourceEncoder<Z> getEncoder() {
        return this.dataLoadProvider.getEncoder();
    }
    
    @Override
    public ModelLoader<A, T> getModelLoader() {
        return this.modelLoader;
    }
    
    @Override
    public ResourceDecoder<T, Z> getSourceDecoder() {
        return this.dataLoadProvider.getSourceDecoder();
    }
    
    @Override
    public Encoder<T> getSourceEncoder() {
        return this.dataLoadProvider.getSourceEncoder();
    }
    
    @Override
    public ResourceTranscoder<Z, R> getTranscoder() {
        return this.transcoder;
    }
}
