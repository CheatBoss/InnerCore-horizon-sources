package com.zhekasmirnov.horizon;

import com.zhekasmirnov.horizon.activity.util.*;
import android.widget.*;
import java.lang.reflect.*;
import android.content.*;
import android.util.*;
import android.os.*;
import android.app.*;
import com.zhekasmirnov.horizon.compiler.packages.*;
import java.io.*;

public class LaunchUtils
{
    public static boolean requiresAlarmManagerLaunch() {
        return Build.VERSION.SDK_INT >= 29;
    }
    
    public static void popStayInForegroundTasks(final Activity activity) {
        activity.startActivity(new Intent((Context)activity, (Class)EmptyActivity.class));
        activity.startActivity(Intent.createChooser(new Intent("com.zhekasmirnov.horizon.activity.action.TRAMPOLINE"), (CharSequence)"Loading..."));
        try {
            Thread.sleep(500L);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void forceRestartIntoDifferentAbi(final Activity activity, final Intent targetIntent) {
        Toast.makeText((Context)activity, (CharSequence)"RESTARTING", 1).show();
        final Intent theIntent = (targetIntent != null) ? targetIntent : activity.getPackageManager().getLaunchIntentForPackage(activity.getPackageName());
        if (requiresAlarmManagerLaunch()) {
            popStayInForegroundTasks(activity);
        }
        try {
            rebootIntoInstrumentation(activity, theIntent);
        }
        catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e2) {
            throw new RuntimeException(e2);
        }
        catch (ClassNotFoundException e3) {
            throw new RuntimeException(e3);
        }
        catch (NoSuchMethodException e4) {
            throw new RuntimeException(e4);
        }
    }
    
    private static String getOverriddenNativeAbi() {
        final String arch = System.getProperty("os.arch");
        final String targetAbi = "armeabi-v7a";
        if (arch.startsWith("armv") || arch.startsWith("aarch")) {
            return targetAbi;
        }
        if (arch.equals("x86") || arch.equals("i686") || arch.equals("x86_64")) {
            return "x86";
        }
        return targetAbi;
    }
    
    private static void rebootIntoInstrumentation(final Activity activity, final Intent targetIntent) throws InvocationTargetException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException {
        final boolean launchViaAlarm = requiresAlarmManagerLaunch();
        if (launchViaAlarm) {
            setupAlarm(activity, targetIntent);
        }
        final String abiOverride = getOverriddenNativeAbi();
        final Bundle bundle = new Bundle();
        if (launchViaAlarm) {
            bundle.putByte("skipLaunch", (byte)1);
        }
        else {
            bundle.putParcelable("launchIntent", (Parcelable)targetIntent);
        }
        final ComponentName componentName = new ComponentName((Context)activity, (Class)RelaunchInstrumentation.class);
        Object iActivityManager;
        try {
            iActivityManager = ActivityManager.class.getMethod("getService", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
        }
        catch (NoSuchMethodException nme) {
            System.err.println(nme);
            iActivityManager = Class.forName("android.app.ActivityManagerNative").getMethod("getDefault", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
        }
        try {
            Log.i("LAUNCH-WRAPPER", "launching instrumentation...");
            final Object result = iActivityManager.getClass().getMethod("startInstrumentation", ComponentName.class, String.class, Integer.TYPE, Bundle.class, Class.forName("android.app.IInstrumentationWatcher"), Class.forName("android.app.IUiAutomationConnection"), Integer.TYPE, String.class).invoke(iActivityManager, componentName, null, 0, bundle, null, null, 0, abiOverride);
            System.out.println("STARTED INSTRUMENTATION " + result);
            while (true) {
                Thread.sleep(10000L);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private static void setupAlarm(final Activity activity, final Intent targetIntent) {
        final AlarmManager alarmMgr = (AlarmManager)activity.getSystemService("alarm");
        final long timeMillis = SystemClock.elapsedRealtime() + 500L;
        final Intent intent = new Intent(targetIntent);
        intent.addFlags(268468224);
        createLock((Context)activity);
        alarmMgr.set(2, timeMillis, PendingIntent.getActivity((Context)activity, 0, intent, 0));
    }
    
    public static File getLockFile(final Context context) {
        return new File(Environment.getDataDirFile(context), ".launch_lock");
    }
    
    public static boolean receiveLock(final Context context) {
        final File lock = getLockFile(context);
        if (lock.exists()) {
            lock.delete();
            return true;
        }
        return false;
    }
    
    public static void createLock(final Context context) {
        final File lock = getLockFile(context);
        lock.getParentFile().mkdirs();
        lock.delete();
        try {
            lock.createNewFile();
        }
        catch (IOException e) {
            throw new RuntimeException("failed to create launch lock " + lock, e);
        }
    }
    
    public static boolean checkLock(final Context context) {
        return getLockFile(context).exists();
    }
}
