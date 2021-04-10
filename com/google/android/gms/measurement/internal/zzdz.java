package com.google.android.gms.measurement.internal;

import android.text.*;
import android.os.*;
import com.google.android.gms.common.internal.safeparcel.*;

final class zzdz implements Runnable
{
    private final /* synthetic */ zzh zzaqn;
    private final /* synthetic */ String zzaqq;
    private final /* synthetic */ zzad zzaqr;
    private final /* synthetic */ zzdr zzasg;
    private final /* synthetic */ boolean zzasi;
    private final /* synthetic */ boolean zzasj;
    
    zzdz(final zzdr zzasg, final boolean zzasi, final boolean zzasj, final zzad zzaqr, final zzh zzaqn, final String zzaqq) {
        this.zzasg = zzasg;
        this.zzasi = zzasi;
        this.zzasj = zzasj;
        this.zzaqr = zzaqr;
        this.zzaqn = zzaqn;
        this.zzaqq = zzaqq;
    }
    
    @Override
    public final void run() {
        final zzag zzd = this.zzasg.zzasa;
        if (zzd == null) {
            this.zzasg.zzgo().zzjd().zzbx("Discarding data. Failed to send event to service");
            return;
        }
        if (this.zzasi) {
            final zzdr zzasg = this.zzasg;
            AbstractSafeParcelable zzaqr;
            if (this.zzasj) {
                zzaqr = null;
            }
            else {
                zzaqr = this.zzaqr;
            }
            zzasg.zza(zzd, zzaqr, this.zzaqn);
        }
        else {
            try {
                if (TextUtils.isEmpty((CharSequence)this.zzaqq)) {
                    zzd.zza(this.zzaqr, this.zzaqn);
                }
                else {
                    zzd.zza(this.zzaqr, this.zzaqq, this.zzasg.zzgo().zzjn());
                }
            }
            catch (RemoteException ex) {
                this.zzasg.zzgo().zzjd().zzg("Failed to send event to the service", ex);
            }
        }
        this.zzasg.zzcy();
    }
}
