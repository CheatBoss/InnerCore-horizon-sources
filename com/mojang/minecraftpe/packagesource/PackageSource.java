package com.mojang.minecraftpe.packagesource;

import java.util.*;
import android.util.*;

public abstract class PackageSource
{
    static final EnumMap<StringResourceId, String> stringMap;
    
    static {
        stringMap = new EnumMap<StringResourceId, String>(StringResourceId.class);
    }
    
    public static String getStringResource(final StringResourceId stringResourceId) {
        if (PackageSource.stringMap.containsKey(stringResourceId)) {
            return PackageSource.stringMap.get(stringResourceId);
        }
        Log.e("PackageSource", String.format("getStringResource - id: %s is not set.", stringResourceId.name()));
        return stringResourceId.name();
    }
    
    public static void setStringResource(final int n, final String s) {
        setStringResource(StringResourceId.fromInt(n), s);
    }
    
    public static void setStringResource(final StringResourceId stringResourceId, final String s) {
        if (PackageSource.stringMap.containsKey(stringResourceId)) {
            Log.w("PackageSource", String.format("setStringResource - id: %s already set.", stringResourceId.name()));
        }
        PackageSource.stringMap.put(stringResourceId, s);
    }
    
    public abstract void abortDownload();
    
    public abstract void destructor();
    
    public abstract void downloadFiles(final String p0, final long p1, final boolean p2, final boolean p3);
    
    public abstract String getDownloadDirectoryPath();
    
    public abstract String getMountPath(final String p0);
    
    public abstract void mountFiles(final String p0);
    
    public abstract void pauseDownload();
    
    public abstract void resumeDownload();
    
    public abstract void resumeDownloadOnCell();
    
    public abstract void unmountFiles(final String p0);
    
    public enum StringResourceId
    {
        KILOBYTES_PER_SECOND(18), 
        NOTIFICATIONCHANNEL_DESCRIPTION(21), 
        NOTIFICATIONCHANNEL_NAME(20), 
        STATE_COMPLETED(5), 
        STATE_CONNECTING(3), 
        STATE_DOWNLOADING(4), 
        STATE_FAILED(17), 
        STATE_FAILED_CANCELLED(16), 
        STATE_FAILED_FETCHING_URL(14), 
        STATE_FAILED_SDCARD_FULL(15), 
        STATE_FAILED_UNLICENSED(13), 
        STATE_FETCHING_URL(2), 
        STATE_IDLE(1), 
        STATE_PAUSED_BY_REQUEST(8), 
        STATE_PAUSED_NETWORK_SETUP_FAILURE(7), 
        STATE_PAUSED_NETWORK_UNAVAILABLE(6), 
        STATE_PAUSED_ROAMING(11), 
        STATE_PAUSED_SDCARD_UNAVAILABLE(12), 
        STATE_PAUSED_WIFI_DISABLED(10), 
        STATE_PAUSED_WIFI_UNAVAILABLE(9), 
        STATE_UNKNOWN(0), 
        TIME_REMAINING_NOTIFICATION(19);
        
        private final int value;
        
        private StringResourceId(final int value) {
            this.value = value;
        }
        
        public static StringResourceId fromInt(final int n) {
            final StringResourceId[] values = values();
            for (int i = 0; i < values.length; ++i) {
                if (values[i].getValue() == n) {
                    return values[i];
                }
            }
            throw new IllegalArgumentException("Invalid value");
        }
        
        public int getValue() {
            return this.value;
        }
    }
}
