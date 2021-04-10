package android.support.v4.widget;

import android.view.animation.*;
import android.content.*;
import java.util.*;
import android.util.*;
import android.view.*;
import android.support.v4.view.*;

public class ViewDragHelper
{
    private static final int BASE_SETTLE_DURATION = 256;
    public static final int DIRECTION_ALL = 3;
    public static final int DIRECTION_HORIZONTAL = 1;
    public static final int DIRECTION_VERTICAL = 2;
    public static final int EDGE_ALL = 15;
    public static final int EDGE_BOTTOM = 8;
    public static final int EDGE_LEFT = 1;
    public static final int EDGE_RIGHT = 2;
    private static final int EDGE_SIZE = 20;
    public static final int EDGE_TOP = 4;
    public static final int INVALID_POINTER = -1;
    private static final int MAX_SETTLE_DURATION = 600;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    private static final String TAG = "ViewDragHelper";
    private static final Interpolator sInterpolator;
    private int mActivePointerId;
    private final Callback mCallback;
    private View mCapturedView;
    private int mDragState;
    private int[] mEdgeDragsInProgress;
    private int[] mEdgeDragsLocked;
    private int mEdgeSize;
    private int[] mInitialEdgesTouched;
    private float[] mInitialMotionX;
    private float[] mInitialMotionY;
    private float[] mLastMotionX;
    private float[] mLastMotionY;
    private float mMaxVelocity;
    private float mMinVelocity;
    private final ViewGroup mParentView;
    private int mPointersDown;
    private boolean mReleaseInProgress;
    private ScrollerCompat mScroller;
    private final Runnable mSetIdleRunnable;
    private int mTouchSlop;
    private int mTrackingEdges;
    private VelocityTracker mVelocityTracker;
    
    static {
        sInterpolator = (Interpolator)new Interpolator() {
            public float getInterpolation(float n) {
                --n;
                return n * n * n * n * n + 1.0f;
            }
        };
    }
    
    private ViewDragHelper(final Context context, final ViewGroup mParentView, final Callback mCallback) {
        this.mActivePointerId = -1;
        this.mSetIdleRunnable = new Runnable() {
            @Override
            public void run() {
                ViewDragHelper.this.setDragState(0);
            }
        };
        if (mParentView == null) {
            throw new IllegalArgumentException("Parent view may not be null");
        }
        if (mCallback == null) {
            throw new IllegalArgumentException("Callback may not be null");
        }
        this.mParentView = mParentView;
        this.mCallback = mCallback;
        final ViewConfiguration value = ViewConfiguration.get(context);
        this.mEdgeSize = (int)(context.getResources().getDisplayMetrics().density * 20.0f + 0.5f);
        this.mTouchSlop = value.getScaledTouchSlop();
        this.mMaxVelocity = (float)value.getScaledMaximumFlingVelocity();
        this.mMinVelocity = (float)value.getScaledMinimumFlingVelocity();
        this.mScroller = ScrollerCompat.create(context, ViewDragHelper.sInterpolator);
    }
    
    private boolean checkNewEdgeDrag(float abs, float abs2, final int n, final int n2) {
        abs = Math.abs(abs);
        abs2 = Math.abs(abs2);
        if ((this.mInitialEdgesTouched[n] & n2) == n2 && (this.mTrackingEdges & n2) != 0x0 && (this.mEdgeDragsLocked[n] & n2) != n2 && (this.mEdgeDragsInProgress[n] & n2) != n2) {
            if (abs <= this.mTouchSlop && abs2 <= this.mTouchSlop) {
                return false;
            }
            if (abs < abs2 * 0.5f && this.mCallback.onEdgeLock(n2)) {
                final int[] mEdgeDragsLocked = this.mEdgeDragsLocked;
                mEdgeDragsLocked[n] |= n2;
                return false;
            }
            if ((this.mEdgeDragsInProgress[n] & n2) == 0x0 && abs > this.mTouchSlop) {
                return true;
            }
        }
        return false;
    }
    
    private boolean checkTouchSlop(final View view, final float n, final float n2) {
        if (view == null) {
            return false;
        }
        final boolean b = this.mCallback.getViewHorizontalDragRange(view) > 0;
        final boolean b2 = this.mCallback.getViewVerticalDragRange(view) > 0;
        if (b && b2) {
            if (n * n + n2 * n2 <= this.mTouchSlop * this.mTouchSlop) {
                return false;
            }
        }
        else if (b) {
            if (Math.abs(n) <= this.mTouchSlop) {
                return false;
            }
        }
        else {
            if (!b2) {
                return false;
            }
            if (Math.abs(n2) <= this.mTouchSlop) {
                return false;
            }
        }
        return true;
    }
    
