package com.appboy.ui.inappmessage.listeners;

import android.content.*;
import android.view.*;

public class SimpleSwipeDismissTouchListener implements View$OnTouchListener
{
    private final GestureDetector mSwipeGestureListener;
    
    public SimpleSwipeDismissTouchListener(final Context context) {
        this.mSwipeGestureListener = new GestureDetector(context, (GestureDetector$OnGestureListener)new SwipeGestureListener());
    }
    
    public void onSwipeLeft() {
    }
    
    public void onSwipeRight() {
    }
    
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        return this.mSwipeGestureListener.onTouchEvent(motionEvent);
    }
    
    private final class SwipeGestureListener extends GestureDetector$SimpleOnGestureListener
    {
        private static final int SWIPE_DISTANCE_THRESHOLD = 120;
        private static final int SWIPE_VELOCITY_THRESHOLD = 90;
        
        public boolean onFling(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, float n2) {
            n2 = motionEvent2.getX() - motionEvent.getX();
            if (Math.abs(n2) > Math.abs(motionEvent2.getY() - motionEvent.getY()) && Math.abs(n2) > 120.0f && Math.abs(n) > 90.0f) {
                if (n2 > 0.0f) {
                    SimpleSwipeDismissTouchListener.this.onSwipeRight();
                }
                else {
                    SimpleSwipeDismissTouchListener.this.onSwipeLeft();
                }
                return true;
            }
            return false;
        }
    }
}
