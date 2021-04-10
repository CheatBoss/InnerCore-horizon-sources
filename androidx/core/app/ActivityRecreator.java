package androidx.core.app;

import java.lang.reflect.*;
import java.util.*;
import android.content.res.*;
import android.util.*;
import androidx.annotation.*;
import android.app.*;
import android.os.*;

@RestrictTo({ RestrictTo$Scope.LIBRARY })
final class ActivityRecreator
{
    private static final String LOG_TAG = "ActivityRecreator";
    protected static final Class<?> activityThreadClass;
    private static final Handler mainHandler;
    protected static final Field mainThreadField;
    protected static final Method performStopActivity2ParamsMethod;
    protected static final Method performStopActivity3ParamsMethod;
    protected static final Method requestRelaunchActivityMethod;
    protected static final Field tokenField;
    
    static {
        mainHandler = new Handler(Looper.getMainLooper());
        activityThreadClass = getActivityThreadClass();
        mainThreadField = getMainThreadField();
        tokenField = getTokenField();
        performStopActivity3ParamsMethod = getPerformStopActivity3Params(ActivityRecreator.activityThreadClass);
        performStopActivity2ParamsMethod = getPerformStopActivity2Params(ActivityRecreator.activityThreadClass);
        requestRelaunchActivityMethod = getRequestRelaunchActivityMethod(ActivityRecreator.activityThreadClass);
    }
    
    private ActivityRecreator() {
    }
    
    private static Class<?> getActivityThreadClass() {
        try {
            return Class.forName("android.app.ActivityThread");
        }
        catch (Throwable t) {
            return null;
        }
    }
    
    private static Field getMainThreadField() {
        try {
            final Field declaredField = Activity.class.getDeclaredField("mMainThread");
            declaredField.setAccessible(true);
            return declaredField;
        }
        catch (Throwable t) {
            return null;
        }
    }
    
    private static Method getPerformStopActivity2Params(final Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        try {
            final Method declaredMethod = clazz.getDeclaredMethod("performStopActivity", IBinder.class, Boolean.TYPE);
            declaredMethod.setAccessible(true);
            return declaredMethod;
        }
        catch (Throwable t) {
            return null;
        }
    }
    
    private static Method getPerformStopActivity3Params(final Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        try {
            final Method declaredMethod = clazz.getDeclaredMethod("performStopActivity", IBinder.class, Boolean.TYPE, String.class);
            declaredMethod.setAccessible(true);
            return declaredMethod;
        }
        catch (Throwable t) {
            return null;
        }
    }
    
    private static Method getRequestRelaunchActivityMethod(final Class<?> clazz) {
        if (needsRelaunchCall()) {
            if (clazz == null) {
                return null;
            }
            try {
                final Method declaredMethod = clazz.getDeclaredMethod("requestRelaunchActivity", IBinder.class, List.class, List.class, Integer.TYPE, Boolean.TYPE, Configuration.class, Configuration.class, Boolean.TYPE, Boolean.TYPE);
                declaredMethod.setAccessible(true);
                return declaredMethod;
            }
            catch (Throwable t) {
                return null;
            }
        }
        return null;
    }
    
    private static Field getTokenField() {
        try {
            final Field declaredField = Activity.class.getDeclaredField("mToken");
            declaredField.setAccessible(true);
            return declaredField;
        }
        catch (Throwable t) {
            return null;
        }
    }
    
    private static boolean needsRelaunchCall() {
        return Build$VERSION.SDK_INT == 26 || Build$VERSION.SDK_INT == 27;
    }
    
