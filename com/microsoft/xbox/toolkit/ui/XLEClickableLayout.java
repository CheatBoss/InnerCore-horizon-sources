package com.microsoft.xbox.toolkit.ui;

import android.content.*;
import android.util.*;
import android.view.accessibility.*;
import android.widget.*;
import android.view.*;

public class XLEClickableLayout extends RelativeLayout
{
    public XLEClickableLayout(final Context context) {
        super(context);
        this.setSoundEffectsEnabled(false);
    }
    
    public XLEClickableLayout(final Context context, final AttributeSet set) {
        super(context, set);
        this.setSoundEffectsEnabled(false);
    }
    
    public XLEClickableLayout(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.setSoundEffectsEnabled(false);
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClickable(true);
        accessibilityNodeInfo.setClassName((CharSequence)Button.class.getName());
    }
    
    public void setOnClickListener(final View$OnClickListener view$OnClickListener) {
        super.setOnClickListener(TouchUtil.createOnClickListener(view$OnClickListener));
    }
}
