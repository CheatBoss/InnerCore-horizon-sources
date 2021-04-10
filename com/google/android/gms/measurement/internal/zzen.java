package com.google.android.gms.measurement.internal;

import android.app.job.*;

final class zzen implements Runnable
{
    private final JobParameters zzace;
    private final zzel zzasr;
    private final zzap zzasu;
    
    zzen(final zzel zzasr, final zzap zzasu, final JobParameters zzace) {
        this.zzasr = zzasr;
        this.zzasu = zzasu;
        this.zzace = zzace;
    }
    
    @Override
    public final void run() {
        this.zzasr.zza(this.zzasu, this.zzace);
    }
}
