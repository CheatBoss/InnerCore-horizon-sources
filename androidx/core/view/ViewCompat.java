package androidx.core.view;

import java.util.concurrent.atomic.*;
import androidx.core.*;
import androidx.collection.*;
import androidx.core.view.accessibility.*;
import android.content.res.*;
import android.annotation.*;
import android.view.accessibility.*;
import android.text.*;
import android.os.*;
import android.animation.*;
import android.graphics.drawable.*;
import java.lang.reflect.*;
import android.graphics.*;
import android.content.*;
import android.view.*;
import java.util.*;
import java.lang.annotation.*;
import androidx.annotation.*;
import java.lang.ref.*;
import android.util.*;

public class ViewCompat
{
    private static final int[] ACCESSIBILITY_ACTIONS_RESOURCE_IDS;
    public static final int ACCESSIBILITY_LIVE_REGION_ASSERTIVE = 2;
    public static final int ACCESSIBILITY_LIVE_REGION_NONE = 0;
    public static final int ACCESSIBILITY_LIVE_REGION_POLITE = 1;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_AUTO = 0;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_NO = 2;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS = 4;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_YES = 1;
    @Deprecated
    public static final int LAYER_TYPE_HARDWARE = 2;
    @Deprecated
    public static final int LAYER_TYPE_NONE = 0;
    @Deprecated
    public static final int LAYER_TYPE_SOFTWARE = 1;
    public static final int LAYOUT_DIRECTION_INHERIT = 2;
    public static final int LAYOUT_DIRECTION_LOCALE = 3;
    public static final int LAYOUT_DIRECTION_LTR = 0;
    public static final int LAYOUT_DIRECTION_RTL = 1;
    @Deprecated
    public static final int MEASURED_HEIGHT_STATE_SHIFT = 16;
    @Deprecated
    public static final int MEASURED_SIZE_MASK = 16777215;
    @Deprecated
    public static final int MEASURED_STATE_MASK = -16777216;
    @Deprecated
    public static final int MEASURED_STATE_TOO_SMALL = 16777216;
    @Deprecated
    public static final int OVER_SCROLL_ALWAYS = 0;
    @Deprecated
    public static final int OVER_SCROLL_IF_CONTENT_SCROLLS = 1;
    @Deprecated
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
    public static final int TYPE_NON_TOUCH = 1;
    public static final int TYPE_TOUCH = 0;
    private static boolean sAccessibilityDelegateCheckFailed;
    private static Field sAccessibilityDelegateField;
    private static AccessibilityPaneVisibilityManager sAccessibilityPaneVisibilityManager;
    private static Method sChildrenDrawingOrderMethod;
    private static Method sDispatchFinishTemporaryDetach;
    private static Method sDispatchStartTemporaryDetach;
    private static Field sMinHeightField;
    private static boolean sMinHeightFieldFetched;
    private static Field sMinWidthField;
    private static boolean sMinWidthFieldFetched;
    private static final AtomicInteger sNextGeneratedId;
    private static boolean sTempDetachBound;
    private static ThreadLocal<Rect> sThreadLocalRect;
    private static WeakHashMap<View, String> sTransitionNameMap;
    private static WeakHashMap<View, ViewPropertyAnimatorCompat> sViewPropertyAnimatorMap;
    
    static {
        sNextGeneratedId = new AtomicInteger(1);
        ViewCompat.sViewPropertyAnimatorMap = null;
        ViewCompat.sAccessibilityDelegateCheckFailed = false;
        ACCESSIBILITY_ACTIONS_RESOURCE_IDS = new int[] { R$id.accessibility_custom_action_0, R$id.accessibility_custom_action_1, R$id.accessibility_custom_action_2, R$id.accessibility_custom_action_3, R$id.accessibility_custom_action_4, R$id.accessibility_custom_action_5, R$id.accessibility_custom_action_6, R$id.accessibility_custom_action_7, R$id.accessibility_custom_action_8, R$id.accessibility_custom_action_9, R$id.accessibility_custom_action_10, R$id.accessibility_custom_action_11, R$id.accessibility_custom_action_12, R$id.accessibility_custom_action_13, R$id.accessibility_custom_action_14, R$id.accessibility_custom_action_15, R$id.accessibility_custom_action_16, R$id.accessibility_custom_action_17, R$id.accessibility_custom_action_18, R$id.accessibility_custom_action_19, R$id.accessibility_custom_action_20, R$id.accessibility_custom_action_21, R$id.accessibility_custom_action_22, R$id.accessibility_custom_action_23, R$id.accessibility_custom_action_24, R$id.accessibility_custom_action_25, R$id.accessibility_custom_action_26, R$id.accessibility_custom_action_27, R$id.accessibility_custom_action_28, R$id.accessibility_custom_action_29, R$id.accessibility_custom_action_30, R$id.accessibility_custom_action_31 };
        ViewCompat.sAccessibilityPaneVisibilityManager = new AccessibilityPaneVisibilityManager();
    }
    
    protected ViewCompat() {
    }
    
    private static AccessibilityViewProperty<Boolean> accessibilityHeadingProperty() {
        return (AccessibilityViewProperty<Boolean>)new AccessibilityViewProperty<Boolean>(R$id.tag_accessibility_heading, Boolean.class, 28) {
            @RequiresApi(28)
            Boolean frameworkGet(final View view) {
                return view.isAccessibilityHeading();
            }
            
            @RequiresApi(28)
            void frameworkSet(final View view, final Boolean b) {
                view.setAccessibilityHeading((boolean)b);
            }
            
            boolean shouldUpdate(final Boolean b, final Boolean b2) {
                return ((AccessibilityViewProperty)this).booleanNullToFalseEquals(b, b2) ^ true;
            }
        };
    }
    
    public static int addAccessibilityAction(@NonNull final View view, @NonNull final CharSequence charSequence, @NonNull final AccessibilityViewCommand accessibilityViewCommand) {
        final int availableActionIdFromResources = getAvailableActionIdFromResources(view);
        if (availableActionIdFromResources != -1) {
            addAccessibilityAction(view, new AccessibilityNodeInfoCompat.AccessibilityActionCompat(availableActionIdFromResources, charSequence, accessibilityViewCommand));
        }
        return availableActionIdFromResources;
    }
    
    private static void addAccessibilityAction(@NonNull final View view, @NonNull final AccessibilityNodeInfoCompat.AccessibilityActionCompat accessibilityActionCompat) {
        if (Build$VERSION.SDK_INT >= 21) {
            getOrCreateAccessibilityDelegateCompat(view);
            removeActionWithId(accessibilityActionCompat.getId(), view);
            getActionList(view).add(accessibilityActionCompat);
            notifyViewAccessibilityStateChangedIfNeeded(view, 0);
        }
    }
    
    public static void addKeyboardNavigationClusters(@NonNull final View view, @NonNull final Collection<View> collection, final int n) {
        if (Build$VERSION.SDK_INT >= 26) {
            view.addKeyboardNavigationClusters((Collection)collection, n);
        }
    }
    
    public static void addOnUnhandledKeyEventListener(@NonNull final View view, @NonNull final OnUnhandledKeyEventListenerCompat onUnhandledKeyEventListenerCompat) {
        if (Build$VERSION.SDK_INT >= 28) {
            Object o;
            if ((o = view.getTag(R$id.tag_unhandled_key_listeners)) == null) {
                o = new ArrayMap();
                view.setTag(R$id.tag_unhandled_key_listeners, o);
            }
            final View$OnUnhandledKeyEventListener view$OnUnhandledKeyEventListener = (View$OnUnhandledKeyEventListener)new View$OnUnhandledKeyEventListener() {
                public boolean onUnhandledKeyEvent(final View view, final KeyEvent keyEvent) {
                    return onUnhandledKeyEventListenerCompat.onUnhandledKeyEvent(view, keyEvent);
                }
            };
            ((Map<OnUnhandledKeyEventListenerCompat, View$OnUnhandledKeyEventListener>)o).put(onUnhandledKeyEventListenerCompat, (View$OnUnhandledKeyEventListener)view$OnUnhandledKeyEventListener);
            view.addOnUnhandledKeyEventListener((View$OnUnhandledKeyEventListener)view$OnUnhandledKeyEventListener);
            return;
        }
        ArrayList<OnUnhandledKeyEventListenerCompat> list;
        if ((list = (ArrayList<OnUnhandledKeyEventListenerCompat>)view.getTag(R$id.tag_unhandled_key_listeners)) == null) {
            list = new ArrayList<OnUnhandledKeyEventListenerCompat>();
            view.setTag(R$id.tag_unhandled_key_listeners, (Object)list);
        }
        list.add(onUnhandledKeyEventListenerCompat);
        if (list.size() == 1) {
            UnhandledKeyEventManager.registerListeningView(view);
        }
    }
    
