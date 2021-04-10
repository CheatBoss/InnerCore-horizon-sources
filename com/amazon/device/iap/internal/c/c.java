package com.amazon.device.iap.internal.c;

import com.amazon.device.iap.internal.util.*;
import android.content.*;

public class c
{
    private static c a;
    private static final String b;
    private static final String c;
    
    static {
        com.amazon.device.iap.internal.c.c.a = new c();
        b = c.class.getSimpleName();
        final StringBuilder sb = new StringBuilder();
        sb.append(c.class.getName());
        sb.append("_PREFS_");
        c = sb.toString();
    }
    
    public static c a() {
        return com.amazon.device.iap.internal.c.c.a;
    }
    
    public String a(final String s, final String s2) {
        final String b = com.amazon.device.iap.internal.c.c.b;
        final StringBuilder sb = new StringBuilder();
        sb.append("enter getReceiptIdFromSku for sku [");
        sb.append(s2);
        sb.append("], user [");
        sb.append(s);
        sb.append("]");
        e.a(b, sb.toString());
        String string = null;
        try {
            d.a(s, "userId");
            d.a(s2, "sku");
            final Context b2 = com.amazon.device.iap.internal.d.d().b();
            d.a(b2, "context");
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(com.amazon.device.iap.internal.c.c.c);
            sb2.append(s);
            string = b2.getSharedPreferences(sb2.toString(), 0).getString(s2, (String)null);
        }
        finally {
            final String b3 = com.amazon.device.iap.internal.c.c.b;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("error in saving v1 Entitlement:");
            sb3.append(s2);
            sb3.append(":");
            final Throwable t;
            sb3.append(t.getMessage());
            e.a(b3, sb3.toString());
        }
        final String b4 = com.amazon.device.iap.internal.c.c.b;
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("leaving saveEntitlementRecord for sku [");
        sb4.append(s2);
        sb4.append("], user [");
        sb4.append(s);
        sb4.append("]");
        e.a(b4, sb4.toString());
        return string;
    }
    
    public void a(final String s, final String s2, final String s3) {
        final String b = com.amazon.device.iap.internal.c.c.b;
        final StringBuilder sb = new StringBuilder();
        sb.append("enter saveEntitlementRecord for v1 Entitlement [");
        sb.append(s2);
        sb.append("/");
        sb.append(s3);
        sb.append("], user [");
        sb.append(s);
        sb.append("]");
        e.a(b, sb.toString());
        try {
            d.a(s, "userId");
            d.a(s2, "receiptId");
            d.a(s3, "sku");
            final Context b2 = com.amazon.device.iap.internal.d.d().b();
            d.a(b2, "context");
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(com.amazon.device.iap.internal.c.c.c);
            sb2.append(s);
            final SharedPreferences$Editor edit = b2.getSharedPreferences(sb2.toString(), 0).edit();
            edit.putString(s3, s2);
            edit.commit();
        }
        finally {
            final String b3 = com.amazon.device.iap.internal.c.c.b;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("error in saving v1 Entitlement:");
            sb3.append(s2);
            sb3.append("/");
            sb3.append(s3);
            sb3.append(":");
            final Throwable t;
            sb3.append(t.getMessage());
            e.a(b3, sb3.toString());
        }
        final String b4 = com.amazon.device.iap.internal.c.c.b;
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("leaving saveEntitlementRecord for v1 Entitlement [");
        sb4.append(s2);
        sb4.append("/");
        sb4.append(s3);
        sb4.append("], user [");
        sb4.append(s);
        sb4.append("]");
        e.a(b4, sb4.toString());
    }
}
