package com.bumptech.glide.request.target;

import com.bumptech.glide.request.*;
import android.graphics.*;
import android.os.*;
import android.annotation.*;
import android.view.*;
import java.util.*;
import java.lang.ref.*;
import android.util.*;

public abstract class ViewTarget<T extends View, Z> extends BaseTarget<Z>
{
    private static final String TAG = "ViewTarget";
    private static boolean isTagUsedAtLeastOnce;
    private static Integer tagId;
    private final SizeDeterminer sizeDeterminer;
    protected final T view;
    
    static {
        ViewTarget.isTagUsedAtLeastOnce = false;
        ViewTarget.tagId = null;
    }
    
    public ViewTarget(final T view) {
        if (view == null) {
            throw new NullPointerException("View must not be null!");
        }
        this.view = view;
        this.sizeDeterminer = new SizeDeterminer(view);
    }
    
    private Object getTag() {
        if (ViewTarget.tagId == null) {
            return this.view.getTag();
        }
        return this.view.getTag((int)ViewTarget.tagId);
    }
    
    private void setTag(final Object tag) {
        if (ViewTarget.tagId == null) {
            ViewTarget.isTagUsedAtLeastOnce = true;
            this.view.setTag(tag);
            return;
        }
        this.view.setTag((int)ViewTarget.tagId, tag);
    }
    
    public static void setTagId(final int n) {
        if (ViewTarget.tagId == null && !ViewTarget.isTagUsedAtLeastOnce) {
            ViewTarget.tagId = n;
            return;
        }
        throw new IllegalArgumentException("You cannot set the tag id more than once or change the tag id after the first request has been made");
    }
    
    @Override
    public Request getRequest() {
        final Object tag = this.getTag();
        if (tag == null) {
            return null;
        }
        if (tag instanceof Request) {
            return (Request)tag;
        }
        throw new IllegalArgumentException("You must not call setTag() on a view Glide is targeting");
    }
    
    @Override
    public void getSize(final SizeReadyCallback sizeReadyCallback) {
        this.sizeDeterminer.getSize(sizeReadyCallback);
    }
    
    public T getView() {
        return this.view;
    }
    
    @Override
    public void setRequest(final Request tag) {
        this.setTag(tag);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Target for: ");
        sb.append(this.view);
        return sb.toString();
    }
    
    private static class SizeDeterminer
    {
        private static final int PENDING_SIZE = 0;
        private final List<SizeReadyCallback> cbs;
        private Point displayDimens;
        private SizeDeterminerLayoutListener layoutListener;
        private final View view;
        
        public SizeDeterminer(final View view) {
            this.cbs = new ArrayList<SizeReadyCallback>();
            this.view = view;
        }
        
        private void checkCurrentDimens() {
            if (this.cbs.isEmpty()) {
                return;
            }
            final int viewWidthOrParam = this.getViewWidthOrParam();
            final int viewHeightOrParam = this.getViewHeightOrParam();
            if (!this.isSizeValid(viewWidthOrParam)) {
                return;
            }
            if (!this.isSizeValid(viewHeightOrParam)) {
                return;
            }
            this.notifyCbs(viewWidthOrParam, viewHeightOrParam);
            final ViewTreeObserver viewTreeObserver = this.view.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.removeOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this.layoutListener);
            }
            this.layoutListener = null;
        }
        
        @TargetApi(13)
        private Point getDisplayDimens() {
            if (this.displayDimens != null) {
                return this.displayDimens;
            }
            final Display defaultDisplay = ((WindowManager)this.view.getContext().getSystemService("window")).getDefaultDisplay();
            if (Build$VERSION.SDK_INT >= 13) {
                defaultDisplay.getSize(this.displayDimens = new Point());
            }
            else {
                this.displayDimens = new Point(defaultDisplay.getWidth(), defaultDisplay.getHeight());
            }
            return this.displayDimens;
        }
        
        private int getSizeForParam(final int n, final boolean b) {
            if (n != -2) {
                return n;
            }
            final Point displayDimens = this.getDisplayDimens();
            if (b) {
                return displayDimens.y;
            }
            return displayDimens.x;
        }
        
        private int getViewHeightOrParam() {
            final ViewGroup$LayoutParams layoutParams = this.view.getLayoutParams();
            if (this.isSizeValid(this.view.getHeight())) {
                return this.view.getHeight();
            }
            if (layoutParams != null) {
                return this.getSizeForParam(layoutParams.height, true);
            }
            return 0;
        }
        
        private int getViewWidthOrParam() {
            final ViewGroup$LayoutParams layoutParams = this.view.getLayoutParams();
            if (this.isSizeValid(this.view.getWidth())) {
                return this.view.getWidth();
            }
            if (layoutParams != null) {
                return this.getSizeForParam(layoutParams.width, false);
            }
            return 0;
        }
        
        private boolean isSizeValid(final int n) {
            return n > 0 || n == -2;
        }
        
        private void notifyCbs(final int n, final int n2) {
            final Iterator<SizeReadyCallback> iterator = this.cbs.iterator();
            while (iterator.hasNext()) {
                iterator.next().onSizeReady(n, n2);
            }
            this.cbs.clear();
        }
        
        public void getSize(final SizeReadyCallback sizeReadyCallback) {
            final int viewWidthOrParam = this.getViewWidthOrParam();
            final int viewHeightOrParam = this.getViewHeightOrParam();
            if (this.isSizeValid(viewWidthOrParam) && this.isSizeValid(viewHeightOrParam)) {
                sizeReadyCallback.onSizeReady(viewWidthOrParam, viewHeightOrParam);
                return;
            }
            if (!this.cbs.contains(sizeReadyCallback)) {
                this.cbs.add(sizeReadyCallback);
            }
            if (this.layoutListener == null) {
                this.view.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)(this.layoutListener = new SizeDeterminerLayoutListener(this)));
            }
        }
        
        private static class SizeDeterminerLayoutListener implements ViewTreeObserver$OnPreDrawListener
        {
            private final WeakReference<SizeDeterminer> sizeDeterminerRef;
            
            public SizeDeterminerLayoutListener(final SizeDeterminer sizeDeterminer) {
                this.sizeDeterminerRef = new WeakReference<SizeDeterminer>(sizeDeterminer);
            }
            
            public boolean onPreDraw() {
                if (Log.isLoggable("ViewTarget", 2)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("OnGlobalLayoutListener called listener=");
                    sb.append(this);
                    Log.v("ViewTarget", sb.toString());
                }
                final SizeDeterminer sizeDeterminer = this.sizeDeterminerRef.get();
                if (sizeDeterminer != null) {
                    sizeDeterminer.checkCurrentDimens();
                }
                return true;
            }
        }
    }
}
