package com.google.firebase.iid;

import javax.annotation.*;
import android.util.*;
import android.content.*;

final class zzay extends BroadcastReceiver
{
    @Nullable
    private zzax zzdh;
    
    public zzay(final zzax zzdh) {
        this.zzdh = zzdh;
    }
    
    public final void onReceive(final Context context, final Intent intent) {
        final zzax zzdh = this.zzdh;
        if (zzdh == null) {
            return;
        }
        if (!zzdh.zzan()) {
            return;
        }
        if (FirebaseInstanceId.zzk()) {
            Log.d("FirebaseInstanceId", "Connectivity changed. Starting background sync.");
        }
        FirebaseInstanceId.zza(this.zzdh, 0L);
        this.zzdh.getContext().unregisterReceiver((BroadcastReceiver)this);
        this.zzdh = null;
    }
    
    public final void zzao() {
        if (FirebaseInstanceId.zzk()) {
            Log.d("FirebaseInstanceId", "Connectivity change received registered");
        }
        this.zzdh.getContext().registerReceiver((BroadcastReceiver)this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }
}
