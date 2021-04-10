package android.support.v4.view;

import android.content.res.*;
import android.view.accessibility.*;
import android.support.v4.view.accessibility.*;
import android.os.*;
import android.view.*;
import android.graphics.*;
import java.lang.annotation.*;
import java.util.*;
import android.util.*;
import android.graphics.drawable.*;
import java.lang.reflect.*;
import android.support.annotation.*;

public final class ViewCompat
{
    public static final int ACCESSIBILITY_LIVE_REGION_ASSERTIVE = 2;
    public static final int ACCESSIBILITY_LIVE_REGION_NONE = 0;
    public static final int ACCESSIBILITY_LIVE_REGION_POLITE = 1;
    private static final long FAKE_FRAME_TIME = 10L;
    static final ViewCompatImpl IMPL;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_AUTO = 0;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_NO = 2;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS = 4;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_YES = 1;
    public static final int LAYER_TYPE_HARDWARE = 2;
    public static final int LAYER_TYPE_NONE = 0;
    public static final int LAYER_TYPE_SOFTWARE = 1;
    public static final int LAYOUT_DIRECTION_INHERIT = 2;
    public static final int LAYOUT_DIRECTION_LOCALE = 3;
    public static final int LAYOUT_DIRECTION_LTR = 0;
    public static final int LAYOUT_DIRECTION_RTL = 1;
    public static final int MEASURED_HEIGHT_STATE_SHIFT = 16;
    public static final int MEASURED_SIZE_MASK = 16777215;
    public static final int MEASURED_STATE_MASK = -16777216;
    public static final int MEASURED_STATE_TOO_SMALL = 16777216;
    public static final int OVER_SCROLL_ALWAYS = 0;
    public static final int OVER_SCROLL_IF_CONTENT_SCROLLS = 1;
    public static final int OVER_SCROLL_NEVER = 2;
    public static final int SCROLL_AXIS_HORIZONTAL = 1;
    public static final int SCROLL_AXIS_NONE = 0;
    public static final int SCROLL_AXIS_VERTICAL = 2;
    public static final int SCROLL_INDICATOR_BOTTOM = 2;
    public static final int SCROLL_INDICATOR_END = 32;
    public static final int SCROLL_INDICATOR_LEFT = 4;
    public static final int SCROLL_INDICATOR_RIGHT = 8;
    public static final int SCROLL_INDICATOR_START = 16;
    public static final int SCROLL_INDICATOR_TOP = 1;
    private static final String TAG = "ViewCompat";
    
    static {
        final int sdk_INT = Build$VERSION.SDK_INT;
        ViewCompatImpl impl;
        if (sdk_INT >= 23) {
            impl = new MarshmallowViewCompatImpl();
        }
        else if (sdk_INT >= 21) {
            impl = new LollipopViewCompatImpl();
        }
        else if (sdk_INT >= 19) {
            impl = new KitKatViewCompatImpl();
        }
        else if (sdk_INT >= 17) {
            impl = new JbMr1ViewCompatImpl();
        }
        else if (sdk_INT >= 16) {
            impl = new JBViewCompatImpl();
        }
        else if (sdk_INT >= 15) {
            impl = new ICSMr1ViewCompatImpl();
        }
        else if (sdk_INT >= 14) {
            impl = new ICSViewCompatImpl();
        }
        else if (sdk_INT >= 11) {
            impl = new HCViewCompatImpl();
        }
        else if (sdk_INT >= 9) {
            impl = new GBViewCompatImpl();
        }
        else if (sdk_INT >= 7) {
            impl = new EclairMr1ViewCompatImpl();
        }
        else {
            impl = new BaseViewCompatImpl();
        }
        IMPL = impl;
    }
    
    private ViewCompat() {
    }
    
    public static ViewPropertyAnimatorCompat animate(final View view) {
        return ViewCompat.IMPL.animate(view);
    }
    
    public static boolean canScrollHorizontally(final View view, final int n) {
        return ViewCompat.IMPL.canScrollHorizontally(view, n);
    }
    
    public static boolean canScrollVertically(final View view, final int n) {
        return ViewCompat.IMPL.canScrollVertically(view, n);
    }
    
    public static int combineMeasuredStates(final int n, final int n2) {
        return ViewCompat.IMPL.combineMeasuredStates(n, n2);
    }
    
    public static WindowInsetsCompat dispatchApplyWindowInsets(final View view, final WindowInsetsCompat windowInsetsCompat) {
        return ViewCompat.IMPL.dispatchApplyWindowInsets(view, windowInsetsCompat);
    }
    
    public static void dispatchFinishTemporaryDetach(final View view) {
        ViewCompat.IMPL.dispatchFinishTemporaryDetach(view);
    }
    
    public static boolean dispatchNestedFling(final View view, final float n, final float n2, final boolean b) {
        return ViewCompat.IMPL.dispatchNestedFling(view, n, n2, b);
    }
    
    public static boolean dispatchNestedPreFling(final View view, final float n, final float n2) {
        return ViewCompat.IMPL.dispatchNestedPreFling(view, n, n2);
    }
    
    public static boolean dispatchNestedPreScroll(final View view, final int n, final int n2, final int[] array, final int[] array2) {
        return ViewCompat.IMPL.dispatchNestedPreScroll(view, n, n2, array, array2);
    }
    
    public static boolean dispatchNestedScroll(final View view, final int n, final int n2, final int n3, final int n4, final int[] array) {
        return ViewCompat.IMPL.dispatchNestedScroll(view, n, n2, n3, n4, array);
    }
    
    public static void dispatchStartTemporaryDetach(final View view) {
        ViewCompat.IMPL.dispatchStartTemporaryDetach(view);
    }
    
    public static int getAccessibilityLiveRegion(final View view) {
        return ViewCompat.IMPL.getAccessibilityLiveRegion(view);
    }
    
    public static AccessibilityNodeProviderCompat getAccessibilityNodeProvider(final View view) {
        return ViewCompat.IMPL.getAccessibilityNodeProvider(view);
    }
    
    public static float getAlpha(final View view) {
        return ViewCompat.IMPL.getAlpha(view);
    }
    
    public static ColorStateList getBackgroundTintList(final View view) {
        return ViewCompat.IMPL.getBackgroundTintList(view);
    }
    
    public static PorterDuff$Mode getBackgroundTintMode(final View view) {
        return ViewCompat.IMPL.getBackgroundTintMode(view);
    }
    
    public static Rect getClipBounds(final View view) {
        return ViewCompat.IMPL.getClipBounds(view);
    }
    
    public static float getElevation(final View view) {
        return ViewCompat.IMPL.getElevation(view);
    }
    
    public static boolean getFitsSystemWindows(final View view) {
        return ViewCompat.IMPL.getFitsSystemWindows(view);
    }
    
    public static int getImportantForAccessibility(final View view) {
        return ViewCompat.IMPL.getImportantForAccessibility(view);
    }
    
    public static int getLabelFor(final View view) {
        return ViewCompat.IMPL.getLabelFor(view);
    }
    
    public static int getLayerType(final View view) {
        return ViewCompat.IMPL.getLayerType(view);
    }
    
    public static int getLayoutDirection(final View view) {
        return ViewCompat.IMPL.getLayoutDirection(view);
    }
    
    public static int getMeasuredHeightAndState(final View view) {
        return ViewCompat.IMPL.getMeasuredHeightAndState(view);
    }
    
    public static int getMeasuredState(final View view) {
        return ViewCompat.IMPL.getMeasuredState(view);
    }
    
    public static int getMeasuredWidthAndState(final View view) {
        return ViewCompat.IMPL.getMeasuredWidthAndState(view);
    }
    
    public static int getMinimumHeight(final View view) {
        return ViewCompat.IMPL.getMinimumHeight(view);
    }
    
