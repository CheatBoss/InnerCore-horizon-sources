package com.bumptech.glide.load.resource.gif;

import com.bumptech.glide.load.engine.bitmap_recycle.*;
import com.bumptech.glide.gifdecoder.*;
import android.graphics.*;
import com.bumptech.glide.load.*;
import com.bumptech.glide.load.engine.*;
import android.util.*;
import java.io.*;
import com.bumptech.glide.util.*;
import com.bumptech.glide.load.resource.*;
import com.bumptech.glide.gifencoder.*;
import com.bumptech.glide.load.resource.bitmap.*;

public class GifResourceEncoder implements ResourceEncoder<GifDrawable>
{
    private static final Factory FACTORY;
    private static final String TAG = "GifEncoder";
    private final BitmapPool bitmapPool;
    private final Factory factory;
    private final GifDecoder.BitmapProvider provider;
    
    static {
        FACTORY = new Factory();
    }
    
    public GifResourceEncoder(final BitmapPool bitmapPool) {
        this(bitmapPool, GifResourceEncoder.FACTORY);
    }
    
    GifResourceEncoder(final BitmapPool bitmapPool, final Factory factory) {
        this.bitmapPool = bitmapPool;
        this.provider = new GifBitmapProvider(bitmapPool);
        this.factory = factory;
    }
    
    private GifDecoder decodeHeaders(final byte[] data) {
        final GifHeaderParser buildParser = this.factory.buildParser();
        buildParser.setData(data);
        final GifHeader header = buildParser.parseHeader();
        final GifDecoder buildDecoder = this.factory.buildDecoder(this.provider);
        buildDecoder.setData(header, data);
        buildDecoder.advance();
        return buildDecoder;
    }
    
    private Resource<Bitmap> getTransformedFrame(final Bitmap bitmap, final Transformation<Bitmap> transformation, final GifDrawable gifDrawable) {
        final Resource<Bitmap> buildFrameResource = this.factory.buildFrameResource(bitmap, this.bitmapPool);
        final Resource<Bitmap> transform = transformation.transform(buildFrameResource, gifDrawable.getIntrinsicWidth(), gifDrawable.getIntrinsicHeight());
        if (!buildFrameResource.equals(transform)) {
            buildFrameResource.recycle();
        }
        return transform;
    }
    
    private boolean writeDataDirect(final byte[] array, final OutputStream outputStream) {
        try {
            outputStream.write(array);
            return true;
        }
        catch (IOException ex) {
            if (Log.isLoggable("GifEncoder", 3)) {
                Log.d("GifEncoder", "Failed to write data to output stream in GifResourceEncoder", (Throwable)ex);
            }
            return false;
        }
    }
    
    @Override
    public boolean encode(final Resource<GifDrawable> resource, OutputStream transformedFrame) {
        final long logTime = LogTime.getLogTime();
        final GifDrawable gifDrawable = resource.get();
        final Transformation<Bitmap> frameTransformation = gifDrawable.getFrameTransformation();
        if (frameTransformation instanceof UnitTransformation) {
            return this.writeDataDirect(gifDrawable.getData(), transformedFrame);
        }
        final GifDecoder decodeHeaders = this.decodeHeaders(gifDrawable.getData());
        final AnimatedGifEncoder buildEncoder = this.factory.buildEncoder();
        if (!buildEncoder.start(transformedFrame)) {
            return false;
        }
        int i = 0;
        while (i < decodeHeaders.getFrameCount()) {
            transformedFrame = (OutputStream)this.getTransformedFrame(decodeHeaders.getNextFrame(), frameTransformation, gifDrawable);
            try {
                if (!buildEncoder.addFrame(((Resource<Bitmap>)transformedFrame).get())) {
                    return false;
                }
                buildEncoder.setDelay(decodeHeaders.getDelay(decodeHeaders.getCurrentFrameIndex()));
                decodeHeaders.advance();
                ((Resource)transformedFrame).recycle();
                ++i;
                continue;
            }
            finally {
                ((Resource)transformedFrame).recycle();
            }
            break;
        }
        final boolean finish = buildEncoder.finish();
        if (Log.isLoggable("GifEncoder", 2)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Encoded gif with ");
            sb.append(decodeHeaders.getFrameCount());
            sb.append(" frames and ");
            sb.append(gifDrawable.getData().length);
            sb.append(" bytes in ");
            sb.append(LogTime.getElapsedMillis(logTime));
            sb.append(" ms");
            Log.v("GifEncoder", sb.toString());
        }
        return finish;
    }
    
    @Override
    public String getId() {
        return "";
    }
    
    static class Factory
    {
        public GifDecoder buildDecoder(final GifDecoder.BitmapProvider bitmapProvider) {
            return new GifDecoder(bitmapProvider);
        }
        
        public AnimatedGifEncoder buildEncoder() {
            return new AnimatedGifEncoder();
        }
        
        public Resource<Bitmap> buildFrameResource(final Bitmap bitmap, final BitmapPool bitmapPool) {
            return new BitmapResource(bitmap, bitmapPool);
        }
        
        public GifHeaderParser buildParser() {
            return new GifHeaderParser();
        }
    }
}
