package com.google.android.gms.common.internal;

import com.google.android.gms.dynamic.*;
import com.google.android.gms.common.*;
import com.google.android.gms.internal.stable.*;
import android.os.*;

public interface IGoogleCertificatesApi extends IInterface
{
    IObjectWrapper getGoogleCertificates() throws RemoteException;
    
    IObjectWrapper getGoogleReleaseCertificates() throws RemoteException;
    
    boolean isGoogleOrPlatformSigned(final GoogleCertificatesQuery p0, final IObjectWrapper p1) throws RemoteException;
    
    boolean isGoogleReleaseSigned(final String p0, final IObjectWrapper p1) throws RemoteException;
    
    boolean isGoogleSigned(final String p0, final IObjectWrapper p1) throws RemoteException;
    
    public abstract static class Stub extends zzb implements IGoogleCertificatesApi
    {
        public static IGoogleCertificatesApi asInterface(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.common.internal.IGoogleCertificatesApi");
            if (queryLocalInterface instanceof IGoogleCertificatesApi) {
                return (IGoogleCertificatesApi)queryLocalInterface;
            }
            return new Proxy(binder);
        }
        
        @Override
        protected boolean dispatchTransaction(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            IObjectWrapper objectWrapper;
            if (n != 1) {
                if (n != 2) {
                    boolean b;
                    if (n != 3) {
                        if (n != 4) {
                            if (n != 5) {
                                return false;
                            }
                            b = this.isGoogleOrPlatformSigned(com.google.android.gms.internal.stable.zzc.zza(parcel, GoogleCertificatesQuery.CREATOR), IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()));
                        }
                        else {
                            b = this.isGoogleSigned(parcel.readString(), IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()));
                        }
                    }
                    else {
                        b = this.isGoogleReleaseSigned(parcel.readString(), IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()));
                    }
                    parcel2.writeNoException();
                    com.google.android.gms.internal.stable.zzc.zza(parcel2, b);
                    return true;
                }
                objectWrapper = this.getGoogleReleaseCertificates();
            }
            else {
                objectWrapper = this.getGoogleCertificates();
            }
            parcel2.writeNoException();
            com.google.android.gms.internal.stable.zzc.zza(parcel2, (IInterface)objectWrapper);
            return true;
        }
        
        public static class Proxy extends zza implements IGoogleCertificatesApi
        {
            Proxy(final IBinder binder) {
                super(binder, "com.google.android.gms.common.internal.IGoogleCertificatesApi");
            }
            
            @Override
            public IObjectWrapper getGoogleCertificates() throws RemoteException {
                final Parcel transactAndReadException = this.transactAndReadException(1, this.obtainAndWriteInterfaceToken());
                final IObjectWrapper interface1 = IObjectWrapper.Stub.asInterface(transactAndReadException.readStrongBinder());
                transactAndReadException.recycle();
                return interface1;
            }
            
            @Override
            public IObjectWrapper getGoogleReleaseCertificates() throws RemoteException {
                final Parcel transactAndReadException = this.transactAndReadException(2, this.obtainAndWriteInterfaceToken());
                final IObjectWrapper interface1 = IObjectWrapper.Stub.asInterface(transactAndReadException.readStrongBinder());
                transactAndReadException.recycle();
                return interface1;
            }
            
            @Override
            public boolean isGoogleOrPlatformSigned(final GoogleCertificatesQuery googleCertificatesQuery, final IObjectWrapper objectWrapper) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (Parcelable)googleCertificatesQuery);
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (IInterface)objectWrapper);
                final Parcel transactAndReadException = this.transactAndReadException(5, obtainAndWriteInterfaceToken);
                final boolean zza = com.google.android.gms.internal.stable.zzc.zza(transactAndReadException);
                transactAndReadException.recycle();
                return zza;
            }
            
            @Override
            public boolean isGoogleReleaseSigned(final String s, final IObjectWrapper objectWrapper) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                obtainAndWriteInterfaceToken.writeString(s);
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (IInterface)objectWrapper);
                final Parcel transactAndReadException = this.transactAndReadException(3, obtainAndWriteInterfaceToken);
                final boolean zza = com.google.android.gms.internal.stable.zzc.zza(transactAndReadException);
                transactAndReadException.recycle();
                return zza;
            }
            
            @Override
            public boolean isGoogleSigned(final String s, final IObjectWrapper objectWrapper) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                obtainAndWriteInterfaceToken.writeString(s);
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (IInterface)objectWrapper);
                final Parcel transactAndReadException = this.transactAndReadException(4, obtainAndWriteInterfaceToken);
                final boolean zza = com.google.android.gms.internal.stable.zzc.zza(transactAndReadException);
                transactAndReadException.recycle();
                return zza;
            }
        }
    }
}
