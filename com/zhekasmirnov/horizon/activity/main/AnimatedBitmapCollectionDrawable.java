package com.zhekasmirnov.horizon.activity.main;

import android.graphics.drawable.*;
import java.util.*;
import android.graphics.*;
import android.support.annotation.*;

public class AnimatedBitmapCollectionDrawable extends Drawable
{
    private static final Paint paint;
    private List<Bitmap> allBitmaps;
    private int changeInterval;
    private int changeTime;
    private Bitmap lastBitmap;
    private Bitmap currentBitmap;
    private Rect lastRect;
    private Rect currentRect;
    private boolean isFading;
    private long fadeStartTime;
    private float fade;
    private int bitmapIndex;
    private float movementSpeed;
    private float xPadding;
    private float yPadding;
    private boolean integerScale;
    
    public AnimatedBitmapCollectionDrawable(final Collection<Bitmap> bitmaps, final int changeInterval, final int changeTime) {
        this.allBitmaps = new ArrayList<Bitmap>();
        this.isFading = false;
        this.fadeStartTime = -1L;
        this.fade = 0.0f;
        this.bitmapIndex = 0;
        this.movementSpeed = 0.0f;
        this.xPadding = 0.0f;
        this.yPadding = 0.0f;
        this.integerScale = false;
        this.allBitmaps.addAll(bitmaps);
        this.changeInterval = changeInterval;
        this.changeTime = changeTime;
    }
    
    public void setCurrentBitmap(final Bitmap bitmap) {
        this.lastBitmap = this.currentBitmap;
        this.lastRect = this.currentRect;
        this.currentBitmap = bitmap;
        if (bitmap != null) {
            this.currentRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        }
        else {
            this.currentRect = null;
        }
    }
    
    private RectF getScaledRect(final Canvas canvas, final Rect rect, final double xPadding, final double yPadding) {
        final int width = canvas.getWidth();
        final int height = canvas.getHeight();
        double scale;
        if (this.integerScale) {
            scale = Math.max((int)Math.ceil((width + xPadding * 2.0) / rect.width()), (int)Math.ceil((height + yPadding * 2.0) / rect.height()));
        }
        else {
            scale = Math.max((width + xPadding * 2.0) / rect.width(), (height + yPadding * 2.0) / rect.height());
        }
        final RectF result = new RectF((float)(int)(-rect.width() * scale / 2.0), (float)(int)(-rect.height() * scale / 2.0), (float)(int)(rect.width() * scale / 2.0), (float)(int)(rect.height() * scale / 2.0));
        result.offset(width / 2.0f, height / 2.0f);
        return result;
    }
    
    private void update() {
        if (this.allBitmaps.size() > 0) {
            final long time = System.currentTimeMillis();
            if (this.isFading) {
                this.fade = (time - this.fadeStartTime) / (float)this.changeTime;
                if (this.fade > 1.0f) {
                    this.fade = 1.0f;
                    this.isFading = false;
                }
            }
            else if (time > this.fadeStartTime + this.changeInterval) {
                this.isFading = true;
                this.fadeStartTime = time;
                this.bitmapIndex = (this.bitmapIndex + 1) % this.allBitmaps.size();
                this.setCurrentBitmap(this.allBitmaps.get(this.bitmapIndex));
                this.fade = 0.0f;
            }
        }
    }
    
    private static float cycle(final double cycle) {
        final double time = System.currentTimeMillis() * cycle;
        final float fract = (float)(time - Math.floor(time));
        return ((int)time % 2 == 0) ? fract : (1.0f - fract);
    }
    
    private static void move(final RectF rect, final double frequency, final double xRad, final double yRad) {
        final double time = System.currentTimeMillis() * frequency;
        rect.offset((float)Math.cos(time) * (float)xRad, (float)Math.sin(time) * (float)yRad);
    }
    
    public void setAnimationParameters(final float movementSpeed, final float xPadding, final float yPadding, final boolean integerScale) {
        this.movementSpeed = movementSpeed;
        this.xPadding = xPadding;
        this.yPadding = yPadding;
        this.integerScale = integerScale;
    }
    
    private void drawBitmap(final Canvas canvas, final Rect rect, final Bitmap bitmap, int alpha) {
        if (rect == null || bitmap == null) {
            return;
        }
        alpha = Math.max(0, Math.min(255, alpha));
        if (alpha == 0) {
            return;
        }
        final Paint alphaPaint = new Paint(AnimatedBitmapCollectionDrawable.paint);
        alphaPaint.setAlpha(alpha);
        final float padding = 50.0f;
        final RectF scaled = this.getScaledRect(canvas, rect, this.xPadding, this.yPadding);
        move(scaled, this.movementSpeed, this.xPadding, this.yPadding);
        canvas.drawBitmap(bitmap, rect, scaled, alphaPaint);
    }
    
    public void draw(@NonNull final Canvas canvas) {
        final int fade = (int)(this.fade * 255.0f);
        this.drawBitmap(canvas, this.lastRect, this.lastBitmap, 255);
        this.drawBitmap(canvas, this.currentRect, this.currentBitmap, fade);
        this.update();
        this.invalidateSelf();
    }
    
    public void setAlpha(final int alpha) {
    }
    
    public void setColorFilter(@Nullable final ColorFilter colorFilter) {
    }
    
    public int getOpacity() {
        return -1;
    }
    
    static {
        (paint = new Paint()).setFilterBitmap(false);
        AnimatedBitmapCollectionDrawable.paint.setAntiAlias(true);
    }
}
