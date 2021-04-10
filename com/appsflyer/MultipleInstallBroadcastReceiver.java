package com.appsflyer;

import android.content.*;
import java.util.*;
import android.content.pm.*;

public class MultipleInstallBroadcastReceiver extends BroadcastReceiver
{
    public void onReceive(final Context context, final Intent intent) {
        if (intent == null) {
            return;
        }
        String s = null;
        try {
            intent.getStringExtra("referrer");
        }
        finally {
            final Throwable t;
            AFLogger.afErrorLog("error in BroadcastReceiver ", t);
            s = null;
        }
        if (s != null && AppsFlyerLibCore.getSharedPreferences(context).getString("referrer", (String)null) != null) {
            AppsFlyerLibCore.getInstance();
            AppsFlyerLibCore.\u0131(context, s);
            return;
        }
        AFLogger.afInfoLog("MultipleInstallBroadcastReceiver called");
        AppsFlyerLibCore.getInstance().\u01c3(context, intent);
        for (final ResolveInfo resolveInfo : context.getPackageManager().queryBroadcastReceivers(new Intent("com.android.vending.INSTALL_REFERRER"), 0)) {
            final String action = intent.getAction();
            if (((PackageItemInfo)resolveInfo.activityInfo).packageName.equals(context.getPackageName()) && "com.android.vending.INSTALL_REFERRER".equals(action) && !this.getClass().getName().equals(((PackageItemInfo)resolveInfo.activityInfo).name)) {
                final StringBuilder sb = new StringBuilder("trigger onReceive: class: ");
                sb.append(((PackageItemInfo)resolveInfo.activityInfo).name);
                AFLogger.afInfoLog(sb.toString());
                try {
                    ((BroadcastReceiver)Class.forName(((PackageItemInfo)resolveInfo.activityInfo).name).newInstance()).onReceive(context, intent);
                }
                finally {
                    final StringBuilder sb2 = new StringBuilder("error in BroadcastReceiver ");
                    sb2.append(((PackageItemInfo)resolveInfo.activityInfo).name);
                    final Throwable t2;
                    AFLogger.afErrorLog(sb2.toString(), t2);
                }
            }
        }
    }
}
