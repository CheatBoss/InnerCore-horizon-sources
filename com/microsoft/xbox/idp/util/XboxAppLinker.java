package com.microsoft.xbox.idp.util;

import android.net.*;
import android.content.*;
import java.util.*;
import android.content.pm.*;
import android.util.*;

public class XboxAppLinker
{
    private static final String AMAZON_STORE_URI = "amzn://apps/android?p=";
    private static final String AMAZON_TABLET_STORE_PACKAGE = "com.amazon.venezia";
    private static final String AMAZON_UNDERGROUND_PACKAGE = "com.amazon.mShop.android";
    private static final String OCULUS_STORE_WEB_URI = "oculus.store://link/products?referrer=manual&item_id=";
    private static final String OCULUS_XBOXAPP_APP_ID = "1193603937358048";
    private static final String PLAY_STORE_PACKAGE = "com.android.vending";
    private static final String PLAY_STORE_URI = "market://details?id=";
    private static final String PLAY_STORE_WEB_URI = "https://play.google.com/store/apps/details?id=";
    private static final String TAG;
    public static final String XBOXAPP_BETA_PACKAGE = "com.microsoft.xboxone.smartglass.beta";
    public static final String XBOXAPP_PACKAGE = "com.microsoft.xboxone.smartglass";
    public static boolean betaAppInstalled;
    public static boolean mainAppInstalled;
    
    static {
        TAG = XboxAppLinker.class.getSimpleName();
    }
    
    public static Intent getAppIntent(final Context context, final String s) {
        return context.getPackageManager().getLaunchIntentForPackage(s);
    }
    
    public static Intent getXboxAppInAnyMarketIntent(final Context context) {
        Intent intent;
        if ((intent = getXboxAppInMarketIntent(context, "market://details?id=", "com.android.vending")) == null) {
            intent = getXboxAppInMarketIntent(context, "amzn://apps/android?p=", "com.amazon.mShop.android");
        }
        Intent xboxAppInMarketIntent;
        if ((xboxAppInMarketIntent = intent) == null) {
            xboxAppInMarketIntent = getXboxAppInMarketIntent(context, "amzn://apps/android?p=", "com.amazon.venezia");
        }
        Intent intent2;
        if ((intent2 = xboxAppInMarketIntent) == null) {
            intent2 = new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=com.microsoft.xboxone.smartglass"));
        }
        intent2.setFlags(270565376);
        return intent2;
    }
    
    public static Intent getXboxAppInMarketIntent(final Context context, final String s, final String s2) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("com.microsoft.xboxone.smartglass");
        final Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(sb.toString()));
        for (final ResolveInfo resolveInfo : context.getPackageManager().queryIntentActivities(intent, 0)) {
            if (resolveInfo.activityInfo.applicationInfo.packageName.equals(s2)) {
                final ActivityInfo activityInfo = resolveInfo.activityInfo;
                final ComponentName component = new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);
                intent.setFlags(270532608);
                intent.setComponent(component);
                return intent;
            }
        }
        return null;
    }
    
    public static Intent getXboxAppInOculusMarketIntent(final Context context) {
        final Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("oculus.store://link/products?referrer=manual&item_id=1193603937358048"));
        intent.setFlags(270565376);
        return intent;
    }
    
    public static Intent getXboxAppLaunchIntent(final Context context) {
        PackageManager packageManager;
        String s;
        if (XboxAppLinker.betaAppInstalled) {
            packageManager = context.getPackageManager();
            s = "com.microsoft.xboxone.smartglass.beta";
        }
        else {
            packageManager = context.getPackageManager();
            s = "com.microsoft.xboxone.smartglass";
        }
        return packageManager.getLaunchIntentForPackage(s);
    }
    
    public static boolean isInstalled(final Context context, final String s) {
        try {
            context.getPackageManager().getPackageInfo(s, 1);
            return true;
        }
        catch (PackageManager$NameNotFoundException ex) {
            return false;
        }
    }
    
    public static boolean isServiceInstalled(final String s, final Context context, final String s2) {
        try {
            context.getPackageManager().getServiceInfo(new ComponentName(s, s2), 0);
            return true;
        }
        catch (PackageManager$NameNotFoundException ex) {
            Log.i(XboxAppLinker.TAG, ex.getClass().toString());
            Log.i(XboxAppLinker.TAG, ex.getMessage());
            return false;
        }
    }
    
    public static void launchXboxAppStorePage(final Context context) {
        context.startActivity(getXboxAppInAnyMarketIntent(context));
    }
    
    public static boolean xboxAppIsInstalled(final Context context) {
        final boolean installed = isInstalled(context, "com.microsoft.xboxone.smartglass");
        boolean b = false;
        if (installed) {
            XboxAppLinker.mainAppInstalled = true;
        }
        else {
            XboxAppLinker.mainAppInstalled = false;
        }
        if (isInstalled(context, "com.microsoft.xboxone.smartglass.beta")) {
            XboxAppLinker.betaAppInstalled = true;
        }
        else {
            XboxAppLinker.betaAppInstalled = false;
        }
        if (!XboxAppLinker.mainAppInstalled) {
            if (XboxAppLinker.betaAppInstalled) {
                return true;
            }
        }
        else {
            b = true;
        }
        return b;
    }
}
