package com.google.android.gms.internal.measurement;

import android.os.*;

public class zzq implements IInterface
{
    private final IBinder zzqt;
    private final String zzqu;
    
    protected zzq(final IBinder zzqt, final String zzqu) {
        this.zzqt = zzqt;
        this.zzqu = zzqu;
    }
    
    public IBinder asBinder() {
        return this.zzqt;
    }
    
    protected final Parcel obtainAndWriteInterfaceToken() {
        final Parcel obtain = Parcel.obtain();
        obtain.writeInterfaceToken(this.zzqu);
        return obtain;
    }
    
    protected final Parcel transactAndReadException(final int n, final Parcel parcel) throws RemoteException {
        final Parcel obtain = Parcel.obtain();
        try {
            try {
                this.zzqt.transact(n, parcel, obtain, 0);
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
    
    protected final void transactAndReadExceptionReturnVoid(final int n, final Parcel parcel) throws RemoteException {
        final Parcel obtain = Parcel.obtain();
        try {
            this.zzqt.transact(n, parcel, obtain, 0);
            obtain.readException();
        }
        finally {
            parcel.recycle();
            obtain.recycle();
        }
    }
}
