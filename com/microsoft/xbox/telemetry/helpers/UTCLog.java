package com.microsoft.xbox.telemetry.helpers;

import android.util.*;

public class UTCLog
{
    static final String UTCLOGTAG = "UTCLOGGING";
    
    public static void log(final String s, final Object... array) {
        try {
            final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            if (stackTrace.length > 3) {
                final String methodName = stackTrace[3].getMethodName();
                final StringBuilder sb = new StringBuilder();
                sb.append(String.format("%s: ", methodName));
                sb.append(s);
                Log.d("UTCLOGGING", String.format(sb.toString(), array));
                return;
            }
            Log.d("UTCLOGGING", String.format(s, array));
        }
        catch (Exception ex) {
            UTCError.trackException(ex, "UTCLog.log");
            if (ex.getMessage().equals("Format specifier: s")) {
                Log.e("UTCLOGGING", ex.getMessage());
            }
            Log.e("UTCLOGGING", ex.getMessage());
        }
    }
}
