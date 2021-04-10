package android.support.v4.app;

import android.graphics.drawable.*;
import android.content.res.*;
import android.os.*;
import android.util.*;
import android.app.*;
import java.lang.reflect.*;
import android.widget.*;
import android.view.*;

class ActionBarDrawerToggleHoneycomb
{
    private static final String TAG = "ActionBarDrawerToggleHoneycomb";
    private static final int[] THEME_ATTRS;
    
    static {
        THEME_ATTRS = new int[] { 16843531 };
    }
    
    public static Drawable getThemeUpIndicator(final Activity activity) {
        final TypedArray obtainStyledAttributes = activity.obtainStyledAttributes(ActionBarDrawerToggleHoneycomb.THEME_ATTRS);
        final Drawable drawable = obtainStyledAttributes.getDrawable(0);
        obtainStyledAttributes.recycle();
        return drawable;
    }
    
    public static Object setActionBarDescription(final Object o, final Activity activity, final int n) {
        Object o2 = o;
        if (o == null) {
            o2 = new SetIndicatorInfo(activity);
        }
        final SetIndicatorInfo setIndicatorInfo = (SetIndicatorInfo)o2;
        if (setIndicatorInfo.setHomeAsUpIndicator != null) {
            try {
                final ActionBar actionBar = activity.getActionBar();
                setIndicatorInfo.setHomeActionContentDescription.invoke(actionBar, n);
                if (Build$VERSION.SDK_INT <= 19) {
                    actionBar.setSubtitle(actionBar.getSubtitle());
                    return o2;
                }
            }
            catch (Exception ex) {
                Log.w("ActionBarDrawerToggleHoneycomb", "Couldn't set content description via JB-MR2 API", (Throwable)ex);
            }
        }
        return o2;
    }
    
    public static Object setActionBarUpIndicator(final Object o, final Activity activity, final Drawable imageDrawable, final int n) {
        Object o2 = o;
        if (o == null) {
            o2 = new SetIndicatorInfo(activity);
        }
        final SetIndicatorInfo setIndicatorInfo = (SetIndicatorInfo)o2;
        if (setIndicatorInfo.setHomeAsUpIndicator != null) {
            try {
                final ActionBar actionBar = activity.getActionBar();
                setIndicatorInfo.setHomeAsUpIndicator.invoke(actionBar, imageDrawable);
                setIndicatorInfo.setHomeActionContentDescription.invoke(actionBar, n);
                return o2;
            }
            catch (Exception ex) {
                Log.w("ActionBarDrawerToggleHoneycomb", "Couldn't set home-as-up indicator via JB-MR2 API", (Throwable)ex);
                return o2;
            }
        }
        if (setIndicatorInfo.upIndicatorView != null) {
            setIndicatorInfo.upIndicatorView.setImageDrawable(imageDrawable);
            return o2;
        }
        Log.w("ActionBarDrawerToggleHoneycomb", "Couldn't set home-as-up indicator");
        return o2;
    }
    
    private static class SetIndicatorInfo
    {
        public Method setHomeActionContentDescription;
        public Method setHomeAsUpIndicator;
        public ImageView upIndicatorView;
        
        SetIndicatorInfo(final Activity activity) {
            try {
                this.setHomeAsUpIndicator = ActionBar.class.getDeclaredMethod("setHomeAsUpIndicator", Drawable.class);
                this.setHomeActionContentDescription = ActionBar.class.getDeclaredMethod("setHomeActionContentDescription", Integer.TYPE);
            }
            catch (NoSuchMethodException ex) {
                final View viewById = activity.findViewById(16908332);
                if (viewById != null) {
                    final ViewGroup viewGroup = (ViewGroup)viewById.getParent();
                    if (viewGroup.getChildCount() == 2) {
                        View child = viewGroup.getChildAt(0);
                        final View child2 = viewGroup.getChildAt(1);
                        if (child.getId() == 16908332) {
                            child = child2;
                        }
                        if (child instanceof ImageView) {
                            this.upIndicatorView = (ImageView)child;
                        }
                    }
                }
            }
        }
    }
}