    private float clampMag(final float n, final float n2, final float n3) {
        final float abs = Math.abs(n);
        if (abs < n2) {
            return 0.0f;
        }
        if (abs <= n3) {
            return n;
        }
        if (n <= 0.0f) {
            return -n3;
        }
        return n3;
    }
    
    private int clampMag(final int n, final int n2, final int n3) {
        final int abs = Math.abs(n);
        if (abs < n2) {
            return 0;
        }
        if (abs <= n3) {
            return n;
        }
        if (n <= 0) {
            return -n3;
        }
        return n3;
    }
    
    private void clearMotionHistory() {
        if (this.mInitialMotionX == null) {
            return;
        }
        Arrays.fill(this.mInitialMotionX, 0.0f);
        Arrays.fill(this.mInitialMotionY, 0.0f);
        Arrays.fill(this.mLastMotionX, 0.0f);
        Arrays.fill(this.mLastMotionY, 0.0f);
        Arrays.fill(this.mInitialEdgesTouched, 0);
        Arrays.fill(this.mEdgeDragsInProgress, 0);
        Arrays.fill(this.mEdgeDragsLocked, 0);
        this.mPointersDown = 0;
    }
    
    private void clearMotionHistory(final int n) {
        if (this.mInitialMotionX == null) {
            return;
        }
        this.mInitialMotionX[n] = 0.0f;
        this.mInitialMotionY[n] = 0.0f;
        this.mLastMotionX[n] = 0.0f;
        this.mLastMotionY[n] = 0.0f;
        this.mInitialEdgesTouched[n] = 0;
        this.mEdgeDragsInProgress[n] = 0;
        this.mEdgeDragsLocked[n] = 0;
        this.mPointersDown &= ~(1 << n);
    }
    
    private int computeAxisDuration(int n, int abs, final int n2) {
        if (n == 0) {
            return 0;
        }
        final int width = this.mParentView.getWidth();
        final int n3 = width / 2;
        final float min = Math.min(1.0f, Math.abs(n) / (float)width);
        final float n4 = (float)n3;
        final float distanceInfluenceForSnapDuration = this.distanceInfluenceForSnapDuration(min);
        abs = Math.abs(abs);
        if (abs > 0) {
            n = Math.round(Math.abs((n4 + distanceInfluenceForSnapDuration * n4) / abs) * 1000.0f) * 4;
        }
        else {
            n = (int)((Math.abs(n) / (float)n2 + 1.0f) * 256.0f);
        }
        return Math.min(n, 600);
    }
    
    private int computeSettleDuration(final View view, int computeAxisDuration, int computeAxisDuration2, int clampMag, int clampMag2) {
        clampMag = this.clampMag(clampMag, (int)this.mMinVelocity, (int)this.mMaxVelocity);
        clampMag2 = this.clampMag(clampMag2, (int)this.mMinVelocity, (int)this.mMaxVelocity);
        final int abs = Math.abs(computeAxisDuration);
        final int abs2 = Math.abs(computeAxisDuration2);
        final int abs3 = Math.abs(clampMag);
        final int abs4 = Math.abs(clampMag2);
        final int n = abs3 + abs4;
        final int n2 = abs + abs2;
        float n3;
        float n4;
        if (clampMag != 0) {
            n3 = (float)abs3;
            n4 = (float)n;
        }
        else {
            n3 = (float)abs;
            n4 = (float)n2;
        }
        final float n5 = n3 / n4;
        float n6;
        float n7;
        if (clampMag2 != 0) {
            n6 = (float)abs4;
            n7 = (float)n;
        }
        else {
            n6 = (float)abs2;
            n7 = (float)n2;
        }
        final float n8 = n6 / n7;
        computeAxisDuration = this.computeAxisDuration(computeAxisDuration, clampMag, this.mCallback.getViewHorizontalDragRange(view));
        computeAxisDuration2 = this.computeAxisDuration(computeAxisDuration2, clampMag2, this.mCallback.getViewVerticalDragRange(view));
        return (int)(computeAxisDuration * n5 + computeAxisDuration2 * n8);
    }
    
    public static ViewDragHelper create(final ViewGroup viewGroup, final float n, final Callback callback) {
        final ViewDragHelper create = create(viewGroup, callback);
        create.mTouchSlop *= (int)(1.0f / n);
        return create;
    }
    
    public static ViewDragHelper create(final ViewGroup viewGroup, final Callback callback) {
        return new ViewDragHelper(viewGroup.getContext(), viewGroup, callback);
    }
    
