package com.google.firebase.iid;

import android.content.*;

final class zzc implements Runnable
{
    private final /* synthetic */ Intent zzl;
    private final /* synthetic */ Intent zzm;
    private final /* synthetic */ zzb zzn;
    
    zzc(final zzb zzn, final Intent zzl, final Intent zzm) {
        this.zzn = zzn;
        this.zzl = zzl;
        this.zzm = zzm;
    }
    
    @Override
    public final void run() {
        this.zzn.zzd(this.zzl);
        this.zzn.zza(this.zzm);
    }
}
