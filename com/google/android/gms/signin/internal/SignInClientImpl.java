package com.google.android.gms.signin.internal;

import android.content.*;
import com.google.android.gms.common.api.*;
import com.google.android.gms.signin.*;
import com.google.android.gms.auth.api.signin.internal.*;
import com.google.android.gms.common.internal.*;
import android.util.*;
import android.os.*;
import android.accounts.*;
import com.google.android.gms.auth.api.signin.*;

public class SignInClientImpl extends GmsClient<ISignInService> implements SignInClient
{
    private final Bundle zzada;
    private final boolean zzads;
    private final ClientSettings zzgf;
    private Integer zzsc;
    
    public SignInClientImpl(final Context context, final Looper looper, final boolean zzads, final ClientSettings zzgf, final Bundle zzada, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 44, zzgf, connectionCallbacks, onConnectionFailedListener);
        this.zzads = zzads;
        this.zzgf = zzgf;
        this.zzada = zzada;
        this.zzsc = zzgf.getClientSessionId();
    }
    
    public SignInClientImpl(final Context context, final Looper looper, final boolean b, final ClientSettings clientSettings, final SignInOptions signInOptions, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        this(context, looper, b, clientSettings, createBundleFromClientSettings(clientSettings), connectionCallbacks, onConnectionFailedListener);
    }
    
    public static Bundle createBundleFromClientSettings(final ClientSettings clientSettings) {
        final SignInOptions signInOptions = clientSettings.getSignInOptions();
        final Integer clientSessionId = clientSettings.getClientSessionId();
        final Bundle bundle = new Bundle();
        bundle.putParcelable("com.google.android.gms.signin.internal.clientRequestedAccount", (Parcelable)clientSettings.getAccount());
        if (clientSessionId != null) {
            bundle.putInt("com.google.android.gms.common.internal.ClientSettings.sessionId", (int)clientSessionId);
        }
        if (signInOptions != null) {
            bundle.putBoolean("com.google.android.gms.signin.internal.offlineAccessRequested", signInOptions.isOfflineAccessRequested());
            bundle.putBoolean("com.google.android.gms.signin.internal.idTokenRequested", signInOptions.isIdTokenRequested());
            bundle.putString("com.google.android.gms.signin.internal.serverClientId", signInOptions.getServerClientId());
            bundle.putBoolean("com.google.android.gms.signin.internal.usePromptModeForAuthCode", true);
            bundle.putBoolean("com.google.android.gms.signin.internal.forceCodeForRefreshToken", signInOptions.isForceCodeForRefreshToken());
            bundle.putString("com.google.android.gms.signin.internal.hostedDomain", signInOptions.getHostedDomain());
            bundle.putBoolean("com.google.android.gms.signin.internal.waitForAccessTokenRefresh", signInOptions.waitForAccessTokenRefresh());
            if (signInOptions.getAuthApiSignInModuleVersion() != null) {
                bundle.putLong("com.google.android.gms.signin.internal.authApiSignInModuleVersion", (long)signInOptions.getAuthApiSignInModuleVersion());
            }
            if (signInOptions.getRealClientLibraryVersion() != null) {
                bundle.putLong("com.google.android.gms.signin.internal.realClientLibraryVersion", (long)signInOptions.getRealClientLibraryVersion());
            }
        }
        return bundle;
    }
    
    @Override
    public void connect() {
        this.connect((ConnectionProgressReportCallbacks)new LegacyClientCallbackAdapter());
    }
    
    @Override
    protected ISignInService createServiceInterface(final IBinder binder) {
        return ISignInService.Stub.asInterface(binder);
    }
    
    @Override
    protected Bundle getGetServiceRequestExtraArgs() {
        if (!this.getContext().getPackageName().equals(this.zzgf.getRealClientPackageName())) {
            this.zzada.putString("com.google.android.gms.signin.internal.realClientPackageName", this.zzgf.getRealClientPackageName());
        }
        return this.zzada;
    }
    
    @Override
    public int getMinApkVersion() {
        return 12451000;
    }
    
    @Override
    protected String getServiceDescriptor() {
        return "com.google.android.gms.signin.internal.ISignInService";
    }
    
    @Override
    protected String getStartServiceAction() {
        return "com.google.android.gms.signin.service.START";
    }
    
    @Override
    public boolean requiresSignIn() {
        return this.zzads;
    }
    
    @Override
    public void signIn(final ISignInCallbacks signInCallbacks) {
        Preconditions.checkNotNull(signInCallbacks, "Expecting a valid ISignInCallbacks");
        try {
            final Account accountOrDefault = this.zzgf.getAccountOrDefault();
            GoogleSignInAccount savedDefaultGoogleSignInAccount = null;
            if ("<<default account>>".equals(accountOrDefault.name)) {
                savedDefaultGoogleSignInAccount = Storage.getInstance(this.getContext()).getSavedDefaultGoogleSignInAccount();
            }
            this.getService().signIn(new SignInRequest(new ResolveAccountRequest(accountOrDefault, this.zzsc, savedDefaultGoogleSignInAccount)), signInCallbacks);
        }
        catch (RemoteException ex) {
            Log.w("SignInClientImpl", "Remote service probably died when signIn is called");
            try {
                signInCallbacks.onSignInComplete(new SignInResponse(8));
            }
            catch (RemoteException ex2) {
                Log.wtf("SignInClientImpl", "ISignInCallbacks#onSignInComplete should be executed from the same process, unexpected RemoteException.", (Throwable)ex);
            }
        }
    }
}
