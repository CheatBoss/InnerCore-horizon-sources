package com.appsflyer.internal;

import android.content.*;
import java.util.*;
import android.util.*;
import android.view.*;
import com.appsflyer.*;

public final class q
{
    q() {
    }
    
    public static Map<String, String> \u0399(final Context context) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        try {
            final DisplayMetrics displayMetrics = new DisplayMetrics();
            ((WindowManager)context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
            final int screenLayout = context.getResources().getConfiguration().screenLayout;
            hashMap.put("x_px", String.valueOf(displayMetrics.widthPixels));
            hashMap.put("y_px", String.valueOf(displayMetrics.heightPixels));
            hashMap.put("d_dpi", String.valueOf(displayMetrics.densityDpi));
            hashMap.put("size", String.valueOf(screenLayout & 0xF));
            hashMap.put("xdp", String.valueOf(displayMetrics.xdpi));
            hashMap.put("ydp", String.valueOf(displayMetrics.ydpi));
            return hashMap;
        }
        finally {
            final Throwable t;
            AFLogger.afErrorLog("Couldn't aggregate screen stats: ", t);
            return hashMap;
        }
    }
}
