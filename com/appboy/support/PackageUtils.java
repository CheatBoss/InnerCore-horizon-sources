package com.appboy.support;

import android.content.*;

public class PackageUtils
{
    private static final String a;
    private static String b;
    
    static {
        a = AppboyLogger.getAppboyLogTag(PackageUtils.class);
    }
    
    public static String getResourcePackageName(final Context context) {
        final String b = PackageUtils.b;
        if (b != null) {
            return b;
        }
        return PackageUtils.b = context.getPackageName();
    }
    
    public static void setResourcePackageName(final String b) {
        if (!StringUtils.isNullOrBlank(b)) {
            PackageUtils.b = b;
            return;
        }
        AppboyLogger.e(PackageUtils.a, "Package name may not be null or blank");
    }
}
