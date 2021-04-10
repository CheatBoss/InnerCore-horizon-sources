package android.support.v4.view.animation;

import android.view.animation.*;
import android.os.*;
import android.graphics.*;

public final class PathInterpolatorCompat
{
    private PathInterpolatorCompat() {
    }
    
    public static Interpolator create(final float n, final float n2) {
        if (Build$VERSION.SDK_INT >= 21) {
            return PathInterpolatorCompatApi21.create(n, n2);
        }
        return PathInterpolatorCompatBase.create(n, n2);
    }
    
    public static Interpolator create(final float n, final float n2, final float n3, final float n4) {
        if (Build$VERSION.SDK_INT >= 21) {
            return PathInterpolatorCompatApi21.create(n, n2, n3, n4);
        }
        return PathInterpolatorCompatBase.create(n, n2, n3, n4);
    }
    
    public static Interpolator create(final Path path) {
        if (Build$VERSION.SDK_INT >= 21) {
            return PathInterpolatorCompatApi21.create(path);
        }
        return PathInterpolatorCompatBase.create(path);
    }
}
