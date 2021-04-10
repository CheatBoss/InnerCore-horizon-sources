package com.google.android.gms.measurement.internal;

import com.google.android.gms.measurement.*;

final class zzdb implements Runnable
{
    private final /* synthetic */ zzcs zzarc;
    private final /* synthetic */ AppMeasurement.ConditionalUserProperty zzarj;
    
    zzdb(final zzcs zzarc, final AppMeasurement.ConditionalUserProperty zzarj) {
        this.zzarc = zzarc;
        this.zzarj = zzarj;
    }
    
    @Override
    public final void run() {
        this.zzarc.zzc(this.zzarj);
    }
}
