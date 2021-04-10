package com.google.android.gms.common.util.concurrent;

import android.os.*;

final class zza implements Runnable
{
    private final int priority;
    private final Runnable zzaax;
    
    public zza(final Runnable zzaax, final int priority) {
        this.zzaax = zzaax;
        this.priority = priority;
    }
    
    @Override
    public final void run() {
        Process.setThreadPriority(this.priority);
        this.zzaax.run();
    }
}
