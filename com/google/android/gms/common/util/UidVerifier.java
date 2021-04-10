package com.google.android.gms.common.util;

import android.content.*;
import com.google.android.gms.common.*;
import android.util.*;
import android.content.pm.*;
import com.google.android.gms.common.wrappers.*;

public final class UidVerifier
{
    public static boolean isGooglePlayServicesUid(final Context context, final int n) {
        if (!uidHasPackageName(context, n, "com.google.android.gms")) {
            return false;
        }
        final PackageManager packageManager = context.getPackageManager();
        try {
            return GoogleSignatureVerifier.getInstance(context).isGooglePublicSignedPackage(packageManager.getPackageInfo("com.google.android.gms", 64));
        }
        catch (PackageManager$NameNotFoundException ex) {
            if (Log.isLoggable("UidVerifier", 3)) {
                Log.d("UidVerifier", "Package manager can't find google play services package, defaulting to false");
            }
            return false;
        }
    }
    
    public static boolean uidHasPackageName(final Context context, final int n, final String s) {
        return Wrappers.packageManager(context).uidHasPackageName(n, s);
    }
}
