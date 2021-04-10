package org.mineprogramming.horizon.innercore.util;

import com.bumptech.glide.load.resource.bitmap.*;
import android.content.*;
import com.bumptech.glide.load.engine.bitmap_recycle.*;
import android.graphics.*;

public class NoAntialiasTransformation extends BitmapTransformation
{
    private static final String ID = "org.mineprogramming.horizon.innercore.glide.transformation.noantialias";
    
    public NoAntialiasTransformation(final Context context) {
        super(context);
    }
    
    public boolean equals(final Object o) {
        return o instanceof NoAntialiasTransformation;
    }
    
    public String getId() {
        return "org.mineprogramming.horizon.innercore.glide.transformation.noantialias";
    }
    
    public int hashCode() {
        return "org.mineprogramming.horizon.innercore.glide.transformation.noantialias".hashCode();
    }
    
    protected Bitmap transform(final BitmapPool bitmapPool, final Bitmap bitmap, final int n, final int n2) {
        if (bitmap.getWidth() == n && bitmap.getHeight() == n2) {
            return bitmap;
        }
        return Bitmap.createScaledBitmap(bitmap, n, n2, false);
    }
}
