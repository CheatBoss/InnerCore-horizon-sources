package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.safeparcel.*;
import android.os.*;

final class zzdv implements Runnable
{
    private final /* synthetic */ zzh zzaqn;
    private final /* synthetic */ zzdr zzasg;
    
    zzdv(final zzdr zzasg, final zzh zzaqn) {
        this.zzasg = zzasg;
        this.zzaqn = zzaqn;
    }
    
    @Override
    public final void run() {
        final zzag zzd = this.zzasg.zzasa;
        if (zzd == null) {
            this.zzasg.zzgo().zzjd().zzbx("Discarding data. Failed to send app launch");
            return;
        }
        try {
            zzd.zza(this.zzaqn);
            this.zzasg.zza(zzd, null, this.zzaqn);
            this.zzasg.zzcy();
        }
        catch (RemoteException ex) {
            this.zzasg.zzgo().zzjd().zzg("Failed to send app launch to the service", ex);
        }
    }
}
