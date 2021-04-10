package android.support.v4.widget;

import android.content.*;
import android.content.res.*;
import java.util.*;
import android.graphics.*;
import android.util.*;
import android.support.v4.view.*;
import android.view.animation.*;
import android.view.accessibility.*;
import android.widget.*;
import android.support.v4.view.accessibility.*;
import android.view.*;
import android.os.*;

public class NestedScrollView extends FrameLayout implements NestedScrollingParent, NestedScrollingChild, ScrollingView
{
    private static final AccessibilityDelegate ACCESSIBILITY_DELEGATE;
    static final int ANIMATED_SCROLL_GAP = 250;
    private static final int INVALID_POINTER = -1;
    static final float MAX_SCROLL_FACTOR = 0.5f;
    private static final int[] SCROLLVIEW_STYLEABLE;
    private static final String TAG = "NestedScrollView";
    private int mActivePointerId;
    private final NestedScrollingChildHelper mChildHelper;
    private View mChildToScrollTo;
    private EdgeEffectCompat mEdgeGlowBottom;
    private EdgeEffectCompat mEdgeGlowTop;
    private boolean mFillViewport;
    private boolean mIsBeingDragged;
    private boolean mIsLaidOut;
    private boolean mIsLayoutDirty;
    private int mLastMotionY;
    private long mLastScroll;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private int mNestedYOffset;
    private OnScrollChangeListener mOnScrollChangeListener;
    private final NestedScrollingParentHelper mParentHelper;
    private SavedState mSavedState;
    private final int[] mScrollConsumed;
    private final int[] mScrollOffset;
    private ScrollerCompat mScroller;
    private boolean mSmoothScrollingEnabled;
    private final Rect mTempRect;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;
    private float mVerticalScrollFactor;
    
    static {
        ACCESSIBILITY_DELEGATE = new AccessibilityDelegate();
        SCROLLVIEW_STYLEABLE = new int[] { 16843130 };
    }
    
    public NestedScrollView(final Context context) {
        this(context, null);
    }
    
