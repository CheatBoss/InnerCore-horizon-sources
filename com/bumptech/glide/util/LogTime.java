package com.bumptech.glide.util;

import android.os.*;
import android.annotation.*;

public final class LogTime
{
    private static final double MILLIS_MULTIPLIER;
    
    static {
        final int sdk_INT = Build$VERSION.SDK_INT;
        double millis_MULTIPLIER = 1.0;
        if (17 <= sdk_INT) {
            millis_MULTIPLIER = 1.0 / Math.pow(10.0, 6.0);
        }
        MILLIS_MULTIPLIER = millis_MULTIPLIER;
    }
    
    private LogTime() {
    }
    
    public static double getElapsedMillis(final long n) {
        return (getLogTime() - n) * LogTime.MILLIS_MULTIPLIER;
    }
    
    @TargetApi(17)
    public static long getLogTime() {
        if (17 <= Build$VERSION.SDK_INT) {
            return SystemClock.elapsedRealtimeNanos();
        }
        return System.currentTimeMillis();
    }
}
