package com.google.firebase.iid;

import android.util.*;

final class zzg implements Runnable
{
    private final /* synthetic */ zzd zzt;
    private final /* synthetic */ zzf zzu;
    
    zzg(final zzf zzu, final zzd zzt) {
        this.zzu = zzu;
        this.zzt = zzt;
    }
    
    @Override
    public final void run() {
        if (Log.isLoggable("EnhancedIntentService", 3)) {
            Log.d("EnhancedIntentService", "bg processing of the intent starting now");
        }
        this.zzu.zzs.zzd(this.zzt.intent);
        this.zzt.finish();
    }
}
