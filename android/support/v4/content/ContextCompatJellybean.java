package android.support.v4.content;

import android.content.*;
import android.os.*;

class ContextCompatJellybean
{
    public static void startActivities(final Context context, final Intent[] array, final Bundle bundle) {
        context.startActivities(array, bundle);
    }
}
