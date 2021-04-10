package com.google.android.gms.measurement.internal;

import android.os.*;
import android.content.*;
import com.google.android.gms.measurement.*;

final class zzbl implements Runnable
{
    private final /* synthetic */ Context val$context;
    private final /* synthetic */ zzbt zzaoj;
    private final /* synthetic */ zzap zzaok;
    private final /* synthetic */ long zzaol;
    private final /* synthetic */ Bundle zzaom;
    private final /* synthetic */ BroadcastReceiver$PendingResult zzrf;
    
    zzbl(final zzbj zzbj, final zzbt zzaoj, final long zzaol, final Bundle zzaom, final Context val$context, final zzap zzaok, final BroadcastReceiver$PendingResult zzrf) {
        this.zzaoj = zzaoj;
        this.zzaol = zzaol;
        this.zzaom = zzaom;
        this.val$context = val$context;
        this.zzaok = zzaok;
        this.zzrf = zzrf;
    }
    
    @Override
    public final void run() {
        final long value = this.zzaoj.zzgp().zzanj.get();
        long zzaol;
        final long n = zzaol = this.zzaol;
        Label_0049: {
            if (value > 0L) {
                if (n < value) {
                    zzaol = n;
                    if (n > 0L) {
                        break Label_0049;
                    }
                }
                zzaol = value - 1L;
            }
        }
        if (zzaol > 0L) {
            this.zzaom.putLong("click_timestamp", zzaol);
        }
        this.zzaom.putString("_cis", "referrer broadcast");
        AppMeasurement.getInstance(this.val$context).logEventInternal("auto", "_cmp", this.zzaom);
        this.zzaok.zzjl().zzbx("Install campaign recorded");
        final BroadcastReceiver$PendingResult zzrf = this.zzrf;
        if (zzrf != null) {
            zzrf.finish();
        }
    }
}
