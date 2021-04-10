package com.google.firebase.iid;

import android.os.*;
import android.util.*;

public final class zzf extends Binder
{
    private final zzb zzs;
    
    zzf(final zzb zzs) {
        this.zzs = zzs;
    }
    
    public final void zza(final zzd zzd) {
        if (Binder.getCallingUid() != Process.myUid()) {
            throw new SecurityException("Binding only allowed within app");
        }
        if (Log.isLoggable("EnhancedIntentService", 3)) {
            Log.d("EnhancedIntentService", "service received new intent via bind strategy");
        }
        if (this.zzs.zzc(zzd.intent)) {
            zzd.finish();
            return;
        }
        if (Log.isLoggable("EnhancedIntentService", 3)) {
            Log.d("EnhancedIntentService", "intent being queued for bg execution");
        }
        this.zzs.zzh.execute(new zzg(this, zzd));
    }
}
