package com.google.android.gms.measurement.internal;

import android.content.*;

final class zzej implements Runnable
{
    private final /* synthetic */ zzef zzasp;
    
    zzej(final zzef zzasp) {
        this.zzasp = zzasp;
    }
    
    @Override
    public final void run() {
        final zzdr zzasg = this.zzasp.zzasg;
        final Context context = this.zzasp.zzasg.getContext();
        this.zzasp.zzasg.zzgr();
        zzasg.onServiceDisconnected(new ComponentName(context, "com.google.android.gms.measurement.AppMeasurementService"));
    }
}
