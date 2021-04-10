package com.appsflyer;

import android.content.*;
import java.util.concurrent.*;
import android.os.*;
import java.util.*;
import android.app.*;

public class Foreground
{
    public static long CHECK_DELAY = 500L;
    public static Listener listener;
    
    static void \u0269(final Context context, final Listener listener) {
        Foreground.listener = listener;
        final Application$ActivityLifecycleCallbacks application$ActivityLifecycleCallbacks = (Application$ActivityLifecycleCallbacks)new Application$ActivityLifecycleCallbacks() {
            private Executor \u01c3 = Executors.newSingleThreadExecutor();
            boolean \u0269 = true;
            boolean \u0399;
            
            public final void onActivityCreated(final Activity activity, final Bundle bundle) {
                this.\u01c3.execute(new Runnable() {
                    @Override
                    public final void run() {
                        AFDeepLinkManager.getInstance().collectIntentsFromActivities(activity.getIntent());
                    }
                });
            }
            
            public final void onActivityDestroyed(final Activity activity) {
            }
            
            public final void onActivityPaused(final Activity activity) {
                this.\u01c3.execute(new Runnable() {
                    @Override
                    public final void run() {
                        Application$ActivityLifecycleCallbacks.this.\u0269 = true;
                        final Context applicationContext = ((Context)activity).getApplicationContext();
                        try {
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public final void run() {
                                    if (Application$ActivityLifecycleCallbacks.this.\u0399 && Application$ActivityLifecycleCallbacks.this.\u0269) {
                                        Application$ActivityLifecycleCallbacks.this.\u0399 = false;
                                        try {
                                            listener.onBecameBackground(applicationContext);
                                        }
                                        catch (Exception ex) {
                                            AFLogger.afErrorLog("Listener threw exception! ", ex);
                                        }
                                    }
                                }
                            }, Foreground.CHECK_DELAY);
                        }
                        finally {
                            final Throwable t;
                            AFLogger.afErrorLog("Background task failed with a throwable: ", t);
                        }
                    }
                });
            }
            
            public final void onActivityResumed(final Activity activity) {
                this.\u01c3.execute(new Runnable() {
                    @Override
                    public final void run() {
                        if (!Application$ActivityLifecycleCallbacks.this.\u0399) {
                            try {
                                listener.onBecameForeground(activity);
                            }
                            catch (Exception ex) {
                                AFLogger.afErrorLog("Listener thrown an exception: ", ex);
                            }
                        }
                        Application$ActivityLifecycleCallbacks.this.\u0269 = false;
                        Application$ActivityLifecycleCallbacks.this.\u0399 = true;
                    }
                });
            }
            
            public final void onActivitySaveInstanceState(final Activity activity, final Bundle bundle) {
            }
            
            public final void onActivityStarted(final Activity activity) {
            }
            
            public final void onActivityStopped(final Activity activity) {
            }
        };
        if (context instanceof Activity) {
            ((Application$ActivityLifecycleCallbacks)application$ActivityLifecycleCallbacks).onActivityResumed((Activity)context);
        }
        ((Application)context.getApplicationContext()).registerActivityLifecycleCallbacks((Application$ActivityLifecycleCallbacks)application$ActivityLifecycleCallbacks);
    }
    
    public interface Listener
    {
        void onBecameBackground(final Context p0);
        
        void onBecameForeground(final Activity p0);
    }
}
