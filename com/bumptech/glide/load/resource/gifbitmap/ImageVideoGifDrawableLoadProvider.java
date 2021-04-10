package com.bumptech.glide.load.resource.gifbitmap;

import com.bumptech.glide.provider.*;
import com.bumptech.glide.load.model.*;
import com.bumptech.glide.load.*;
import android.graphics.*;
import java.io.*;
import com.bumptech.glide.load.resource.gif.*;
import com.bumptech.glide.load.engine.bitmap_recycle.*;
import com.bumptech.glide.load.resource.file.*;

public class ImageVideoGifDrawableLoadProvider implements DataLoadProvider<ImageVideoWrapper, GifBitmapWrapper>
{
    private final ResourceDecoder<File, GifBitmapWrapper> cacheDecoder;
    private final ResourceEncoder<GifBitmapWrapper> encoder;
    private final ResourceDecoder<ImageVideoWrapper, GifBitmapWrapper> sourceDecoder;
    private final Encoder<ImageVideoWrapper> sourceEncoder;
    
    public ImageVideoGifDrawableLoadProvider(final DataLoadProvider<ImageVideoWrapper, Bitmap> dataLoadProvider, final DataLoadProvider<InputStream, GifDrawable> dataLoadProvider2, final BitmapPool bitmapPool) {
        final GifBitmapWrapperResourceDecoder sourceDecoder = new GifBitmapWrapperResourceDecoder(dataLoadProvider.getSourceDecoder(), dataLoadProvider2.getSourceDecoder(), bitmapPool);
        this.cacheDecoder = new FileToStreamDecoder<GifBitmapWrapper>((ResourceDecoder<InputStream, Object>)new GifBitmapWrapperStreamResourceDecoder(sourceDecoder));
        this.sourceDecoder = sourceDecoder;
        this.encoder = new GifBitmapWrapperResourceEncoder(dataLoadProvider.getEncoder(), dataLoadProvider2.getEncoder());
        this.sourceEncoder = dataLoadProvider.getSourceEncoder();
    }
    
    @Override
    public ResourceDecoder<File, GifBitmapWrapper> getCacheDecoder() {
        return this.cacheDecoder;
    }
    
    @Override
    public ResourceEncoder<GifBitmapWrapper> getEncoder() {
        return this.encoder;
    }
    
    @Override
    public ResourceDecoder<ImageVideoWrapper, GifBitmapWrapper> getSourceDecoder() {
        return this.sourceDecoder;
    }
    
    @Override
    public Encoder<ImageVideoWrapper> getSourceEncoder() {
        return this.sourceEncoder;
    }
}
