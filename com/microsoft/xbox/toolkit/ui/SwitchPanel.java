package com.microsoft.xbox.toolkit.ui;

import android.content.*;
import android.util.*;
import android.widget.*;
import android.view.*;
import android.content.res.*;
import android.graphics.*;
import android.animation.*;
import com.microsoft.xbox.toolkit.*;

public class SwitchPanel extends LinearLayout
{
    private static final int LAYOUT_BLOCK_TIMEOUT_MS = 150;
    private AnimatorListenerAdapter AnimateInListener;
    private AnimatorListenerAdapter AnimateOutListener;
    private final int INVALID_STATE_ID;
    private final int VALID_CONTENT_STATE;
    private boolean active;
    private boolean blocking;
    private View newView;
    private View oldView;
    private int selectedState;
    private boolean shouldAnimate;
    
    public SwitchPanel(final Context context) {
        super(context);
        this.INVALID_STATE_ID = -1;
        this.VALID_CONTENT_STATE = 0;
        this.blocking = false;
        this.active = false;
        this.oldView = null;
        this.newView = null;
        this.shouldAnimate = true;
        this.AnimateInListener = new AnimatorListenerAdapter() {
            public void onAnimationCancel(final Animator animator) {
                SwitchPanel.this.onAnimateInEnd();
            }
            
            public void onAnimationEnd(final Animator animator) {
                SwitchPanel.this.onAnimateInEnd();
            }
            
            public void onAnimationStart(final Animator animator) {
                SwitchPanel.this.onAnimateInStart();
            }
        };
        this.AnimateOutListener = new AnimatorListenerAdapter() {
            public void onAnimationCancel(final Animator animator) {
                SwitchPanel.this.onAnimateOutEnd();
            }
            
            public void onAnimationEnd(final Animator animator) {
                SwitchPanel.this.onAnimateOutEnd();
            }
            
            public void onAnimationStart(final Animator animator) {
                SwitchPanel.this.onAnimateOutStart();
            }
        };
        throw new UnsupportedOperationException();
    }
    
    public SwitchPanel(final Context context, final AttributeSet set) {
        super(context, set);
        this.INVALID_STATE_ID = -1;
        this.VALID_CONTENT_STATE = 0;
        this.blocking = false;
        this.active = false;
        this.oldView = null;
        this.newView = null;
        this.shouldAnimate = true;
        this.AnimateInListener = new AnimatorListenerAdapter() {
            public void onAnimationCancel(final Animator animator) {
                SwitchPanel.this.onAnimateInEnd();
            }
            
            public void onAnimationEnd(final Animator animator) {
                SwitchPanel.this.onAnimateInEnd();
            }
            
            public void onAnimationStart(final Animator animator) {
                SwitchPanel.this.onAnimateInStart();
            }
        };
        this.AnimateOutListener = new AnimatorListenerAdapter() {
            public void onAnimationCancel(final Animator animator) {
                SwitchPanel.this.onAnimateOutEnd();
            }
            
            public void onAnimationEnd(final Animator animator) {
                SwitchPanel.this.onAnimateOutEnd();
            }
            
            public void onAnimationStart(final Animator animator) {
                SwitchPanel.this.onAnimateOutStart();
            }
        };
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, XLERValueHelper.getStyleableRValueArray("SwitchPanel"));
        this.selectedState = obtainStyledAttributes.getInteger(XLERValueHelper.getStyleableRValue("SwitchPanel_selectedState"), -1);
        obtainStyledAttributes.recycle();
        if (this.selectedState >= 0) {
            this.setLayoutParams((ViewGroup$LayoutParams)new RelativeLayout$LayoutParams(-1, -1));
            return;
        }
        throw new IllegalArgumentException("You must specify the selectedState attribute in the xml, and the value must be positive.");
    }
    
    private void onAnimateInEnd() {
        this.setBlocking(false);
        final View newView = this.newView;
        if (newView != null) {
            newView.setLayerType(0, (Paint)null);
        }
    }
    
    private void onAnimateInStart() {
        final View newView = this.newView;
        if (newView != null) {
            newView.setLayerType(2, (Paint)null);
            this.setBlocking(true);
        }
    }
    
    private void onAnimateOutEnd() {
        final View oldView = this.oldView;
        if (oldView != null) {
            oldView.setVisibility(8);
            this.oldView.setLayerType(0, (Paint)null);
        }
    }
    
    private void onAnimateOutStart() {
        final View oldView = this.oldView;
        if (oldView != null) {
            oldView.setLayerType(2, (Paint)null);
            this.setBlocking(true);
        }
    }
    
    private void updateVisibility(final int n, final int n2) {
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (!(child instanceof SwitchPanelChild)) {
                throw new UnsupportedOperationException("All children of SwitchPanel must implement the SwitchPanelChild interface. All other types are not supported and should be removed.");
            }
            final int state = ((SwitchPanelChild)child).getState();
            if (state == n) {
                this.oldView = child;
            }
            else if (state == n2) {
                this.newView = child;
            }
            else {
                child.setVisibility(8);
            }
        }
        if (this.shouldAnimate && n2 == 0) {
            final View newView = this.newView;
            if (newView != null) {
                newView.setAlpha(0.0f);
                this.newView.setVisibility(0);
                this.requestLayout();
                final View oldView = this.oldView;
                if (oldView != null) {
                    oldView.animate().alpha(0.0f).setDuration(150L).setListener((Animator$AnimatorListener)this.AnimateOutListener);
                }
                this.newView.animate().alpha(1.0f).setDuration(150L).setListener((Animator$AnimatorListener)this.AnimateInListener);
                return;
            }
        }
        final View oldView2 = this.oldView;
        if (oldView2 != null) {
            oldView2.setVisibility(8);
        }
        final View newView2 = this.newView;
        if (newView2 != null) {
            newView2.setAlpha(1.0f);
            this.newView.setVisibility(0);
        }
        this.requestLayout();
    }
    
    public int getState() {
        return this.selectedState;
    }
    
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.updateVisibility(-1, this.selectedState);
    }
    
    public void setActive(final boolean active) {
        this.active = active;
    }
    
    public void setBlocking(final boolean blocking) {
        if (this.blocking != blocking) {
            this.blocking = blocking;
            if (blocking) {
                BackgroundThreadWaitor.getInstance().setBlocking(BackgroundThreadWaitor.WaitType.ListLayout, 150);
                return;
            }
            BackgroundThreadWaitor.getInstance().clearBlocking(BackgroundThreadWaitor.WaitType.ListLayout);
        }
    }
    
    public void setShouldAnimate(final boolean shouldAnimate) {
        this.shouldAnimate = shouldAnimate;
    }
    
    public void setState(final int selectedState) {
        if (selectedState >= 0) {
            final int selectedState2 = this.selectedState;
            if (selectedState2 != selectedState) {
                this.updateVisibility(selectedState2, this.selectedState = selectedState);
            }
            return;
        }
        throw new IllegalArgumentException("New state must be a positive value.");
    }
    
    public interface SwitchPanelChild
    {
        int getState();
    }
}
