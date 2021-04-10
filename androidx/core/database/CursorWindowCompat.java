package androidx.core.database;

import android.database.*;
import android.os.*;
import androidx.annotation.*;

public final class CursorWindowCompat
{
    private CursorWindowCompat() {
    }
    
    @NonNull
    public static CursorWindow create(@Nullable final String s, final long n) {
        if (Build$VERSION.SDK_INT >= 28) {
            return new CursorWindow(s, n);
        }
        if (Build$VERSION.SDK_INT >= 15) {
            return new CursorWindow(s);
        }
        return new CursorWindow(false);
    }
}
