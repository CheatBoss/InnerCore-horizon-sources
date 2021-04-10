package com.google.android.gms.common.api.internal;

import com.google.android.gms.tasks.*;
import com.google.android.gms.common.*;
import com.google.android.gms.common.api.*;
import android.os.*;

public final class zzf<ResultT> extends zzb
{
    private final TaskCompletionSource<ResultT> zzdu;
    private final TaskApiCall<Api.AnyClient, ResultT> zzdy;
    private final StatusExceptionMapper zzdz;
    
    public final Feature[] getRequiredFeatures() {
        return this.zzdy.zzca();
    }
    
    public final boolean shouldAutoResolveMissingFeatures() {
        return this.zzdy.shouldAutoResolveMissingFeatures();
    }
    
    @Override
    public final void zza(final Status status) {
        this.zzdu.trySetException(this.zzdz.getException(status));
    }
    
    @Override
    public final void zza(final GoogleApiManager.zza<?> zza) throws DeadObjectException {
        try {
            this.zzdy.doExecute(zza.zzae(), this.zzdu);
        }
        catch (RuntimeException ex) {
            this.zza(ex);
        }
        catch (RemoteException ex2) {
            this.zza(zza(ex2));
        }
        catch (DeadObjectException ex3) {
            throw ex3;
        }
    }
    
    @Override
    public final void zza(final zzaa zzaa, final boolean b) {
        zzaa.zza(this.zzdu, b);
    }
    
    @Override
    public final void zza(final RuntimeException ex) {
        this.zzdu.trySetException(ex);
    }
}
