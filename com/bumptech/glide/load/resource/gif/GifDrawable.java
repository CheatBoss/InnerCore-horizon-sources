package com.bumptech.glide.load.resource.gif;

import com.bumptech.glide.load.resource.drawable.*;
import android.content.*;
import com.bumptech.glide.load.engine.bitmap_recycle.*;
import com.bumptech.glide.load.*;
import com.bumptech.glide.gifdecoder.*;
import android.view.*;
import android.os.*;
import android.annotation.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.content.res.*;

public class GifDrawable extends GlideDrawable implements FrameCallback
{
    private boolean applyGravity;
    private final GifDecoder decoder;
    private final Rect destRect;
    private final GifFrameLoader frameLoader;
    private boolean isRecycled;
    private boolean isRunning;
    private boolean isStarted;
    private boolean isVisible;
    private int loopCount;
    private int maxLoopCount;
    private final Paint paint;
    private final GifState state;
    
    public GifDrawable(final Context context, final GifDecoder.BitmapProvider bitmapProvider, final BitmapPool bitmapPool, final Transformation<Bitmap> transformation, final int n, final int n2, final GifHeader gifHeader, final byte[] array, final Bitmap bitmap) {
        this(new GifState(gifHeader, array, context, transformation, n, n2, bitmapProvider, bitmapPool, bitmap));
    }
    
    GifDrawable(final GifDecoder decoder, final GifFrameLoader frameLoader, final Bitmap firstFrame, final BitmapPool bitmapPool, final Paint paint) {
        this.destRect = new Rect();
        this.isVisible = true;
        this.maxLoopCount = -1;
        this.decoder = decoder;
        this.frameLoader = frameLoader;
        this.state = new GifState(null);
        this.paint = paint;
        this.state.bitmapPool = bitmapPool;
        this.state.firstFrame = firstFrame;
    }
    
    GifDrawable(final GifState state) {
        this.destRect = new Rect();
        this.isVisible = true;
        this.maxLoopCount = -1;
        if (state == null) {
            throw new NullPointerException("GifState must not be null");
        }
        this.state = state;
        this.decoder = new GifDecoder(state.bitmapProvider);
        this.paint = new Paint();
        this.decoder.setData(state.gifHeader, state.data);
        this.frameLoader = new GifFrameLoader(state.context, (GifFrameLoader.FrameCallback)this, this.decoder, state.targetWidth, state.targetHeight);
    }
    
    private void reset() {
        this.frameLoader.clear();
        this.invalidateSelf();
    }
    
    private void resetLoopCount() {
        this.loopCount = 0;
    }
    
    private void startRunning() {
        if (this.decoder.getFrameCount() == 1) {
            this.invalidateSelf();
            return;
        }
        if (!this.isRunning) {
            this.isRunning = true;
            this.frameLoader.start();
            this.invalidateSelf();
        }
    }
    
    private void stopRunning() {
        this.isRunning = false;
        this.frameLoader.stop();
    }
    
    public void draw(final Canvas canvas) {
        if (this.isRecycled) {
            return;
        }
        if (this.applyGravity) {
            Gravity.apply(119, this.getIntrinsicWidth(), this.getIntrinsicHeight(), this.getBounds(), this.destRect);
            this.applyGravity = false;
        }
        Bitmap bitmap = this.frameLoader.getCurrentFrame();
        if (bitmap == null) {
            bitmap = this.state.firstFrame;
        }
        canvas.drawBitmap(bitmap, (Rect)null, this.destRect, this.paint);
    }
    
    public Drawable$ConstantState getConstantState() {
        return this.state;
    }
    
    public byte[] getData() {
        return this.state.data;
    }
    
    public GifDecoder getDecoder() {
        return this.decoder;
    }
    
    public Bitmap getFirstFrame() {
        return this.state.firstFrame;
    }
    
    public int getFrameCount() {
        return this.decoder.getFrameCount();
    }
    
    public Transformation<Bitmap> getFrameTransformation() {
        return this.state.frameTransformation;
    }
    
    public int getIntrinsicHeight() {
        return this.state.firstFrame.getHeight();
    }
    
    public int getIntrinsicWidth() {
        return this.state.firstFrame.getWidth();
    }
    
    public int getOpacity() {
        return -2;
    }
    
    @Override
    public boolean isAnimated() {
        return true;
    }
    
    boolean isRecycled() {
        return this.isRecycled;
    }
    
    public boolean isRunning() {
        return this.isRunning;
    }
    
