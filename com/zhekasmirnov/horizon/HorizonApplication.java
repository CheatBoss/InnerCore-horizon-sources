package com.zhekasmirnov.horizon;

import android.support.multidex.*;
import java.lang.ref.*;
import com.google.firebase.*;
import com.google.firebase.analytics.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import com.zhekasmirnov.horizon.util.*;
import java.io.*;
import java.util.*;
import com.zhekasmirnov.horizon.activity.main.*;
import android.content.*;
import android.app.*;
import android.os.*;

public class HorizonApplication extends MultiDexApplication implements Thread.UncaughtExceptionHandler
{
    private static WeakReference<HorizonApplication> instance;
    private final List<Activity> activityStack;
    private final List<Activity> runningActivities;
    private final HashSet<Activity> activitiesOnTop;
    private Thread.UncaughtExceptionHandler systemExceptionHandler;
    private static final HashMap<String, ApplicationLock> applicationLocks;
    private FirebaseApp mFirebaseApp;
    private FirebaseAnalytics mFirebaseAnalytics;
    
    public static HorizonApplication getInstance() {
        return (HorizonApplication.instance != null) ? HorizonApplication.instance.get() : null;
    }
    
    public HorizonApplication() {
        this.activityStack = new ArrayList<Activity>();
        this.runningActivities = new ArrayList<Activity>();
        this.activitiesOnTop = new HashSet<Activity>();
        this.systemExceptionHandler = null;
        this.mFirebaseApp = null;
        this.mFirebaseAnalytics = null;
        HorizonApplication.instance = new WeakReference<HorizonApplication>(this);
        this.registerActivityLifecycleCallbacks((Application.ActivityLifecycleCallbacks)new Application.ActivityLifecycleCallbacks() {
            public void onActivityCreated(final Activity activity, final Bundle savedInstanceState) {
                synchronized (HorizonApplication.this.activityStack) {
                    HorizonApplication.this.activityStack.add(0, activity);
                    HorizonApplication.this.runningActivities.add(activity);
                }
            }
            
            public void onActivityStarted(final Activity activity) {
                synchronized (HorizonApplication.this.activityStack) {
                    if (!HorizonApplication.this.runningActivities.contains(activity)) {
                        HorizonApplication.this.runningActivities.add(activity);
                    }
                }
                synchronized (HorizonApplication.this.activitiesOnTop) {
                    HorizonApplication.this.activitiesOnTop.add(activity);
                }
            }
            
            public void onActivityResumed(final Activity activity) {
                synchronized (HorizonApplication.this.activitiesOnTop) {
                    HorizonApplication.this.activitiesOnTop.add(activity);
                }
            }
            
            public void onActivityPaused(final Activity activity) {
            }
            
            public void onActivityStopped(final Activity activity) {
                synchronized (HorizonApplication.this.activityStack) {
                    HorizonApplication.this.runningActivities.remove(activity);
                }
                synchronized (HorizonApplication.this.activitiesOnTop) {
                    HorizonApplication.this.activitiesOnTop.remove(activity);
                }
            }
            
            public void onActivitySaveInstanceState(final Activity activity, final Bundle outState) {
            }
            
            public void onActivityDestroyed(final Activity activity) {
                synchronized (HorizonApplication.this.activityStack) {
                    HorizonApplication.this.activityStack.remove(activity);
                }
            }
        });
    }
    
