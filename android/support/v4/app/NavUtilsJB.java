package android.support.v4.app;

import android.app.*;
import android.content.*;
import android.content.pm.*;

class NavUtilsJB
{
    public static Intent getParentActivityIntent(final Activity activity) {
        return activity.getParentActivityIntent();
    }
    
    public static String getParentActivityName(final ActivityInfo activityInfo) {
        return activityInfo.parentActivityName;
    }
    
    public static void navigateUpTo(final Activity activity, final Intent intent) {
        activity.navigateUpTo(intent);
    }
    
    public static boolean shouldUpRecreateTask(final Activity activity, final Intent intent) {
        return activity.shouldUpRecreateTask(intent);
    }
}
