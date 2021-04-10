package com.zhekasmirnov.horizon.activity.util;

import android.content.*;
import android.net.*;
import android.os.*;

public class MIUIPermissionHelper
{
    public static void jumpToPermissionsSettings(final Context context) {
        try {
            final Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
            localIntent.putExtra("extra_pkgname", context.getPackageName());
            context.startActivity(localIntent);
        }
        catch (Exception ex) {
            try {
                final Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                localIntent.putExtra("extra_pkgname", context.getPackageName());
                context.startActivity(localIntent);
            }
            catch (Exception ex2) {
                final Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                final Uri uri = Uri.fromParts("package", context.getPackageName(), (String)null);
                intent.setData(uri);
                context.startActivity(intent);
            }
        }
    }
    
    public static boolean isMIUI() {
        return "xiaomi".equals(Build.BRAND.toLowerCase()) || "xiaomi".equals(Build.MANUFACTURER.toLowerCase());
    }
}
