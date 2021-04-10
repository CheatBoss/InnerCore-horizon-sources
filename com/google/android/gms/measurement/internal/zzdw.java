package com.google.android.gms.measurement.internal;

import android.os.*;

final class zzdw implements Runnable
{
    private final /* synthetic */ zzdn zzary;
    private final /* synthetic */ zzdr zzasg;
    
    zzdw(final zzdr zzasg, final zzdn zzary) {
        this.zzasg = zzasg;
        this.zzary = zzary;
    }
    
    @Override
    public final void run() {
        final zzag zzd = this.zzasg.zzasa;
        if (zzd == null) {
            this.zzasg.zzgo().zzjd().zzbx("Failed to send current screen to service");
            return;
        }
        try {
            long zzarm;
            String zzuw;
            String zzarl;
            String s;
            if (this.zzary == null) {
                zzarm = 0L;
                zzuw = null;
                zzarl = null;
                s = this.zzasg.getContext().getPackageName();
            }
            else {
                zzarm = this.zzary.zzarm;
                zzuw = this.zzary.zzuw;
                zzarl = this.zzary.zzarl;
                s = this.zzasg.getContext().getPackageName();
            }
            zzd.zza(zzarm, zzuw, zzarl, s);
            this.zzasg.zzcy();
        }
        catch (RemoteException ex) {
            this.zzasg.zzgo().zzjd().zzg("Failed to send current screen to the service", ex);
        }
    }
}