    private void dispatchViewReleased(final float n, final float n2) {
        this.mReleaseInProgress = true;
        this.mCallback.onViewReleased(this.mCapturedView, n, n2);
        this.mReleaseInProgress = false;
        if (this.mDragState == 1) {
            this.setDragState(0);
        }
    }
    
    private float distanceInfluenceForSnapDuration(final float n) {
        return (float)Math.sin((float)((n - 0.5f) * 0.4712389167638204));
    }
    
    private void dragTo(int clampViewPositionVertical, final int n, final int n2, final int n3) {
        final int left = this.mCapturedView.getLeft();
        final int top = this.mCapturedView.getTop();
        int clampViewPositionHorizontal = clampViewPositionVertical;
        if (n2 != 0) {
            clampViewPositionHorizontal = this.mCallback.clampViewPositionHorizontal(this.mCapturedView, clampViewPositionVertical, n2);
            ViewCompat.offsetLeftAndRight(this.mCapturedView, clampViewPositionHorizontal - left);
        }
        clampViewPositionVertical = n;
        if (n3 != 0) {
            clampViewPositionVertical = this.mCallback.clampViewPositionVertical(this.mCapturedView, n, n3);
            ViewCompat.offsetTopAndBottom(this.mCapturedView, clampViewPositionVertical - top);
        }
        if (n2 != 0 || n3 != 0) {
            this.mCallback.onViewPositionChanged(this.mCapturedView, clampViewPositionHorizontal, clampViewPositionVertical, clampViewPositionHorizontal - left, clampViewPositionVertical - top);
        }
    }
    
    private void ensureMotionHistorySizeForId(int n) {
        if (this.mInitialMotionX == null || this.mInitialMotionX.length <= n) {
            ++n;
            final float[] mInitialMotionX = new float[n];
            final float[] mInitialMotionY = new float[n];
            final float[] mLastMotionX = new float[n];
            final float[] mLastMotionY = new float[n];
            final int[] mInitialEdgesTouched = new int[n];
            final int[] mEdgeDragsInProgress = new int[n];
            final int[] mEdgeDragsLocked = new int[n];
            if (this.mInitialMotionX != null) {
                System.arraycopy(this.mInitialMotionX, 0, mInitialMotionX, 0, this.mInitialMotionX.length);
                System.arraycopy(this.mInitialMotionY, 0, mInitialMotionY, 0, this.mInitialMotionY.length);
                System.arraycopy(this.mLastMotionX, 0, mLastMotionX, 0, this.mLastMotionX.length);
                System.arraycopy(this.mLastMotionY, 0, mLastMotionY, 0, this.mLastMotionY.length);
                System.arraycopy(this.mInitialEdgesTouched, 0, mInitialEdgesTouched, 0, this.mInitialEdgesTouched.length);
                System.arraycopy(this.mEdgeDragsInProgress, 0, mEdgeDragsInProgress, 0, this.mEdgeDragsInProgress.length);
                System.arraycopy(this.mEdgeDragsLocked, 0, mEdgeDragsLocked, 0, this.mEdgeDragsLocked.length);
            }
            this.mInitialMotionX = mInitialMotionX;
            this.mInitialMotionY = mInitialMotionY;
            this.mLastMotionX = mLastMotionX;
            this.mLastMotionY = mLastMotionY;
            this.mInitialEdgesTouched = mInitialEdgesTouched;
            this.mEdgeDragsInProgress = mEdgeDragsInProgress;
            this.mEdgeDragsLocked = mEdgeDragsLocked;
        }
    }
    
    private boolean forceSettleCapturedViewAt(int n, int n2, int computeSettleDuration, final int n3) {
        final int left = this.mCapturedView.getLeft();
        final int top = this.mCapturedView.getTop();
        n -= left;
        n2 -= top;
        if (n == 0 && n2 == 0) {
            this.mScroller.abortAnimation();
            this.setDragState(0);
            return false;
        }
        computeSettleDuration = this.computeSettleDuration(this.mCapturedView, n, n2, computeSettleDuration, n3);
        this.mScroller.startScroll(left, top, n, n2, computeSettleDuration);
        this.setDragState(2);
        return true;
    }
    
    private int getEdgesTouched(int n, final int n2) {
        int n3;
        final boolean b = (n3 = ((n < this.mParentView.getLeft() + this.mEdgeSize) ? 1 : 0)) != 0;
        if (n2 < this.mParentView.getTop() + this.mEdgeSize) {
            n3 = ((b ? 1 : 0) | 0x4);
        }
        int n4 = n3;
        if (n > this.mParentView.getRight() - this.mEdgeSize) {
            n4 = (n3 | 0x2);
        }
        n = n4;
        if (n2 > this.mParentView.getBottom() - this.mEdgeSize) {
            n = (n4 | 0x8);
        }
        return n;
    }
    
