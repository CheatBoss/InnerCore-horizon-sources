package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.*;
import java.util.*;
import com.google.android.gms.common.internal.*;

final class zzbn implements Runnable
{
    private final /* synthetic */ ConnectionResult zzkl;
    private final /* synthetic */ GoogleApiManager.zzc zzkr;
    
    zzbn(final GoogleApiManager.zzc zzkr, final ConnectionResult zzkl) {
        this.zzkr = zzkr;
        this.zzkl = zzkl;
    }
    
    @Override
    public final void run() {
        if (!this.zzkl.isSuccess()) {
            this.zzkr.zzjy.zzju.get(this.zzkr.zzhc).onConnectionFailed(this.zzkl);
            return;
        }
        GoogleApiManager.zzc.zza(this.zzkr, true);
        if (this.zzkr.zzka.requiresSignIn()) {
            this.zzkr.zzbu();
            return;
        }
        this.zzkr.zzka.getRemoteService(null, Collections.emptySet());
    }
}
