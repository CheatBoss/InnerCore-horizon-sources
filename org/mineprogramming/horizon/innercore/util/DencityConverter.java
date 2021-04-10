package org.mineprogramming.horizon.innercore.util;

import android.util.*;
import android.content.*;

public class DencityConverter
{
    private static DisplayMetrics displayMetrics;
    
    public static int dp(final float n) {
        return (int)TypedValue.applyDimension(1, n, DencityConverter.displayMetrics);
    }
    
    public static void initializeDensity(final Context context) {
        DencityConverter.displayMetrics = context.getResources().getDisplayMetrics();
    }
    
    public static int sp(final float n) {
        return (int)TypedValue.applyDimension(2, n, DencityConverter.displayMetrics);
    }
}
