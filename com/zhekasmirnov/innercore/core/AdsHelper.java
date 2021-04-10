package com.zhekasmirnov.innercore.core;

import java.lang.ref.*;
import android.app.*;
import android.widget.*;

public class AdsHelper
{
    private static WeakReference<Activity> ctx;
    private static int height;
    private static boolean isInitialized;
    private static boolean isShowed;
    private static int width;
    private static PopupWindow window;
    
    static {
        AdsHelper.isInitialized = false;
        AdsHelper.isShowed = false;
    }
    
    public static void checkPermissions() {
    }
    
    public static void hideBanner() {
    }
    
    private static void initializeAds() {
    }
    
    public static void prepareVideo() {
    }
    
    public static void setContext(final Activity activity) {
    }
    
    public static void showBanner() {
    }
    
    public static void showVideo() {
    }
}
