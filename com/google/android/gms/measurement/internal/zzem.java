package com.google.android.gms.measurement.internal;

import android.content.*;

final class zzem implements Runnable
{
    private final int zzacb;
    private final zzel zzasr;
    private final zzap zzass;
    private final Intent zzast;
    
    zzem(final zzel zzasr, final int zzacb, final zzap zzass, final Intent zzast) {
        this.zzasr = zzasr;
        this.zzacb = zzacb;
        this.zzass = zzass;
        this.zzast = zzast;
    }
    
    @Override
    public final void run() {
        this.zzasr.zza(this.zzacb, this.zzass, this.zzast);
    }
}
