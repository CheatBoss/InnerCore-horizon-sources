package com.amazon.device.iap.internal.c;

import com.amazon.device.iap.internal.*;
import com.amazon.device.iap.internal.util.*;
import android.os.*;
import android.content.*;
import com.amazon.device.iap.model.*;
import org.json.*;
import java.util.*;

public class a
{
    private static final String a;
    private static final String b;
    private static final String c;
    private static int d;
    private static final a e;
    
    static {
        a = a.class.getSimpleName();
        final StringBuilder sb = new StringBuilder();
        sb.append(a.class.getName());
        sb.append("_PREFS");
        b = sb.toString();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(a.class.getName());
        sb2.append("_CLEANER_PREFS");
        c = sb2.toString();
        com.amazon.device.iap.internal.c.a.d = 604800000;
        e = new a();
    }
    
    public static a a() {
        return com.amazon.device.iap.internal.c.a.e;
    }
    
    private void a(final long n) {
        final Context b = com.amazon.device.iap.internal.d.d().b();
        com.amazon.device.iap.internal.util.d.a(b, "context");
        final SharedPreferences$Editor edit = b.getSharedPreferences(com.amazon.device.iap.internal.c.a.c, 0).edit();
        edit.putLong("LAST_CLEANING_TIME", n);
        edit.commit();
    }
    
    private void e() {
        com.amazon.device.iap.internal.util.e.a(com.amazon.device.iap.internal.c.a.a, "enter old receipts cleanup! ");
        final Context b = com.amazon.device.iap.internal.d.d().b();
        com.amazon.device.iap.internal.util.d.a(b, "context");
        this.a(System.currentTimeMillis());
        new Handler().post((Runnable)new Runnable() {
            @Override
            public void run() {
                try {
                    com.amazon.device.iap.internal.util.e.a(com.amazon.device.iap.internal.c.a.a, "perform house keeping! ");
                    final SharedPreferences sharedPreferences = b.getSharedPreferences(com.amazon.device.iap.internal.c.a.b, 0);
                    for (final String s : sharedPreferences.getAll().keySet()) {
                        try {
                            if (System.currentTimeMillis() - com.amazon.device.iap.internal.c.d.a(sharedPreferences.getString(s, (String)null)).c() <= com.amazon.device.iap.internal.c.a.d) {
                                continue;
                            }
                            final String b = com.amazon.device.iap.internal.c.a.a;
                            final StringBuilder sb = new StringBuilder();
                            sb.append("house keeping - try remove Receipt:");
                            sb.append(s);
                            sb.append(" since it's too old");
                            com.amazon.device.iap.internal.util.e.a(b, sb.toString());
                            com.amazon.device.iap.internal.c.a.this.a(s);
                        }
                        catch (com.amazon.device.iap.internal.c.e e) {
                            final String b2 = com.amazon.device.iap.internal.c.a.a;
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("house keeping - try remove Receipt:");
                            sb2.append(s);
                            sb2.append(" since it's invalid ");
                            com.amazon.device.iap.internal.util.e.a(b2, sb2.toString());
                            com.amazon.device.iap.internal.c.a.this.a(s);
                        }
                    }
                }
                finally {
                    final String b3 = com.amazon.device.iap.internal.c.a.a;
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("Error in running cleaning job:");
                    final Throwable t;
                    sb3.append(t);
                    com.amazon.device.iap.internal.util.e.a(b3, sb3.toString());
                }
            }
        });
    }
    
    private long f() {
        final Context b = com.amazon.device.iap.internal.d.d().b();
        com.amazon.device.iap.internal.util.d.a(b, "context");
        final long currentTimeMillis = System.currentTimeMillis();
        final long long1 = b.getSharedPreferences(com.amazon.device.iap.internal.c.a.c, 0).getLong("LAST_CLEANING_TIME", 0L);
        if (long1 == 0L) {
            this.a(currentTimeMillis);
            return currentTimeMillis;
        }
        return long1;
    }
    
    public void a(final String s) {
        final String a = com.amazon.device.iap.internal.c.a.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("enter removeReceipt for receipt[");
        sb.append(s);
        sb.append("]");
        com.amazon.device.iap.internal.util.e.a(a, sb.toString());
        final Context b = com.amazon.device.iap.internal.d.d().b();
        com.amazon.device.iap.internal.util.d.a(b, "context");
        final SharedPreferences$Editor edit = b.getSharedPreferences(com.amazon.device.iap.internal.c.a.b, 0).edit();
        edit.remove(s);
        edit.commit();
        final String a2 = com.amazon.device.iap.internal.c.a.a;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("leave removeReceipt for receipt[");
        sb2.append(s);
        sb2.append("]");
        com.amazon.device.iap.internal.util.e.a(a2, sb2.toString());
    }
    
