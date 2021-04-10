package com.mojang.minecraftpe;

import android.content.*;
import android.os.*;
import android.view.*;

public class PopupView
{
    private View mContentView;
    private Context mContext;
    private int mHeight;
    private int mOriginX;
    private int mOriginY;
    private View mParentView;
    private View mPopupView;
    private int mWidth;
    private WindowManager mWindowManager;
    
    public PopupView(final Context mContext) {
        this.mContext = mContext;
        this.mWindowManager = (WindowManager)mContext.getSystemService("window");
    }
    
    private void addPopupView() {
        this.mPopupView = this.mContentView;
        final WindowManager$LayoutParams popupLayout = this.createPopupLayout(this.mParentView.getWindowToken());
        this.setLayoutRect(popupLayout);
        this.invokePopup(popupLayout);
    }
    
    private int computeFlags(final int n) {
        return n | 0x20;
    }
    
    private WindowManager$LayoutParams createPopupLayout(final IBinder token) {
        final WindowManager$LayoutParams windowManager$LayoutParams = new WindowManager$LayoutParams();
        windowManager$LayoutParams.format = -3;
        windowManager$LayoutParams.flags = this.computeFlags(windowManager$LayoutParams.flags);
        windowManager$LayoutParams.type = 1000;
        windowManager$LayoutParams.token = token;
        windowManager$LayoutParams.softInputMode = 1;
        final StringBuilder sb = new StringBuilder();
        sb.append("PopupWindow:");
        sb.append(Integer.toHexString(this.hashCode()));
        windowManager$LayoutParams.setTitle((CharSequence)sb.toString());
        windowManager$LayoutParams.windowAnimations = -1;
        return windowManager$LayoutParams;
    }
    
    private void invokePopup(final WindowManager$LayoutParams windowManager$LayoutParams) {
        windowManager$LayoutParams.packageName = this.mContext.getPackageName();
        this.mWindowManager.addView(this.mPopupView, (ViewGroup$LayoutParams)windowManager$LayoutParams);
    }
    
    private void preparePopup(final WindowManager$LayoutParams windowManager$LayoutParams) {
    }
    
    private void removePopupView() {
        try {
            this.mWindowManager.removeView(this.mPopupView);
        }
        catch (Exception ex) {}
    }
    
    private void setLayoutRect(final WindowManager$LayoutParams windowManager$LayoutParams) {
        windowManager$LayoutParams.width = this.mWidth;
        windowManager$LayoutParams.height = this.mHeight;
        windowManager$LayoutParams.x = this.mOriginX;
        windowManager$LayoutParams.y = this.mOriginY;
        windowManager$LayoutParams.gravity = 51;
    }
    
    public void dismiss() {
        if (this.mPopupView != null) {
            this.removePopupView();
            final View mPopupView = this.mPopupView;
            final View mContentView = this.mContentView;
            if (mPopupView != mContentView && mPopupView instanceof ViewGroup) {
                ((ViewGroup)mPopupView).removeView(mContentView);
            }
            this.mPopupView = null;
        }
    }
    
    public boolean getVisible() {
        final View mPopupView = this.mPopupView;
        return mPopupView != null && mPopupView.getParent() != null;
    }
    
    public void setContentView(final View mContentView) {
        this.mContentView = mContentView;
    }
    
    public void setHeight(final int mHeight) {
        this.mHeight = mHeight;
    }
    
    public void setParentView(final View mParentView) {
        this.mParentView = mParentView;
    }
    
    public void setRect(final int mOriginX, final int n, final int mOriginY, final int n2) {
        this.mWidth = n - mOriginX;
        this.mHeight = n2 - mOriginY;
        this.mOriginX = mOriginX;
        this.mOriginY = mOriginY;
    }
    
    public void setVisible(final boolean b) {
        if (b != this.getVisible()) {
            if (b) {
                this.addPopupView();
                return;
            }
            this.removePopupView();
        }
    }
    
    public void setWidth(final int mWidth) {
        this.mWidth = mWidth;
    }
    
    public void update() {
        if (this.getVisible()) {
            final WindowManager$LayoutParams layoutRect = (WindowManager$LayoutParams)this.mPopupView.getLayoutParams();
            final int computeFlags = this.computeFlags(layoutRect.flags);
            if (computeFlags != layoutRect.flags) {
                layoutRect.flags = computeFlags;
            }
            this.setLayoutRect(layoutRect);
            this.mWindowManager.updateViewLayout(this.mPopupView, (ViewGroup$LayoutParams)layoutRect);
        }
    }
}
