package com.google.android.gms.common.api.internal;

import android.content.*;
import java.util.*;
import com.google.android.gms.common.api.*;
import com.google.android.gms.signin.*;
import android.util.*;
import com.google.android.gms.common.*;
import com.google.android.gms.common.internal.*;
import com.google.android.gms.signin.internal.*;
import android.os.*;

public final class zzby extends BaseSignInCallbacks implements ConnectionCallbacks, OnConnectionFailedListener
{
    private static Api.AbstractClientBuilder<? extends SignInClient, SignInOptions> zzlv;
    private final Context mContext;
    private final Handler mHandler;
    private Set<Scope> mScopes;
    private final Api.AbstractClientBuilder<? extends SignInClient, SignInOptions> zzby;
    private ClientSettings zzgf;
    private SignInClient zzhn;
    private zzcb zzlw;
    
    static {
        zzby.zzlv = SignIn.CLIENT_BUILDER;
    }
    
    public zzby(final Context context, final Handler handler, final ClientSettings clientSettings) {
        this(context, handler, clientSettings, com.google.android.gms.common.api.internal.zzby.zzlv);
    }
    
    public zzby(final Context mContext, final Handler mHandler, final ClientSettings clientSettings, final Api.AbstractClientBuilder<? extends SignInClient, SignInOptions> zzby) {
        this.mContext = mContext;
        this.mHandler = mHandler;
        this.zzgf = Preconditions.checkNotNull(clientSettings, "ClientSettings must not be null");
        this.mScopes = clientSettings.getRequiredScopes();
        this.zzby = zzby;
    }
    
    private final void zzb(final SignInResponse signInResponse) {
        Label_0117: {
            ConnectionResult connectionResult;
            if ((connectionResult = signInResponse.getConnectionResult()).isSuccess()) {
                final ResolveAccountResponse resolveAccountResponse = signInResponse.getResolveAccountResponse();
                connectionResult = resolveAccountResponse.getConnectionResult();
                if (connectionResult.isSuccess()) {
                    this.zzlw.zza(resolveAccountResponse.getAccountAccessor(), this.mScopes);
                    break Label_0117;
                }
                final String value = String.valueOf(connectionResult);
                final StringBuilder sb = new StringBuilder(String.valueOf(value).length() + 48);
                sb.append("Sign-in succeeded with resolve account failure: ");
                sb.append(value);
                Log.wtf("SignInCoordinator", sb.toString(), (Throwable)new Exception());
            }
            this.zzlw.zzg(connectionResult);
        }
        ((Api.Client)this.zzhn).disconnect();
    }
    
    @Override
    public final void onConnected(final Bundle bundle) {
        this.zzhn.signIn(this);
    }
    
    @Override
    public final void onConnectionFailed(final ConnectionResult connectionResult) {
        this.zzlw.zzg(connectionResult);
    }
    
    @Override
    public final void onConnectionSuspended(final int n) {
        ((Api.Client)this.zzhn).disconnect();
    }
    
    @Override
    public final void onSignInComplete(final SignInResponse signInResponse) {
        this.mHandler.post((Runnable)new zzca(this, signInResponse));
    }
    
    public final void zza(final zzcb zzlw) {
        final SignInClient zzhn = this.zzhn;
        if (zzhn != null) {
            ((Api.Client)zzhn).disconnect();
        }
        this.zzgf.setClientSessionId(System.identityHashCode(this));
        final Api.AbstractClientBuilder<? extends SignInClient, SignInOptions> zzby = this.zzby;
        final Context mContext = this.mContext;
        final Looper looper = this.mHandler.getLooper();
        final ClientSettings zzgf = this.zzgf;
        this.zzhn = (SignInClient)zzby.buildClient(mContext, looper, zzgf, zzgf.getSignInOptions(), this, this);
        this.zzlw = zzlw;
        final Set<Scope> mScopes = this.mScopes;
        if (mScopes != null && !mScopes.isEmpty()) {
            this.zzhn.connect();
            return;
        }
        this.mHandler.post((Runnable)new zzbz(this));
    }
    
    public final void zzbz() {
        final SignInClient zzhn = this.zzhn;
        if (zzhn != null) {
            ((Api.Client)zzhn).disconnect();
        }
    }
}
