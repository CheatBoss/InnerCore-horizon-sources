package com.google.android.gms.measurement.internal;

import android.text.*;
import android.os.*;
import com.google.android.gms.common.internal.safeparcel.*;

final class zzea implements Runnable
{
    private final /* synthetic */ zzh zzaqn;
    private final /* synthetic */ zzdr zzasg;
    private final /* synthetic */ boolean zzasi;
    private final /* synthetic */ boolean zzasj;
    private final /* synthetic */ zzl zzask;
    private final /* synthetic */ zzl zzasl;
    
    zzea(final zzdr zzasg, final boolean zzasi, final boolean zzasj, final zzl zzask, final zzh zzaqn, final zzl zzasl) {
        this.zzasg = zzasg;
        this.zzasi = zzasi;
        this.zzasj = zzasj;
        this.zzask = zzask;
        this.zzaqn = zzaqn;
        this.zzasl = zzasl;
    }
    
    @Override
    public final void run() {
        final zzag zzd = this.zzasg.zzasa;
        if (zzd == null) {
            this.zzasg.zzgo().zzjd().zzbx("Discarding data. Failed to send conditional user property to service");
            return;
        }
        if (this.zzasi) {
            final zzdr zzasg = this.zzasg;
            AbstractSafeParcelable zzask;
            if (this.zzasj) {
                zzask = null;
            }
            else {
                zzask = this.zzask;
            }
            zzasg.zza(zzd, zzask, this.zzaqn);
        }
        else {
            try {
                if (TextUtils.isEmpty((CharSequence)this.zzasl.packageName)) {
                    zzd.zza(this.zzask, this.zzaqn);
                }
                else {
                    zzd.zzb(this.zzask);
                }
            }
            catch (RemoteException ex) {
                this.zzasg.zzgo().zzjd().zzg("Failed to send conditional user property to the service", ex);
            }
        }
        this.zzasg.zzcy();
    }
}
