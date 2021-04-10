package com.microsoft.xboxtcui;

import android.os.*;
import android.net.*;
import android.content.*;
import java.util.*;
import com.microsoft.xbox.toolkit.*;
import com.microsoft.xbox.telemetry.helpers.*;
import android.content.pm.*;

public class XboxAppDeepLinker
{
    public static final String ACTION_FIND_PEOPLE = "com.microsoft.xbox.action.ACTION_FIND_PEOPLE";
    private static final String ACTION_VIEW_ACHIEVEMENTS = "com.microsoft.xbox.action.ACTION_VIEW_ACHIEVEMENTS";
    private static final String ACTION_VIEW_GAME_PROFILE = "com.microsoft.xbox.action.ACTION_VIEW_GAME_PROFILE";
    private static final String ACTION_VIEW_SETTINGS = "com.microsoft.xbox.action.ACTION_VIEW_SETTINGS";
    private static final String ACTION_VIEW_USER_PROFILE = "com.microsoft.xbox.action.ACTION_VIEW_USER_PROFILE";
    private static final String AMAZON_FIRE_TV_MODEL_PREFIX = "AFT";
    private static final String AMAZON_MANUFACTURER = "Amazon";
    private static final String AMAZON_STORE_URI = "amzn://apps/android?p=";
    private static final String AMAZON_TABLET_STORE_PACKAGE = "com.amazon.venezia";
    private static final String AMAZON_UNDERGROUND_PACKAGE = "com.amazon.mShop.android";
    private static final String EXTRA_IS_XBOX360_GAME = "com.microsoft.xbox.extra.IS_XBOX360_GAME";
    private static final String EXTRA_TITLEID = "com.microsoft.xbox.extra.TITLEID";
    private static final String EXTRA_XUID = "com.microsoft.xbox.extra.XUID";
    private static final String OCULUS_STORE_WEB_URI = "oculus.store://link/products?referrer=manual&item_id=";
    private static final String OCULUS_XBOXAPP_APP_ID = "1193603937358048";
    private static final String PLAY_STORE_PACKAGE = "com.android.vending";
    private static final String PLAY_STORE_URI = "market://details?id=";
    private static final String PLAY_STORE_WEB_URI = "https://play.google.com/store/apps/details?id=";
    private static final String XBOXAPP_BETA_PACKAGE = "com.microsoft.xboxone.smartglass.beta";
    private static final String XBOXAPP_PACKAGE = "com.microsoft.xboxone.smartglass";
    private static boolean betaAppInstalled;
    private static boolean mainAppInstalled;
    
    private XboxAppDeepLinker() {
    }
    
    public static boolean appDeeplinkingSupported() {
        return (Build.MANUFACTURER.equalsIgnoreCase("Amazon") && Build.MODEL.startsWith("AFT")) ^ true;
    }
    
    private static String getActivityTitle() {
        return "DeepLink";
    }
    
    private static Intent getXboxAppInStoreIntent(final Context context, final String s, final String s2) {
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
    
    private static Intent getXboxAppLaunchIntent(final Context context) {
        XLEAssert.assertTrue(XboxAppDeepLinker.mainAppInstalled || XboxAppDeepLinker.betaAppInstalled);
        PackageManager packageManager;
        String s;
        if (XboxAppDeepLinker.betaAppInstalled) {
            packageManager = context.getPackageManager();
            s = "com.microsoft.xboxone.smartglass.beta";
        }
        else {
            packageManager = context.getPackageManager();
            s = "com.microsoft.xboxone.smartglass";
        }
        return packageManager.getLaunchIntentForPackage(s);
    }
    
    private static void launchXboxAppStorePage(final Context context) {
        Intent intent;
        if ((intent = getXboxAppInStoreIntent(context, "market://details?id=", "com.android.vending")) == null) {
            intent = getXboxAppInStoreIntent(context, "amzn://apps/android?p=", "com.amazon.mShop.android");
        }
        Intent xboxAppInStoreIntent;
        if ((xboxAppInStoreIntent = intent) == null) {
            xboxAppInStoreIntent = getXboxAppInStoreIntent(context, "amzn://apps/android?p=", "com.amazon.venezia");
        }
        Intent intent2;
        if ((intent2 = xboxAppInStoreIntent) == null) {
            intent2 = new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=com.microsoft.xboxone.smartglass"));
        }
        intent2.setFlags(270565376);
        context.startActivity(intent2);
    }
    
    private static void launchXboxAppStorePageInOculusStore(final Context context) {
        final Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("oculus.store://link/products?referrer=manual&item_id=1193603937358048"));
        intent.setFlags(270565376);
        context.startActivity(intent);
    }
    
    public static boolean showAddFriends(final Context context) {
        if (!appDeeplinkingSupported()) {
            return false;
        }
        String packageName;
        if (context == null) {
            packageName = "";
        }
        else {
            packageName = context.getPackageName();
        }
        if (xboxAppIsInstalled(context)) {
            final String trackFriendSuggestionsLink = UTCDeepLink.trackFriendSuggestionsLink(getActivityTitle(), packageName);
            final Intent xboxAppLaunchIntent = getXboxAppLaunchIntent(context);
            xboxAppLaunchIntent.setAction("com.microsoft.xbox.action.ACTION_FIND_PEOPLE");
            xboxAppLaunchIntent.putExtra("deepLinkId", trackFriendSuggestionsLink);
            xboxAppLaunchIntent.putExtra("deepLinkCaller", packageName);
            context.startActivity(xboxAppLaunchIntent);
        }
        else {
            UTCDeepLink.trackUserSendToStore(getActivityTitle(), packageName, "DeepLink - Friend Suggestions");
            launchXboxAppStorePageInOculusStore(context);
        }
        return true;
    }
    
