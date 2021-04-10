package com.zhekasmirnov.innercore.utils;

import android.content.*;
import android.app.*;
import com.zhekasmirnov.mcpe161.*;
import com.zhekasmirnov.innercore.api.log.*;
import android.util.*;
import android.graphics.*;
import android.view.*;
import java.io.*;

public class UIUtils
{
    public static int screenHeight;
    public static int screenWidth;
    public static int xOffset;
    public static int yOffset;
    
    static {
        UIUtils.xOffset = 0;
        UIUtils.yOffset = 0;
    }
    
    public static ComponentName getActivityOnTop() {
        try {
            return ((ActivityManager)getContext().getSystemService("activity")).getRunningTasks(1).get(0).topActivity;
        }
        catch (NullPointerException | ClassCastException ex) {
            return null;
        }
    }
    
    public static Activity getContext() {
        return EnvironmentSetup.getCurrentActivity();
    }
    
    public static View getDecorView() {
        return getContext().getWindow().getDecorView();
    }
    
    public static void getOffsets(final int[] array) {
        final Activity currentActivity = InnerCore.getInstance().getCurrentActivity();
        if (currentActivity != null) {
            final View decorView = currentActivity.getWindow().getDecorView();
            if (decorView != null) {
                final int[] array2 = new int[2];
                decorView.getLocationOnScreen(array);
                array2[1] = 0;
            }
        }
    }
    
    public static void initialize(final Activity activity) {
        refreshScreenParams(activity);
    }
    
    public static boolean isInnerCoreActivityOpened() {
        final ComponentName activityOnTop = getActivityOnTop();
        return activityOnTop != null && "com.zhekasmirnov.innercore".equals(activityOnTop.getPackageName());
    }
    
    public static void log(final String s) {
        ICLog.d("INNERCORE-UI", s);
    }
    
    public static void processError(final Exception ex) {
        ICLog.e("INNERCORE-UI", "exception occured in UI engine:", ex);
    }
    
    private static void refreshScreenParams(final Activity activity) {
        new DisplayMetrics();
        final Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
        final Point point = new Point();
        defaultDisplay.getRealSize(point);
        final Point point2 = new Point();
        defaultDisplay.getSize(point2);
        final Activity currentActivity = InnerCore.getInstance().getCurrentActivity();
        if (currentActivity != null) {
            final View decorView = currentActivity.getWindow().getDecorView();
            if (decorView != null) {
                final int[] array = new int[2];
                decorView.getLocationOnScreen(array);
                final PrintStream out = System.out;
                final StringBuilder sb = new StringBuilder();
                sb.append("decor location on screen: ");
                sb.append(array[0]);
                sb.append(" ");
                sb.append(array[1]);
                out.println(sb.toString());
                UIUtils.screenWidth = Math.max(point.x, point.y) - array[0];
                UIUtils.screenHeight = Math.min(point.x, point.y);
                UIUtils.xOffset = 0;
                UIUtils.yOffset = 0;
                return;
            }
        }
        UIUtils.screenWidth = Math.max(point2.x, point2.y);
        UIUtils.screenHeight = Math.min(point2.x, point2.y);
        UIUtils.xOffset = 0;
        UIUtils.yOffset = 0;
    }
    
    public static void runOnUiThread(final Runnable runnable) {
        getContext().runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                }
                catch (Exception ex) {
                    UIUtils.processError(ex);
                }
            }
        });
    }
    
    public static void runOnUiThreadUnsafe(final Runnable runnable) {
        getContext().runOnUiThread(runnable);
    }
}
