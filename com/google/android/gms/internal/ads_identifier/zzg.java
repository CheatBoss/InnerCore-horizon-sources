package com.google.android.gms.internal.ads_identifier;

import android.os.*;

public final class zzg extends zza implements zze
{
    zzg(final IBinder binder) {
        super(binder, "com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
    }
    
    @Override
    public final String getId() throws RemoteException {
        final Parcel transactAndReadException = this.transactAndReadException(1, this.obtainAndWriteInterfaceToken());
        final String string = transactAndReadException.readString();
        transactAndReadException.recycle();
        return string;
    }
    
    @Override
    public final boolean zzb(final boolean b) throws RemoteException {
        final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, true);
        final Parcel transactAndReadException = this.transactAndReadException(2, obtainAndWriteInterfaceToken);
        final boolean zza = zzc.zza(transactAndReadException);
        transactAndReadException.recycle();
        return zza;
    }
}
