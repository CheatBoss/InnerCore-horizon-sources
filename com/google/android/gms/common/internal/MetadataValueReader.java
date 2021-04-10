package com.google.android.gms.common.internal;

import android.content.*;
import android.util.*;
import android.content.pm.*;
import com.google.android.gms.common.wrappers.*;
import android.os.*;

public class MetadataValueReader
{
    private static Object sLock;
    private static boolean zzui;
    private static String zzuj;
    private static int zzuk;
    
    static {
        MetadataValueReader.sLock = new Object();
    }
    
    public static String getGoogleAppId(final Context context) {
        zze(context);
        return MetadataValueReader.zzuj;
    }
    
    public static int getGooglePlayServicesVersion(final Context context) {
        zze(context);
        return MetadataValueReader.zzuk;
    }
    
    private static void zze(final Context context) {
        synchronized (MetadataValueReader.sLock) {
            if (MetadataValueReader.zzui) {
                return;
            }
            MetadataValueReader.zzui = true;
            final String packageName = context.getPackageName();
            final PackageManagerWrapper packageManager = Wrappers.packageManager(context);
            try {
                final Bundle metaData = packageManager.getApplicationInfo(packageName, 128).metaData;
                if (metaData == null) {
                    return;
                }
                MetadataValueReader.zzuj = metaData.getString("com.google.app.id");
                MetadataValueReader.zzuk = metaData.getInt("com.google.android.gms.version");
            }
            catch (PackageManager$NameNotFoundException ex) {
                Log.wtf("MetadataValueReader", "This should never happen.", (Throwable)ex);
            }
        }
    }
}
