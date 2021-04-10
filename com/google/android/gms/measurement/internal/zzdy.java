package com.google.android.gms.measurement.internal;

import android.os.*;

final class zzdy implements Runnable
{
    private final /* synthetic */ zzh zzaqn;
    private final /* synthetic */ zzdr zzasg;
    
    zzdy(final zzdr zzasg, final zzh zzaqn) {
        this.zzasg = zzasg;
        this.zzaqn = zzaqn;
    }
    
    @Override
    public final void run() {
        final zzag zzd = this.zzasg.zzasa;
        if (zzd == null) {
            this.zzasg.zzgo().zzjd().zzbx("Failed to send measurementEnabled to service");
            return;
        }
        try {
            zzd.zzb(this.zzaqn);
            this.zzasg.zzcy();
        }
        catch (RemoteException ex) {
            this.zzasg.zzgo().zzjd().zzg("Failed to send measurementEnabled to the service", ex);
        }
    }
}
