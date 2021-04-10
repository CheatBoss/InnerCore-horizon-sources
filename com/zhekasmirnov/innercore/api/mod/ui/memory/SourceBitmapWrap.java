package com.zhekasmirnov.innercore.api.mod.ui.memory;

import com.zhekasmirnov.innercore.api.mod.ui.*;
import android.graphics.*;

public class SourceBitmapWrap extends BitmapWrap
{
    private boolean isDirectlyFromSource;
    private String name;
    
    SourceBitmapWrap(final String name, final int width, final int height) {
        this.isDirectlyFromSource = false;
        this.name = name;
        this.width = width;
        this.height = height;
        this.isStored = true;
    }
    
    @Override
    public void recycle() {
        if (this.isDirectlyFromSource) {
            this.bitmap = null;
            this.isRecycled = true;
            this.removeCache();
            return;
        }
        super.recycle();
    }
    
    @Override
    public BitmapWrap resize(final int n, final int n2) {
        final BitmapWrap wrap = BitmapWrap.wrap(this.name, n, n2);
        this.storeIfNeeded();
        return wrap;
    }
    
    @Override
    public boolean restore() {
        final int width = this.width;
        boolean isDirectlyFromSource = false;
        if (width >= 1 && this.height >= 1) {
            final Bitmap safe = TextureSource.instance.getSafe(this.name);
            this.bitmap = Bitmap.createScaledBitmap(safe, this.width, this.height, false);
            if (safe == this.bitmap) {
                isDirectlyFromSource = true;
            }
            this.isDirectlyFromSource = isDirectlyFromSource;
            return true;
        }
        this.recycle();
        return false;
    }
    
    @Override
    public boolean store() {
        if (this.bitmap != null && !this.isDirectlyFromSource) {
            this.bitmap.recycle();
            this.bitmap = null;
        }
        return true;
    }
}
