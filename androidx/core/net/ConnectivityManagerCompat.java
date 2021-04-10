package androidx.core.net;

import android.content.*;
import android.net.*;
import android.os.*;
import java.lang.annotation.*;
import androidx.annotation.*;

public final class ConnectivityManagerCompat
{
    public static final int RESTRICT_BACKGROUND_STATUS_DISABLED = 1;
    public static final int RESTRICT_BACKGROUND_STATUS_ENABLED = 3;
    public static final int RESTRICT_BACKGROUND_STATUS_WHITELISTED = 2;
    
    private ConnectivityManagerCompat() {
    }
    
    @Nullable
    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    public static NetworkInfo getNetworkInfoFromBroadcast(@NonNull final ConnectivityManager connectivityManager, @NonNull final Intent intent) {
        final NetworkInfo networkInfo = (NetworkInfo)intent.getParcelableExtra("networkInfo");
        if (networkInfo != null) {
            return connectivityManager.getNetworkInfo(networkInfo.getType());
        }
        return null;
    }
    
    public static int getRestrictBackgroundStatus(@NonNull final ConnectivityManager connectivityManager) {
        if (Build$VERSION.SDK_INT >= 24) {
            return connectivityManager.getRestrictBackgroundStatus();
        }
        return 3;
    }
    
    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    public static boolean isActiveNetworkMetered(@NonNull final ConnectivityManager connectivityManager) {
        if (Build$VERSION.SDK_INT >= 16) {
            return connectivityManager.isActiveNetworkMetered();
        }
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            return true;
        }
        switch (activeNetworkInfo.getType()) {
            default: {
                return true;
            }
            case 1:
            case 7:
            case 9: {
                return false;
            }
            case 0:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6: {
                return true;
            }
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public @interface RestrictBackgroundStatus {
    }
}
