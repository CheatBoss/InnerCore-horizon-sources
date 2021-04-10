package com.google.android.gms.measurement;

import com.google.android.gms.measurement.internal.*;
import android.content.*;

public final class AppMeasurementInstallReferrerReceiver extends BroadcastReceiver implements zzbm
{
    private zzbj zzadq;
    
    public final BroadcastReceiver$PendingResult doGoAsync() {
        return this.goAsync();
    }
    
    public final void doStartService(final Context context, final Intent intent) {
    }
    
    public final void onReceive(final Context context, final Intent intent) {
        if (this.zzadq == null) {
            this.zzadq = new zzbj(this);
        }
        this.zzadq.onReceive(context, intent);
    }
}
