package androidx.core.widget;

import android.view.*;
import android.os.*;
import android.widget.*;
import androidx.annotation.*;

public final class PopupMenuCompat
{
    private PopupMenuCompat() {
    }
    
    @Nullable
    public static View$OnTouchListener getDragToOpenListener(@NonNull final Object o) {
        if (Build$VERSION.SDK_INT >= 19) {
            return ((PopupMenu)o).getDragToOpenListener();
        }
        return null;
    }
}
