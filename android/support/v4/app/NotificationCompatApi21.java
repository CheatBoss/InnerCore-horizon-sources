package android.support.v4.app;

import android.os.*;
import android.app.*;
import android.content.*;
import android.widget.*;
import android.graphics.*;
import java.util.*;

class NotificationCompatApi21
{
    public static final String CATEGORY_ALARM = "alarm";
    public static final String CATEGORY_CALL = "call";
    public static final String CATEGORY_EMAIL = "email";
    public static final String CATEGORY_ERROR = "err";
    public static final String CATEGORY_EVENT = "event";
    public static final String CATEGORY_MESSAGE = "msg";
    public static final String CATEGORY_PROGRESS = "progress";
    public static final String CATEGORY_PROMO = "promo";
    public static final String CATEGORY_RECOMMENDATION = "recommendation";
    public static final String CATEGORY_SERVICE = "service";
    public static final String CATEGORY_SOCIAL = "social";
    public static final String CATEGORY_STATUS = "status";
    public static final String CATEGORY_SYSTEM = "sys";
    public static final String CATEGORY_TRANSPORT = "transport";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_MESSAGES = "messages";
    private static final String KEY_ON_READ = "on_read";
    private static final String KEY_ON_REPLY = "on_reply";
    private static final String KEY_PARTICIPANTS = "participants";
    private static final String KEY_REMOTE_INPUT = "remote_input";
    private static final String KEY_TEXT = "text";
    private static final String KEY_TIMESTAMP = "timestamp";
    
    private static RemoteInput fromCompatRemoteInput(final RemoteInputCompatBase.RemoteInput remoteInput) {
        return new RemoteInput$Builder(remoteInput.getResultKey()).setLabel(remoteInput.getLabel()).setChoices(remoteInput.getChoices()).setAllowFreeFormInput(remoteInput.getAllowFreeFormInput()).addExtras(remoteInput.getExtras()).build();
    }
    
    static Bundle getBundleForUnreadConversation(final NotificationCompatBase.UnreadConversation unreadConversation) {
        final String s = null;
        if (unreadConversation == null) {
            return null;
        }
        final Bundle bundle = new Bundle();
        final String[] participants = unreadConversation.getParticipants();
        int i = 0;
        String s2 = s;
        if (participants != null) {
            s2 = s;
            if (unreadConversation.getParticipants().length > 1) {
                s2 = unreadConversation.getParticipants()[0];
            }
        }
        Parcelable[] array;
        for (array = new Parcelable[unreadConversation.getMessages().length]; i < array.length; ++i) {
            final Bundle bundle2 = new Bundle();
            bundle2.putString("text", unreadConversation.getMessages()[i]);
            bundle2.putString("author", s2);
            array[i] = (Parcelable)bundle2;
        }
        bundle.putParcelableArray("messages", array);
        final RemoteInputCompatBase.RemoteInput remoteInput = unreadConversation.getRemoteInput();
        if (remoteInput != null) {
            bundle.putParcelable("remote_input", (Parcelable)fromCompatRemoteInput(remoteInput));
        }
        bundle.putParcelable("on_reply", (Parcelable)unreadConversation.getReplyPendingIntent());
        bundle.putParcelable("on_read", (Parcelable)unreadConversation.getReadPendingIntent());
        bundle.putStringArray("participants", unreadConversation.getParticipants());
        bundle.putLong("timestamp", unreadConversation.getLatestTimestamp());
        return bundle;
    }
    
    public static String getCategory(final Notification notification) {
        return notification.category;
    }
    
