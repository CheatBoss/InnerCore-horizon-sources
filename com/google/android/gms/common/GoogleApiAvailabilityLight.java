package com.google.android.gms.common;

import android.text.*;
import com.google.android.gms.common.wrappers.*;
import android.content.pm.*;
import android.content.*;
import com.google.android.gms.common.internal.*;
import com.google.android.gms.common.util.*;
import android.app.*;

public class GoogleApiAvailabilityLight
{
    public static final int GOOGLE_PLAY_SERVICES_VERSION_CODE;
    private static final GoogleApiAvailabilityLight zzaw;
    
    static {
        GOOGLE_PLAY_SERVICES_VERSION_CODE = GooglePlayServicesUtilLight.GOOGLE_PLAY_SERVICES_VERSION_CODE;
        zzaw = new GoogleApiAvailabilityLight();
    }
    
    GoogleApiAvailabilityLight() {
    }
    
    public static GoogleApiAvailabilityLight getInstance() {
        return GoogleApiAvailabilityLight.zzaw;
    }
    
    private static String zza(final Context context, final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append("gcore_");
        sb.append(GoogleApiAvailabilityLight.GOOGLE_PLAY_SERVICES_VERSION_CODE);
        sb.append("-");
        if (!TextUtils.isEmpty((CharSequence)s)) {
            sb.append(s);
        }
        sb.append("-");
        if (context != null) {
            sb.append(context.getPackageName());
        }
        sb.append("-");
        if (context != null) {
            try {
                sb.append(Wrappers.packageManager(context).getPackageInfo(context.getPackageName(), 0).versionCode);
            }
            catch (PackageManager$NameNotFoundException ex) {}
        }
        return sb.toString();
    }
    
    public int getApkVersion(final Context context) {
        return GooglePlayServicesUtilLight.getApkVersion(context);
    }
    
    public Intent getErrorResolutionIntent(final Context context, final int n, final String s) {
        if (n != 1 && n != 2) {
            if (n != 3) {
                return null;
            }
            return GmsIntents.createSettingsIntent("com.google.android.gms");
        }
        else {
            if (context != null && DeviceProperties.isWearableWithoutPlayStore(context)) {
                return GmsIntents.createAndroidWearUpdateIntent();
            }
            return GmsIntents.createPlayStoreIntent("com.google.android.gms", zza(context, s));
        }
    }
    
    public PendingIntent getErrorResolutionPendingIntent(final Context context, final int n, final int n2) {
        return this.getErrorResolutionPendingIntent(context, n, n2, null);
    }
    
    public PendingIntent getErrorResolutionPendingIntent(final Context context, final int n, final int n2, final String s) {
        final Intent errorResolutionIntent = this.getErrorResolutionIntent(context, n, s);
        if (errorResolutionIntent == null) {
            return null;
        }
        return PendingIntent.getActivity(context, n2, errorResolutionIntent, 134217728);
    }
    
    public String getErrorString(final int n) {
        return GooglePlayServicesUtilLight.getErrorString(n);
    }
    
    public int isGooglePlayServicesAvailable(final Context context) {
        return this.isGooglePlayServicesAvailable(context, GoogleApiAvailabilityLight.GOOGLE_PLAY_SERVICES_VERSION_CODE);
    }
    
    public int isGooglePlayServicesAvailable(final Context context, int googlePlayServicesAvailable) {
        if (GooglePlayServicesUtilLight.isPlayServicesPossiblyUpdating(context, googlePlayServicesAvailable = GooglePlayServicesUtilLight.isGooglePlayServicesAvailable(context, googlePlayServicesAvailable))) {
            googlePlayServicesAvailable = 18;
        }
        return googlePlayServicesAvailable;
    }
    
    public boolean isUninstalledAppPossiblyUpdating(final Context context, final String s) {
        return GooglePlayServicesUtilLight.isUninstalledAppPossiblyUpdating(context, s);
    }
    
    public boolean isUserResolvableError(final int n) {
        return GooglePlayServicesUtilLight.isUserRecoverableError(n);
    }
}