    public void a(String a, final String s, final String s2, final String s3) {
        final String a2 = a.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("enter saveReceipt for receipt [");
        sb.append(s3);
        sb.append("]");
        com.amazon.device.iap.internal.util.e.a(a2, sb.toString());
        try {
            com.amazon.device.iap.internal.util.d.a(s, "userId");
            com.amazon.device.iap.internal.util.d.a(s2, "receiptId");
            com.amazon.device.iap.internal.util.d.a(s3, "receiptString");
            final Context b = com.amazon.device.iap.internal.d.d().b();
            com.amazon.device.iap.internal.util.d.a(b, "context");
            final com.amazon.device.iap.internal.c.d d = new com.amazon.device.iap.internal.c.d(s, s3, a, System.currentTimeMillis());
            final SharedPreferences$Editor edit = b.getSharedPreferences(a.b, 0).edit();
            edit.putString(s2, d.d());
            edit.commit();
        }
        finally {
            final String a3 = a.a;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("error in saving pending receipt:");
            sb2.append(a);
            sb2.append("/");
            sb2.append(s3);
            sb2.append(":");
            final Throwable t;
            sb2.append(t.getMessage());
            com.amazon.device.iap.internal.util.e.a(a3, sb2.toString());
        }
        a = a.a;
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("leaving saveReceipt for receipt id [");
        sb3.append(s2);
        sb3.append("]");
        com.amazon.device.iap.internal.util.e.a(a, sb3.toString());
    }
    
    public Set<Receipt> b(final String s) {
        final Context b = com.amazon.device.iap.internal.d.d().b();
        com.amazon.device.iap.internal.util.d.a(b, "context");
        final String a = com.amazon.device.iap.internal.c.a.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("enter getLocalReceipts for user[");
        sb.append(s);
        sb.append("]");
        com.amazon.device.iap.internal.util.e.a(a, sb.toString());
        final HashSet<Receipt> set = new HashSet<Receipt>();
        if (!com.amazon.device.iap.internal.util.d.a(s)) {
            final Map all = b.getSharedPreferences(com.amazon.device.iap.internal.c.a.b, 0).getAll();
            for (final String s2 : all.keySet()) {
                final String s3 = (String)all.get(s2);
                String s4;
                StringBuilder sb3;
                String s5;
                try {
                    final com.amazon.device.iap.internal.c.d a2 = com.amazon.device.iap.internal.c.d.a(s3);
                    set.add(com.amazon.device.iap.internal.util.a.a(new JSONObject(a2.b()), s, a2.a()));
                    continue;
                }
                catch (JSONException ex) {
                    this.a(s2);
                    final String a3 = com.amazon.device.iap.internal.c.a.a;
                    final StringBuilder sb2 = new StringBuilder();
                }
                catch (com.amazon.device.iap.internal.b.d d) {
                    this.a(s2);
                    s4 = com.amazon.device.iap.internal.c.a.a;
                    sb3 = new StringBuilder();
                    s5 = "failed to verify signature:[";
                }
                finally {
                    s4 = com.amazon.device.iap.internal.c.a.a;
                    sb3 = new StringBuilder();
                    s5 = "failed to load the receipt from SharedPreference:[";
                }
                sb3.append(s5);
                sb3.append(s3);
                sb3.append("]");
                com.amazon.device.iap.internal.util.e.b(s4, sb3.toString());
            }
            final String a4 = com.amazon.device.iap.internal.c.a.a;
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("leaving getLocalReceipts for user[");
            sb4.append(s);
            sb4.append("], ");
            sb4.append(set.size());
            sb4.append(" local receipts found.");
            com.amazon.device.iap.internal.util.e.a(a4, sb4.toString());
            if (System.currentTimeMillis() - this.f() > com.amazon.device.iap.internal.c.a.d) {
                this.e();
            }
            return set;
        }
        final String a5 = com.amazon.device.iap.internal.c.a.a;
        final StringBuilder sb5 = new StringBuilder();
        sb5.append("empty UserId: ");
        sb5.append(s);
        com.amazon.device.iap.internal.util.e.b(a5, sb5.toString());
        final StringBuilder sb6 = new StringBuilder();
        sb6.append("Invalid UserId:");
        sb6.append(s);
        throw new RuntimeException(sb6.toString());
    }
    
    public String c(String s) {
        final Context b = com.amazon.device.iap.internal.d.d().b();
        com.amazon.device.iap.internal.util.d.a(b, "context");
        if (!com.amazon.device.iap.internal.util.d.a(s)) {
            s = b.getSharedPreferences(com.amazon.device.iap.internal.c.a.b, 0).getString(s, (String)null);
            if (s != null) {
                try {
                    s = com.amazon.device.iap.internal.c.d.a(s).a();
                    return s;
                }
                catch (com.amazon.device.iap.internal.c.e e) {}
            }
            return null;
        }
        final String a = com.amazon.device.iap.internal.c.a.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("empty receiptId: ");
        sb.append(s);
        com.amazon.device.iap.internal.util.e.b(a, sb.toString());
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Invalid ReceiptId:");
        sb2.append(s);
        throw new RuntimeException(sb2.toString());
    }
}
