package com.google.android.gms.common.internal;

import com.google.android.gms.dynamic.*;
import android.os.*;
import com.google.android.gms.internal.stable.*;

public interface ICertData extends IInterface
{
    IObjectWrapper getBytesWrapped() throws RemoteException;
    
    int getHashCode() throws RemoteException;
    
    public abstract static class Stub extends zzb implements ICertData
    {
        public Stub() {
            super("com.google.android.gms.common.internal.ICertData");
        }
        
        public static ICertData asInterface(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.common.internal.ICertData");
            if (queryLocalInterface instanceof ICertData) {
                return (ICertData)queryLocalInterface;
            }
            return new Proxy(binder);
        }
        
        @Override
        protected boolean dispatchTransaction(int hashCode, final Parcel parcel, final Parcel parcel2, final int n) throws RemoteException {
            if (hashCode == 1) {
                final IObjectWrapper bytesWrapped = this.getBytesWrapped();
                parcel2.writeNoException();
                com.google.android.gms.internal.stable.zzc.zza(parcel2, (IInterface)bytesWrapped);
                return true;
            }
            if (hashCode != 2) {
                return false;
            }
            hashCode = this.getHashCode();
            parcel2.writeNoException();
            parcel2.writeInt(hashCode);
            return true;
        }
        
        public static class Proxy extends zza implements ICertData
        {
            Proxy(final IBinder binder) {
                super(binder, "com.google.android.gms.common.internal.ICertData");
            }
            
            @Override
            public IObjectWrapper getBytesWrapped() throws RemoteException {
                final Parcel transactAndReadException = this.transactAndReadException(1, this.obtainAndWriteInterfaceToken());
                final IObjectWrapper interface1 = IObjectWrapper.Stub.asInterface(transactAndReadException.readStrongBinder());
                transactAndReadException.recycle();
                return interface1;
            }
            
            @Override
            public int getHashCode() throws RemoteException {
                final Parcel transactAndReadException = this.transactAndReadException(2, this.obtainAndWriteInterfaceToken());
                final int int1 = transactAndReadException.readInt();
                transactAndReadException.recycle();
                return int1;
            }
        }
    }
}
