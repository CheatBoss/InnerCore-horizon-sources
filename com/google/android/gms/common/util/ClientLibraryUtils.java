package com.google.android.gms.common.util;

import android.content.*;
import com.google.android.gms.common.wrappers.*;
import android.content.pm.*;

public class ClientLibraryUtils
{
    public static boolean isPackageStopped(final Context context, final String s) {
        "com.google.android.gms".equals(s);
        try {
            return (Wrappers.packageManager(context).getApplicationInfo(s, 0).flags & 0x200000) != 0x0;
        }
        catch (PackageManager$NameNotFoundException ex) {
            return false;
        }
    }
}
