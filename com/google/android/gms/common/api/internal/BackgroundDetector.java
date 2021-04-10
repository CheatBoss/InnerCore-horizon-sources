package com.google.android.gms.common.api.internal;

import java.util.concurrent.atomic.*;
import java.util.*;
import android.content.*;
import android.os.*;
import android.content.res.*;
import com.google.android.gms.common.util.*;
import android.app.*;

public final class BackgroundDetector implements Application$ActivityLifecycleCallbacks, ComponentCallbacks2
{
    private static final BackgroundDetector zzem;
    private final AtomicBoolean zzen;
    private final AtomicBoolean zzeo;
    private final ArrayList<BackgroundStateChangeListener> zzep;
    private boolean zzeq;
    
    static {
        zzem = new BackgroundDetector();
    }
    
    private BackgroundDetector() {
        this.zzen = new AtomicBoolean();
        this.zzeo = new AtomicBoolean();
        this.zzep = new ArrayList<BackgroundStateChangeListener>();
        this.zzeq = false;
    }
    
    public static BackgroundDetector getInstance() {
        return BackgroundDetector.zzem;
    }
    
    public static void initialize(final Application application) {
        synchronized (BackgroundDetector.zzem) {
            if (!BackgroundDetector.zzem.zzeq) {
                application.registerActivityLifecycleCallbacks((Application$ActivityLifecycleCallbacks)BackgroundDetector.zzem);
                application.registerComponentCallbacks((ComponentCallbacks)BackgroundDetector.zzem);
                BackgroundDetector.zzem.zzeq = true;
            }
        }
    }
    
    private final void onBackgroundStateChanged(final boolean b) {
        synchronized (BackgroundDetector.zzem) {
            final ArrayList<BackgroundStateChangeListener> list = this.zzep;
            final int size = list.size();
            int i = 0;
            while (i < size) {
                final BackgroundStateChangeListener value = list.get(i);
                ++i;
                value.onBackgroundStateChanged(b);
            }
        }
    }
    
    public final void addListener(final BackgroundStateChangeListener backgroundStateChangeListener) {
        synchronized (BackgroundDetector.zzem) {
            this.zzep.add(backgroundStateChangeListener);
        }
    }
    
    public final boolean isInBackground() {
        return this.zzen.get();
    }
    
    public final void onActivityCreated(final Activity activity, final Bundle bundle) {
        final boolean compareAndSet = this.zzen.compareAndSet(true, false);
        this.zzeo.set(true);
        if (compareAndSet) {
            this.onBackgroundStateChanged(false);
        }
    }
    
    public final void onActivityDestroyed(final Activity activity) {
    }
    
    public final void onActivityPaused(final Activity activity) {
    }
    
    public final void onActivityResumed(final Activity activity) {
        final boolean compareAndSet = this.zzen.compareAndSet(true, false);
        this.zzeo.set(true);
        if (compareAndSet) {
            this.onBackgroundStateChanged(false);
        }
    }
    
    public final void onActivitySaveInstanceState(final Activity activity, final Bundle bundle) {
    }
    
    public final void onActivityStarted(final Activity activity) {
    }
    
    public final void onActivityStopped(final Activity activity) {
    }
    
    public final void onConfigurationChanged(final Configuration configuration) {
    }
    
    public final void onLowMemory() {
    }
    
    public final void onTrimMemory(final int n) {
        if (n == 20 && this.zzen.compareAndSet(false, true)) {
            this.zzeo.set(true);
            this.onBackgroundStateChanged(true);
        }
    }
    
    public final boolean readCurrentStateIfPossible(final boolean b) {
        if (!this.zzeo.get()) {
            if (!PlatformVersion.isAtLeastJellyBean()) {
                return b;
            }
            final ActivityManager$RunningAppProcessInfo activityManager$RunningAppProcessInfo = new ActivityManager$RunningAppProcessInfo();
            ActivityManager.getMyMemoryState(activityManager$RunningAppProcessInfo);
            if (!this.zzeo.getAndSet(true) && activityManager$RunningAppProcessInfo.importance > 100) {
                this.zzen.set(true);
            }
        }
        return this.isInBackground();
    }
    
    public interface BackgroundStateChangeListener
    {
        void onBackgroundStateChanged(final boolean p0);
    }
}
