package com.google.android.gms.measurement.internal;

final class zzdj implements Runnable
{
    private final /* synthetic */ boolean zzaes;
    private final /* synthetic */ zzcs zzarc;
    
    zzdj(final zzcs zzarc, final boolean zzaes) {
        this.zzarc = zzarc;
        this.zzaes = zzaes;
    }
    
    @Override
    public final void run() {
        final boolean enabled = this.zzarc.zzadj.isEnabled();
        final boolean zzko = this.zzarc.zzadj.zzko();
        this.zzarc.zzadj.zzd(this.zzaes);
        if (zzko == this.zzaes) {
            this.zzarc.zzadj.zzgo().zzjl().zzg("Default data collection state already set to", this.zzaes);
        }
        if (this.zzarc.zzadj.isEnabled() == enabled || this.zzarc.zzadj.isEnabled() != this.zzarc.zzadj.zzko()) {
            this.zzarc.zzadj.zzgo().zzji().zze("Default data collection is different than actual status", this.zzaes, enabled);
        }
        this.zzarc.zzky();
    }
}
