package com.google.android.gms.common.internal;

import android.accounts.*;
import com.google.android.gms.internal.stable.*;
import android.os.*;

public interface IAccountAccessor extends IInterface
{
    Account getAccount() throws RemoteException;
    
    public abstract static class Stub extends zzb implements IAccountAccessor
    {
        public static IAccountAccessor asInterface(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.common.internal.IAccountAccessor");
            if (queryLocalInterface instanceof IAccountAccessor) {
                return (IAccountAccessor)queryLocalInterface;
            }
            return new Proxy(binder);
        }
        
        @Override
        protected boolean dispatchTransaction(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            if (n == 2) {
                final Account account = this.getAccount();
                parcel2.writeNoException();
                com.google.android.gms.internal.stable.zzc.zzb(parcel2, (Parcelable)account);
                return true;
            }
            return false;
        }
        
        public static class Proxy extends zza implements IAccountAccessor
        {
            Proxy(final IBinder binder) {
                super(binder, "com.google.android.gms.common.internal.IAccountAccessor");
            }
            
            @Override
            public Account getAccount() throws RemoteException {
                final Parcel transactAndReadException = this.transactAndReadException(2, this.obtainAndWriteInterfaceToken());
                final Account account = com.google.android.gms.internal.stable.zzc.zza(transactAndReadException, (android.os.Parcelable$Creator<Account>)Account.CREATOR);
                transactAndReadException.recycle();
                return account;
            }
        }
    }
}
