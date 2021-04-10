package com.bumptech.glide.load.resource.bitmap;

import com.bumptech.glide.load.resource.drawable.*;
import android.content.res.*;
import android.view.*;
import android.graphics.drawable.*;
import android.graphics.*;

public class GlideBitmapDrawable extends GlideDrawable
{
    private boolean applyGravity;
    private final Rect destRect;
    private int height;
    private boolean mutated;
    private BitmapState state;
    private int width;
    
    public GlideBitmapDrawable(final Resources resources, final Bitmap bitmap) {
        this(resources, new BitmapState(bitmap));
    }
    
    GlideBitmapDrawable(final Resources resources, final BitmapState state) {
        this.destRect = new Rect();
        if (state == null) {
            throw new NullPointerException("BitmapState must not be null");
        }
        this.state = state;
        int targetDensity;
        if (resources != null) {
            targetDensity = resources.getDisplayMetrics().densityDpi;
            if (targetDensity == 0) {
                targetDensity = 160;
            }
            state.targetDensity = targetDensity;
        }
        else {
            targetDensity = state.targetDensity;
        }
        this.width = state.bitmap.getScaledWidth(targetDensity);
        this.height = state.bitmap.getScaledHeight(targetDensity);
    }
    
    public void draw(final Canvas canvas) {
        if (this.applyGravity) {
            Gravity.apply(119, this.width, this.height, this.getBounds(), this.destRect);
            this.applyGravity = false;
        }
        canvas.drawBitmap(this.state.bitmap, (Rect)null, this.destRect, this.state.paint);
    }
    
    public Bitmap getBitmap() {
        return this.state.bitmap;
    }
    
    public Drawable$ConstantState getConstantState() {
        return this.state;
    }
    
    public int getIntrinsicHeight() {
        return this.height;
    }
    
    public int getIntrinsicWidth() {
        return this.width;
    }
    
    public int getOpacity() {
        final Bitmap bitmap = this.state.bitmap;
        if (bitmap != null && !bitmap.hasAlpha() && this.state.paint.getAlpha() >= 255) {
            return -1;
        }
        return -3;
    }
    
    @Override
    public boolean isAnimated() {
        return false;
    }
    
    public boolean isRunning() {
        return false;
    }
    
    public Drawable mutate() {
        if (!this.mutated && super.mutate() == this) {
            this.state = new BitmapState(this.state);
            this.mutated = true;
        }
        return this;
    }
    
    protected void onBoundsChange(final Rect rect) {
        super.onBoundsChange(rect);
        this.applyGravity = true;
    }
    
    public void setAlpha(final int alpha) {
        if (this.state.paint.getAlpha() != alpha) {
            this.state.setAlpha(alpha);
            this.invalidateSelf();
        }
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        this.state.setColorFilter(colorFilter);
        this.invalidateSelf();
    }
    
    @Override
    public void setLoopCount(final int n) {
    }
    
    public void start() {
    }
    
    public void stop() {
    }
    
    static class BitmapState extends Drawable$ConstantState
    {
        private static final Paint DEFAULT_PAINT;
        private static final int DEFAULT_PAINT_FLAGS = 6;
        private static final int GRAVITY = 119;
        final Bitmap bitmap;
        Paint paint;
        int targetDensity;
        
        static {
            DEFAULT_PAINT = new Paint(6);
        }
        
        public BitmapState(final Bitmap bitmap) {
            this.paint = BitmapState.DEFAULT_PAINT;
            this.bitmap = bitmap;
        }
        
        BitmapState(final BitmapState bitmapState) {
            this(bitmapState.bitmap);
            this.targetDensity = bitmapState.targetDensity;
        }
        
        public int getChangingConfigurations() {
            return 0;
        }
        
        void mutatePaint() {
            if (BitmapState.DEFAULT_PAINT == this.paint) {
                this.paint = new Paint(6);
            }
        }
        
        public Drawable newDrawable() {
            return new GlideBitmapDrawable(null, this);
        }
        
        public Drawable newDrawable(final Resources resources) {
            return new GlideBitmapDrawable(resources, this);
        }
        
        void setAlpha(final int alpha) {
            this.mutatePaint();
            this.paint.setAlpha(alpha);
        }
        
        void setColorFilter(final ColorFilter colorFilter) {
            this.mutatePaint();
            this.paint.setColorFilter(colorFilter);
        }
    }
}
