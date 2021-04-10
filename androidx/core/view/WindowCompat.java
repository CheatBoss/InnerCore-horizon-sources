package androidx.core.view;

import androidx.annotation.*;
import android.view.*;
import android.os.*;

public final class WindowCompat
{
    public static final int FEATURE_ACTION_BAR = 8;
    public static final int FEATURE_ACTION_BAR_OVERLAY = 9;
    public static final int FEATURE_ACTION_MODE_OVERLAY = 10;
    
    private WindowCompat() {
    }
    
    @NonNull
    public static <T extends View> T requireViewById(@NonNull final Window window, @IdRes final int n) {
        if (Build$VERSION.SDK_INT >= 28) {
            return (T)window.requireViewById(n);
        }
        final View viewById = window.findViewById(n);
        if (viewById == null) {
            throw new IllegalArgumentException("ID does not reference a View inside this Window");
        }
        return (T)viewById;
    }
}
