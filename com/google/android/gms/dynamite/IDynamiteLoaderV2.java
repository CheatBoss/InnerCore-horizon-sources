package com.google.android.gms.dynamite;

import com.google.android.gms.dynamic.*;
import android.os.*;
import com.google.android.gms.internal.stable.*;

public interface IDynamiteLoaderV2 extends IInterface
{
    IObjectWrapper loadModule(final IObjectWrapper p0, final String p1, final byte[] p2) throws RemoteException;
    
    IObjectWrapper loadModule2(final IObjectWrapper p0, final String p1, final int p2, final IObjectWrapper p3) throws RemoteException;
    
    public abstract static class Stub extends zzb implements IDynamiteLoaderV2
    {
        public static IDynamiteLoaderV2 asInterface(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.dynamite.IDynamiteLoaderV2");
            if (queryLocalInterface instanceof IDynamiteLoaderV2) {
                return (IDynamiteLoaderV2)queryLocalInterface;
            }
            return new Proxy(binder);
        }
        
        @Override
        protected boolean dispatchTransaction(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            IObjectWrapper objectWrapper;
            if (n != 1) {
                if (n != 2) {
                    return false;
                }
                objectWrapper = this.loadModule2(IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()), parcel.readString(), parcel.readInt(), IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()));
            }
            else {
                objectWrapper = this.loadModule(IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()), parcel.readString(), parcel.createByteArray());
            }
            parcel2.writeNoException();
            com.google.android.gms.internal.stable.zzc.zza(parcel2, (IInterface)objectWrapper);
            return true;
        }
        
        public static class Proxy extends zza implements IDynamiteLoaderV2
        {
            Proxy(final IBinder binder) {
                super(binder, "com.google.android.gms.dynamite.IDynamiteLoaderV2");
            }
            
            @Override
            public IObjectWrapper loadModule(final IObjectWrapper objectWrapper, final String s, final byte[] array) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (IInterface)objectWrapper);
                obtainAndWriteInterfaceToken.writeString(s);
                obtainAndWriteInterfaceToken.writeByteArray(array);
                final Parcel transactAndReadException = this.transactAndReadException(1, obtainAndWriteInterfaceToken);
                final IObjectWrapper interface1 = IObjectWrapper.Stub.asInterface(transactAndReadException.readStrongBinder());
                transactAndReadException.recycle();
                return interface1;
            }
            
            @Override
            public IObjectWrapper loadModule2(final IObjectWrapper objectWrapper, final String s, final int n, final IObjectWrapper objectWrapper2) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (IInterface)objectWrapper);
                obtainAndWriteInterfaceToken.writeString(s);
                obtainAndWriteInterfaceToken.writeInt(n);
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (IInterface)objectWrapper2);
                final Parcel transactAndReadException = this.transactAndReadException(2, obtainAndWriteInterfaceToken);
                final IObjectWrapper interface1 = IObjectWrapper.Stub.asInterface(transactAndReadException.readStrongBinder());
                transactAndReadException.recycle();
                return interface1;
            }
        }
    }
}
