package com.google.android.gms.measurement.internal;

final class zzbk implements Runnable
{
    private final /* synthetic */ zzbt zzaoj;
    private final /* synthetic */ zzap zzaok;
    
    zzbk(final zzbj zzbj, final zzbt zzaoj, final zzap zzaok) {
        this.zzaoj = zzaoj;
        this.zzaok = zzaok;
    }
    
    @Override
    public final void run() {
        if (this.zzaoj.zzkg() == null) {
            this.zzaok.zzjd().zzbx("Install Referrer Reporter is null");
            return;
        }
        final zzbg zzkg = this.zzaoj.zzkg();
        zzkg.zzadj.zzgb();
        zzkg.zzcd(zzkg.zzadj.getContext().getPackageName());
    }
}
