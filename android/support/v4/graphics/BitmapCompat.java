package android.support.v4.graphics;

import android.os.*;
import android.graphics.*;

public final class BitmapCompat
{
    static final BitmapImpl IMPL;
    
    static {
        final int sdk_INT = Build$VERSION.SDK_INT;
        BitmapImpl impl;
        if (sdk_INT >= 19) {
            impl = new KitKatBitmapCompatImpl();
        }
        else if (sdk_INT >= 18) {
            impl = new JbMr2BitmapCompatImpl();
        }
        else if (sdk_INT >= 12) {
            impl = new HcMr1BitmapCompatImpl();
        }
        else {
            impl = new BaseBitmapImpl();
        }
        IMPL = impl;
    }
    
    private BitmapCompat() {
    }
    
    public static int getAllocationByteCount(final Bitmap bitmap) {
        return BitmapCompat.IMPL.getAllocationByteCount(bitmap);
    }
    
    public static boolean hasMipMap(final Bitmap bitmap) {
        return BitmapCompat.IMPL.hasMipMap(bitmap);
    }
    
    public static void setHasMipMap(final Bitmap bitmap, final boolean b) {
        BitmapCompat.IMPL.setHasMipMap(bitmap, b);
    }
    
    static class BaseBitmapImpl implements BitmapImpl
    {
        @Override
        public int getAllocationByteCount(final Bitmap bitmap) {
            return bitmap.getRowBytes() * bitmap.getHeight();
        }
        
        @Override
        public boolean hasMipMap(final Bitmap bitmap) {
            return false;
        }
        
        @Override
        public void setHasMipMap(final Bitmap bitmap, final boolean b) {
        }
    }
    
    interface BitmapImpl
    {
        int getAllocationByteCount(final Bitmap p0);
        
        boolean hasMipMap(final Bitmap p0);
        
        void setHasMipMap(final Bitmap p0, final boolean p1);
    }
    
    static class HcMr1BitmapCompatImpl extends BaseBitmapImpl
    {
        @Override
        public int getAllocationByteCount(final Bitmap bitmap) {
            return BitmapCompatHoneycombMr1.getAllocationByteCount(bitmap);
        }
    }
    
    static class JbMr2BitmapCompatImpl extends HcMr1BitmapCompatImpl
    {
        @Override
        public boolean hasMipMap(final Bitmap bitmap) {
            return BitmapCompatJellybeanMR2.hasMipMap(bitmap);
        }
        
        @Override
        public void setHasMipMap(final Bitmap bitmap, final boolean b) {
            BitmapCompatJellybeanMR2.setHasMipMap(bitmap, b);
        }
    }
    
    static class KitKatBitmapCompatImpl extends JbMr2BitmapCompatImpl
    {
        @Override
        public int getAllocationByteCount(final Bitmap bitmap) {
            return BitmapCompatKitKat.getAllocationByteCount(bitmap);
        }
    }
}
