package com.appsflyer;

import android.net.*;
import android.content.*;
import java.util.concurrent.*;
import java.util.*;
import java.net.*;

public class AFDeepLinkManager
{
    public static AFDeepLinkManager instance;
    public static Uri trampolineUri;
    public static String[] \u0131;
    static String[] \u01c3;
    static volatile boolean \u0269;
    static final int \u0399;
    
    static {
        \u0399 = (int)TimeUnit.SECONDS.toMillis(2L);
    }
    
    private AFDeepLinkManager() {
    }
    
    public static AFDeepLinkManager getInstance() {
        if (AFDeepLinkManager.instance == null) {
            AFDeepLinkManager.instance = new AFDeepLinkManager();
        }
        return AFDeepLinkManager.instance;
    }
    
    private static boolean \u01c3(final String s) {
        if (AFDeepLinkManager.\u01c3 == null) {
            return false;
        }
        if (s.contains("af_tranid=")) {
            return false;
        }
        final StringBuilder sb = new StringBuilder("Validate ESP URLs :");
        sb.append(Arrays.asList(AFDeepLinkManager.\u01c3));
        AFLogger.afRDLog(sb.toString());
        final String[] \u01c3 = AFDeepLinkManager.\u01c3;
        for (int length = \u01c3.length, i = 0; i < length; ++i) {
            final String s2 = \u01c3[i];
            if (s.contains("://".concat(String.valueOf(s2)))) {
                AFLogger.afRDLog("Deeplink matches ESP domain: ".concat(String.valueOf(s2)));
                return true;
            }
        }
        return false;
    }
    
    static void \u0269(final Intent intent, final Context context, final Map<String, Object> map) {
        Uri data;
        if (intent != null && "android.intent.action.VIEW".equals(intent.getAction())) {
            data = intent.getData();
        }
        else {
            data = null;
        }
        if (data != null) {
            if (!intent.hasExtra("af_consumed")) {
                intent.putExtra("af_consumed", System.currentTimeMillis());
                \u0399(context, map, data);
                return;
            }
            final StringBuilder sb = new StringBuilder("skipping re-use of previously consumed deep link: ");
            sb.append(data.toString());
            sb.append(" w/af_consumed");
            AFLogger.afInfoLog(sb.toString());
        }
        else {
            if (AFDeepLinkManager.trampolineUri != null) {
                final StringBuilder sb2 = new StringBuilder("using trampoline Intent fallback with URI: ");
                sb2.append(AFDeepLinkManager.trampolineUri);
                AFLogger.afInfoLog(sb2.toString());
                \u0399(context, map, AFDeepLinkManager.trampolineUri);
                return;
            }
            if (AppsFlyerLibCore.getInstance().latestDeepLink != null) {
                final StringBuilder sb3 = new StringBuilder("using Unity/plugin Intent fallback with URI: ");
                sb3.append(AppsFlyerLibCore.getInstance().latestDeepLink.toString());
                AFLogger.afInfoLog(sb3.toString());
                \u0399(context, map, AppsFlyerLibCore.getInstance().latestDeepLink);
                return;
            }
            AFLogger.afDebugLog("No deep link detected");
        }
    }
    
    static void \u0399(final Context context, final Map<String, Object> map, final Uri uri) {
        if (\u01c3(uri.toString())) {
            AFDeepLinkManager.\u0269 = true;
            final AFExecutor instance = AFExecutor.getInstance();
            if (instance.\u01c3 == null) {
                instance.\u01c3 = Executors.newSingleThreadScheduledExecutor(instance.\u0131);
            }
            instance.\u01c3.execute(new Runnable() {
                @Override
                public final void run() {
                    final HashMap<String, String> hashMap = new HashMap<String, String>();
                    final long currentTimeMillis = System.currentTimeMillis();
                    final Uri uri = null;
                    final Uri uri2 = null;
                    Uri uri3 = uri;
                    long n2 = 0L;
                    Uri \u03b9 = null;
                    try {
                        final StringBuilder sb = new StringBuilder("ESP deeplink resolving is started: ");
                        uri3 = uri;
                        sb.append(uri.toString());
                        uri3 = uri;
                        AFLogger.afDebugLog(sb.toString());
                        uri3 = uri;
                        final HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(uri.toString()).openConnection();
                        uri3 = uri;
                        httpURLConnection.setInstanceFollowRedirects(false);
                        uri3 = uri;
                        httpURLConnection.setReadTimeout(AFDeepLinkManager.\u0399);
                        uri3 = uri;
                        httpURLConnection.setConnectTimeout(AFDeepLinkManager.\u0399);
                        uri3 = uri;
                        httpURLConnection.setRequestProperty("User-agent", "Dalvik/2.1.0 (Linux; U; Android 6.0.1; Nexus 5 Build/M4B30Z)");
                        uri3 = uri;
                        httpURLConnection.connect();
                        uri3 = uri;
                        AFLogger.afDebugLog("ESP deeplink resolving is finished");
                        uri3 = uri;
                        hashMap.put("status", String.valueOf(httpURLConnection.getResponseCode()));
                        Uri parse = uri2;
                        uri3 = uri;
                        if (httpURLConnection.getResponseCode() >= 300) {
                            parse = uri2;
                            uri3 = uri;
                            if (httpURLConnection.getResponseCode() <= 305) {
                                uri3 = uri;
                                parse = Uri.parse(httpURLConnection.getHeaderField("Location"));
                            }
                        }
                        uri3 = parse;
                        final long n = System.currentTimeMillis() - currentTimeMillis;
                        uri3 = parse;
                        httpURLConnection.disconnect();
                    }
                    finally {
                        final Throwable t;
                        hashMap.put("error", t.getLocalizedMessage());
                        hashMap.put("status", "-1");
                        n2 = System.currentTimeMillis() - currentTimeMillis;
                        AFLogger.afErrorLog(t.getMessage(), t);
                        \u03b9 = uri3;
                    }
                    hashMap.put("latency", Long.toString(n2));
                    String string;
                    if (\u03b9 != null) {
                        string = \u03b9.toString();
                    }
                    else {
                        string = "";
                    }
                    hashMap.put("res", string);
                    synchronized (map) {
                        map.put("af_deeplink_r", hashMap);
                        map.put("af_deeplink", uri.toString());
                        // monitorexit(this.\u0269)
                        AFDeepLinkManager.\u0269 = false;
                        if (\u03b9 == null) {
                            \u03b9 = uri;
                        }
                        AppsFlyerLibCore.getInstance().handleDeepLinkCallback(context, map, \u03b9);
                    }
                }
            });
        }
        else {
            AppsFlyerLibCore.getInstance().handleDeepLinkCallback(context, map, uri);
        }
        AFDeepLinkManager.trampolineUri = null;
    }
    
    protected void collectIntentsFromActivities(final Intent intent) {
        Uri data;
        if (intent != null && "android.intent.action.VIEW".equals(intent.getAction())) {
            data = intent.getData();
        }
        else {
            data = null;
        }
        if (data != null && intent.getData() != AFDeepLinkManager.trampolineUri) {
            AFDeepLinkManager.trampolineUri = intent.getData();
        }
    }
}
