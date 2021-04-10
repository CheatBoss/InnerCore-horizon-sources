package com.google.android.gms.measurement.internal;

final class zzcn implements Runnable
{
    private final /* synthetic */ String zzaeq;
    private final /* synthetic */ zzbv zzaqo;
    private final /* synthetic */ String zzaqq;
    private final /* synthetic */ String zzaqt;
    private final /* synthetic */ long zzaqu;
    
    zzcn(final zzbv zzaqo, final String zzaqt, final String zzaqq, final String zzaeq, final long zzaqu) {
        this.zzaqo = zzaqo;
        this.zzaqt = zzaqt;
        this.zzaqq = zzaqq;
        this.zzaeq = zzaeq;
        this.zzaqu = zzaqu;
    }
    
    @Override
    public final void run() {
        if (this.zzaqt == null) {
            this.zzaqo.zzamz.zzmb().zzgh().zza(this.zzaqq, null);
            return;
        }
        this.zzaqo.zzamz.zzmb().zzgh().zza(this.zzaqq, new zzdn(this.zzaeq, this.zzaqt, this.zzaqu));
    }
}
