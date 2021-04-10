package com.appsflyer.internal.instant;

import android.content.*;
import android.os.*;

public class AFInstantApps
{
    public static boolean isInstantApp(final Context context) {
        if (Build$VERSION.SDK_INT >= 26) {
            return context.getPackageManager().isInstantApp();
        }
        try {
            context.getClassLoader().loadClass("com.google.android.instantapps.supervisor.InstantAppsRuntime");
            return true;
        }
        catch (ClassNotFoundException ex) {
            return false;
        }
    }
}
