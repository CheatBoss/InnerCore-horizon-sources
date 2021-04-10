package com.google.android.gms.common.internal;

import com.google.android.gms.internal.stable.*;
import android.os.*;

public interface IResolveAccountCallbacks extends IInterface
{
    void onAccountResolutionComplete(final ResolveAccountResponse p0) throws RemoteException;
    
    public abstract static class Stub extends zzb implements IResolveAccountCallbacks
    {
        public static IResolveAccountCallbacks asInterface(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.common.internal.IResolveAccountCallbacks");
            if (queryLocalInterface instanceof IResolveAccountCallbacks) {
                return (IResolveAccountCallbacks)queryLocalInterface;
            }
            return new Proxy(binder);
        }
        
        @Override
        protected boolean dispatchTransaction(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            if (n == 2) {
                this.onAccountResolutionComplete(com.google.android.gms.internal.stable.zzc.zza(parcel, ResolveAccountResponse.CREATOR));
                parcel2.writeNoException();
                return true;
            }
            return false;
        }
        
        public static class Proxy extends zza implements IResolveAccountCallbacks
        {
            Proxy(final IBinder binder) {
                super(binder, "com.google.android.gms.common.internal.IResolveAccountCallbacks");
            }
            
            @Override
            public void onAccountResolutionComplete(final ResolveAccountResponse resolveAccountResponse) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (Parcelable)resolveAccountResponse);
                this.transactAndReadExceptionReturnVoid(2, obtainAndWriteInterfaceToken);
            }
        }
    }
}
