package com.google.android.gms.common.api;

import android.content.*;
import com.google.android.gms.common.internal.*;
import com.google.android.gms.auth.api.signin.*;
import android.accounts.*;
import java.util.*;
import android.os.*;
import com.google.android.gms.common.api.internal.*;

public class GoogleApi<O extends Api.ApiOptions>
{
    private final Api<O> mApi;
    private final Context mContext;
    private final int mId;
    private final O zzcl;
    private final zzh<O> zzcm;
    
    protected ClientSettings.Builder createClientSettingsBuilder() {
        final ClientSettings.Builder builder = new ClientSettings.Builder();
        final Api.ApiOptions zzcl = this.zzcl;
        Account account = null;
        Label_0069: {
            if (zzcl instanceof Api.ApiOptions.HasGoogleSignInAccountOptions) {
                final GoogleSignInAccount googleSignInAccount = ((Api.ApiOptions.HasGoogleSignInAccountOptions)zzcl).getGoogleSignInAccount();
                if (googleSignInAccount != null) {
                    account = googleSignInAccount.getAccount();
                    break Label_0069;
                }
            }
            final Api.ApiOptions zzcl2 = this.zzcl;
            if (zzcl2 instanceof Api.ApiOptions.HasAccountOptions) {
                account = ((Api.ApiOptions.HasAccountOptions)zzcl2).getAccount();
            }
            else {
                account = null;
            }
        }
        final ClientSettings.Builder setAccount = builder.setAccount(account);
        final Api.ApiOptions zzcl3 = this.zzcl;
        if (zzcl3 instanceof Api.ApiOptions.HasGoogleSignInAccountOptions) {
            final GoogleSignInAccount googleSignInAccount2 = ((Api.ApiOptions.HasGoogleSignInAccountOptions)zzcl3).getGoogleSignInAccount();
            if (googleSignInAccount2 != null) {
                final Set<Scope> set = googleSignInAccount2.getRequestedScopes();
                return setAccount.addAllRequiredScopes(set).setRealClientClassName(this.mContext.getClass().getName()).setRealClientPackageName(this.mContext.getPackageName());
            }
        }
        final Set<Scope> set = Collections.emptySet();
        return setAccount.addAllRequiredScopes(set).setRealClientClassName(this.mContext.getClass().getName()).setRealClientPackageName(this.mContext.getPackageName());
    }
    
    public final int getInstanceId() {
        return this.mId;
    }
    
    public Api.Client zza(final Looper looper, final GoogleApiManager.zza<O> zza) {
        return (Api.Client)this.mApi.zzk().buildClient(this.mContext, looper, this.createClientSettingsBuilder().build(), this.zzcl, zza, zza);
    }
    
    public zzby zza(final Context context, final Handler handler) {
        return new zzby(context, handler, this.createClientSettingsBuilder().build());
    }
    
    public final zzh<O> zzm() {
        return this.zzcm;
    }
}
