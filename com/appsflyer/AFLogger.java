package com.appsflyer;

import android.util.*;
import com.appsflyer.internal.*;
import java.util.concurrent.*;
import java.util.*;

public class AFLogger
{
    private static long \u03b9;
    
    static {
        AFLogger.\u03b9 = System.currentTimeMillis();
    }
    
    public static void afDebugLog(final String s) {
        if (\u0399(LogLevel.DEBUG)) {
            Log.d("AppsFlyer_5.4.1", \u01c3(s, false));
        }
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        ai.\u0269.\u0131(null, "D", \u01c3(s, true));
    }
    
    public static void afErrorLog(final String s, final Throwable t) {
        \u0131(s, t, true);
    }
    
    public static void afErrorLog(final String s, final Throwable t, final boolean b) {
        \u0131(s, t, b);
    }
    
    public static void afInfoLog(final String s) {
        afInfoLog(s, true);
    }
    
    public static void afInfoLog(final String s, final boolean b) {
        if (\u0399(LogLevel.INFO)) {
            Log.i("AppsFlyer_5.4.1", \u01c3(s, false));
        }
        if (b) {
            if (ai.\u0269 == null) {
                ai.\u0269 = new ai();
            }
            ai.\u0269.\u0131(null, "I", \u01c3(s, true));
        }
    }
    
    public static void afRDLog(final String s) {
        if (\u0399(LogLevel.VERBOSE)) {
            Log.v("AppsFlyer_5.4.1", \u01c3(s, false));
        }
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        ai.\u0269.\u0131(null, "V", \u01c3(s, true));
    }
    
    public static void afWarnLog(final String s) {
        \u0399(s);
    }
    
    public static void resetDeltaTime() {
        AFLogger.\u03b9 = System.currentTimeMillis();
    }
    
    private static void \u0131(String s, final Throwable t, final boolean b) {
        if (\u0399(LogLevel.ERROR) && b) {
            Log.e("AppsFlyer_5.4.1", \u01c3(s, false), t);
        }
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        final ai \u0269 = ai.\u0269;
        final Throwable cause = t.getCause();
        final String simpleName = t.getClass().getSimpleName();
        if (cause == null) {
            s = t.getMessage();
        }
        else {
            s = cause.getMessage();
        }
        StackTraceElement[] array;
        if (cause == null) {
            array = t.getStackTrace();
        }
        else {
            array = cause.getStackTrace();
        }
        int i = 1;
        String[] array2;
        if (array == null) {
            array2 = new String[] { s };
        }
        else {
            final String[] array3 = new String[array.length + 1];
            array3[0] = s;
            while (i < array.length) {
                array3[i] = array[i].toString();
                ++i;
            }
            array2 = array3;
        }
        \u0269.\u0131("exception", simpleName, array2);
    }
    
    private static String \u01c3(final String s, final boolean b) {
        if (!b && LogLevel.VERBOSE.getLevel() > AppsFlyerProperties.getInstance().getInt("logLevel", LogLevel.NONE.getLevel())) {
            return s;
        }
        final StringBuilder sb = new StringBuilder("(");
        sb.append(\u0399(System.currentTimeMillis() - AFLogger.\u03b9));
        sb.append(") [");
        sb.append(Thread.currentThread().getName());
        sb.append("] ");
        sb.append(s);
        return sb.toString();
    }
    
    static void \u01c3(final String s) {
        if (!\u0269()) {
            Log.d("AppsFlyer_5.4.1", \u01c3(s, false));
        }
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        ai.\u0269.\u0131(null, "F", s);
    }
    
    private static boolean \u0269() {
        return AppsFlyerProperties.getInstance().isLogsDisabledCompletely();
    }
    
    private static String \u0399(long minutes) {
        final long hours = TimeUnit.MILLISECONDS.toHours(minutes);
        final long n = minutes - TimeUnit.HOURS.toMillis(hours);
        minutes = TimeUnit.MILLISECONDS.toMinutes(n);
        final long n2 = n - TimeUnit.MINUTES.toMillis(minutes);
        final long seconds = TimeUnit.MILLISECONDS.toSeconds(n2);
        return String.format(Locale.getDefault(), "%02d:%02d:%02d:%03d", hours, minutes, seconds, TimeUnit.MILLISECONDS.toMillis(n2 - TimeUnit.SECONDS.toMillis(seconds)));
    }
    
    static void \u0399(final String s) {
        if (\u0399(LogLevel.WARNING)) {
            Log.w("AppsFlyer_5.4.1", \u01c3(s, false));
        }
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        ai.\u0269.\u0131(null, "W", \u01c3(s, true));
    }
    
    private static boolean \u0399(final LogLevel logLevel) {
        return logLevel.getLevel() <= AppsFlyerProperties.getInstance().getInt("logLevel", LogLevel.NONE.getLevel());
    }
    
    public enum LogLevel
    {
        DEBUG(4), 
        ERROR(1), 
        INFO(3), 
        NONE(0), 
        VERBOSE(5), 
        WARNING(2);
        
        private int \u0269;
        
        private LogLevel(final int \u0269) {
            this.\u0269 = \u0269;
        }
        
        public final int getLevel() {
            return this.\u0269;
        }
    }
}
