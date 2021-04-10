package com.google.android.gms.measurement;

import android.support.v4.content.*;
import com.google.android.gms.measurement.internal.*;
import android.content.*;

public final class AppMeasurementReceiver extends WakefulBroadcastReceiver implements zzbm
{
    private zzbj zzadq;
    
    @Override
    public final BroadcastReceiver$PendingResult doGoAsync() {
        return this.goAsync();
    }
    
    @Override
    public final void doStartService(final Context context, final Intent intent) {
        WakefulBroadcastReceiver.startWakefulService(context, intent);
    }
    
    public final void onReceive(final Context context, final Intent intent) {
        if (this.zzadq == null) {
            this.zzadq = new zzbj(this);
        }
        this.zzadq.onReceive(context, intent);
    }
}
