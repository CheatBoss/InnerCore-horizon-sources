package com.microsoft.xbox.toolkit.ui;

import android.widget.*;
import android.content.*;
import android.util.*;
import android.graphics.*;

public class FastProgressBar extends ProgressBar
{
    private boolean isEnabled;
    private int visibility;
    
    public FastProgressBar(final Context context, final AttributeSet set) {
        super(context, set);
        this.setEnabled(true);
        this.setVisibility(0);
    }
    
    protected void onDraw(final Canvas canvas) {
        synchronized (this) {
            super.onDraw(canvas);
            this.postInvalidateDelayed(33L);
        }
    }
    
    public void setEnabled(final boolean isEnabled) {
        if (this.isEnabled != isEnabled) {
            if (!(this.isEnabled = isEnabled)) {
                this.visibility = this.getVisibility();
                super.setVisibility(8);
                return;
            }
            super.setVisibility(this.visibility);
        }
    }
    
    public void setVisibility(final int n) {
        if (this.isEnabled) {
            super.setVisibility(n);
            return;
        }
        this.visibility = n;
    }
}
