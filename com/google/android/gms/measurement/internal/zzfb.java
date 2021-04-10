package com.google.android.gms.measurement.internal;

final class zzfb implements Runnable
{
    private final /* synthetic */ zzff zzatx;
    private final /* synthetic */ zzfa zzaty;
    
    zzfb(final zzfa zzaty, final zzff zzatx) {
        this.zzaty = zzaty;
        this.zzatx = zzatx;
    }
    
    @Override
    public final void run() {
        this.zzaty.zza(this.zzatx);
        this.zzaty.start();
    }
}
