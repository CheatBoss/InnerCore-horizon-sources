package com.amazon.device.iap;

import android.content.*;
import com.amazon.device.iap.internal.*;
import com.amazon.device.iap.internal.util.*;

public final class ResponseReceiver extends BroadcastReceiver
{
    private static final String TAG;
    
    static {
        TAG = ResponseReceiver.class.getSimpleName();
    }
    
    public void onReceive(final Context context, final Intent intent) {
        try {
            d.d().a(context, intent);
        }
        catch (Exception ex) {
            final String tag = ResponseReceiver.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("Error in onReceive: ");
            sb.append(ex);
            e.b(tag, sb.toString());
        }
    }
}
