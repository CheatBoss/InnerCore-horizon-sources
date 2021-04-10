package com.microsoft.xbox.toolkit.ui;

import android.widget.*;
import com.microsoft.xboxtcui.*;
import android.util.*;
import com.microsoft.xbox.toolkit.system.*;
import android.content.res.*;
import com.microsoft.xbox.toolkit.*;
import java.util.*;
import com.microsoft.xbox.toolkit.anim.*;
import android.content.*;
import android.os.*;
import android.view.*;

public abstract class ScreenLayout extends FrameLayout
{
    private static ArrayList<View> badList;
    private boolean allEventsEnabled;
    private boolean drawerEnabled;
    private boolean isActive;
    private boolean isEditable;
    private boolean isReady;
    private boolean isStarted;
    protected boolean isTombstoned;
    private Runnable onLayoutChangedListener;
    private int orientation;
    private int screenPercent;
    
    static {
        ScreenLayout.badList = new ArrayList<View>();
    }
    
    public ScreenLayout() {
        this(XboxTcuiSdk.getApplicationContext());
    }
    
    public ScreenLayout(final Context context) {
        this(context, 0);
    }
    
    public ScreenLayout(final Context context, final int n) {
        super(context);
        this.onLayoutChangedListener = null;
        this.isEditable = false;
        this.screenPercent = 100;
        this.drawerEnabled = true;
        this.allEventsEnabled = true;
        this.Initialize(n);
    }
    
