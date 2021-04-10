package androidx.core.view;

import android.view.*;
import androidx.annotation.*;

public final class OneShotPreDrawListener implements ViewTreeObserver$OnPreDrawListener, View$OnAttachStateChangeListener
{
    private final Runnable mRunnable;
    private final View mView;
    private ViewTreeObserver mViewTreeObserver;
    
    private OneShotPreDrawListener(final View mView, final Runnable mRunnable) {
        this.mView = mView;
        this.mViewTreeObserver = mView.getViewTreeObserver();
        this.mRunnable = mRunnable;
    }
    
    @NonNull
    public static OneShotPreDrawListener add(@NonNull final View view, @NonNull final Runnable runnable) {
        if (view == null) {
            throw new NullPointerException("view == null");
        }
        if (runnable == null) {
            throw new NullPointerException("runnable == null");
        }
        final OneShotPreDrawListener oneShotPreDrawListener = new OneShotPreDrawListener(view, runnable);
        view.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)oneShotPreDrawListener);
        view.addOnAttachStateChangeListener((View$OnAttachStateChangeListener)oneShotPreDrawListener);
        return oneShotPreDrawListener;
    }
    
    public boolean onPreDraw() {
        this.removeListener();
        this.mRunnable.run();
        return true;
    }
    
    public void onViewAttachedToWindow(final View view) {
        this.mViewTreeObserver = view.getViewTreeObserver();
    }
    
    public void onViewDetachedFromWindow(final View view) {
        this.removeListener();
    }
    
    public void removeListener() {
        if (this.mViewTreeObserver.isAlive()) {
            this.mViewTreeObserver.removeOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this);
        }
        else {
            this.mView.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this);
        }
        this.mView.removeOnAttachStateChangeListener((View$OnAttachStateChangeListener)this);
    }
}
