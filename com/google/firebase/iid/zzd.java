package com.google.firebase.iid;

import android.content.*;
import java.util.concurrent.*;

final class zzd
{
    final Intent intent;
    private final BroadcastReceiver$PendingResult zzo;
    private boolean zzp;
    private final ScheduledFuture<?> zzq;
    
    zzd(final Intent intent, final BroadcastReceiver$PendingResult zzo, final ScheduledExecutorService scheduledExecutorService) {
        this.zzp = false;
        this.intent = intent;
        this.zzo = zzo;
        this.zzq = scheduledExecutorService.schedule(new zze(this, intent), 9500L, TimeUnit.MILLISECONDS);
    }
    
    final void finish() {
        synchronized (this) {
            if (!this.zzp) {
                this.zzo.finish();
                this.zzq.cancel(false);
                this.zzp = true;
            }
        }
    }
}
