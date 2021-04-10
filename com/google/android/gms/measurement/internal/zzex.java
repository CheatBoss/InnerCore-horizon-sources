package com.google.android.gms.measurement.internal;

final class zzex extends zzv
{
    private final /* synthetic */ zzfa zzasv;
    private final /* synthetic */ zzew zzatb;
    
    zzex(final zzew zzatb, final zzcq zzcq, final zzfa zzasv) {
        this.zzatb = zzatb;
        this.zzasv = zzasv;
        super(zzcq);
    }
    
    @Override
    public final void run() {
        this.zzatb.cancel();
        this.zzatb.zzgo().zzjl().zzbx("Starting upload from DelayedRunnable");
        this.zzasv.zzlt();
    }
}
