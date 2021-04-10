package androidx.core.util;

import androidx.annotation.*;
import android.os.*;
import java.util.*;

public class ObjectsCompat
{
    private ObjectsCompat() {
    }
    
    public static boolean equals(@Nullable final Object o, @Nullable final Object o2) {
        if (Build$VERSION.SDK_INT >= 19) {
            return Objects.equals(o, o2);
        }
        return o == o2 || (o != null && o.equals(o2));
    }
    
    public static int hash(@Nullable final Object... array) {
        if (Build$VERSION.SDK_INT >= 19) {
            return Objects.hash(array);
        }
        return Arrays.hashCode(array);
    }
    
    public static int hashCode(@Nullable final Object o) {
        if (o != null) {
            return o.hashCode();
        }
        return 0;
    }
}
