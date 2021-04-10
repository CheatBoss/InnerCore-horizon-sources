package androidx.core.database.sqlite;

import android.database.sqlite.*;
import androidx.annotation.*;
import android.os.*;

public final class SQLiteCursorCompat
{
    private SQLiteCursorCompat() {
    }
    
    public static void setFillWindowForwardOnly(@NonNull final SQLiteCursor sqLiteCursor, final boolean fillWindowForwardOnly) {
        if (Build$VERSION.SDK_INT >= 28) {
            sqLiteCursor.setFillWindowForwardOnly(fillWindowForwardOnly);
        }
    }
}
