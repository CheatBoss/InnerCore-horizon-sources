package com.appsflyer.share;

import java.util.*;
import com.appsflyer.*;
import com.appsflyer.internal.*;
import java.lang.ref.*;
import java.net.*;
import android.net.*;
import android.content.*;

public class CrossPromotionHelper
{
    private static String \u03b9 = "https://%simpression.%s";
    
    public static void trackAndOpenStore(final Context context, final String s, final String s2) {
        trackAndOpenStore(context, s, s2, null);
    }
    
    public static void trackAndOpenStore(final Context context, final String s, final String s2, final Map<String, String> map) {
        final LinkGenerator \u03b9 = \u0399(context, s, s2, map, ServerConfigHandler.getUrl(Constants.\u0269));
        if (AppsFlyerProperties.getInstance().getBoolean("waitForCustomerId", false)) {
            AFLogger.afInfoLog("CustomerUserId not set, track And Open Store is disabled", true);
            return;
        }
        Map<String, String> map2;
        if ((map2 = map) == null) {
            map2 = new HashMap<String, String>();
        }
        map2.put("af_campaign", s2);
        AppsFlyerLib.getInstance().trackEvent(context, "af_cross_promotion", (Map<String, Object>)map2);
        new Thread(new a(\u03b9.generateLink(), new am(), context, AppsFlyerLib.getInstance().isTrackingStopped())).start();
    }
    
    public static void trackCrossPromoteImpression(final Context context, final String s, final String s2) {
        trackCrossPromoteImpression(context, s, s2, null);
    }
    
    public static void trackCrossPromoteImpression(final Context context, final String s, final String s2, final Map<String, String> map) {
        if (AppsFlyerProperties.getInstance().getBoolean("waitForCustomerId", false)) {
            AFLogger.afInfoLog("CustomerUserId not set, Promote Impression is disabled", true);
            return;
        }
        new Thread(new a(\u0399(context, s, s2, map, ServerConfigHandler.getUrl(CrossPromotionHelper.\u03b9)).generateLink(), null, null, AppsFlyerLib.getInstance().isTrackingStopped())).start();
    }
    
    private static LinkGenerator \u0399(final Context context, final String \u03b9, final String campaign, final Map<String, String> map, final String \u0269) {
        final LinkGenerator linkGenerator = new LinkGenerator("af_cross_promotion");
        linkGenerator.\u0269 = \u0269;
        linkGenerator.\u03b9 = \u03b9;
        linkGenerator.addParameter("af_siteid", context.getPackageName());
        if (campaign != null) {
            linkGenerator.setCampaign(campaign);
        }
        if (map != null) {
            linkGenerator.addParameters(map);
        }
        final String string = AppsFlyerProperties.getInstance().getString("advertiserId");
        if (string != null) {
            linkGenerator.addParameter("advertising_id", string);
        }
        return linkGenerator;
    }
    
    static final class a implements Runnable
    {
        private final am \u01c3;
        private final WeakReference<Context> \u0269;
        private final String \u0399;
        private final boolean \u03b9;
        
        a(final String \u03b9, final am \u01c3, final Context context, final boolean \u03b92) {
            this.\u0399 = \u03b9;
            this.\u01c3 = \u01c3;
            this.\u0269 = new WeakReference<Context>(context);
            this.\u03b9 = \u03b92;
        }
        
        @Override
        public final void run() {
            if (this.\u03b9) {
                return;
            }
            HttpURLConnection httpURLConnection2;
            try {
                final HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(this.\u0399).openConnection();
                try {
                    httpURLConnection.setConnectTimeout(10000);
                    httpURLConnection.setInstanceFollowRedirects(false);
                    final int responseCode = httpURLConnection.getResponseCode();
                    if (responseCode != 200) {
                        if (responseCode != 301 && responseCode != 302) {
                            final StringBuilder sb = new StringBuilder("call to ");
                            sb.append(this.\u0399);
                            sb.append(" failed: ");
                            sb.append(responseCode);
                            AFLogger.afInfoLog(sb.toString());
                        }
                        else {
                            final StringBuilder sb2 = new StringBuilder("Cross promotion redirection success: ");
                            sb2.append(this.\u0399);
                            AFLogger.afInfoLog(sb2.toString(), false);
                            if (this.\u01c3 != null && this.\u0269.get() != null) {
                                this.\u01c3.\u0269 = httpURLConnection.getHeaderField("Location");
                                final am \u01c3 = this.\u01c3;
                                final Context context = this.\u0269.get();
                                if (\u01c3.\u0269 != null) {
                                    context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(\u01c3.\u0269)).setFlags(268435456));
                                }
                            }
                        }
                    }
                    else {
                        final StringBuilder sb3 = new StringBuilder("Cross promotion impressions success: ");
                        sb3.append(this.\u0399);
                        AFLogger.afInfoLog(sb3.toString(), false);
                    }
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    return;
                }
                finally {}
            }
            finally {
                httpURLConnection2 = null;
            }
            try {
                final Throwable t;
                AFLogger.afErrorLog(t.getMessage(), t, true);
                if (httpURLConnection2 != null) {
                    httpURLConnection2.disconnect();
                }
            }
            finally {
                if (httpURLConnection2 != null) {
                    httpURLConnection2.disconnect();
                }
            }
        }
    }
}