    public ScreenLayout(final Context context, final AttributeSet set) {
        super(context, set);
        this.onLayoutChangedListener = null;
        this.isEditable = false;
        this.screenPercent = 100;
        this.drawerEnabled = true;
        this.allEventsEnabled = true;
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, XLERValueHelper.getStyleableRValueArray("ScreenLayout"));
        int int1;
        if (obtainStyledAttributes.hasValue(XLERValueHelper.getStyleableRValue("ScreenLayout_screenDIPs"))) {
            int1 = (int)(obtainStyledAttributes.getDimensionPixelSize(XLERValueHelper.getStyleableRValue("ScreenLayout_screenDIPs"), SystemUtil.getScreenWidth()) / (float)SystemUtil.getScreenWidth() * 100.0f);
        }
        else {
            int1 = obtainStyledAttributes.getInt(XLERValueHelper.getStyleableRValue("ScreenLayout_screenPercent"), -2);
        }
        this.screenPercent = int1;
        obtainStyledAttributes.recycle();
        this.Initialize(7);
    }
    
    public static void addViewThatCausesAndroidLeaks(final View view) {
        ScreenLayout.badList.add(view);
    }
    
    private void removeAllViewsAndWorkaroundAndroidLeaks() {
        XLEAssert.assertTrue(Thread.currentThread() == ThreadManager.UIThread);
        this.removeAllViews();
        final Iterator<View> iterator = ScreenLayout.badList.iterator();
        while (iterator.hasNext()) {
            removeViewAndWorkaroundAndroidLeaks(iterator.next());
        }
        ScreenLayout.badList.clear();
    }
    
    public static void removeViewAndWorkaroundAndroidLeaks(final View view) {
        if (view != null) {
            final ViewParent parent = view.getParent();
            final boolean b = parent instanceof ViewGroup;
            final boolean b2 = false;
            if (b) {
                ((ViewGroup)parent).removeAllViews();
                XLEAssert.assertTrue(view.getParent() == null);
            }
            if (view instanceof ViewGroup) {
                final ViewGroup viewGroup = (ViewGroup)view;
                viewGroup.removeAllViews();
                viewGroup.destroyDrawingCache();
                boolean b3 = b2;
                if (viewGroup.getChildCount() == 0) {
                    b3 = true;
                }
                XLEAssert.assertTrue(b3);
            }
        }
    }
    
    protected void Initialize(final int orientation) {
        this.isReady = false;
        this.isActive = false;
        this.isStarted = false;
        this.orientation = orientation;
    }
    
    public void adjustBottomMargin(final int n) {
    }
    
    public abstract void forceRefresh();
    
    public abstract void forceUpdateViewImmediately();
    
    public XLEAnimationPackage getAnimateIn(final boolean b) {
        return null;
    }
    
    public XLEAnimationPackage getAnimateOut(final boolean b) {
        return null;
    }
    
    public boolean getCanAutoLaunch() {
        return this.isEditable ^ true;
    }
    
    public String getContent() {
        return null;
    }
    
    public boolean getIsActive() {
        return this.isActive;
    }
    
    public boolean getIsEditable() {
        return this.isEditable;
    }
    
    public boolean getIsReady() {
        return this.isReady;
    }
    
    public boolean getIsStarted() {
        return this.isStarted;
    }
    
    public boolean getIsTombstoned() {
        return this.isTombstoned;
    }
    
    public String getLocalClassName() {
        return this.getClass().getName();
    }
    
    public abstract String getName();
    
    public String getRelativeId() {
        return null;
    }
    
    public int getScreenPercent() {
        return this.screenPercent;
    }
    
    public boolean getShouldShowAppbar() {
        return this.isEditable ^ true;
    }
    
    public Boolean getTrackPage() {
        return true;
    }
    
    public boolean isAllEventsEnabled() {
        return this.allEventsEnabled;
    }
    
    public boolean isAnimateOnPop() {
        return true;
    }
    
    public boolean isAnimateOnPush() {
        return true;
    }
    
    public boolean isDrawerEnabled() {
        return this.drawerEnabled;
    }
    
    public boolean isKeepPreviousScreen() {
        return false;
    }
    
    public void leaveScreen(final Runnable runnable) {
        runnable.run();
    }
    
    public void onActivityResult(final int n, final int n2, final Intent intent) {
    }
    
    public abstract void onAnimateInCompleted();
    
    public abstract void onAnimateInStarted();
    
    public void onApplicationPause() {
    }
    
    public void onApplicationResume() {
    }
    
    public abstract boolean onBackButtonPressed();
    
    public boolean onContextItemSelected(final MenuItem menuItem) {
        return false;
    }
    
    public void onCreate() {
    }
    
    public void onCreateContextMenu(final ContextMenu contextMenu, final View view, final ContextMenu$ContextMenuInfo contextMenu$ContextMenuInfo) {
    }
    
    public void onDestroy() {
        this.removeAllViewsAndWorkaroundAndroidLeaks();
    }
    
    public boolean onHoverEvent(final MotionEvent motionEvent) {
        return !this.allEventsEnabled || super.onHoverEvent(motionEvent);
    }
    
    public boolean onInterceptHoverEvent(final MotionEvent motionEvent) {
        return !this.allEventsEnabled || super.onInterceptHoverEvent(motionEvent);
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        return !this.allEventsEnabled || super.onInterceptTouchEvent(motionEvent);
    }
    
    public void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        final Runnable onLayoutChangedListener = this.onLayoutChangedListener;
        if (onLayoutChangedListener != null) {
            onLayoutChangedListener.run();
        }
    }
    
    public void onPause() {
        this.isReady = false;
    }
    
    public void onRehydrate() {
        this.isTombstoned = false;
        this.onRehydrateOverride();
    }
    
    public abstract void onRehydrateOverride();
    
    public void onRestart() {
    }
    
    public void onRestoreInstanceState(final Bundle bundle) {
    }
    
    public void onResume() {
        this.isReady = true;
    }
    
    public void onSaveInstanceState(final Bundle bundle) {
    }
    
    public void onSetActive() {
        this.isActive = true;
    }
    
    public void onSetInactive() {
        this.isActive = false;
    }
    
    public void onStart() {
        this.isStarted = true;
    }
    
    public void onStop() {
        this.isStarted = false;
    }
    
    public void onTombstone() {
        this.isTombstoned = true;
        this.removeAllViewsAndWorkaroundAndroidLeaks();
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        return !this.allEventsEnabled || super.onTouchEvent(motionEvent);
    }
    
    public void removeBottomMargin() {
    }
    
    public void resetBottomMargin() {
    }
    
    public void setAllEventsEnabled(final boolean allEventsEnabled) {
        this.allEventsEnabled = allEventsEnabled;
    }
    
    public void setContentView(final int n) {
        LayoutInflater.from(this.getContext()).inflate(n, (ViewGroup)this, true);
    }
    
    public void setDrawerEnabled(final boolean drawerEnabled) {
        this.drawerEnabled = drawerEnabled;
    }
    
    public void setIsEditable(final boolean isEditable) {
        this.isEditable = isEditable;
    }
    
    public void setOnLayoutChangedListener(final Runnable onLayoutChangedListener) {
        this.onLayoutChangedListener = onLayoutChangedListener;
    }
    
    public ScreenLayout setScreenPercent(final int screenPercent) {
        this.screenPercent = screenPercent;
        return this;
    }
    
    public void setScreenState(final int n) {
    }
    
    public View xleFindViewId(final int n) {
        return this.findViewById(n);
    }
}
