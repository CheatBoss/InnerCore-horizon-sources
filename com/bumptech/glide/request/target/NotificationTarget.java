package com.bumptech.glide.request.target;

import android.graphics.*;
import android.content.*;
import android.widget.*;
import android.app.*;
import com.bumptech.glide.request.animation.*;

public class NotificationTarget extends SimpleTarget<Bitmap>
{
    private final Context context;
    private final Notification notification;
    private final int notificationId;
    private final RemoteViews remoteViews;
    private final int viewId;
    
    public NotificationTarget(final Context context, final RemoteViews remoteViews, final int viewId, final int n, final int n2, final Notification notification, final int notificationId) {
        super(n, n2);
        if (context == null) {
            throw new NullPointerException("Context must not be null!");
        }
        if (notification == null) {
            throw new NullPointerException("Notification object can not be null!");
        }
        if (remoteViews == null) {
            throw new NullPointerException("RemoteViews object can not be null!");
        }
        this.context = context;
        this.viewId = viewId;
        this.notification = notification;
        this.notificationId = notificationId;
        this.remoteViews = remoteViews;
    }
    
    public NotificationTarget(final Context context, final RemoteViews remoteViews, final int n, final Notification notification, final int n2) {
        this(context, remoteViews, n, Integer.MIN_VALUE, Integer.MIN_VALUE, notification, n2);
    }
    
    private void update() {
        ((NotificationManager)this.context.getSystemService("notification")).notify(this.notificationId, this.notification);
    }
    
    @Override
    public void onResourceReady(final Bitmap bitmap, final GlideAnimation<? super Bitmap> glideAnimation) {
        this.remoteViews.setImageViewBitmap(this.viewId, bitmap);
        this.update();
    }
}
