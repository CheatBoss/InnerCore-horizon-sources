package com.bumptech.glide.load.resource.gif;

import com.bumptech.glide.gifdecoder.*;
import android.graphics.*;
import android.content.*;
import com.bumptech.glide.*;
import com.bumptech.glide.load.engine.bitmap_recycle.*;
import com.bumptech.glide.load.model.*;
import com.bumptech.glide.load.resource.*;
import com.bumptech.glide.load.engine.*;
import com.bumptech.glide.load.*;
import com.bumptech.glide.request.target.*;
import com.bumptech.glide.request.animation.*;
import android.os.*;
import java.util.*;
import java.security.*;
import java.io.*;

class GifFrameLoader
{
    private final FrameCallback callback;
    private DelayTarget current;
    private final GifDecoder gifDecoder;
    private final Handler handler;
    private boolean isCleared;
    private boolean isLoadPending;
    private boolean isRunning;
    private GenericRequestBuilder<GifDecoder, GifDecoder, Bitmap, Bitmap> requestBuilder;
    
    public GifFrameLoader(final Context context, final FrameCallback frameCallback, final GifDecoder gifDecoder, final int n, final int n2) {
        this(frameCallback, gifDecoder, null, getRequestBuilder(context, gifDecoder, n, n2, Glide.get(context).getBitmapPool()));
    }
    
    GifFrameLoader(final FrameCallback callback, final GifDecoder gifDecoder, final Handler handler, final GenericRequestBuilder<GifDecoder, GifDecoder, Bitmap, Bitmap> requestBuilder) {
        this.isRunning = false;
        this.isLoadPending = false;
        Handler handler2 = handler;
        if (handler == null) {
            handler2 = new Handler(Looper.getMainLooper(), (Handler$Callback)new FrameLoaderCallback());
        }
        this.callback = callback;
        this.gifDecoder = gifDecoder;
        this.handler = handler2;
        this.requestBuilder = requestBuilder;
    }
    
    private static GenericRequestBuilder<GifDecoder, GifDecoder, Bitmap, Bitmap> getRequestBuilder(final Context context, final GifDecoder gifDecoder, final int n, final int n2, final BitmapPool bitmapPool) {
        return (GenericRequestBuilder<GifDecoder, GifDecoder, Bitmap, Bitmap>)Glide.with(context).using((ModelLoader<GifDecoder, GifDecoder>)new GifFrameModelLoader(), GifDecoder.class).load(gifDecoder).as(Bitmap.class).sourceEncoder(NullEncoder.get()).decoder((ResourceDecoder<DataType, Bitmap>)new GifFrameResourceDecoder(bitmapPool)).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).override(n, n2);
    }
    
    private void loadNextFrame() {
        if (!this.isRunning) {
            return;
        }
        if (this.isLoadPending) {
            return;
        }
        this.isLoadPending = true;
        this.gifDecoder.advance();
        this.requestBuilder.signature(new FrameSignature()).into(new DelayTarget(this.handler, this.gifDecoder.getCurrentFrameIndex(), SystemClock.uptimeMillis() + this.gifDecoder.getNextDelay()));
    }
    
    public void clear() {
        this.stop();
        if (this.current != null) {
            Glide.clear(this.current);
            this.current = null;
        }
        this.isCleared = true;
    }
    
    public Bitmap getCurrentFrame() {
        if (this.current != null) {
            return this.current.getResource();
        }
        return null;
    }
    
    void onFrameReady(final DelayTarget current) {
        if (this.isCleared) {
            this.handler.obtainMessage(2, (Object)current).sendToTarget();
            return;
        }
        final DelayTarget current2 = this.current;
        this.current = current;
        this.callback.onFrameReady(current.index);
        if (current2 != null) {
            this.handler.obtainMessage(2, (Object)current2).sendToTarget();
        }
        this.isLoadPending = false;
        this.loadNextFrame();
    }
    
    public void setFrameTransformation(final Transformation<Bitmap> transformation) {
        if (transformation == null) {
            throw new NullPointerException("Transformation must not be null");
        }
        this.requestBuilder = this.requestBuilder.transform(transformation);
    }
    
    public void start() {
        if (this.isRunning) {
            return;
        }
        this.isRunning = true;
        this.isCleared = false;
        this.loadNextFrame();
    }
    
    public void stop() {
        this.isRunning = false;
    }
    
    static class DelayTarget extends SimpleTarget<Bitmap>
    {
        private final Handler handler;
        private final int index;
        private Bitmap resource;
        private final long targetTime;
        
        public DelayTarget(final Handler handler, final int index, final long targetTime) {
            this.handler = handler;
            this.index = index;
            this.targetTime = targetTime;
        }
        
        public Bitmap getResource() {
            return this.resource;
        }
        
        @Override
        public void onResourceReady(final Bitmap resource, final GlideAnimation<? super Bitmap> glideAnimation) {
            this.resource = resource;
            this.handler.sendMessageAtTime(this.handler.obtainMessage(1, (Object)this), this.targetTime);
        }
    }
    
    public interface FrameCallback
    {
        void onFrameReady(final int p0);
    }
    
    private class FrameLoaderCallback implements Handler$Callback
    {
        public static final int MSG_CLEAR = 2;
        public static final int MSG_DELAY = 1;
        
        public boolean handleMessage(final Message message) {
            if (message.what == 1) {
                GifFrameLoader.this.onFrameReady((DelayTarget)message.obj);
                return true;
            }
            if (message.what == 2) {
                Glide.clear((Target<?>)message.obj);
            }
            return false;
        }
    }
    
    static class FrameSignature implements Key
    {
        private final UUID uuid;
        
        public FrameSignature() {
            this(UUID.randomUUID());
        }
        
        FrameSignature(final UUID uuid) {
            this.uuid = uuid;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof FrameSignature && ((FrameSignature)o).uuid.equals(this.uuid);
        }
        
        @Override
        public int hashCode() {
            return this.uuid.hashCode();
        }
        
        @Override
        public void updateDiskCacheKey(final MessageDigest messageDigest) throws UnsupportedEncodingException {
            throw new UnsupportedOperationException("Not implemented");
        }
    }
}
