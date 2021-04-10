package com.appboy.ui.inappmessage.listeners;

import android.view.*;
import android.animation.*;

public class SwipeDismissTouchListener implements View$OnTouchListener
{
    private long mAnimationTime;
    private DismissCallbacks mCallbacks;
    private float mDownX;
    private float mDownY;
    private int mMaxFlingVelocity;
    private int mMinFlingVelocity;
    private int mSlop;
    private boolean mSwiping;
    private int mSwipingSlop;
    private Object mToken;
    private float mTranslationX;
    private VelocityTracker mVelocityTracker;
    private View mView;
    private int mViewWidth;
    
    public SwipeDismissTouchListener(final View mView, final Object mToken, final DismissCallbacks mCallbacks) {
        this.mViewWidth = 1;
        final ViewConfiguration value = ViewConfiguration.get(mView.getContext());
        this.mSlop = value.getScaledTouchSlop();
        this.mMinFlingVelocity = value.getScaledMinimumFlingVelocity() * 16;
        this.mMaxFlingVelocity = value.getScaledMaximumFlingVelocity();
        this.mAnimationTime = mView.getContext().getResources().getInteger(17694720);
        this.mView = mView;
        this.mToken = mToken;
        this.mCallbacks = mCallbacks;
    }
    
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        motionEvent.offsetLocation(this.mTranslationX, 0.0f);
        if (this.mViewWidth < 2) {
            this.mViewWidth = this.mView.getWidth();
        }
        final int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 0) {
            int n = 1;
            final boolean b = true;
            if (actionMasked != 1) {
                if (actionMasked != 2) {
                    if (actionMasked != 3) {
                        return false;
                    }
                    if (this.mVelocityTracker == null) {
                        return false;
                    }
                    this.mView.animate().translationX(0.0f).alpha(1.0f).setDuration(this.mAnimationTime).setListener((Animator$AnimatorListener)null);
                    this.mVelocityTracker.recycle();
                    this.mVelocityTracker = null;
                    this.mTranslationX = 0.0f;
                    this.mDownX = 0.0f;
                    this.mDownY = 0.0f;
                    return this.mSwiping = false;
                }
                else {
                    final VelocityTracker mVelocityTracker = this.mVelocityTracker;
                    if (mVelocityTracker == null) {
                        return false;
                    }
                    mVelocityTracker.addMovement(motionEvent);
                    final float mTranslationX = motionEvent.getRawX() - this.mDownX;
                    final float rawY = motionEvent.getRawY();
                    final float mDownY = this.mDownY;
                    if (Math.abs(mTranslationX) > this.mSlop && Math.abs(rawY - mDownY) < Math.abs(mTranslationX) / 2.0f) {
                        this.mSwiping = true;
                        int mSlop;
                        if (mTranslationX > 0.0f) {
                            mSlop = this.mSlop;
                        }
                        else {
                            mSlop = -this.mSlop;
                        }
                        this.mSwipingSlop = mSlop;
                        this.mView.getParent().requestDisallowInterceptTouchEvent(true);
                        final MotionEvent obtain = MotionEvent.obtain(motionEvent);
                        obtain.setAction(motionEvent.getActionIndex() << 8 | 0x3);
                        this.mView.onTouchEvent(obtain);
                        obtain.recycle();
                    }
                    if (this.mSwiping) {
                        this.mTranslationX = mTranslationX;
                        this.mView.setTranslationX(mTranslationX - this.mSwipingSlop);
                        return true;
                    }
                }
            }
            else {
                if (this.mVelocityTracker == null) {
                    return false;
                }
                final float n2 = motionEvent.getRawX() - this.mDownX;
                this.mVelocityTracker.addMovement(motionEvent);
                this.mVelocityTracker.computeCurrentVelocity(1000);
                final float xVelocity = this.mVelocityTracker.getXVelocity();
                final float abs = Math.abs(xVelocity);
                final float abs2 = Math.abs(this.mVelocityTracker.getYVelocity());
                int n3;
                if (Math.abs(n2) > this.mViewWidth / 2 && this.mSwiping) {
                    if (n2 > 0.0f) {
                        n3 = 1;
                    }
                    else {
                        n3 = 0;
                    }
                }
                else if (this.mMinFlingVelocity <= abs && abs <= this.mMaxFlingVelocity && abs2 < abs && this.mSwiping) {
                    final boolean b2 = xVelocity < 0.0f == n2 < 0.0f;
                    final boolean b3 = this.mVelocityTracker.getXVelocity() > 0.0f && b;
                    n = (b2 ? 1 : 0);
                    n3 = (b3 ? 1 : 0);
                }
                else {
                    n3 = 0;
                    n = 0;
                }
                if (n != 0) {
                    final ViewPropertyAnimator animate = this.mView.animate();
                    int mViewWidth;
                    if (n3 != 0) {
                        mViewWidth = this.mViewWidth;
                    }
                    else {
                        mViewWidth = -this.mViewWidth;
                    }
                    animate.translationX((float)mViewWidth).alpha(0.0f).setDuration(this.mAnimationTime).setListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationEnd(final Animator animator) {
                            SwipeDismissTouchListener.this.performDismiss();
                        }
                    });
                }
                else if (this.mSwiping) {
                    this.mView.animate().translationX(0.0f).alpha(1.0f).setDuration(this.mAnimationTime).setListener((Animator$AnimatorListener)null);
                }
                this.mVelocityTracker.recycle();
                this.mVelocityTracker = null;
                this.mTranslationX = 0.0f;
                this.mDownX = 0.0f;
                this.mDownY = 0.0f;
                this.mSwiping = false;
            }
            return false;
        }
        this.mDownX = motionEvent.getRawX();
        this.mDownY = motionEvent.getRawY();
        if (this.mCallbacks.canDismiss(this.mToken)) {
            (this.mVelocityTracker = VelocityTracker.obtain()).addMovement(motionEvent);
        }
        return false;
    }
    
    public void performDismiss() {
        final ViewGroup$LayoutParams layoutParams = this.mView.getLayoutParams();
        final int height = this.mView.getHeight();
        final ValueAnimator setDuration = ValueAnimator.ofInt(new int[] { height, 1 }).setDuration(this.mAnimationTime);
        setDuration.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                SwipeDismissTouchListener.this.mCallbacks.onDismiss(SwipeDismissTouchListener.this.mView, SwipeDismissTouchListener.this.mToken);
                SwipeDismissTouchListener.this.mView.setAlpha(1.0f);
                SwipeDismissTouchListener.this.mView.setTranslationX(0.0f);
                layoutParams.height = height;
                SwipeDismissTouchListener.this.mView.setLayoutParams(layoutParams);
            }
        });
        setDuration.addUpdateListener((ValueAnimator$AnimatorUpdateListener)new ValueAnimator$AnimatorUpdateListener() {
            public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                layoutParams.height = (int)valueAnimator.getAnimatedValue();
                SwipeDismissTouchListener.this.mView.setLayoutParams(layoutParams);
            }
        });
        setDuration.start();
    }
    
    public interface DismissCallbacks
    {
        boolean canDismiss(final Object p0);
        
        void onDismiss(final View p0, final Object p1);
    }
}
