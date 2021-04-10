package com.bumptech.glide.signature;

import java.util.concurrent.*;
import com.bumptech.glide.load.*;
import android.content.*;
import java.util.*;
import android.content.pm.*;

public final class ApplicationVersionSignature
{
    private static final ConcurrentHashMap<String, Key> PACKAGE_NAME_TO_KEY;
    
    static {
        PACKAGE_NAME_TO_KEY = new ConcurrentHashMap<String, Key>();
    }
    
    private ApplicationVersionSignature() {
    }
    
    public static Key obtain(final Context context) {
        final String packageName = context.getPackageName();
        Key key;
        if ((key = ApplicationVersionSignature.PACKAGE_NAME_TO_KEY.get(packageName)) == null) {
            final Key obtainVersionSignature = obtainVersionSignature(context);
            if ((key = ApplicationVersionSignature.PACKAGE_NAME_TO_KEY.putIfAbsent(packageName, obtainVersionSignature)) == null) {
                key = obtainVersionSignature;
            }
        }
        return key;
    }
    
    private static Key obtainVersionSignature(final Context context) {
        final PackageInfo packageInfo = null;
        PackageInfo packageInfo2;
        try {
            packageInfo2 = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        }
        catch (PackageManager$NameNotFoundException ex) {
            ex.printStackTrace();
            packageInfo2 = packageInfo;
        }
        String s;
        if (packageInfo2 != null) {
            s = String.valueOf(packageInfo2.versionCode);
        }
        else {
            s = UUID.randomUUID().toString();
        }
        return new StringSignature(s);
    }
    
    static void reset() {
        ApplicationVersionSignature.PACKAGE_NAME_TO_KEY.clear();
    }
}
