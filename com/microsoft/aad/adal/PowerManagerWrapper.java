package com.microsoft.aad.adal;

import android.content.*;
import android.os.*;

public class PowerManagerWrapper
{
    private static PowerManagerWrapper sInstance;
    
    public static PowerManagerWrapper getInstance() {
        synchronized (PowerManagerWrapper.class) {
            if (PowerManagerWrapper.sInstance == null) {
                PowerManagerWrapper.sInstance = new PowerManagerWrapper();
            }
            return PowerManagerWrapper.sInstance;
        }
    }
    
    static void setInstance(final PowerManagerWrapper sInstance) {
        PowerManagerWrapper.sInstance = sInstance;
    }
    
    public boolean isDeviceIdleMode(final Context context) {
        return ((PowerManager)context.getSystemService("power")).isDeviceIdleMode();
    }
    
    public boolean isIgnoringBatteryOptimizations(final Context context) {
        return ((PowerManager)context.getSystemService("power")).isIgnoringBatteryOptimizations(context.getPackageName());
    }
}
