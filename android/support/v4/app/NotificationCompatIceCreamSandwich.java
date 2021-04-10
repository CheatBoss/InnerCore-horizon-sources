package android.support.v4.app;

import android.content.*;
import android.widget.*;
import android.app.*;
import android.graphics.*;

class NotificationCompatIceCreamSandwich
{
    public static class Builder implements NotificationBuilderWithBuilderAccessor
    {
        private Notification$Builder b;
        
        public Builder(final Context context, final Notification notification, final CharSequence contentTitle, final CharSequence contentText, final CharSequence contentInfo, final RemoteViews remoteViews, final int number, final PendingIntent contentIntent, final PendingIntent pendingIntent, final Bitmap largeIcon, final int n, final int n2, final boolean b) {
            final Notification$Builder setLights = new Notification$Builder(context).setWhen(notification.when).setSmallIcon(notification.icon, notification.iconLevel).setContent(notification.contentView).setTicker(notification.tickerText, remoteViews).setSound(notification.sound, notification.audioStreamType).setVibrate(notification.vibrate).setLights(notification.ledARGB, notification.ledOnMS, notification.ledOffMS);
            final int flags = notification.flags;
            final boolean b2 = false;
            this.b = setLights.setOngoing((flags & 0x2) != 0x0).setOnlyAlertOnce((notification.flags & 0x8) != 0x0).setAutoCancel((notification.flags & 0x10) != 0x0).setDefaults(notification.defaults).setContentTitle(contentTitle).setContentText(contentText).setContentInfo(contentInfo).setContentIntent(contentIntent).setDeleteIntent(notification.deleteIntent).setFullScreenIntent(pendingIntent, (notification.flags & 0x80) != 0x0 || b2).setLargeIcon(largeIcon).setNumber(number).setProgress(n, n2, b);
        }
        
        @Override
        public Notification build() {
            return this.b.getNotification();
        }
        
        @Override
        public Notification$Builder getBuilder() {
            return this.b;
        }
    }
}
