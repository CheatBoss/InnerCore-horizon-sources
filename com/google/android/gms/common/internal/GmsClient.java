package com.google.android.gms.common.internal;

import android.accounts.*;
import android.content.*;
import android.os.*;
import com.google.android.gms.common.api.*;
import java.util.*;
import com.google.android.gms.common.*;

public abstract class GmsClient<T extends IInterface> extends BaseGmsClient<T> implements Client
{
    private final Set<Scope> mScopes;
    private final ClientSettings zzgf;
    private final Account zzs;
    
    protected GmsClient(final Context context, final Looper looper, final int n, final ClientSettings clientSettings, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        this(context, looper, GmsClientSupervisor.getInstance(context), GoogleApiAvailability.getInstance(), n, clientSettings, Preconditions.checkNotNull(connectionCallbacks), Preconditions.checkNotNull(onConnectionFailedListener));
    }
    
    protected GmsClient(final Context context, final Looper looper, final GmsClientSupervisor gmsClientSupervisor, final GoogleApiAvailability googleApiAvailability, final int n, final ClientSettings zzgf, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, gmsClientSupervisor, googleApiAvailability, n, zza(connectionCallbacks), zza(onConnectionFailedListener), zzgf.getRealClientClassName());
        this.zzgf = zzgf;
        this.zzs = zzgf.getAccount();
        this.mScopes = this.zza(zzgf.getAllRequestedScopes());
    }
    
    private static BaseConnectionCallbacks zza(final GoogleApiClient.ConnectionCallbacks connectionCallbacks) {
        if (connectionCallbacks == null) {
            return null;
        }
        return new zzf(connectionCallbacks);
    }
    
    private static BaseOnConnectionFailedListener zza(final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        if (onConnectionFailedListener == null) {
            return null;
        }
        return new zzg(onConnectionFailedListener);
    }
    
    private final Set<Scope> zza(final Set<Scope> set) {
        final Set<Scope> validateScopes = this.validateScopes(set);
        final Iterator<Scope> iterator = validateScopes.iterator();
        while (iterator.hasNext()) {
            if (set.contains(iterator.next())) {
                continue;
            }
            throw new IllegalStateException("Expanding scopes is not permitted, use implied scopes instead");
        }
        return validateScopes;
    }
    
    @Override
    public final Account getAccount() {
        return this.zzs;
    }
    
    @Override
    public int getMinApkVersion() {
        return super.getMinApkVersion();
    }
    
    @Override
    public Feature[] getRequiredFeatures() {
        return new Feature[0];
    }
    
    @Override
    protected final Set<Scope> getScopes() {
        return this.mScopes;
    }
    
    protected Set<Scope> validateScopes(final Set<Scope> set) {
        return set;
    }
}
