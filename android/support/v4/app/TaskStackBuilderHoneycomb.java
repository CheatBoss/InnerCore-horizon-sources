package android.support.v4.app;

import android.content.*;
import android.app.*;

class TaskStackBuilderHoneycomb
{
    public static PendingIntent getActivitiesPendingIntent(final Context context, final int n, final Intent[] array, final int n2) {
        return PendingIntent.getActivities(context, n, array, n2);
    }
}
