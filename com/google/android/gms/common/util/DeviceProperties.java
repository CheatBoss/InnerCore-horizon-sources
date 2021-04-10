package com.google.android.gms.common.util;

import android.content.*;
import com.google.android.gms.common.*;
import android.os.*;

public final class DeviceProperties
{
    private static Boolean zzzn;
    private static Boolean zzzo;
    private static Boolean zzzr;
    
    public static boolean isIoT(final Context context) {
        if (DeviceProperties.zzzr == null) {
            DeviceProperties.zzzr = (context.getPackageManager().hasSystemFeature("android.hardware.type.iot") || context.getPackageManager().hasSystemFeature("android.hardware.type.embedded"));
        }
        return DeviceProperties.zzzr;
    }
    
    public static boolean isSidewinder(final Context context) {
        if (DeviceProperties.zzzo == null) {
            DeviceProperties.zzzo = (PlatformVersion.isAtLeastLollipop() && context.getPackageManager().hasSystemFeature("cn.google"));
        }
        return DeviceProperties.zzzo;
    }
    
    public static boolean isUserBuild() {
        if (GooglePlayServicesUtilLight.sIsTestMode) {
            return GooglePlayServicesUtilLight.sTestIsUserBuild;
        }
        return "user".equals(Build.TYPE);
    }
    
    public static boolean isWearable(final Context context) {
        if (DeviceProperties.zzzn == null) {
            DeviceProperties.zzzn = (PlatformVersion.isAtLeastKitKatWatch() && context.getPackageManager().hasSystemFeature("android.hardware.type.watch"));
        }
        return DeviceProperties.zzzn;
    }
    
    public static boolean isWearableWithoutPlayStore(final Context context) {
        return (!PlatformVersion.isAtLeastN() || isSidewinder(context)) && isWearable(context);
    }
}
