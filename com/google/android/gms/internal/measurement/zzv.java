package com.google.android.gms.internal.measurement;

import android.os.*;

public abstract class zzv extends zzr implements zzu
{
    public static zzu zza(final IBinder binder) {
        if (binder == null) {
            return null;
        }
        final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.finsky.externalreferrer.IGetInstallReferrerService");
        if (queryLocalInterface instanceof zzu) {
            return (zzu)queryLocalInterface;
        }
        return new zzw(binder);
    }
}
