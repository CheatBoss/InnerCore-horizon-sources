package com.microsoft.xbox.toolkit.ui;

import android.widget.*;
import android.content.*;
import android.util.*;
import com.microsoft.xboxtcui.*;
import android.content.res.*;
import android.view.*;

public class CustomTypefaceTextView extends TextView
{
    public CustomTypefaceTextView(final Context context, final AttributeSet set) {
        super(context, set);
        if (!this.isInEditMode()) {
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R$styleable.CustomTypeface);
            final String string = obtainStyledAttributes.getString(R$styleable.CustomTypeface_typefaceSource);
            final String string2 = obtainStyledAttributes.getString(R$styleable.CustomTypeface_uppercaseText);
            if (string2 != null) {
                this.setText((CharSequence)string2.toUpperCase());
            }
            this.applyCustomTypeface(context, string);
            obtainStyledAttributes.recycle();
        }
    }
    
    public CustomTypefaceTextView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        if (!this.isInEditMode()) {
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R$styleable.CustomTypeface);
            this.applyCustomTypeface(context, obtainStyledAttributes.getString(R$styleable.CustomTypeface_typefaceSource));
            obtainStyledAttributes.recycle();
        }
    }
    
    public CustomTypefaceTextView(final Context context, final String s) {
        super(context);
        this.applyCustomTypeface(context, s);
    }
    
    private void applyCustomTypeface(final Context context, final String s) {
        if (s != null) {
            this.setTypeface(FontManager.Instance().getTypeface(this.getContext(), s));
        }
        this.setCursorVisible(false);
    }
    
    public void setClickable(final boolean b) {
        if (!b) {
            return;
        }
        throw new UnsupportedOperationException("If you want CustomTypefaceTextView to be clickable, use XLEButton instead.");
    }
    
    public void setOnClickListener(final View$OnClickListener view$OnClickListener) {
        throw new UnsupportedOperationException("If you want CustomTypefaceTextView to be clickable, use XLEButton instead.");
    }
}
