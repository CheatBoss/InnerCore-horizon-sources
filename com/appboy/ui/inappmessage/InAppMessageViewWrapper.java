package com.appboy.ui.inappmessage;

import com.appboy.support.*;
import java.util.*;
import com.appboy.ui.inappmessage.views.*;
import com.appboy.ui.support.*;
import android.view.animation.*;
import com.appboy.models.*;
import com.appboy.ui.inappmessage.listeners.*;
import android.widget.*;
import com.appboy.enums.inappmessage.*;
import android.app.*;
import android.view.*;

public class InAppMessageViewWrapper implements IInAppMessageViewWrapper
{
    private static final String TAG;
    private List<View> mButtons;
    private View mClickableInAppMessageView;
    private View mCloseButton;
    private final Animation mClosingAnimation;
    private Runnable mDismissRunnable;
    private final IInAppMessage mInAppMessage;
    private final View mInAppMessageView;
    private final IInAppMessageViewLifecycleListener mInAppMessageViewLifecycleListener;
    private boolean mIsAnimatingClose;
    private final Animation mOpeningAnimation;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(InAppMessageViewWrapper.class);
    }
    
    public InAppMessageViewWrapper(final View view, final IInAppMessage mInAppMessage, final IInAppMessageViewLifecycleListener mInAppMessageViewLifecycleListener, final Animation mOpeningAnimation, final Animation mClosingAnimation, final View mClickableInAppMessageView) {
        this.mInAppMessageView = view;
        this.mInAppMessage = mInAppMessage;
        this.mInAppMessageViewLifecycleListener = mInAppMessageViewLifecycleListener;
        this.mIsAnimatingClose = false;
        if (mClickableInAppMessageView != null) {
            this.mClickableInAppMessageView = mClickableInAppMessageView;
        }
        else {
            this.mClickableInAppMessageView = view;
        }
        if (this.mInAppMessage instanceof InAppMessageSlideup) {
            final TouchAwareSwipeDismissTouchListener onTouchListener = new TouchAwareSwipeDismissTouchListener(view, null, this.createDismissCallbacks());
            onTouchListener.setTouchListener(this.createTouchAwareListener());
            this.mClickableInAppMessageView.setOnTouchListener((View$OnTouchListener)onTouchListener);
        }
        this.mOpeningAnimation = mOpeningAnimation;
        this.mClosingAnimation = mClosingAnimation;
        this.mClickableInAppMessageView.setOnClickListener(this.createClickListener());
    }
    
    public InAppMessageViewWrapper(final View view, final IInAppMessage inAppMessage, final IInAppMessageViewLifecycleListener inAppMessageViewLifecycleListener, final Animation animation, final Animation animation2, final View view2, final List<View> mButtons, final View mCloseButton) {
        this(view, inAppMessage, inAppMessageViewLifecycleListener, animation, animation2, view2);
        if (mCloseButton != null) {
            (this.mCloseButton = mCloseButton).setOnClickListener(this.createCloseInAppMessageClickListener());
        }
        if (mButtons != null) {
            this.mButtons = mButtons;
            final Iterator<View> iterator = mButtons.iterator();
            while (iterator.hasNext()) {
                iterator.next().setOnClickListener(this.createButtonClickListener());
            }
        }
    }
    
    private void addDismissRunnable() {
        if (this.mDismissRunnable == null) {
            final Runnable mDismissRunnable = new Runnable() {
                @Override
                public void run() {
                    AppboyInAppMessageManager.getInstance().hideCurrentlyDisplayingInAppMessage(true);
                }
            };
            this.mDismissRunnable = mDismissRunnable;
            this.mInAppMessageView.postDelayed((Runnable)mDismissRunnable, (long)this.mInAppMessage.getDurationInMilliseconds());
        }
    }
    
    private void announceForAccessibilityIfNecessary() {
        final View mInAppMessageView = this.mInAppMessageView;
        if (mInAppMessageView instanceof IInAppMessageImmersiveView) {
            mInAppMessageView.announceForAccessibility((CharSequence)this.mInAppMessage.getMessage());
            return;
        }
        if (mInAppMessageView instanceof AppboyInAppMessageHtmlBaseView) {
            mInAppMessageView.announceForAccessibility((CharSequence)"In-app message displayed.");
        }
    }
    
    private void closeInAppMessageView() {
        AppboyLogger.d(InAppMessageViewWrapper.TAG, "Closing in-app message view");
        ViewUtils.removeViewFromParent(this.mInAppMessageView);
        final View mInAppMessageView = this.mInAppMessageView;
        if (mInAppMessageView instanceof AppboyInAppMessageHtmlBaseView) {
            final AppboyInAppMessageHtmlBaseView appboyInAppMessageHtmlBaseView = (AppboyInAppMessageHtmlBaseView)mInAppMessageView;
            if (appboyInAppMessageHtmlBaseView.getMessageWebView() != null) {
                AppboyLogger.d(InAppMessageViewWrapper.TAG, "Called destroy on the AppboyInAppMessageHtmlBaseView WebView");
                appboyInAppMessageHtmlBaseView.getMessageWebView().destroy();
            }
        }
        this.mInAppMessageViewLifecycleListener.afterClosed(this.mInAppMessage);
    }
    
    private Animation$AnimationListener createAnimationListener(final boolean b) {
        if (b) {
            return (Animation$AnimationListener)new Animation$AnimationListener() {
                public void onAnimationEnd(final Animation animation) {
                    if (InAppMessageViewWrapper.this.mInAppMessage.getDismissType() == DismissType.AUTO_DISMISS) {
                        InAppMessageViewWrapper.this.addDismissRunnable();
                    }
                    AppboyLogger.d(InAppMessageViewWrapper.TAG, "In-app message animated into view.");
                    ViewUtils.setFocusableInTouchModeAndRequestFocus(InAppMessageViewWrapper.this.mInAppMessageView);
                    InAppMessageViewWrapper.this.announceForAccessibilityIfNecessary();
                    InAppMessageViewWrapper.this.mInAppMessageViewLifecycleListener.afterOpened(InAppMessageViewWrapper.this.mInAppMessageView, InAppMessageViewWrapper.this.mInAppMessage);
                }
                
                public void onAnimationRepeat(final Animation animation) {
                }
                
                public void onAnimationStart(final Animation animation) {
                }
            };
        }
        return (Animation$AnimationListener)new Animation$AnimationListener() {
            public void onAnimationEnd(final Animation animation) {
                InAppMessageViewWrapper.this.mInAppMessageView.clearAnimation();
                InAppMessageViewWrapper.this.mInAppMessageView.setVisibility(8);
                InAppMessageViewWrapper.this.closeInAppMessageView();
            }
            
            public void onAnimationRepeat(final Animation animation) {
            }
            
            public void onAnimationStart(final Animation animation) {
            }
        };
    }
    
    private View$OnClickListener createButtonClickListener() {
        return (View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                final IInAppMessageImmersive inAppMessageImmersive = (IInAppMessageImmersive)InAppMessageViewWrapper.this.mInAppMessage;
                for (int i = 0; i < InAppMessageViewWrapper.this.mButtons.size(); ++i) {
                    if (view.getId() == ((View)InAppMessageViewWrapper.this.mButtons.get(i)).getId()) {
                        InAppMessageViewWrapper.this.mInAppMessageViewLifecycleListener.onButtonClicked(new InAppMessageCloser(InAppMessageViewWrapper.this), inAppMessageImmersive.getMessageButtons().get(i), inAppMessageImmersive);
                        return;
                    }
                }
            }
        };
    }
    
    private View$OnClickListener createClickListener() {
        return (View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                if (InAppMessageViewWrapper.this.mInAppMessage instanceof IInAppMessageImmersive) {
                    final IInAppMessageImmersive inAppMessageImmersive = (IInAppMessageImmersive)InAppMessageViewWrapper.this.mInAppMessage;
                    if (inAppMessageImmersive.getMessageButtons() == null || inAppMessageImmersive.getMessageButtons().size() == 0) {
                        InAppMessageViewWrapper.this.mInAppMessageViewLifecycleListener.onClicked(new InAppMessageCloser(InAppMessageViewWrapper.this), InAppMessageViewWrapper.this.mInAppMessageView, InAppMessageViewWrapper.this.mInAppMessage);
                    }
                }
                else {
                    InAppMessageViewWrapper.this.mInAppMessageViewLifecycleListener.onClicked(new InAppMessageCloser(InAppMessageViewWrapper.this), InAppMessageViewWrapper.this.mInAppMessageView, InAppMessageViewWrapper.this.mInAppMessage);
                }
            }
        };
    }
    
    private View$OnClickListener createCloseInAppMessageClickListener() {
        return (View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                AppboyInAppMessageManager.getInstance().hideCurrentlyDisplayingInAppMessage(true);
            }
        };
    }
    
    private SwipeDismissTouchListener.DismissCallbacks createDismissCallbacks() {
        return new SwipeDismissTouchListener.DismissCallbacks() {
            @Override
            public boolean canDismiss(final Object o) {
                return true;
            }
            
            @Override
            public void onDismiss(final View view, final Object o) {
                InAppMessageViewWrapper.this.mInAppMessage.setAnimateOut(false);
                AppboyInAppMessageManager.getInstance().hideCurrentlyDisplayingInAppMessage(true);
            }
        };
    }
    
    private TouchAwareSwipeDismissTouchListener.ITouchListener createTouchAwareListener() {
        return new TouchAwareSwipeDismissTouchListener.ITouchListener() {
            @Override
            public void onTouchEnded() {
                if (InAppMessageViewWrapper.this.mInAppMessage.getDismissType() == DismissType.AUTO_DISMISS) {
                    InAppMessageViewWrapper.this.addDismissRunnable();
                }
            }
            
            @Override
            public void onTouchStartedOrContinued() {
                InAppMessageViewWrapper.this.mInAppMessageView.removeCallbacks(InAppMessageViewWrapper.this.mDismissRunnable);
            }
        };
    }
    
    private FrameLayout$LayoutParams getLayoutParams(final FrameLayout frameLayout, int topVisibleCoordinate) {
        final FrameLayout$LayoutParams frameLayout$LayoutParams = new FrameLayout$LayoutParams(-1, -2);
        final IInAppMessage mInAppMessage = this.mInAppMessage;
        if (mInAppMessage instanceof InAppMessageSlideup) {
            int gravity;
            if (((InAppMessageSlideup)mInAppMessage).getSlideFrom() == SlideFrom.TOP) {
                gravity = 48;
            }
            else {
                gravity = 80;
            }
            frameLayout$LayoutParams.gravity = gravity;
        }
        if (topVisibleCoordinate > 0 && topVisibleCoordinate == frameLayout.getHeight()) {
            topVisibleCoordinate = ViewUtils.getTopVisibleCoordinate((View)frameLayout);
            final String tag = InAppMessageViewWrapper.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("Detected status bar height of ");
            sb.append(topVisibleCoordinate);
            sb.append(".");
            AppboyLogger.d(tag, sb.toString());
            frameLayout$LayoutParams.setMargins(0, topVisibleCoordinate, 0, 0);
        }
        return frameLayout$LayoutParams;
    }
    
    private void open(final FrameLayout frameLayout, final int n) {
        this.mInAppMessageViewLifecycleListener.beforeOpened(this.mInAppMessageView, this.mInAppMessage);
        AppboyLogger.d(InAppMessageViewWrapper.TAG, "Adding In-app message view to root FrameLayout.");
        frameLayout.addView(this.mInAppMessageView, (ViewGroup$LayoutParams)this.getLayoutParams(frameLayout, n));
        if (this.mInAppMessage.getAnimateIn()) {
            AppboyLogger.d(InAppMessageViewWrapper.TAG, "In-app message view will animate into the visible area.");
            this.setAndStartAnimation(true);
            return;
        }
        AppboyLogger.d(InAppMessageViewWrapper.TAG, "In-app message view will be placed instantly into the visible area.");
        if (this.mInAppMessage.getDismissType() == DismissType.AUTO_DISMISS) {
            this.addDismissRunnable();
        }
        ViewUtils.setFocusableInTouchModeAndRequestFocus(this.mInAppMessageView);
        this.announceForAccessibilityIfNecessary();
        this.mInAppMessageViewLifecycleListener.afterOpened(this.mInAppMessageView, this.mInAppMessage);
    }
    
    private void setAndStartAnimation(final boolean b) {
        Animation animation;
        if (b) {
            animation = this.mOpeningAnimation;
        }
        else {
            animation = this.mClosingAnimation;
        }
        animation.setAnimationListener(this.createAnimationListener(b));
        this.mInAppMessageView.clearAnimation();
        this.mInAppMessageView.setAnimation(animation);
        animation.startNow();
        this.mInAppMessageView.invalidate();
    }
    
    @Override
    public void close() {
        this.mInAppMessageView.removeCallbacks(this.mDismissRunnable);
        this.mInAppMessageViewLifecycleListener.beforeClosed(this.mInAppMessageView, this.mInAppMessage);
        if (this.mInAppMessage.getAnimateOut()) {
            this.mIsAnimatingClose = true;
            this.setAndStartAnimation(false);
            return;
        }
        this.closeInAppMessageView();
    }
    
    @Override
    public IInAppMessage getInAppMessage() {
        return this.mInAppMessage;
    }
    
    @Override
    public View getInAppMessageView() {
        return this.mInAppMessageView;
    }
    
    @Override
    public boolean getIsAnimatingClose() {
        return this.mIsAnimatingClose;
    }
    
    @Override
    public void open(final Activity activity) {
        final FrameLayout frameLayout = (FrameLayout)activity.getWindow().getDecorView().findViewById(16908290);
        final int height = frameLayout.getHeight();
        final int displayHeight = ViewUtils.getDisplayHeight(activity);
        if (height == 0) {
            final ViewTreeObserver viewTreeObserver = frameLayout.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)new ViewTreeObserver$OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        final String access$000 = InAppMessageViewWrapper.TAG;
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Detected root view height of ");
                        sb.append(frameLayout.getHeight());
                        sb.append(", display height of ");
                        sb.append(displayHeight);
                        sb.append(" in onGlobalLayout");
                        AppboyLogger.d(access$000, sb.toString());
                        frameLayout.removeView(InAppMessageViewWrapper.this.mInAppMessageView);
                        InAppMessageViewWrapper.this.open(frameLayout, displayHeight);
                        frameLayout.getViewTreeObserver().removeOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
                    }
                });
            }
        }
        else {
            final String tag = InAppMessageViewWrapper.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("Detected root view height of ");
            sb.append(height);
            sb.append(", display height of ");
            sb.append(displayHeight);
            AppboyLogger.d(tag, sb.toString());
            this.open(frameLayout, displayHeight);
        }
    }
}
