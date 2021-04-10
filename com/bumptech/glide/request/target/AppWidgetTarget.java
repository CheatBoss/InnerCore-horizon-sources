package com.bumptech.glide.request.target;

import android.graphics.*;
import android.content.*;
import android.widget.*;
import android.appwidget.*;
import com.bumptech.glide.request.animation.*;

public class AppWidgetTarget extends SimpleTarget<Bitmap>
{
    private final ComponentName componentName;
    private final Context context;
    private final RemoteViews remoteViews;
    private final int viewId;
    private final int[] widgetIds;
    
    public AppWidgetTarget(final Context context, final RemoteViews remoteViews, final int viewId, final int n, final int n2, final ComponentName componentName) {
        super(n, n2);
        if (context == null) {
            throw new NullPointerException("Context can not be null!");
        }
        if (componentName == null) {
            throw new NullPointerException("ComponentName can not be null!");
        }
        if (remoteViews == null) {
            throw new NullPointerException("RemoteViews object can not be null!");
        }
        this.context = context;
        this.remoteViews = remoteViews;
        this.viewId = viewId;
        this.componentName = componentName;
        this.widgetIds = null;
    }
    
    public AppWidgetTarget(final Context context, final RemoteViews remoteViews, final int viewId, final int n, final int n2, final int... widgetIds) {
        super(n, n2);
        if (context == null) {
            throw new NullPointerException("Context can not be null!");
        }
        if (widgetIds == null) {
            throw new NullPointerException("WidgetIds can not be null!");
        }
        if (widgetIds.length == 0) {
            throw new IllegalArgumentException("WidgetIds must have length > 0");
        }
        if (remoteViews == null) {
            throw new NullPointerException("RemoteViews object can not be null!");
        }
        this.context = context;
        this.remoteViews = remoteViews;
        this.viewId = viewId;
        this.widgetIds = widgetIds;
        this.componentName = null;
    }
    
    public AppWidgetTarget(final Context context, final RemoteViews remoteViews, final int n, final ComponentName componentName) {
        this(context, remoteViews, n, Integer.MIN_VALUE, Integer.MIN_VALUE, componentName);
    }
    
    public AppWidgetTarget(final Context context, final RemoteViews remoteViews, final int n, final int... array) {
        this(context, remoteViews, n, Integer.MIN_VALUE, Integer.MIN_VALUE, array);
    }
    
    private void update() {
        final AppWidgetManager instance = AppWidgetManager.getInstance(this.context);
        if (this.componentName != null) {
            instance.updateAppWidget(this.componentName, this.remoteViews);
            return;
        }
        instance.updateAppWidget(this.widgetIds, this.remoteViews);
    }
    
    @Override
    public void onResourceReady(final Bitmap bitmap, final GlideAnimation<? super Bitmap> glideAnimation) {
        this.remoteViews.setImageViewBitmap(this.viewId, bitmap);
        this.update();
    }
}
