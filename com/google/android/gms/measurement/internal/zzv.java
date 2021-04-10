package com.google.android.gms.measurement.internal;

import android.os.*;
import com.google.android.gms.common.internal.*;
import com.google.android.gms.internal.measurement.*;

abstract class zzv
{
    private static volatile Handler handler;
    private final zzcq zzahw;
    private final Runnable zzyo;
    private volatile long zzyp;
    
    zzv(final zzcq zzahw) {
        Preconditions.checkNotNull(zzahw);
        this.zzahw = zzahw;
        this.zzyo = new zzw(this, zzahw);
    }
    
    private final Handler getHandler() {
        if (zzv.handler != null) {
            return zzv.handler;
        }
        synchronized (zzv.class) {
            if (zzv.handler == null) {
                zzv.handler = new zzdx(this.zzahw.getContext().getMainLooper());
            }
            return zzv.handler;
        }
    }
    
    final void cancel() {
        this.zzyp = 0L;
        this.getHandler().removeCallbacks(this.zzyo);
    }
    
    public abstract void run();
    
    public final boolean zzej() {
        return this.zzyp != 0L;
    }
    
    public final void zzh(final long n) {
        this.cancel();
        if (n >= 0L) {
            this.zzyp = this.zzahw.zzbx().currentTimeMillis();
            if (!this.getHandler().postDelayed(this.zzyo, n)) {
                this.zzahw.zzgo().zzjd().zzg("Failed to schedule delayed post. time", n);
            }
        }
    }
}
