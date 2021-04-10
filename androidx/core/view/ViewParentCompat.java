package androidx.core.view;

import android.view.*;
import android.os.*;
import android.util.*;
import androidx.annotation.*;
import android.view.accessibility.*;

public final class ViewParentCompat
{
    private static final String TAG = "ViewParentCompat";
    private static int[] sTempNestedScrollConsumed;
    
    private ViewParentCompat() {
    }
    
    private static int[] getTempNestedScrollConsumed() {
        if (ViewParentCompat.sTempNestedScrollConsumed == null) {
            ViewParentCompat.sTempNestedScrollConsumed = new int[2];
        }
        else {
            ViewParentCompat.sTempNestedScrollConsumed[0] = 0;
            ViewParentCompat.sTempNestedScrollConsumed[1] = 0;
        }
        return ViewParentCompat.sTempNestedScrollConsumed;
    }
    
    public static void notifySubtreeAccessibilityStateChanged(final ViewParent viewParent, final View view, final View view2, final int n) {
        if (Build$VERSION.SDK_INT >= 19) {
            viewParent.notifySubtreeAccessibilityStateChanged(view, view2, n);
        }
    }
    
    public static boolean onNestedFling(final ViewParent viewParent, final View view, final float n, final float n2, final boolean b) {
        if (Build$VERSION.SDK_INT >= 21) {
            try {
                return viewParent.onNestedFling(view, n, n2, b);
            }
            catch (AbstractMethodError abstractMethodError) {
                final StringBuilder sb = new StringBuilder();
                sb.append("ViewParent ");
                sb.append(viewParent);
                sb.append(" does not implement interface method onNestedFling");
                Log.e("ViewParentCompat", sb.toString(), (Throwable)abstractMethodError);
                return false;
            }
        }
        if (viewParent instanceof NestedScrollingParent) {
            return ((NestedScrollingParent)viewParent).onNestedFling(view, n, n2, b);
        }
        return false;
    }
    
    public static boolean onNestedPreFling(final ViewParent viewParent, final View view, final float n, final float n2) {
        if (Build$VERSION.SDK_INT >= 21) {
            try {
                return viewParent.onNestedPreFling(view, n, n2);
            }
            catch (AbstractMethodError abstractMethodError) {
                final StringBuilder sb = new StringBuilder();
                sb.append("ViewParent ");
                sb.append(viewParent);
                sb.append(" does not implement interface method onNestedPreFling");
                Log.e("ViewParentCompat", sb.toString(), (Throwable)abstractMethodError);
                return false;
            }
        }
        if (viewParent instanceof NestedScrollingParent) {
            return ((NestedScrollingParent)viewParent).onNestedPreFling(view, n, n2);
        }
        return false;
    }
    
    public static void onNestedPreScroll(final ViewParent viewParent, final View view, final int n, final int n2, final int[] array) {
        onNestedPreScroll(viewParent, view, n, n2, array, 0);
    }
    
