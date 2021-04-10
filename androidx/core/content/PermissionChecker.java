package androidx.core.content;

import android.content.*;
import android.os.*;
import androidx.core.app.*;
import java.lang.annotation.*;
import androidx.annotation.*;

public final class PermissionChecker
{
    public static final int PERMISSION_DENIED = -1;
    public static final int PERMISSION_DENIED_APP_OP = -2;
    public static final int PERMISSION_GRANTED = 0;
    
    private PermissionChecker() {
    }
    
    public static int checkCallingOrSelfPermission(@NonNull final Context context, @NonNull final String s) {
        String packageName;
        if (Binder.getCallingPid() == Process.myPid()) {
            packageName = context.getPackageName();
        }
        else {
            packageName = null;
        }
        return checkPermission(context, s, Binder.getCallingPid(), Binder.getCallingUid(), packageName);
    }
    
    public static int checkCallingPermission(@NonNull final Context context, @NonNull final String s, @Nullable final String s2) {
        if (Binder.getCallingPid() == Process.myPid()) {
            return -1;
        }
        return checkPermission(context, s, Binder.getCallingPid(), Binder.getCallingUid(), s2);
    }
    
    public static int checkPermission(@NonNull final Context context, @NonNull String s, final int n, final int n2, @Nullable final String s2) {
        if (context.checkPermission(s, n, n2) == -1) {
            return -1;
        }
        final String permissionToOp = AppOpsManagerCompat.permissionToOp(s);
        if (permissionToOp == null) {
            return 0;
        }
        if ((s = s2) == null) {
            final String[] packagesForUid = context.getPackageManager().getPackagesForUid(n2);
            if (packagesForUid == null) {
                return -1;
            }
            if (packagesForUid.length <= 0) {
                return -1;
            }
            s = packagesForUid[0];
        }
        if (AppOpsManagerCompat.noteProxyOpNoThrow(context, permissionToOp, s) != 0) {
            return -2;
        }
        return 0;
    }
    
    public static int checkSelfPermission(@NonNull final Context context, @NonNull final String s) {
        return checkPermission(context, s, Process.myPid(), Process.myUid(), context.getPackageName());
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public @interface PermissionResult {
    }
}
