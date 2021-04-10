package com.microsoft.xbox.toolkit.ui;

import android.graphics.drawable.*;
import android.view.*;

public class ButtonStateHandler
{
    protected boolean disabled;
    private XLEBitmap.XLEBitmapDrawable disabledImage;
    private int disabledImageHandle;
    private XLEBitmap.XLEBitmapDrawable enabledImage;
    private int enabledImageHandle;
    protected boolean pressed;
    private XLEBitmap.XLEBitmapDrawable pressedImage;
    private int pressedImageHandle;
    private ButtonStateHandlerRunnable pressedStateRunnable;
    
    public ButtonStateHandler() {
        this.disabled = false;
        this.pressed = false;
        this.disabledImageHandle = -1;
        this.enabledImageHandle = -1;
        this.pressedImageHandle = -1;
        this.disabledImage = null;
        this.enabledImage = null;
        this.pressedImage = null;
        this.pressedStateRunnable = null;
    }
    
    public boolean getDisabled() {
        return this.disabled;
    }
    
    public Drawable getImageDrawable() {
        final boolean pressed = this.pressed;
        Object drawable = null;
        if (pressed && this.pressedImageHandle != -1) {
            final XLEBitmap.XLEBitmapDrawable pressedImage = this.pressedImage;
            if (pressedImage == null) {
                return null;
            }
            return (Drawable)pressedImage.getDrawable();
        }
        else {
            if (!this.disabled || this.disabledImageHandle == -1) {
                if (this.enabledImageHandle != -1) {
                    final XLEBitmap.XLEBitmapDrawable enabledImage = this.enabledImage;
                    if (enabledImage == null) {
                        return null;
                    }
                    drawable = enabledImage.getDrawable();
                }
                return (Drawable)drawable;
            }
            final XLEBitmap.XLEBitmapDrawable disabledImage = this.disabledImage;
            if (disabledImage == null) {
                return null;
            }
            return (Drawable)disabledImage.getDrawable();
        }
    }
    
    public boolean onSizeChanged(final int n, final int n2) {
        boolean b;
        if (this.disabledImage == null && this.disabledImageHandle != -1) {
            this.disabledImage = TextureManager.Instance().loadScaledResourceDrawable(this.disabledImageHandle);
            b = true;
        }
        else {
            b = false;
        }
        boolean b2 = b;
        if (this.enabledImage == null) {
            b2 = b;
            if (this.enabledImageHandle != -1) {
                this.enabledImage = TextureManager.Instance().loadScaledResourceDrawable(this.enabledImageHandle);
                b2 = true;
            }
        }
        if (this.pressedImage == null && this.pressedImageHandle != -1) {
            this.pressedImage = TextureManager.Instance().loadScaledResourceDrawable(this.pressedImageHandle);
            return true;
        }
        return b2;
    }
    
    public boolean onTouch(final MotionEvent motionEvent) {
        final boolean pressed = this.pressed;
        if (motionEvent.getAction() == 0) {
            this.pressed = true;
        }
        else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
            this.pressed = false;
        }
        final ButtonStateHandlerRunnable pressedStateRunnable = this.pressedStateRunnable;
        if (pressedStateRunnable != null) {
            final boolean pressed2 = this.pressed;
            if (pressed != pressed2) {
                pressedStateRunnable.onPressStateChanged(pressed2);
            }
        }
        return false;
    }
    
    public void setDisabled(final boolean disabled) {
        this.disabled = disabled;
    }
    
    public void setDisabledImageHandle(final int disabledImageHandle) {
        this.disabledImageHandle = disabledImageHandle;
    }
    
    public void setEnabled(final boolean b) {
        this.disabled = (b ^ true);
    }
    
    public void setEnabledImageHandle(final int enabledImageHandle) {
        this.enabledImageHandle = enabledImageHandle;
    }
    
    public void setPressedImageHandle(final int pressedImageHandle) {
        this.pressedImageHandle = pressedImageHandle;
    }
    
    public void setPressedStateRunnable(final ButtonStateHandlerRunnable pressedStateRunnable) {
        this.pressedStateRunnable = pressedStateRunnable;
    }
    
    public interface ButtonStateHandlerRunnable
    {
        void onPressStateChanged(final boolean p0);
    }
}
