package androidx.core.graphics;

import android.graphics.*;
import androidx.annotation.*;
import android.os.*;

public final class BitmapCompat
{
    private BitmapCompat() {
    }
    
    public static int getAllocationByteCount(@NonNull final Bitmap bitmap) {
        if (Build$VERSION.SDK_INT >= 19) {
            return bitmap.getAllocationByteCount();
        }
        return bitmap.getByteCount();
    }
    
    public static boolean hasMipMap(@NonNull final Bitmap bitmap) {
        return Build$VERSION.SDK_INT >= 18 && bitmap.hasMipMap();
    }
    
    public static void setHasMipMap(@NonNull final Bitmap bitmap, final boolean hasMipMap) {
        if (Build$VERSION.SDK_INT >= 18) {
            bitmap.setHasMipMap(hasMipMap);
        }
    }
}
