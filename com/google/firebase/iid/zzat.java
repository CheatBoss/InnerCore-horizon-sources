package com.google.firebase.iid;

import android.os.*;

final class zzat extends Handler
{
    private final /* synthetic */ zzas zzct;
    
    zzat(final zzas zzct, final Looper looper) {
        this.zzct = zzct;
        super(looper);
    }
    
    public final void handleMessage(final Message message) {
        this.zzct.zzb(message);
    }
}
