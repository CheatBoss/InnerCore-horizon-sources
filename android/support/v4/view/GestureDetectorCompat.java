package android.support.v4.view;

import android.content.*;
import android.os.*;
import android.view.*;

public final class GestureDetectorCompat
{
    private final GestureDetectorCompatImpl mImpl;
    
    public GestureDetectorCompat(final Context context, final GestureDetector$OnGestureListener gestureDetector$OnGestureListener) {
        this(context, gestureDetector$OnGestureListener, null);
    }
    
    public GestureDetectorCompat(final Context context, final GestureDetector$OnGestureListener gestureDetector$OnGestureListener, final Handler handler) {
        GestureDetectorCompatImpl mImpl;
        if (Build$VERSION.SDK_INT > 17) {
            mImpl = new GestureDetectorCompatImplJellybeanMr2(context, gestureDetector$OnGestureListener, handler);
        }
        else {
            mImpl = new GestureDetectorCompatImplBase(context, gestureDetector$OnGestureListener, handler);
        }
        this.mImpl = mImpl;
    }
    
    public boolean isLongpressEnabled() {
        return this.mImpl.isLongpressEnabled();
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        return this.mImpl.onTouchEvent(motionEvent);
    }
    
    public void setIsLongpressEnabled(final boolean isLongpressEnabled) {
        this.mImpl.setIsLongpressEnabled(isLongpressEnabled);
    }
    
    public void setOnDoubleTapListener(final GestureDetector$OnDoubleTapListener onDoubleTapListener) {
        this.mImpl.setOnDoubleTapListener(onDoubleTapListener);
    }
    
    interface GestureDetectorCompatImpl
    {
        boolean isLongpressEnabled();
        
        boolean onTouchEvent(final MotionEvent p0);
        
        void setIsLongpressEnabled(final boolean p0);
        
        void setOnDoubleTapListener(final GestureDetector$OnDoubleTapListener p0);
    }
    
    static class GestureDetectorCompatImplBase implements GestureDetectorCompatImpl
    {
        private static final int DOUBLE_TAP_TIMEOUT;
        private static final int LONGPRESS_TIMEOUT;
        private static final int LONG_PRESS = 2;
        private static final int SHOW_PRESS = 1;
        private static final int TAP = 3;
        private static final int TAP_TIMEOUT;
        private boolean mAlwaysInBiggerTapRegion;
        private boolean mAlwaysInTapRegion;
        private MotionEvent mCurrentDownEvent;
        private boolean mDeferConfirmSingleTap;
        private GestureDetector$OnDoubleTapListener mDoubleTapListener;
        private int mDoubleTapSlopSquare;
        private float mDownFocusX;
        private float mDownFocusY;
        private final Handler mHandler;
        private boolean mInLongPress;
        private boolean mIsDoubleTapping;
        private boolean mIsLongpressEnabled;
        private float mLastFocusX;
        private float mLastFocusY;
        private final GestureDetector$OnGestureListener mListener;
        private int mMaximumFlingVelocity;
        private int mMinimumFlingVelocity;
        private MotionEvent mPreviousUpEvent;
        private boolean mStillDown;
        private int mTouchSlopSquare;
        private VelocityTracker mVelocityTracker;
        
        static {
            LONGPRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
            TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
            DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();
        }
        
        public GestureDetectorCompatImplBase(final Context context, final GestureDetector$OnGestureListener mListener, final Handler handler) {
            if (handler != null) {
                this.mHandler = new GestureHandler(handler);
            }
            else {
                this.mHandler = new GestureHandler();
            }
            this.mListener = mListener;
            if (mListener instanceof GestureDetector$OnDoubleTapListener) {
                this.setOnDoubleTapListener((GestureDetector$OnDoubleTapListener)mListener);
            }
            this.init(context);
        }
        
        private void cancel() {
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
            this.mHandler.removeMessages(3);
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
            this.mIsDoubleTapping = false;
            this.mStillDown = false;
            this.mAlwaysInTapRegion = false;
            this.mAlwaysInBiggerTapRegion = false;
            this.mDeferConfirmSingleTap = false;
            if (this.mInLongPress) {
                this.mInLongPress = false;
            }
        }
        
        private void cancelTaps() {
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
            this.mHandler.removeMessages(3);
            this.mIsDoubleTapping = false;
            this.mAlwaysInTapRegion = false;
            this.mAlwaysInBiggerTapRegion = false;
            this.mDeferConfirmSingleTap = false;
            if (this.mInLongPress) {
                this.mInLongPress = false;
            }
        }
        
