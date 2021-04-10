package com.appsflyer.internal;

import android.content.*;
import android.net.*;
import com.appsflyer.*;

public final class af
{
    af() {
    }
    
    public static void \u0131(final Context context, final String s) {
        if (s != null) {
            AFLogger.afInfoLog("updateServerUninstallToken called with: ".concat(String.valueOf(s)));
            final c.c \u03b9 = c.c.\u03b9(AppsFlyerProperties.getInstance().getString("afUninstallToken"));
            final SharedPreferences sharedPreferences = AppsFlyerLibCore.getSharedPreferences(context);
            if (sharedPreferences.getBoolean("sentRegisterRequestToAF", false) && \u03b9.\u0269 != null && \u03b9.\u0269.equals(s)) {
                return;
            }
            AppsFlyerProperties.getInstance().set("afUninstallToken", s);
            if (AppsFlyerLibCore.\u0131(sharedPreferences)) {
                AppsFlyerLibCore.getInstance().\u0269(context, s);
            }
        }
    }
    
    public static boolean \u01c3(final Context context) {
        if (AppsFlyerLib.getInstance().isTrackingStopped()) {
            return false;
        }
        try {
            Class.forName("com.google.firebase.messaging.FirebaseMessagingService");
            if (AndroidUtils.\u0269(context, new Intent("com.google.firebase.MESSAGING_EVENT", (Uri)null, context, (Class)FirebaseMessagingServiceListener.class))) {
                return true;
            }
            AFLogger.afWarnLog("Cannot verify existence of our InstanceID Listener Service in the manifest. Please refer to documentation.");
            return false;
        }
        catch (ClassNotFoundException ex) {
            return false;
        }
        finally {
            final Throwable t;
            AFLogger.afErrorLog("An error occurred while trying to verify manifest declarations: ", t);
            return false;
        }
    }
}
