package com.google.android.gms.measurement.internal;

import android.content.*;

final class zzeh implements Runnable
{
    private final /* synthetic */ ComponentName val$name;
    private final /* synthetic */ zzef zzasp;
    
    zzeh(final zzef zzasp, final ComponentName val$name) {
        this.zzasp = zzasp;
        this.val$name = val$name;
    }
    
    @Override
    public final void run() {
        this.zzasp.zzasg.onServiceDisconnected(this.val$name);
    }
}
