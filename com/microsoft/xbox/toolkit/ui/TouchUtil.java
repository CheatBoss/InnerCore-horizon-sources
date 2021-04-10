package com.microsoft.xbox.toolkit.ui;

import com.microsoft.xbox.toolkit.*;
import android.widget.*;
import android.view.*;

public class TouchUtil
{
    public static View$OnClickListener createOnClickListener(final View$OnClickListener view$OnClickListener) {
        if (view$OnClickListener == null) {
            return null;
        }
        XLEAssert.assertNotNull("Original listener is null.", view$OnClickListener);
        return view$OnClickListener;
    }
    
    public static AdapterView$OnItemClickListener createOnItemClickListener(final AdapterView$OnItemClickListener adapterView$OnItemClickListener) {
        if (adapterView$OnItemClickListener == null) {
            return null;
        }
        XLEAssert.assertNotNull("Original listener is null.", adapterView$OnItemClickListener);
        return adapterView$OnItemClickListener;
    }
    
    public static View$OnLongClickListener createOnLongClickListener(final View$OnLongClickListener view$OnLongClickListener) {
        if (view$OnLongClickListener == null) {
            return null;
        }
        XLEAssert.assertNotNull("Original listener is null.", view$OnLongClickListener);
        return view$OnLongClickListener;
    }
}