    protected static boolean queueOnStopIfNecessary(Object value, final Activity activity) {
        try {
            final Object value2 = ActivityRecreator.tokenField.get(activity);
            if (value2 != value) {
                return false;
            }
            value = ActivityRecreator.mainThreadField.get(activity);
            ActivityRecreator.mainHandler.postAtFrontOfQueue((Runnable)new Runnable() {
                @Override
                public void run() {
                    try {
                        if (ActivityRecreator.performStopActivity3ParamsMethod != null) {
                            ActivityRecreator.performStopActivity3ParamsMethod.invoke(value, value2, false, "AppCompat recreation");
                        }
                        else {
                            ActivityRecreator.performStopActivity2ParamsMethod.invoke(value, value2, false);
                        }
                    }
                    catch (Throwable t) {
                        Log.e("ActivityRecreator", "Exception while invoking performStopActivity", t);
                    }
                    catch (RuntimeException ex) {
                        if (ex.getClass() == RuntimeException.class && ex.getMessage() != null && ex.getMessage().startsWith("Unable to stop")) {
                            throw ex;
                        }
                    }
                }
            });
            return true;
        }
        catch (Throwable t) {
            Log.e("ActivityRecreator", "Exception while fetching field values", t);
            return false;
        }
    }
    
    static boolean recreate(@NonNull final Activity activity) {
        if (Build$VERSION.SDK_INT >= 28) {
            activity.recreate();
            return true;
        }
        if (needsRelaunchCall() && ActivityRecreator.requestRelaunchActivityMethod == null) {
            return false;
        }
        if (ActivityRecreator.performStopActivity2ParamsMethod == null && ActivityRecreator.performStopActivity3ParamsMethod == null) {
            return false;
        }
        try {
            final Object value = ActivityRecreator.tokenField.get(activity);
            if (value == null) {
                return false;
            }
            final Object value2 = ActivityRecreator.mainThreadField.get(activity);
            if (value2 == null) {
                return false;
            }
            final Application application = activity.getApplication();
            final LifecycleCheckCallbacks lifecycleCheckCallbacks = new LifecycleCheckCallbacks(activity);
            application.registerActivityLifecycleCallbacks((Application$ActivityLifecycleCallbacks)lifecycleCheckCallbacks);
            ActivityRecreator.mainHandler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    lifecycleCheckCallbacks.currentlyRecreatingToken = value;
                }
            });
            try {
                if (needsRelaunchCall()) {
                    ActivityRecreator.requestRelaunchActivityMethod.invoke(value2, value, null, null, 0, false, null, null, false, false);
                }
                else {
                    activity.recreate();
                }
                return true;
            }
            finally {
                ActivityRecreator.mainHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        application.unregisterActivityLifecycleCallbacks((Application$ActivityLifecycleCallbacks)lifecycleCheckCallbacks);
                    }
                });
            }
        }
        catch (Throwable t) {
            return false;
        }
    }
    
    private static final class LifecycleCheckCallbacks implements Application$ActivityLifecycleCallbacks
    {
        Object currentlyRecreatingToken;
        private Activity mActivity;
        private boolean mDestroyed;
        private boolean mStarted;
        private boolean mStopQueued;
        
        LifecycleCheckCallbacks(@NonNull final Activity mActivity) {
            this.mStarted = false;
            this.mDestroyed = false;
            this.mStopQueued = false;
            this.mActivity = mActivity;
        }
        
        public void onActivityCreated(final Activity activity, final Bundle bundle) {
        }
        
        public void onActivityDestroyed(final Activity activity) {
            if (this.mActivity == activity) {
                this.mActivity = null;
                this.mDestroyed = true;
            }
        }
        
        public void onActivityPaused(final Activity activity) {
            if (this.mDestroyed && !this.mStopQueued && !this.mStarted && ActivityRecreator.queueOnStopIfNecessary(this.currentlyRecreatingToken, activity)) {
                this.mStopQueued = true;
                this.currentlyRecreatingToken = null;
            }
        }
        
        public void onActivityResumed(final Activity activity) {
        }
        
        public void onActivitySaveInstanceState(final Activity activity, final Bundle bundle) {
        }
        
        public void onActivityStarted(final Activity activity) {
            if (this.mActivity == activity) {
                this.mStarted = true;
            }
        }
        
        public void onActivityStopped(final Activity activity) {
        }
    }
}
