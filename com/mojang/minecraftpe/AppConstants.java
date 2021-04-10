package com.mojang.minecraftpe;

import android.content.*;
import android.util.*;
import android.os.*;
import android.content.pm.*;

public class AppConstants
{
    public static String ANDROID_BUILD;
    public static String ANDROID_VERSION;
    public static String APP_PACKAGE;
    public static int APP_VERSION;
    public static String APP_VERSION_NAME;
    public static String PHONE_MANUFACTURER;
    public static String PHONE_MODEL;
    private static AsyncTask<Void, Object, String> loadIdentifiersTask;
    
    public static void loadFromContext(final Context context) {
        Log.i("MinecraftPlatform", "CrashManager: AppConstants loadFromContext started");
        AppConstants.ANDROID_VERSION = Build$VERSION.RELEASE;
        AppConstants.ANDROID_BUILD = Build.DISPLAY;
        AppConstants.PHONE_MODEL = Build.MODEL;
        AppConstants.PHONE_MANUFACTURER = Build.MANUFACTURER;
        loadPackageData(context);
    }
    
    private static void loadPackageData(final Context context) {
        if (context != null) {
            try {
                final PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                AppConstants.APP_PACKAGE = packageInfo.packageName;
                AppConstants.APP_VERSION = packageInfo.versionCode;
                AppConstants.APP_VERSION_NAME = packageInfo.versionName;
                Log.i("MinecraftPlatform", "CrashManager: AppConstants loadFromContext finished succesfully");
            }
            catch (PackageManager$NameNotFoundException ex) {
                Log.w("MinecraftPlatform", "CrashManager: Exception thrown when accessing the package info", (Throwable)ex);
            }
        }
    }
}
