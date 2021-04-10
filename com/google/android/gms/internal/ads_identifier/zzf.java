package com.google.android.gms.internal.ads_identifier;

import android.os.*;

public abstract class zzf extends zzb implements zze
{
    public static zze zza(final IBinder binder) {
        if (binder == null) {
            return null;
        }
        final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
        if (queryLocalInterface instanceof zze) {
            return (zze)queryLocalInterface;
        }
        return new zzg(binder);
    }
}
