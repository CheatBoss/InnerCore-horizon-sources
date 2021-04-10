package androidx.core.content.pm;

import android.content.pm.*;
import androidx.annotation.*;
import android.os.*;

public final class PackageInfoCompat
{
    private PackageInfoCompat() {
    }
    
    public static long getLongVersionCode(@NonNull final PackageInfo packageInfo) {
        if (Build$VERSION.SDK_INT >= 28) {
            return packageInfo.getLongVersionCode();
        }
        return packageInfo.versionCode;
    }
}
