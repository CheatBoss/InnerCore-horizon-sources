package com.google.firebase.iid;

import java.util.concurrent.*;
import android.util.*;
import org.json.*;
import android.text.*;

final class zzaw
{
    private static final long zzdc;
    private final long timestamp;
    final String zzbn;
    private final String zzdd;
    
    static {
        zzdc = TimeUnit.DAYS.toMillis(7L);
    }
    
    private zzaw(final String zzbn, final String zzdd, final long timestamp) {
        this.zzbn = zzbn;
        this.zzdd = zzdd;
        this.timestamp = timestamp;
    }
    
    static String zza(final zzaw zzaw) {
        if (zzaw == null) {
            return null;
        }
        return zzaw.zzbn;
    }
    
    static String zza(String s, final String s2, final long n) {
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", (Object)s);
            jsonObject.put("appVersion", (Object)s2);
            jsonObject.put("timestamp", n);
            s = jsonObject.toString();
            return s;
        }
        catch (JSONException ex) {
            s = String.valueOf(ex);
            final StringBuilder sb = new StringBuilder(String.valueOf(s).length() + 24);
            sb.append("Failed to encode token: ");
            sb.append(s);
            Log.w("FirebaseInstanceId", sb.toString());
            return null;
        }
    }
    
    static zzaw zzi(String value) {
        if (TextUtils.isEmpty((CharSequence)value)) {
            return null;
        }
        if (value.startsWith("{")) {
            try {
                final JSONObject jsonObject = new JSONObject(value);
                return new zzaw(jsonObject.getString("token"), jsonObject.getString("appVersion"), jsonObject.getLong("timestamp"));
            }
            catch (JSONException ex) {
                value = String.valueOf(ex);
                final StringBuilder sb = new StringBuilder(String.valueOf(value).length() + 23);
                sb.append("Failed to parse token: ");
                sb.append(value);
                Log.w("FirebaseInstanceId", sb.toString());
                return null;
            }
        }
        return new zzaw(value, null, 0L);
    }
    
    final boolean zzj(final String s) {
        return System.currentTimeMillis() > this.timestamp + zzaw.zzdc || !s.equals(this.zzdd);
    }
}
