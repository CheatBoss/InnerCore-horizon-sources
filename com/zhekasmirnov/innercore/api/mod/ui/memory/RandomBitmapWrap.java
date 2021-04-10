package com.zhekasmirnov.innercore.api.mod.ui.memory;

import android.graphics.*;
import com.zhekasmirnov.innercore.api.log.*;
import java.io.*;

public class RandomBitmapWrap extends BitmapWrap
{
    private static long cacheId;
    private File cache;
    
    static {
        RandomBitmapWrap.cacheId = "bitmap-cache".hashCode();
    }
    
    RandomBitmapWrap(Bitmap copy) {
        if (copy == null || copy.isRecycled()) {
            copy = RandomBitmapWrap.MISSING_BITMAP.copy(RandomBitmapWrap.MISSING_BITMAP.getConfig(), true);
        }
        this.bitmap = copy;
        this.width = this.bitmap.getWidth();
        this.height = this.bitmap.getHeight();
        this.config = this.bitmap.getConfig();
        final StringBuilder sb = new StringBuilder();
        sb.append("");
        final long cacheId = RandomBitmapWrap.cacheId;
        RandomBitmapWrap.cacheId = cacheId + 1L;
        sb.append(cacheId);
        this.cache = BitmapCache.getCacheFile(sb.toString());
    }
    
    @Override
    public void recycle() {
        super.recycle();
        this.cache.delete();
    }
    
    @Override
    public BitmapWrap resize(final int width, final int height) {
        this.restoreIfNeeded();
        Bitmap bitmap2;
        final Bitmap bitmap = bitmap2 = this.bitmap;
        if (bitmap != null) {
            bitmap2 = bitmap;
            if (!bitmap.isRecycled()) {
                bitmap2 = Bitmap.createScaledBitmap(bitmap, width, height, false);
            }
        }
        if (bitmap2 != this.bitmap) {
            return new RandomBitmapWrap(bitmap2);
        }
        this.width = width;
        this.height = height;
        return this;
    }
    
    @Override
    public boolean restore() {
        if (this.bitmap != null) {
            this.bitmap.recycle();
            this.bitmap = null;
        }
        try {
            this.bitmap = Bitmap.createBitmap(this.width, this.height, this.config);
            BitmapCache.readFromFile(this.cache, this.bitmap);
            return true;
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("failed to restore bitmap ");
            sb.append(this.bitmap);
            ICLog.e("UI", sb.toString(), ex);
            this.recycle();
            return false;
        }
    }
    
    @Override
    public boolean store() {
        if (this.bitmap != null) {
            if (!this.bitmap.isRecycled()) {
                try {
                    BitmapCache.writeToFile(this.cache, this.bitmap);
                    this.bitmap.recycle();
                    this.bitmap = null;
                    return true;
                }
                catch (IOException ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("failed to store bitmap ");
                    sb.append(this.bitmap);
                    ICLog.e("UI", sb.toString(), ex);
                    this.recycle();
                    return false;
                }
            }
        }
        this.recycle();
        return false;
    }
}
