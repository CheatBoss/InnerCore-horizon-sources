package com.appboy.ui.support;

import com.appboy.support.*;
import android.app.*;
import android.graphics.*;
import android.view.*;

public class ViewUtils
{
    private static final int TABLET_SMALLEST_WIDTH_DP = 600;
    private static final String TAG;
    private static int sDisplayHeight;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(ViewUtils.class);
    }
    
    public static double convertDpToPixels(final Activity activity, final double n) {
        final double n2 = activity.getResources().getDisplayMetrics().density;
        Double.isNaN(n2);
        return n * n2;
    }
    
    public static int getDisplayHeight(final Activity activity) {
        final int sDisplayHeight = ViewUtils.sDisplayHeight;
        if (sDisplayHeight > 0) {
            return sDisplayHeight;
        }
        final Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
        final Point point = new Point();
        defaultDisplay.getSize(point);
        return ViewUtils.sDisplayHeight = point.y;
    }
    
    public static int getTopVisibleCoordinate(final View view) {
        final Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }
    
    public static boolean isRunningOnTablet(final Activity activity) {
        return activity.getResources().getConfiguration().smallestScreenWidthDp >= 600;
    }
    
    public static void removeViewFromParent(final View view) {
        if (view != null && view.getParent() instanceof ViewGroup) {
            final ViewGroup focusableInTouchModeAndRequestFocus = (ViewGroup)view.getParent();
            setFocusableInTouchModeAndRequestFocus((View)focusableInTouchModeAndRequestFocus);
            focusableInTouchModeAndRequestFocus.removeView(view);
        }
    }
    
    public static void setActivityRequestedOrientation(final Activity activity, final int requestedOrientation) {
        try {
            activity.setRequestedOrientation(requestedOrientation);
        }
        catch (Exception ex) {
            final String tag = ViewUtils.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to set requested orientation ");
            sb.append(requestedOrientation);
            sb.append(" for activity class: ");
            sb.append(activity.getLocalClassName());
            AppboyLogger.e(tag, sb.toString(), ex);
        }
    }
    
    public static void setFocusableInTouchModeAndRequestFocus(final View view) {
        try {
            view.setFocusableInTouchMode(true);
            view.requestFocus();
        }
        catch (Exception ex) {
            AppboyLogger.e(ViewUtils.TAG, "Caught exception while setting view to focusable in touch mode and requesting focus.", ex);
        }
    }
}
