package androidx.core.app;

import android.app.*;
import androidx.annotation.*;
import android.os.*;

public final class ActivityManagerCompat
{
    private ActivityManagerCompat() {
    }
    
    public static boolean isLowRamDevice(@NonNull final ActivityManager activityManager) {
        return Build$VERSION.SDK_INT >= 19 && activityManager.isLowRamDevice();
    }
}
