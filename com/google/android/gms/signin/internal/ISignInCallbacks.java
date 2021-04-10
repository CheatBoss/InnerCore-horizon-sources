package com.google.android.gms.signin.internal;

import com.google.android.gms.common.*;
import com.google.android.gms.common.api.*;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.internal.stable.*;
import android.os.*;

public interface ISignInCallbacks extends IInterface
{
    void onAuthAccountComplete(final ConnectionResult p0, final AuthAccountResult p1) throws RemoteException;
    
    void onGetCurrentAccountComplete(final Status p0, final GoogleSignInAccount p1) throws RemoteException;
    
    void onRecordConsentComplete(final Status p0) throws RemoteException;
    
    void onSaveAccountToSessionStoreComplete(final Status p0) throws RemoteException;
    
    void onSignInComplete(final SignInResponse p0) throws RemoteException;
    
    public abstract static class Stub extends zzb implements ISignInCallbacks
    {
        public Stub() {
            super("com.google.android.gms.signin.internal.ISignInCallbacks");
        }
        
        public static ISignInCallbacks asInterface(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.signin.internal.ISignInCallbacks");
            if (queryLocalInterface instanceof ISignInCallbacks) {
                return (ISignInCallbacks)queryLocalInterface;
            }
            return new Proxy(binder);
        }
        
        @Override
        protected boolean dispatchTransaction(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            if (n != 3) {
                if (n != 4) {
                    if (n != 6) {
                        if (n != 7) {
                            if (n != 8) {
                                return false;
                            }
                            this.onSignInComplete(com.google.android.gms.internal.stable.zzc.zza(parcel, SignInResponse.CREATOR));
                        }
                        else {
                            this.onGetCurrentAccountComplete(com.google.android.gms.internal.stable.zzc.zza(parcel, Status.CREATOR), com.google.android.gms.internal.stable.zzc.zza(parcel, GoogleSignInAccount.CREATOR));
                        }
                    }
                    else {
                        this.onRecordConsentComplete(com.google.android.gms.internal.stable.zzc.zza(parcel, Status.CREATOR));
                    }
                }
                else {
                    this.onSaveAccountToSessionStoreComplete(com.google.android.gms.internal.stable.zzc.zza(parcel, Status.CREATOR));
                }
            }
            else {
                this.onAuthAccountComplete(com.google.android.gms.internal.stable.zzc.zza(parcel, ConnectionResult.CREATOR), com.google.android.gms.internal.stable.zzc.zza(parcel, AuthAccountResult.CREATOR));
            }
            parcel2.writeNoException();
            return true;
        }
        
        public static class Proxy extends zza implements ISignInCallbacks
        {
            Proxy(final IBinder binder) {
                super(binder, "com.google.android.gms.signin.internal.ISignInCallbacks");
            }
            
            @Override
            public void onAuthAccountComplete(final ConnectionResult connectionResult, final AuthAccountResult authAccountResult) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (Parcelable)connectionResult);
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (Parcelable)authAccountResult);
                this.transactAndReadExceptionReturnVoid(3, obtainAndWriteInterfaceToken);
            }
            
            @Override
            public void onGetCurrentAccountComplete(final Status status, final GoogleSignInAccount googleSignInAccount) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (Parcelable)status);
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (Parcelable)googleSignInAccount);
                this.transactAndReadExceptionReturnVoid(7, obtainAndWriteInterfaceToken);
            }
            
            @Override
            public void onRecordConsentComplete(final Status status) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (Parcelable)status);
                this.transactAndReadExceptionReturnVoid(6, obtainAndWriteInterfaceToken);
            }
            
            @Override
            public void onSaveAccountToSessionStoreComplete(final Status status) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (Parcelable)status);
                this.transactAndReadExceptionReturnVoid(4, obtainAndWriteInterfaceToken);
            }
            
            @Override
            public void onSignInComplete(final SignInResponse signInResponse) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (Parcelable)signInResponse);
                this.transactAndReadExceptionReturnVoid(8, obtainAndWriteInterfaceToken);
            }
        }
    }
}
