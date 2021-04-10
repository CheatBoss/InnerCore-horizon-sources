package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.*;

final class zzbz implements Runnable
{
    private final /* synthetic */ zzby zzlx;
    
    zzbz(final zzby zzlx) {
        this.zzlx = zzlx;
    }
    
    @Override
    public final void run() {
        this.zzlx.zzlw.zzg(new ConnectionResult(4));
    }
}
