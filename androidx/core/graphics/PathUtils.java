package androidx.core.graphics;

import androidx.annotation.*;
import java.util.*;
import android.graphics.*;

public final class PathUtils
{
    private PathUtils() {
    }
    
    @NonNull
    @RequiresApi(26)
    public static Collection<PathSegment> flatten(@NonNull final Path path) {
        return flatten(path, 0.5f);
    }
    
    @NonNull
    @RequiresApi(26)
    public static Collection<PathSegment> flatten(@NonNull final Path path, @FloatRange(from = 0.0) float n) {
        final float[] approximate = path.approximate(n);
        final int n2 = approximate.length / 3;
        final ArrayList list = new ArrayList<PathSegment>(n2);
        for (int i = 1; i < n2; ++i) {
            final int n3 = i * 3;
            final int n4 = (i - 1) * 3;
            n = approximate[n3];
            final float n5 = approximate[n3 + 1];
            final float n6 = approximate[n3 + 2];
            final float n7 = approximate[n4];
            final float n8 = approximate[n4 + 1];
            final float n9 = approximate[n4 + 2];
            if (n != n7 && (n5 != n8 || n6 != n9)) {
                list.add(new PathSegment(new PointF(n8, n9), n7, new PointF(n5, n6), n));
            }
        }
        return (Collection<PathSegment>)list;
    }
}
