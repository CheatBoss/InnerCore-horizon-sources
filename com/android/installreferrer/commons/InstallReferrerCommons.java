package com.android.installreferrer.commons;

import android.util.*;

public final class InstallReferrerCommons
{
    public static void logVerbose(final String s, final String s2) {
        if (Log.isLoggable(s, 2)) {
            Log.v(s, s2);
        }
    }
    
    public static void logWarn(final String s, final String s2) {
        if (Log.isLoggable(s, 5)) {
            Log.w(s, s2);
        }
    }
}
