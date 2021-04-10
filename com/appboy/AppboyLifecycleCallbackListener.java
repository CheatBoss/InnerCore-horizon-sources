package com.appboy;

import android.app.*;
import com.appboy.push.*;
import android.os.*;
import com.appboy.ui.inappmessage.*;

public class AppboyLifecycleCallbackListener implements Application$ActivityLifecycleCallbacks
{
    private final boolean mRegisterInAppMessageManager;
    private final boolean mSessionHandlingEnabled;
    
    public AppboyLifecycleCallbackListener() {
        this(true, true);
    }
    
    public AppboyLifecycleCallbackListener(final boolean mSessionHandlingEnabled, final boolean mRegisterInAppMessageManager) {
        this.mRegisterInAppMessageManager = mRegisterInAppMessageManager;
        this.mSessionHandlingEnabled = mSessionHandlingEnabled;
    }
    
    private static boolean shouldIgnoreActivity(final Activity activity) {
        return activity.getClass().equals(AppboyNotificationRoutingActivity.class);
    }
    
    public void onActivityCreated(final Activity activity, final Bundle bundle) {
        if (this.mRegisterInAppMessageManager && !shouldIgnoreActivity(activity)) {
            AppboyInAppMessageManager.getInstance().ensureSubscribedToInAppMessageEvents(activity.getApplicationContext());
        }
    }
    
    public void onActivityDestroyed(final Activity activity) {
    }
    
    public void onActivityPaused(final Activity activity) {
        if (this.mRegisterInAppMessageManager && !shouldIgnoreActivity(activity)) {
            AppboyInAppMessageManager.getInstance().unregisterInAppMessageManager(activity);
        }
    }
    
    public void onActivityResumed(final Activity activity) {
        if (this.mRegisterInAppMessageManager && !shouldIgnoreActivity(activity)) {
            AppboyInAppMessageManager.getInstance().registerInAppMessageManager(activity);
        }
    }
    
    public void onActivitySaveInstanceState(final Activity activity, final Bundle bundle) {
    }
    
    public void onActivityStarted(final Activity activity) {
        if (this.mSessionHandlingEnabled && !shouldIgnoreActivity(activity)) {
            Appboy.getInstance(activity.getApplicationContext()).openSession(activity);
        }
    }
    
    public void onActivityStopped(final Activity activity) {
        if (this.mSessionHandlingEnabled && !shouldIgnoreActivity(activity)) {
            Appboy.getInstance(activity.getApplicationContext()).closeSession(activity);
        }
    }
}
