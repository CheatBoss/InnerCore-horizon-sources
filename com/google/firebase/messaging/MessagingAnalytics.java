package com.google.firebase.messaging;

import android.content.*;
import com.google.firebase.analytics.connector.*;
import com.google.firebase.*;
import android.util.*;
import android.os.*;
import android.text.*;

public class MessagingAnalytics
{
    public static void logNotificationDismiss(final Intent intent) {
        zza("_nd", intent);
    }
    
    public static void logNotificationForeground(final Intent intent) {
        zza("_nf", intent);
    }
    
    public static void logNotificationOpen(final Intent intent) {
        if (intent != null) {
            if ("1".equals(intent.getStringExtra("google.c.a.tc"))) {
                final AnalyticsConnector analyticsConnector = FirebaseApp.getInstance().get(AnalyticsConnector.class);
                if (Log.isLoggable("FirebaseMessaging", 3)) {
                    Log.d("FirebaseMessaging", "Received event with track-conversion=true. Setting user property and reengagement event");
                }
                if (analyticsConnector != null) {
                    final String stringExtra = intent.getStringExtra("google.c.a.c_id");
                    analyticsConnector.setUserProperty("fcm", "_ln", stringExtra);
                    final Bundle bundle = new Bundle();
                    bundle.putString("source", "Firebase");
                    bundle.putString("medium", "notification");
                    bundle.putString("campaign", stringExtra);
                    analyticsConnector.logEvent("fcm", "_cmp", bundle);
                }
                else {
                    Log.w("FirebaseMessaging", "Unable to set user property for conversion tracking:  analytics library is missing");
                }
            }
            else if (Log.isLoggable("FirebaseMessaging", 3)) {
                Log.d("FirebaseMessaging", "Received event with track-conversion=false. Do not set user property");
            }
        }
        zza("_no", intent);
    }
    
    public static void logNotificationReceived(final Intent intent) {
        zza("_nr", intent);
    }
    
    public static boolean shouldUploadMetrics(final Intent intent) {
        return intent != null && "1".equals(intent.getStringExtra("google.c.a.e"));
    }
    
    private static void zza(final String s, final Intent intent) {
        final Bundle bundle = new Bundle();
        final String stringExtra = intent.getStringExtra("google.c.a.c_id");
        if (stringExtra != null) {
            bundle.putString("_nmid", stringExtra);
        }
        final String stringExtra2 = intent.getStringExtra("google.c.a.c_l");
        if (stringExtra2 != null) {
            bundle.putString("_nmn", stringExtra2);
        }
        final String stringExtra3 = intent.getStringExtra("google.c.a.m_l");
        if (!TextUtils.isEmpty((CharSequence)stringExtra3)) {
            bundle.putString("label", stringExtra3);
        }
        final String stringExtra4 = intent.getStringExtra("google.c.a.m_c");
        if (!TextUtils.isEmpty((CharSequence)stringExtra4)) {
            bundle.putString("message_channel", stringExtra4);
        }
        String stringExtra5 = intent.getStringExtra("from");
        if (stringExtra5 == null || !stringExtra5.startsWith("/topics/")) {
            stringExtra5 = null;
        }
        if (stringExtra5 != null) {
            bundle.putString("_nt", stringExtra5);
        }
        if (intent.hasExtra("google.c.a.ts")) {
            try {
                bundle.putInt("_nmt", Integer.parseInt(intent.getStringExtra("google.c.a.ts")));
            }
            catch (NumberFormatException ex) {
                Log.w("FirebaseMessaging", "Error while parsing timestamp in GCM event", (Throwable)ex);
            }
        }
        if (intent.hasExtra("google.c.a.udt")) {
            try {
                bundle.putInt("_ndt", Integer.parseInt(intent.getStringExtra("google.c.a.udt")));
            }
            catch (NumberFormatException ex2) {
                Log.w("FirebaseMessaging", "Error while parsing use_device_time in GCM event", (Throwable)ex2);
            }
        }
        if (Log.isLoggable("FirebaseMessaging", 3)) {
            final String value = String.valueOf(bundle);
            final StringBuilder sb = new StringBuilder(String.valueOf(s).length() + 22 + String.valueOf(value).length());
            sb.append("Sending event=");
            sb.append(s);
            sb.append(" params=");
            sb.append(value);
            Log.d("FirebaseMessaging", sb.toString());
        }
        final AnalyticsConnector analyticsConnector = FirebaseApp.getInstance().get(AnalyticsConnector.class);
        if (analyticsConnector != null) {
            analyticsConnector.logEvent("fcm", s, bundle);
            return;
        }
        Log.w("FirebaseMessaging", "Unable to log event: analytics library is missing");
    }
}
