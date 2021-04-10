package android.support.v4.app;

import java.util.*;
import android.os.*;
import android.content.*;
import android.widget.*;
import android.app.*;
import android.graphics.*;

class NotificationCompatApi20
{
    public static void addAction(final Notification$Builder notification$Builder, final NotificationCompatBase.Action action) {
        final Notification$Action$Builder notification$Action$Builder = new Notification$Action$Builder(action.getIcon(), action.getTitle(), action.getActionIntent());
        if (action.getRemoteInputs() != null) {
            final RemoteInput[] fromCompat = RemoteInputCompatApi20.fromCompat(action.getRemoteInputs());
            for (int length = fromCompat.length, i = 0; i < length; ++i) {
                notification$Action$Builder.addRemoteInput(fromCompat[i]);
            }
        }
        if (action.getExtras() != null) {
            notification$Action$Builder.addExtras(action.getExtras());
        }
        notification$Builder.addAction(notification$Action$Builder.build());
    }
    
    public static NotificationCompatBase.Action getAction(final Notification notification, final int n, final NotificationCompatBase.Factory factory, final RemoteInputCompatBase.Factory factory2) {
        return getActionCompatFromAction(notification.actions[n], factory, factory2);
    }
    
    private static NotificationCompatBase.Action getActionCompatFromAction(final Notification$Action notification$Action, final NotificationCompatBase.Factory factory, final RemoteInputCompatBase.Factory factory2) {
        return factory.build(notification$Action.icon, notification$Action.title, notification$Action.actionIntent, notification$Action.getExtras(), RemoteInputCompatApi20.toCompat(notification$Action.getRemoteInputs(), factory2));
    }
    
    private static Notification$Action getActionFromActionCompat(final NotificationCompatBase.Action action) {
        final Notification$Action$Builder addExtras = new Notification$Action$Builder(action.getIcon(), action.getTitle(), action.getActionIntent()).addExtras(action.getExtras());
        final RemoteInputCompatBase.RemoteInput[] remoteInputs = action.getRemoteInputs();
        if (remoteInputs != null) {
            final RemoteInput[] fromCompat = RemoteInputCompatApi20.fromCompat(remoteInputs);
            for (int length = fromCompat.length, i = 0; i < length; ++i) {
                addExtras.addRemoteInput(fromCompat[i]);
            }
        }
        return addExtras.build();
    }
    
    public static NotificationCompatBase.Action[] getActionsFromParcelableArrayList(final ArrayList<Parcelable> list, final NotificationCompatBase.Factory factory, final RemoteInputCompatBase.Factory factory2) {
        if (list == null) {
            return null;
        }
        final NotificationCompatBase.Action[] array = factory.newArray(list.size());
        for (int i = 0; i < array.length; ++i) {
            array[i] = getActionCompatFromAction((Notification$Action)list.get(i), factory, factory2);
        }
        return array;
    }
    
    public static String getGroup(final Notification notification) {
        return notification.getGroup();
    }
    
    public static boolean getLocalOnly(final Notification notification) {
        return (notification.flags & 0x100) != 0x0;
    }
    
    public static ArrayList<Parcelable> getParcelableArrayListForActions(final NotificationCompatBase.Action[] array) {
        if (array == null) {
            return null;
        }
        final ArrayList<Parcelable> list = new ArrayList<Parcelable>(array.length);
        for (int length = array.length, i = 0; i < length; ++i) {
            list.add((Parcelable)getActionFromActionCompat(array[i]));
        }
        return list;
    }
    
    public static String getSortKey(final Notification notification) {
        return notification.getSortKey();
    }
    
    public static boolean isGroupSummary(final Notification notification) {
        return (notification.flags & 0x200) != 0x0;
    }
    
    public static class Builder implements NotificationBuilderWithBuilderAccessor, NotificationBuilderWithActions
    {
        private Notification$Builder b;
        private Bundle mExtras;
        
        public Builder(final Context context, final Notification notification, final CharSequence contentTitle, final CharSequence contentText, final CharSequence contentInfo, final RemoteViews remoteViews, final int number, final PendingIntent contentIntent, final PendingIntent pendingIntent, final Bitmap largeIcon, final int n, final int n2, final boolean b, final boolean showWhen, final boolean usesChronometer, final int priority, final CharSequence subText, final boolean localOnly, final ArrayList<String> list, final Bundle bundle, final String group, final boolean groupSummary, final String sortKey) {
            final Notification$Builder setLights = new Notification$Builder(context).setWhen(notification.when).setShowWhen(showWhen).setSmallIcon(notification.icon, notification.iconLevel).setContent(notification.contentView).setTicker(notification.tickerText, remoteViews).setSound(notification.sound, notification.audioStreamType).setVibrate(notification.vibrate).setLights(notification.ledARGB, notification.ledOnMS, notification.ledOffMS);
            final int flags = notification.flags;
            final boolean b2 = false;
            this.b = setLights.setOngoing((flags & 0x2) != 0x0).setOnlyAlertOnce((notification.flags & 0x8) != 0x0).setAutoCancel((notification.flags & 0x10) != 0x0).setDefaults(notification.defaults).setContentTitle(contentTitle).setContentText(contentText).setSubText(subText).setContentInfo(contentInfo).setContentIntent(contentIntent).setDeleteIntent(notification.deleteIntent).setFullScreenIntent(pendingIntent, (notification.flags & 0x80) != 0x0 || b2).setLargeIcon(largeIcon).setNumber(number).setUsesChronometer(usesChronometer).setPriority(priority).setProgress(n, n2, b).setLocalOnly(localOnly).setGroup(group).setGroupSummary(groupSummary).setSortKey(sortKey);
            this.mExtras = new Bundle();
            if (bundle != null) {
                this.mExtras.putAll(bundle);
            }
            if (list != null && !list.isEmpty()) {
                this.mExtras.putStringArray("android.people", (String[])list.toArray(new String[list.size()]));
            }
        }
        
        @Override
        public void addAction(final NotificationCompatBase.Action action) {
            NotificationCompatApi20.addAction(this.b, action);
        }
        
        @Override
        public Notification build() {
            this.b.setExtras(this.mExtras);
            return this.b.build();
        }
        
        @Override
        public Notification$Builder getBuilder() {
            return this.b;
        }
    }
}
