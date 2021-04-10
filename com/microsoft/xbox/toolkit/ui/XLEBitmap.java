package com.microsoft.xbox.toolkit.ui;

import android.content.res.*;
import java.io.*;
import android.graphics.*;
import android.graphics.drawable.*;

public class XLEBitmap
{
    public static String ALLOCATION_TAG = "XLEBITMAP";
    private Bitmap bitmapSrc;
    
    private XLEBitmap(final Bitmap bitmapSrc) {
        this.bitmapSrc = null;
        this.bitmapSrc = bitmapSrc;
    }
    
    public static XLEBitmap createBitmap(final int n, final int n2, final Bitmap$Config bitmap$Config) {
        return createBitmap(Bitmap.createBitmap(n, n2, bitmap$Config));
    }
    
    public static XLEBitmap createBitmap(final Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        return new XLEBitmap(bitmap);
    }
    
    public static XLEBitmap createScaledBitmap(final XLEBitmap xleBitmap, final int n, final int n2, final boolean b) {
        return createBitmap(Bitmap.createScaledBitmap(xleBitmap.bitmapSrc, n, n2, b));
    }
    
    public static XLEBitmap createScaledBitmap8888(final XLEBitmap xleBitmap, final int n, final int n2, final boolean b) {
        return createBitmap(TextureResizer.createScaledBitmap8888(xleBitmap.bitmapSrc, n, n2, b));
    }
    
    public static XLEBitmap decodeResource(final Resources resources, final int n) {
        return createBitmap(BitmapFactory.decodeResource(resources, n));
    }
    
    public static XLEBitmap decodeResource(final Resources resources, final int n, final BitmapFactory$Options bitmapFactory$Options) {
        return createBitmap(BitmapFactory.decodeResource(resources, n, bitmapFactory$Options));
    }
    
    public static XLEBitmap decodeStream(final InputStream inputStream) {
        return createBitmap(BitmapFactory.decodeStream(inputStream));
    }
    
    public static XLEBitmap decodeStream(final InputStream inputStream, final BitmapFactory$Options bitmapFactory$Options) {
        return createBitmap(BitmapFactory.decodeStream(inputStream, (Rect)null, bitmapFactory$Options));
    }
    
    public void finalize() {
    }
    
    public Bitmap getBitmap() {
        return this.bitmapSrc;
    }
    
    public int getByteCount() {
        return this.bitmapSrc.getRowBytes() * this.bitmapSrc.getHeight();
    }
    
    public XLEBitmapDrawable getDrawable() {
        return new XLEBitmapDrawable(new BitmapDrawable(this.bitmapSrc));
    }
    
    public static class XLEBitmapDrawable
    {
        private BitmapDrawable drawable;
        
        public XLEBitmapDrawable(final BitmapDrawable drawable) {
            this.drawable = drawable;
        }
        
        public BitmapDrawable getDrawable() {
            return this.drawable;
        }
    }
}
