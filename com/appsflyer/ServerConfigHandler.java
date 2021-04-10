package com.appsflyer;

import com.appsflyer.internal.*;
import org.json.*;

public class ServerConfigHandler
{
    public static String getUrl(final String s) {
        return String.format(s, AppsFlyerLib.getInstance().getHostPrefix(), AppsFlyerLibCore.getInstance().getHostName());
    }
    
    static JSONObject \u0131(String o) {
        final JSONException ex = null;
        String optString = null;
        try {
            final JSONObject jsonObject = new JSONObject((String)o);
            try {
                final boolean boolean1 = AppsFlyerProperties.getInstance().getBoolean("disableProxy", false);
                if (jsonObject.optBoolean("monitor", false) && !boolean1) {
                    if (ai.\u0269 == null) {
                        ai.\u0269 = new ai();
                    }
                    ai.\u0269.\u01c3();
                }
                else {
                    if (ai.\u0269 == null) {
                        ai.\u0269 = new ai();
                    }
                    ai.\u0269.\u0131();
                    if (ai.\u0269 == null) {
                        ai.\u0269 = new ai();
                    }
                    ai.\u0269.\u03b9();
                }
                final Object o2;
                if (!jsonObject.has("ol_id")) {
                    return (JSONObject)o2;
                }
                final String optString2 = jsonObject.optString("ol_scheme", (String)null);
                final String optString3 = jsonObject.optString("ol_domain", (String)null);
                optString = jsonObject.optString("ol_ver", (String)null);
                if (optString2 != null) {
                    AppsFlyerProperties.getInstance().set("onelinkScheme", optString2);
                }
                if (optString3 != null) {
                    AppsFlyerProperties.getInstance().set("onelinkDomain", optString3);
                }
                if (optString != null) {
                    AppsFlyerProperties.getInstance().set("onelinkVersion", optString);
                    return jsonObject;
                }
                return (JSONObject)o2;
            }
            catch (JSONException ex) {}
        }
        catch (JSONException ex2) {
            o = ex;
        }
        finally {
            o = optString;
        }
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        ai.\u0269.\u0131();
        Object o2 = o;
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
            o2 = o;
        }
        ai.\u0269.\u03b9();
        return (JSONObject)o2;
    }
}