    public NestedScrollView(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public NestedScrollView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mTempRect = new Rect();
        this.mIsLayoutDirty = true;
        this.mIsLaidOut = false;
        this.mChildToScrollTo = null;
        this.mIsBeingDragged = false;
        this.mSmoothScrollingEnabled = true;
        this.mActivePointerId = -1;
        this.mScrollOffset = new int[2];
        this.mScrollConsumed = new int[2];
        this.initScrollView();
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, NestedScrollView.SCROLLVIEW_STYLEABLE, n, 0);
        this.setFillViewport(obtainStyledAttributes.getBoolean(0, false));
        obtainStyledAttributes.recycle();
        this.mParentHelper = new NestedScrollingParentHelper((ViewGroup)this);
        this.mChildHelper = new NestedScrollingChildHelper((View)this);
        this.setNestedScrollingEnabled(true);
        ViewCompat.setAccessibilityDelegate((View)this, NestedScrollView.ACCESSIBILITY_DELEGATE);
    }
    
    private boolean canScroll() {
        final boolean b = false;
        final View child = this.getChildAt(0);
        boolean b2 = b;
        if (child != null) {
            final int height = child.getHeight();
            b2 = b;
            if (this.getHeight() < this.getPaddingTop() + height + this.getPaddingBottom()) {
                b2 = true;
            }
        }
        return b2;
    }
    
    private static int clamp(final int n, final int n2, final int n3) {
        int n4;
        if (n2 < n3 && n >= 0) {
            n4 = n;
            if (n2 + n > n3) {
                return n3 - n2;
            }
        }
        else {
            n4 = 0;
        }
        return n4;
    }
    
    private void doScrollY(final int n) {
        if (n != 0) {
            if (this.mSmoothScrollingEnabled) {
                this.smoothScrollBy(0, n);
                return;
            }
            this.scrollBy(0, n);
        }
    }
    
    private void endDrag() {
        this.mIsBeingDragged = false;
        this.recycleVelocityTracker();
        this.stopNestedScroll();
        if (this.mEdgeGlowTop != null) {
            this.mEdgeGlowTop.onRelease();
            this.mEdgeGlowBottom.onRelease();
        }
    }
    
    private void ensureGlows() {
        if (ViewCompat.getOverScrollMode((View)this) != 2) {
            if (this.mEdgeGlowTop == null) {
                final Context context = this.getContext();
                this.mEdgeGlowTop = new EdgeEffectCompat(context);
                this.mEdgeGlowBottom = new EdgeEffectCompat(context);
            }
            return;
        }
        this.mEdgeGlowTop = null;
        this.mEdgeGlowBottom = null;
    }
    
    private View findFocusableViewInBounds(final boolean b, final int n, final int n2) {
        final ArrayList focusables = this.getFocusables(2);
        final int size = focusables.size();
        View view = null;
        int i = 0;
        int n3 = 0;
        while (i < size) {
            final View view2 = focusables.get(i);
            final int top = view2.getTop();
            final int bottom = view2.getBottom();
            View view3 = view;
            int n4 = n3;
            Label_0232: {
                if (n < bottom) {
                    view3 = view;
                    n4 = n3;
                    if (top < n2) {
                        final boolean b2 = n < top && bottom < n2;
                        if (view == null) {
                            view3 = view2;
                            n4 = (b2 ? 1 : 0);
                        }
                        else {
                            final boolean b3 = (b && top < view.getTop()) || (!b && bottom > view.getBottom());
                            if (n3 != 0) {
                                view3 = view;
                                n4 = n3;
                                if (!b2) {
                                    break Label_0232;
                                }
                                view3 = view;
                                n4 = n3;
                                if (!b3) {
                                    break Label_0232;
                                }
                            }
                            else {
                                if (b2) {
                                    view3 = view2;
                                    n4 = 1;
                                    break Label_0232;
                                }
                                view3 = view;
                                n4 = n3;
                                if (!b3) {
                                    break Label_0232;
                                }
                            }
                            view3 = view2;
                            n4 = n3;
                        }
                    }
                }
            }
            ++i;
            view = view3;
            n3 = n4;
        }
        return view;
    }
    
    private void flingWithNestedDispatch(final int n) {
        final int scrollY = this.getScrollY();
        final boolean b = (scrollY > 0 || n > 0) && (scrollY < this.getScrollRange() || n < 0);
        final float n2 = (float)n;
        if (!this.dispatchNestedPreFling(0.0f, n2)) {
            this.dispatchNestedFling(0.0f, n2, b);
            if (b) {
                this.fling(n);
            }
        }
    }
    
    private int getScrollRange() {
        final int childCount = this.getChildCount();
        int max = 0;
        if (childCount > 0) {
            max = Math.max(0, this.getChildAt(0).getHeight() - (this.getHeight() - this.getPaddingBottom() - this.getPaddingTop()));
        }
        return max;
    }
    
    private float getVerticalScrollFactorCompat() {
        if (this.mVerticalScrollFactor == 0.0f) {
            final TypedValue typedValue = new TypedValue();
            final Context context = this.getContext();
            if (!context.getTheme().resolveAttribute(16842829, typedValue, true)) {
                throw new IllegalStateException("Expected theme to define listPreferredItemHeight.");
            }
            this.mVerticalScrollFactor = typedValue.getDimension(context.getResources().getDisplayMetrics());
        }
        return this.mVerticalScrollFactor;
    }
    
    private boolean inChild(final int n, final int n2) {
        final int childCount = this.getChildCount();
        boolean b2;
        final boolean b = b2 = false;
        if (childCount > 0) {
            final int scrollY = this.getScrollY();
            final View child = this.getChildAt(0);
            b2 = b;
            if (n2 >= child.getTop() - scrollY) {
                b2 = b;
                if (n2 < child.getBottom() - scrollY) {
                    b2 = b;
                    if (n >= child.getLeft()) {
                        b2 = b;
                        if (n < child.getRight()) {
                            b2 = true;
                        }
                    }
                }
            }
        }
        return b2;
    }
    
    private void initOrResetVelocityTracker() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
            return;
        }
        this.mVelocityTracker.clear();
    }
    
    private void initScrollView() {
        this.mScroller = ScrollerCompat.create(this.getContext(), null);
        this.setFocusable(true);
        this.setDescendantFocusability(262144);
        this.setWillNotDraw(false);
        final ViewConfiguration value = ViewConfiguration.get(this.getContext());
        this.mTouchSlop = value.getScaledTouchSlop();
        this.mMinimumVelocity = value.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = value.getScaledMaximumFlingVelocity();
    }
    
    private void initVelocityTrackerIfNotExists() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
    }
    
    private boolean isOffScreen(final View view) {
        return this.isWithinDeltaOfScreen(view, 0, this.getHeight()) ^ true;
    }
    
    private static boolean isViewDescendantOf(final View view, final View view2) {
        if (view != view2) {
            final ViewParent parent = view.getParent();
            if (!(parent instanceof ViewGroup) || !isViewDescendantOf((View)parent, view2)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isWithinDeltaOfScreen(final View view, final int n, final int n2) {
        view.getDrawingRect(this.mTempRect);
        this.offsetDescendantRectToMyCoords(view, this.mTempRect);
        return this.mTempRect.bottom + n >= this.getScrollY() && this.mTempRect.top - n <= this.getScrollY() + n2;
    }
    
    private void onSecondaryPointerUp(final MotionEvent motionEvent) {
        final int n = (motionEvent.getAction() & 0xFF00) >> 8;
        if (MotionEventCompat.getPointerId(motionEvent, n) == this.mActivePointerId) {
            int n2;
            if (n == 0) {
                n2 = 1;
            }
            else {
                n2 = 0;
            }
            this.mLastMotionY = (int)MotionEventCompat.getY(motionEvent, n2);
            this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, n2);
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.clear();
            }
        }
    }
    
    private void recycleVelocityTracker() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }
    
    private boolean scrollAndFocus(final int n, int n2, final int n3) {
        final int height = this.getHeight();
        final int scrollY = this.getScrollY();
        final int n4 = height + scrollY;
        final boolean b = false;
        final boolean b2 = n == 33;
        Object focusableViewInBounds;
        if ((focusableViewInBounds = this.findFocusableViewInBounds(b2, n2, n3)) == null) {
            focusableViewInBounds = this;
        }
        boolean b3;
        if (n2 >= scrollY && n3 <= n4) {
            b3 = b;
        }
        else {
            if (b2) {
                n2 -= scrollY;
            }
            else {
                n2 = n3 - n4;
            }
            this.doScrollY(n2);
            b3 = true;
        }
        if (focusableViewInBounds != this.findFocus()) {
            ((View)focusableViewInBounds).requestFocus(n);
        }
        return b3;
    }
    
    private void scrollToChild(final View view) {
        view.getDrawingRect(this.mTempRect);
        this.offsetDescendantRectToMyCoords(view, this.mTempRect);
        final int computeScrollDeltaToGetChildRectOnScreen = this.computeScrollDeltaToGetChildRectOnScreen(this.mTempRect);
        if (computeScrollDeltaToGetChildRectOnScreen != 0) {
            this.scrollBy(0, computeScrollDeltaToGetChildRectOnScreen);
        }
    }
    
    private boolean scrollToChildRect(final Rect rect, final boolean b) {
        final int computeScrollDeltaToGetChildRectOnScreen = this.computeScrollDeltaToGetChildRectOnScreen(rect);
        final boolean b2 = computeScrollDeltaToGetChildRectOnScreen != 0;
        if (b2) {
            if (b) {
                this.scrollBy(0, computeScrollDeltaToGetChildRectOnScreen);
                return b2;
            }
            this.smoothScrollBy(0, computeScrollDeltaToGetChildRectOnScreen);
        }
        return b2;
    }
    
    public void addView(final View view) {
        if (this.getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }
        super.addView(view);
    }
    
    public void addView(final View view, final int n) {
        if (this.getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }
        super.addView(view, n);
    }
    
    public void addView(final View view, final int n, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        if (this.getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }
        super.addView(view, n, viewGroup$LayoutParams);
    }
    
    public void addView(final View view, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        if (this.getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }
        super.addView(view, viewGroup$LayoutParams);
    }
    
    public boolean arrowScroll(int descendantFocusability) {
        View focus;
        if ((focus = this.findFocus()) == this) {
            focus = null;
        }
        final View nextFocus = FocusFinder.getInstance().findNextFocus((ViewGroup)this, focus, descendantFocusability);
        final int maxScrollAmount = this.getMaxScrollAmount();
        boolean b = false;
        if (nextFocus != null && this.isWithinDeltaOfScreen(nextFocus, maxScrollAmount, this.getHeight())) {
            nextFocus.getDrawingRect(this.mTempRect);
            this.offsetDescendantRectToMyCoords(nextFocus, this.mTempRect);
            this.doScrollY(this.computeScrollDeltaToGetChildRectOnScreen(this.mTempRect));
            nextFocus.requestFocus(descendantFocusability);
        }
        else {
            int scrollY;
            if (descendantFocusability == 33 && this.getScrollY() < maxScrollAmount) {
                scrollY = this.getScrollY();
            }
            else {
                scrollY = maxScrollAmount;
                if (descendantFocusability == 130) {
                    scrollY = maxScrollAmount;
                    if (this.getChildCount() > 0) {
                        final int n = this.getChildAt(0).getBottom() - (this.getScrollY() + this.getHeight() - this.getPaddingBottom());
                        if (n < (scrollY = maxScrollAmount)) {
                            scrollY = n;
                        }
                    }
                }
            }
            if (scrollY == 0) {
                return b;
            }
            if (descendantFocusability != 130) {
                scrollY = -scrollY;
            }
            this.doScrollY(scrollY);
        }
        if (focus != null && focus.isFocused() && this.isOffScreen(focus)) {
            descendantFocusability = this.getDescendantFocusability();
            this.setDescendantFocusability(131072);
            this.requestFocus();
            this.setDescendantFocusability(descendantFocusability);
        }
        b = true;
        return b;
    }
    
    public int computeHorizontalScrollExtent() {
        return super.computeHorizontalScrollExtent();
    }
    
    public int computeHorizontalScrollOffset() {
        return super.computeHorizontalScrollOffset();
    }
    
    public int computeHorizontalScrollRange() {
        return super.computeHorizontalScrollRange();
    }
    
    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            final int scrollX = this.getScrollX();
            final int scrollY = this.getScrollY();
            final int currX = this.mScroller.getCurrX();
            final int currY = this.mScroller.getCurrY();
            if (scrollX != currX || scrollY != currY) {
                final int scrollRange = this.getScrollRange();
                final int overScrollMode = ViewCompat.getOverScrollMode((View)this);
                final boolean b = overScrollMode == 0 || (overScrollMode == 1 && scrollRange > 0);
                this.overScrollByCompat(currX - scrollX, currY - scrollY, scrollX, scrollY, 0, scrollRange, 0, 0, false);
                if (b) {
                    this.ensureGlows();
                    if (currY <= 0 && scrollY > 0) {
                        this.mEdgeGlowTop.onAbsorb((int)this.mScroller.getCurrVelocity());
                        return;
                    }
                    if (currY >= scrollRange && scrollY < scrollRange) {
                        this.mEdgeGlowBottom.onAbsorb((int)this.mScroller.getCurrVelocity());
                    }
                }
            }
        }
    }
    
    protected int computeScrollDeltaToGetChildRectOnScreen(final Rect rect) {
        if (this.getChildCount() == 0) {
            return 0;
        }
        final int height = this.getHeight();
        final int scrollY = this.getScrollY();
        final int n = scrollY + height;
        final int verticalFadingEdgeLength = this.getVerticalFadingEdgeLength();
        int n2 = scrollY;
        if (rect.top > 0) {
            n2 = scrollY + verticalFadingEdgeLength;
        }
        int n3 = n;
        if (rect.bottom < this.getChildAt(0).getHeight()) {
            n3 = n - verticalFadingEdgeLength;
        }
        if (rect.bottom > n3 && rect.top > n2) {
            int n4;
            if (rect.height() > height) {
                n4 = rect.top - n2;
            }
            else {
                n4 = rect.bottom - n3;
            }
            return Math.min(n4 + 0, this.getChildAt(0).getBottom() - n3);
        }
        if (rect.top < n2 && rect.bottom < n3) {
            int n5;
            if (rect.height() > height) {
                n5 = 0 - (n3 - rect.bottom);
            }
            else {
                n5 = 0 - (n2 - rect.top);
            }
            return Math.max(n5, -this.getScrollY());
        }
        return 0;
    }
    
    public int computeVerticalScrollExtent() {
        return super.computeVerticalScrollExtent();
    }
    
    public int computeVerticalScrollOffset() {
        return Math.max(0, super.computeVerticalScrollOffset());
    }
    
    public int computeVerticalScrollRange() {
        final int childCount = this.getChildCount();
        final int n = this.getHeight() - this.getPaddingBottom() - this.getPaddingTop();
        if (childCount == 0) {
            return n;
        }
        final int bottom = this.getChildAt(0).getBottom();
        final int scrollY = this.getScrollY();
        final int max = Math.max(0, bottom - n);
        if (scrollY < 0) {
            return bottom - scrollY;
        }
        int n2 = bottom;
        if (scrollY > max) {
            n2 = bottom + (scrollY - max);
        }
        return n2;
    }
    
    public boolean dispatchKeyEvent(final KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent) || this.executeKeyEvent(keyEvent);
    }
    
    public boolean dispatchNestedFling(final float n, final float n2, final boolean b) {
        return this.mChildHelper.dispatchNestedFling(n, n2, b);
    }
    
    public boolean dispatchNestedPreFling(final float n, final float n2) {
        return this.mChildHelper.dispatchNestedPreFling(n, n2);
    }
    
    public boolean dispatchNestedPreScroll(final int n, final int n2, final int[] array, final int[] array2) {
        return this.mChildHelper.dispatchNestedPreScroll(n, n2, array, array2);
    }
    
    public boolean dispatchNestedScroll(final int n, final int n2, final int n3, final int n4, final int[] array) {
        return this.mChildHelper.dispatchNestedScroll(n, n2, n3, n4, array);
    }
    
    public void draw(final Canvas canvas) {
        super.draw(canvas);
        if (this.mEdgeGlowTop != null) {
            final int scrollY = this.getScrollY();
            if (!this.mEdgeGlowTop.isFinished()) {
                final int save = canvas.save();
                final int width = this.getWidth();
                final int paddingLeft = this.getPaddingLeft();
                final int paddingRight = this.getPaddingRight();
                canvas.translate((float)this.getPaddingLeft(), (float)Math.min(0, scrollY));
                this.mEdgeGlowTop.setSize(width - paddingLeft - paddingRight, this.getHeight());
                if (this.mEdgeGlowTop.draw(canvas)) {
                    ViewCompat.postInvalidateOnAnimation((View)this);
                }
                canvas.restoreToCount(save);
            }
            if (!this.mEdgeGlowBottom.isFinished()) {
                final int save2 = canvas.save();
                final int n = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
                final int height = this.getHeight();
                canvas.translate((float)(-n + this.getPaddingLeft()), (float)(Math.max(this.getScrollRange(), scrollY) + height));
                canvas.rotate(180.0f, (float)n, 0.0f);
                this.mEdgeGlowBottom.setSize(n, height);
                if (this.mEdgeGlowBottom.draw(canvas)) {
                    ViewCompat.postInvalidateOnAnimation((View)this);
                }
                canvas.restoreToCount(save2);
            }
        }
    }
    
    public boolean executeKeyEvent(final KeyEvent keyEvent) {
        this.mTempRect.setEmpty();
        final boolean canScroll = this.canScroll();
        final boolean b = false;
        int n = 130;
        if (!canScroll) {
            boolean b2 = b;
            if (this.isFocused()) {
                b2 = b;
                if (keyEvent.getKeyCode() != 4) {
                    View focus;
                    if ((focus = this.findFocus()) == this) {
                        focus = null;
                    }
                    final View nextFocus = FocusFinder.getInstance().findNextFocus((ViewGroup)this, focus, 130);
                    b2 = b;
                    if (nextFocus != null) {
                        b2 = b;
                        if (nextFocus != this) {
                            b2 = b;
                            if (nextFocus.requestFocus(130)) {
                                b2 = true;
                            }
                        }
                    }
                }
            }
            return b2;
        }
        if (keyEvent.getAction() == 0) {
            final int keyCode = keyEvent.getKeyCode();
            if (keyCode != 62) {
                switch (keyCode) {
                    default: {
                        return false;
                    }
                    case 20: {
                        if (!keyEvent.isAltPressed()) {
                            return this.arrowScroll(130);
                        }
                        return this.fullScroll(130);
                    }
                    case 19: {
                        if (!keyEvent.isAltPressed()) {
                            return this.arrowScroll(33);
                        }
                        return this.fullScroll(33);
                    }
                }
            }
            else {
                if (keyEvent.isShiftPressed()) {
                    n = 33;
                }
                this.pageScroll(n);
            }
        }
        return false;
    }
    
    public void fling(final int n) {
        if (this.getChildCount() > 0) {
            final int n2 = this.getHeight() - this.getPaddingBottom() - this.getPaddingTop();
            this.mScroller.fling(this.getScrollX(), this.getScrollY(), 0, n, 0, 0, 0, Math.max(0, this.getChildAt(0).getHeight() - n2), 0, n2 / 2);
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }
    
    public boolean fullScroll(final int n) {
        final boolean b = n == 130;
        final int height = this.getHeight();
        this.mTempRect.top = 0;
        this.mTempRect.bottom = height;
        if (b) {
            final int childCount = this.getChildCount();
            if (childCount > 0) {
                this.mTempRect.bottom = this.getChildAt(childCount - 1).getBottom() + this.getPaddingBottom();
                this.mTempRect.top = this.mTempRect.bottom - height;
            }
        }
        return this.scrollAndFocus(n, this.mTempRect.top, this.mTempRect.bottom);
    }
    
    protected float getBottomFadingEdgeStrength() {
        if (this.getChildCount() == 0) {
            return 0.0f;
        }
        final int verticalFadingEdgeLength = this.getVerticalFadingEdgeLength();
        final int n = this.getChildAt(0).getBottom() - this.getScrollY() - (this.getHeight() - this.getPaddingBottom());
        if (n < verticalFadingEdgeLength) {
            return n / (float)verticalFadingEdgeLength;
        }
        return 1.0f;
    }
    
    public int getMaxScrollAmount() {
        return (int)(this.getHeight() * 0.5f);
    }
    
    public int getNestedScrollAxes() {
        return this.mParentHelper.getNestedScrollAxes();
    }
    
    protected float getTopFadingEdgeStrength() {
        if (this.getChildCount() == 0) {
            return 0.0f;
        }
        final int verticalFadingEdgeLength = this.getVerticalFadingEdgeLength();
        final int scrollY = this.getScrollY();
        if (scrollY < verticalFadingEdgeLength) {
            return scrollY / (float)verticalFadingEdgeLength;
        }
        return 1.0f;
    }
    
    public boolean hasNestedScrollingParent() {
        return this.mChildHelper.hasNestedScrollingParent();
    }
    
    public boolean isFillViewport() {
        return this.mFillViewport;
    }
    
    public boolean isNestedScrollingEnabled() {
        return this.mChildHelper.isNestedScrollingEnabled();
    }
    
    public boolean isSmoothScrollingEnabled() {
        return this.mSmoothScrollingEnabled;
    }
    
    protected void measureChild(final View view, final int n, final int n2) {
        view.measure(getChildMeasureSpec(n, this.getPaddingLeft() + this.getPaddingRight(), view.getLayoutParams().width), View$MeasureSpec.makeMeasureSpec(0, 0));
    }
    
    protected void measureChildWithMargins(final View view, final int n, final int n2, final int n3, final int n4) {
        final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams = (ViewGroup$MarginLayoutParams)view.getLayoutParams();
        view.measure(getChildMeasureSpec(n, this.getPaddingLeft() + this.getPaddingRight() + viewGroup$MarginLayoutParams.leftMargin + viewGroup$MarginLayoutParams.rightMargin + n2, viewGroup$MarginLayoutParams.width), View$MeasureSpec.makeMeasureSpec(viewGroup$MarginLayoutParams.topMargin + viewGroup$MarginLayoutParams.bottomMargin, 0));
    }
    
    public void onAttachedToWindow() {
        this.mIsLaidOut = false;
    }
    
    public boolean onGenericMotionEvent(final MotionEvent motionEvent) {
        if ((MotionEventCompat.getSource(motionEvent) & 0x2) != 0x0) {
            if (motionEvent.getAction() != 8) {
                return false;
            }
            if (!this.mIsBeingDragged) {
                final float axisValue = MotionEventCompat.getAxisValue(motionEvent, 9);
                if (axisValue != 0.0f) {
                    final int n = (int)(this.getVerticalScrollFactorCompat() * axisValue);
                    final int scrollRange = this.getScrollRange();
                    final int scrollY = this.getScrollY();
                    final int n2 = scrollY - n;
                    int n3;
                    if (n2 < 0) {
                        n3 = 0;
                    }
                    else if ((n3 = n2) > scrollRange) {
                        n3 = scrollRange;
                    }
                    if (n3 != scrollY) {
                        super.scrollTo(this.getScrollX(), n3);
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        final int action = motionEvent.getAction();
        if (action == 2 && this.mIsBeingDragged) {
            return true;
        }
        final int n = action & 0xFF;
        if (n != 6) {
            switch (n) {
                case 2: {
                    final int mActivePointerId = this.mActivePointerId;
                    if (mActivePointerId == -1) {
                        break;
                    }
                    final int pointerIndex = MotionEventCompat.findPointerIndex(motionEvent, mActivePointerId);
                    if (pointerIndex == -1) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Invalid pointerId=");
                        sb.append(mActivePointerId);
                        sb.append(" in onInterceptTouchEvent");
                        Log.e("NestedScrollView", sb.toString());
                        break;
                    }
                    final int mLastMotionY = (int)MotionEventCompat.getY(motionEvent, pointerIndex);
                    if (Math.abs(mLastMotionY - this.mLastMotionY) <= this.mTouchSlop || (0x2 & this.getNestedScrollAxes()) != 0x0) {
                        break;
                    }
                    this.mIsBeingDragged = true;
                    this.mLastMotionY = mLastMotionY;
                    this.initVelocityTrackerIfNotExists();
                    this.mVelocityTracker.addMovement(motionEvent);
                    this.mNestedYOffset = 0;
                    final ViewParent parent = this.getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                        break;
                    }
                    break;
                }
                case 1:
                case 3: {
                    this.mIsBeingDragged = false;
                    this.mActivePointerId = -1;
                    this.recycleVelocityTracker();
                    if (this.mScroller.springBack(this.getScrollX(), this.getScrollY(), 0, 0, 0, this.getScrollRange())) {
                        ViewCompat.postInvalidateOnAnimation((View)this);
                    }
                    this.stopNestedScroll();
                    break;
                }
                case 0: {
                    final int mLastMotionY2 = (int)motionEvent.getY();
                    if (!this.inChild((int)motionEvent.getX(), mLastMotionY2)) {
                        this.mIsBeingDragged = false;
                        this.recycleVelocityTracker();
                        break;
                    }
                    this.mLastMotionY = mLastMotionY2;
                    this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, 0);
                    this.initOrResetVelocityTracker();
                    this.mVelocityTracker.addMovement(motionEvent);
                    this.mScroller.computeScrollOffset();
                    this.mIsBeingDragged = (this.mScroller.isFinished() ^ true);
                    this.startNestedScroll(2);
                    break;
                }
            }
        }
        else {
            this.onSecondaryPointerUp(motionEvent);
        }
        return this.mIsBeingDragged;
    }
    
    protected void onLayout(final boolean b, int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        this.mIsLayoutDirty = false;
        if (this.mChildToScrollTo != null && isViewDescendantOf(this.mChildToScrollTo, (View)this)) {
            this.scrollToChild(this.mChildToScrollTo);
        }
        this.mChildToScrollTo = null;
        if (!this.mIsLaidOut) {
            if (this.mSavedState != null) {
                this.scrollTo(this.getScrollX(), this.mSavedState.scrollPosition);
                this.mSavedState = null;
            }
            if (this.getChildCount() > 0) {
                n = this.getChildAt(0).getMeasuredHeight();
            }
            else {
                n = 0;
            }
            n = Math.max(0, n - (n4 - n2 - this.getPaddingBottom() - this.getPaddingTop()));
            if (this.getScrollY() > n) {
                this.scrollTo(this.getScrollX(), n);
            }
            else if (this.getScrollY() < 0) {
                this.scrollTo(this.getScrollX(), 0);
            }
        }
        this.scrollTo(this.getScrollX(), this.getScrollY());
        this.mIsLaidOut = true;
    }
    
    protected void onMeasure(final int n, int measuredHeight) {
        super.onMeasure(n, measuredHeight);
        if (!this.mFillViewport) {
            return;
        }
        if (View$MeasureSpec.getMode(measuredHeight) != 0 && this.getChildCount() > 0) {
            final View child = this.getChildAt(0);
            measuredHeight = this.getMeasuredHeight();
            if (child.getMeasuredHeight() < measuredHeight) {
                child.measure(getChildMeasureSpec(n, this.getPaddingLeft() + this.getPaddingRight(), ((FrameLayout$LayoutParams)child.getLayoutParams()).width), View$MeasureSpec.makeMeasureSpec(measuredHeight - this.getPaddingTop() - this.getPaddingBottom(), 1073741824));
            }
        }
    }
    
    public boolean onNestedFling(final View view, final float n, final float n2, final boolean b) {
        if (!b) {
            this.flingWithNestedDispatch((int)n2);
            return true;
        }
        return false;
    }
    
    public boolean onNestedPreFling(final View view, final float n, final float n2) {
        return false;
    }
    
    public void onNestedPreScroll(final View view, final int n, final int n2, final int[] array) {
    }
    
    public void onNestedScroll(final View view, int scrollY, final int n, final int n2, final int n3) {
        scrollY = this.getScrollY();
        this.scrollBy(0, n3);
        scrollY = this.getScrollY() - scrollY;
        this.dispatchNestedScroll(0, scrollY, 0, n3 - scrollY, null);
    }
    
    public void onNestedScrollAccepted(final View view, final View view2, final int n) {
        this.mParentHelper.onNestedScrollAccepted(view, view2, n);
        this.startNestedScroll(2);
    }
    
    protected void onOverScrolled(final int n, final int n2, final boolean b, final boolean b2) {
        super.scrollTo(n, n2);
    }
    
    protected boolean onRequestFocusInDescendants(final int n, final Rect rect) {
        int n2;
        if (n == 2) {
            n2 = 130;
        }
        else if ((n2 = n) == 1) {
            n2 = 33;
        }
        View view;
        if (rect == null) {
            view = FocusFinder.getInstance().findNextFocus((ViewGroup)this, (View)null, n2);
        }
        else {
            view = FocusFinder.getInstance().findNextFocusFromRect((ViewGroup)this, rect, n2);
        }
        if (view != null) {
            if (!this.isOffScreen(view)) {
                return view.requestFocus(n2, rect);
            }
        }
        return false;
    }
    
    protected void onRestoreInstanceState(final Parcelable parcelable) {
        final SavedState mSavedState = (SavedState)parcelable;
        super.onRestoreInstanceState(mSavedState.getSuperState());
        this.mSavedState = mSavedState;
        this.requestLayout();
    }
    
    protected Parcelable onSaveInstanceState() {
        final SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.scrollPosition = this.getScrollY();
        return (Parcelable)savedState;
    }
    
    protected void onScrollChanged(final int n, final int n2, final int n3, final int n4) {
        super.onScrollChanged(n, n2, n3, n4);
        if (this.mOnScrollChangeListener != null) {
            this.mOnScrollChangeListener.onScrollChange(this, n, n2, n3, n4);
        }
    }
    
    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        final View focus = this.findFocus();
        if (focus != null) {
            if (this == focus) {
                return;
            }
            if (this.isWithinDeltaOfScreen(focus, 0, n4)) {
                focus.getDrawingRect(this.mTempRect);
                this.offsetDescendantRectToMyCoords(focus, this.mTempRect);
                this.doScrollY(this.computeScrollDeltaToGetChildRectOnScreen(this.mTempRect));
            }
        }
    }
    
    public boolean onStartNestedScroll(final View view, final View view2, final int n) {
        return (n & 0x2) != 0x0;
    }
    
    public void onStopNestedScroll(final View view) {
        this.mParentHelper.onStopNestedScroll(view);
        this.stopNestedScroll();
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        this.initVelocityTrackerIfNotExists();
        final MotionEvent obtain = MotionEvent.obtain(motionEvent);
        final int actionMasked = MotionEventCompat.getActionMasked(motionEvent);
        if (actionMasked == 0) {
            this.mNestedYOffset = 0;
        }
        obtain.offsetLocation(0.0f, (float)this.mNestedYOffset);
        Label_0890: {
            Label_0787: {
                switch (actionMasked) {
                    default: {
                        break Label_0890;
                    }
                    case 6: {
                        this.onSecondaryPointerUp(motionEvent);
                        this.mLastMotionY = (int)MotionEventCompat.getY(motionEvent, MotionEventCompat.findPointerIndex(motionEvent, this.mActivePointerId));
                        break Label_0890;
                    }
                    case 5: {
                        final int actionIndex = MotionEventCompat.getActionIndex(motionEvent);
                        this.mLastMotionY = (int)MotionEventCompat.getY(motionEvent, actionIndex);
                        this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, actionIndex);
                        break Label_0890;
                    }
                    case 3: {
                        if (this.mIsBeingDragged && this.getChildCount() > 0 && this.mScroller.springBack(this.getScrollX(), this.getScrollY(), 0, 0, 0, this.getScrollRange())) {
                            break;
                        }
                        break Label_0787;
                    }
                    case 2: {
                        final int pointerIndex = MotionEventCompat.findPointerIndex(motionEvent, this.mActivePointerId);
                        if (pointerIndex == -1) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Invalid pointerId=");
                            sb.append(this.mActivePointerId);
                            sb.append(" in onTouchEvent");
                            Log.e("NestedScrollView", sb.toString());
                            break Label_0890;
                        }
                        final int n = (int)MotionEventCompat.getY(motionEvent, pointerIndex);
                        int n3;
                        final int n2 = n3 = this.mLastMotionY - n;
                        if (this.dispatchNestedPreScroll(0, n2, this.mScrollConsumed, this.mScrollOffset)) {
                            n3 = n2 - this.mScrollConsumed[1];
                            obtain.offsetLocation(0.0f, (float)this.mScrollOffset[1]);
                            this.mNestedYOffset += this.mScrollOffset[1];
                        }
                        int n4 = n3;
                        if (!this.mIsBeingDragged) {
                            n4 = n3;
                            if (Math.abs(n3) > this.mTouchSlop) {
                                final ViewParent parent = this.getParent();
                                if (parent != null) {
                                    parent.requestDisallowInterceptTouchEvent(true);
                                }
                                this.mIsBeingDragged = true;
                                if (n3 > 0) {
                                    n4 = n3 - this.mTouchSlop;
                                }
                                else {
                                    n4 = n3 + this.mTouchSlop;
                                }
                            }
                        }
                        if (!this.mIsBeingDragged) {
                            break Label_0890;
                        }
                        this.mLastMotionY = n - this.mScrollOffset[1];
                        final int scrollY = this.getScrollY();
                        final int scrollRange = this.getScrollRange();
                        final int overScrollMode = ViewCompat.getOverScrollMode((View)this);
                        final boolean b = overScrollMode == 0 || (overScrollMode == 1 && scrollRange > 0);
                        if (this.overScrollByCompat(0, n4, 0, this.getScrollY(), 0, scrollRange, 0, 0, true) && !this.hasNestedScrollingParent()) {
                            this.mVelocityTracker.clear();
                        }
                        final int n5 = this.getScrollY() - scrollY;
                        if (this.dispatchNestedScroll(0, n5, 0, n4 - n5, this.mScrollOffset)) {
                            this.mLastMotionY -= this.mScrollOffset[1];
                            obtain.offsetLocation(0.0f, (float)this.mScrollOffset[1]);
                            this.mNestedYOffset += this.mScrollOffset[1];
                            break Label_0890;
                        }
                        if (!b) {
                            break Label_0890;
                        }
                        this.ensureGlows();
                        final int n6 = scrollY + n4;
                        Label_0670: {
                            EdgeEffectCompat edgeEffectCompat;
                            if (n6 < 0) {
                                this.mEdgeGlowTop.onPull(n4 / (float)this.getHeight(), MotionEventCompat.getX(motionEvent, pointerIndex) / this.getWidth());
                                if (this.mEdgeGlowBottom.isFinished()) {
                                    break Label_0670;
                                }
                                edgeEffectCompat = this.mEdgeGlowBottom;
                            }
                            else {
                                if (n6 <= scrollRange) {
                                    break Label_0670;
                                }
                                this.mEdgeGlowBottom.onPull(n4 / (float)this.getHeight(), 1.0f - MotionEventCompat.getX(motionEvent, pointerIndex) / this.getWidth());
                                if (this.mEdgeGlowTop.isFinished()) {
                                    break Label_0670;
                                }
                                edgeEffectCompat = this.mEdgeGlowTop;
                            }
                            edgeEffectCompat.onRelease();
                        }
                        if (this.mEdgeGlowTop != null && (!this.mEdgeGlowTop.isFinished() || !this.mEdgeGlowBottom.isFinished())) {
                            ViewCompat.postInvalidateOnAnimation((View)this);
                        }
                        break Label_0890;
                    }
                    case 1: {
                        if (!this.mIsBeingDragged) {
                            break Label_0787;
                        }
                        final VelocityTracker mVelocityTracker = this.mVelocityTracker;
                        mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
                        final int n7 = (int)VelocityTrackerCompat.getYVelocity(mVelocityTracker, this.mActivePointerId);
                        if (Math.abs(n7) > this.mMinimumVelocity) {
                            this.flingWithNestedDispatch(-n7);
                            break Label_0787;
                        }
                        if (this.mScroller.springBack(this.getScrollX(), this.getScrollY(), 0, 0, 0, this.getScrollRange())) {
                            break;
                        }
                        break Label_0787;
                    }
                    case 0: {
                        if (this.getChildCount() == 0) {
                            return false;
                        }
                        final boolean mIsBeingDragged = this.mScroller.isFinished() ^ true;
                        this.mIsBeingDragged = mIsBeingDragged;
                        if (mIsBeingDragged) {
                            final ViewParent parent2 = this.getParent();
                            if (parent2 != null) {
                                parent2.requestDisallowInterceptTouchEvent(true);
                            }
                        }
                        if (!this.mScroller.isFinished()) {
                            this.mScroller.abortAnimation();
                        }
                        this.mLastMotionY = (int)motionEvent.getY();
                        this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, 0);
                        this.startNestedScroll(2);
                        break Label_0890;
                    }
                }
                ViewCompat.postInvalidateOnAnimation((View)this);
            }
            this.mActivePointerId = -1;
            this.endDrag();
        }
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.addMovement(obtain);
        }
        obtain.recycle();
        return true;
    }
    
    boolean overScrollByCompat(int n, int n2, int n3, int n4, int n5, int n6, int n7, final int n8, final boolean b) {
        final int overScrollMode = ViewCompat.getOverScrollMode((View)this);
        final boolean b2 = this.computeHorizontalScrollRange() > this.computeHorizontalScrollExtent();
        final boolean b3 = this.computeVerticalScrollRange() > this.computeVerticalScrollExtent();
        final boolean b4 = overScrollMode == 0 || (overScrollMode == 1 && b2);
        final boolean b5 = overScrollMode == 0 || (overScrollMode == 1 && b3);
        n3 += n;
        if (!b4) {
            n = 0;
        }
        else {
            n = n7;
        }
        n4 += n2;
        if (!b5) {
            n2 = 0;
        }
        else {
            n2 = n8;
        }
        n7 = -n;
        n += n5;
        n5 = -n2;
        n6 += n2;
        boolean b6 = false;
        Label_0187: {
            if (n3 <= n) {
                if (n3 >= n7) {
                    n2 = n3;
                    b6 = false;
                    break Label_0187;
                }
                n = n7;
            }
            b6 = true;
            n2 = n;
        }
        boolean b7 = false;
        Label_0222: {
            if (n4 > n6) {
                n = n6;
            }
            else {
                if (n4 >= n5) {
                    n = n4;
                    b7 = false;
                    break Label_0222;
                }
                n = n5;
            }
            b7 = true;
        }
        if (b7) {
            this.mScroller.springBack(n2, n, 0, 0, 0, this.getScrollRange());
        }
        this.onOverScrolled(n2, n, b6, b7);
        return b6 || b7;
    }
    
    public boolean pageScroll(final int n) {
        final boolean b = false;
        final boolean b2 = n == 130;
        final int height = this.getHeight();
        Label_0132: {
            Rect rect;
            int top;
            if (b2) {
                this.mTempRect.top = this.getScrollY() + height;
                final int childCount = this.getChildCount();
                if (childCount <= 0) {
                    break Label_0132;
                }
                final View child = this.getChildAt(childCount - 1);
                if (this.mTempRect.top + height <= child.getBottom()) {
                    break Label_0132;
                }
                rect = this.mTempRect;
                top = child.getBottom() - height;
            }
            else {
                this.mTempRect.top = this.getScrollY() - height;
                if (this.mTempRect.top >= 0) {
                    break Label_0132;
                }
                rect = this.mTempRect;
                top = (b ? 1 : 0);
            }
            rect.top = top;
        }
        this.mTempRect.bottom = this.mTempRect.top + height;
        return this.scrollAndFocus(n, this.mTempRect.top, this.mTempRect.bottom);
    }
    
    public void requestChildFocus(final View view, final View mChildToScrollTo) {
        if (!this.mIsLayoutDirty) {
            this.scrollToChild(mChildToScrollTo);
        }
        else {
            this.mChildToScrollTo = mChildToScrollTo;
        }
        super.requestChildFocus(view, mChildToScrollTo);
    }
    
    public boolean requestChildRectangleOnScreen(final View view, final Rect rect, final boolean b) {
        rect.offset(view.getLeft() - view.getScrollX(), view.getTop() - view.getScrollY());
        return this.scrollToChildRect(rect, b);
    }
    
    public void requestDisallowInterceptTouchEvent(final boolean b) {
        if (b) {
            this.recycleVelocityTracker();
        }
        super.requestDisallowInterceptTouchEvent(b);
    }
    
    public void requestLayout() {
        this.mIsLayoutDirty = true;
        super.requestLayout();
    }
    
    public void scrollTo(int clamp, int clamp2) {
        if (this.getChildCount() > 0) {
            final View child = this.getChildAt(0);
            clamp = clamp(clamp, this.getWidth() - this.getPaddingRight() - this.getPaddingLeft(), child.getWidth());
            clamp2 = clamp(clamp2, this.getHeight() - this.getPaddingBottom() - this.getPaddingTop(), child.getHeight());
            if (clamp != this.getScrollX() || clamp2 != this.getScrollY()) {
                super.scrollTo(clamp, clamp2);
            }
        }
    }
    
    public void setFillViewport(final boolean mFillViewport) {
        if (mFillViewport != this.mFillViewport) {
            this.mFillViewport = mFillViewport;
            this.requestLayout();
        }
    }
    
    public void setNestedScrollingEnabled(final boolean nestedScrollingEnabled) {
        this.mChildHelper.setNestedScrollingEnabled(nestedScrollingEnabled);
    }
    
    public void setOnScrollChangeListener(final OnScrollChangeListener mOnScrollChangeListener) {
        this.mOnScrollChangeListener = mOnScrollChangeListener;
    }
    
    public void setSmoothScrollingEnabled(final boolean mSmoothScrollingEnabled) {
        this.mSmoothScrollingEnabled = mSmoothScrollingEnabled;
    }
    
    public boolean shouldDelayChildPressedState() {
        return true;
    }
    
    public final void smoothScrollBy(int n, int max) {
        if (this.getChildCount() == 0) {
            return;
        }
        if (AnimationUtils.currentAnimationTimeMillis() - this.mLastScroll > 250L) {
            n = this.getHeight();
            final int max2 = Math.max(0, this.getChildAt(0).getHeight() - (n - this.getPaddingBottom() - this.getPaddingTop()));
            n = this.getScrollY();
            max = Math.max(0, Math.min(max + n, max2));
            this.mScroller.startScroll(this.getScrollX(), n, 0, max - n);
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
        else {
            if (!this.mScroller.isFinished()) {
                this.mScroller.abortAnimation();
            }
            this.scrollBy(n, max);
        }
        this.mLastScroll = AnimationUtils.currentAnimationTimeMillis();
    }
    
    public final void smoothScrollTo(final int n, final int n2) {
        this.smoothScrollBy(n - this.getScrollX(), n2 - this.getScrollY());
    }
    
    public boolean startNestedScroll(final int n) {
        return this.mChildHelper.startNestedScroll(n);
    }
    
    public void stopNestedScroll() {
        this.mChildHelper.stopNestedScroll();
    }
    
    static class AccessibilityDelegate extends AccessibilityDelegateCompat
    {
        @Override
        public void onInitializeAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(view, accessibilityEvent);
            final NestedScrollView nestedScrollView = (NestedScrollView)view;
            accessibilityEvent.setClassName((CharSequence)ScrollView.class.getName());
            final AccessibilityRecordCompat record = AccessibilityEventCompat.asRecord(accessibilityEvent);
            record.setScrollable(nestedScrollView.getScrollRange() > 0);
            record.setScrollX(nestedScrollView.getScrollX());
            record.setScrollY(nestedScrollView.getScrollY());
            record.setMaxScrollX(nestedScrollView.getScrollX());
            record.setMaxScrollY(nestedScrollView.getScrollRange());
        }
        
        @Override
        public void onInitializeAccessibilityNodeInfo(final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            final NestedScrollView nestedScrollView = (NestedScrollView)view;
            accessibilityNodeInfoCompat.setClassName(ScrollView.class.getName());
            if (nestedScrollView.isEnabled()) {
                final int access$000 = nestedScrollView.getScrollRange();
                if (access$000 > 0) {
                    accessibilityNodeInfoCompat.setScrollable(true);
                    if (nestedScrollView.getScrollY() > 0) {
                        accessibilityNodeInfoCompat.addAction(8192);
                    }
                    if (nestedScrollView.getScrollY() < access$000) {
                        accessibilityNodeInfoCompat.addAction(4096);
                    }
                }
            }
        }
        
        @Override
        public boolean performAccessibilityAction(final View view, int n, final Bundle bundle) {
            if (super.performAccessibilityAction(view, n, bundle)) {
                return true;
            }
            final NestedScrollView nestedScrollView = (NestedScrollView)view;
            if (!nestedScrollView.isEnabled()) {
                return false;
            }
            if (n != 4096) {
                if (n != 8192) {
                    return false;
                }
                n = nestedScrollView.getHeight();
                n = Math.max(nestedScrollView.getScrollY() - (n - nestedScrollView.getPaddingBottom() - nestedScrollView.getPaddingTop()), 0);
                if (n != nestedScrollView.getScrollY()) {
                    nestedScrollView.smoothScrollTo(0, n);
                    return true;
                }
                return false;
            }
            else {
                n = nestedScrollView.getHeight();
                n = Math.min(nestedScrollView.getScrollY() + (n - nestedScrollView.getPaddingBottom() - nestedScrollView.getPaddingTop()), nestedScrollView.getScrollRange());
                if (n != nestedScrollView.getScrollY()) {
                    nestedScrollView.smoothScrollTo(0, n);
                    return true;
                }
                return false;
            }
        }
    }
    
    public interface OnScrollChangeListener
    {
        void onScrollChange(final NestedScrollView p0, final int p1, final int p2, final int p3, final int p4);
    }
    
    static class SavedState extends View$BaseSavedState
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        public int scrollPosition;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<SavedState>() {
                public SavedState createFromParcel(final Parcel parcel) {
                    return new SavedState(parcel);
                }
                
                public SavedState[] newArray(final int n) {
                    return new SavedState[n];
                }
            };
        }
        
        public SavedState(final Parcel parcel) {
            super(parcel);
            this.scrollPosition = parcel.readInt();
        }
        
        SavedState(final Parcelable parcelable) {
            super(parcelable);
        }
        
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("HorizontalScrollView.SavedState{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(" scrollPosition=");
            sb.append(this.scrollPosition);
            sb.append("}");
            return sb.toString();
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.scrollPosition);
        }
    }
}
