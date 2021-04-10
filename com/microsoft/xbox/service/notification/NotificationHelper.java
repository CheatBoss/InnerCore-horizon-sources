package com.microsoft.xbox.service.notification;

import com.google.firebase.messaging.*;
import android.content.*;

public class NotificationHelper
{
    public static NotificationResult tryParseXboxLiveNotification(final RemoteMessage remoteMessage, final Context context) {
        return new NotificationResult(remoteMessage, context);
    }
}
