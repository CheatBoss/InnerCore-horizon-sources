package com.microsoft.xbox.toolkit.ui;

import android.widget.*;
import android.content.*;
import android.util.*;
import android.graphics.*;

public class XLEListView extends ListView
{
    public XLEListView(final Context context) {
        super(context);
    }
    
    public XLEListView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public XLEListView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    protected void dispatchDraw(final Canvas canvas) {
        try {
            super.dispatchDraw(canvas);
        }
        catch (IndexOutOfBoundsException ex) {}
    }
}
