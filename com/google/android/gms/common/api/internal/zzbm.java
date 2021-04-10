package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.*;

final class zzbm implements Runnable
{
    private final /* synthetic */ zzbl zzkm;
    
    zzbm(final zzbl zzkm) {
        this.zzkm = zzkm;
    }
    
    @Override
    public final void run() {
        this.zzkm.zzkk.zzka.disconnect();
    }
}
