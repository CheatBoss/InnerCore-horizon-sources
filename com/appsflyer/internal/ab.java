package com.appsflyer.internal;

import android.net.*;
import android.text.*;
import com.appsflyer.*;
import java.util.*;
import org.json.*;
import javax.net.ssl.*;
import java.io.*;

public final class ab extends OneLinkHttpTask
{
    private static List<String> \u01c3;
    private String \u0269;
    private String \u0399;
    public b \u03b9;
    
    static {
        ab.\u01c3 = Arrays.asList("onelink.me", "onelnk.com", "app.aflink.com");
    }
    
    public ab(final Uri uri, final AppsFlyerLibCore appsFlyerLibCore) {
        super(appsFlyerLibCore);
        if (!TextUtils.isEmpty((CharSequence)uri.getHost()) && !TextUtils.isEmpty((CharSequence)uri.getPath())) {
            final Iterator<String> iterator = ab.\u01c3.iterator();
            int n = 0;
            int n2 = 0;
            while (iterator.hasNext()) {
                if (uri.getHost().contains(iterator.next())) {
                    n2 = 1;
                }
            }
            int n3 = n2;
            if (AFDeepLinkManager.\u0131 != null) {
                final StringBuilder sb = new StringBuilder("Validate custom domain URLs: ");
                sb.append(Arrays.asList(AFDeepLinkManager.\u0131));
                AFLogger.afRDLog(sb.toString());
                final String[] \u0131 = AFDeepLinkManager.\u0131;
                final int length = \u0131.length;
                while (true) {
                    n3 = n2;
                    if (n >= length) {
                        break;
                    }
                    final String s = \u0131[n];
                    int n4 = n2;
                    if (uri.getHost().contains(s)) {
                        n4 = n2;
                        if (s != "") {
                            final StringBuilder sb2 = new StringBuilder("DeepLink matches customDomain: ");
                            sb2.append(uri.toString());
                            AFLogger.afDebugLog(sb2.toString());
                            n4 = 1;
                        }
                    }
                    ++n;
                    n2 = n4;
                }
            }
            final String[] split = uri.getPath().split("/");
            if (n3 != 0 && split.length == 3) {
                super.oneLinkId = split[1];
                this.\u0269 = split[2];
                this.\u0399 = uri.toString();
            }
        }
    }
    
    public final String getOneLinkUrl() {
        final StringBuilder sb = new StringBuilder();
        sb.append(ServerConfigHandler.getUrl("https://%sonelink.%s/shortlink-sdk/v1"));
        sb.append("/");
        sb.append(super.oneLinkId);
        sb.append("?id=");
        sb.append(this.\u0269);
        return sb.toString();
    }
    
    public final void handleResponse(final String s) {
        try {
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            final JSONObject jsonObject = new JSONObject(s);
            final Iterator keys = jsonObject.keys();
            while (keys.hasNext()) {
                final String s2 = keys.next();
                hashMap.put(s2, jsonObject.optString(s2));
            }
            this.\u03b9.\u0269(hashMap);
        }
        catch (JSONException ex) {
            this.\u03b9.\u0131("Can't parse one link data");
            AFLogger.afErrorLog("Error while parsing to json ".concat(String.valueOf(s)), (Throwable)ex);
        }
    }
    
    public final void initRequest(final HttpsURLConnection httpsURLConnection) throws JSONException, IOException {
        httpsURLConnection.setRequestMethod("GET");
    }
    
    public final void onErrorResponse() {
        String \u03b9 = this.\u0399;
        if (\u03b9 == null) {
            \u03b9 = "Can't get one link data";
        }
        this.\u03b9.\u0131(\u03b9);
    }
    
    public final boolean \u03b9() {
        return !TextUtils.isEmpty((CharSequence)super.oneLinkId) && !TextUtils.isEmpty((CharSequence)this.\u0269) && !super.oneLinkId.equals("app");
    }
    
    public interface b
    {
        void \u0131(final String p0);
        
        void \u0269(final Map<String, String> p0);
    }
}
