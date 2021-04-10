package com.appboy.support;

import android.util.*;
import android.content.pm.*;
import android.content.*;
import java.util.*;

public final class IntentUtils
{
    private static final String a;
    private static final Random b;
    
    static {
        a = String.format("%s.%s", "Appboy v2.6.0 .", IntentUtils.class.getName());
        b = new Random();
    }
    
    public static void addComponentAndSendBroadcast(final Context context, final Intent intent) {
        final List queryBroadcastReceivers = context.getPackageManager().queryBroadcastReceivers(intent, 0);
        if (queryBroadcastReceivers == null) {
            final String a = IntentUtils.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("No components found for the following intent: ");
            sb.append(intent.getAction());
            Log.d(a, sb.toString());
            return;
        }
        for (final ResolveInfo resolveInfo : queryBroadcastReceivers) {
            final Intent intent2 = new Intent(intent);
            intent2.setComponent(new ComponentName(resolveInfo.activityInfo.applicationInfo.packageName, resolveInfo.activityInfo.name));
            context.sendBroadcast(intent2);
        }
    }
    
    public static int getRequestCode() {
        return IntentUtils.b.nextInt();
    }
}