    private boolean isValidPointerForActionMove(final int n) {
        if (!this.isPointerDown(n)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Ignoring pointerId=");
            sb.append(n);
            sb.append(" because ACTION_DOWN was not received ");
            sb.append("for this pointer before ACTION_MOVE. It likely happened because ");
            sb.append(" ViewDragHelper did not receive all the events in the event stream.");
            Log.e("ViewDragHelper", sb.toString());
            return false;
        }
        return true;
    }
    
    private void releaseViewForPointerUp() {
        this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaxVelocity);
        this.dispatchViewReleased(this.clampMag(VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity), this.clampMag(VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity));
    }
    
    private void reportNewEdgeDrags(final float n, final float n2, final int n3) {
        boolean b = true;
        if (!this.checkNewEdgeDrag(n, n2, n3, 1)) {
            b = false;
        }
        int n4 = b ? 1 : 0;
        if (this.checkNewEdgeDrag(n2, n, n3, 4)) {
            n4 = ((b ? 1 : 0) | 0x4);
        }
        int n5 = n4;
        if (this.checkNewEdgeDrag(n, n2, n3, 2)) {
            n5 = (n4 | 0x2);
        }
        int n6 = n5;
        if (this.checkNewEdgeDrag(n2, n, n3, 8)) {
            n6 = (n5 | 0x8);
        }
        if (n6 != 0) {
            final int[] mEdgeDragsInProgress = this.mEdgeDragsInProgress;
            mEdgeDragsInProgress[n3] |= n6;
            this.mCallback.onEdgeDragStarted(n6, n3);
        }
    }
    
    private void saveInitialMotion(final float n, final float n2, final int n3) {
        this.ensureMotionHistorySizeForId(n3);
        this.mInitialMotionX[n3] = (this.mLastMotionX[n3] = n);
        this.mInitialMotionY[n3] = (this.mLastMotionY[n3] = n2);
        this.mInitialEdgesTouched[n3] = this.getEdgesTouched((int)n, (int)n2);
        this.mPointersDown |= 1 << n3;
    }
    
    private void saveLastMotion(final MotionEvent motionEvent) {
        for (int pointerCount = MotionEventCompat.getPointerCount(motionEvent), i = 0; i < pointerCount; ++i) {
            final int pointerId = MotionEventCompat.getPointerId(motionEvent, i);
            final float x = MotionEventCompat.getX(motionEvent, i);
            final float y = MotionEventCompat.getY(motionEvent, i);
            this.mLastMotionX[pointerId] = x;
            this.mLastMotionY[pointerId] = y;
        }
    }
    
    public void abort() {
        this.cancel();
        if (this.mDragState == 2) {
            final int currX = this.mScroller.getCurrX();
            final int currY = this.mScroller.getCurrY();
            this.mScroller.abortAnimation();
            final int currX2 = this.mScroller.getCurrX();
            final int currY2 = this.mScroller.getCurrY();
            this.mCallback.onViewPositionChanged(this.mCapturedView, currX2, currY2, currX2 - currX, currY2 - currY);
        }
        this.setDragState(0);
    }
    
    protected boolean canScroll(final View view, final boolean b, final int n, final int n2, final int n3, final int n4) {
        if (view instanceof ViewGroup) {
            final ViewGroup viewGroup = (ViewGroup)view;
            final int scrollX = view.getScrollX();
            final int scrollY = view.getScrollY();
            for (int i = viewGroup.getChildCount() - 1; i >= 0; --i) {
                final View child = viewGroup.getChildAt(i);
                final int n5 = n3 + scrollX;
                if (n5 >= child.getLeft() && n5 < child.getRight()) {
                    final int n6 = n4 + scrollY;
                    if (n6 >= child.getTop() && n6 < child.getBottom() && this.canScroll(child, true, n, n2, n5 - child.getLeft(), n6 - child.getTop())) {
                        return true;
                    }
                }
            }
        }
        return b && (ViewCompat.canScrollHorizontally(view, -n) || ViewCompat.canScrollVertically(view, -n2));
    }
    
    public void cancel() {
        this.mActivePointerId = -1;
        this.clearMotionHistory();
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }
    
    public void captureChildView(final View mCapturedView, final int mActivePointerId) {
        if (mCapturedView.getParent() != this.mParentView) {
            final StringBuilder sb = new StringBuilder();
            sb.append("captureChildView: parameter must be a descendant of the ViewDragHelper's tracked parent view (");
            sb.append(this.mParentView);
            sb.append(")");
            throw new IllegalArgumentException(sb.toString());
        }
        this.mCapturedView = mCapturedView;
        this.mActivePointerId = mActivePointerId;
        this.mCallback.onViewCaptured(mCapturedView, mActivePointerId);
        this.setDragState(1);
    }
    
    public boolean checkTouchSlop(final int n) {
        for (int length = this.mInitialMotionX.length, i = 0; i < length; ++i) {
            if (this.checkTouchSlop(n, i)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean checkTouchSlop(int n, final int n2) {
        if (!this.isPointerDown(n2)) {
            return false;
        }
        final boolean b = (n & 0x1) == 0x1;
        if ((n & 0x2) == 0x2) {
            n = 1;
        }
        else {
            n = 0;
        }
        final float n3 = this.mLastMotionX[n2] - this.mInitialMotionX[n2];
        final float n4 = this.mLastMotionY[n2] - this.mInitialMotionY[n2];
        if (b && n != 0) {
            if (n3 * n3 + n4 * n4 <= this.mTouchSlop * this.mTouchSlop) {
                return false;
            }
        }
        else if (b) {
            if (Math.abs(n3) <= this.mTouchSlop) {
                return false;
            }
        }
        else {
            if (n == 0) {
                return false;
            }
            if (Math.abs(n4) <= this.mTouchSlop) {
                return false;
            }
        }
        return true;
    }
    
    public boolean continueSettling(final boolean b) {
        if (this.mDragState == 2) {
            final boolean computeScrollOffset = this.mScroller.computeScrollOffset();
            final int currX = this.mScroller.getCurrX();
            final int currY = this.mScroller.getCurrY();
            final int n = currX - this.mCapturedView.getLeft();
            final int n2 = currY - this.mCapturedView.getTop();
            if (n != 0) {
                ViewCompat.offsetLeftAndRight(this.mCapturedView, n);
            }
            if (n2 != 0) {
                ViewCompat.offsetTopAndBottom(this.mCapturedView, n2);
            }
            if (n != 0 || n2 != 0) {
                this.mCallback.onViewPositionChanged(this.mCapturedView, currX, currY, n, n2);
            }
            boolean b2 = computeScrollOffset;
            if (computeScrollOffset) {
                b2 = computeScrollOffset;
                if (currX == this.mScroller.getFinalX()) {
                    b2 = computeScrollOffset;
                    if (currY == this.mScroller.getFinalY()) {
                        this.mScroller.abortAnimation();
                        b2 = false;
                    }
                }
            }
            if (!b2) {
                if (b) {
                    this.mParentView.post(this.mSetIdleRunnable);
                }
                else {
                    this.setDragState(0);
                }
            }
        }
        return this.mDragState == 2;
    }
    
    public View findTopChildUnder(final int n, final int n2) {
        int childCount = this.mParentView.getChildCount();
        while (true) {
            final int n3 = childCount - 1;
            if (n3 < 0) {
                return null;
            }
            final View child = this.mParentView.getChildAt(this.mCallback.getOrderedChildIndex(n3));
            childCount = n3;
            if (n < child.getLeft()) {
                continue;
            }
            childCount = n3;
            if (n >= child.getRight()) {
                continue;
            }
            childCount = n3;
            if (n2 < child.getTop()) {
                continue;
            }
            childCount = n3;
            if (n2 < child.getBottom()) {
                return child;
            }
        }
    }
    
    public void flingCapturedView(final int n, final int n2, final int n3, final int n4) {
        if (!this.mReleaseInProgress) {
            throw new IllegalStateException("Cannot flingCapturedView outside of a call to Callback#onViewReleased");
        }
        this.mScroller.fling(this.mCapturedView.getLeft(), this.mCapturedView.getTop(), (int)VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mActivePointerId), (int)VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mActivePointerId), n, n3, n2, n4);
        this.setDragState(2);
    }
    
    public int getActivePointerId() {
        return this.mActivePointerId;
    }
    
    public View getCapturedView() {
        return this.mCapturedView;
    }
    
    public int getEdgeSize() {
        return this.mEdgeSize;
    }
    
    public float getMinVelocity() {
        return this.mMinVelocity;
    }
    
    public int getTouchSlop() {
        return this.mTouchSlop;
    }
    
    public int getViewDragState() {
        return this.mDragState;
    }
    
    public boolean isCapturedViewUnder(final int n, final int n2) {
        return this.isViewUnder(this.mCapturedView, n, n2);
    }
    
    public boolean isEdgeTouched(final int n) {
        for (int length = this.mInitialEdgesTouched.length, i = 0; i < length; ++i) {
            if (this.isEdgeTouched(n, i)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isEdgeTouched(final int n, final int n2) {
        return this.isPointerDown(n2) && (n & this.mInitialEdgesTouched[n2]) != 0x0;
    }
    
    public boolean isPointerDown(final int n) {
        return (1 << n & this.mPointersDown) != 0x0;
    }
    
    public boolean isViewUnder(final View view, final int n, final int n2) {
        if (view != null) {
            if (n >= view.getLeft() && n < view.getRight() && n2 >= view.getTop() && n2 < view.getBottom()) {
                return true;
            }
        }
        return false;
    }
    
    public void processTouchEvent(final MotionEvent motionEvent) {
        final int actionMasked = MotionEventCompat.getActionMasked(motionEvent);
        final int actionIndex = MotionEventCompat.getActionIndex(motionEvent);
        if (actionMasked == 0) {
            this.cancel();
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        final int n = 0;
        int i = 0;
        switch (actionMasked) {
            default: {}
            case 6: {
                final int pointerId = MotionEventCompat.getPointerId(motionEvent, actionIndex);
                Label_0224: {
                    if (this.mDragState == 1 && pointerId == this.mActivePointerId) {
                        while (true) {
                            while (i < MotionEventCompat.getPointerCount(motionEvent)) {
                                final int pointerId2 = MotionEventCompat.getPointerId(motionEvent, i);
                                if (pointerId2 != this.mActivePointerId) {
                                    if (this.findTopChildUnder((int)MotionEventCompat.getX(motionEvent, i), (int)MotionEventCompat.getY(motionEvent, i)) == this.mCapturedView && this.tryCaptureViewForDrag(this.mCapturedView, pointerId2)) {
                                        final int mActivePointerId = this.mActivePointerId;
                                        if (mActivePointerId == -1) {
                                            this.releaseViewForPointerUp();
                                        }
                                        break Label_0224;
                                    }
                                }
                                ++i;
                            }
                            final int mActivePointerId = -1;
                            continue;
                        }
                    }
                }
                this.clearMotionHistory(pointerId);
            }
            case 5: {
                final int pointerId3 = MotionEventCompat.getPointerId(motionEvent, actionIndex);
                final float x = MotionEventCompat.getX(motionEvent, actionIndex);
                final float y = MotionEventCompat.getY(motionEvent, actionIndex);
                this.saveInitialMotion(x, y, pointerId3);
                if (this.mDragState == 0) {
                    this.tryCaptureViewForDrag(this.findTopChildUnder((int)x, (int)y), pointerId3);
                    final int n2 = this.mInitialEdgesTouched[pointerId3];
                    if ((this.mTrackingEdges & n2) != 0x0) {
                        this.mCallback.onEdgeTouched(n2 & this.mTrackingEdges, pointerId3);
                        return;
                    }
                    break;
                }
                else {
                    if (this.isCapturedViewUnder((int)x, (int)y)) {
                        this.tryCaptureViewForDrag(this.mCapturedView, pointerId3);
                        return;
                    }
                    break;
                }
                break;
            }
            case 3: {
                if (this.mDragState == 1) {
                    this.dispatchViewReleased(0.0f, 0.0f);
                }
                this.cancel();
            }
            case 2: {
                if (this.mDragState != 1) {
                    for (int pointerCount = MotionEventCompat.getPointerCount(motionEvent), j = n; j < pointerCount; ++j) {
                        final int pointerId4 = MotionEventCompat.getPointerId(motionEvent, j);
                        if (this.isValidPointerForActionMove(pointerId4)) {
                            final float x2 = MotionEventCompat.getX(motionEvent, j);
                            final float y2 = MotionEventCompat.getY(motionEvent, j);
                            final float n3 = x2 - this.mInitialMotionX[pointerId4];
                            final float n4 = y2 - this.mInitialMotionY[pointerId4];
                            this.reportNewEdgeDrags(n3, n4, pointerId4);
                            if (this.mDragState == 1) {
                                break;
                            }
                            final View topChildUnder = this.findTopChildUnder((int)x2, (int)y2);
                            if (this.checkTouchSlop(topChildUnder, n3, n4) && this.tryCaptureViewForDrag(topChildUnder, pointerId4)) {
                                break;
                            }
                        }
                    }
                    this.saveLastMotion(motionEvent);
                    return;
                }
                if (this.isValidPointerForActionMove(this.mActivePointerId)) {
                    final int pointerIndex = MotionEventCompat.findPointerIndex(motionEvent, this.mActivePointerId);
                    final float x3 = MotionEventCompat.getX(motionEvent, pointerIndex);
                    final float y3 = MotionEventCompat.getY(motionEvent, pointerIndex);
                    final int n5 = (int)(x3 - this.mLastMotionX[this.mActivePointerId]);
                    final int n6 = (int)(y3 - this.mLastMotionY[this.mActivePointerId]);
                    this.dragTo(this.mCapturedView.getLeft() + n5, this.mCapturedView.getTop() + n6, n5, n6);
                    this.saveLastMotion(motionEvent);
                    return;
                }
                break;
            }
            case 1: {
                if (this.mDragState == 1) {
                    this.releaseViewForPointerUp();
                }
                this.cancel();
            }
            case 0: {
                final float x4 = motionEvent.getX();
                final float y4 = motionEvent.getY();
                final int pointerId5 = MotionEventCompat.getPointerId(motionEvent, 0);
                final View topChildUnder2 = this.findTopChildUnder((int)x4, (int)y4);
                this.saveInitialMotion(x4, y4, pointerId5);
                this.tryCaptureViewForDrag(topChildUnder2, pointerId5);
                final int n7 = this.mInitialEdgesTouched[pointerId5];
                if ((this.mTrackingEdges & n7) != 0x0) {
                    this.mCallback.onEdgeTouched(n7 & this.mTrackingEdges, pointerId5);
                    break;
                }
                break;
            }
        }
    }
    
    void setDragState(final int mDragState) {
        this.mParentView.removeCallbacks(this.mSetIdleRunnable);
        if (this.mDragState != mDragState) {
            this.mDragState = mDragState;
            this.mCallback.onViewDragStateChanged(mDragState);
            if (this.mDragState == 0) {
                this.mCapturedView = null;
            }
        }
    }
    
    public void setEdgeTrackingEnabled(final int mTrackingEdges) {
        this.mTrackingEdges = mTrackingEdges;
    }
    
    public void setMinVelocity(final float mMinVelocity) {
        this.mMinVelocity = mMinVelocity;
    }
    
    public boolean settleCapturedViewAt(final int n, final int n2) {
        if (!this.mReleaseInProgress) {
            throw new IllegalStateException("Cannot settleCapturedViewAt outside of a call to Callback#onViewReleased");
        }
        return this.forceSettleCapturedViewAt(n, n2, (int)VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mActivePointerId), (int)VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mActivePointerId));
    }
    
    public boolean shouldInterceptTouchEvent(final MotionEvent motionEvent) {
        final int actionMasked = MotionEventCompat.getActionMasked(motionEvent);
        final int actionIndex = MotionEventCompat.getActionIndex(motionEvent);
        if (actionMasked == 0) {
            this.cancel();
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        switch (actionMasked) {
            case 6: {
                this.clearMotionHistory(MotionEventCompat.getPointerId(motionEvent, actionIndex));
                break;
            }
            case 5: {
                final int pointerId = MotionEventCompat.getPointerId(motionEvent, actionIndex);
                final float x = MotionEventCompat.getX(motionEvent, actionIndex);
                final float y = MotionEventCompat.getY(motionEvent, actionIndex);
                this.saveInitialMotion(x, y, pointerId);
                if (this.mDragState == 0) {
                    final int n = this.mInitialEdgesTouched[pointerId];
                    if ((this.mTrackingEdges & n) != 0x0) {
                        this.mCallback.onEdgeTouched(n & this.mTrackingEdges, pointerId);
                        break;
                    }
                    break;
                }
                else {
                    if (this.mDragState != 2) {
                        break;
                    }
                    final View topChildUnder = this.findTopChildUnder((int)x, (int)y);
                    if (topChildUnder == this.mCapturedView) {
                        this.tryCaptureViewForDrag(topChildUnder, pointerId);
                        break;
                    }
                    break;
                }
                break;
            }
            case 2: {
                if (this.mInitialMotionX != null && this.mInitialMotionY != null) {
                    for (int pointerCount = MotionEventCompat.getPointerCount(motionEvent), i = 0; i < pointerCount; ++i) {
                        final int pointerId2 = MotionEventCompat.getPointerId(motionEvent, i);
                        if (this.isValidPointerForActionMove(pointerId2)) {
                            final float x2 = MotionEventCompat.getX(motionEvent, i);
                            final float y2 = MotionEventCompat.getY(motionEvent, i);
                            final float n2 = x2 - this.mInitialMotionX[pointerId2];
                            final float n3 = y2 - this.mInitialMotionY[pointerId2];
                            final View topChildUnder2 = this.findTopChildUnder((int)x2, (int)y2);
                            final boolean b = topChildUnder2 != null && this.checkTouchSlop(topChildUnder2, n2, n3);
                            if (b) {
                                final int left = topChildUnder2.getLeft();
                                final int n4 = (int)n2;
                                final int clampViewPositionHorizontal = this.mCallback.clampViewPositionHorizontal(topChildUnder2, left + n4, n4);
                                final int top = topChildUnder2.getTop();
                                final int n5 = (int)n3;
                                final int clampViewPositionVertical = this.mCallback.clampViewPositionVertical(topChildUnder2, top + n5, n5);
                                final int viewHorizontalDragRange = this.mCallback.getViewHorizontalDragRange(topChildUnder2);
                                final int viewVerticalDragRange = this.mCallback.getViewVerticalDragRange(topChildUnder2);
                                if (viewHorizontalDragRange == 0 || (viewHorizontalDragRange > 0 && clampViewPositionHorizontal == left)) {
                                    if (viewVerticalDragRange == 0) {
                                        break;
                                    }
                                    if (viewVerticalDragRange > 0 && clampViewPositionVertical == top) {
                                        break;
                                    }
                                }
                            }
                            this.reportNewEdgeDrags(n2, n3, pointerId2);
                            if (this.mDragState == 1) {
                                break;
                            }
                            if (b && this.tryCaptureViewForDrag(topChildUnder2, pointerId2)) {
                                break;
                            }
                        }
                    }
                    this.saveLastMotion(motionEvent);
                    break;
                }
                break;
            }
            case 1:
            case 3: {
                this.cancel();
                break;
            }
            case 0: {
                final float x3 = motionEvent.getX();
                final float y3 = motionEvent.getY();
                final int pointerId3 = MotionEventCompat.getPointerId(motionEvent, 0);
                this.saveInitialMotion(x3, y3, pointerId3);
                final View topChildUnder3 = this.findTopChildUnder((int)x3, (int)y3);
                if (topChildUnder3 == this.mCapturedView && this.mDragState == 2) {
                    this.tryCaptureViewForDrag(topChildUnder3, pointerId3);
                }
                final int n6 = this.mInitialEdgesTouched[pointerId3];
                if ((this.mTrackingEdges & n6) != 0x0) {
                    this.mCallback.onEdgeTouched(n6 & this.mTrackingEdges, pointerId3);
                    break;
                }
                break;
            }
        }
        return this.mDragState == 1;
    }
    
    public boolean smoothSlideViewTo(final View mCapturedView, final int n, final int n2) {
        this.mCapturedView = mCapturedView;
        this.mActivePointerId = -1;
        final boolean forceSettleCapturedView = this.forceSettleCapturedViewAt(n, n2, 0, 0);
        if (!forceSettleCapturedView && this.mDragState == 0 && this.mCapturedView != null) {
            this.mCapturedView = null;
        }
        return forceSettleCapturedView;
    }
    
    boolean tryCaptureViewForDrag(final View view, final int mActivePointerId) {
        if (view == this.mCapturedView && this.mActivePointerId == mActivePointerId) {
            return true;
        }
        if (view != null && this.mCallback.tryCaptureView(view, mActivePointerId)) {
            this.captureChildView(view, this.mActivePointerId = mActivePointerId);
            return true;
        }
        return false;
    }
    
    public abstract static class Callback
    {
        public int clampViewPositionHorizontal(final View view, final int n, final int n2) {
            return 0;
        }
        
        public int clampViewPositionVertical(final View view, final int n, final int n2) {
            return 0;
        }
        
        public int getOrderedChildIndex(final int n) {
            return n;
        }
        
        public int getViewHorizontalDragRange(final View view) {
            return 0;
        }
        
        public int getViewVerticalDragRange(final View view) {
            return 0;
        }
        
        public void onEdgeDragStarted(final int n, final int n2) {
        }
        
        public boolean onEdgeLock(final int n) {
            return false;
        }
        
        public void onEdgeTouched(final int n, final int n2) {
        }
        
        public void onViewCaptured(final View view, final int n) {
        }
        
        public void onViewDragStateChanged(final int n) {
        }
        
        public void onViewPositionChanged(final View view, final int n, final int n2, final int n3, final int n4) {
        }
        
        public void onViewReleased(final View view, final float n, final float n2) {
        }
        
        public abstract boolean tryCaptureView(final View p0, final int p1);
    }
}
