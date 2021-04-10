package com.google.firebase.iid;

import android.os.*;

final class zzad implements Handler$Callback
{
    private final zzac zzbz;
    
    zzad(final zzac zzbz) {
        this.zzbz = zzbz;
    }
    
    public final boolean handleMessage(final Message message) {
        return this.zzbz.zza(message);
    }
}