    protected void onBoundsChange(final Rect rect) {
        super.onBoundsChange(rect);
        this.applyGravity = true;
    }
    
    @TargetApi(11)
    @Override
    public void onFrameReady(final int n) {
        if (Build$VERSION.SDK_INT >= 11 && this.getCallback() == null) {
            this.stop();
            this.reset();
            return;
        }
        this.invalidateSelf();
        if (n == this.decoder.getFrameCount() - 1) {
            ++this.loopCount;
        }
        if (this.maxLoopCount != -1 && this.loopCount >= this.maxLoopCount) {
            this.stop();
        }
    }
    
    public void recycle() {
        this.isRecycled = true;
        this.state.bitmapPool.put(this.state.firstFrame);
        this.frameLoader.clear();
        this.frameLoader.stop();
    }
    
    public void setAlpha(final int alpha) {
        this.paint.setAlpha(alpha);
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
    }
    
    public void setFrameTransformation(final Transformation<Bitmap> transformation, final Bitmap firstFrame) {
        if (firstFrame == null) {
            throw new NullPointerException("The first frame of the GIF must not be null");
        }
        if (transformation == null) {
            throw new NullPointerException("The frame transformation must not be null");
        }
        this.state.frameTransformation = transformation;
        this.state.firstFrame = firstFrame;
        this.frameLoader.setFrameTransformation(transformation);
    }
    
    void setIsRunning(final boolean isRunning) {
        this.isRunning = isRunning;
    }
    
    @Override
    public void setLoopCount(final int maxLoopCount) {
        if (maxLoopCount <= 0 && maxLoopCount != -1 && maxLoopCount != 0) {
            throw new IllegalArgumentException("Loop count must be greater than 0, or equal to GlideDrawable.LOOP_FOREVER, or equal to GlideDrawable.LOOP_INTRINSIC");
        }
        if (maxLoopCount == 0) {
            this.maxLoopCount = this.decoder.getLoopCount();
            return;
        }
        this.maxLoopCount = maxLoopCount;
    }
    
    public boolean setVisible(final boolean isVisible, final boolean b) {
        if (!(this.isVisible = isVisible)) {
            this.stopRunning();
        }
        else if (this.isStarted) {
            this.startRunning();
        }
        return super.setVisible(isVisible, b);
    }
    
    public void start() {
        this.isStarted = true;
        this.resetLoopCount();
        if (this.isVisible) {
            this.startRunning();
        }
    }
    
    public void stop() {
        this.isStarted = false;
        this.stopRunning();
        if (Build$VERSION.SDK_INT < 11) {
            this.reset();
        }
    }
    
    static class GifState extends Drawable$ConstantState
    {
        private static final int GRAVITY = 119;
        BitmapPool bitmapPool;
        GifDecoder.BitmapProvider bitmapProvider;
        Context context;
        byte[] data;
        Bitmap firstFrame;
        Transformation<Bitmap> frameTransformation;
        GifHeader gifHeader;
        int targetHeight;
        int targetWidth;
        
        public GifState(final GifHeader gifHeader, final byte[] data, final Context context, final Transformation<Bitmap> frameTransformation, final int targetWidth, final int targetHeight, final GifDecoder.BitmapProvider bitmapProvider, final BitmapPool bitmapPool, final Bitmap firstFrame) {
            if (firstFrame == null) {
                throw new NullPointerException("The first frame of the GIF must not be null");
            }
            this.gifHeader = gifHeader;
            this.data = data;
            this.bitmapPool = bitmapPool;
            this.firstFrame = firstFrame;
            this.context = context.getApplicationContext();
            this.frameTransformation = frameTransformation;
            this.targetWidth = targetWidth;
            this.targetHeight = targetHeight;
            this.bitmapProvider = bitmapProvider;
        }
        
        public GifState(final GifState gifState) {
            if (gifState != null) {
                this.gifHeader = gifState.gifHeader;
                this.data = gifState.data;
                this.context = gifState.context;
                this.frameTransformation = gifState.frameTransformation;
                this.targetWidth = gifState.targetWidth;
                this.targetHeight = gifState.targetHeight;
                this.bitmapProvider = gifState.bitmapProvider;
                this.bitmapPool = gifState.bitmapPool;
                this.firstFrame = gifState.firstFrame;
            }
        }
        
        public int getChangingConfigurations() {
            return 0;
        }
        
        public Drawable newDrawable() {
            return new GifDrawable(this);
        }
        
        public Drawable newDrawable(final Resources resources) {
            return this.newDrawable();
        }
    }
}
