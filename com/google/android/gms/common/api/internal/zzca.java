package com.google.android.gms.common.api.internal;

import com.google.android.gms.signin.internal.*;

final class zzca implements Runnable
{
    private final /* synthetic */ SignInResponse zzid;
    private final /* synthetic */ zzby zzlx;
    
    zzca(final zzby zzlx, final SignInResponse zzid) {
        this.zzlx = zzlx;
        this.zzid = zzid;
    }
    
    @Override
    public final void run() {
        this.zzlx.zzb(this.zzid);
    }
}