    @NonNull
    public static ViewPropertyAnimatorCompat animate(@NonNull final View view) {
        if (ViewCompat.sViewPropertyAnimatorMap == null) {
            ViewCompat.sViewPropertyAnimatorMap = new WeakHashMap<View, ViewPropertyAnimatorCompat>();
        }
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat;
        if ((viewPropertyAnimatorCompat = ViewCompat.sViewPropertyAnimatorMap.get(view)) == null) {
            viewPropertyAnimatorCompat = new ViewPropertyAnimatorCompat(view);
            ViewCompat.sViewPropertyAnimatorMap.put(view, viewPropertyAnimatorCompat);
        }
        return viewPropertyAnimatorCompat;
    }
    
    private static void bindTempDetach() {
        try {
            ViewCompat.sDispatchStartTemporaryDetach = View.class.getDeclaredMethod("dispatchStartTemporaryDetach", (Class<?>[])new Class[0]);
            ViewCompat.sDispatchFinishTemporaryDetach = View.class.getDeclaredMethod("dispatchFinishTemporaryDetach", (Class<?>[])new Class[0]);
        }
        catch (NoSuchMethodException ex) {
            Log.e("ViewCompat", "Couldn't find method", (Throwable)ex);
        }
        ViewCompat.sTempDetachBound = true;
    }
    
    @Deprecated
    public static boolean canScrollHorizontally(final View view, final int n) {
        return view.canScrollHorizontally(n);
    }
    
    @Deprecated
    public static boolean canScrollVertically(final View view, final int n) {
        return view.canScrollVertically(n);
    }
    
