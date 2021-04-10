package com.appboy;

import com.appboy.configuration.*;
import android.content.*;
import android.os.*;
import android.app.*;

public interface IAppboyNotificationFactory
{
    Notification createNotification(final AppboyConfigurationProvider p0, final Context p1, final Bundle p2, final Bundle p3);
}
