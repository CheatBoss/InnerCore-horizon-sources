package com.appboy.support;

import bo.app.*;
import android.util.*;

public class AppboyLogger
{
    public static final int SUPPRESS = Integer.MAX_VALUE;
    private static bx a;
    private static final String b;
    private static boolean c;
    private static int d;
    private static final int e;
    private static final int f;
    
    static {
        b = getAppboyLogTag(AppboyLogger.class);
        AppboyLogger.d = 4;
        e = 15;
        f = 65;
    }
    
    private static void a(final String s, final String s2, final Throwable t) {
        final bx a = AppboyLogger.a;
        if (a != null && a.a() && s != null) {
            AppboyLogger.a.a(s, s2, t);
        }
    }
    
    public static void checkForSystemLogLevelProperty() {
        synchronized (AppboyLogger.class) {
            final String a = eh.a("log.tag.APPBOY", "");
            if (!StringUtils.isNullOrBlank(a) && a.trim().equalsIgnoreCase("verbose")) {
                AppboyLogger.c = true;
                AppboyLogger.d = 2;
                final String b = AppboyLogger.b;
                final StringBuilder sb = new StringBuilder();
                sb.append("AppboyLogger log level set to ");
                sb.append(a);
                sb.append(" via device system property. Note that subsequent calls to AppboyLogger.setLogLevel() will have no effect.");
                i(b, sb.toString());
            }
        }
    }
    
    public static int d(final String s, final String s2) {
        return d(s, s2, true);
    }
    
    public static int d(final String s, final String s2, final Throwable t) {
        return d(s, s2, t, true);
    }
    
    public static int d(final String s, final String s2, final Throwable t, final boolean b) {
        if (b) {
            a(s, s2, null);
        }
        if (AppboyLogger.d > 3) {
            return 0;
        }
        if (t != null) {
            return Log.d(s, s2, t);
        }
        return Log.d(s, s2);
    }
    
    public static int d(final String s, final String s2, final boolean b) {
        return d(s, s2, null, b);
    }
    
    public static int e(final String s, final String s2) {
        a(s, s2, null);
        if (AppboyLogger.d <= 6) {
            return Log.e(s, s2);
        }
        return 0;
    }
    
    public static int e(final String s, final String s2, final Throwable t) {
        a(s, s2, null);
        if (AppboyLogger.d <= 6) {
            return Log.e(s, s2, t);
        }
        return 0;
    }
    
    public static String getAppboyLogTag(final Class clazz) {
        String s = clazz.getName();
        final int length = s.length();
        final int f = AppboyLogger.f;
        if (length > f) {
            s = s.substring(length - f);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Appboy v2.6.0 .");
        sb.append(s);
        return sb.toString();
    }
    
    public static int getLogLevel() {
        return AppboyLogger.d;
    }
    
    public static int i(final String s, final String s2) {
        return i(s, s2, true);
    }
    
    public static int i(final String s, final String s2, final Throwable t) {
        return i(s, s2, t, true);
    }
    
    public static int i(final String s, final String s2, final Throwable t, final boolean b) {
        if (b) {
            a(s, s2, null);
        }
        if (AppboyLogger.d > 4) {
            return 0;
        }
        if (t != null) {
            return Log.i(s, s2, t);
        }
        return Log.i(s, s2);
    }
    
    public static int i(final String s, final String s2, final boolean b) {
        return d(s, s2, null, b);
    }
    
    public static void setLogLevel(final int d) {
        synchronized (AppboyLogger.class) {
            if (!AppboyLogger.c) {
                AppboyLogger.d = d;
            }
            else {
                final String b = AppboyLogger.b;
                final StringBuilder sb = new StringBuilder();
                sb.append("Log level already set via system property. AppboyLogger.setLogLevel() ignored for level: ");
                sb.append(d);
                w(b, sb.toString());
            }
        }
    }
    
    public static void setTestUserDeviceLoggingManager(final bx a) {
        AppboyLogger.a = a;
    }
    
    public static int v(final String s, final String s2) {
        if (AppboyLogger.d <= 2) {
            return Log.v(s, s2);
        }
        return 0;
    }
    
    public static int v(final String s, final String s2, final Throwable t) {
        if (AppboyLogger.d <= 2) {
            return Log.v(s, s2, t);
        }
        return 0;
    }
    
    public static int w(final String s, final String s2) {
        a(s, s2, null);
        if (AppboyLogger.d <= 5) {
            return Log.w(s, s2);
        }
        return 0;
    }
    
    public static int w(final String s, final String s2, final Throwable t) {
        a(s, s2, t);
        if (AppboyLogger.d <= 5) {
            return Log.w(s, s2, t);
        }
        return 0;
    }
    
    public static int w(final String s, final Throwable t) {
        a(s, null, t);
        if (AppboyLogger.d <= 5) {
            return Log.w(s, t);
        }
        return 0;
    }
}
