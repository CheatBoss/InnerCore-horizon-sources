package com.google.android.gms.common.internal;

import com.google.android.gms.internal.stable.*;
import android.os.*;

public interface IGmsCallbacks extends IInterface
{
    void onAccountValidationComplete(final int p0, final Bundle p1) throws RemoteException;
    
    void onPostInitComplete(final int p0, final IBinder p1, final Bundle p2) throws RemoteException;
    
    void onPostInitCompleteWithConnectionInfo(final int p0, final IBinder p1, final ConnectionInfo p2) throws RemoteException;
    
    public abstract static class Stub extends zzb implements IGmsCallbacks
    {
        public Stub() {
            super("com.google.android.gms.common.internal.IGmsCallbacks");
        }
        
        public static IGmsCallbacks asInterface(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.common.internal.IGmsCallbacks");
            if (queryLocalInterface instanceof IGmsCallbacks) {
                return (IGmsCallbacks)queryLocalInterface;
            }
            return new Proxy(binder);
        }
        
        @Override
        protected boolean dispatchTransaction(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            if (n != 1) {
                if (n != 2) {
                    if (n != 3) {
                        return false;
                    }
                    this.onPostInitCompleteWithConnectionInfo(parcel.readInt(), parcel.readStrongBinder(), com.google.android.gms.internal.stable.zzc.zza(parcel, ConnectionInfo.CREATOR));
                }
                else {
                    this.onAccountValidationComplete(parcel.readInt(), com.google.android.gms.internal.stable.zzc.zza(parcel, (android.os.Parcelable$Creator<Bundle>)Bundle.CREATOR));
                }
            }
            else {
                this.onPostInitComplete(parcel.readInt(), parcel.readStrongBinder(), com.google.android.gms.internal.stable.zzc.zza(parcel, (android.os.Parcelable$Creator<Bundle>)Bundle.CREATOR));
            }
            parcel2.writeNoException();
            return true;
        }
        
        public static class Proxy extends zza implements IGmsCallbacks
        {
            Proxy(final IBinder binder) {
                super(binder, "com.google.android.gms.common.internal.IGmsCallbacks");
            }
            
            @Override
            public void onAccountValidationComplete(final int n, final Bundle bundle) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                obtainAndWriteInterfaceToken.writeInt(n);
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (Parcelable)bundle);
                this.transactAndReadExceptionReturnVoid(2, obtainAndWriteInterfaceToken);
            }
            
            @Override
            public void onPostInitComplete(final int n, final IBinder binder, final Bundle bundle) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                obtainAndWriteInterfaceToken.writeInt(n);
                obtainAndWriteInterfaceToken.writeStrongBinder(binder);
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (Parcelable)bundle);
                this.transactAndReadExceptionReturnVoid(1, obtainAndWriteInterfaceToken);
            }
            
            @Override
            public void onPostInitCompleteWithConnectionInfo(final int n, final IBinder binder, final ConnectionInfo connectionInfo) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                obtainAndWriteInterfaceToken.writeInt(n);
                obtainAndWriteInterfaceToken.writeStrongBinder(binder);
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (Parcelable)connectionInfo);
                this.transactAndReadExceptionReturnVoid(3, obtainAndWriteInterfaceToken);
            }
        }
    }
}
