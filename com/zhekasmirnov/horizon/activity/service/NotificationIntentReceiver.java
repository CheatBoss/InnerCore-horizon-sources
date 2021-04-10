package com.zhekasmirnov.horizon.activity.service;

import android.content.*;
import java.util.*;
import android.app.*;

public class NotificationIntentReceiver extends BroadcastReceiver
{
    private static final HashMap<String, List<IActionListener>> listeners;
    private static int intentRequestCode;
    
    public static void addListener(final String action, final IActionListener listener) {
        List<IActionListener> listenersForAction = NotificationIntentReceiver.listeners.get(action);
        if (listenersForAction == null) {
            listenersForAction = new ArrayList<IActionListener>();
            NotificationIntentReceiver.listeners.put(action, listenersForAction);
        }
        listenersForAction.add(listener);
    }
    
    public void onReceive(final Context context, final Intent intent) {
        final List<IActionListener> listenersForIntent = NotificationIntentReceiver.listeners.get(intent.getStringExtra("notification_action"));
        if (listenersForIntent != null) {
            for (final IActionListener listener : listenersForIntent) {
                listener.onAction(context, intent);
            }
        }
    }
    
    public static Intent makeIntent(final Context context, final String action) {
        return new Intent(context, (Class)NotificationIntentReceiver.class).putExtra("notification_action", action);
    }
    
    public static PendingIntent makePendingIntent(final Context context, final Intent intent) {
        return PendingIntent.getBroadcast(context, NotificationIntentReceiver.intentRequestCode++, intent, 134217728);
    }
    
    static {
        listeners = new HashMap<String, List<IActionListener>>();
        NotificationIntentReceiver.intentRequestCode = 0;
    }
    
    public interface IActionListener
    {
        void onAction(final Context p0, final Intent p1);
    }
}
