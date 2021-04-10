package com.google.android.gms.internal.measurement;

import android.database.*;
import android.os.*;

final class zzsj extends ContentObserver
{
    private final /* synthetic */ zzsi zzbqx;
    
    zzsj(final zzsi zzbqx, final Handler handler) {
        this.zzbqx = zzbqx;
        super((Handler)null);
    }
    
    public final void onChange(final boolean b) {
        this.zzbqx.zzta();
        this.zzbqx.zztc();
    }
}
