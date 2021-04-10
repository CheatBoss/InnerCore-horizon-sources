package com.appsflyer.internal;

import java.util.*;
import java.text.*;
import org.json.*;
import android.content.pm.*;
import com.appsflyer.*;
import android.os.*;

public final class ai
{
    public static ai \u0269;
    private boolean \u0131;
    private String \u0196;
    private JSONObject \u01c3;
    private JSONArray \u0399;
    public boolean \u03b9;
    private boolean \u0406;
    private int \u0456;
    private boolean \u04c0;
    
    public ai() {
        this.\u0131 = true;
        this.\u0456 = 0;
        this.\u0196 = "-1";
        final boolean boolean1 = AppsFlyerProperties.getInstance().getBoolean("disableProxy", false);
        this.\u04c0 = boolean1;
        this.\u03b9 = (true ^ boolean1);
        this.\u0399 = new JSONArray();
        this.\u0456 = 0;
        this.\u0406 = false;
    }
    
    private void \u01c3(final String s, final String s2, final String s3, final String s4, final String s5, final String s6) {
        synchronized (this) {
            this.\u01c3.put("brand", (Object)s);
            this.\u01c3.put("model", (Object)s2);
            this.\u01c3.put("platform", (Object)"Android");
            this.\u01c3.put("platform_version", (Object)s3);
            if (s4 != null && s4.length() > 0) {
                this.\u01c3.put("advertiserId", (Object)s4);
            }
            if (s5 != null && s5.length() > 0) {
                this.\u01c3.put("imei", (Object)s5);
            }
            if (s6 != null && s6.length() > 0) {
                this.\u01c3.put("android_id", (Object)s6);
            }
        }
    }
    
    private void \u0399(final String s, final String s2, final String s3, final String s4) {
        // monitorenter(this)
        Label_0027: {
            if (s == null) {
                break Label_0027;
            }
            while (true) {
                while (true) {
                    try {
                        if (s.length() > 0) {
                            this.\u01c3.put("app_id", (Object)s);
                        }
                        if (s2 != null && s2.length() > 0) {
                            this.\u01c3.put("app_version", (Object)s2);
                        }
                        if (s3 != null && s3.length() > 0) {
                            this.\u01c3.put("channel", (Object)s3);
                        }
                        if (s4 != null && s4.length() > 0) {
                            this.\u01c3.put("preInstall", (Object)s4);
                        }
                        // monitorexit(this)
                        return;
                        // monitorexit(this)
                        return;
                    }
                    finally {}
                    continue;
                }
            }
        }
    }
    
    private void \u03b9(final String s, final String s2, final String s3, final String s4) {
        synchronized (this) {
            this.\u01c3.put("sdk_version", (Object)s);
            if (s2 != null && s2.length() > 0) {
                this.\u01c3.put("devkey", (Object)s2);
            }
            if (s3 != null && s3.length() > 0) {
                this.\u01c3.put("originalAppsFlyerId", (Object)s3);
            }
            if (s4 != null && s4.length() > 0) {
                this.\u01c3.put("uid", (Object)s4);
            }
        }
    }
    
    private boolean \u0456() {
        return this.\u03b9 && (this.\u0131 || this.\u0406);
    }
    
    private void \u04c0() {
        synchronized (this) {
            this.\u0399 = null;
            this.\u0399 = new JSONArray();
            this.\u0456 = 0;
        }
    }
    
    public final void \u0131() {
        synchronized (this) {
            this.\u0131 = false;
            this.\u04c0();
        }
    }
    
    public final void \u0131(String s, final String s2, final String... array) {
        synchronized (this) {
            if (this.\u0456()) {
                if (this.\u0456 < 98304) {
                    try {
                        final long currentTimeMillis = System.currentTimeMillis();
                        String string = "";
                        if (array.length > 0) {
                            final StringBuilder sb = new StringBuilder();
                            for (int i = array.length - 1; i > 0; --i) {
                                sb.append(array[i]);
                                sb.append(", ");
                            }
                            sb.append(array[0]);
                            string = sb.toString();
                        }
                        final String format = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.ENGLISH).format(currentTimeMillis);
                        if (s != null) {
                            s = String.format("%18s %5s _/%s [%s] %s %s", format, Thread.currentThread().getId(), "AppsFlyer_5.4.1", s, s2, string);
                        }
                        else {
                            s = String.format("%18s %5s %s/%s %s", format, Thread.currentThread().getId(), s2, "AppsFlyer_5.4.1", string);
                        }
                        this.\u0399.put((Object)s);
                        this.\u0456 += s.getBytes().length;
                    }
                    finally {}
                }
            }
        }
    }
    
    public final void \u01c3() {
        synchronized (this) {
            this.\u0406 = true;
            this.\u0131("r_debugging_on", new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ", Locale.ENGLISH).format(System.currentTimeMillis()), new String[0]);
        }
    }
    
    public final void \u0269() {
        synchronized (this) {
            this.\u01c3 = null;
            this.\u0399 = null;
            ai.\u0269 = null;
        }
    }
    
    public final void \u0269(final String \u0269) {
        synchronized (this) {
            this.\u0196 = \u0269;
        }
    }
    
    public final String \u0399() {
        // monitorenter(this)
        String s = null;
        try {
            try {
                this.\u01c3.put("data", (Object)this.\u0399);
                this.\u01c3.toString();
                try {
                    this.\u04c0();
                }
                catch (JSONException ex) {}
            }
            finally {
            }
            // monitorexit(this)
        }
        catch (JSONException ex2) {
            s = null;
        }
        // monitorexit(this)
        return s;
    }
    
    public final void \u0399(final String s, final PackageManager packageManager) {
        synchronized (this) {
            final AppsFlyerProperties instance = AppsFlyerProperties.getInstance();
            final AppsFlyerLibCore instance2 = AppsFlyerLibCore.getInstance();
            final String string = instance.getString("remote_debug_static_data");
            if (string != null) {
                try {
                    this.\u01c3 = new JSONObject(string);
                }
                finally {}
            }
            else {
                this.\u01c3 = new JSONObject();
                this.\u01c3(Build.BRAND, Build.MODEL, Build$VERSION.RELEASE, instance.getString("advertiserId"), instance2.\u0399, instance2.\u03b9);
                final StringBuilder sb = new StringBuilder("5.4.1.");
                sb.append(AppsFlyerLibCore.\u0269);
                this.\u03b9(sb.toString(), instance.getString("AppsFlyerKey"), instance.getString("KSAppsFlyerId"), instance.getString("uid"));
                try {
                    this.\u0399(s, String.valueOf(packageManager.getPackageInfo(s, 0).versionCode), instance.getString("channel"), instance.getString("preInstallName"));
                }
                finally {}
                instance.set("remote_debug_static_data", this.\u01c3.toString());
            }
            try {
                this.\u01c3.put("launch_counter", (Object)this.\u0196);
            }
            catch (JSONException ex) {
                ((Throwable)ex).printStackTrace();
            }
        }
    }
    
    public final void \u03b9() {
        synchronized (this) {
            this.\u0131("r_debugging_off", new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ", Locale.ENGLISH).format(System.currentTimeMillis()), new String[0]);
            this.\u0406 = false;
            this.\u0131 = false;
        }
    }
    
    public final boolean \u0406() {
        return this.\u0406;
    }
}
