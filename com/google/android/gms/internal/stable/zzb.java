package com.google.android.gms.internal.stable;

import android.os.*;

public class zzb extends Binder implements IInterface
{
    private static zzd zzc;
    
    protected zzb(final String s) {
        this.attachInterface((IInterface)this, s);
    }
    
    public IBinder asBinder() {
        return (IBinder)this;
    }
    
    protected boolean dispatchTransaction(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
        return false;
    }
    
    public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
        return this.routeToSuperOrEnforceInterface(n, parcel, parcel2, n2) || this.dispatchTransaction(n, parcel, parcel2, n2);
    }
    
    protected boolean routeToSuperOrEnforceInterface(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
        if (n > 16777215) {
            return super.onTransact(n, parcel, parcel2, n2);
        }
        parcel.enforceInterface(this.getInterfaceDescriptor());
        return false;
    }
}
