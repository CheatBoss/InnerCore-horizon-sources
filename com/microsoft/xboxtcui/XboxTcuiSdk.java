package com.microsoft.xboxtcui;

import android.app.*;
import android.content.*;
import android.content.res.*;
import com.microsoft.xbox.toolkit.*;

public final class XboxTcuiSdk
{
    private static Activity activity;
    private static Context applicationContext;
    private static AssetManager assetManager;
    private static ContentResolver contentResolver;
    private static Resources resources;
    
    public static Activity getActivity() {
        XLEAssert.assertNotNull(XboxTcuiSdk.activity);
        return XboxTcuiSdk.activity;
    }
    
    public static Context getApplicationContext() {
        XLEAssert.assertNotNull(XboxTcuiSdk.activity);
        if (XboxTcuiSdk.applicationContext == null) {
            XboxTcuiSdk.applicationContext = XboxTcuiSdk.activity.getApplicationContext();
        }
        return XboxTcuiSdk.applicationContext;
    }
    
    public static AssetManager getAssetManager() {
        XLEAssert.assertNotNull(XboxTcuiSdk.activity);
        if (XboxTcuiSdk.assetManager == null) {
            XboxTcuiSdk.assetManager = XboxTcuiSdk.activity.getAssets();
        }
        return XboxTcuiSdk.assetManager;
    }
    
    public static ContentResolver getContentResolver() {
        XLEAssert.assertNotNull(XboxTcuiSdk.activity);
        if (XboxTcuiSdk.contentResolver == null) {
            XboxTcuiSdk.contentResolver = XboxTcuiSdk.activity.getContentResolver();
        }
        return XboxTcuiSdk.contentResolver;
    }
    
    public static boolean getIsTablet() {
        return false;
    }
    
    public static Resources getResources() {
        XLEAssert.assertNotNull(XboxTcuiSdk.activity);
        if (XboxTcuiSdk.resources == null) {
            XboxTcuiSdk.resources = XboxTcuiSdk.activity.getResources();
        }
        return XboxTcuiSdk.resources;
    }
    
    public static Object getSystemService(final String s) {
        XLEAssert.assertNotNull(XboxTcuiSdk.activity);
        return XboxTcuiSdk.activity.getSystemService(s);
    }
    
    public static void sdkInitialize(final Activity activity) {
        synchronized (XboxTcuiSdk.class) {
            XLEAssert.assertNotNull(activity);
            XboxTcuiSdk.activity = activity;
        }
    }
}
