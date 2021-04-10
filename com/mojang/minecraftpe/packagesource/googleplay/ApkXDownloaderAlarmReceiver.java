package com.mojang.minecraftpe.packagesource.googleplay;

import android.content.*;
import android.util.*;
import com.google.android.vending.expansion.downloader.*;
import android.content.pm.*;

public class ApkXDownloaderAlarmReceiver extends BroadcastReceiver
{
    public void onReceive(final Context context, final Intent intent) {
        Log.d("ApkXDownloaderAlarmReceiver", "Alarm received");
        try {
            DownloaderClientMarshaller.startDownloadServiceIfRequired(context, intent, ApkXDownloaderService.class);
        }
        catch (PackageManager$NameNotFoundException ex) {
            ex.printStackTrace();
            final StringBuilder sb = new StringBuilder();
            sb.append("Exception: ");
            sb.append(ex.getClass().getName());
            sb.append(":");
            sb.append(ex.getMessage());
            Log.d("ApkXDownloaderAlarmReceiver", sb.toString());
            ex.printStackTrace();
        }
    }
}