    public static void onNestedPreScroll(final ViewParent viewParent, final View view, final int n, final int n2, final int[] array, final int n3) {
        if (viewParent instanceof NestedScrollingParent2) {
            ((NestedScrollingParent2)viewParent).onNestedPreScroll(view, n, n2, array, n3);
            return;
        }
        if (n3 == 0) {
            if (Build$VERSION.SDK_INT >= 21) {
                try {
                    viewParent.onNestedPreScroll(view, n, n2, array);
                    return;
                }
                catch (AbstractMethodError abstractMethodError) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("ViewParent ");
                    sb.append(viewParent);
                    sb.append(" does not implement interface method onNestedPreScroll");
                    Log.e("ViewParentCompat", sb.toString(), (Throwable)abstractMethodError);
                    return;
                }
            }
            if (viewParent instanceof NestedScrollingParent) {
                ((NestedScrollingParent)viewParent).onNestedPreScroll(view, n, n2, array);
            }
        }
    }
    
    public static void onNestedScroll(final ViewParent viewParent, final View view, final int n, final int n2, final int n3, final int n4) {
        onNestedScroll(viewParent, view, n, n2, n3, n4, 0, getTempNestedScrollConsumed());
    }
    
    public static void onNestedScroll(final ViewParent viewParent, final View view, final int n, final int n2, final int n3, final int n4, final int n5) {
        onNestedScroll(viewParent, view, n, n2, n3, n4, n5, getTempNestedScrollConsumed());
    }
    
    public static void onNestedScroll(final ViewParent viewParent, final View view, final int n, final int n2, final int n3, final int n4, final int n5, @NonNull final int[] array) {
        if (viewParent instanceof NestedScrollingParent3) {
            ((NestedScrollingParent3)viewParent).onNestedScroll(view, n, n2, n3, n4, n5, array);
            return;
        }
        array[0] += n3;
        array[1] += n4;
        if (viewParent instanceof NestedScrollingParent2) {
            ((NestedScrollingParent2)viewParent).onNestedScroll(view, n, n2, n3, n4, n5);
            return;
        }
        if (n5 == 0) {
            if (Build$VERSION.SDK_INT >= 21) {
                try {
                    viewParent.onNestedScroll(view, n, n2, n3, n4);
                    return;
                }
                catch (AbstractMethodError abstractMethodError) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("ViewParent ");
                    sb.append(viewParent);
                    sb.append(" does not implement interface method onNestedScroll");
                    Log.e("ViewParentCompat", sb.toString(), (Throwable)abstractMethodError);
                    return;
                }
            }
            if (viewParent instanceof NestedScrollingParent) {
                ((NestedScrollingParent)viewParent).onNestedScroll(view, n, n2, n3, n4);
            }
        }
    }
    
    public static void onNestedScrollAccepted(final ViewParent viewParent, final View view, final View view2, final int n) {
        onNestedScrollAccepted(viewParent, view, view2, n, 0);
    }
    
    public static void onNestedScrollAccepted(final ViewParent viewParent, final View view, final View view2, final int n, final int n2) {
        if (viewParent instanceof NestedScrollingParent2) {
            ((NestedScrollingParent2)viewParent).onNestedScrollAccepted(view, view2, n, n2);
            return;
        }
        if (n2 == 0) {
            if (Build$VERSION.SDK_INT >= 21) {
                try {
                    viewParent.onNestedScrollAccepted(view, view2, n);
                    return;
                }
                catch (AbstractMethodError abstractMethodError) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("ViewParent ");
                    sb.append(viewParent);
                    sb.append(" does not implement interface method onNestedScrollAccepted");
                    Log.e("ViewParentCompat", sb.toString(), (Throwable)abstractMethodError);
                    return;
                }
            }
            if (viewParent instanceof NestedScrollingParent) {
                ((NestedScrollingParent)viewParent).onNestedScrollAccepted(view, view2, n);
            }
        }
    }
    
    public static boolean onStartNestedScroll(final ViewParent viewParent, final View view, final View view2, final int n) {
        return onStartNestedScroll(viewParent, view, view2, n, 0);
    }
    
    public static boolean onStartNestedScroll(final ViewParent viewParent, final View view, final View view2, final int n, final int n2) {
        if (viewParent instanceof NestedScrollingParent2) {
            return ((NestedScrollingParent2)viewParent).onStartNestedScroll(view, view2, n, n2);
        }
        if (n2 == 0) {
            if (Build$VERSION.SDK_INT >= 21) {
                try {
                    return viewParent.onStartNestedScroll(view, view2, n);
                }
                catch (AbstractMethodError abstractMethodError) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("ViewParent ");
                    sb.append(viewParent);
                    sb.append(" does not implement interface method onStartNestedScroll");
                    Log.e("ViewParentCompat", sb.toString(), (Throwable)abstractMethodError);
                    return false;
                }
            }
            if (viewParent instanceof NestedScrollingParent) {
                return ((NestedScrollingParent)viewParent).onStartNestedScroll(view, view2, n);
            }
        }
        return false;
    }
    
    public static void onStopNestedScroll(final ViewParent viewParent, final View view) {
        onStopNestedScroll(viewParent, view, 0);
    }
    
    public static void onStopNestedScroll(final ViewParent viewParent, final View view, final int n) {
        if (viewParent instanceof NestedScrollingParent2) {
            ((NestedScrollingParent2)viewParent).onStopNestedScroll(view, n);
            return;
        }
        if (n == 0) {
            if (Build$VERSION.SDK_INT >= 21) {
                try {
                    viewParent.onStopNestedScroll(view);
                    return;
                }
                catch (AbstractMethodError abstractMethodError) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("ViewParent ");
                    sb.append(viewParent);
                    sb.append(" does not implement interface method onStopNestedScroll");
                    Log.e("ViewParentCompat", sb.toString(), (Throwable)abstractMethodError);
                    return;
                }
            }
            if (viewParent instanceof NestedScrollingParent) {
                ((NestedScrollingParent)viewParent).onStopNestedScroll(view);
            }
        }
    }
    
    @Deprecated
    public static boolean requestSendAccessibilityEvent(final ViewParent viewParent, final View view, final AccessibilityEvent accessibilityEvent) {
        return viewParent.requestSendAccessibilityEvent(view, accessibilityEvent);
    }
}
