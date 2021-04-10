package com.bumptech.glide.provider;

import java.io.*;
import com.bumptech.glide.load.*;
import com.bumptech.glide.load.resource.transcode.*;
import com.bumptech.glide.load.model.*;

public class ChildLoadProvider<A, T, Z, R> implements LoadProvider<A, T, Z, R>, Cloneable
{
    private ResourceDecoder<File, Z> cacheDecoder;
    private ResourceEncoder<Z> encoder;
    private final LoadProvider<A, T, Z, R> parent;
    private ResourceDecoder<T, Z> sourceDecoder;
    private Encoder<T> sourceEncoder;
    private ResourceTranscoder<Z, R> transcoder;
    
    public ChildLoadProvider(final LoadProvider<A, T, Z, R> parent) {
        this.parent = parent;
    }
    
    public ChildLoadProvider<A, T, Z, R> clone() {
        try {
            return (ChildLoadProvider)super.clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public ResourceDecoder<File, Z> getCacheDecoder() {
        if (this.cacheDecoder != null) {
            return this.cacheDecoder;
        }
        return this.parent.getCacheDecoder();
    }
    
    @Override
    public ResourceEncoder<Z> getEncoder() {
        if (this.encoder != null) {
            return this.encoder;
        }
        return this.parent.getEncoder();
    }
    
    @Override
    public ModelLoader<A, T> getModelLoader() {
        return this.parent.getModelLoader();
    }
    
    @Override
    public ResourceDecoder<T, Z> getSourceDecoder() {
        if (this.sourceDecoder != null) {
            return this.sourceDecoder;
        }
        return this.parent.getSourceDecoder();
    }
    
    @Override
    public Encoder<T> getSourceEncoder() {
        if (this.sourceEncoder != null) {
            return this.sourceEncoder;
        }
        return this.parent.getSourceEncoder();
    }
    
    @Override
    public ResourceTranscoder<Z, R> getTranscoder() {
        if (this.transcoder != null) {
            return this.transcoder;
        }
        return this.parent.getTranscoder();
    }
    
    public void setCacheDecoder(final ResourceDecoder<File, Z> cacheDecoder) {
        this.cacheDecoder = cacheDecoder;
    }
    
    public void setEncoder(final ResourceEncoder<Z> encoder) {
        this.encoder = encoder;
    }
    
    public void setSourceDecoder(final ResourceDecoder<T, Z> sourceDecoder) {
        this.sourceDecoder = sourceDecoder;
    }
    
    public void setSourceEncoder(final Encoder<T> sourceEncoder) {
        this.sourceEncoder = sourceEncoder;
    }
    
    public void setTranscoder(final ResourceTranscoder<Z, R> transcoder) {
        this.transcoder = transcoder;
    }
}
