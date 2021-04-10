package com.google.android.gms.common.api.internal;

import com.google.android.gms.tasks.*;
import com.google.android.gms.common.api.*;
import android.os.*;

abstract class zzc<T> extends zzb
{
    protected final TaskCompletionSource<T> zzdu;
    
    public zzc(final int n, final TaskCompletionSource<T> zzdu) {
        super(n);
        this.zzdu = zzdu;
    }
    
    @Override
    public void zza(final Status status) {
        this.zzdu.trySetException(new ApiException(status));
    }
    
    @Override
    public final void zza(final GoogleApiManager.zza<?> zza) throws DeadObjectException {
        try {
            this.zzb(zza);
        }
        catch (RuntimeException ex) {
            this.zza(ex);
        }
        catch (RemoteException ex2) {
            this.zza(zza(ex2));
        }
        catch (DeadObjectException ex3) {
            this.zza(zza((RemoteException)ex3));
            throw ex3;
        }
    }
    
    @Override
    public void zza(final zzaa zzaa, final boolean b) {
    }
    
    @Override
    public void zza(final RuntimeException ex) {
        this.zzdu.trySetException(ex);
    }
    
    protected abstract void zzb(final GoogleApiManager.zza<?> p0) throws RemoteException;
}