        private void dispatchLongPress() {
            this.mHandler.removeMessages(3);
            this.mDeferConfirmSingleTap = false;
            this.mInLongPress = true;
            this.mListener.onLongPress(this.mCurrentDownEvent);
        }
        
        private void init(final Context context) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null");
            }
            if (this.mListener == null) {
                throw new IllegalArgumentException("OnGestureListener must not be null");
            }
            this.mIsLongpressEnabled = true;
            final ViewConfiguration value = ViewConfiguration.get(context);
            final int scaledTouchSlop = value.getScaledTouchSlop();
            final int scaledDoubleTapSlop = value.getScaledDoubleTapSlop();
            this.mMinimumFlingVelocity = value.getScaledMinimumFlingVelocity();
            this.mMaximumFlingVelocity = value.getScaledMaximumFlingVelocity();
            this.mTouchSlopSquare = scaledTouchSlop * scaledTouchSlop;
            this.mDoubleTapSlopSquare = scaledDoubleTapSlop * scaledDoubleTapSlop;
        }
        
        private boolean isConsideredDoubleTap(final MotionEvent motionEvent, final MotionEvent motionEvent2, final MotionEvent motionEvent3) {
            if (this.mAlwaysInBiggerTapRegion) {
                if (motionEvent3.getEventTime() - motionEvent2.getEventTime() <= GestureDetectorCompatImplBase.DOUBLE_TAP_TIMEOUT) {
                    final int n = (int)motionEvent.getX() - (int)motionEvent3.getX();
                    final int n2 = (int)motionEvent.getY() - (int)motionEvent3.getY();
                    if (n * n + n2 * n2 < this.mDoubleTapSlopSquare) {
                        return true;
                    }
                }
            }
            return false;
        }
        
        @Override
        public boolean isLongpressEnabled() {
            return this.mIsLongpressEnabled;
        }
        
        @Override
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            final int action = motionEvent.getAction();
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(motionEvent);
            final int n = action & 0xFF;
            final boolean b = n == 6;
            int actionIndex;
            if (b) {
                actionIndex = MotionEventCompat.getActionIndex(motionEvent);
            }
            else {
                actionIndex = -1;
            }
            final int pointerCount = MotionEventCompat.getPointerCount(motionEvent);
            int i = 0;
            float n2 = 0.0f;
            float n3 = 0.0f;
            while (i < pointerCount) {
                if (actionIndex != i) {
                    n2 += MotionEventCompat.getX(motionEvent, i);
                    n3 += MotionEventCompat.getY(motionEvent, i);
                }
                ++i;
            }
            int n4;
            if (b) {
                n4 = pointerCount - 1;
            }
            else {
                n4 = pointerCount;
            }
            final float n5 = (float)n4;
            final float n6 = n2 / n5;
            final float n7 = n3 / n5;
            boolean b2 = false;
            while (true) {
                switch (n) {
                    default: {
                        break Label_1139;
                    }
                    case 5: {
                        this.mLastFocusX = n6;
                        this.mDownFocusX = n6;
                        this.mLastFocusY = n7;
                        this.mDownFocusY = n7;
                        this.cancelTaps();
                        return false;
                    }
                    case 3: {
                        this.cancel();
                        return false;
                    }
                    case 2: {
                        if (this.mInLongPress) {
                            break Label_1139;
                        }
                        final float n8 = this.mLastFocusX - n6;
                        final float n9 = this.mLastFocusY - n7;
                        if (this.mIsDoubleTapping) {
                            return this.mDoubleTapListener.onDoubleTapEvent(motionEvent) | false;
                        }
                        if (this.mAlwaysInTapRegion) {
                            final int n10 = (int)(n6 - this.mDownFocusX);
                            final int n11 = (int)(n7 - this.mDownFocusY);
                            final int n12 = n10 * n10 + n11 * n11;
                            boolean onScroll;
                            if (n12 > this.mTouchSlopSquare) {
                                onScroll = this.mListener.onScroll(this.mCurrentDownEvent, motionEvent, n8, n9);
                                this.mLastFocusX = n6;
                                this.mLastFocusY = n7;
                                this.mAlwaysInTapRegion = false;
                                this.mHandler.removeMessages(3);
                                this.mHandler.removeMessages(1);
                                this.mHandler.removeMessages(2);
                            }
                            else {
                                onScroll = false;
                            }
                            b2 = onScroll;
                            if (n12 > this.mTouchSlopSquare) {
                                this.mAlwaysInBiggerTapRegion = false;
                                return onScroll;
                            }
                            break;
                        }
                        else {
                            if (Math.abs(n8) >= 1.0f || Math.abs(n9) >= 1.0f) {
                                final boolean onScroll2 = this.mListener.onScroll(this.mCurrentDownEvent, motionEvent, n8, n9);
                                this.mLastFocusX = n6;
                                this.mLastFocusY = n7;
                                return onScroll2;
                            }
                            break Label_1139;
                        }
                        break;
                    }
                    case 1: {
                        this.mStillDown = false;
                        final MotionEvent obtain = MotionEvent.obtain(motionEvent);
                        boolean b3 = false;
                        Label_0811: {
                            if (this.mIsDoubleTapping) {
                                b3 = (this.mDoubleTapListener.onDoubleTapEvent(motionEvent) | false);
                            }
                            else {
                                if (this.mInLongPress) {
                                    this.mHandler.removeMessages(3);
                                    this.mInLongPress = false;
                                }
                                else {
                                    if (this.mAlwaysInTapRegion) {
                                        b3 = this.mListener.onSingleTapUp(motionEvent);
                                        if (this.mDeferConfirmSingleTap && this.mDoubleTapListener != null) {
                                            this.mDoubleTapListener.onSingleTapConfirmed(motionEvent);
                                        }
                                        break Label_0811;
                                    }
                                    final VelocityTracker mVelocityTracker = this.mVelocityTracker;
                                    final int pointerId = MotionEventCompat.getPointerId(motionEvent, 0);
                                    mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumFlingVelocity);
                                    final float yVelocity = VelocityTrackerCompat.getYVelocity(mVelocityTracker, pointerId);
                                    final float xVelocity = VelocityTrackerCompat.getXVelocity(mVelocityTracker, pointerId);
                                    if (Math.abs(yVelocity) > this.mMinimumFlingVelocity || Math.abs(xVelocity) > this.mMinimumFlingVelocity) {
                                        b3 = this.mListener.onFling(this.mCurrentDownEvent, motionEvent, xVelocity, yVelocity);
                                        break Label_0811;
                                    }
                                }
                                b3 = false;
                            }
                        }
                        if (this.mPreviousUpEvent != null) {
                            this.mPreviousUpEvent.recycle();
                        }
                        this.mPreviousUpEvent = obtain;
                        if (this.mVelocityTracker != null) {
                            this.mVelocityTracker.recycle();
                            this.mVelocityTracker = null;
                        }
                        this.mIsDoubleTapping = false;
                        this.mDeferConfirmSingleTap = false;
                        this.mHandler.removeMessages(1);
                        this.mHandler.removeMessages(2);
                        return b3;
                    }
                    case 0: {
                        boolean b4 = false;
                        Label_0996: {
                            if (this.mDoubleTapListener != null) {
                                final boolean hasMessages = this.mHandler.hasMessages(3);
                                if (hasMessages) {
                                    this.mHandler.removeMessages(3);
                                }
                                if (this.mCurrentDownEvent != null && this.mPreviousUpEvent != null && hasMessages && this.isConsideredDoubleTap(this.mCurrentDownEvent, this.mPreviousUpEvent, motionEvent)) {
                                    this.mIsDoubleTapping = true;
                                    b4 = (this.mDoubleTapListener.onDoubleTap(this.mCurrentDownEvent) | false | this.mDoubleTapListener.onDoubleTapEvent(motionEvent));
                                    break Label_0996;
                                }
                                this.mHandler.sendEmptyMessageDelayed(3, (long)GestureDetectorCompatImplBase.DOUBLE_TAP_TIMEOUT);
                            }
                            b4 = false;
                        }
                        this.mLastFocusX = n6;
                        this.mDownFocusX = n6;
                        this.mLastFocusY = n7;
                        this.mDownFocusY = n7;
                        if (this.mCurrentDownEvent != null) {
                            this.mCurrentDownEvent.recycle();
                        }
                        this.mCurrentDownEvent = MotionEvent.obtain(motionEvent);
                        this.mAlwaysInTapRegion = true;
                        this.mAlwaysInBiggerTapRegion = true;
                        this.mStillDown = true;
                        this.mInLongPress = false;
                        this.mDeferConfirmSingleTap = false;
                        if (this.mIsLongpressEnabled) {
                            this.mHandler.removeMessages(2);
                            this.mHandler.sendEmptyMessageAtTime(2, this.mCurrentDownEvent.getDownTime() + GestureDetectorCompatImplBase.TAP_TIMEOUT + GestureDetectorCompatImplBase.LONGPRESS_TIMEOUT);
                        }
                        this.mHandler.sendEmptyMessageAtTime(1, this.mCurrentDownEvent.getDownTime() + GestureDetectorCompatImplBase.TAP_TIMEOUT);
                        return this.mListener.onDown(motionEvent) | b4;
                    }
                    case 4: {
                        b2 = false;
                        break;
                    }
                    case 6: {
                        this.mLastFocusX = n6;
                        this.mDownFocusX = n6;
                        this.mLastFocusY = n7;
                        this.mDownFocusY = n7;
                        this.mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumFlingVelocity);
                        final int actionIndex2 = MotionEventCompat.getActionIndex(motionEvent);
                        final int pointerId2 = MotionEventCompat.getPointerId(motionEvent, actionIndex2);
                        final float xVelocity2 = VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, pointerId2);
                        final float yVelocity2 = VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, pointerId2);
                        for (int j = 0; j < pointerCount; ++j) {
                            if (j != actionIndex2) {
                                final int pointerId3 = MotionEventCompat.getPointerId(motionEvent, j);
                                if (VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, pointerId3) * xVelocity2 + VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, pointerId3) * yVelocity2 < 0.0f) {
                                    this.mVelocityTracker.clear();
                                    return false;
                                }
                            }
                        }
                        continue;
                    }
                }
                break;
            }
            return b2;
        }
        
        @Override
        public void setIsLongpressEnabled(final boolean mIsLongpressEnabled) {
            this.mIsLongpressEnabled = mIsLongpressEnabled;
        }
        
        @Override
        public void setOnDoubleTapListener(final GestureDetector$OnDoubleTapListener mDoubleTapListener) {
            this.mDoubleTapListener = mDoubleTapListener;
        }
        
        private class GestureHandler extends Handler
        {
            GestureHandler() {
            }
            
            GestureHandler(final Handler handler) {
                super(handler.getLooper());
            }
            
            public void handleMessage(final Message message) {
                switch (message.what) {
                    default: {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Unknown message ");
                        sb.append(message);
                        throw new RuntimeException(sb.toString());
                    }
                    case 3: {
                        if (GestureDetectorCompatImplBase.this.mDoubleTapListener == null) {
                            break;
                        }
                        if (!GestureDetectorCompatImplBase.this.mStillDown) {
                            GestureDetectorCompatImplBase.this.mDoubleTapListener.onSingleTapConfirmed(GestureDetectorCompatImplBase.this.mCurrentDownEvent);
                            return;
                        }
                        GestureDetectorCompatImplBase.this.mDeferConfirmSingleTap = true;
                    }
                    case 2: {
                        GestureDetectorCompatImplBase.this.dispatchLongPress();
                    }
                    case 1: {
                        GestureDetectorCompatImplBase.this.mListener.onShowPress(GestureDetectorCompatImplBase.this.mCurrentDownEvent);
                        break;
                    }
                }
            }
        }
    }
    
    static class GestureDetectorCompatImplJellybeanMr2 implements GestureDetectorCompatImpl
    {
        private final GestureDetector mDetector;
        
        public GestureDetectorCompatImplJellybeanMr2(final Context context, final GestureDetector$OnGestureListener gestureDetector$OnGestureListener, final Handler handler) {
            this.mDetector = new GestureDetector(context, gestureDetector$OnGestureListener, handler);
        }
        
        @Override
        public boolean isLongpressEnabled() {
            return this.mDetector.isLongpressEnabled();
        }
        
        @Override
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            return this.mDetector.onTouchEvent(motionEvent);
        }
        
        @Override
        public void setIsLongpressEnabled(final boolean isLongpressEnabled) {
            this.mDetector.setIsLongpressEnabled(isLongpressEnabled);
        }
        
        @Override
        public void setOnDoubleTapListener(final GestureDetector$OnDoubleTapListener onDoubleTapListener) {
            this.mDetector.setOnDoubleTapListener(onDoubleTapListener);
        }
    }
}
