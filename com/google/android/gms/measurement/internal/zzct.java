package com.google.android.gms.measurement.internal;

import java.util.concurrent.atomic.*;

final class zzct implements Runnable
{
    private final /* synthetic */ AtomicReference zzarb;
    private final /* synthetic */ zzcs zzarc;
    
    @Override
    public final void run() {
        final AtomicReference zzarb = this.zzarb;
        // monitorenter(zzarb)
        try {
            this.zzarb.set(this.zzarc.zzgq().zzba(this.zzarc.zzgf().zzal()));
            return;
        }
        finally {
            this.zzarb.notify();
        }
        try {}
        finally {
        }
        // monitorexit(zzarb)
    }
}
