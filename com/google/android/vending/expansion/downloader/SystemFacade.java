package com.google.android.vending.expansion.downloader;

import android.util.*;
import android.net.*;
import android.telephony.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;

class SystemFacade
{
    private Context mContext;
    private NotificationManager mNotificationManager;
    
    public SystemFacade(final Context mContext) {
        this.mContext = mContext;
        this.mNotificationManager = (NotificationManager)mContext.getSystemService("notification");
    }
    
    public void cancelAllNotifications() {
        this.mNotificationManager.cancelAll();
    }
    
    public void cancelNotification(final long n) {
        this.mNotificationManager.cancel((int)n);
    }
    
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
    
    public Integer getActiveNetworkType() {
        final ConnectivityManager connectivityManager = (ConnectivityManager)this.mContext.getSystemService("connectivity");
        if (connectivityManager == null) {
            Log.w("LVLDL", "couldn't get connectivity manager");
            return null;
        }
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            return null;
        }
        return activeNetworkInfo.getType();
    }
    
    public Long getMaxBytesOverMobile() {
        return 2147483647L;
    }
    
    public Long getRecommendedMaxBytesOverMobile() {
        return 2097152L;
    }
    
    public boolean isNetworkRoaming() {
        final ConnectivityManager connectivityManager = (ConnectivityManager)this.mContext.getSystemService("connectivity");
        final boolean b = false;
        String s;
        if (connectivityManager == null) {
            s = "couldn't get connectivity manager";
        }
        else {
            final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            final boolean b2 = activeNetworkInfo != null && activeNetworkInfo.getType() == 0;
            final TelephonyManager telephonyManager = (TelephonyManager)this.mContext.getSystemService("phone");
            if (telephonyManager != null) {
                boolean b3 = b;
                if (b2) {
                    b3 = b;
                    if (telephonyManager.isNetworkRoaming()) {
                        b3 = true;
                    }
                }
                return b3;
            }
            s = "couldn't get telephony manager";
        }
        Log.w("LVLDL", s);
        return false;
    }
    
    public void postNotification(final long n, final Notification notification) {
        this.mNotificationManager.notify((int)n, notification);
    }
    
    public void sendBroadcast(final Intent intent) {
        this.mContext.sendBroadcast(intent);
    }
    
    public void startThread(final Thread thread) {
        thread.start();
    }
    
    public boolean userOwnsPackage(final int n, final String s) throws PackageManager$NameNotFoundException {
        final PackageManager packageManager = this.mContext.getPackageManager();
        boolean b = false;
        if (packageManager.getApplicationInfo(s, 0).uid == n) {
            b = true;
        }
        return b;
    }
}