    public static boolean showTitleAchievements(final Context context, final String s) {
        if (!appDeeplinkingSupported()) {
            return false;
        }
        String packageName;
        if (context == null) {
            packageName = "";
        }
        else {
            packageName = context.getPackageName();
        }
        if (xboxAppIsInstalled(context)) {
            final String trackGameHubAchievementsLink = UTCDeepLink.trackGameHubAchievementsLink(getActivityTitle(), packageName, s);
            final Intent xboxAppLaunchIntent = getXboxAppLaunchIntent(context);
            xboxAppLaunchIntent.setAction("com.microsoft.xbox.action.ACTION_VIEW_ACHIEVEMENTS");
            xboxAppLaunchIntent.putExtra("com.microsoft.xbox.extra.TITLEID", s);
            xboxAppLaunchIntent.putExtra("deepLinkId", trackGameHubAchievementsLink);
            xboxAppLaunchIntent.putExtra("deepLinkCaller", packageName);
            context.startActivity(xboxAppLaunchIntent);
        }
        else {
            UTCDeepLink.trackUserSendToStore(getActivityTitle(), packageName, "DeepLink - GameHub Achievements");
            launchXboxAppStorePage(context);
        }
        return true;
    }
    
    public static boolean showTitleHub(final Context context, final String s) {
        if (!appDeeplinkingSupported()) {
            return false;
        }
        String packageName;
        if (context == null) {
            packageName = "";
        }
        else {
            packageName = context.getPackageName();
        }
        if (xboxAppIsInstalled(context)) {
            final String trackGameHubLink = UTCDeepLink.trackGameHubLink(getActivityTitle(), packageName, s);
            final Intent xboxAppLaunchIntent = getXboxAppLaunchIntent(context);
            xboxAppLaunchIntent.setAction("com.microsoft.xbox.action.ACTION_VIEW_GAME_PROFILE");
            xboxAppLaunchIntent.putExtra("com.microsoft.xbox.extra.TITLEID", s);
            xboxAppLaunchIntent.putExtra("deepLinkId", trackGameHubLink);
            xboxAppLaunchIntent.putExtra("deepLinkCaller", packageName);
            context.startActivity(xboxAppLaunchIntent);
        }
        else {
            UTCDeepLink.trackUserSendToStore(getActivityTitle(), packageName, "DeepLink - GameHub");
            launchXboxAppStorePage(context);
        }
        return true;
    }
    
    public static boolean showUserProfile(final Context context, final String s) {
        if (!appDeeplinkingSupported()) {
            return false;
        }
        String packageName;
        if (context == null) {
            packageName = "";
        }
        else {
            packageName = context.getPackageName();
        }
        if (xboxAppIsInstalled(context)) {
            final String trackUserProfileLink = UTCDeepLink.trackUserProfileLink(getActivityTitle(), packageName, s);
            final Intent xboxAppLaunchIntent = getXboxAppLaunchIntent(context);
            xboxAppLaunchIntent.setAction("com.microsoft.xbox.action.ACTION_VIEW_USER_PROFILE");
            xboxAppLaunchIntent.putExtra("com.microsoft.xbox.extra.XUID", s);
            xboxAppLaunchIntent.putExtra("deepLinkId", trackUserProfileLink);
            xboxAppLaunchIntent.putExtra("deepLinkCaller", packageName);
            context.startActivity(xboxAppLaunchIntent);
        }
        else {
            UTCDeepLink.trackUserSendToStore(getActivityTitle(), packageName, "DeepLink - User Profile");
            launchXboxAppStorePage(context);
        }
        return true;
    }
    
    public static boolean showUserSettings(final Context context) {
        if (!appDeeplinkingSupported()) {
            return false;
        }
        String packageName;
        if (context == null) {
            packageName = "";
        }
        else {
            packageName = context.getPackageName();
        }
        if (xboxAppIsInstalled(context)) {
            final String trackUserSettingsLink = UTCDeepLink.trackUserSettingsLink(getActivityTitle(), packageName);
            final Intent xboxAppLaunchIntent = getXboxAppLaunchIntent(context);
            xboxAppLaunchIntent.setAction("com.microsoft.xbox.action.ACTION_VIEW_SETTINGS");
            xboxAppLaunchIntent.putExtra("deepLinkId", trackUserSettingsLink);
            xboxAppLaunchIntent.putExtra("deepLinkCaller", packageName);
            context.startActivity(xboxAppLaunchIntent);
        }
        else {
            UTCDeepLink.trackUserSendToStore(getActivityTitle(), packageName, "DeepLink - User Settings");
            launchXboxAppStorePage(context);
        }
        return true;
    }
    
    private static boolean xboxAppIsInstalled(final Context context) {
        boolean b = false;
        try {
            context.getPackageManager().getPackageInfo("com.microsoft.xboxone.smartglass", 1);
            XboxAppDeepLinker.mainAppInstalled = true;
        }
        catch (PackageManager$NameNotFoundException ex) {
            XboxAppDeepLinker.mainAppInstalled = false;
        }
        try {
            context.getPackageManager().getPackageInfo("com.microsoft.xboxone.smartglass.beta", 1);
            XboxAppDeepLinker.betaAppInstalled = true;
        }
        catch (PackageManager$NameNotFoundException ex2) {
            XboxAppDeepLinker.betaAppInstalled = false;
        }
        if (XboxAppDeepLinker.mainAppInstalled || XboxAppDeepLinker.betaAppInstalled) {
            b = true;
        }
        return b;
    }
}
