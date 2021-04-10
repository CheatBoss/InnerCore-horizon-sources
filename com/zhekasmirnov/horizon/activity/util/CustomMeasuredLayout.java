package com.zhekasmirnov.horizon.activity.util;

import android.widget.*;
import android.content.*;
import android.util.*;
import android.support.annotation.*;
import android.view.*;

public class CustomMeasuredLayout extends LinearLayout
{
    private float maxWidthPercentage;
    private float maxHeightPercentage;
    
    public CustomMeasuredLayout(final Context context) {
        super(context);
        this.maxWidthPercentage = 0.5f;
        this.maxHeightPercentage = 1.0f;
    }
    
    public CustomMeasuredLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.maxWidthPercentage = 0.5f;
        this.maxHeightPercentage = 1.0f;
    }
    
    public CustomMeasuredLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.maxWidthPercentage = 0.5f;
        this.maxHeightPercentage = 1.0f;
    }
    
    @RequiresApi(api = 21)
    public CustomMeasuredLayout(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.maxWidthPercentage = 0.5f;
        this.maxHeightPercentage = 1.0f;
    }
    
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final ViewParent parent = this.getParent();
        if (parent instanceof View) {
            this.setMeasuredDimension((int)Math.min(this.maxWidthPercentage * ((View)parent).getMeasuredWidth(), (float)this.getMeasuredWidth()), (int)Math.min(this.maxHeightPercentage * ((View)parent).getMeasuredHeight(), (float)this.getMeasuredHeight()));
        }
    }
}
