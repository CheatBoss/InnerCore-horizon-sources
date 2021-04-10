package com.google.firebase.iid;

import android.os.*;

public final class zzv implements zzu
{
    private final IBinder zzbl;
    
    zzv(final IBinder zzbl) {
        this.zzbl = zzbl;
    }
    
    public final IBinder asBinder() {
        return this.zzbl;
    }
    
    @Override
    public final void send(final Message message) throws RemoteException {
        final Parcel obtain = Parcel.obtain();
        obtain.writeInterfaceToken("com.google.android.gms.iid.IMessengerCompat");
        obtain.writeInt(1);
        message.writeToParcel(obtain, 0);
        try {
            this.zzbl.transact(1, obtain, (Parcel)null, 1);
        }
        finally {
            obtain.recycle();
        }
    }
}