    public static int getMinimumWidth(final View view) {
        return ViewCompat.IMPL.getMinimumWidth(view);
    }
    
    public static int getOverScrollMode(final View view) {
        return ViewCompat.IMPL.getOverScrollMode(view);
    }
    
    public static int getPaddingEnd(final View view) {
        return ViewCompat.IMPL.getPaddingEnd(view);
    }
    
    public static int getPaddingStart(final View view) {
        return ViewCompat.IMPL.getPaddingStart(view);
    }
    
    public static ViewParent getParentForAccessibility(final View view) {
        return ViewCompat.IMPL.getParentForAccessibility(view);
    }
    
    public static float getPivotX(final View view) {
        return ViewCompat.IMPL.getPivotX(view);
    }
    
    public static float getPivotY(final View view) {
        return ViewCompat.IMPL.getPivotY(view);
    }
    
    public static float getRotation(final View view) {
        return ViewCompat.IMPL.getRotation(view);
    }
    
    public static float getRotationX(final View view) {
        return ViewCompat.IMPL.getRotationX(view);
    }
    
    public static float getRotationY(final View view) {
        return ViewCompat.IMPL.getRotationY(view);
    }
    
    public static float getScaleX(final View view) {
        return ViewCompat.IMPL.getScaleX(view);
    }
    
    public static float getScaleY(final View view) {
        return ViewCompat.IMPL.getScaleY(view);
    }
    
    public static int getScrollIndicators(@NonNull final View view) {
        return ViewCompat.IMPL.getScrollIndicators(view);
    }
    
    public static String getTransitionName(final View view) {
        return ViewCompat.IMPL.getTransitionName(view);
    }
    
    public static float getTranslationX(final View view) {
        return ViewCompat.IMPL.getTranslationX(view);
    }
    
    public static float getTranslationY(final View view) {
        return ViewCompat.IMPL.getTranslationY(view);
    }
    
    public static float getTranslationZ(final View view) {
        return ViewCompat.IMPL.getTranslationZ(view);
    }
    
    public static int getWindowSystemUiVisibility(final View view) {
        return ViewCompat.IMPL.getWindowSystemUiVisibility(view);
    }
    
    public static float getX(final View view) {
        return ViewCompat.IMPL.getX(view);
    }
    
    public static float getY(final View view) {
        return ViewCompat.IMPL.getY(view);
    }
    
    public static float getZ(final View view) {
        return ViewCompat.IMPL.getZ(view);
    }
    
    public static boolean hasAccessibilityDelegate(final View view) {
        return ViewCompat.IMPL.hasAccessibilityDelegate(view);
    }
    
    public static boolean hasNestedScrollingParent(final View view) {
        return ViewCompat.IMPL.hasNestedScrollingParent(view);
    }
    
    public static boolean hasOnClickListeners(final View view) {
        return ViewCompat.IMPL.hasOnClickListeners(view);
    }
    
    public static boolean hasOverlappingRendering(final View view) {
        return ViewCompat.IMPL.hasOverlappingRendering(view);
    }
    
    public static boolean hasTransientState(final View view) {
        return ViewCompat.IMPL.hasTransientState(view);
    }
    
    public static boolean isAttachedToWindow(final View view) {
        return ViewCompat.IMPL.isAttachedToWindow(view);
    }
    
    public static boolean isLaidOut(final View view) {
        return ViewCompat.IMPL.isLaidOut(view);
    }
    
    public static boolean isNestedScrollingEnabled(final View view) {
        return ViewCompat.IMPL.isNestedScrollingEnabled(view);
    }
    
    public static boolean isOpaque(final View view) {
        return ViewCompat.IMPL.isOpaque(view);
    }
    
    public static boolean isPaddingRelative(final View view) {
        return ViewCompat.IMPL.isPaddingRelative(view);
    }
    
    public static void jumpDrawablesToCurrentState(final View view) {
        ViewCompat.IMPL.jumpDrawablesToCurrentState(view);
    }
    
    public static void offsetLeftAndRight(final View view, final int n) {
        ViewCompat.IMPL.offsetLeftAndRight(view, n);
    }
    
    public static void offsetTopAndBottom(final View view, final int n) {
        ViewCompat.IMPL.offsetTopAndBottom(view, n);
    }
    
    public static WindowInsetsCompat onApplyWindowInsets(final View view, final WindowInsetsCompat windowInsetsCompat) {
        return ViewCompat.IMPL.onApplyWindowInsets(view, windowInsetsCompat);
    }
    
