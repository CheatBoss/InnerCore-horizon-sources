package com.google.android.gms.internal.measurement;

import android.os.*;

public final class zzw extends zzq implements zzu
{
    zzw(final IBinder binder) {
        super(binder, "com.google.android.finsky.externalreferrer.IGetInstallReferrerService");
    }
    
    @Override
    public final Bundle zza(final Bundle bundle) throws RemoteException {
        final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
        zzs.zza(obtainAndWriteInterfaceToken, (Parcelable)bundle);
        final Parcel transactAndReadException = this.transactAndReadException(1, obtainAndWriteInterfaceToken);
        final Bundle bundle2 = zzs.zza(transactAndReadException, (android.os.Parcelable$Creator<Bundle>)Bundle.CREATOR);
        transactAndReadException.recycle();
        return bundle2;
    }
}
