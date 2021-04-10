package com.google.android.gms.signin.internal;

import com.google.android.gms.common.*;
import android.os.*;
import com.google.android.gms.common.api.*;
import com.google.android.gms.auth.api.signin.*;

public class BaseSignInCallbacks extends Stub
{
    @Override
    public void onAuthAccountComplete(final ConnectionResult connectionResult, final AuthAccountResult authAccountResult) throws RemoteException {
    }
    
    @Override
    public void onGetCurrentAccountComplete(final Status status, final GoogleSignInAccount googleSignInAccount) throws RemoteException {
    }
    
    @Override
    public void onRecordConsentComplete(final Status status) throws RemoteException {
    }
    
    @Override
    public void onSaveAccountToSessionStoreComplete(final Status status) throws RemoteException {
    }
    
    @Override
    public void onSignInComplete(final SignInResponse signInResponse) throws RemoteException {
    }
}
