package com.google.android.gms.common.api.internal;

import com.google.android.gms.tasks.*;

final class zzac implements OnCompleteListener<Object>
{
    private final /* synthetic */ zzaa zzgz;
    private final /* synthetic */ TaskCompletionSource zzha;
    
    zzac(final zzaa zzgz, final TaskCompletionSource zzha) {
        this.zzgz = zzgz;
        this.zzha = zzha;
    }
    
    @Override
    public final void onComplete(final Task<Object> task) {
        this.zzgz.zzgx.remove(this.zzha);
    }
}
