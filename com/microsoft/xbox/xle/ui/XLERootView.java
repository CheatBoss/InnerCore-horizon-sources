package com.microsoft.xbox.xle.ui;

import android.content.*;
import android.util.*;
import com.microsoft.xboxtcui.*;
import com.microsoft.xbox.toolkit.system.*;
import android.widget.*;
import android.view.*;
import android.graphics.*;
import android.content.res.*;

public class XLERootView extends RelativeLayout
{
    private static final int UNASSIGNED_ACTIVITY_BODY_ID = -1;
    private View activityBody;
    private int activityBodyIndex;
    private String headerName;
    private boolean isTopLevel;
    private long lastFps;
    private long lastMs;
    private int origPaddingBottom;
    private boolean showTitleBar;
    
    public XLERootView(final Context context) {
        super(context);
        this.isTopLevel = false;
        this.showTitleBar = true;
        this.lastMs = 0L;
        this.lastFps = 0L;
        throw new UnsupportedOperationException();
    }
    
    public XLERootView(Context obtainStyledAttributes, final AttributeSet set) {
        super(obtainStyledAttributes, set);
        this.isTopLevel = false;
        this.showTitleBar = true;
        this.lastMs = 0L;
        this.lastFps = 0L;
        obtainStyledAttributes = (Context)obtainStyledAttributes.obtainStyledAttributes(set, R$styleable.XLERootView);
        if (obtainStyledAttributes != null) {
            try {
                this.activityBodyIndex = ((TypedArray)obtainStyledAttributes).getResourceId(R$styleable.XLERootView_activityBody, -1);
                this.isTopLevel = ((TypedArray)obtainStyledAttributes).getBoolean(R$styleable.XLERootView_isTopLevel, false);
                this.showTitleBar = ((TypedArray)obtainStyledAttributes).getBoolean(R$styleable.XLERootView_showTitleBar, true);
                final int int1 = ((TypedArray)obtainStyledAttributes).getInt(R$styleable.XLERootView_minScreenPercent, Integer.MIN_VALUE);
                if (int1 != Integer.MIN_VALUE) {
                    this.setMinimumWidth(Math.max(0, int1) * SystemUtil.getScreenWidth() / 100);
                }
                this.headerName = ((TypedArray)obtainStyledAttributes).getString(R$styleable.XLERootView_headerName);
            }
            finally {
                ((TypedArray)obtainStyledAttributes).recycle();
            }
        }
    }
    
    private void initialize() {
        final int activityBodyIndex = this.activityBodyIndex;
        if (activityBodyIndex != -1) {
            this.activityBody = this.findViewById(activityBodyIndex);
        }
        else {
            this.activityBody = (View)this;
        }
        this.origPaddingBottom = this.getPaddingBottom();
        final View activityBody = this.activityBody;
        if (activityBody != null && activityBody != this) {
            final ViewGroup$LayoutParams layoutParams = activityBody.getLayoutParams();
            final RelativeLayout$LayoutParams relativeLayout$LayoutParams = new RelativeLayout$LayoutParams(layoutParams);
            relativeLayout$LayoutParams.width = -1;
            relativeLayout$LayoutParams.height = -1;
            relativeLayout$LayoutParams.addRule(10);
            if (layoutParams instanceof ViewGroup$MarginLayoutParams) {
                final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams = (ViewGroup$MarginLayoutParams)layoutParams;
                this.setPadding(this.getPaddingLeft() + viewGroup$MarginLayoutParams.leftMargin, this.getPaddingTop() + viewGroup$MarginLayoutParams.topMargin, this.getPaddingRight() + viewGroup$MarginLayoutParams.rightMargin, this.origPaddingBottom + viewGroup$MarginLayoutParams.bottomMargin);
                relativeLayout$LayoutParams.setMargins(0, 0, 0, 0);
            }
            this.removeView(this.activityBody);
            this.addView(this.activityBody, (ViewGroup$LayoutParams)relativeLayout$LayoutParams);
        }
    }
    
    protected void dispatchDraw(final Canvas canvas) {
        super.dispatchDraw(canvas);
    }
    
    public String getHeaderName() {
        return this.headerName;
    }
    
    public boolean getIsTopLevel() {
        return this.isTopLevel;
    }
    
    public boolean getShowTitleBar() {
        return this.showTitleBar;
    }
    
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.initialize();
    }
    
    public void setBottomMargin(final int n) {
        this.setPadding(this.getPaddingLeft(), this.getPaddingTop(), this.getPaddingRight(), this.origPaddingBottom + n);
    }
}
