package com.google.android.gms.measurement.internal;

import android.os.*;

final class zzdp implements Runnable
{
    private final /* synthetic */ boolean zzaru;
    private final /* synthetic */ zzdn zzarv;
    private final /* synthetic */ zzdn zzarw;
    private final /* synthetic */ zzdo zzarx;
    
    zzdp(final zzdo zzarx, final boolean zzaru, final zzdn zzarv, final zzdn zzarw) {
        this.zzarx = zzarx;
        this.zzaru = zzaru;
        this.zzarv = zzarv;
        this.zzarw = zzarw;
    }
    
    @Override
    public final void run() {
        if (this.zzaru && this.zzarx.zzaro != null) {
            final zzdo zzarx = this.zzarx;
            zzarx.zza(zzarx.zzaro);
        }
        final zzdn zzarv = this.zzarv;
        if (zzarv == null || zzarv.zzarm != this.zzarw.zzarm || !zzfk.zzu(this.zzarv.zzarl, this.zzarw.zzarl) || !zzfk.zzu(this.zzarv.zzuw, this.zzarw.zzuw)) {
            final Bundle bundle = new Bundle();
            zzdo.zza(this.zzarw, bundle, true);
            final zzdn zzarv2 = this.zzarv;
            if (zzarv2 != null) {
                if (zzarv2.zzuw != null) {
                    bundle.putString("_pn", this.zzarv.zzuw);
                }
                bundle.putString("_pc", this.zzarv.zzarl);
                bundle.putLong("_pi", this.zzarv.zzarm);
            }
            this.zzarx.zzge().zza("auto", "_vs", bundle);
        }
        this.zzarx.zzaro = this.zzarw;
        this.zzarx.zzgg().zzb(this.zzarw);
    }
}
