package com.google.android.gms.common;

import android.widget.*;
import android.view.*;
import android.content.*;
import com.google.android.gms.base.*;
import com.google.android.gms.common.api.*;
import android.util.*;
import com.google.android.gms.common.internal.*;
import com.google.android.gms.dynamic.*;
import android.content.res.*;

public final class SignInButton extends FrameLayout implements View$OnClickListener
{
    private int mColor;
    private int mSize;
    private View zzbw;
    private View$OnClickListener zzbx;
    
    public SignInButton(final Context context) {
        this(context, null);
    }
    
    public SignInButton(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public SignInButton(Context obtainStyledAttributes, final AttributeSet set, final int n) {
        super(obtainStyledAttributes, set, n);
        this.zzbx = null;
        obtainStyledAttributes = (Context)obtainStyledAttributes.getTheme().obtainStyledAttributes(set, R$styleable.SignInButton, 0, 0);
        try {
            this.mSize = ((TypedArray)obtainStyledAttributes).getInt(R$styleable.SignInButton_buttonSize, 0);
            this.mColor = ((TypedArray)obtainStyledAttributes).getInt(R$styleable.SignInButton_colorScheme, 2);
            ((TypedArray)obtainStyledAttributes).recycle();
            this.setStyle(this.mSize, this.mColor);
        }
        finally {
            ((TypedArray)obtainStyledAttributes).recycle();
        }
    }
    
    public final void onClick(final View view) {
        final View$OnClickListener zzbx = this.zzbx;
        if (zzbx != null && view == this.zzbw) {
            zzbx.onClick((View)this);
        }
    }
    
    public final void setColorScheme(final int n) {
        this.setStyle(this.mSize, n);
    }
    
    public final void setEnabled(final boolean b) {
        super.setEnabled(b);
        this.zzbw.setEnabled(b);
    }
    
    public final void setOnClickListener(final View$OnClickListener zzbx) {
        this.zzbx = zzbx;
        final View zzbw = this.zzbw;
        if (zzbw != null) {
            zzbw.setOnClickListener((View$OnClickListener)this);
        }
    }
    
    @Deprecated
    public final void setScopes(final Scope[] array) {
        this.setStyle(this.mSize, this.mColor);
    }
    
    public final void setSize(final int n) {
        this.setStyle(n, this.mColor);
    }
    
    public final void setStyle(int mSize, int mColor) {
        this.mSize = mSize;
        this.mColor = mColor;
        final Context context = this.getContext();
        final View zzbw = this.zzbw;
        if (zzbw != null) {
            this.removeView(zzbw);
        }
        try {
            this.zzbw = SignInButtonCreator.createView(context, this.mSize, this.mColor);
        }
        catch (RemoteCreator.RemoteCreatorException ex) {
            Log.w("SignInButton", "Sign in button not found, using placeholder instead");
            mSize = this.mSize;
            mColor = this.mColor;
            final SignInButtonImpl zzbw2 = new SignInButtonImpl(context);
            zzbw2.configure(context.getResources(), mSize, mColor);
            this.zzbw = (View)zzbw2;
        }
        this.addView(this.zzbw);
        this.zzbw.setEnabled(this.isEnabled());
        this.zzbw.setOnClickListener((View$OnClickListener)this);
    }
    
    @Deprecated
    public final void setStyle(final int n, final int n2, final Scope[] array) {
        this.setStyle(n, n2);
    }
}
