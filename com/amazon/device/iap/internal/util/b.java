package com.amazon.device.iap.internal.util;

import android.content.*;

public class b
{
    private static final String a;
    
    static {
        final StringBuilder sb = new StringBuilder();
        sb.append(b.class.getName());
        sb.append("_PREFS");
        a = sb.toString();
    }
    
    public static String a(final String s) {
        d.a((Object)s, "userId");
        final Context b = com.amazon.device.iap.internal.d.d().b();
        d.a(b, "context");
        return b.getSharedPreferences(com.amazon.device.iap.internal.util.b.a, 0).getString(s, (String)null);
    }
    
    public static void a(final String s, final String s2) {
        d.a((Object)s, "userId");
        final Context b = com.amazon.device.iap.internal.d.d().b();
        d.a(b, "context");
        final SharedPreferences$Editor edit = b.getSharedPreferences(com.amazon.device.iap.internal.util.b.a, 0).edit();
        edit.putString(s, s2);
        edit.commit();
    }
}
