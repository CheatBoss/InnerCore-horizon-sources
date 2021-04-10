package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.internal.*;

final class zzbl implements SignOutCallbacks
{
    final /* synthetic */ GoogleApiManager.zza zzkk;
    
    zzbl(final GoogleApiManager.zza zzkk) {
        this.zzkk = zzkk;
    }
    
    @Override
    public final void onSignOutComplete() {
        this.zzkk.zzjy.handler.post((Runnable)new zzbm(this));
    }
}
