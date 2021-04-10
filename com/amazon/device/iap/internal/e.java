package com.amazon.device.iap.internal;

import android.util.*;
import com.amazon.device.iap.internal.a.*;
import com.amazon.device.iap.internal.b.*;

public final class e
{
    private static final String a;
    private static volatile boolean b;
    private static volatile boolean c;
    private static volatile c d;
    private static volatile a e;
    private static volatile b f;
    
    static {
        a = e.class.getName();
    }
    
    private static <T> T a(final Class<T> clazz) {
        try {
            return d().a(clazz).newInstance();
        }
        catch (Exception ex) {
            final String a = com.amazon.device.iap.internal.e.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("error getting instance for ");
            sb.append(clazz);
            Log.e(a, sb.toString(), (Throwable)ex);
            return null;
        }
    }
    
    public static boolean a() {
        if (com.amazon.device.iap.internal.e.c) {
            return com.amazon.device.iap.internal.e.b;
        }
        synchronized (e.class) {
            if (com.amazon.device.iap.internal.e.c) {
                return com.amazon.device.iap.internal.e.b;
            }
            try {
                e.class.getClassLoader().loadClass("com.amazon.android.Kiwi");
                com.amazon.device.iap.internal.e.b = false;
            }
            finally {
                com.amazon.device.iap.internal.e.b = true;
            }
            com.amazon.device.iap.internal.e.c = true;
            return com.amazon.device.iap.internal.e.b;
        }
    }
    
    public static c b() {
        if (com.amazon.device.iap.internal.e.d == null) {
            synchronized (e.class) {
                if (com.amazon.device.iap.internal.e.d == null) {
                    com.amazon.device.iap.internal.e.d = a(c.class);
                }
            }
        }
        return com.amazon.device.iap.internal.e.d;
    }
    
    public static a c() {
        if (com.amazon.device.iap.internal.e.e == null) {
            synchronized (e.class) {
                if (com.amazon.device.iap.internal.e.e == null) {
                    com.amazon.device.iap.internal.e.e = a(a.class);
                }
            }
        }
        return com.amazon.device.iap.internal.e.e;
    }
    
    private static b d() {
        if (com.amazon.device.iap.internal.e.f == null) {
            synchronized (e.class) {
                if (com.amazon.device.iap.internal.e.f == null) {
                    b f;
                    if (a()) {
                        f = new d();
                    }
                    else {
                        f = new g();
                    }
                    com.amazon.device.iap.internal.e.f = f;
                }
            }
        }
        return com.amazon.device.iap.internal.e.f;
    }
}
