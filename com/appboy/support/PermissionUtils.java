package com.appboy.support;

import android.content.*;

public class PermissionUtils
{
    private static final String a;
    
    static {
        a = AppboyLogger.getAppboyLogTag(PermissionUtils.class);
    }
    
    public static boolean hasPermission(final Context context, final String s) {
        boolean b = false;
        try {
            if (context.checkCallingOrSelfPermission(s) == 0) {
                b = true;
            }
            return b;
        }
        finally {
            final String a = PermissionUtils.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failure checking permission ");
            sb.append(s);
            final Throwable t;
            AppboyLogger.e(a, sb.toString(), t);
            return false;
        }
    }
}
