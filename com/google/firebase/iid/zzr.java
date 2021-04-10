package com.google.firebase.iid;

import android.os.*;
import com.google.android.gms.tasks.*;

final class zzr implements Runnable
{
    private final zzq zzbh;
    private final Bundle zzbi;
    private final TaskCompletionSource zzbj;
    
    zzr(final zzq zzbh, final Bundle zzbi, final TaskCompletionSource zzbj) {
        this.zzbh = zzbh;
        this.zzbi = zzbi;
        this.zzbj = zzbj;
    }
    
    @Override
    public final void run() {
        this.zzbh.zza(this.zzbi, this.zzbj);
    }
}
