package com.microsoft.aad.adal;

import java.util.*;
import android.content.*;
import android.os.*;

public interface IBrokerAccountService extends IInterface
{
    Bundle acquireTokenSilently(final Map p0) throws RemoteException;
    
    Bundle getBrokerUsers() throws RemoteException;
    
    Intent getIntentForInteractiveRequest() throws RemoteException;
    
    void removeAccounts() throws RemoteException;
    
    public abstract static class Stub extends Binder implements IBrokerAccountService
    {
        private static final String DESCRIPTOR = "com.microsoft.aad.adal.IBrokerAccountService";
        static final int TRANSACTION_acquireTokenSilently = 2;
        static final int TRANSACTION_getBrokerUsers = 1;
        static final int TRANSACTION_getIntentForInteractiveRequest = 3;
        static final int TRANSACTION_removeAccounts = 4;
        
        public Stub() {
            this.attachInterface((IInterface)this, "com.microsoft.aad.adal.IBrokerAccountService");
        }
        
        public static IBrokerAccountService asInterface(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.microsoft.aad.adal.IBrokerAccountService");
            if (queryLocalInterface != null && queryLocalInterface instanceof IBrokerAccountService) {
                return (IBrokerAccountService)queryLocalInterface;
            }
            return new Proxy(binder);
        }
        
        public IBinder asBinder() {
            return (IBinder)this;
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            if (n != 1) {
                if (n != 2) {
                    if (n != 3) {
                        if (n == 4) {
                            parcel.enforceInterface("com.microsoft.aad.adal.IBrokerAccountService");
                            this.removeAccounts();
                            parcel2.writeNoException();
                            return true;
                        }
                        if (n != 1598968902) {
                            return super.onTransact(n, parcel, parcel2, n2);
                        }
                        parcel2.writeString("com.microsoft.aad.adal.IBrokerAccountService");
                        return true;
                    }
                    else {
                        parcel.enforceInterface("com.microsoft.aad.adal.IBrokerAccountService");
                        final Intent intentForInteractiveRequest = this.getIntentForInteractiveRequest();
                        parcel2.writeNoException();
                        if (intentForInteractiveRequest != null) {
                            parcel2.writeInt(1);
                            intentForInteractiveRequest.writeToParcel(parcel2, 1);
                            return true;
                        }
                        parcel2.writeInt(0);
                        return true;
                    }
                }
                else {
                    parcel.enforceInterface("com.microsoft.aad.adal.IBrokerAccountService");
                    final Bundle acquireTokenSilently = this.acquireTokenSilently(parcel.readHashMap(this.getClass().getClassLoader()));
                    parcel2.writeNoException();
                    if (acquireTokenSilently != null) {
                        parcel2.writeInt(1);
                        acquireTokenSilently.writeToParcel(parcel2, 1);
                        return true;
                    }
                    parcel2.writeInt(0);
                    return true;
                }
            }
            else {
                parcel.enforceInterface("com.microsoft.aad.adal.IBrokerAccountService");
                final Bundle brokerUsers = this.getBrokerUsers();
                parcel2.writeNoException();
                if (brokerUsers != null) {
                    parcel2.writeInt(1);
                    brokerUsers.writeToParcel(parcel2, 1);
                    return true;
                }
                parcel2.writeInt(0);
                return true;
            }
        }
        
        private static class Proxy implements IBrokerAccountService
        {
            private IBinder mRemote;
            
            Proxy(final IBinder mRemote) {
                this.mRemote = mRemote;
            }
            
            @Override
            public Bundle acquireTokenSilently(final Map map) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.microsoft.aad.adal.IBrokerAccountService");
                    obtain.writeMap(map);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    Bundle bundle;
                    if (obtain2.readInt() != 0) {
                        bundle = (Bundle)Bundle.CREATOR.createFromParcel(obtain2);
                    }
                    else {
                        bundle = null;
                    }
                    return bundle;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            public IBinder asBinder() {
                return this.mRemote;
            }
            
            @Override
            public Bundle getBrokerUsers() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.microsoft.aad.adal.IBrokerAccountService");
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    Bundle bundle;
                    if (obtain2.readInt() != 0) {
                        bundle = (Bundle)Bundle.CREATOR.createFromParcel(obtain2);
                    }
                    else {
                        bundle = null;
                    }
                    return bundle;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public Intent getIntentForInteractiveRequest() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.microsoft.aad.adal.IBrokerAccountService");
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    Intent intent;
                    if (obtain2.readInt() != 0) {
                        intent = (Intent)Intent.CREATOR.createFromParcel(obtain2);
                    }
                    else {
                        intent = null;
                    }
                    return intent;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            public String getInterfaceDescriptor() {
                return "com.microsoft.aad.adal.IBrokerAccountService";
            }
            
            @Override
            public void removeAccounts() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.microsoft.aad.adal.IBrokerAccountService");
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
