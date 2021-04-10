package android.support.v4.animation;

import android.view.*;
import java.util.*;

class DonutAnimatorCompatProvider implements AnimatorProvider
{
    @Override
    public void clearInterpolator(final View view) {
    }
    
    @Override
    public ValueAnimatorCompat emptyValueAnimator() {
        return new DonutFloatValueAnimator();
    }
    
    private static class DonutFloatValueAnimator implements ValueAnimatorCompat
    {
        private long mDuration;
        private boolean mEnded;
        private float mFraction;
        List<AnimatorListenerCompat> mListeners;
        private Runnable mLoopRunnable;
        private long mStartTime;
        private boolean mStarted;
        View mTarget;
        List<AnimatorUpdateListenerCompat> mUpdateListeners;
        
        public DonutFloatValueAnimator() {
            this.mListeners = new ArrayList<AnimatorListenerCompat>();
            this.mUpdateListeners = new ArrayList<AnimatorUpdateListenerCompat>();
            this.mDuration = 200L;
            this.mFraction = 0.0f;
            this.mStarted = false;
            this.mEnded = false;
            this.mLoopRunnable = new Runnable() {
                @Override
                public void run() {
                    float n = (DonutFloatValueAnimator.this.getTime() - DonutFloatValueAnimator.this.mStartTime) * 1.0f / DonutFloatValueAnimator.this.mDuration;
                    if (n > 1.0f || DonutFloatValueAnimator.this.mTarget.getParent() == null) {
                        n = 1.0f;
                    }
                    DonutFloatValueAnimator.this.mFraction = n;
                    DonutFloatValueAnimator.this.notifyUpdateListeners();
                    if (DonutFloatValueAnimator.this.mFraction >= 1.0f) {
                        DonutFloatValueAnimator.this.dispatchEnd();
                        return;
                    }
                    DonutFloatValueAnimator.this.mTarget.postDelayed(DonutFloatValueAnimator.this.mLoopRunnable, 16L);
                }
            };
        }
        
        private void dispatchCancel() {
            int size = this.mListeners.size();
            while (true) {
                --size;
                if (size < 0) {
                    break;
                }
                this.mListeners.get(size).onAnimationCancel(this);
            }
        }
        
        private void dispatchEnd() {
            int size = this.mListeners.size();
            while (true) {
                --size;
                if (size < 0) {
                    break;
                }
                this.mListeners.get(size).onAnimationEnd(this);
            }
        }
        
        private void dispatchStart() {
            int size = this.mListeners.size();
            while (true) {
                --size;
                if (size < 0) {
                    break;
                }
                this.mListeners.get(size).onAnimationStart(this);
            }
        }
        
        private long getTime() {
            return this.mTarget.getDrawingTime();
        }
        
        private void notifyUpdateListeners() {
            int size = this.mUpdateListeners.size();
            while (true) {
                --size;
                if (size < 0) {
                    break;
                }
                this.mUpdateListeners.get(size).onAnimationUpdate(this);
            }
        }
        
        @Override
        public void addListener(final AnimatorListenerCompat animatorListenerCompat) {
            this.mListeners.add(animatorListenerCompat);
        }
        
        @Override
        public void addUpdateListener(final AnimatorUpdateListenerCompat animatorUpdateListenerCompat) {
            this.mUpdateListeners.add(animatorUpdateListenerCompat);
        }
        
        @Override
        public void cancel() {
            if (this.mEnded) {
                return;
            }
            this.mEnded = true;
            if (this.mStarted) {
                this.dispatchCancel();
            }
            this.dispatchEnd();
        }
        
        @Override
        public float getAnimatedFraction() {
            return this.mFraction;
        }
        
        @Override
        public void setDuration(final long mDuration) {
            if (!this.mStarted) {
                this.mDuration = mDuration;
            }
        }
        
        @Override
        public void setTarget(final View mTarget) {
            this.mTarget = mTarget;
        }
        
        @Override
        public void start() {
            if (this.mStarted) {
                return;
            }
            this.mStarted = true;
            this.dispatchStart();
            this.mFraction = 0.0f;
            this.mStartTime = this.getTime();
            this.mTarget.postDelayed(this.mLoopRunnable, 16L);
        }
    }
}
