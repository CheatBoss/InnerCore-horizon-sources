package com.googleplay.licensing;

import android.os.*;

public interface ILicensingService extends IInterface
{
    void checkLicense(final long p0, final String p1, final ILicenseResultListener p2) throws RemoteException;
    
    public abstract static class Stub extends Binder implements ILicensingService
    {
        private static final String DESCRIPTOR = "com.android.vending.licensing.ILicensingService";
        static final int TRANSACTION_checkLicense = 1;
        
        public Stub() {
            this.attachInterface((IInterface)this, "com.android.vending.licensing.ILicensingService");
        }
        
        public static ILicensingService asInterface(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.android.vending.licensing.ILicensingService");
            if (queryLocalInterface != null && queryLocalInterface instanceof ILicensingService) {
                return (ILicensingService)queryLocalInterface;
            }
            return new Proxy(binder);
        }
        
        public IBinder asBinder() {
            return (IBinder)this;
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            if (n == 1) {
                parcel.enforceInterface("com.android.vending.licensing.ILicensingService");
                this.checkLicense(parcel.readLong(), parcel.readString(), ILicenseResultListener.Stub.asInterface(parcel.readStrongBinder()));
                return true;
            }
            if (n != 1598968902) {
                return super.onTransact(n, parcel, parcel2, n2);
            }
            parcel2.writeString("com.android.vending.licensing.ILicensingService");
            return true;
        }
        
        private static class Proxy implements ILicensingService
        {
            private IBinder mRemote;
            
            Proxy(final IBinder mRemote) {
                this.mRemote = mRemote;
            }
            
            public IBinder asBinder() {
                return this.mRemote;
            }
            
            @Override
            public void checkLicense(final long n, final String s, final ILicenseResultListener licenseResultListener) throws RemoteException {
                while (true) {
                    final Parcel obtain = Parcel.obtain();
                    while (true) {
                        try {
                            obtain.writeInterfaceToken("com.android.vending.licensing.ILicensingService");
                            obtain.writeLong(n);
                            obtain.writeString(s);
                            if (licenseResultListener != null) {
                                final IBinder binder = licenseResultListener.asBinder();
                                obtain.writeStrongBinder(binder);
                                this.mRemote.transact(1, obtain, (Parcel)null, 1);
                                return;
                            }
                        }
                        finally {
                            obtain.recycle();
                        }
                        final IBinder binder = null;
                        continue;
                    }
                }
            }
            
            public String getInterfaceDescriptor() {
                return "com.android.vending.licensing.ILicensingService";
            }
        }
    }
}
