package com.appsflyer;

import org.json.*;
import java.util.*;
import android.content.*;

public class AppsFlyerProperties
{
    public static final String ADDITIONAL_CUSTOM_DATA = "additionalCustomData";
    public static final String AF_KEY = "AppsFlyerKey";
    public static final String AF_WAITFOR_CUSTOMERID = "waitForCustomerId";
    public static final String APP_ID = "appid";
    public static final String APP_USER_ID = "AppUserId";
    public static final String CHANNEL = "channel";
    public static final String COLLECT_ANDROID_ID = "collectAndroidId";
    public static final String COLLECT_ANDROID_ID_FORCE_BY_USER = "collectAndroidIdForceByUser";
    public static final String COLLECT_FACEBOOK_ATTR_ID = "collectFacebookAttrId";
    public static final String COLLECT_FINGER_PRINT = "collectFingerPrint";
    public static final String COLLECT_IMEI = "collectIMEI";
    public static final String COLLECT_IMEI_FORCE_BY_USER = "collectIMEIForceByUser";
    public static final String COLLECT_MAC = "collectMAC";
    public static final String COLLECT_OAID = "collectOAID";
    public static final String CURRENCY_CODE = "currencyCode";
    public static final String DEVICE_TRACKING_DISABLED = "deviceTrackingDisabled";
    public static final String DISABLE_KEYSTORE = "keyPropDisableAFKeystore";
    public static final String DISABLE_LOGS_COMPLETELY = "disableLogs";
    public static final String DISABLE_OTHER_SDK = "disableOtherSdk";
    public static final String DPM = "disableProxy";
    public static final String EMAIL_CRYPT_TYPE = "userEmailsCryptType";
    public static final String ENABLE_GPS_FALLBACK = "enableGpsFallback";
    public static final String EXTENSION = "sdkExtension";
    public static final String IS_UPDATE = "IS_UPDATE";
    public static final String LAUNCH_PROTECT_ENABLED = "launchProtectEnabled";
    public static final String NEW_REFERRER_SENT = "newGPReferrerSent";
    public static final String ONELINK_DOMAIN = "onelinkDomain";
    public static final String ONELINK_ID = "oneLinkSlug";
    public static final String ONELINK_SCHEME = "onelinkScheme";
    public static final String USER_EMAIL = "userEmail";
    public static final String USER_EMAILS = "userEmails";
    public static final String USE_HTTP_FALLBACK = "useHttpFallback";
    private static AppsFlyerProperties \u0399;
    private String \u0131;
    private boolean \u01c3;
    private Map<String, Object> \u0269;
    private boolean \u0279;
    private boolean \u03b9;
    
    static {
        AppsFlyerProperties.\u0399 = new AppsFlyerProperties();
    }
    
    private AppsFlyerProperties() {
        this.\u0269 = new HashMap<String, Object>();
        this.\u0279 = false;
    }
    
    public static AppsFlyerProperties getInstance() {
        return AppsFlyerProperties.\u0399;
    }
    
    private boolean \u03b9() {
        return this.\u0279;
    }
    
    public boolean getBoolean(String string, final boolean b) {
        string = this.getString(string);
        if (string == null) {
            return b;
        }
        return Boolean.valueOf(string);
    }
    
    public int getInt(String string, final int n) {
        string = this.getString(string);
        if (string == null) {
            return n;
        }
        return Integer.valueOf(string).intValue();
    }
    
    public long getLong(String string, final long n) {
        string = this.getString(string);
        if (string == null) {
            return n;
        }
        return Long.valueOf(string).longValue();
    }
    
    public Object getObject(final String s) {
        synchronized (this) {
            return this.\u0269.get(s);
        }
    }
    
    public String getReferrer(final Context context) {
        final String \u0131 = this.\u0131;
        if (\u0131 != null) {
            return \u0131;
        }
        if (this.getString("AF_REFERRER") != null) {
            return this.getString("AF_REFERRER");
        }
        if (context == null) {
            return null;
        }
        return AppsFlyerLibCore.getSharedPreferences(context).getString("referrer", (String)null);
    }
    
    public String getString(String s) {
        synchronized (this) {
            s = this.\u0269.get(s);
            return s;
        }
    }
    
    public boolean isEnableLog() {
        return this.getBoolean("shouldLog", true);
    }
    
    protected boolean isFirstLaunchCalled() {
        return this.\u03b9;
    }
    
    public boolean isLogsDisabledCompletely() {
        return this.getBoolean("disableLogs", false);
    }
    
    protected boolean isOnReceiveCalled() {
        return this.\u01c3;
    }
    
    public boolean isOtherSdkStringDisabled() {
        return this.getBoolean("disableOtherSdk", false);
    }
    
    public void loadProperties(final Context context) {
        synchronized (this) {
            if (this.\u03b9()) {
                return;
            }
            final String string = AppsFlyerLibCore.getSharedPreferences(context).getString("savedProperties", (String)null);
            if (string != null) {
                AFLogger.afDebugLog("Loading properties..");
                try {
                    final JSONObject jsonObject = new JSONObject(string);
                    final Iterator keys = jsonObject.keys();
                    while (keys.hasNext()) {
                        final String s = keys.next();
                        if (this.\u0269.get(s) == null) {
                            this.\u0269.put(s, jsonObject.getString(s));
                        }
                    }
                    this.\u0279 = true;
                }
                catch (JSONException ex) {
                    AFLogger.afErrorLog("Failed loading properties", (Throwable)ex);
                }
                final StringBuilder sb = new StringBuilder("Done loading properties: ");
                sb.append(this.\u0279);
                AFLogger.afDebugLog(sb.toString());
            }
        }
    }
    
    public void remove(final String s) {
        synchronized (this) {
            this.\u0269.remove(s);
        }
    }
    
    public void saveProperties(final SharedPreferences sharedPreferences) {
        synchronized (this) {
            sharedPreferences.edit().putString("savedProperties", new JSONObject((Map)this.\u0269).toString()).apply();
        }
    }
    
    public void set(final String s, final int n) {
        synchronized (this) {
            this.\u0269.put(s, Integer.toString(n));
        }
    }
    
    public void set(final String s, final long n) {
        synchronized (this) {
            this.\u0269.put(s, Long.toString(n));
        }
    }
    
    public void set(final String s, final String s2) {
        synchronized (this) {
            this.\u0269.put(s, s2);
        }
    }
    
    public void set(final String s, final boolean b) {
        synchronized (this) {
            this.\u0269.put(s, Boolean.toString(b));
        }
    }
    
    public void set(final String s, final String[] array) {
        synchronized (this) {
            this.\u0269.put(s, array);
        }
    }
    
    public void setCustomData(final String s) {
        synchronized (this) {
            this.\u0269.put("additionalCustomData", s);
        }
    }
    
    protected void setFirstLaunchCalled() {
        this.\u03b9 = true;
    }
    
    protected void setFirstLaunchCalled(final boolean \u03b9) {
        this.\u03b9 = \u03b9;
    }
    
    protected void setOnReceiveCalled() {
        this.\u01c3 = true;
    }
    
    protected void setReferrer(final String \u0131) {
        this.set("AF_REFERRER", \u0131);
        this.\u0131 = \u0131;
    }
    
    public void setUserEmails(final String s) {
        synchronized (this) {
            this.\u0269.put("userEmails", s);
        }
    }
    
    public enum EmailsCryptType
    {
        NONE(0), 
        SHA256(3);
        
        private final int \u01c3;
        
        private EmailsCryptType(final int \u01c3) {
            this.\u01c3 = \u01c3;
        }
        
        public final int getValue() {
            return this.\u01c3;
        }
    }
}
