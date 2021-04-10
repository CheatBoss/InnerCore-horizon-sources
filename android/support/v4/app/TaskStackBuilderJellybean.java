package android.support.v4.app;

import android.content.*;
import android.os.*;
import android.app.*;

class TaskStackBuilderJellybean
{
    public static PendingIntent getActivitiesPendingIntent(final Context context, final int n, final Intent[] array, final int n2, final Bundle bundle) {
        return PendingIntent.getActivities(context, n, array, n2, bundle);
    }
}
