package com.google.android.gms.common.util;

import android.os.*;

public class DefaultClock implements Clock
{
    private static final DefaultClock zzzk;
    
    static {
        zzzk = new DefaultClock();
    }
    
    private DefaultClock() {
    }
    
    public static Clock getInstance() {
        return DefaultClock.zzzk;
    }
    
    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
    
    @Override
    public long elapsedRealtime() {
        return SystemClock.elapsedRealtime();
    }
    
    @Override
    public long nanoTime() {
        return System.nanoTime();
    }
}
