package com.google.android.gms.common.api.internal;

import java.util.concurrent.atomic.*;
import android.os.*;
import android.content.*;
import com.google.android.gms.common.*;
import android.app.*;

public abstract class zzk extends LifecycleCallback implements DialogInterface$OnCancelListener
{
    protected volatile boolean mStarted;
    protected final GoogleApiAvailability zzdg;
    protected final AtomicReference<zzl> zzer;
    private final Handler zzes;
    
    private static int zza(final zzl zzl) {
        if (zzl == null) {
            return -1;
        }
        return zzl.zzu();
    }
    
    public void onCancel(final DialogInterface dialogInterface) {
        this.zza(new ConnectionResult(13, null), zza(this.zzer.get()));
        this.zzt();
    }
    
    protected abstract void zza(final ConnectionResult p0, final int p1);
    
    public final void zzb(final ConnectionResult connectionResult, final int n) {
        final zzl zzl = new zzl(connectionResult, n);
        if (this.zzer.compareAndSet(null, zzl)) {
            this.zzes.post((Runnable)new zzm(this, zzl));
        }
    }
    
    protected abstract void zzr();
    
    protected final void zzt() {
        this.zzer.set(null);
        this.zzr();
    }
}
