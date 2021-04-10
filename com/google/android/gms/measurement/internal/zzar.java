package com.google.android.gms.measurement.internal;

public final class zzar
{
    private final int priority;
    private final /* synthetic */ zzap zzamm;
    private final boolean zzamn;
    private final boolean zzamo;
    
    zzar(final zzap zzamm, final int priority, final boolean zzamn, final boolean zzamo) {
        this.zzamm = zzamm;
        this.priority = priority;
        this.zzamn = zzamn;
        this.zzamo = zzamo;
    }
    
    public final void zzbx(final String s) {
        this.zzamm.zza(this.priority, this.zzamn, this.zzamo, s, null, null, null);
    }
    
    public final void zzd(final String s, final Object o, final Object o2, final Object o3) {
        this.zzamm.zza(this.priority, this.zzamn, this.zzamo, s, o, o2, o3);
    }
    
    public final void zze(final String s, final Object o, final Object o2) {
        this.zzamm.zza(this.priority, this.zzamn, this.zzamo, s, o, o2, null);
    }
    
    public final void zzg(final String s, final Object o) {
        this.zzamm.zza(this.priority, this.zzamn, this.zzamo, s, o, null, null);
    }
}
