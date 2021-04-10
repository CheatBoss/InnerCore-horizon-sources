package com.appboy.push;

import com.appboy.*;
import com.appboy.configuration.*;
import android.content.*;
import android.os.*;
import android.app.*;
import android.support.v4.app.*;

public class AppboyNotificationFactory implements IAppboyNotificationFactory
{
    private static volatile AppboyNotificationFactory sInstance;
    
    public static AppboyNotificationFactory getInstance() {
        if (AppboyNotificationFactory.sInstance == null) {
            synchronized (AppboyNotificationFactory.class) {
                if (AppboyNotificationFactory.sInstance == null) {
                    AppboyNotificationFactory.sInstance = new AppboyNotificationFactory();
                }
            }
        }
        return AppboyNotificationFactory.sInstance;
    }
    
    @Override
    public Notification createNotification(final AppboyConfigurationProvider appboyConfigurationProvider, final Context context, final Bundle bundle, final Bundle bundle2) {
        return this.populateNotificationBuilder(appboyConfigurationProvider, context, bundle, bundle2).build();
    }
    
    public NotificationCompat.Builder populateNotificationBuilder(final AppboyConfigurationProvider appboyConfigurationProvider, final Context context, final Bundle bundle, final Bundle bundle2) {
        AppboyNotificationUtils.prefetchBitmapsIfNewlyReceivedStoryPush(context, bundle);
        final NotificationCompat.Builder setAutoCancel = new NotificationCompat.Builder(context).setAutoCancel(true);
        AppboyNotificationUtils.setTitleIfPresent(setAutoCancel, bundle);
        AppboyNotificationUtils.setContentIfPresent(setAutoCancel, bundle);
        AppboyNotificationUtils.setTickerIfPresent(setAutoCancel, bundle);
        AppboyNotificationUtils.setSetShowWhen(setAutoCancel, bundle);
        AppboyNotificationUtils.setContentIntentIfPresent(context, setAutoCancel, bundle);
        AppboyNotificationUtils.setDeleteIntent(context, setAutoCancel, bundle);
        AppboyNotificationUtils.setSmallIcon(appboyConfigurationProvider, setAutoCancel);
        AppboyNotificationUtils.setLargeIconIfPresentAndSupported(context, appboyConfigurationProvider, setAutoCancel, bundle);
        AppboyNotificationUtils.setSoundIfPresentAndSupported(setAutoCancel, bundle);
        AppboyNotificationUtils.setSummaryTextIfPresentAndSupported(setAutoCancel, bundle);
        AppboyNotificationUtils.setPriorityIfPresentAndSupported(setAutoCancel, bundle);
        AppboyNotificationUtils.setStyleIfSupported(context, setAutoCancel, bundle, bundle2);
        AppboyNotificationActionUtils.addNotificationActions(context, setAutoCancel, bundle);
        AppboyNotificationUtils.setAccentColorIfPresentAndSupported(appboyConfigurationProvider, setAutoCancel, bundle);
        AppboyNotificationUtils.setCategoryIfPresentAndSupported(setAutoCancel, bundle);
        AppboyNotificationUtils.setVisibilityIfPresentAndSupported(setAutoCancel, bundle);
        AppboyNotificationUtils.setPublicVersionIfPresentAndSupported(context, appboyConfigurationProvider, setAutoCancel, bundle);
        AppboyNotificationUtils.setNotificationChannelIfSupported(context, appboyConfigurationProvider, setAutoCancel, bundle);
        AppboyNotificationUtils.setNotificationBadgeNumberIfPresent(setAutoCancel, bundle);
        return setAutoCancel;
    }
}
