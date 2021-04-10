package com.google.android.gms.common.internal;

import com.google.android.gms.dynamic.*;
import com.google.android.gms.internal.stable.*;
import android.os.*;

public interface ISignInButtonCreator extends IInterface
{
    IObjectWrapper newSignInButton(final IObjectWrapper p0, final int p1, final int p2) throws RemoteException;
    
    IObjectWrapper newSignInButtonFromConfig(final IObjectWrapper p0, final SignInButtonConfig p1) throws RemoteException;
    
    public abstract static class Stub extends zzb implements ISignInButtonCreator
    {
        public static ISignInButtonCreator asInterface(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.common.internal.ISignInButtonCreator");
            if (queryLocalInterface instanceof ISignInButtonCreator) {
                return (ISignInButtonCreator)queryLocalInterface;
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
                objectWrapper = this.newSignInButtonFromConfig(IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()), com.google.android.gms.internal.stable.zzc.zza(parcel, SignInButtonConfig.CREATOR));
            }
            else {
                objectWrapper = this.newSignInButton(IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt(), parcel.readInt());
            }
            parcel2.writeNoException();
            com.google.android.gms.internal.stable.zzc.zza(parcel2, (IInterface)objectWrapper);
            return true;
        }
        
        public static class Proxy extends zza implements ISignInButtonCreator
        {
            Proxy(final IBinder binder) {
                super(binder, "com.google.android.gms.common.internal.ISignInButtonCreator");
            }
            
            @Override
            public IObjectWrapper newSignInButton(final IObjectWrapper objectWrapper, final int n, final int n2) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (IInterface)objectWrapper);
                obtainAndWriteInterfaceToken.writeInt(n);
                obtainAndWriteInterfaceToken.writeInt(n2);
                final Parcel transactAndReadException = this.transactAndReadException(1, obtainAndWriteInterfaceToken);
                final IObjectWrapper interface1 = IObjectWrapper.Stub.asInterface(transactAndReadException.readStrongBinder());
                transactAndReadException.recycle();
                return interface1;
            }
            
            @Override
            public IObjectWrapper newSignInButtonFromConfig(final IObjectWrapper objectWrapper, final SignInButtonConfig signInButtonConfig) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (IInterface)objectWrapper);
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (Parcelable)signInButtonConfig);
                final Parcel transactAndReadException = this.transactAndReadException(2, obtainAndWriteInterfaceToken);
                final IObjectWrapper interface1 = IObjectWrapper.Stub.asInterface(transactAndReadException.readStrongBinder());
                transactAndReadException.recycle();
                return interface1;
            }
        }
    }
}
