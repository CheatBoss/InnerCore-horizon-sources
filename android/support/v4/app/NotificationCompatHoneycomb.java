package android.support.v4.app;

import android.content.*;
import android.widget.*;
import android.graphics.*;
import android.app.*;

class NotificationCompatHoneycomb
{
    static Notification add(final Context context, final Notification notification, final CharSequence contentTitle, final CharSequence contentText, final CharSequence contentInfo, final RemoteViews remoteViews, final int number, final PendingIntent contentIntent, final PendingIntent pendingIntent, final Bitmap largeIcon) {
        final Notification$Builder setLights = new Notification$Builder(context).setWhen(notification.when).setSmallIcon(notification.icon, notification.iconLevel).setContent(notification.contentView).setTicker(notification.tickerText, remoteViews).setSound(notification.sound, notification.audioStreamType).setVibrate(notification.vibrate).setLights(notification.ledARGB, notification.ledOnMS, notification.ledOffMS);
        final int flags = notification.flags;
        final boolean b = false;
        final Notification$Builder setDeleteIntent = setLights.setOngoing((flags & 0x2) != 0x0).setOnlyAlertOnce((notification.flags & 0x8) != 0x0).setAutoCancel((notification.flags & 0x10) != 0x0).setDefaults(notification.defaults).setContentTitle(contentTitle).setContentText(contentText).setContentInfo(contentInfo).setContentIntent(contentIntent).setDeleteIntent(notification.deleteIntent);
        boolean b2 = b;
        if ((notification.flags & 0x80) != 0x0) {
            b2 = true;
        }
        return setDeleteIntent.setFullScreenIntent(pendingIntent, b2).setLargeIcon(largeIcon).setNumber(number).getNotification();
    }
}
