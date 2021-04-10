package com.appboy.ui.support;

import com.appboy.support.*;
import android.os.*;
import android.net.*;
import java.util.*;
import android.content.*;
import android.content.pm.*;

public class UriUtils
{
    private static final String TAG;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(UriUtils.class);
    }
    
    public static Intent getMainActivityIntent(final Context context, final Bundle bundle) {
        final Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        launchIntentForPackage.setFlags(872415232);
        if (bundle != null) {
            launchIntentForPackage.putExtras(bundle);
        }
        return launchIntentForPackage;
    }
    
    public static Map<String, String> getQueryParameters(final Uri uri) {
        if (uri.isOpaque()) {
            AppboyLogger.d(UriUtils.TAG, "URI is not hierarchical. There are no query parameters to parse.");
            return Collections.emptyMap();
        }
        final String encodedQuery = uri.getEncodedQuery();
        if (encodedQuery == null) {
            return Collections.emptyMap();
        }
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        int n = 0;
        int n2;
        do {
            if ((n2 = encodedQuery.indexOf(38, n)) == -1) {
                n2 = encodedQuery.length();
            }
            final int index = encodedQuery.indexOf(61, n);
            int n3;
            if (index > n2 || (n3 = index) == -1) {
                n3 = n2;
            }
            if (n2 > n) {
                hashMap.put(Uri.decode(encodedQuery.substring(n, n3)), Uri.decode(encodedQuery.substring(n3 + 1, n2)));
            }
        } while ((n = n2 + 1) < encodedQuery.length());
        return (Map<String, String>)Collections.unmodifiableMap((Map<?, ?>)hashMap);
    }
    
    public static boolean isActivityRegisteredInManifest(final Context context, final String s) {
        boolean b = false;
        try {
            if (context.getPackageManager().getActivityInfo(new ComponentName(context, s), 0) != null) {
                b = true;
            }
            return b;
        }
        catch (PackageManager$NameNotFoundException ex) {
            final String tag = UriUtils.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("Could not find activity info for class with name: ");
            sb.append(s);
            AppboyLogger.w(tag, sb.toString(), (Throwable)ex);
            return false;
        }
    }
}
