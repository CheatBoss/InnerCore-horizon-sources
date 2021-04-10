package androidx.core.app;

import android.content.*;
import android.os.*;
import android.app.*;
import androidx.annotation.*;

public final class AppOpsManagerCompat
{
    public static final int MODE_ALLOWED = 0;
    public static final int MODE_DEFAULT = 3;
    public static final int MODE_ERRORED = 2;
    public static final int MODE_IGNORED = 1;
    
    private AppOpsManagerCompat() {
    }
    
    public static int noteOp(@NonNull final Context context, @NonNull final String s, final int n, @NonNull final String s2) {
        if (Build$VERSION.SDK_INT >= 19) {
            return ((AppOpsManager)context.getSystemService("appops")).noteOp(s, n, s2);
        }
        return 1;
    }
    
    public static int noteOpNoThrow(@NonNull final Context context, @NonNull final String s, final int n, @NonNull final String s2) {
        if (Build$VERSION.SDK_INT >= 19) {
            return ((AppOpsManager)context.getSystemService("appops")).noteOpNoThrow(s, n, s2);
        }
        return 1;
    }
    
    public static int noteProxyOp(@NonNull final Context context, @NonNull final String s, @NonNull final String s2) {
        if (Build$VERSION.SDK_INT >= 23) {
            return ((AppOpsManager)context.getSystemService((Class)AppOpsManager.class)).noteProxyOp(s, s2);
        }
        return 1;
    }
    
    public static int noteProxyOpNoThrow(@NonNull final Context context, @NonNull final String s, @NonNull final String s2) {
        if (Build$VERSION.SDK_INT >= 23) {
            return ((AppOpsManager)context.getSystemService((Class)AppOpsManager.class)).noteProxyOpNoThrow(s, s2);
        }
        return 1;
    }
    
    @Nullable
    public static String permissionToOp(@NonNull final String s) {
        if (Build$VERSION.SDK_INT >= 23) {
            return AppOpsManager.permissionToOp(s);
        }
        return null;
    }
}
