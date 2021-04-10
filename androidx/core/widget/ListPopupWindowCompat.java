package androidx.core.widget;

import android.widget.*;
import android.view.*;
import android.os.*;
import androidx.annotation.*;

public final class ListPopupWindowCompat
{
    private ListPopupWindowCompat() {
    }
    
    @Nullable
    public static View$OnTouchListener createDragToOpenListener(@NonNull final ListPopupWindow listPopupWindow, @NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 19) {
            return listPopupWindow.createDragToOpenListener(view);
        }
        return null;
    }
    
    @Deprecated
    public static View$OnTouchListener createDragToOpenListener(final Object o, final View view) {
        return createDragToOpenListener((ListPopupWindow)o, view);
    }
}