    public static void onInitializeAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
        ViewCompat.IMPL.onInitializeAccessibilityEvent(view, accessibilityEvent);
    }
    
    public static void onInitializeAccessibilityNodeInfo(final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        ViewCompat.IMPL.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
    }
    
    public static void onPopulateAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
        ViewCompat.IMPL.onPopulateAccessibilityEvent(view, accessibilityEvent);
    }
    
    public static boolean performAccessibilityAction(final View view, final int n, final Bundle bundle) {
        return ViewCompat.IMPL.performAccessibilityAction(view, n, bundle);
    }
    
    public static void postInvalidateOnAnimation(final View view) {
        ViewCompat.IMPL.postInvalidateOnAnimation(view);
    }
    
    public static void postInvalidateOnAnimation(final View view, final int n, final int n2, final int n3, final int n4) {
        ViewCompat.IMPL.postInvalidateOnAnimation(view, n, n2, n3, n4);
    }
    
    public static void postOnAnimation(final View view, final Runnable runnable) {
        ViewCompat.IMPL.postOnAnimation(view, runnable);
    }
    
    public static void postOnAnimationDelayed(final View view, final Runnable runnable, final long n) {
        ViewCompat.IMPL.postOnAnimationDelayed(view, runnable, n);
    }
    
    public static void requestApplyInsets(final View view) {
        ViewCompat.IMPL.requestApplyInsets(view);
    }
    
    public static int resolveSizeAndState(final int n, final int n2, final int n3) {
        return ViewCompat.IMPL.resolveSizeAndState(n, n2, n3);
    }
    
    public static void setAccessibilityDelegate(final View view, final AccessibilityDelegateCompat accessibilityDelegateCompat) {
        ViewCompat.IMPL.setAccessibilityDelegate(view, accessibilityDelegateCompat);
    }
    
    public static void setAccessibilityLiveRegion(final View view, final int n) {
        ViewCompat.IMPL.setAccessibilityLiveRegion(view, n);
    }
    
    public static void setActivated(final View view, final boolean b) {
        ViewCompat.IMPL.setActivated(view, b);
    }
    
    public static void setAlpha(final View view, @FloatRange(from = 0.0, to = 1.0) final float n) {
        ViewCompat.IMPL.setAlpha(view, n);
    }
    
    public static void setBackgroundTintList(final View view, final ColorStateList list) {
        ViewCompat.IMPL.setBackgroundTintList(view, list);
    }
    
    public static void setBackgroundTintMode(final View view, final PorterDuff$Mode porterDuff$Mode) {
        ViewCompat.IMPL.setBackgroundTintMode(view, porterDuff$Mode);
    }
    
    public static void setChildrenDrawingOrderEnabled(final ViewGroup viewGroup, final boolean b) {
        ViewCompat.IMPL.setChildrenDrawingOrderEnabled(viewGroup, b);
    }
    
    public static void setClipBounds(final View view, final Rect rect) {
        ViewCompat.IMPL.setClipBounds(view, rect);
    }
    
    public static void setElevation(final View view, final float n) {
        ViewCompat.IMPL.setElevation(view, n);
    }
    
    public static void setFitsSystemWindows(final View view, final boolean b) {
        ViewCompat.IMPL.setFitsSystemWindows(view, b);
    }
    
    public static void setHasTransientState(final View view, final boolean b) {
        ViewCompat.IMPL.setHasTransientState(view, b);
    }
    
    public static void setImportantForAccessibility(final View view, final int n) {
        ViewCompat.IMPL.setImportantForAccessibility(view, n);
    }
    
    public static void setLabelFor(final View view, @IdRes final int n) {
        ViewCompat.IMPL.setLabelFor(view, n);
    }
    
    public static void setLayerPaint(final View view, final Paint paint) {
        ViewCompat.IMPL.setLayerPaint(view, paint);
    }
    
    public static void setLayerType(final View view, final int n, final Paint paint) {
        ViewCompat.IMPL.setLayerType(view, n, paint);
    }
    
    public static void setLayoutDirection(final View view, final int n) {
        ViewCompat.IMPL.setLayoutDirection(view, n);
    }
    
    public static void setNestedScrollingEnabled(final View view, final boolean b) {
        ViewCompat.IMPL.setNestedScrollingEnabled(view, b);
    }
    
    public static void setOnApplyWindowInsetsListener(final View view, final OnApplyWindowInsetsListener onApplyWindowInsetsListener) {
        ViewCompat.IMPL.setOnApplyWindowInsetsListener(view, onApplyWindowInsetsListener);
    }
    
    public static void setOverScrollMode(final View view, final int n) {
        ViewCompat.IMPL.setOverScrollMode(view, n);
    }
    
    public static void setPaddingRelative(final View view, final int n, final int n2, final int n3, final int n4) {
        ViewCompat.IMPL.setPaddingRelative(view, n, n2, n3, n4);
    }
    
    public static void setPivotX(final View view, final float n) {
        ViewCompat.IMPL.setPivotX(view, n);
    }
    
    public static void setPivotY(final View view, final float n) {
        ViewCompat.IMPL.setPivotY(view, n);
    }
    
    public static void setRotation(final View view, final float n) {
        ViewCompat.IMPL.setRotation(view, n);
    }
    
    public static void setRotationX(final View view, final float n) {
        ViewCompat.IMPL.setRotationX(view, n);
    }
    
    public static void setRotationY(final View view, final float n) {
        ViewCompat.IMPL.setRotationY(view, n);
    }
    
    public static void setSaveFromParentEnabled(final View view, final boolean b) {
        ViewCompat.IMPL.setSaveFromParentEnabled(view, b);
    }
    
    public static void setScaleX(final View view, final float n) {
        ViewCompat.IMPL.setScaleX(view, n);
    }
    
    public static void setScaleY(final View view, final float n) {
        ViewCompat.IMPL.setScaleY(view, n);
    }
    
    public static void setScrollIndicators(@NonNull final View view, final int n) {
        ViewCompat.IMPL.setScrollIndicators(view, n);
    }
    
    public static void setScrollIndicators(@NonNull final View view, final int n, final int n2) {
        ViewCompat.IMPL.setScrollIndicators(view, n, n2);
    }
    
    public static void setTransitionName(final View view, final String s) {
        ViewCompat.IMPL.setTransitionName(view, s);
    }
    
    public static void setTranslationX(final View view, final float n) {
        ViewCompat.IMPL.setTranslationX(view, n);
    }
    
    public static void setTranslationY(final View view, final float n) {
        ViewCompat.IMPL.setTranslationY(view, n);
    }
    
    public static void setTranslationZ(final View view, final float n) {
        ViewCompat.IMPL.setTranslationZ(view, n);
    }
    
    public static void setX(final View view, final float n) {
        ViewCompat.IMPL.setX(view, n);
    }
    
    public static void setY(final View view, final float n) {
        ViewCompat.IMPL.setY(view, n);
    }
    
    public static boolean startNestedScroll(final View view, final int n) {
        return ViewCompat.IMPL.startNestedScroll(view, n);
    }
    
    public static void stopNestedScroll(final View view) {
        ViewCompat.IMPL.stopNestedScroll(view);
    }
    
    @Retention(RetentionPolicy.SOURCE)
    private @interface AccessibilityLiveRegion {
    }
    
    static class BaseViewCompatImpl implements ViewCompatImpl
    {
        private Method mDispatchFinishTemporaryDetach;
        private Method mDispatchStartTemporaryDetach;
        private boolean mTempDetachBound;
        WeakHashMap<View, ViewPropertyAnimatorCompat> mViewPropertyAnimatorCompatMap;
        
        BaseViewCompatImpl() {
            this.mViewPropertyAnimatorCompatMap = null;
        }
        
        private void bindTempDetach() {
            try {
                this.mDispatchStartTemporaryDetach = View.class.getDeclaredMethod("dispatchStartTemporaryDetach", (Class<?>[])new Class[0]);
                this.mDispatchFinishTemporaryDetach = View.class.getDeclaredMethod("dispatchFinishTemporaryDetach", (Class<?>[])new Class[0]);
            }
            catch (NoSuchMethodException ex) {
                Log.e("ViewCompat", "Couldn't find method", (Throwable)ex);
            }
            this.mTempDetachBound = true;
        }
        
        private boolean canScrollingViewScrollHorizontally(final ScrollingView scrollingView, final int n) {
            final int computeHorizontalScrollOffset = scrollingView.computeHorizontalScrollOffset();
            final int n2 = scrollingView.computeHorizontalScrollRange() - scrollingView.computeHorizontalScrollExtent();
            if (n2 == 0) {
                return false;
            }
            if (n < 0) {
                if (computeHorizontalScrollOffset <= 0) {
                    return false;
                }
            }
            else if (computeHorizontalScrollOffset >= n2 - 1) {
                return false;
            }
            return true;
        }
        
        private boolean canScrollingViewScrollVertically(final ScrollingView scrollingView, final int n) {
            final int computeVerticalScrollOffset = scrollingView.computeVerticalScrollOffset();
            final int n2 = scrollingView.computeVerticalScrollRange() - scrollingView.computeVerticalScrollExtent();
            if (n2 == 0) {
                return false;
            }
            if (n < 0) {
                if (computeVerticalScrollOffset <= 0) {
                    return false;
                }
            }
            else if (computeVerticalScrollOffset >= n2 - 1) {
                return false;
            }
            return true;
        }
        
        @Override
        public ViewPropertyAnimatorCompat animate(final View view) {
            return new ViewPropertyAnimatorCompat(view);
        }
        
        @Override
        public boolean canScrollHorizontally(final View view, final int n) {
            return view instanceof ScrollingView && this.canScrollingViewScrollHorizontally((ScrollingView)view, n);
        }
        
        @Override
        public boolean canScrollVertically(final View view, final int n) {
            return view instanceof ScrollingView && this.canScrollingViewScrollVertically((ScrollingView)view, n);
        }
        
        @Override
        public int combineMeasuredStates(final int n, final int n2) {
            return n | n2;
        }
        
        @Override
        public WindowInsetsCompat dispatchApplyWindowInsets(final View view, final WindowInsetsCompat windowInsetsCompat) {
            return windowInsetsCompat;
        }
        
        @Override
        public void dispatchFinishTemporaryDetach(final View view) {
            if (!this.mTempDetachBound) {
                this.bindTempDetach();
            }
            if (this.mDispatchFinishTemporaryDetach != null) {
                try {
                    this.mDispatchFinishTemporaryDetach.invoke(view, new Object[0]);
                    return;
                }
                catch (Exception ex) {
                    Log.d("ViewCompat", "Error calling dispatchFinishTemporaryDetach", (Throwable)ex);
                    return;
                }
            }
            view.onFinishTemporaryDetach();
        }
        
        @Override
        public boolean dispatchNestedFling(final View view, final float n, final float n2, final boolean b) {
            return view instanceof NestedScrollingChild && ((NestedScrollingChild)view).dispatchNestedFling(n, n2, b);
        }
        
        @Override
        public boolean dispatchNestedPreFling(final View view, final float n, final float n2) {
            return view instanceof NestedScrollingChild && ((NestedScrollingChild)view).dispatchNestedPreFling(n, n2);
        }
        
        @Override
        public boolean dispatchNestedPreScroll(final View view, final int n, final int n2, final int[] array, final int[] array2) {
            return view instanceof NestedScrollingChild && ((NestedScrollingChild)view).dispatchNestedPreScroll(n, n2, array, array2);
        }
        
        @Override
        public boolean dispatchNestedScroll(final View view, final int n, final int n2, final int n3, final int n4, final int[] array) {
            return view instanceof NestedScrollingChild && ((NestedScrollingChild)view).dispatchNestedScroll(n, n2, n3, n4, array);
        }
        
        @Override
        public void dispatchStartTemporaryDetach(final View view) {
            if (!this.mTempDetachBound) {
                this.bindTempDetach();
            }
            if (this.mDispatchStartTemporaryDetach != null) {
                try {
                    this.mDispatchStartTemporaryDetach.invoke(view, new Object[0]);
                    return;
                }
                catch (Exception ex) {
                    Log.d("ViewCompat", "Error calling dispatchStartTemporaryDetach", (Throwable)ex);
                    return;
                }
            }
            view.onStartTemporaryDetach();
        }
        
        @Override
        public int getAccessibilityLiveRegion(final View view) {
            return 0;
        }
        
        @Override
        public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(final View view) {
            return null;
        }
        
        @Override
        public float getAlpha(final View view) {
            return 1.0f;
        }
        
        @Override
        public ColorStateList getBackgroundTintList(final View view) {
            return ViewCompatBase.getBackgroundTintList(view);
        }
        
        @Override
        public PorterDuff$Mode getBackgroundTintMode(final View view) {
            return ViewCompatBase.getBackgroundTintMode(view);
        }
        
        @Override
        public Rect getClipBounds(final View view) {
            return null;
        }
        
        @Override
        public float getElevation(final View view) {
            return 0.0f;
        }
        
        @Override
        public boolean getFitsSystemWindows(final View view) {
            return false;
        }
        
        long getFrameTime() {
            return 10L;
        }
        
        @Override
        public int getImportantForAccessibility(final View view) {
            return 0;
        }
        
        @Override
        public int getLabelFor(final View view) {
            return 0;
        }
        
        @Override
        public int getLayerType(final View view) {
            return 0;
        }
        
        @Override
        public int getLayoutDirection(final View view) {
            return 0;
        }
        
        @Override
        public int getMeasuredHeightAndState(final View view) {
            return view.getMeasuredHeight();
        }
        
        @Override
        public int getMeasuredState(final View view) {
            return 0;
        }
        
        @Override
        public int getMeasuredWidthAndState(final View view) {
            return view.getMeasuredWidth();
        }
        
        @Override
        public int getMinimumHeight(final View view) {
            return ViewCompatBase.getMinimumHeight(view);
        }
        
        @Override
        public int getMinimumWidth(final View view) {
            return ViewCompatBase.getMinimumWidth(view);
        }
        
        @Override
        public int getOverScrollMode(final View view) {
            return 2;
        }
        
        @Override
        public int getPaddingEnd(final View view) {
            return view.getPaddingRight();
        }
        
        @Override
        public int getPaddingStart(final View view) {
            return view.getPaddingLeft();
        }
        
        @Override
        public ViewParent getParentForAccessibility(final View view) {
            return view.getParent();
        }
        
        @Override
        public float getPivotX(final View view) {
            return 0.0f;
        }
        
        @Override
        public float getPivotY(final View view) {
            return 0.0f;
        }
        
        @Override
        public float getRotation(final View view) {
            return 0.0f;
        }
        
        @Override
        public float getRotationX(final View view) {
            return 0.0f;
        }
        
        @Override
        public float getRotationY(final View view) {
            return 0.0f;
        }
        
        @Override
        public float getScaleX(final View view) {
            return 0.0f;
        }
        
        @Override
        public float getScaleY(final View view) {
            return 0.0f;
        }
        
        @Override
        public int getScrollIndicators(final View view) {
            return 0;
        }
        
        @Override
        public String getTransitionName(final View view) {
            return null;
        }
        
        @Override
        public float getTranslationX(final View view) {
            return 0.0f;
        }
        
        @Override
        public float getTranslationY(final View view) {
            return 0.0f;
        }
        
        @Override
        public float getTranslationZ(final View view) {
            return 0.0f;
        }
        
        @Override
        public int getWindowSystemUiVisibility(final View view) {
            return 0;
        }
        
        @Override
        public float getX(final View view) {
            return 0.0f;
        }
        
        @Override
        public float getY(final View view) {
            return 0.0f;
        }
        
        @Override
        public float getZ(final View view) {
            return this.getTranslationZ(view) + this.getElevation(view);
        }
        
        @Override
        public boolean hasAccessibilityDelegate(final View view) {
            return false;
        }
        
        @Override
        public boolean hasNestedScrollingParent(final View view) {
            return view instanceof NestedScrollingChild && ((NestedScrollingChild)view).hasNestedScrollingParent();
        }
        
        @Override
        public boolean hasOnClickListeners(final View view) {
            return false;
        }
        
        @Override
        public boolean hasOverlappingRendering(final View view) {
            return true;
        }
        
        @Override
        public boolean hasTransientState(final View view) {
            return false;
        }
        
        @Override
        public boolean isAttachedToWindow(final View view) {
            return ViewCompatBase.isAttachedToWindow(view);
        }
        
        @Override
        public boolean isImportantForAccessibility(final View view) {
            return true;
        }
        
        @Override
        public boolean isLaidOut(final View view) {
            return ViewCompatBase.isLaidOut(view);
        }
        
        @Override
        public boolean isNestedScrollingEnabled(final View view) {
            return view instanceof NestedScrollingChild && ((NestedScrollingChild)view).isNestedScrollingEnabled();
        }
        
        @Override
        public boolean isOpaque(final View view) {
            final Drawable background = view.getBackground();
            return background != null && background.getOpacity() == -1;
        }
        
        @Override
        public boolean isPaddingRelative(final View view) {
            return false;
        }
        
        @Override
        public void jumpDrawablesToCurrentState(final View view) {
        }
        
        @Override
        public void offsetLeftAndRight(final View view, final int n) {
            ViewCompatBase.offsetLeftAndRight(view, n);
        }
        
        @Override
        public void offsetTopAndBottom(final View view, final int n) {
            ViewCompatBase.offsetTopAndBottom(view, n);
        }
        
        @Override
        public WindowInsetsCompat onApplyWindowInsets(final View view, final WindowInsetsCompat windowInsetsCompat) {
            return windowInsetsCompat;
        }
        
        @Override
        public void onInitializeAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
        }
        
        @Override
        public void onInitializeAccessibilityNodeInfo(final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        }
        
        @Override
        public void onPopulateAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
        }
        
        @Override
        public boolean performAccessibilityAction(final View view, final int n, final Bundle bundle) {
            return false;
        }
        
        @Override
        public void postInvalidateOnAnimation(final View view) {
            view.invalidate();
        }
        
        @Override
        public void postInvalidateOnAnimation(final View view, final int n, final int n2, final int n3, final int n4) {
            view.invalidate(n, n2, n3, n4);
        }
        
        @Override
        public void postOnAnimation(final View view, final Runnable runnable) {
            view.postDelayed(runnable, this.getFrameTime());
        }
        
        @Override
        public void postOnAnimationDelayed(final View view, final Runnable runnable, final long n) {
            view.postDelayed(runnable, this.getFrameTime() + n);
        }
        
        @Override
        public void requestApplyInsets(final View view) {
        }
        
        @Override
        public int resolveSizeAndState(final int n, final int n2, final int n3) {
            return View.resolveSize(n, n2);
        }
        
        @Override
        public void setAccessibilityDelegate(final View view, final AccessibilityDelegateCompat accessibilityDelegateCompat) {
        }
        
        @Override
        public void setAccessibilityLiveRegion(final View view, final int n) {
        }
        
        @Override
        public void setActivated(final View view, final boolean b) {
        }
        
        @Override
        public void setAlpha(final View view, final float n) {
        }
        
        @Override
        public void setBackgroundTintList(final View view, final ColorStateList list) {
            ViewCompatBase.setBackgroundTintList(view, list);
        }
        
        @Override
        public void setBackgroundTintMode(final View view, final PorterDuff$Mode porterDuff$Mode) {
            ViewCompatBase.setBackgroundTintMode(view, porterDuff$Mode);
        }
        
        @Override
        public void setChildrenDrawingOrderEnabled(final ViewGroup viewGroup, final boolean b) {
        }
        
        @Override
        public void setClipBounds(final View view, final Rect rect) {
        }
        
        @Override
        public void setElevation(final View view, final float n) {
        }
        
        @Override
        public void setFitsSystemWindows(final View view, final boolean b) {
        }
        
        @Override
        public void setHasTransientState(final View view, final boolean b) {
        }
        
        @Override
        public void setImportantForAccessibility(final View view, final int n) {
        }
        
        @Override
        public void setLabelFor(final View view, final int n) {
        }
        
        @Override
        public void setLayerPaint(final View view, final Paint paint) {
        }
        
        @Override
        public void setLayerType(final View view, final int n, final Paint paint) {
        }
        
        @Override
        public void setLayoutDirection(final View view, final int n) {
        }
        
        @Override
        public void setNestedScrollingEnabled(final View view, final boolean nestedScrollingEnabled) {
            if (view instanceof NestedScrollingChild) {
                ((NestedScrollingChild)view).setNestedScrollingEnabled(nestedScrollingEnabled);
            }
        }
        
        @Override
        public void setOnApplyWindowInsetsListener(final View view, final OnApplyWindowInsetsListener onApplyWindowInsetsListener) {
        }
        
        @Override
        public void setOverScrollMode(final View view, final int n) {
        }
        
        @Override
        public void setPaddingRelative(final View view, final int n, final int n2, final int n3, final int n4) {
            view.setPadding(n, n2, n3, n4);
        }
        
        @Override
        public void setPivotX(final View view, final float n) {
        }
        
        @Override
        public void setPivotY(final View view, final float n) {
        }
        
        @Override
        public void setRotation(final View view, final float n) {
        }
        
        @Override
        public void setRotationX(final View view, final float n) {
        }
        
        @Override
        public void setRotationY(final View view, final float n) {
        }
        
        @Override
        public void setSaveFromParentEnabled(final View view, final boolean b) {
        }
        
        @Override
        public void setScaleX(final View view, final float n) {
        }
        
        @Override
        public void setScaleY(final View view, final float n) {
        }
        
        @Override
        public void setScrollIndicators(final View view, final int n) {
        }
        
        @Override
        public void setScrollIndicators(final View view, final int n, final int n2) {
        }
        
        @Override
        public void setTransitionName(final View view, final String s) {
        }
        
        @Override
        public void setTranslationX(final View view, final float n) {
        }
        
        @Override
        public void setTranslationY(final View view, final float n) {
        }
        
        @Override
        public void setTranslationZ(final View view, final float n) {
        }
        
        @Override
        public void setX(final View view, final float n) {
        }
        
        @Override
        public void setY(final View view, final float n) {
        }
        
        @Override
        public boolean startNestedScroll(final View view, final int n) {
            return view instanceof NestedScrollingChild && ((NestedScrollingChild)view).startNestedScroll(n);
        }
        
        @Override
        public void stopNestedScroll(final View view) {
            if (view instanceof NestedScrollingChild) {
                ((NestedScrollingChild)view).stopNestedScroll();
            }
        }
    }
    
    static class EclairMr1ViewCompatImpl extends BaseViewCompatImpl
    {
        @Override
        public boolean isOpaque(final View view) {
            return ViewCompatEclairMr1.isOpaque(view);
        }
        
        @Override
        public void setChildrenDrawingOrderEnabled(final ViewGroup viewGroup, final boolean b) {
            ViewCompatEclairMr1.setChildrenDrawingOrderEnabled(viewGroup, b);
        }
    }
    
    static class GBViewCompatImpl extends EclairMr1ViewCompatImpl
    {
        @Override
        public int getOverScrollMode(final View view) {
            return ViewCompatGingerbread.getOverScrollMode(view);
        }
        
        @Override
        public void setOverScrollMode(final View view, final int n) {
            ViewCompatGingerbread.setOverScrollMode(view, n);
        }
    }
    
    static class HCViewCompatImpl extends GBViewCompatImpl
    {
        @Override
        public int combineMeasuredStates(final int n, final int n2) {
            return ViewCompatHC.combineMeasuredStates(n, n2);
        }
        
        @Override
        public float getAlpha(final View view) {
            return ViewCompatHC.getAlpha(view);
        }
        
        @Override
        long getFrameTime() {
            return ViewCompatHC.getFrameTime();
        }
        
        @Override
        public int getLayerType(final View view) {
            return ViewCompatHC.getLayerType(view);
        }
        
        @Override
        public int getMeasuredHeightAndState(final View view) {
            return ViewCompatHC.getMeasuredHeightAndState(view);
        }
        
        @Override
        public int getMeasuredState(final View view) {
            return ViewCompatHC.getMeasuredState(view);
        }
        
        @Override
        public int getMeasuredWidthAndState(final View view) {
            return ViewCompatHC.getMeasuredWidthAndState(view);
        }
        
        @Override
        public float getPivotX(final View view) {
            return ViewCompatHC.getPivotX(view);
        }
        
        @Override
        public float getPivotY(final View view) {
            return ViewCompatHC.getPivotY(view);
        }
        
        @Override
        public float getRotation(final View view) {
            return ViewCompatHC.getRotation(view);
        }
        
        @Override
        public float getRotationX(final View view) {
            return ViewCompatHC.getRotationX(view);
        }
        
        @Override
        public float getRotationY(final View view) {
            return ViewCompatHC.getRotationY(view);
        }
        
        @Override
        public float getScaleX(final View view) {
            return ViewCompatHC.getScaleX(view);
        }
        
        @Override
        public float getScaleY(final View view) {
            return ViewCompatHC.getScaleY(view);
        }
        
        @Override
        public float getTranslationX(final View view) {
            return ViewCompatHC.getTranslationX(view);
        }
        
        @Override
        public float getTranslationY(final View view) {
            return ViewCompatHC.getTranslationY(view);
        }
        
        @Override
        public float getX(final View view) {
            return ViewCompatHC.getX(view);
        }
        
        @Override
        public float getY(final View view) {
            return ViewCompatHC.getY(view);
        }
        
        @Override
        public void jumpDrawablesToCurrentState(final View view) {
            ViewCompatHC.jumpDrawablesToCurrentState(view);
        }
        
        @Override
        public void offsetLeftAndRight(final View view, final int n) {
            ViewCompatHC.offsetLeftAndRight(view, n);
        }
        
        @Override
        public void offsetTopAndBottom(final View view, final int n) {
            ViewCompatHC.offsetTopAndBottom(view, n);
        }
        
        @Override
        public int resolveSizeAndState(final int n, final int n2, final int n3) {
            return ViewCompatHC.resolveSizeAndState(n, n2, n3);
        }
        
        @Override
        public void setActivated(final View view, final boolean b) {
            ViewCompatHC.setActivated(view, b);
        }
        
        @Override
        public void setAlpha(final View view, final float n) {
            ViewCompatHC.setAlpha(view, n);
        }
        
        @Override
        public void setLayerPaint(final View view, final Paint paint) {
            this.setLayerType(view, this.getLayerType(view), paint);
            view.invalidate();
        }
        
        @Override
        public void setLayerType(final View view, final int n, final Paint paint) {
            ViewCompatHC.setLayerType(view, n, paint);
        }
        
        @Override
        public void setPivotX(final View view, final float n) {
            ViewCompatHC.setPivotX(view, n);
        }
        
        @Override
        public void setPivotY(final View view, final float n) {
            ViewCompatHC.setPivotY(view, n);
        }
        
        @Override
        public void setRotation(final View view, final float n) {
            ViewCompatHC.setRotation(view, n);
        }
        
        @Override
        public void setRotationX(final View view, final float n) {
            ViewCompatHC.setRotationX(view, n);
        }
        
        @Override
        public void setRotationY(final View view, final float n) {
            ViewCompatHC.setRotationY(view, n);
        }
        
        @Override
        public void setSaveFromParentEnabled(final View view, final boolean b) {
            ViewCompatHC.setSaveFromParentEnabled(view, b);
        }
        
        @Override
        public void setScaleX(final View view, final float n) {
            ViewCompatHC.setScaleX(view, n);
        }
        
        @Override
        public void setScaleY(final View view, final float n) {
            ViewCompatHC.setScaleY(view, n);
        }
        
        @Override
        public void setTranslationX(final View view, final float n) {
            ViewCompatHC.setTranslationX(view, n);
        }
        
        @Override
        public void setTranslationY(final View view, final float n) {
            ViewCompatHC.setTranslationY(view, n);
        }
        
        @Override
        public void setX(final View view, final float n) {
            ViewCompatHC.setX(view, n);
        }
        
        @Override
        public void setY(final View view, final float n) {
            ViewCompatHC.setY(view, n);
        }
    }
    
    static class ICSMr1ViewCompatImpl extends ICSViewCompatImpl
    {
        @Override
        public boolean hasOnClickListeners(final View view) {
            return ViewCompatICSMr1.hasOnClickListeners(view);
        }
    }
    
    static class ICSViewCompatImpl extends HCViewCompatImpl
    {
        static boolean accessibilityDelegateCheckFailed;
        static Field mAccessibilityDelegateField;
        
        static {
            ICSViewCompatImpl.accessibilityDelegateCheckFailed = false;
        }
        
        @Override
        public ViewPropertyAnimatorCompat animate(final View view) {
            if (this.mViewPropertyAnimatorCompatMap == null) {
                this.mViewPropertyAnimatorCompatMap = new WeakHashMap<View, ViewPropertyAnimatorCompat>();
            }
            ViewPropertyAnimatorCompat viewPropertyAnimatorCompat;
            if ((viewPropertyAnimatorCompat = this.mViewPropertyAnimatorCompatMap.get(view)) == null) {
                viewPropertyAnimatorCompat = new ViewPropertyAnimatorCompat(view);
                this.mViewPropertyAnimatorCompatMap.put(view, viewPropertyAnimatorCompat);
            }
            return viewPropertyAnimatorCompat;
        }
        
        @Override
        public boolean canScrollHorizontally(final View view, final int n) {
            return ViewCompatICS.canScrollHorizontally(view, n);
        }
        
        @Override
        public boolean canScrollVertically(final View view, final int n) {
            return ViewCompatICS.canScrollVertically(view, n);
        }
        
        @Override
        public boolean hasAccessibilityDelegate(final View view) {
            final boolean accessibilityDelegateCheckFailed = ICSViewCompatImpl.accessibilityDelegateCheckFailed;
            boolean b = false;
            if (accessibilityDelegateCheckFailed) {
                return false;
            }
            if (ICSViewCompatImpl.mAccessibilityDelegateField == null) {
                try {
                    (ICSViewCompatImpl.mAccessibilityDelegateField = View.class.getDeclaredField("mAccessibilityDelegate")).setAccessible(true);
                }
                catch (Throwable t) {
                    ICSViewCompatImpl.accessibilityDelegateCheckFailed = true;
                    return false;
                }
            }
            try {
                if (ICSViewCompatImpl.mAccessibilityDelegateField.get(view) != null) {
                    b = true;
                }
                return b;
            }
            catch (Throwable t2) {
                ICSViewCompatImpl.accessibilityDelegateCheckFailed = true;
                return false;
            }
        }
        
        @Override
        public void onInitializeAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
            ViewCompatICS.onInitializeAccessibilityEvent(view, accessibilityEvent);
        }
        
        @Override
        public void onInitializeAccessibilityNodeInfo(final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            ViewCompatICS.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat.getInfo());
        }
        
        @Override
        public void onPopulateAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
            ViewCompatICS.onPopulateAccessibilityEvent(view, accessibilityEvent);
        }
        
        @Override
        public void setAccessibilityDelegate(final View view, @Nullable final AccessibilityDelegateCompat accessibilityDelegateCompat) {
            Object bridge;
            if (accessibilityDelegateCompat == null) {
                bridge = null;
            }
            else {
                bridge = accessibilityDelegateCompat.getBridge();
            }
            ViewCompatICS.setAccessibilityDelegate(view, bridge);
        }
        
        @Override
        public void setFitsSystemWindows(final View view, final boolean b) {
            ViewCompatICS.setFitsSystemWindows(view, b);
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    private @interface ImportantForAccessibility {
    }
    
    static class JBViewCompatImpl extends ICSMr1ViewCompatImpl
    {
        @Override
        public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(final View view) {
            final Object accessibilityNodeProvider = ViewCompatJB.getAccessibilityNodeProvider(view);
            if (accessibilityNodeProvider != null) {
                return new AccessibilityNodeProviderCompat(accessibilityNodeProvider);
            }
            return null;
        }
        
        @Override
        public boolean getFitsSystemWindows(final View view) {
            return ViewCompatJB.getFitsSystemWindows(view);
        }
        
        @Override
        public int getImportantForAccessibility(final View view) {
            return ViewCompatJB.getImportantForAccessibility(view);
        }
        
        @Override
        public int getMinimumHeight(final View view) {
            return ViewCompatJB.getMinimumHeight(view);
        }
        
        @Override
        public int getMinimumWidth(final View view) {
            return ViewCompatJB.getMinimumWidth(view);
        }
        
        @Override
        public ViewParent getParentForAccessibility(final View view) {
            return ViewCompatJB.getParentForAccessibility(view);
        }
        
        @Override
        public boolean hasOverlappingRendering(final View view) {
            return ViewCompatJB.hasOverlappingRendering(view);
        }
        
        @Override
        public boolean hasTransientState(final View view) {
            return ViewCompatJB.hasTransientState(view);
        }
        
        @Override
        public boolean performAccessibilityAction(final View view, final int n, final Bundle bundle) {
            return ViewCompatJB.performAccessibilityAction(view, n, bundle);
        }
        
        @Override
        public void postInvalidateOnAnimation(final View view) {
            ViewCompatJB.postInvalidateOnAnimation(view);
        }
        
        @Override
        public void postInvalidateOnAnimation(final View view, final int n, final int n2, final int n3, final int n4) {
            ViewCompatJB.postInvalidateOnAnimation(view, n, n2, n3, n4);
        }
        
        @Override
        public void postOnAnimation(final View view, final Runnable runnable) {
            ViewCompatJB.postOnAnimation(view, runnable);
        }
        
        @Override
        public void postOnAnimationDelayed(final View view, final Runnable runnable, final long n) {
            ViewCompatJB.postOnAnimationDelayed(view, runnable, n);
        }
        
        @Override
        public void requestApplyInsets(final View view) {
            ViewCompatJB.requestApplyInsets(view);
        }
        
        @Override
        public void setHasTransientState(final View view, final boolean b) {
            ViewCompatJB.setHasTransientState(view, b);
        }
        
        @Override
        public void setImportantForAccessibility(final View view, final int n) {
            int n2 = n;
            if (n == 4) {
                n2 = 2;
            }
            ViewCompatJB.setImportantForAccessibility(view, n2);
        }
    }
    
    static class JbMr1ViewCompatImpl extends JBViewCompatImpl
    {
        @Override
        public int getLabelFor(final View view) {
            return ViewCompatJellybeanMr1.getLabelFor(view);
        }
        
        @Override
        public int getLayoutDirection(final View view) {
            return ViewCompatJellybeanMr1.getLayoutDirection(view);
        }
        
        @Override
        public int getPaddingEnd(final View view) {
            return ViewCompatJellybeanMr1.getPaddingEnd(view);
        }
        
        @Override
        public int getPaddingStart(final View view) {
            return ViewCompatJellybeanMr1.getPaddingStart(view);
        }
        
        @Override
        public int getWindowSystemUiVisibility(final View view) {
            return ViewCompatJellybeanMr1.getWindowSystemUiVisibility(view);
        }
        
        @Override
        public boolean isPaddingRelative(final View view) {
            return ViewCompatJellybeanMr1.isPaddingRelative(view);
        }
        
        @Override
        public void setLabelFor(final View view, final int n) {
            ViewCompatJellybeanMr1.setLabelFor(view, n);
        }
        
        @Override
        public void setLayerPaint(final View view, final Paint paint) {
            ViewCompatJellybeanMr1.setLayerPaint(view, paint);
        }
        
        @Override
        public void setLayoutDirection(final View view, final int n) {
            ViewCompatJellybeanMr1.setLayoutDirection(view, n);
        }
        
        @Override
        public void setPaddingRelative(final View view, final int n, final int n2, final int n3, final int n4) {
            ViewCompatJellybeanMr1.setPaddingRelative(view, n, n2, n3, n4);
        }
    }
    
    static class JbMr2ViewCompatImpl extends JbMr1ViewCompatImpl
    {
        @Override
        public Rect getClipBounds(final View view) {
            return ViewCompatJellybeanMr2.getClipBounds(view);
        }
        
        @Override
        public void setClipBounds(final View view, final Rect rect) {
            ViewCompatJellybeanMr2.setClipBounds(view, rect);
        }
    }
    
    static class KitKatViewCompatImpl extends JbMr2ViewCompatImpl
    {
        @Override
        public int getAccessibilityLiveRegion(final View view) {
            return ViewCompatKitKat.getAccessibilityLiveRegion(view);
        }
        
        @Override
        public boolean isAttachedToWindow(final View view) {
            return ViewCompatKitKat.isAttachedToWindow(view);
        }
        
        @Override
        public boolean isLaidOut(final View view) {
            return ViewCompatKitKat.isLaidOut(view);
        }
        
        @Override
        public void setAccessibilityLiveRegion(final View view, final int n) {
            ViewCompatKitKat.setAccessibilityLiveRegion(view, n);
        }
        
        @Override
        public void setImportantForAccessibility(final View view, final int n) {
            ViewCompatJB.setImportantForAccessibility(view, n);
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    private @interface LayerType {
    }
    
    @Retention(RetentionPolicy.SOURCE)
    private @interface LayoutDirectionMode {
    }
    
    static class LollipopViewCompatImpl extends KitKatViewCompatImpl
    {
        @Override
        public WindowInsetsCompat dispatchApplyWindowInsets(final View view, final WindowInsetsCompat windowInsetsCompat) {
            return ViewCompatLollipop.dispatchApplyWindowInsets(view, windowInsetsCompat);
        }
        
        @Override
        public boolean dispatchNestedFling(final View view, final float n, final float n2, final boolean b) {
            return ViewCompatLollipop.dispatchNestedFling(view, n, n2, b);
        }
        
        @Override
        public boolean dispatchNestedPreFling(final View view, final float n, final float n2) {
            return ViewCompatLollipop.dispatchNestedPreFling(view, n, n2);
        }
        
        @Override
        public boolean dispatchNestedPreScroll(final View view, final int n, final int n2, final int[] array, final int[] array2) {
            return ViewCompatLollipop.dispatchNestedPreScroll(view, n, n2, array, array2);
        }
        
        @Override
        public boolean dispatchNestedScroll(final View view, final int n, final int n2, final int n3, final int n4, final int[] array) {
            return ViewCompatLollipop.dispatchNestedScroll(view, n, n2, n3, n4, array);
        }
        
        @Override
        public ColorStateList getBackgroundTintList(final View view) {
            return ViewCompatLollipop.getBackgroundTintList(view);
        }
        
        @Override
        public PorterDuff$Mode getBackgroundTintMode(final View view) {
            return ViewCompatLollipop.getBackgroundTintMode(view);
        }
        
        @Override
        public float getElevation(final View view) {
            return ViewCompatLollipop.getElevation(view);
        }
        
        @Override
        public String getTransitionName(final View view) {
            return ViewCompatLollipop.getTransitionName(view);
        }
        
        @Override
        public float getTranslationZ(final View view) {
            return ViewCompatLollipop.getTranslationZ(view);
        }
        
        @Override
        public float getZ(final View view) {
            return ViewCompatLollipop.getZ(view);
        }
        
        @Override
        public boolean hasNestedScrollingParent(final View view) {
            return ViewCompatLollipop.hasNestedScrollingParent(view);
        }
        
        @Override
        public boolean isImportantForAccessibility(final View view) {
            return ViewCompatLollipop.isImportantForAccessibility(view);
        }
        
        @Override
        public boolean isNestedScrollingEnabled(final View view) {
            return ViewCompatLollipop.isNestedScrollingEnabled(view);
        }
        
        @Override
        public WindowInsetsCompat onApplyWindowInsets(final View view, final WindowInsetsCompat windowInsetsCompat) {
            return ViewCompatLollipop.onApplyWindowInsets(view, windowInsetsCompat);
        }
        
        @Override
        public void requestApplyInsets(final View view) {
            ViewCompatLollipop.requestApplyInsets(view);
        }
        
        @Override
        public void setBackgroundTintList(final View view, final ColorStateList list) {
            ViewCompatLollipop.setBackgroundTintList(view, list);
        }
        
        @Override
        public void setBackgroundTintMode(final View view, final PorterDuff$Mode porterDuff$Mode) {
            ViewCompatLollipop.setBackgroundTintMode(view, porterDuff$Mode);
        }
        
        @Override
        public void setElevation(final View view, final float n) {
            ViewCompatLollipop.setElevation(view, n);
        }
        
        @Override
        public void setNestedScrollingEnabled(final View view, final boolean b) {
            ViewCompatLollipop.setNestedScrollingEnabled(view, b);
        }
        
        @Override
        public void setOnApplyWindowInsetsListener(final View view, final OnApplyWindowInsetsListener onApplyWindowInsetsListener) {
            ViewCompatLollipop.setOnApplyWindowInsetsListener(view, onApplyWindowInsetsListener);
        }
        
        @Override
        public void setTransitionName(final View view, final String s) {
            ViewCompatLollipop.setTransitionName(view, s);
        }
        
        @Override
        public void setTranslationZ(final View view, final float n) {
            ViewCompatLollipop.setTranslationZ(view, n);
        }
        
        @Override
        public boolean startNestedScroll(final View view, final int n) {
            return ViewCompatLollipop.startNestedScroll(view, n);
        }
        
        @Override
        public void stopNestedScroll(final View view) {
            ViewCompatLollipop.stopNestedScroll(view);
        }
    }
    
    static class MarshmallowViewCompatImpl extends LollipopViewCompatImpl
    {
        @Override
        public int getScrollIndicators(final View view) {
            return ViewCompatMarshmallow.getScrollIndicators(view);
        }
        
        @Override
        public void offsetLeftAndRight(final View view, final int n) {
            ViewCompatMarshmallow.offsetLeftAndRight(view, n);
        }
        
        @Override
        public void offsetTopAndBottom(final View view, final int n) {
            ViewCompatMarshmallow.offsetTopAndBottom(view, n);
        }
        
        @Override
        public void setScrollIndicators(final View view, final int n) {
            ViewCompatMarshmallow.setScrollIndicators(view, n);
        }
        
        @Override
        public void setScrollIndicators(final View view, final int n, final int n2) {
            ViewCompatMarshmallow.setScrollIndicators(view, n, n2);
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    private @interface OverScroll {
    }
    
    @Retention(RetentionPolicy.SOURCE)
    private @interface ResolvedLayoutDirectionMode {
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface ScrollIndicators {
    }
    
    interface ViewCompatImpl
    {
        ViewPropertyAnimatorCompat animate(final View p0);
        
        boolean canScrollHorizontally(final View p0, final int p1);
        
        boolean canScrollVertically(final View p0, final int p1);
        
        int combineMeasuredStates(final int p0, final int p1);
        
        WindowInsetsCompat dispatchApplyWindowInsets(final View p0, final WindowInsetsCompat p1);
        
        void dispatchFinishTemporaryDetach(final View p0);
        
        boolean dispatchNestedFling(final View p0, final float p1, final float p2, final boolean p3);
        
        boolean dispatchNestedPreFling(final View p0, final float p1, final float p2);
        
        boolean dispatchNestedPreScroll(final View p0, final int p1, final int p2, final int[] p3, final int[] p4);
        
        boolean dispatchNestedScroll(final View p0, final int p1, final int p2, final int p3, final int p4, final int[] p5);
        
        void dispatchStartTemporaryDetach(final View p0);
        
        int getAccessibilityLiveRegion(final View p0);
        
        AccessibilityNodeProviderCompat getAccessibilityNodeProvider(final View p0);
        
        float getAlpha(final View p0);
        
        ColorStateList getBackgroundTintList(final View p0);
        
        PorterDuff$Mode getBackgroundTintMode(final View p0);
        
        Rect getClipBounds(final View p0);
        
        float getElevation(final View p0);
        
        boolean getFitsSystemWindows(final View p0);
        
        int getImportantForAccessibility(final View p0);
        
        int getLabelFor(final View p0);
        
        int getLayerType(final View p0);
        
        int getLayoutDirection(final View p0);
        
        int getMeasuredHeightAndState(final View p0);
        
        int getMeasuredState(final View p0);
        
        int getMeasuredWidthAndState(final View p0);
        
        int getMinimumHeight(final View p0);
        
        int getMinimumWidth(final View p0);
        
        int getOverScrollMode(final View p0);
        
        int getPaddingEnd(final View p0);
        
        int getPaddingStart(final View p0);
        
        ViewParent getParentForAccessibility(final View p0);
        
        float getPivotX(final View p0);
        
        float getPivotY(final View p0);
        
        float getRotation(final View p0);
        
        float getRotationX(final View p0);
        
        float getRotationY(final View p0);
        
        float getScaleX(final View p0);
        
        float getScaleY(final View p0);
        
        int getScrollIndicators(final View p0);
        
        String getTransitionName(final View p0);
        
        float getTranslationX(final View p0);
        
        float getTranslationY(final View p0);
        
        float getTranslationZ(final View p0);
        
        int getWindowSystemUiVisibility(final View p0);
        
        float getX(final View p0);
        
        float getY(final View p0);
        
        float getZ(final View p0);
        
        boolean hasAccessibilityDelegate(final View p0);
        
        boolean hasNestedScrollingParent(final View p0);
        
        boolean hasOnClickListeners(final View p0);
        
        boolean hasOverlappingRendering(final View p0);
        
        boolean hasTransientState(final View p0);
        
        boolean isAttachedToWindow(final View p0);
        
        boolean isImportantForAccessibility(final View p0);
        
        boolean isLaidOut(final View p0);
        
        boolean isNestedScrollingEnabled(final View p0);
        
        boolean isOpaque(final View p0);
        
        boolean isPaddingRelative(final View p0);
        
        void jumpDrawablesToCurrentState(final View p0);
        
        void offsetLeftAndRight(final View p0, final int p1);
        
        void offsetTopAndBottom(final View p0, final int p1);
        
        WindowInsetsCompat onApplyWindowInsets(final View p0, final WindowInsetsCompat p1);
        
        void onInitializeAccessibilityEvent(final View p0, final AccessibilityEvent p1);
        
        void onInitializeAccessibilityNodeInfo(final View p0, final AccessibilityNodeInfoCompat p1);
        
        void onPopulateAccessibilityEvent(final View p0, final AccessibilityEvent p1);
        
        boolean performAccessibilityAction(final View p0, final int p1, final Bundle p2);
        
        void postInvalidateOnAnimation(final View p0);
        
        void postInvalidateOnAnimation(final View p0, final int p1, final int p2, final int p3, final int p4);
        
        void postOnAnimation(final View p0, final Runnable p1);
        
        void postOnAnimationDelayed(final View p0, final Runnable p1, final long p2);
        
        void requestApplyInsets(final View p0);
        
        int resolveSizeAndState(final int p0, final int p1, final int p2);
        
        void setAccessibilityDelegate(final View p0, @Nullable final AccessibilityDelegateCompat p1);
        
        void setAccessibilityLiveRegion(final View p0, final int p1);
        
        void setActivated(final View p0, final boolean p1);
        
        void setAlpha(final View p0, final float p1);
        
        void setBackgroundTintList(final View p0, final ColorStateList p1);
        
        void setBackgroundTintMode(final View p0, final PorterDuff$Mode p1);
        
        void setChildrenDrawingOrderEnabled(final ViewGroup p0, final boolean p1);
        
        void setClipBounds(final View p0, final Rect p1);
        
        void setElevation(final View p0, final float p1);
        
        void setFitsSystemWindows(final View p0, final boolean p1);
        
        void setHasTransientState(final View p0, final boolean p1);
        
        void setImportantForAccessibility(final View p0, final int p1);
        
        void setLabelFor(final View p0, final int p1);
        
        void setLayerPaint(final View p0, final Paint p1);
        
        void setLayerType(final View p0, final int p1, final Paint p2);
        
        void setLayoutDirection(final View p0, final int p1);
        
        void setNestedScrollingEnabled(final View p0, final boolean p1);
        
        void setOnApplyWindowInsetsListener(final View p0, final OnApplyWindowInsetsListener p1);
        
        void setOverScrollMode(final View p0, final int p1);
        
        void setPaddingRelative(final View p0, final int p1, final int p2, final int p3, final int p4);
        
        void setPivotX(final View p0, final float p1);
        
        void setPivotY(final View p0, final float p1);
        
        void setRotation(final View p0, final float p1);
        
        void setRotationX(final View p0, final float p1);
        
        void setRotationY(final View p0, final float p1);
        
        void setSaveFromParentEnabled(final View p0, final boolean p1);
        
        void setScaleX(final View p0, final float p1);
        
        void setScaleY(final View p0, final float p1);
        
        void setScrollIndicators(final View p0, final int p1);
        
        void setScrollIndicators(final View p0, final int p1, final int p2);
        
        void setTransitionName(final View p0, final String p1);
        
        void setTranslationX(final View p0, final float p1);
        
        void setTranslationY(final View p0, final float p1);
        
        void setTranslationZ(final View p0, final float p1);
        
        void setX(final View p0, final float p1);
        
        void setY(final View p0, final float p1);
        
        boolean startNestedScroll(final View p0, final int p1);
        
        void stopNestedScroll(final View p0);
    }
}
