package com.zhekasmirnov.innercore.ui;

import android.graphics.drawable.*;
import android.os.*;
import android.view.*;
import com.zhekasmirnov.innercore.utils.*;
import android.graphics.*;
import android.support.annotation.*;

public class LoadingOverlayDrawable extends Drawable
{
    private Bitmap backgroundArtBitmap;
    private Bitmap glowBitmap;
    private float glowOffsetX;
    private float glowOffsetY;
    private final Paint glowPaint;
    private Handler handler;
    private boolean isInitialized;
    private long lastTime;
    private final View parent;
    private float progress;
    private final Paint progressBarPaint;
    private float progressTarget;
    private float scale;
    private String text;
    private float textOffsetX;
    private float textOffsetY;
    private final Paint textPaint;
    private String tip;
    private float tipOffsetY;
    private final Paint tipPaint;
    
    public LoadingOverlayDrawable(final View parent) {
        this.isInitialized = false;
        this.scale = 1.0f;
        this.text = "";
        this.tip = "";
        this.progressTarget = 0.0f;
        this.lastTime = 0L;
        this.parent = parent;
        this.handler = new Handler();
        (this.glowPaint = new Paint()).setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.ADD));
        (this.textPaint = new Paint()).setColor(-1);
        this.textPaint.setTextAlign(Paint$Align.CENTER);
        this.textPaint.setTypeface(FileTools.getMcTypeface());
        (this.tipPaint = new Paint(this.textPaint)).setColor(Color.rgb(200, 185, 240));
        (this.progressBarPaint = new Paint()).setColor(Color.rgb(120, 80, 190));
    }
    
    private static float addToTarget(final float n, float n2, final float n3) {
        if ((n2 -= n) > n3) {
            n2 = n3;
        }
        float n4 = n2;
        if (n2 < -n3) {
            n4 = -n3;
        }
        return n + n4;
    }
    
    private void initializeIfNeeded(final Canvas canvas) {
        if (this.lastTime == 0L) {
            this.lastTime = System.currentTimeMillis();
        }
        if (this.isInitialized) {
            return;
        }
        this.handler = new Handler();
        final Bitmap load = load("ic_loading_background_art");
        final Bitmap load2 = load("ic_loading_glow");
        if (load == null) {
            return;
        }
        if (load2 == null) {
            return;
        }
        final int width = canvas.getWidth();
        final int height = canvas.getHeight();
        final boolean b = width / (float)height > 1.7777778f;
        float n;
        float n2;
        if (b) {
            n = (float)width;
            n2 = 1920.0f;
        }
        else {
            n = (float)height;
            n2 = 1080.0f;
        }
        this.scale = n / n2;
        this.backgroundArtBitmap = Bitmap.createScaledBitmap(load, (int)(load.getWidth() * this.scale), (int)(load.getHeight() * this.scale), true);
        Bitmap backgroundArtBitmap = load;
        if (this.backgroundArtBitmap != load) {
            load.recycle();
            backgroundArtBitmap = this.backgroundArtBitmap;
        }
        while (true) {
            Label_0246: {
                if (!b) {
                    break Label_0246;
                }
                try {
                    this.glowOffsetX = this.scale * 760.0f;
                    this.glowOffsetY = this.scale * 220.0f - (backgroundArtBitmap.getHeight() - height) / 2;
                    this.backgroundArtBitmap = Bitmap.createBitmap(backgroundArtBitmap, 0, (backgroundArtBitmap.getHeight() - height) / 2, width, height);
                Label_0325_Outer:
                    while (true) {
                        if (this.backgroundArtBitmap != backgroundArtBitmap) {
                            backgroundArtBitmap.recycle();
                            final Bitmap backgroundArtBitmap2 = this.backgroundArtBitmap;
                        }
                        while (true) {
                            this.glowBitmap = Bitmap.createScaledBitmap(load2, (int)(load2.getWidth() * this.scale), (int)(load2.getHeight() * this.scale), true);
                            if (this.glowBitmap != load2) {
                                load2.recycle();
                                final Bitmap glowBitmap = this.glowBitmap;
                            }
                            this.textOffsetX = width * 0.5f;
                            this.textOffsetY = height * 0.75f;
                            this.textPaint.setTextSize(this.scale * 55.0f);
                            this.tipOffsetY = this.textOffsetY + this.scale * 85.0f;
                            this.tipPaint.setTextSize(this.scale * 40.0f);
                            this.isInitialized = true;
                            return;
                            final IllegalArgumentException ex;
                            ex.printStackTrace();
                            continue;
                        }
                        this.glowOffsetX = this.scale * 760.0f - (backgroundArtBitmap.getWidth() - width) / 2;
                        this.glowOffsetY = this.scale * 220.0f;
                        this.backgroundArtBitmap = Bitmap.createBitmap(backgroundArtBitmap, (backgroundArtBitmap.getWidth() - width) / 2, 0, width, height);
                        continue Label_0325_Outer;
                    }
                }
                catch (IllegalArgumentException ex2) {}
            }
            final IllegalArgumentException ex2;
            final IllegalArgumentException ex = ex2;
            continue;
        }
    }
    
    private static Bitmap load(final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append("innercore/ui/");
        sb.append(s);
        sb.append(".png");
        return FileTools.getAssetAsBitmap(sb.toString());
    }
    
    public void draw(@NonNull final Canvas canvas) {
        this.initializeIfNeeded(canvas);
        if (this.backgroundArtBitmap != null) {
            canvas.drawBitmap(this.backgroundArtBitmap, 0.0f, 0.0f, (Paint)null);
        }
        if (this.glowBitmap != null) {
            this.glowPaint.setAlpha((int)(80.0f * ((float)Math.sin(System.currentTimeMillis() * 0.0033) * 0.5f + 0.5f)));
            canvas.drawBitmap(this.glowBitmap, this.glowOffsetX, this.glowOffsetY, this.glowPaint);
        }
        if (this.text != null) {
            String s = this.text;
            if (this.text.endsWith("...")) {
                final int n = (int)((long)(System.currentTimeMillis() * 0.0033) % 3L);
                final StringBuilder sb = new StringBuilder();
                sb.append(this.text.substring(0, this.text.length() - 3));
                String s2;
                if (n == 0) {
                    s2 = ".";
                }
                else if (n == 1) {
                    s2 = "..";
                }
                else {
                    s2 = "...";
                }
                sb.append(s2);
                s = sb.toString();
            }
            canvas.drawText(s, this.textOffsetX, this.textOffsetY, this.textPaint);
        }
        if (this.tip != null) {
            canvas.drawText(this.tip, this.textOffsetX, this.tipOffsetY, this.tipPaint);
        }
        final long n2 = System.currentTimeMillis() - this.lastTime;
        this.lastTime += n2;
        this.progress = addToTarget(this.progress, this.progressTarget, n2 * 2.0E-4f);
        if (this.progress > 0.0f) {
            final int width = canvas.getWidth();
            final int height = canvas.getHeight();
            canvas.drawRect(0.0f, height - (float)Math.round(Math.min(20.0, Math.max(4.0, height * 0.008))), width * this.progress, (float)height, this.progressBarPaint);
        }
        this.handler.postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                LoadingOverlayDrawable.this.parent.invalidate();
            }
        }, 75L);
    }
    
    public int getOpacity() {
        return -1;
    }
    
    public void setAlpha(final int n) {
    }
    
    public void setColorFilter(@Nullable final ColorFilter colorFilter) {
    }
    
    public void setProgress(final float progressTarget) {
        this.progressTarget = progressTarget;
    }
    
    public void setText(final String text) {
        this.text = text;
    }
    
    public void setTip(final String tip) {
        this.tip = tip;
    }
}
