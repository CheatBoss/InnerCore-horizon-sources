package com.google.android.gms.internal.ads_identifier;

import android.os.*;

public class zza implements IInterface
{
    private final IBinder zza;
    private final String zzb;
    
    protected zza(final IBinder zza, final String zzb) {
        this.zza = zza;
        this.zzb = zzb;
    }
    
    public IBinder asBinder() {
        return this.zza;
    }
    
    protected final Parcel obtainAndWriteInterfaceToken() {
        final Parcel obtain = Parcel.obtain();
        obtain.writeInterfaceToken(this.zzb);
        return obtain;
    }
    
    protected final Parcel transactAndReadException(final int n, final Parcel parcel) throws RemoteException {
        final Parcel obtain = Parcel.obtain();
        try {
            try {
                this.zza.transact(n, parcel, obtain, 0);
                obtain.readException();
                parcel.recycle();
                return obtain;
            }
            finally {}
        }
        catch (RuntimeException ex) {
            final Parcel parcel2;
            parcel2.recycle();
            throw ex;
        }
        parcel.recycle();
    }
}
