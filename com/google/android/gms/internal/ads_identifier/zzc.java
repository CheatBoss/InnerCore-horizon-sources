package com.google.android.gms.internal.ads_identifier;

import android.os.*;

public class zzc
{
    private static final ClassLoader zzd;
    
    static {
        zzd = zzc.class.getClassLoader();
    }
    
    private zzc() {
    }
    
    public static void zza(final Parcel parcel, final boolean b) {
        parcel.writeInt(1);
    }
    
    public static boolean zza(final Parcel parcel) {
        return parcel.readInt() != 0;
    }
}
