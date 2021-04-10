package com.google.firebase.iid;

final class zzag implements Runnable
{
    private final zzac zzbz;
    private final zzaj zzca;
    
    zzag(final zzac zzbz, final zzaj zzca) {
        this.zzbz = zzbz;
        this.zzca = zzca;
    }
    
    @Override
    public final void run() {
        this.zzbz.zza(this.zzca.zzcc);
    }
}
