package com.microsoft.xbox.toolkit.ui;

import android.content.*;
import android.util.*;
import com.microsoft.xbox.toolkit.*;
import android.widget.*;
import android.content.res.*;
import android.view.*;

public class XLEButton extends Button
{
    private boolean alwaysClickable;
    protected boolean disableSound;
    private int disabledTextColor;
    private int enabledTextColor;
    protected ButtonStateHandler stateHandler;
    
    public XLEButton(final Context context) {
        super(context);
        this.stateHandler = new ButtonStateHandler();
        this.setSoundEffectsEnabled(this.disableSound = false);
    }
    
    public XLEButton(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public XLEButton(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.stateHandler = new ButtonStateHandler();
        this.disableSound = false;
        if (this.isInEditMode()) {
            return;
        }
        this.setSoundEffectsEnabled(false);
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, XLERValueHelper.getStyleableRValueArray("XLEButton"));
        try {
            this.stateHandler.setDisabled(obtainStyledAttributes.getBoolean(XLERValueHelper.getStyleableRValue("XLEButton_disabled"), false));
            this.stateHandler.setDisabledImageHandle(obtainStyledAttributes.getResourceId(XLERValueHelper.getStyleableRValue("XLEButton_disabledImage"), -1));
            this.stateHandler.setEnabledImageHandle(obtainStyledAttributes.getResourceId(XLERValueHelper.getStyleableRValue("XLEButton_enabledImage"), -1));
            this.stateHandler.setPressedImageHandle(obtainStyledAttributes.getResourceId(XLERValueHelper.getStyleableRValue("XLEButton_pressedImage"), -1));
            this.disableSound = obtainStyledAttributes.getBoolean(XLERValueHelper.getStyleableRValue("XLEButton_disableSound"), false);
            this.setLayoutParams((ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-2, -2));
            final TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(set, XLERValueHelper.getStyleableRValueArray("CustomTypeface"));
            final String string = obtainStyledAttributes2.getString(XLERValueHelper.getStyleableRValue("CustomTypeface_typefaceSource"));
            obtainStyledAttributes2.recycle();
            if (string != null && string.length() > 0) {
                this.applyCustomTypeface(context, string);
            }
            this.enabledTextColor = this.getCurrentTextColor();
            this.disabledTextColor = obtainStyledAttributes.getColor(XLERValueHelper.getStyleableRValue("XLEButton_disabledTextColor"), this.enabledTextColor);
            if (this.alwaysClickable = obtainStyledAttributes.getBoolean(XLERValueHelper.getStyleableRValue("XLEButton_alwaysClickable"), false)) {
                super.setEnabled(true);
                super.setClickable(true);
            }
        }
        finally {
            obtainStyledAttributes.recycle();
        }
    }
    
    private void applyCustomTypeface(final Context context, final String s) {
        if (s != null) {
            this.setTypeface(FontManager.Instance().getTypeface(this.getContext(), s));
        }
    }
    
    private boolean hasSize() {
        return this.getWidth() > 0 && this.getHeight() > 0;
    }
    
    protected void onFinishInflate() {
        this.updateImage();
        this.setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                final boolean onTouch = XLEButton.this.stateHandler.onTouch(motionEvent);
                XLEButton.this.updateImage();
                return onTouch;
            }
        });
    }
    
    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        if (this.hasSize() && this.stateHandler.onSizeChanged(this.getWidth(), this.getHeight())) {
            this.updateImage();
        }
    }
    
    public void setEnabled(final boolean b) {
        if (!this.alwaysClickable) {
            super.setEnabled(b);
        }
        if (this.stateHandler == null) {
            this.stateHandler = new ButtonStateHandler();
        }
        this.stateHandler.setEnabled(b);
        this.updateImage();
        this.updateTextColor();
    }
    
    public void setOnClickListener(final View$OnClickListener onClickListener) {
        if (this.disableSound) {
            super.setOnClickListener(onClickListener);
            return;
        }
        super.setOnClickListener(TouchUtil.createOnClickListener(onClickListener));
    }
    
    public void setOnLongClickListener(final View$OnLongClickListener onLongClickListener) {
        if (this.disableSound) {
            super.setOnLongClickListener(onLongClickListener);
            return;
        }
        super.setOnLongClickListener(TouchUtil.createOnLongClickListener(onLongClickListener));
    }
    
    public void setPressedStateRunnable(final ButtonStateHandler.ButtonStateHandlerRunnable pressedStateRunnable) {
        this.stateHandler.setPressedStateRunnable(pressedStateRunnable);
    }
    
    public void setTypeFace(final String s) {
        this.applyCustomTypeface(this.getContext(), s);
    }
    
    protected void updateImage() {
        if (this.stateHandler.getImageDrawable() != null) {
            this.setBackgroundDrawable(this.stateHandler.getImageDrawable());
        }
    }
    
    protected void updateTextColor() {
        if (this.enabledTextColor != this.disabledTextColor) {
            int textColor;
            if (this.stateHandler.getDisabled()) {
                textColor = this.disabledTextColor;
            }
            else {
                textColor = this.enabledTextColor;
            }
            this.setTextColor(textColor);
        }
    }
}
