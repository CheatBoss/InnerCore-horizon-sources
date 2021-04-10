package androidx.core.content.pm;

import android.content.pm.*;
import android.os.*;
import android.annotation.*;
import java.lang.annotation.*;
import androidx.annotation.*;

public final class PermissionInfoCompat
{
    private PermissionInfoCompat() {
    }
    
    @SuppressLint({ "WrongConstant" })
    public static int getProtection(@NonNull final PermissionInfo permissionInfo) {
        if (Build$VERSION.SDK_INT >= 28) {
            return permissionInfo.getProtection();
        }
        return permissionInfo.protectionLevel & 0xF;
    }
    
    @SuppressLint({ "WrongConstant" })
    public static int getProtectionFlags(@NonNull final PermissionInfo permissionInfo) {
        if (Build$VERSION.SDK_INT >= 28) {
            return permissionInfo.getProtectionFlags();
        }
        return permissionInfo.protectionLevel & 0xFFFFFFF0;
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo$Scope.LIBRARY })
    public @interface Protection {
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @SuppressLint({ "UniqueConstants" })
    @RestrictTo({ RestrictTo$Scope.LIBRARY })
    public @interface ProtectionFlags {
    }
}
