package com.google.android.gms.signin.internal;

import android.accounts.*;
import com.google.android.gms.common.internal.*;
import com.google.android.gms.internal.stable.*;
import android.os.*;

public interface ISignInService extends IInterface
{
    void authAccount(final AuthAccountRequest p0, final ISignInCallbacks p1) throws RemoteException;
    
    void clearAccountFromSessionStore(final int p0) throws RemoteException;
    
    void getCurrentAccount(final ISignInCallbacks p0) throws RemoteException;
    
    void onCheckServerAuthorization(final CheckServerAuthResult p0) throws RemoteException;
    
    void onUploadServerAuthCode(final boolean p0) throws RemoteException;
    
    void recordConsent(final RecordConsentRequest p0, final ISignInCallbacks p1) throws RemoteException;
    
    void resolveAccount(final ResolveAccountRequest p0, final IResolveAccountCallbacks p1) throws RemoteException;
    
    void saveAccountToSessionStore(final int p0, final Account p1, final ISignInCallbacks p2) throws RemoteException;
    
    void saveDefaultAccountToSharedPref(final IAccountAccessor p0, final int p1, final boolean p2) throws RemoteException;
    
    void setGamesHasBeenGreeted(final boolean p0) throws RemoteException;
    
    void signIn(final SignInRequest p0, final ISignInCallbacks p1) throws RemoteException;
    
    public abstract static class Stub extends zzb implements ISignInService
    {
        public static ISignInService asInterface(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.signin.internal.ISignInService");
            if (queryLocalInterface instanceof ISignInService) {
                return (ISignInService)queryLocalInterface;
            }
            return new Proxy(binder);
        }
        
        @Override
        protected boolean dispatchTransaction(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            switch (n) {
                default: {
                    return false;
                }
                case 13: {
                    this.setGamesHasBeenGreeted(com.google.android.gms.internal.stable.zzc.zza(parcel));
                    break;
                }
                case 12: {
                    this.signIn(com.google.android.gms.internal.stable.zzc.zza(parcel, SignInRequest.CREATOR), ISignInCallbacks.Stub.asInterface(parcel.readStrongBinder()));
                    break;
                }
                case 11: {
                    this.getCurrentAccount(ISignInCallbacks.Stub.asInterface(parcel.readStrongBinder()));
                    break;
                }
                case 10: {
                    this.recordConsent(com.google.android.gms.internal.stable.zzc.zza(parcel, RecordConsentRequest.CREATOR), ISignInCallbacks.Stub.asInterface(parcel.readStrongBinder()));
                    break;
                }
                case 9: {
                    this.saveDefaultAccountToSharedPref(IAccountAccessor.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt(), com.google.android.gms.internal.stable.zzc.zza(parcel));
                    break;
                }
                case 8: {
                    this.saveAccountToSessionStore(parcel.readInt(), com.google.android.gms.internal.stable.zzc.zza(parcel, (android.os.Parcelable$Creator<Account>)Account.CREATOR), ISignInCallbacks.Stub.asInterface(parcel.readStrongBinder()));
                    break;
                }
                case 7: {
                    this.clearAccountFromSessionStore(parcel.readInt());
                    break;
                }
                case 5: {
                    this.resolveAccount(com.google.android.gms.internal.stable.zzc.zza(parcel, ResolveAccountRequest.CREATOR), IResolveAccountCallbacks.Stub.asInterface(parcel.readStrongBinder()));
                    break;
                }
                case 4: {
                    this.onUploadServerAuthCode(com.google.android.gms.internal.stable.zzc.zza(parcel));
                    break;
                }
                case 3: {
                    this.onCheckServerAuthorization(com.google.android.gms.internal.stable.zzc.zza(parcel, CheckServerAuthResult.CREATOR));
                    break;
                }
                case 2: {
                    this.authAccount(com.google.android.gms.internal.stable.zzc.zza(parcel, AuthAccountRequest.CREATOR), ISignInCallbacks.Stub.asInterface(parcel.readStrongBinder()));
                    break;
                }
            }
            parcel2.writeNoException();
            return true;
        }
        
        public static class Proxy extends zza implements ISignInService
        {
            Proxy(final IBinder binder) {
                super(binder, "com.google.android.gms.signin.internal.ISignInService");
            }
            
            @Override
            public void authAccount(final AuthAccountRequest authAccountRequest, final ISignInCallbacks signInCallbacks) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (Parcelable)authAccountRequest);
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (IInterface)signInCallbacks);
                this.transactAndReadExceptionReturnVoid(2, obtainAndWriteInterfaceToken);
            }
            
            @Override
            public void clearAccountFromSessionStore(final int n) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                obtainAndWriteInterfaceToken.writeInt(n);
                this.transactAndReadExceptionReturnVoid(7, obtainAndWriteInterfaceToken);
            }
            
            @Override
            public void getCurrentAccount(final ISignInCallbacks signInCallbacks) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (IInterface)signInCallbacks);
                this.transactAndReadExceptionReturnVoid(11, obtainAndWriteInterfaceToken);
            }
            
            @Override
            public void onCheckServerAuthorization(final CheckServerAuthResult checkServerAuthResult) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (Parcelable)checkServerAuthResult);
                this.transactAndReadExceptionReturnVoid(3, obtainAndWriteInterfaceToken);
            }
            
            @Override
            public void onUploadServerAuthCode(final boolean b) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, b);
                this.transactAndReadExceptionReturnVoid(4, obtainAndWriteInterfaceToken);
            }
            
            @Override
            public void recordConsent(final RecordConsentRequest recordConsentRequest, final ISignInCallbacks signInCallbacks) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (Parcelable)recordConsentRequest);
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (IInterface)signInCallbacks);
                this.transactAndReadExceptionReturnVoid(10, obtainAndWriteInterfaceToken);
            }
            
            @Override
            public void resolveAccount(final ResolveAccountRequest resolveAccountRequest, final IResolveAccountCallbacks resolveAccountCallbacks) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (Parcelable)resolveAccountRequest);
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (IInterface)resolveAccountCallbacks);
                this.transactAndReadExceptionReturnVoid(5, obtainAndWriteInterfaceToken);
            }
            
            @Override
            public void saveAccountToSessionStore(final int n, final Account account, final ISignInCallbacks signInCallbacks) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                obtainAndWriteInterfaceToken.writeInt(n);
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (Parcelable)account);
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (IInterface)signInCallbacks);
                this.transactAndReadExceptionReturnVoid(8, obtainAndWriteInterfaceToken);
            }
            
            @Override
            public void saveDefaultAccountToSharedPref(final IAccountAccessor accountAccessor, final int n, final boolean b) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (IInterface)accountAccessor);
                obtainAndWriteInterfaceToken.writeInt(n);
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, b);
                this.transactAndReadExceptionReturnVoid(9, obtainAndWriteInterfaceToken);
            }
            
            @Override
            public void setGamesHasBeenGreeted(final boolean b) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, b);
                this.transactAndReadExceptionReturnVoid(13, obtainAndWriteInterfaceToken);
            }
            
            @Override
            public void signIn(final SignInRequest signInRequest, final ISignInCallbacks signInCallbacks) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (Parcelable)signInRequest);
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, (IInterface)signInCallbacks);
                this.transactAndReadExceptionReturnVoid(12, obtainAndWriteInterfaceToken);
            }
        }
    }
}
