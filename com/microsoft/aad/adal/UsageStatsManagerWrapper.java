package com.microsoft.aad.adal;

import android.content.*;
import android.app.usage.*;

public class UsageStatsManagerWrapper
{
    private static UsageStatsManagerWrapper sInstance;
    
    public static UsageStatsManagerWrapper getInstance() {
        synchronized (UsageStatsManagerWrapper.class) {
            if (UsageStatsManagerWrapper.sInstance == null) {
                UsageStatsManagerWrapper.sInstance = new UsageStatsManagerWrapper();
            }
            return UsageStatsManagerWrapper.sInstance;
        }
    }
    
    static void setInstance(final UsageStatsManagerWrapper sInstance) {
        synchronized (UsageStatsManagerWrapper.class) {
            UsageStatsManagerWrapper.sInstance = sInstance;
        }
    }
    
    public boolean isAppInactive(final Context context) {
        return ((UsageStatsManager)context.getSystemService("usagestats")).isAppInactive(context.getPackageName());
    }
}
