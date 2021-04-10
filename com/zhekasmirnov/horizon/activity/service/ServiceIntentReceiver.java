package com.zhekasmirnov.horizon.activity.service;

import android.content.*;
import android.os.*;

public class ServiceIntentReceiver extends BroadcastReceiver
{
    public void onReceive(final Context context, final Intent intent) {
        if (Build.VERSION.SDK_INT >= 21 && "android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            NotificationService.schedule(context);
        }
    }
}
