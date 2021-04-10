package com.microsoft.xboxtcui;

import android.content.*;
import android.util.*;
import android.app.*;
import com.microsoft.xbox.xle.app.activity.Profile.*;
import com.microsoft.xbox.toolkit.ui.*;
import java.lang.reflect.*;
import java.util.*;

public class Interop
{
    private static final String TAG;
    private static final XboxTcuiWindowDialog.DetachedCallback detachedCallback;
    
    static {
        TAG = Interop.class.getSimpleName();
        detachedCallback = new XboxTcuiWindowDialog.DetachedCallback() {
            @Override
            public void onDetachedFromWindow() {
                tcui_completed_callback(0);
            }
        };
    }
    
    public static void ShowAddFriends(final Context context) {
        Log.i(Interop.TAG, "Deeplink - ShowAddFriends");
        int n;
        if (XboxAppDeepLinker.showAddFriends(context)) {
            n = 0;
        }
        else {
            n = 1;
        }
        tcui_completed_callback(n);
    }
    
    public static void ShowProfileCardUI(final Activity activity, final String s, final String s2, final String s3) {
        final String tag = Interop.TAG;
        final StringBuilder sb = new StringBuilder();
        sb.append("TCUI- ShowProfileCardUI: meXuid:");
        sb.append(s);
        Log.i(tag, sb.toString());
        final String tag2 = Interop.TAG;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("TCUI- ShowProfileCardUI: targeProfileXuid:");
        sb2.append(s2);
        Log.i(tag2, sb2.toString());
        final String tag3 = Interop.TAG;
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("TCUI- ShowProfileCardUI: privileges:");
        sb3.append(s3);
        Log.i(tag3, sb3.toString());
        Activity foregroundActivity;
        if ((foregroundActivity = getForegroundActivity()) == null) {
            foregroundActivity = activity;
        }
        final ActivityParameters activityParameters = new ActivityParameters();
        activityParameters.putMeXuid(s);
        activityParameters.putSelectedProfile(s2);
        activityParameters.putPrivileges(s3);
        activity.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                try {
                    final XboxTcuiWindowDialog xboxTcuiWindowDialog = new XboxTcuiWindowDialog(foregroundActivity, ProfileScreen.class, activityParameters);
                    xboxTcuiWindowDialog.setDetachedCallback(Interop.detachedCallback);
                    xboxTcuiWindowDialog.show();
                }
                catch (Exception ex) {
                    Log.i(Interop.TAG, Log.getStackTraceString((Throwable)ex));
                    tcui_completed_callback(1);
                }
            }
        });
    }
    
    public static void ShowTitleAchievements(final Context context, final String s) {
        Log.i(Interop.TAG, "Deeplink - ShowTitleAchievements");
        int n;
        if (XboxAppDeepLinker.showTitleAchievements(context, s)) {
            n = 0;
        }
        else {
            n = 1;
        }
        tcui_completed_callback(n);
    }
    
    public static void ShowTitleHub(final Context context, final String s) {
        Log.i(Interop.TAG, "Deeplink - ShowTitleHub");
        int n;
        if (XboxAppDeepLinker.showTitleHub(context, s)) {
            n = 0;
        }
        else {
            n = 1;
        }
        tcui_completed_callback(n);
    }
    
    public static void ShowUserProfile(final Context context, final String s) {
        Log.i(Interop.TAG, "Deeplink - ShowUserProfile");
        int n;
        if (XboxAppDeepLinker.showUserProfile(context, s)) {
            n = 0;
        }
        else {
            n = 1;
        }
        tcui_completed_callback(n);
    }
    
    public static void ShowUserSettings(final Context context) {
        Log.i(Interop.TAG, "Deeplink - ShowUserSettings");
        int n;
        if (XboxAppDeepLinker.showUserSettings(context)) {
            n = 0;
        }
        else {
            n = 1;
        }
        tcui_completed_callback(n);
    }
    
    private static Activity getForegroundActivity() {
        try {
            final Class<?> forName = Class.forName("android.app.ActivityThread");
            final Object invoke = forName.getMethod("currentActivityThread", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
            final Field declaredField = forName.getDeclaredField("mActivities");
            declaredField.setAccessible(true);
            Class<?> class1 = null;
            Block_3: {
                for (final Object next : ((Map)declaredField.get(invoke)).values()) {
                    class1 = next.getClass();
                    final Field declaredField2 = class1.getDeclaredField("paused");
                    declaredField2.setAccessible(true);
                    if (!declaredField2.getBoolean(next)) {
                        break Block_3;
                    }
                }
                return null;
            }
            final Field declaredField3 = class1.getDeclaredField("activity");
            declaredField3.setAccessible(true);
            final Object next;
            return (Activity)declaredField3.get(next);
        }
        catch (Exception ex) {
            Log.i(Interop.TAG, Log.getStackTraceString((Throwable)ex));
        }
        return null;
    }
    
    private static native void tcui_completed_callback(final int p0);
}
