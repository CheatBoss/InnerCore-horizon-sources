package com.google.android.gms.common.api.internal;

import com.google.android.gms.tasks.*;
import com.google.android.gms.common.api.*;
import android.os.*;

public final class zzg extends zzc<Boolean>
{
    private final ListenerHolder.ListenerKey<?> zzea;
    
    public zzg(final ListenerHolder.ListenerKey<?> zzea, final TaskCompletionSource<Boolean> taskCompletionSource) {
        super(4, taskCompletionSource);
        this.zzea = zzea;
    }
    
    public final void zzb(final GoogleApiManager.zza<?> zza) throws RemoteException {
        final zzbv zzbv = zza.zzbn().remove(this.zzea);
        if (zzbv != null) {
            zzbv.zzlu.unregisterListener(zza.zzae(), (TaskCompletionSource<Boolean>)this.zzdu);
            zzbv.zzlt.clearListener();
            return;
        }
        this.zzdu.trySetResult((T)false);
    }
}
