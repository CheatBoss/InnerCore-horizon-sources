package com.appboy.ui.inappmessage.listeners;

import android.view.*;

public class TouchAwareSwipeDismissTouchListener extends SwipeDismissTouchListener
{
    private ITouchListener mTouchListener;
    
    public TouchAwareSwipeDismissTouchListener(final View view, final Object o, final DismissCallbacks dismissCallbacks) {
        super(view, o, dismissCallbacks);
    }
    
    @Override
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        final int action = motionEvent.getAction();
        if (action != 0) {
            if (action == 1 || action == 3) {
                final ITouchListener mTouchListener = this.mTouchListener;
                if (mTouchListener != null) {
                    mTouchListener.onTouchEnded();
                }
            }
        }
        else {
            final ITouchListener mTouchListener2 = this.mTouchListener;
            if (mTouchListener2 != null) {
                mTouchListener2.onTouchStartedOrContinued();
            }
        }
        return super.onTouch(view, motionEvent);
    }
    
    public void setTouchListener(final ITouchListener mTouchListener) {
        this.mTouchListener = mTouchListener;
    }
    
    public interface ITouchListener
    {
        void onTouchEnded();
        
        void onTouchStartedOrContinued();
    }
}
