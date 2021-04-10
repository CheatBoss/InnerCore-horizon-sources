package com.appsflyer.internal;

import android.os.*;
import android.provider.*;
import com.appsflyer.*;
import android.content.*;
import java.util.*;
import com.google.android.gms.common.*;
import com.google.android.gms.ads.identifier.*;

public final class v
{
    v() {
    }
    
    public static w \u0399(final ContentResolver contentResolver) {
        final w w = null;
        if (contentResolver == null) {
            return null;
        }
        w w2 = w;
        if (AppsFlyerProperties.getInstance().getString("amazon_aid") == null) {
            w2 = w;
            if ("Amazon".equals(Build.MANUFACTURER)) {
                final int int1 = Settings$Secure.getInt(contentResolver, "limit_ad_tracking", 2);
                if (int1 == 0) {
                    final String string = Settings$Secure.getString(contentResolver, "advertising_id");
                    final w.e \u0131 = com.appsflyer.internal.w.e.\u0131;
                    return new w(string, false);
                }
                if (int1 == 2) {
                    return null;
                }
                String s = null;
                try {
                    Settings$Secure.getString(contentResolver, "advertising_id");
                }
                finally {
                    final Throwable t;
                    AFLogger.afErrorLog("Couldn't fetch Amazon Advertising ID (Ad-Tracking is limited!)", t);
                    s = "";
                }
                final w.e \u01312 = com.appsflyer.internal.w.e.\u0131;
                w2 = new w(s, true);
            }
        }
        return w2;
    }
    
    public static void \u03b9(final Context context, final Map<String, Object> map) {
        AFLogger.afInfoLog("Trying to fetch GAID..");
        final StringBuilder sb = new StringBuilder();
        int n = 0;
        try {
            GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
        }
        finally {
            final Throwable t;
            AFLogger.afErrorLog(t.getMessage(), t);
            n = -1;
        }
        String s2 = null;
        String s = null;
        boolean b2 = false;
        Throwable t2 = null;
        try {
            Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient");
            final AdvertisingIdClient.Info advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
            if (advertisingIdInfo == null) {
                sb.append("gpsAdInfo-null |");
                throw new a("GpsAdIndo is null");
            }
            final String id = advertisingIdInfo.getId();
            try {
                Boolean.toString(advertisingIdInfo.isLimitAdTrackingEnabled() ^ true);
                Label_0091: {
                    if (id == null) {
                        break Label_0091;
                    }
                    try {
                        if (id.length() == 0) {
                            sb.append("emptyOrNull |");
                        }
                        final boolean b = true;
                    }
                    finally {}
                }
            }
            finally {}
        }
        finally {
            s = (s2 = null);
            b2 = false;
            final Throwable t3;
            t2 = t3;
        }
        AFLogger.afErrorLog(t2.getMessage(), t2);
        sb.append(t2.getClass().getSimpleName());
        sb.append(" |");
        AFLogger.afInfoLog("WARNING: Google Play Services is missing.");
        boolean b = b2;
        Label_0393: {
            if (AppsFlyerProperties.getInstance().getBoolean("enableGpsFallback", true)) {
                try {
                    final u.b \u03b9 = u.\u03b9(context);
                    final String \u0269 = \u03b9.\u0269;
                    Boolean.toString(\u03b9.\u0269() ^ true);
                    if (\u0269 != null) {
                        b = b2;
                        if (\u0269.length() != 0) {
                            break Label_0393;
                        }
                    }
                    sb.append("emptyOrNull (bypass) |");
                    b = b2;
                }
                finally {
                    final Throwable t4;
                    AFLogger.afErrorLog(t4.getMessage(), t4);
                    sb.append(t4.getClass().getSimpleName());
                    sb.append(" |");
                    s = AppsFlyerProperties.getInstance().getString("advertiserId");
                    final String string = AppsFlyerProperties.getInstance().getString("advertiserIdEnabled");
                    if (t4.getLocalizedMessage() != null) {
                        s2 = t4.getLocalizedMessage();
                    }
                    else {
                        s2 = t4.toString();
                    }
                    AFLogger.afInfoLog(s2);
                    b = b2;
                    s2 = string;
                }
            }
        }
        if (context.getClass().getName().equals("android.app.ReceiverRestrictedContext")) {
            s = AppsFlyerProperties.getInstance().getString("advertiserId");
            s2 = AppsFlyerProperties.getInstance().getString("advertiserIdEnabled");
            sb.append("context = android.app.ReceiverRestrictedContext |");
        }
        if (sb.length() > 0) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(n);
            sb2.append(": ");
            sb2.append((Object)sb);
            map.put("gaidError", sb2.toString());
        }
        if (s != null && s2 != null) {
            map.put("advertiserId", s);
            map.put("advertiserIdEnabled", s2);
            AppsFlyerProperties.getInstance().set("advertiserId", s);
            AppsFlyerProperties.getInstance().set("advertiserIdEnabled", s2);
            map.put("isGaidWithGps", String.valueOf(b));
        }
    }
    
    static final class a extends IllegalStateException
    {
        a(final String s) {
            super(s);
        }
    }
}
