package android.support.v4.graphics;

import android.graphics.*;

class BitmapCompatJellybeanMR2
{
    public static boolean hasMipMap(final Bitmap bitmap) {
        return bitmap.hasMipMap();
    }
    
    public static void setHasMipMap(final Bitmap bitmap, final boolean hasMipMap) {
        bitmap.setHasMipMap(hasMipMap);
    }
}
