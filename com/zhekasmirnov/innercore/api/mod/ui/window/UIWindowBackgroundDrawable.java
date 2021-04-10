package com.zhekasmirnov.innercore.api.mod.ui.window;

import android.graphics.drawable.*;
import com.zhekasmirnov.innercore.api.mod.ui.*;
import com.zhekasmirnov.innercore.api.mod.ui.background.*;
import java.util.*;
import com.zhekasmirnov.innercore.api.mod.ui.memory.*;
import com.zhekasmirnov.innercore.api.*;
import android.graphics.*;
import android.support.annotation.*;

public class UIWindowBackgroundDrawable extends Drawable implements IBackgroundProvider
{
    private int backgroundColor;
    private ArrayList<IDrawing> backgroundDrawings;
    private Bitmap cachedBackground;
    private Canvas cachedCanvas;
    private boolean isCacheReleased;
    public UIWindow window;
    
    public UIWindowBackgroundDrawable(final UIWindow window) {
        this.backgroundColor = -1;
        this.backgroundDrawings = new ArrayList<IDrawing>();
        this.cachedBackground = null;
        this.cachedCanvas = null;
        this.isCacheReleased = false;
        this.window = window;
    }
    
    private boolean drawDirty(final Canvas canvas) {
        try {
            final int width = canvas.getWidth();
            final int height = canvas.getHeight();
            if (this.cachedBackground == null) {
                this.cachedBackground = Bitmap.createBitmap(width, height, Bitmap$Config.ARGB_8888);
                this.cachedCanvas = new Canvas(this.cachedBackground);
            }
            else if (this.cachedBackground.getWidth() != width || this.cachedBackground.getHeight() != height) {
                this.cachedBackground.recycle();
                this.cachedBackground = Bitmap.createBitmap(width, height, Bitmap$Config.ARGB_8888);
                this.cachedCanvas = new Canvas(this.cachedBackground);
            }
            this.drawIntoCache(this.cachedCanvas);
            return true;
        }
        catch (OutOfMemoryError outOfMemoryError) {
            this.handleLowMemory();
            return false;
        }
    }
    
    private void drawIntoCache(final Canvas canvas) {
        final float scale = this.window.getScale();
        canvas.drawColor(this.backgroundColor);
        final Iterator<IDrawing> iterator = this.backgroundDrawings.iterator();
        while (iterator.hasNext()) {
            iterator.next().onDraw(canvas, scale);
        }
    }
    
    private void handleLowMemory() {
        this.releaseCache();
        BitmapCache.immediateGC();
        NativeItemModel.tryReleaseModelBitmapsOnLowMemory(16777216);
    }
    
    public void addDrawing(final IDrawing drawing) {
        this.backgroundDrawings.add(drawing);
    }
    
    public void clearAll() {
        this.backgroundDrawings.clear();
    }
    
    public void draw(@NonNull final Canvas canvas) {
        try {
            this.drawIntoCache(canvas);
        }
        catch (OutOfMemoryError outOfMemoryError) {
            this.handleLowMemory();
        }
    }
    
    public int getOpacity() {
        return -3;
    }
    
    public void prepareCache() {
        this.isCacheReleased = false;
    }
    
    public void releaseCache() {
        if (this.cachedBackground != null) {
            this.cachedCanvas = null;
            this.cachedBackground.recycle();
            this.cachedBackground = null;
            this.isCacheReleased = true;
        }
    }
    
    public void setAlpha(@IntRange(from = 0L, to = 255L) final int n) {
    }
    
    public void setBackgroundColor(final int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    
    public void setColorFilter(@Nullable final ColorFilter colorFilter) {
    }
}
