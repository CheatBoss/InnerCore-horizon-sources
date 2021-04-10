package com.google.android.gms.dynamite;

import com.google.android.gms.dynamic.*;
import android.os.*;
import com.google.android.gms.internal.stable.*;

public interface IDynamiteLoader extends IInterface
{
    IObjectWrapper createModuleContext(final IObjectWrapper p0, final String p1, final int p2) throws RemoteException;
    
    int getModuleVersion(final IObjectWrapper p0, final String p1) throws RemoteException;
    
    int getModuleVersion2(final IObjectWrapper p0, final String p1, final boolean p2) throws RemoteException;
    
    public abstract static class Stub extends zzb implements IDynamiteLoader
    {
        public static IDynamiteLoader asInterface(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.dynamite.IDynamiteLoader");
            if (queryLocalInterface instanceof IDynamiteLoader) {
                return (IDynamiteLoader)queryLocalInterface;
            }
            return new Proxy(binder);
        }
        
        @Override
        protected boolean dispatchTransaction(int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            if (n != 1) {
                if (n == 2) {
                    final IObjectWrapper moduleContext = this.createModuleContext(IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()), parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    com.google.android.gms.internal.stable.zzc.zza(parcel2, (IInterface)moduleContext);
                    return true;
                }
                if (n != 3) {
                    return false;
                }
                n = this.getModuleVersion2(IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()), parcel.readString(), com.google.android.gms.internal.stable.zzc.zza(parcel));
            }
            else {
                n = this.getModuleVersion(IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()), parcel.readString());
            }
            parcel2.writeNoException();
            parcel2.writeInt(n);
            return true;
        }
        
        public static class Proxy extends zza implements IDynamiteLoader
        {
            Proxy(final IBinder binder) {
                super(binder, "com.google.android.gms.dynamite.IDynamiteLoader");
            }
            
            @Override
            public IObjectWrapper createModuleContext(final IObjectWrapper objectWrapper, final String s, final int n) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (IInterface)objectWrapper);
                obtainAndWriteInterfaceToken.writeString(s);
                obtainAndWriteInterfaceToken.writeInt(n);
                final Parcel transactAndReadException = this.transactAndReadException(2, obtainAndWriteInterfaceToken);
                final IObjectWrapper interface1 = IObjectWrapper.Stub.asInterface(transactAndReadException.readStrongBinder());
                transactAndReadException.recycle();
                return interface1;
            }
            
            @Override
            public int getModuleVersion(final IObjectWrapper objectWrapper, final String s) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (IInterface)objectWrapper);
                obtainAndWriteInterfaceToken.writeString(s);
                final Parcel transactAndReadException = this.transactAndReadException(1, obtainAndWriteInterfaceToken);
                final int int1 = transactAndReadException.readInt();
                transactAndReadException.recycle();
                return int1;
            }
            
            @Override
            public int getModuleVersion2(final IObjectWrapper objectWrapper, final String s, final boolean b) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (IInterface)objectWrapper);
                obtainAndWriteInterfaceToken.writeString(s);
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, b);
                final Parcel transactAndReadException = this.transactAndReadException(3, obtainAndWriteInterfaceToken);
                final int int1 = transactAndReadException.readInt();
                transactAndReadException.recycle();
                return int1;
            }
        }
    }
}
