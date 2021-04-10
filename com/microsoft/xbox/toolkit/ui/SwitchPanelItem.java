package com.microsoft.xbox.toolkit.ui;

import android.content.*;
import android.util.*;
import com.microsoft.xbox.toolkit.*;
import android.widget.*;
import android.view.*;
import android.content.res.*;

public class SwitchPanelItem extends FrameLayout implements SwitchPanelChild
{
    private final int INVALID_STATE_ID;
    private int state;
    
    public SwitchPanelItem(final Context context, final AttributeSet set) {
        super(context, set);
        this.INVALID_STATE_ID = -1;
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, XLERValueHelper.getStyleableRValueArray("SwitchPanelItem"));
        this.state = obtainStyledAttributes.getInteger(XLERValueHelper.getStyleableRValue("SwitchPanelItem_state"), -1);
        obtainStyledAttributes.recycle();
        if (this.state >= 0) {
            this.setLayoutParams((ViewGroup$LayoutParams)new RelativeLayout$LayoutParams(-1, -1));
            return;
        }
        throw new IllegalArgumentException("You must specify the state attribute in the xml, and the value must be positive.");
    }
    
    public int getState() {
        return this.state;
    }
}
