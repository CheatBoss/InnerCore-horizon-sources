package android.support.v4.app;

import android.app.*;
import android.support.annotation.*;
import android.os.*;

public final class ActivityManagerCompat
{
    private ActivityManagerCompat() {
    }
    
    public static boolean isLowRamDevice(@NonNull final ActivityManager activityManager) {
        return Build$VERSION.SDK_INT >= 19 && ActivityManagerCompatKitKat.isLowRamDevice(activityManager);
    }
}