    public static void cancelDragAndDrop(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 24) {
            view.cancelDragAndDrop();
        }
    }
    
    @Deprecated
    public static int combineMeasuredStates(final int n, final int n2) {
        return View.combineMeasuredStates(n, n2);
    }
    
    private static void compatOffsetLeftAndRight(final View view, final int n) {
        view.offsetLeftAndRight(n);
        if (view.getVisibility() == 0) {
            tickleInvalidationFlag(view);
            final ViewParent parent = view.getParent();
            if (parent instanceof View) {
                tickleInvalidationFlag((View)parent);
            }
        }
    }
    
    private static void compatOffsetTopAndBottom(final View view, final int n) {
        view.offsetTopAndBottom(n);
        if (view.getVisibility() == 0) {
            tickleInvalidationFlag(view);
            final ViewParent parent = view.getParent();
            if (parent instanceof View) {
                tickleInvalidationFlag((View)parent);
            }
        }
    }
    
    public static WindowInsetsCompat dispatchApplyWindowInsets(@NonNull final View view, final WindowInsetsCompat windowInsetsCompat) {
        if (Build$VERSION.SDK_INT >= 21) {
            final WindowInsets windowInsets = (WindowInsets)WindowInsetsCompat.unwrap(windowInsetsCompat);
            final WindowInsets dispatchApplyWindowInsets = view.dispatchApplyWindowInsets(windowInsets);
            WindowInsets windowInsets2 = windowInsets;
            if (!dispatchApplyWindowInsets.equals((Object)windowInsets)) {
                windowInsets2 = new WindowInsets(dispatchApplyWindowInsets);
            }
            return WindowInsetsCompat.wrap(windowInsets2);
        }
        return windowInsetsCompat;
    }
    
    public static void dispatchFinishTemporaryDetach(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 24) {
            view.dispatchFinishTemporaryDetach();
            return;
        }
        if (!ViewCompat.sTempDetachBound) {
            bindTempDetach();
        }
        if (ViewCompat.sDispatchFinishTemporaryDetach != null) {
            try {
                ViewCompat.sDispatchFinishTemporaryDetach.invoke(view, new Object[0]);
            }
            catch (Exception ex) {
                Log.d("ViewCompat", "Error calling dispatchFinishTemporaryDetach", (Throwable)ex);
            }
            return;
        }
        view.onFinishTemporaryDetach();
    }
    
    public static boolean dispatchNestedFling(@NonNull final View view, final float n, final float n2, final boolean b) {
        if (Build$VERSION.SDK_INT >= 21) {
            return view.dispatchNestedFling(n, n2, b);
        }
        return view instanceof NestedScrollingChild && ((NestedScrollingChild)view).dispatchNestedFling(n, n2, b);
    }
    
    public static boolean dispatchNestedPreFling(@NonNull final View view, final float n, final float n2) {
        if (Build$VERSION.SDK_INT >= 21) {
            return view.dispatchNestedPreFling(n, n2);
        }
        return view instanceof NestedScrollingChild && ((NestedScrollingChild)view).dispatchNestedPreFling(n, n2);
    }
    
    public static boolean dispatchNestedPreScroll(@NonNull final View view, final int n, final int n2, @Nullable final int[] array, @Nullable final int[] array2) {
        if (Build$VERSION.SDK_INT >= 21) {
            return view.dispatchNestedPreScroll(n, n2, array, array2);
        }
        return view instanceof NestedScrollingChild && ((NestedScrollingChild)view).dispatchNestedPreScroll(n, n2, array, array2);
    }
    
    public static boolean dispatchNestedPreScroll(@NonNull final View view, final int n, final int n2, @Nullable final int[] array, @Nullable final int[] array2, final int n3) {
        if (view instanceof NestedScrollingChild2) {
            return ((NestedScrollingChild2)view).dispatchNestedPreScroll(n, n2, array, array2, n3);
        }
        return n3 == 0 && dispatchNestedPreScroll(view, n, n2, array, array2);
    }
    
    public static void dispatchNestedScroll(@NonNull final View view, final int n, final int n2, final int n3, final int n4, @Nullable final int[] array, final int n5, @NonNull final int[] array2) {
        if (view instanceof NestedScrollingChild3) {
            ((NestedScrollingChild3)view).dispatchNestedScroll(n, n2, n3, n4, array, n5, array2);
            return;
        }
        dispatchNestedScroll(view, n, n2, n3, n4, array, n5);
    }
    
    public static boolean dispatchNestedScroll(@NonNull final View view, final int n, final int n2, final int n3, final int n4, @Nullable final int[] array) {
        if (Build$VERSION.SDK_INT >= 21) {
            return view.dispatchNestedScroll(n, n2, n3, n4, array);
        }
        return view instanceof NestedScrollingChild && ((NestedScrollingChild)view).dispatchNestedScroll(n, n2, n3, n4, array);
    }
    
    public static boolean dispatchNestedScroll(@NonNull final View view, final int n, final int n2, final int n3, final int n4, @Nullable final int[] array, final int n5) {
        if (view instanceof NestedScrollingChild2) {
            return ((NestedScrollingChild2)view).dispatchNestedScroll(n, n2, n3, n4, array, n5);
        }
        return n5 == 0 && dispatchNestedScroll(view, n, n2, n3, n4, array);
    }
    
    public static void dispatchStartTemporaryDetach(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 24) {
            view.dispatchStartTemporaryDetach();
            return;
        }
        if (!ViewCompat.sTempDetachBound) {
            bindTempDetach();
        }
        if (ViewCompat.sDispatchStartTemporaryDetach != null) {
            try {
                ViewCompat.sDispatchStartTemporaryDetach.invoke(view, new Object[0]);
            }
            catch (Exception ex) {
                Log.d("ViewCompat", "Error calling dispatchStartTemporaryDetach", (Throwable)ex);
            }
            return;
        }
        view.onStartTemporaryDetach();
    }
    
    @UiThread
    static boolean dispatchUnhandledKeyEventBeforeCallback(final View view, final KeyEvent keyEvent) {
        return Build$VERSION.SDK_INT < 28 && UnhandledKeyEventManager.at(view).dispatch(view, keyEvent);
    }
    
    @UiThread
    static boolean dispatchUnhandledKeyEventBeforeHierarchy(final View view, final KeyEvent keyEvent) {
        return Build$VERSION.SDK_INT < 28 && UnhandledKeyEventManager.at(view).preDispatch(keyEvent);
    }
    
    public static void enableAccessibleClickableSpanSupport(final View view) {
        if (Build$VERSION.SDK_INT >= 19) {
            getOrCreateAccessibilityDelegateCompat(view);
        }
    }
    
    public static int generateViewId() {
        if (Build$VERSION.SDK_INT >= 17) {
            return View.generateViewId();
        }
        int value;
        int n;
        do {
            value = ViewCompat.sNextGeneratedId.get();
            if ((n = value + 1) > 16777215) {
                n = 1;
            }
        } while (!ViewCompat.sNextGeneratedId.compareAndSet(value, n));
        return value;
    }
    
    @Nullable
    public static AccessibilityDelegateCompat getAccessibilityDelegate(@NonNull final View view) {
        final View$AccessibilityDelegate accessibilityDelegateInternal = getAccessibilityDelegateInternal(view);
        if (accessibilityDelegateInternal == null) {
            return null;
        }
        if (accessibilityDelegateInternal instanceof AccessibilityDelegateCompat.AccessibilityDelegateAdapter) {
            return ((AccessibilityDelegateCompat.AccessibilityDelegateAdapter)accessibilityDelegateInternal).mCompat;
        }
        return new AccessibilityDelegateCompat(accessibilityDelegateInternal);
    }
    
    @Nullable
    private static View$AccessibilityDelegate getAccessibilityDelegateInternal(@NonNull final View view) {
        if (ViewCompat.sAccessibilityDelegateCheckFailed) {
            return null;
        }
        if (ViewCompat.sAccessibilityDelegateField == null) {
            try {
                (ViewCompat.sAccessibilityDelegateField = View.class.getDeclaredField("mAccessibilityDelegate")).setAccessible(true);
            }
            catch (Throwable t) {
                ViewCompat.sAccessibilityDelegateCheckFailed = true;
                return null;
            }
        }
        try {
            final Object value = ViewCompat.sAccessibilityDelegateField.get(view);
            if (value instanceof View$AccessibilityDelegate) {
                return (View$AccessibilityDelegate)value;
            }
            return null;
        }
        catch (Throwable t2) {
            ViewCompat.sAccessibilityDelegateCheckFailed = true;
            return null;
        }
    }
    
    public static int getAccessibilityLiveRegion(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 19) {
            return view.getAccessibilityLiveRegion();
        }
        return 0;
    }
    
    public static AccessibilityNodeProviderCompat getAccessibilityNodeProvider(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 16) {
            final AccessibilityNodeProvider accessibilityNodeProvider = view.getAccessibilityNodeProvider();
            if (accessibilityNodeProvider != null) {
                return new AccessibilityNodeProviderCompat(accessibilityNodeProvider);
            }
        }
        return null;
    }
    
    @UiThread
    public static CharSequence getAccessibilityPaneTitle(final View view) {
        return paneTitleProperty().get(view);
    }
    
    private static List<AccessibilityNodeInfoCompat.AccessibilityActionCompat> getActionList(final View view) {
        ArrayList<AccessibilityNodeInfoCompat.AccessibilityActionCompat> list;
        if ((list = (ArrayList<AccessibilityNodeInfoCompat.AccessibilityActionCompat>)view.getTag(R$id.tag_accessibility_actions)) == null) {
            list = new ArrayList<AccessibilityNodeInfoCompat.AccessibilityActionCompat>();
            view.setTag(R$id.tag_accessibility_actions, (Object)list);
        }
        return list;
    }
    
    @Deprecated
    public static float getAlpha(final View view) {
        return view.getAlpha();
    }
    
    private static int getAvailableActionIdFromResources(final View view) {
        final List<AccessibilityNodeInfoCompat.AccessibilityActionCompat> actionList = getActionList(view);
        int n = -1;
        for (int n2 = 0; n2 < ViewCompat.ACCESSIBILITY_ACTIONS_RESOURCE_IDS.length && n == -1; ++n2) {
            final int n3 = ViewCompat.ACCESSIBILITY_ACTIONS_RESOURCE_IDS[n2];
            boolean b = true;
            for (int i = 0; i < actionList.size(); ++i) {
                b &= (actionList.get(i).getId() != n3);
            }
            if (b) {
                n = n3;
            }
        }
        return n;
    }
    
    public static ColorStateList getBackgroundTintList(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 21) {
            return view.getBackgroundTintList();
        }
        if (view instanceof TintableBackgroundView) {
            return ((TintableBackgroundView)view).getSupportBackgroundTintList();
        }
        return null;
    }
    
    public static PorterDuff$Mode getBackgroundTintMode(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 21) {
            return view.getBackgroundTintMode();
        }
        if (view instanceof TintableBackgroundView) {
            return ((TintableBackgroundView)view).getSupportBackgroundTintMode();
        }
        return null;
    }
    
    @Nullable
    public static Rect getClipBounds(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 18) {
            return view.getClipBounds();
        }
        return null;
    }
    
    @Nullable
    public static Display getDisplay(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 17) {
            return view.getDisplay();
        }
        if (isAttachedToWindow(view)) {
            return ((WindowManager)view.getContext().getSystemService("window")).getDefaultDisplay();
        }
        return null;
    }
    
    public static float getElevation(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 21) {
            return view.getElevation();
        }
        return 0.0f;
    }
    
    private static Rect getEmptyTempRect() {
        if (ViewCompat.sThreadLocalRect == null) {
            ViewCompat.sThreadLocalRect = new ThreadLocal<Rect>();
        }
        Rect rect;
        if ((rect = ViewCompat.sThreadLocalRect.get()) == null) {
            rect = new Rect();
            ViewCompat.sThreadLocalRect.set(rect);
        }
        rect.setEmpty();
        return rect;
    }
    
    public static boolean getFitsSystemWindows(@NonNull final View view) {
        return Build$VERSION.SDK_INT >= 16 && view.getFitsSystemWindows();
    }
    
    public static int getImportantForAccessibility(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 16) {
            return view.getImportantForAccessibility();
        }
        return 0;
    }
    
    @SuppressLint({ "InlinedApi" })
    public static int getImportantForAutofill(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 26) {
            return view.getImportantForAutofill();
        }
        return 0;
    }
    
    public static int getLabelFor(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 17) {
            return view.getLabelFor();
        }
        return 0;
    }
    
    @Deprecated
    public static int getLayerType(final View view) {
        return view.getLayerType();
    }
    
    public static int getLayoutDirection(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 17) {
            return view.getLayoutDirection();
        }
        return 0;
    }
    
    @Deprecated
    @Nullable
    public static Matrix getMatrix(final View view) {
        return view.getMatrix();
    }
    
    @Deprecated
    public static int getMeasuredHeightAndState(final View view) {
        return view.getMeasuredHeightAndState();
    }
    
    @Deprecated
    public static int getMeasuredState(final View view) {
        return view.getMeasuredState();
    }
    
    @Deprecated
    public static int getMeasuredWidthAndState(final View view) {
        return view.getMeasuredWidthAndState();
    }
    
    public static int getMinimumHeight(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 16) {
            return view.getMinimumHeight();
        }
        if (!ViewCompat.sMinHeightFieldFetched) {
            try {
                (ViewCompat.sMinHeightField = View.class.getDeclaredField("mMinHeight")).setAccessible(true);
            }
            catch (NoSuchFieldException ex) {}
            ViewCompat.sMinHeightFieldFetched = true;
        }
        if (ViewCompat.sMinHeightField != null) {
            try {
                return (int)ViewCompat.sMinHeightField.get(view);
            }
            catch (Exception ex2) {}
        }
        return 0;
    }
    
    public static int getMinimumWidth(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 16) {
            return view.getMinimumWidth();
        }
        if (!ViewCompat.sMinWidthFieldFetched) {
            try {
                (ViewCompat.sMinWidthField = View.class.getDeclaredField("mMinWidth")).setAccessible(true);
            }
            catch (NoSuchFieldException ex) {}
            ViewCompat.sMinWidthFieldFetched = true;
        }
        if (ViewCompat.sMinWidthField != null) {
            try {
                return (int)ViewCompat.sMinWidthField.get(view);
            }
            catch (Exception ex2) {}
        }
        return 0;
    }
    
    public static int getNextClusterForwardId(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 26) {
            return view.getNextClusterForwardId();
        }
        return -1;
    }
    
    static AccessibilityDelegateCompat getOrCreateAccessibilityDelegateCompat(@NonNull final View view) {
        AccessibilityDelegateCompat accessibilityDelegate;
        if ((accessibilityDelegate = getAccessibilityDelegate(view)) == null) {
            accessibilityDelegate = new AccessibilityDelegateCompat();
        }
        setAccessibilityDelegate(view, accessibilityDelegate);
        return accessibilityDelegate;
    }
    
    @Deprecated
    public static int getOverScrollMode(final View view) {
        return view.getOverScrollMode();
    }
    
    @Px
    public static int getPaddingEnd(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 17) {
            return view.getPaddingEnd();
        }
        return view.getPaddingRight();
    }
    
    @Px
    public static int getPaddingStart(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 17) {
            return view.getPaddingStart();
        }
        return view.getPaddingLeft();
    }
    
    public static ViewParent getParentForAccessibility(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 16) {
            return view.getParentForAccessibility();
        }
        return view.getParent();
    }
    
    @Deprecated
    public static float getPivotX(final View view) {
        return view.getPivotX();
    }
    
    @Deprecated
    public static float getPivotY(final View view) {
        return view.getPivotY();
    }
    
    @Deprecated
    public static float getRotation(final View view) {
        return view.getRotation();
    }
    
    @Deprecated
    public static float getRotationX(final View view) {
        return view.getRotationX();
    }
    
    @Deprecated
    public static float getRotationY(final View view) {
        return view.getRotationY();
    }
    
    @Deprecated
    public static float getScaleX(final View view) {
        return view.getScaleX();
    }
    
    @Deprecated
    public static float getScaleY(final View view) {
        return view.getScaleY();
    }
    
    public static int getScrollIndicators(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 23) {
            return view.getScrollIndicators();
        }
        return 0;
    }
    
    @Nullable
    public static String getTransitionName(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 21) {
            return view.getTransitionName();
        }
        if (ViewCompat.sTransitionNameMap == null) {
            return null;
        }
        return ViewCompat.sTransitionNameMap.get(view);
    }
    
    @Deprecated
    public static float getTranslationX(final View view) {
        return view.getTranslationX();
    }
    
    @Deprecated
    public static float getTranslationY(final View view) {
        return view.getTranslationY();
    }
    
    public static float getTranslationZ(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 21) {
            return view.getTranslationZ();
        }
        return 0.0f;
    }
    
    public static int getWindowSystemUiVisibility(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 16) {
            return view.getWindowSystemUiVisibility();
        }
        return 0;
    }
    
    @Deprecated
    public static float getX(final View view) {
        return view.getX();
    }
    
    @Deprecated
    public static float getY(final View view) {
        return view.getY();
    }
    
    public static float getZ(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 21) {
            return view.getZ();
        }
        return 0.0f;
    }
    
    public static boolean hasAccessibilityDelegate(@NonNull final View view) {
        return getAccessibilityDelegateInternal(view) != null;
    }
    
    public static boolean hasExplicitFocusable(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 26) {
            return view.hasExplicitFocusable();
        }
        return view.hasFocusable();
    }
    
    public static boolean hasNestedScrollingParent(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 21) {
            return view.hasNestedScrollingParent();
        }
        return view instanceof NestedScrollingChild && ((NestedScrollingChild)view).hasNestedScrollingParent();
    }
    
    public static boolean hasNestedScrollingParent(@NonNull final View view, final int n) {
        if (view instanceof NestedScrollingChild2) {
            ((NestedScrollingChild2)view).hasNestedScrollingParent(n);
        }
        else if (n == 0) {
            return hasNestedScrollingParent(view);
        }
        return false;
    }
    
    public static boolean hasOnClickListeners(@NonNull final View view) {
        return Build$VERSION.SDK_INT >= 15 && view.hasOnClickListeners();
    }
    
    public static boolean hasOverlappingRendering(@NonNull final View view) {
        return Build$VERSION.SDK_INT < 16 || view.hasOverlappingRendering();
    }
    
    public static boolean hasTransientState(@NonNull final View view) {
        return Build$VERSION.SDK_INT >= 16 && view.hasTransientState();
    }
    
    @UiThread
    public static boolean isAccessibilityHeading(final View view) {
        final Boolean b = accessibilityHeadingProperty().get(view);
        return b != null && b;
    }
    
    public static boolean isAttachedToWindow(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 19) {
            return view.isAttachedToWindow();
        }
        return view.getWindowToken() != null;
    }
    
    public static boolean isFocusedByDefault(@NonNull final View view) {
        return Build$VERSION.SDK_INT >= 26 && view.isFocusedByDefault();
    }
    
    public static boolean isImportantForAccessibility(@NonNull final View view) {
        return Build$VERSION.SDK_INT < 21 || view.isImportantForAccessibility();
    }
    
    public static boolean isImportantForAutofill(@NonNull final View view) {
        return Build$VERSION.SDK_INT < 26 || view.isImportantForAutofill();
    }
    
    public static boolean isInLayout(@NonNull final View view) {
        return Build$VERSION.SDK_INT >= 18 && view.isInLayout();
    }
    
    public static boolean isKeyboardNavigationCluster(@NonNull final View view) {
        return Build$VERSION.SDK_INT >= 26 && view.isKeyboardNavigationCluster();
    }
    
    public static boolean isLaidOut(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 19) {
            return view.isLaidOut();
        }
        return view.getWidth() > 0 && view.getHeight() > 0;
    }
    
    public static boolean isLayoutDirectionResolved(@NonNull final View view) {
        return Build$VERSION.SDK_INT >= 19 && view.isLayoutDirectionResolved();
    }
    
    public static boolean isNestedScrollingEnabled(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 21) {
            return view.isNestedScrollingEnabled();
        }
        return view instanceof NestedScrollingChild && ((NestedScrollingChild)view).isNestedScrollingEnabled();
    }
    
    @Deprecated
    public static boolean isOpaque(final View view) {
        return view.isOpaque();
    }
    
    public static boolean isPaddingRelative(@NonNull final View view) {
        return Build$VERSION.SDK_INT >= 17 && view.isPaddingRelative();
    }
    
    @UiThread
    public static boolean isScreenReaderFocusable(final View view) {
        final Boolean b = screenReaderFocusableProperty().get(view);
        return b != null && b;
    }
    
    @Deprecated
    public static void jumpDrawablesToCurrentState(final View view) {
        view.jumpDrawablesToCurrentState();
    }
    
    public static View keyboardNavigationClusterSearch(@NonNull final View view, final View view2, final int n) {
        if (Build$VERSION.SDK_INT >= 26) {
            return view.keyboardNavigationClusterSearch(view2, n);
        }
        return null;
    }
    
    @RequiresApi(19)
    static void notifyViewAccessibilityStateChangedIfNeeded(final View view, final int contentChangeTypes) {
        if (!((AccessibilityManager)view.getContext().getSystemService("accessibility")).isEnabled()) {
            return;
        }
        final boolean b = getAccessibilityPaneTitle(view) != null;
        if (getAccessibilityLiveRegion(view) == 0) {
            if (!b || view.getVisibility() != 0) {
                if (view.getParent() == null) {
                    return;
                }
                try {
                    view.getParent().notifySubtreeAccessibilityStateChanged(view, view, contentChangeTypes);
                    return;
                }
                catch (AbstractMethodError abstractMethodError) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(view.getParent().getClass().getSimpleName());
                    sb.append(" does not fully implement ViewParent");
                    Log.e("ViewCompat", sb.toString(), (Throwable)abstractMethodError);
                    return;
                }
            }
        }
        final AccessibilityEvent obtain = AccessibilityEvent.obtain();
        int eventType;
        if (b) {
            eventType = 32;
        }
        else {
            eventType = 2048;
        }
        obtain.setEventType(eventType);
        obtain.setContentChangeTypes(contentChangeTypes);
        view.sendAccessibilityEventUnchecked(obtain);
    }
    
    public static void offsetLeftAndRight(@NonNull final View view, final int n) {
        if (Build$VERSION.SDK_INT >= 23) {
            view.offsetLeftAndRight(n);
            return;
        }
        if (Build$VERSION.SDK_INT >= 21) {
            final Rect emptyTempRect = getEmptyTempRect();
            boolean b = false;
            final ViewParent parent = view.getParent();
            if (parent instanceof View) {
                final View view2 = (View)parent;
                emptyTempRect.set(view2.getLeft(), view2.getTop(), view2.getRight(), view2.getBottom());
                b = (emptyTempRect.intersects(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()) ^ true);
            }
            compatOffsetLeftAndRight(view, n);
            if (b && emptyTempRect.intersect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom())) {
                ((View)parent).invalidate(emptyTempRect);
            }
            return;
        }
        compatOffsetLeftAndRight(view, n);
    }
    
    public static void offsetTopAndBottom(@NonNull final View view, final int n) {
        if (Build$VERSION.SDK_INT >= 23) {
            view.offsetTopAndBottom(n);
            return;
        }
        if (Build$VERSION.SDK_INT >= 21) {
            final Rect emptyTempRect = getEmptyTempRect();
            boolean b = false;
            final ViewParent parent = view.getParent();
            if (parent instanceof View) {
                final View view2 = (View)parent;
                emptyTempRect.set(view2.getLeft(), view2.getTop(), view2.getRight(), view2.getBottom());
                b = (emptyTempRect.intersects(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()) ^ true);
            }
            compatOffsetTopAndBottom(view, n);
            if (b && emptyTempRect.intersect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom())) {
                ((View)parent).invalidate(emptyTempRect);
            }
            return;
        }
        compatOffsetTopAndBottom(view, n);
    }
    
    public static WindowInsetsCompat onApplyWindowInsets(@NonNull final View view, final WindowInsetsCompat windowInsetsCompat) {
        if (Build$VERSION.SDK_INT >= 21) {
            final WindowInsets windowInsets = (WindowInsets)WindowInsetsCompat.unwrap(windowInsetsCompat);
            final WindowInsets onApplyWindowInsets = view.onApplyWindowInsets(windowInsets);
            WindowInsets windowInsets2 = windowInsets;
            if (!onApplyWindowInsets.equals((Object)windowInsets)) {
                windowInsets2 = new WindowInsets(onApplyWindowInsets);
            }
            return WindowInsetsCompat.wrap(windowInsets2);
        }
        return windowInsetsCompat;
    }
    
    @Deprecated
    public static void onInitializeAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
        view.onInitializeAccessibilityEvent(accessibilityEvent);
    }
    
    public static void onInitializeAccessibilityNodeInfo(@NonNull final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        view.onInitializeAccessibilityNodeInfo(accessibilityNodeInfoCompat.unwrap());
    }
    
    @Deprecated
    public static void onPopulateAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
        view.onPopulateAccessibilityEvent(accessibilityEvent);
    }
    
    private static AccessibilityViewProperty<CharSequence> paneTitleProperty() {
        return (AccessibilityViewProperty<CharSequence>)new AccessibilityViewProperty<CharSequence>(R$id.tag_accessibility_pane_title, CharSequence.class, 8, 28) {
            @RequiresApi(28)
            CharSequence frameworkGet(final View view) {
                return view.getAccessibilityPaneTitle();
            }
            
            @RequiresApi(28)
            void frameworkSet(final View view, final CharSequence accessibilityPaneTitle) {
                view.setAccessibilityPaneTitle(accessibilityPaneTitle);
            }
            
            boolean shouldUpdate(final CharSequence charSequence, final CharSequence charSequence2) {
                return TextUtils.equals(charSequence, charSequence2) ^ true;
            }
        };
    }
    
    public static boolean performAccessibilityAction(@NonNull final View view, final int n, final Bundle bundle) {
        return Build$VERSION.SDK_INT >= 16 && view.performAccessibilityAction(n, bundle);
    }
    
    public static void postInvalidateOnAnimation(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 16) {
            view.postInvalidateOnAnimation();
            return;
        }
        view.postInvalidate();
    }
    
    public static void postInvalidateOnAnimation(@NonNull final View view, final int n, final int n2, final int n3, final int n4) {
        if (Build$VERSION.SDK_INT >= 16) {
            view.postInvalidateOnAnimation(n, n2, n3, n4);
            return;
        }
        view.postInvalidate(n, n2, n3, n4);
    }
    
    public static void postOnAnimation(@NonNull final View view, final Runnable runnable) {
        if (Build$VERSION.SDK_INT >= 16) {
            view.postOnAnimation(runnable);
            return;
        }
        view.postDelayed(runnable, ValueAnimator.getFrameDelay());
    }
    
    public static void postOnAnimationDelayed(@NonNull final View view, final Runnable runnable, final long n) {
        if (Build$VERSION.SDK_INT >= 16) {
            view.postOnAnimationDelayed(runnable, n);
            return;
        }
        view.postDelayed(runnable, ValueAnimator.getFrameDelay() + n);
    }
    
    public static void removeAccessibilityAction(@NonNull final View view, final int n) {
        if (Build$VERSION.SDK_INT >= 21) {
            removeActionWithId(n, view);
            notifyViewAccessibilityStateChangedIfNeeded(view, 0);
        }
    }
    
    private static void removeActionWithId(final int n, final View view) {
        final List<AccessibilityNodeInfoCompat.AccessibilityActionCompat> actionList = getActionList(view);
        for (int i = 0; i < actionList.size(); ++i) {
            if (actionList.get(i).getId() == n) {
                actionList.remove(i);
                return;
            }
        }
    }
    
    public static void removeOnUnhandledKeyEventListener(@NonNull final View view, @NonNull final OnUnhandledKeyEventListenerCompat onUnhandledKeyEventListenerCompat) {
        if (Build$VERSION.SDK_INT < 28) {
            final ArrayList list = (ArrayList)view.getTag(R$id.tag_unhandled_key_listeners);
            if (list != null) {
                list.remove(onUnhandledKeyEventListenerCompat);
                if (list.size() == 0) {
                    UnhandledKeyEventManager.unregisterListeningView(view);
                }
            }
            return;
        }
        final Map map = (Map)view.getTag(R$id.tag_unhandled_key_listeners);
        if (map == null) {
            return;
        }
        final View$OnUnhandledKeyEventListener view$OnUnhandledKeyEventListener = map.get(onUnhandledKeyEventListenerCompat);
        if (view$OnUnhandledKeyEventListener != null) {
            view.removeOnUnhandledKeyEventListener(view$OnUnhandledKeyEventListener);
        }
    }
    
    public static void replaceAccessibilityAction(@NonNull final View view, @NonNull final AccessibilityNodeInfoCompat.AccessibilityActionCompat accessibilityActionCompat, @Nullable final CharSequence charSequence, @Nullable final AccessibilityViewCommand accessibilityViewCommand) {
        addAccessibilityAction(view, accessibilityActionCompat.createReplacementAction(charSequence, accessibilityViewCommand));
    }
    
    public static void requestApplyInsets(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 20) {
            view.requestApplyInsets();
            return;
        }
        if (Build$VERSION.SDK_INT >= 16) {
            view.requestFitSystemWindows();
        }
    }
    
    @NonNull
    public static <T extends View> T requireViewById(@NonNull View viewById, @IdRes final int n) {
        if (Build$VERSION.SDK_INT >= 28) {
            return (T)viewById.requireViewById(n);
        }
        viewById = viewById.findViewById(n);
        if (viewById == null) {
            throw new IllegalArgumentException("ID does not reference a View inside this View");
        }
        return (T)viewById;
    }
    
    @Deprecated
    public static int resolveSizeAndState(final int n, final int n2, final int n3) {
        return View.resolveSizeAndState(n, n2, n3);
    }
    
    public static boolean restoreDefaultFocus(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 26) {
            return view.restoreDefaultFocus();
        }
        return view.requestFocus();
    }
    
    private static AccessibilityViewProperty<Boolean> screenReaderFocusableProperty() {
        return (AccessibilityViewProperty<Boolean>)new AccessibilityViewProperty<Boolean>(R$id.tag_screen_reader_focusable, Boolean.class, 28) {
            @RequiresApi(28)
            Boolean frameworkGet(final View view) {
                return view.isScreenReaderFocusable();
            }
            
            @RequiresApi(28)
            void frameworkSet(final View view, final Boolean b) {
                view.setScreenReaderFocusable((boolean)b);
            }
            
            boolean shouldUpdate(final Boolean b, final Boolean b2) {
                return ((AccessibilityViewProperty)this).booleanNullToFalseEquals(b, b2) ^ true;
            }
        };
    }
    
    public static void setAccessibilityDelegate(@NonNull final View view, final AccessibilityDelegateCompat accessibilityDelegateCompat) {
        AccessibilityDelegateCompat accessibilityDelegateCompat2 = accessibilityDelegateCompat;
        if (accessibilityDelegateCompat == null) {
            accessibilityDelegateCompat2 = accessibilityDelegateCompat;
            if (getAccessibilityDelegateInternal(view) instanceof AccessibilityDelegateCompat.AccessibilityDelegateAdapter) {
                accessibilityDelegateCompat2 = new AccessibilityDelegateCompat();
            }
        }
        View$AccessibilityDelegate bridge;
        if (accessibilityDelegateCompat2 == null) {
            bridge = null;
        }
        else {
            bridge = accessibilityDelegateCompat2.getBridge();
        }
        view.setAccessibilityDelegate(bridge);
    }
    
    @UiThread
    public static void setAccessibilityHeading(final View view, final boolean b) {
        accessibilityHeadingProperty().set(view, b);
    }
    
    public static void setAccessibilityLiveRegion(@NonNull final View view, final int accessibilityLiveRegion) {
        if (Build$VERSION.SDK_INT >= 19) {
            view.setAccessibilityLiveRegion(accessibilityLiveRegion);
        }
    }
    
    @UiThread
    public static void setAccessibilityPaneTitle(final View view, final CharSequence charSequence) {
        if (Build$VERSION.SDK_INT >= 19) {
            paneTitleProperty().set(view, charSequence);
            if (charSequence != null) {
                ViewCompat.sAccessibilityPaneVisibilityManager.addAccessibilityPane(view);
                return;
            }
            ViewCompat.sAccessibilityPaneVisibilityManager.removeAccessibilityPane(view);
        }
    }
    
    @Deprecated
    public static void setActivated(final View view, final boolean activated) {
        view.setActivated(activated);
    }
    
    @Deprecated
    public static void setAlpha(final View view, @FloatRange(from = 0.0, to = 1.0) final float alpha) {
        view.setAlpha(alpha);
    }
    
    public static void setAutofillHints(@NonNull final View view, @Nullable final String... autofillHints) {
        if (Build$VERSION.SDK_INT >= 26) {
            view.setAutofillHints(autofillHints);
        }
    }
    
    public static void setBackground(@NonNull final View view, @Nullable final Drawable drawable) {
        if (Build$VERSION.SDK_INT >= 16) {
            view.setBackground(drawable);
            return;
        }
        view.setBackgroundDrawable(drawable);
    }
    
    public static void setBackgroundTintList(@NonNull final View view, final ColorStateList list) {
        if (Build$VERSION.SDK_INT >= 21) {
            view.setBackgroundTintList(list);
            if (Build$VERSION.SDK_INT == 21) {
                final Drawable background = view.getBackground();
                final boolean b = view.getBackgroundTintList() != null || view.getBackgroundTintMode() != null;
                if (background != null && b) {
                    if (background.isStateful()) {
                        background.setState(view.getDrawableState());
                    }
                    view.setBackground(background);
                }
            }
        }
        else if (view instanceof TintableBackgroundView) {
            ((TintableBackgroundView)view).setSupportBackgroundTintList(list);
        }
    }
    
    public static void setBackgroundTintMode(@NonNull final View view, final PorterDuff$Mode porterDuff$Mode) {
        if (Build$VERSION.SDK_INT >= 21) {
            view.setBackgroundTintMode(porterDuff$Mode);
            if (Build$VERSION.SDK_INT == 21) {
                final Drawable background = view.getBackground();
                final boolean b = view.getBackgroundTintList() != null || view.getBackgroundTintMode() != null;
                if (background != null && b) {
                    if (background.isStateful()) {
                        background.setState(view.getDrawableState());
                    }
                    view.setBackground(background);
                }
            }
        }
        else if (view instanceof TintableBackgroundView) {
            ((TintableBackgroundView)view).setSupportBackgroundTintMode(porterDuff$Mode);
        }
    }
    
    @Deprecated
    public static void setChildrenDrawingOrderEnabled(final ViewGroup viewGroup, final boolean b) {
        if (ViewCompat.sChildrenDrawingOrderMethod == null) {
            try {
                ViewCompat.sChildrenDrawingOrderMethod = ViewGroup.class.getDeclaredMethod("setChildrenDrawingOrderEnabled", Boolean.TYPE);
            }
            catch (NoSuchMethodException ex) {
                Log.e("ViewCompat", "Unable to find childrenDrawingOrderEnabled", (Throwable)ex);
            }
            ViewCompat.sChildrenDrawingOrderMethod.setAccessible(true);
        }
        try {
            ViewCompat.sChildrenDrawingOrderMethod.invoke(viewGroup, b);
        }
        catch (InvocationTargetException ex2) {
            Log.e("ViewCompat", "Unable to invoke childrenDrawingOrderEnabled", (Throwable)ex2);
        }
        catch (IllegalArgumentException ex3) {
            Log.e("ViewCompat", "Unable to invoke childrenDrawingOrderEnabled", (Throwable)ex3);
        }
        catch (IllegalAccessException ex4) {
            Log.e("ViewCompat", "Unable to invoke childrenDrawingOrderEnabled", (Throwable)ex4);
        }
    }
    
    public static void setClipBounds(@NonNull final View view, final Rect clipBounds) {
        if (Build$VERSION.SDK_INT >= 18) {
            view.setClipBounds(clipBounds);
        }
    }
    
    public static void setElevation(@NonNull final View view, final float elevation) {
        if (Build$VERSION.SDK_INT >= 21) {
            view.setElevation(elevation);
        }
    }
    
    @Deprecated
    public static void setFitsSystemWindows(final View view, final boolean fitsSystemWindows) {
        view.setFitsSystemWindows(fitsSystemWindows);
    }
    
    public static void setFocusedByDefault(@NonNull final View view, final boolean focusedByDefault) {
        if (Build$VERSION.SDK_INT >= 26) {
            view.setFocusedByDefault(focusedByDefault);
        }
    }
    
    public static void setHasTransientState(@NonNull final View view, final boolean hasTransientState) {
        if (Build$VERSION.SDK_INT >= 16) {
            view.setHasTransientState(hasTransientState);
        }
    }
    
    public static void setImportantForAccessibility(@NonNull final View view, final int importantForAccessibility) {
        if (Build$VERSION.SDK_INT >= 19) {
            view.setImportantForAccessibility(importantForAccessibility);
            return;
        }
        if (Build$VERSION.SDK_INT >= 16) {
            int importantForAccessibility2;
            if ((importantForAccessibility2 = importantForAccessibility) == 4) {
                importantForAccessibility2 = 2;
            }
            view.setImportantForAccessibility(importantForAccessibility2);
        }
    }
    
    public static void setImportantForAutofill(@NonNull final View view, final int importantForAutofill) {
        if (Build$VERSION.SDK_INT >= 26) {
            view.setImportantForAutofill(importantForAutofill);
        }
    }
    
    public static void setKeyboardNavigationCluster(@NonNull final View view, final boolean keyboardNavigationCluster) {
        if (Build$VERSION.SDK_INT >= 26) {
            view.setKeyboardNavigationCluster(keyboardNavigationCluster);
        }
    }
    
    public static void setLabelFor(@NonNull final View view, @IdRes final int labelFor) {
        if (Build$VERSION.SDK_INT >= 17) {
            view.setLabelFor(labelFor);
        }
    }
    
    public static void setLayerPaint(@NonNull final View view, final Paint layerPaint) {
        if (Build$VERSION.SDK_INT >= 17) {
            view.setLayerPaint(layerPaint);
            return;
        }
        view.setLayerType(view.getLayerType(), layerPaint);
        view.invalidate();
    }
    
    @Deprecated
    public static void setLayerType(final View view, final int n, final Paint paint) {
        view.setLayerType(n, paint);
    }
    
    public static void setLayoutDirection(@NonNull final View view, final int layoutDirection) {
        if (Build$VERSION.SDK_INT >= 17) {
            view.setLayoutDirection(layoutDirection);
        }
    }
    
    public static void setNestedScrollingEnabled(@NonNull final View view, final boolean b) {
        if (Build$VERSION.SDK_INT >= 21) {
            view.setNestedScrollingEnabled(b);
            return;
        }
        if (view instanceof NestedScrollingChild) {
            ((NestedScrollingChild)view).setNestedScrollingEnabled(b);
        }
    }
    
    public static void setNextClusterForwardId(@NonNull final View view, final int nextClusterForwardId) {
        if (Build$VERSION.SDK_INT >= 26) {
            view.setNextClusterForwardId(nextClusterForwardId);
        }
    }
    
    public static void setOnApplyWindowInsetsListener(@NonNull final View view, final OnApplyWindowInsetsListener onApplyWindowInsetsListener) {
        if (Build$VERSION.SDK_INT >= 21) {
            if (onApplyWindowInsetsListener == null) {
                view.setOnApplyWindowInsetsListener((View$OnApplyWindowInsetsListener)null);
                return;
            }
            view.setOnApplyWindowInsetsListener((View$OnApplyWindowInsetsListener)new View$OnApplyWindowInsetsListener() {
                public WindowInsets onApplyWindowInsets(final View view, final WindowInsets windowInsets) {
                    return (WindowInsets)WindowInsetsCompat.unwrap(onApplyWindowInsetsListener.onApplyWindowInsets(view, WindowInsetsCompat.wrap(windowInsets)));
                }
            });
        }
    }
    
    @Deprecated
    public static void setOverScrollMode(final View view, final int overScrollMode) {
        view.setOverScrollMode(overScrollMode);
    }
    
    public static void setPaddingRelative(@NonNull final View view, @Px final int n, @Px final int n2, @Px final int n3, @Px final int n4) {
        if (Build$VERSION.SDK_INT >= 17) {
            view.setPaddingRelative(n, n2, n3, n4);
            return;
        }
        view.setPadding(n, n2, n3, n4);
    }
    
    @Deprecated
    public static void setPivotX(final View view, final float pivotX) {
        view.setPivotX(pivotX);
    }
    
    @Deprecated
    public static void setPivotY(final View view, final float pivotY) {
        view.setPivotY(pivotY);
    }
    
    public static void setPointerIcon(@NonNull final View view, final PointerIconCompat pointerIconCompat) {
        if (Build$VERSION.SDK_INT >= 24) {
            Object pointerIcon;
            if (pointerIconCompat != null) {
                pointerIcon = pointerIconCompat.getPointerIcon();
            }
            else {
                pointerIcon = null;
            }
            view.setPointerIcon((PointerIcon)pointerIcon);
        }
    }
    
    @Deprecated
    public static void setRotation(final View view, final float rotation) {
        view.setRotation(rotation);
    }
    
    @Deprecated
    public static void setRotationX(final View view, final float rotationX) {
        view.setRotationX(rotationX);
    }
    
    @Deprecated
    public static void setRotationY(final View view, final float rotationY) {
        view.setRotationY(rotationY);
    }
    
    @Deprecated
    public static void setSaveFromParentEnabled(final View view, final boolean saveFromParentEnabled) {
        view.setSaveFromParentEnabled(saveFromParentEnabled);
    }
    
    @Deprecated
    public static void setScaleX(final View view, final float scaleX) {
        view.setScaleX(scaleX);
    }
    
    @Deprecated
    public static void setScaleY(final View view, final float scaleY) {
        view.setScaleY(scaleY);
    }
    
    @UiThread
    public static void setScreenReaderFocusable(final View view, final boolean b) {
        screenReaderFocusableProperty().set(view, b);
    }
    
    public static void setScrollIndicators(@NonNull final View view, final int scrollIndicators) {
        if (Build$VERSION.SDK_INT >= 23) {
            view.setScrollIndicators(scrollIndicators);
        }
    }
    
    public static void setScrollIndicators(@NonNull final View view, final int n, final int n2) {
        if (Build$VERSION.SDK_INT >= 23) {
            view.setScrollIndicators(n, n2);
        }
    }
    
    public static void setTooltipText(@NonNull final View view, @Nullable final CharSequence tooltipText) {
        if (Build$VERSION.SDK_INT >= 26) {
            view.setTooltipText(tooltipText);
        }
    }
    
    public static void setTransitionName(@NonNull final View view, final String transitionName) {
        if (Build$VERSION.SDK_INT >= 21) {
            view.setTransitionName(transitionName);
            return;
        }
        if (ViewCompat.sTransitionNameMap == null) {
            ViewCompat.sTransitionNameMap = new WeakHashMap<View, String>();
        }
        ViewCompat.sTransitionNameMap.put(view, transitionName);
    }
    
    @Deprecated
    public static void setTranslationX(final View view, final float translationX) {
        view.setTranslationX(translationX);
    }
    
    @Deprecated
    public static void setTranslationY(final View view, final float translationY) {
        view.setTranslationY(translationY);
    }
    
    public static void setTranslationZ(@NonNull final View view, final float translationZ) {
        if (Build$VERSION.SDK_INT >= 21) {
            view.setTranslationZ(translationZ);
        }
    }
    
    @Deprecated
    public static void setX(final View view, final float x) {
        view.setX(x);
    }
    
    @Deprecated
    public static void setY(final View view, final float y) {
        view.setY(y);
    }
    
    public static void setZ(@NonNull final View view, final float z) {
        if (Build$VERSION.SDK_INT >= 21) {
            view.setZ(z);
        }
    }
    
    public static boolean startDragAndDrop(@NonNull final View view, final ClipData clipData, final View$DragShadowBuilder view$DragShadowBuilder, final Object o, final int n) {
        if (Build$VERSION.SDK_INT >= 24) {
            return view.startDragAndDrop(clipData, view$DragShadowBuilder, o, n);
        }
        return view.startDrag(clipData, view$DragShadowBuilder, o, n);
    }
    
    public static boolean startNestedScroll(@NonNull final View view, final int n) {
        if (Build$VERSION.SDK_INT >= 21) {
            return view.startNestedScroll(n);
        }
        return view instanceof NestedScrollingChild && ((NestedScrollingChild)view).startNestedScroll(n);
    }
    
    public static boolean startNestedScroll(@NonNull final View view, final int n, final int n2) {
        if (view instanceof NestedScrollingChild2) {
            return ((NestedScrollingChild2)view).startNestedScroll(n, n2);
        }
        return n2 == 0 && startNestedScroll(view, n);
    }
    
    public static void stopNestedScroll(@NonNull final View view) {
        if (Build$VERSION.SDK_INT >= 21) {
            view.stopNestedScroll();
            return;
        }
        if (view instanceof NestedScrollingChild) {
            ((NestedScrollingChild)view).stopNestedScroll();
        }
    }
    
    public static void stopNestedScroll(@NonNull final View view, final int n) {
        if (view instanceof NestedScrollingChild2) {
            ((NestedScrollingChild2)view).stopNestedScroll(n);
            return;
        }
        if (n == 0) {
            stopNestedScroll(view);
        }
    }
    
    private static void tickleInvalidationFlag(final View view) {
        final float translationY = view.getTranslationY();
        view.setTranslationY(1.0f + translationY);
        view.setTranslationY(translationY);
    }
    
    public static void updateDragShadow(@NonNull final View view, final View$DragShadowBuilder view$DragShadowBuilder) {
        if (Build$VERSION.SDK_INT >= 24) {
            view.updateDragShadow(view$DragShadowBuilder);
        }
    }
    
    static class AccessibilityPaneVisibilityManager implements ViewTreeObserver$OnGlobalLayoutListener, View$OnAttachStateChangeListener
    {
        private WeakHashMap<View, Boolean> mPanesToVisible;
        
        AccessibilityPaneVisibilityManager() {
            this.mPanesToVisible = new WeakHashMap<View, Boolean>();
        }
        
        @RequiresApi(19)
        private void checkPaneVisibility(final View view, final boolean b) {
            final boolean b2 = view.getVisibility() == 0;
            if (b != b2) {
                if (b2) {
                    ViewCompat.notifyViewAccessibilityStateChangedIfNeeded(view, 16);
                }
                this.mPanesToVisible.put(view, b2);
            }
        }
        
        @RequiresApi(19)
        private void registerForLayoutCallback(final View view) {
            view.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
        }
        
        @RequiresApi(19)
        private void unregisterForLayoutCallback(final View view) {
            view.getViewTreeObserver().removeOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
        }
        
        @RequiresApi(19)
        void addAccessibilityPane(final View view) {
            this.mPanesToVisible.put(view, view.getVisibility() == 0);
            view.addOnAttachStateChangeListener((View$OnAttachStateChangeListener)this);
            if (view.isAttachedToWindow()) {
                this.registerForLayoutCallback(view);
            }
        }
        
        @RequiresApi(19)
        public void onGlobalLayout() {
            for (final Map.Entry<View, Boolean> entry : this.mPanesToVisible.entrySet()) {
                this.checkPaneVisibility(entry.getKey(), entry.getValue());
            }
        }
        
        @RequiresApi(19)
        public void onViewAttachedToWindow(final View view) {
            this.registerForLayoutCallback(view);
        }
        
        public void onViewDetachedFromWindow(final View view) {
        }
        
        @RequiresApi(19)
        void removeAccessibilityPane(final View view) {
            this.mPanesToVisible.remove(view);
            view.removeOnAttachStateChangeListener((View$OnAttachStateChangeListener)this);
            this.unregisterForLayoutCallback(view);
        }
    }
    
    abstract static class AccessibilityViewProperty<T>
    {
        private final int mContentChangeType;
        private final int mFrameworkMinimumSdk;
        private final int mTagKey;
        private final Class<T> mType;
        
        AccessibilityViewProperty(final int n, final Class<T> clazz, final int n2) {
            this(n, clazz, 0, n2);
        }
        
        AccessibilityViewProperty(final int mTagKey, final Class<T> mType, final int mContentChangeType, final int mFrameworkMinimumSdk) {
            this.mTagKey = mTagKey;
            this.mType = mType;
            this.mContentChangeType = mContentChangeType;
            this.mFrameworkMinimumSdk = mFrameworkMinimumSdk;
        }
        
        private boolean extrasAvailable() {
            return Build$VERSION.SDK_INT >= 19;
        }
        
        private boolean frameworkAvailable() {
            return Build$VERSION.SDK_INT >= this.mFrameworkMinimumSdk;
        }
        
        boolean booleanNullToFalseEquals(final Boolean b, final Boolean b2) {
            boolean b3 = false;
            if ((b != null && b) == (b2 != null && b2)) {
                b3 = true;
            }
            return b3;
        }
        
        abstract T frameworkGet(final View p0);
        
        abstract void frameworkSet(final View p0, final T p1);
        
        T get(final View view) {
            if (this.frameworkAvailable()) {
                return this.frameworkGet(view);
            }
            if (this.extrasAvailable()) {
                final Object tag = view.getTag(this.mTagKey);
                if (this.mType.isInstance(tag)) {
                    return (T)tag;
                }
            }
            return null;
        }
        
        void set(final View view, final T t) {
            if (this.frameworkAvailable()) {
                this.frameworkSet(view, t);
                return;
            }
            if (this.extrasAvailable() && this.shouldUpdate(this.get(view), t)) {
                ViewCompat.getOrCreateAccessibilityDelegateCompat(view);
                view.setTag(this.mTagKey, (Object)t);
                ViewCompat.notifyViewAccessibilityStateChangedIfNeeded(view, 0);
            }
        }
        
        boolean shouldUpdate(final T t, final T t2) {
            return t2.equals(t) ^ true;
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public @interface FocusDirection {
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public @interface FocusRealDirection {
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public @interface FocusRelativeDirection {
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public @interface NestedScrollType {
    }
    
    public interface OnUnhandledKeyEventListenerCompat
    {
        boolean onUnhandledKeyEvent(final View p0, final KeyEvent p1);
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public @interface ScrollAxis {
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public @interface ScrollIndicators {
    }
    
    static class UnhandledKeyEventManager
    {
        private static final ArrayList<WeakReference<View>> sViewsWithListeners;
        private SparseArray<WeakReference<View>> mCapturedKeys;
        private WeakReference<KeyEvent> mLastDispatchedPreViewKeyEvent;
        @Nullable
        private WeakHashMap<View, Boolean> mViewsContainingListeners;
        
        static {
            sViewsWithListeners = new ArrayList<WeakReference<View>>();
        }
        
        UnhandledKeyEventManager() {
            this.mViewsContainingListeners = null;
            this.mCapturedKeys = null;
            this.mLastDispatchedPreViewKeyEvent = null;
        }
        
        static UnhandledKeyEventManager at(final View view) {
            UnhandledKeyEventManager unhandledKeyEventManager;
            if ((unhandledKeyEventManager = (UnhandledKeyEventManager)view.getTag(R$id.tag_unhandled_key_event_manager)) == null) {
                unhandledKeyEventManager = new UnhandledKeyEventManager();
                view.setTag(R$id.tag_unhandled_key_event_manager, (Object)unhandledKeyEventManager);
            }
            return unhandledKeyEventManager;
        }
        
        @Nullable
        private View dispatchInOrder(final View view, final KeyEvent keyEvent) {
            if (this.mViewsContainingListeners == null) {
                return null;
            }
            if (!this.mViewsContainingListeners.containsKey(view)) {
                return null;
            }
            if (view instanceof ViewGroup) {
                final ViewGroup viewGroup = (ViewGroup)view;
                for (int i = viewGroup.getChildCount() - 1; i >= 0; --i) {
                    final View dispatchInOrder = this.dispatchInOrder(viewGroup.getChildAt(i), keyEvent);
                    if (dispatchInOrder != null) {
                        return dispatchInOrder;
                    }
                }
            }
            if (this.onUnhandledKeyEvent(view, keyEvent)) {
                return view;
            }
            return null;
        }
        
        private SparseArray<WeakReference<View>> getCapturedKeys() {
            if (this.mCapturedKeys == null) {
                this.mCapturedKeys = (SparseArray<WeakReference<View>>)new SparseArray();
            }
            return this.mCapturedKeys;
        }
        
        private boolean onUnhandledKeyEvent(@NonNull final View view, @NonNull final KeyEvent keyEvent) {
            final ArrayList list = (ArrayList)view.getTag(R$id.tag_unhandled_key_listeners);
            if (list != null) {
                for (int i = list.size() - 1; i >= 0; --i) {
                    if (list.get(i).onUnhandledKeyEvent(view, keyEvent)) {
                        return true;
                    }
                }
            }
            return false;
        }
        
        private void recalcViewsWithUnhandled() {
            if (this.mViewsContainingListeners != null) {
                this.mViewsContainingListeners.clear();
            }
            if (UnhandledKeyEventManager.sViewsWithListeners.isEmpty()) {
                return;
            }
            while (true) {
                while (true) {
                    int n;
                    synchronized (UnhandledKeyEventManager.sViewsWithListeners) {
                        if (this.mViewsContainingListeners == null) {
                            this.mViewsContainingListeners = new WeakHashMap<View, Boolean>();
                        }
                        n = UnhandledKeyEventManager.sViewsWithListeners.size() - 1;
                        if (n < 0) {
                            return;
                        }
                        final View view = UnhandledKeyEventManager.sViewsWithListeners.get(n).get();
                        if (view == null) {
                            UnhandledKeyEventManager.sViewsWithListeners.remove(n);
                        }
                        else {
                            this.mViewsContainingListeners.put(view, Boolean.TRUE);
                            for (ViewParent viewParent = view.getParent(); viewParent instanceof View; viewParent = viewParent.getParent()) {
                                this.mViewsContainingListeners.put((View)viewParent, Boolean.TRUE);
                            }
                        }
                    }
                    --n;
                    continue;
                }
            }
        }
        
        static void registerListeningView(final View view) {
            while (true) {
                while (true) {
                    Label_0064: {
                        synchronized (UnhandledKeyEventManager.sViewsWithListeners) {
                            final Iterator<WeakReference<View>> iterator = UnhandledKeyEventManager.sViewsWithListeners.iterator();
                            if (!iterator.hasNext()) {
                                UnhandledKeyEventManager.sViewsWithListeners.add(new WeakReference<View>(view));
                                return;
                            }
                            if (iterator.next().get() == view) {
                                return;
                            }
                            break Label_0064;
                        }
                    }
                    continue;
                }
            }
        }
        
        static void unregisterListeningView(final View view) {
            final ArrayList<WeakReference<View>> sViewsWithListeners = UnhandledKeyEventManager.sViewsWithListeners;
            // monitorenter(sViewsWithListeners)
            int n = 0;
            while (true) {
                try {
                    if (n >= UnhandledKeyEventManager.sViewsWithListeners.size()) {
                        return;
                    }
                    if (UnhandledKeyEventManager.sViewsWithListeners.get(n).get() == view) {
                        UnhandledKeyEventManager.sViewsWithListeners.remove(n);
                        return;
                    }
                }
                finally {
                }
                // monitorexit(sViewsWithListeners)
                ++n;
            }
        }
        
        boolean dispatch(View dispatchInOrder, final KeyEvent keyEvent) {
            if (keyEvent.getAction() == 0) {
                this.recalcViewsWithUnhandled();
            }
            dispatchInOrder = this.dispatchInOrder(dispatchInOrder, keyEvent);
            if (keyEvent.getAction() == 0) {
                final int keyCode = keyEvent.getKeyCode();
                if (dispatchInOrder != null && !KeyEvent.isModifierKey(keyCode)) {
                    this.getCapturedKeys().put(keyCode, (Object)new WeakReference(dispatchInOrder));
                }
            }
            return dispatchInOrder != null;
        }
        
        boolean preDispatch(final KeyEvent keyEvent) {
            if (this.mLastDispatchedPreViewKeyEvent != null && this.mLastDispatchedPreViewKeyEvent.get() == keyEvent) {
                return false;
            }
            this.mLastDispatchedPreViewKeyEvent = new WeakReference<KeyEvent>(keyEvent);
            final WeakReference<View> weakReference = null;
            final SparseArray<WeakReference<View>> capturedKeys = this.getCapturedKeys();
            WeakReference<View> weakReference2 = weakReference;
            if (keyEvent.getAction() == 1) {
                final int indexOfKey = capturedKeys.indexOfKey(keyEvent.getKeyCode());
                weakReference2 = weakReference;
                if (indexOfKey >= 0) {
                    weakReference2 = (WeakReference<View>)capturedKeys.valueAt(indexOfKey);
                    capturedKeys.removeAt(indexOfKey);
                }
            }
            WeakReference<View> weakReference3;
            if ((weakReference3 = weakReference2) == null) {
                weakReference3 = (WeakReference<View>)capturedKeys.get(keyEvent.getKeyCode());
            }
            if (weakReference3 != null) {
                final View view = weakReference3.get();
                if (view != null && ViewCompat.isAttachedToWindow(view)) {
                    this.onUnhandledKeyEvent(view, keyEvent);
                }
                return true;
            }
            return false;
        }
    }
}
