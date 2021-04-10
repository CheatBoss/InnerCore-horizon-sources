package com.appsflyer;

import android.content.*;

public class SingleInstallBroadcastReceiver extends BroadcastReceiver
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
        final String string = AppsFlyerProperties.getInstance().getString("referrer_timestamp");
        final long currentTimeMillis = System.currentTimeMillis();
        if (string != null && currentTimeMillis - Long.valueOf(string).longValue() < 2000L) {
            return;
        }
        AFLogger.afInfoLog("SingleInstallBroadcastReceiver called");
        AppsFlyerLibCore.getInstance().\u01c3(context, intent);
        AppsFlyerProperties.getInstance().set("referrer_timestamp", String.valueOf(System.currentTimeMillis()));
    }
}
