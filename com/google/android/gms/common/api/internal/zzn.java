package com.google.android.gms.common.api.internal;

import android.app.*;

final class zzn extends Callback
{
    private final /* synthetic */ Dialog zzex;
    private final /* synthetic */ zzm zzey;
    
    zzn(final zzm zzey, final Dialog zzex) {
        this.zzey = zzey;
        this.zzex = zzex;
    }
    
    @Override
    public final void zzv() {
        this.zzey.zzew.zzt();
        if (this.zzex.isShowing()) {
            this.zzex.dismiss();
        }
    }
}