    public void uncaughtException(final Thread thread, final Throwable exception) {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        final File crashFile = LogFileHandler.getInstance().getNewLogFile("crash.txt");
        try {
            FileUtils.writeFileText(crashFile, stringWriter.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (this.systemExceptionHandler != null) {
            this.systemExceptionHandler.uncaughtException(thread, exception);
        }
    }
    
    public void onCreate() {
        this.systemExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        super.onCreate();
    }
    
    public boolean isActivityRunning(final Class clazz) {
        synchronized (this.activityStack) {
            for (final Activity activity : this.activityStack) {
                if (clazz.isInstance(activity) && !activity.isFinishing() && !this.runningActivities.contains(activity)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public List<Class<? extends Activity>> getRunningActivityClasses() {
        final List<Class<? extends Activity>> classes = new ArrayList<Class<? extends Activity>>();
        synchronized (this.activityStack) {
            for (final Activity activity : this.activityStack) {
                classes.add(activity.getClass());
            }
        }
        return classes;
    }
    
    public Activity getTopRunningActivity0() {
        for (final Activity activity : this.activityStack) {
            if (!activity.isFinishing() && this.runningActivities.contains(activity)) {
                return activity;
            }
        }
        return null;
    }
    
    public static Activity getTopRunningActivity() {
        final HorizonApplication instance = getInstance();
        return (instance != null) ? instance.getTopRunningActivity0() : null;
    }
    
    public static Activity getTopActivity() {
        final HorizonApplication instance = getInstance();
        return (instance != null) ? ((instance.activityStack.size() > 0) ? instance.activityStack.get(0) : null) : null;
    }
    
    public static List<Activity> getActivityStack() {
        final HorizonApplication instance = getInstance();
        return (instance != null) ? new ArrayList<Activity>(instance.activityStack) : new ArrayList<Activity>();
    }
    
    public static HashSet<Activity> getActivitiesOnTop() {
        final HorizonApplication instance = getInstance();
        return (instance != null) ? instance.activitiesOnTop : new HashSet<Activity>();
    }
    
    public static boolean moveToBackgroundIfNotOnTop(final Activity activity) {
        for (final Activity activityOnTop : getActivitiesOnTop()) {
            if (activityOnTop.getClass() != activity.getClass()) {
                return false;
            }
        }
        activity.moveTaskToBack(true);
        return true;
    }
    
    public static void terminate() {
        for (final Activity activity : getActivityStack()) {
            activity.finish();
        }
        Runtime.getRuntime().exit(0);
    }
    
    public static void restart() {
        final Activity context = getTopActivity();
        if (context != null) {
            final Intent intent = new Intent((Context)context, (Class)StartupWrapperActivity.class);
            intent.addFlags(335577088);
            final AlarmManager alarmMgr = (AlarmManager)context.getSystemService("alarm");
            alarmMgr.set(2, SystemClock.elapsedRealtime() + 200L, PendingIntent.getActivity((Context)context, 256, intent, 0));
        }
        Runtime.getRuntime().exit(0);
    }
    
    public static String getAppPackageName() {
        final HorizonApplication instance = getInstance();
        return (instance != null) ? instance.getApplicationInfo().packageName : "com.zheka.horizon";
    }
    
    public static File getExternalDataDir() {
        return new File(Environment.getExternalStorageDirectory(), new File("android/data", getAppPackageName()).getAbsolutePath());
    }
    
    public static ApplicationLock getLock(final String name) {
        synchronized (HorizonApplication.applicationLocks) {
            ApplicationLock lock = HorizonApplication.applicationLocks.get(name);
            if (lock == null) {
                lock = new ApplicationLock();
                HorizonApplication.applicationLocks.put(name, lock);
            }
            return lock;
        }
    }
    
    private void initializeFirebase0(final Context context) {
        this.mFirebaseApp = FirebaseApp.initializeApp(context);
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }
    
    private void sendFirebaseEvent0(final String event, final Bundle bundle) {
        if (this.mFirebaseAnalytics != null) {
            this.mFirebaseAnalytics.logEvent(event, bundle);
            System.out.println("sending firebase event: " + event + " " + bundle);
        }
    }
    
    public static void initializeFirebase(final Context context) {
        final HorizonApplication instance = getInstance();
        if (instance != null) {
            instance.initializeFirebase0(context);
        }
    }
    
    public static void sendFirebaseEvent(final String event, final Bundle bundle) {
        final HorizonApplication instance = getInstance();
        if (instance != null) {
            instance.sendFirebaseEvent0(event, bundle);
        }
    }
    
    static {
        applicationLocks = new HashMap<String, ApplicationLock>();
    }
    
    public static class ApplicationLock
    {
        private boolean isLocked;
        
        public ApplicationLock() {
            this.isLocked = false;
        }
        
        public boolean tryLock() {
            synchronized (this) {
                return !this.isLocked && (this.isLocked = true);
            }
        }
        
        public boolean lock() {
            while (this.isLocked) {
                try {
                    Thread.sleep(10L);
                    continue;
                }
                catch (InterruptedException e) {
                    return false;
                }
                break;
            }
            return true;
        }
        
        public void unlock() {
            synchronized (this) {
                this.isLocked = false;
            }
        }
    }
}