    static NotificationCompatBase.UnreadConversation getUnreadConversationFromBundle(final Bundle bundle, final NotificationCompatBase.Factory factory, final RemoteInputCompatBase.Factory factory2) {
        RemoteInputCompatBase.RemoteInput compatRemoteInput = null;
        if (bundle == null) {
            return null;
        }
        final Parcelable[] parcelableArray = bundle.getParcelableArray("messages");
        String[] array = null;
        Label_0106: {
            if (parcelableArray != null) {
                array = new String[parcelableArray.length];
                final boolean b = false;
                int i = 0;
                while (true) {
                    while (i < array.length) {
                        boolean b2;
                        if (!(parcelableArray[i] instanceof Bundle)) {
                            b2 = b;
                        }
                        else {
                            array[i] = ((Bundle)parcelableArray[i]).getString("text");
                            if (array[i] != null) {
                                ++i;
                                continue;
                            }
                            b2 = b;
                        }
                        if (b2) {
                            break Label_0106;
                        }
                        return null;
                    }
                    boolean b2 = true;
                    continue;
                }
            }
            array = null;
        }
        final PendingIntent pendingIntent = (PendingIntent)bundle.getParcelable("on_read");
        final PendingIntent pendingIntent2 = (PendingIntent)bundle.getParcelable("on_reply");
        final RemoteInput remoteInput = (RemoteInput)bundle.getParcelable("remote_input");
        final String[] stringArray = bundle.getStringArray("participants");
        if (stringArray != null && stringArray.length == 1) {
            if (remoteInput != null) {
                compatRemoteInput = toCompatRemoteInput(remoteInput, factory2);
            }
            return factory.build(array, compatRemoteInput, pendingIntent2, pendingIntent, stringArray, bundle.getLong("timestamp"));
        }
        return null;
    }
    
    private static RemoteInputCompatBase.RemoteInput toCompatRemoteInput(final RemoteInput remoteInput, final RemoteInputCompatBase.Factory factory) {
        return factory.build(remoteInput.getResultKey(), remoteInput.getLabel(), remoteInput.getChoices(), remoteInput.getAllowFreeFormInput(), remoteInput.getExtras());
    }
    
    public static class Builder implements NotificationBuilderWithBuilderAccessor, NotificationBuilderWithActions
    {
        private Notification$Builder b;
        
        public Builder(final Context context, final Notification notification, final CharSequence contentTitle, final CharSequence contentText, final CharSequence contentInfo, final RemoteViews remoteViews, final int number, final PendingIntent contentIntent, final PendingIntent pendingIntent, final Bitmap largeIcon, final int n, final int n2, final boolean b, final boolean showWhen, final boolean usesChronometer, final int priority, final CharSequence subText, final boolean localOnly, final String category, final ArrayList<String> list, final Bundle extras, final int color, final int visibility, final Notification publicVersion, final String group, final boolean groupSummary, final String sortKey) {
            final Notification$Builder setLights = new Notification$Builder(context).setWhen(notification.when).setShowWhen(showWhen).setSmallIcon(notification.icon, notification.iconLevel).setContent(notification.contentView).setTicker(notification.tickerText, remoteViews).setSound(notification.sound, notification.audioStreamType).setVibrate(notification.vibrate).setLights(notification.ledARGB, notification.ledOnMS, notification.ledOffMS);
            final int flags = notification.flags;
            final boolean b2 = false;
            this.b = setLights.setOngoing((flags & 0x2) != 0x0).setOnlyAlertOnce((notification.flags & 0x8) != 0x0).setAutoCancel((notification.flags & 0x10) != 0x0).setDefaults(notification.defaults).setContentTitle(contentTitle).setContentText(contentText).setSubText(subText).setContentInfo(contentInfo).setContentIntent(contentIntent).setDeleteIntent(notification.deleteIntent).setFullScreenIntent(pendingIntent, (notification.flags & 0x80) != 0x0 || b2).setLargeIcon(largeIcon).setNumber(number).setUsesChronometer(usesChronometer).setPriority(priority).setProgress(n, n2, b).setLocalOnly(localOnly).setExtras(extras).setGroup(group).setGroupSummary(groupSummary).setSortKey(sortKey).setCategory(category).setColor(color).setVisibility(visibility).setPublicVersion(publicVersion);
            final Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                this.b.addPerson((String)iterator.next());
            }
        }
        
        @Override
        public void addAction(final NotificationCompatBase.Action action) {
            NotificationCompatApi20.addAction(this.b, action);
        }
        
        @Override
        public Notification build() {
            return this.b.build();
        }
        
        @Override
        public Notification$Builder getBuilder() {
            return this.b;
        }
    }
}
