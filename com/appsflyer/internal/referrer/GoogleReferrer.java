package com.appsflyer.internal.referrer;

import java.util.*;
import android.content.*;
import com.appsflyer.*;
import com.android.installreferrer.api.*;

public class GoogleReferrer
{
    public final Map<String, Object> newMap;
    public final Map<String, Object> oldMap;
    
    public GoogleReferrer() {
        this.oldMap = new HashMap<String, Object>();
        this.newMap = new HashMap<String, Object>();
    }
    
    public static boolean allow(final AppsFlyerLibCore appsFlyerLibCore, final Context context) {
        if (appsFlyerLibCore.getLaunchCounter(AppsFlyerLibCore.getSharedPreferences(context), false) > 2) {
            AFLogger.afRDLog("Install referrer will not load, the counter > 2, ");
            return false;
        }
        try {
            Class.forName("com.android.installreferrer.api.InstallReferrerClient");
            if (AndroidUtils.isPermissionAvailable(context, "com.google.android.finsky.permission.BIND_GET_INSTALL_REFERRER_SERVICE")) {
                AFLogger.afDebugLog("Install referrer is allowed");
                return true;
            }
            AFLogger.afDebugLog("Install referrer is not allowed");
            return false;
        }
        catch (ClassNotFoundException ex) {
            final StringBuilder sb = new StringBuilder("Class ");
            sb.append("com.android.installreferrer.api.InstallReferrerClient");
            sb.append(" not found");
            AFLogger.afRDLog(sb.toString());
            return false;
        }
        finally {
            final Throwable t;
            AFLogger.afErrorLog("An error occurred while trying to verify manifest : ".concat("com.android.installreferrer.api.InstallReferrerClient"), t);
            return false;
        }
    }
    
    public void start(final Context context, final Runnable runnable) {
        try {
            final InstallReferrerClient build = InstallReferrerClient.newBuilder(context).build();
            AFLogger.afDebugLog("Connecting to Install Referrer Library...");
            build.startConnection(new InstallReferrerStateListener() {
                @Override
                public final void onInstallReferrerServiceDisconnected() {
                    AFLogger.afDebugLog("Install Referrer service disconnected");
                }
                
                @Override
                public final void onInstallReferrerSetupFinished(final int n) {
                    GoogleReferrer.this.oldMap.put("code", String.valueOf(n));
                    GoogleReferrer.this.newMap.put("source", "google");
                    GoogleReferrer.this.newMap.put("api_ver", AndroidUtils.getVersionCode(context, "com.android.vending"));
                    GoogleReferrer.this.newMap.put("api_ver_name", AndroidUtils.getVersionName(context, "com.android.vending"));
                    GoogleReferrer.this.newMap.putAll(new MandatoryFields());
                    Label_0567: {
                        Map<String, Object> map = null;
                        String s = null;
                        Label_0555: {
                            if (n != -1) {
                                if (n != 0) {
                                    String s2;
                                    if (n != 1) {
                                        if (n == 2) {
                                            AFLogger.afWarnLog("InstallReferrer FEATURE_NOT_SUPPORTED");
                                            map = GoogleReferrer.this.newMap;
                                            s = "FEATURE_NOT_SUPPORTED";
                                            break Label_0555;
                                        }
                                        if (n == 3) {
                                            AFLogger.afWarnLog("InstallReferrer DEVELOPER_ERROR");
                                            map = GoogleReferrer.this.newMap;
                                            s = "DEVELOPER_ERROR";
                                            break Label_0555;
                                        }
                                        s2 = "responseCode not found.";
                                    }
                                    else {
                                        GoogleReferrer.this.newMap.put("response", "SERVICE_UNAVAILABLE");
                                        s2 = "InstallReferrer not supported";
                                    }
                                    AFLogger.afWarnLog(s2);
                                    break Label_0567;
                                }
                                GoogleReferrer.this.newMap.put("response", "OK");
                                try {
                                    AFLogger.afDebugLog("InstallReferrer connected");
                                    if (build.isReady()) {
                                        final ReferrerDetails installReferrer = build.getInstallReferrer();
                                        final String installReferrer2 = installReferrer.getInstallReferrer();
                                        if (installReferrer2 != null) {
                                            GoogleReferrer.this.oldMap.put("val", installReferrer2);
                                            GoogleReferrer.this.newMap.put("referrer", installReferrer2);
                                        }
                                        final long referrerClickTimestampSeconds = installReferrer.getReferrerClickTimestampSeconds();
                                        GoogleReferrer.this.oldMap.put("clk", Long.toString(referrerClickTimestampSeconds));
                                        GoogleReferrer.this.newMap.put("click_ts", referrerClickTimestampSeconds);
                                        final long installBeginTimestampSeconds = installReferrer.getInstallBeginTimestampSeconds();
                                        GoogleReferrer.this.oldMap.put("install", Long.toString(installBeginTimestampSeconds));
                                        GoogleReferrer.this.newMap.put("install_begin_ts", installBeginTimestampSeconds);
                                        try {
                                            final boolean googlePlayInstantParam = installReferrer.getGooglePlayInstantParam();
                                            GoogleReferrer.this.oldMap.put("instant", googlePlayInstantParam);
                                            final HashMap<String, Boolean> hashMap = new HashMap<String, Boolean>();
                                            hashMap.put("instant", googlePlayInstantParam);
                                            GoogleReferrer.this.newMap.put("google_custom", hashMap);
                                        }
                                        catch (NoSuchMethodError noSuchMethodError) {}
                                    }
                                    else {
                                        AFLogger.afWarnLog("ReferrerClient: InstallReferrer is not ready");
                                        GoogleReferrer.this.oldMap.put("err", "ReferrerClient: InstallReferrer is not ready");
                                    }
                                }
                                finally {
                                    final StringBuilder sb = new StringBuilder("Failed to get install referrer: ");
                                    final Throwable t;
                                    sb.append(t.getMessage());
                                    AFLogger.afWarnLog(sb.toString());
                                    GoogleReferrer.this.oldMap.put("err", t.getMessage());
                                }
                                break Label_0567;
                            }
                            else {
                                AFLogger.afWarnLog("InstallReferrer SERVICE_DISCONNECTED");
                                map = GoogleReferrer.this.newMap;
                                s = "SERVICE_DISCONNECTED";
                            }
                        }
                        map.put("response", s);
                    }
                    AFLogger.afDebugLog("Install Referrer collected locally");
                    runnable.run();
                    build.endConnection();
                }
            });
        }
        finally {
            final Throwable t;
            AFLogger.afErrorLog("referrerClient -> startConnection", t);
        }
    }
}
