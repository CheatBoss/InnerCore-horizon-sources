package android.support.customtabs;

import android.os.*;

public class CustomTabsCallback
{
    public static final int NAVIGATION_ABORTED = 4;
    public static final int NAVIGATION_FAILED = 3;
    public static final int NAVIGATION_FINISHED = 2;
    public static final int NAVIGATION_STARTED = 1;
    public static final int TAB_HIDDEN = 6;
    public static final int TAB_SHOWN = 5;
    
    public void extraCallback(final String s, final Bundle bundle) {
    }
    
    public void onNavigationEvent(final int n, final Bundle bundle) {
    }
}
