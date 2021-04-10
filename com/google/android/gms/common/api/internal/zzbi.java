package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.*;

final class zzbi implements Runnable
{
    private final /* synthetic */ GoogleApiManager.zza zzkk;
    
    zzbi(final GoogleApiManager.zza zzkk) {
        this.zzkk = zzkk;
    }
    
    @Override
    public final void run() {
        this.zzkk.zzbj();
    }
}
