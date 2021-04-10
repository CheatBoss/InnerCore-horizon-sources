package com.googleplay.licensing;

import android.os.*;

public interface ILicenseResultListener extends IInterface
{
    void verifyLicense(final int p0, final String p1, final String p2) throws RemoteException;
    
    public abstract static class Stub extends Binder implements ILicenseResultListener
    {
        private static final String DESCRIPTOR = "com.android.vending.licensing.ILicenseResultListener";
        static final int TRANSACTION_verifyLicense = 1;
        
        public Stub() {
            this.attachInterface((IInterface)this, "com.android.vending.licensing.ILicenseResultListener");
        }
        
        public static ILicenseResultListener asInterface(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.android.vending.licensing.ILicenseResultListener");
            if (queryLocalInterface != null && queryLocalInterface instanceof ILicenseResultListener) {
                return (ILicenseResultListener)queryLocalInterface;
            }
            return new Proxy(binder);
        }
        
        public IBinder asBinder() {
            return (IBinder)this;
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            if (n == 1) {
                parcel.enforceInterface("com.android.vending.licensing.ILicenseResultListener");
                this.verifyLicense(parcel.readInt(), parcel.readString(), parcel.readString());
                return true;
            }
            if (n != 1598968902) {
                return super.onTransact(n, parcel, parcel2, n2);
            }
            parcel2.writeString("com.android.vending.licensing.ILicenseResultListener");
            return true;
        }
        
        private static class Proxy implements ILicenseResultListener
        {
            private IBinder mRemote;
            
            Proxy(final IBinder mRemote) {
                this.mRemote = mRemote;
            }
            
            public IBinder asBinder() {
                return this.mRemote;
            }
            
            public String getInterfaceDescriptor() {
                return "com.android.vending.licensing.ILicenseResultListener";
            }
            
            @Override
            public void verifyLicense(final int n, final String s, final String s2) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.android.vending.licensing.ILicenseResultListener");
                    obtain.writeInt(n);
                    obtain.writeString(s);
                    obtain.writeString(s2);
                    this.mRemote.transact(1, obtain, (Parcel)null, 1);
                }
                finally {
                    obtain.recycle();
                }
            }
        }
    }
}
