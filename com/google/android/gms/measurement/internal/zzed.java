package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.safeparcel.*;

final class zzed implements Runnable
{
    private final /* synthetic */ zzh zzaqn;
    private final /* synthetic */ zzfh zzaqs;
    private final /* synthetic */ zzdr zzasg;
    private final /* synthetic */ boolean zzasj;
    
    zzed(final zzdr zzasg, final boolean zzasj, final zzfh zzaqs, final zzh zzaqn) {
        this.zzasg = zzasg;
        this.zzasj = zzasj;
        this.zzaqs = zzaqs;
        this.zzaqn = zzaqn;
    }
    
    @Override
    public final void run() {
        final zzag zzd = this.zzasg.zzasa;
        if (zzd == null) {
            this.zzasg.zzgo().zzjd().zzbx("Discarding data. Failed to set user attribute");
            return;
        }
        final zzdr zzasg = this.zzasg;
        AbstractSafeParcelable zzaqs;
        if (this.zzasj) {
            zzaqs = null;
        }
        else {
            zzaqs = this.zzaqs;
        }
        zzasg.zza(zzd, zzaqs, this.zzaqn);
        this.zzasg.zzcy();
    }
}
