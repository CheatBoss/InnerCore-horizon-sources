package com.google.android.gms.measurement.internal;

import android.os.*;

final class zzcu implements Runnable
{
    private final /* synthetic */ String val$name;
    private final /* synthetic */ String zzaeh;
    private final /* synthetic */ boolean zzafg;
    private final /* synthetic */ String zzaqq;
    private final /* synthetic */ zzcs zzarc;
    private final /* synthetic */ long zzard;
    private final /* synthetic */ Bundle zzare;
    private final /* synthetic */ boolean zzarf;
    private final /* synthetic */ boolean zzarg;
    
    zzcu(final zzcs zzarc, final String zzaeh, final String val$name, final long zzard, final Bundle zzare, final boolean zzafg, final boolean zzarf, final boolean zzarg, final String zzaqq) {
        this.zzarc = zzarc;
        this.zzaeh = zzaeh;
        this.val$name = val$name;
        this.zzard = zzard;
        this.zzare = zzare;
        this.zzafg = zzafg;
        this.zzarf = zzarf;
        this.zzarg = zzarg;
        this.zzaqq = zzaqq;
    }
    
    @Override
    public final void run() {
        this.zzarc.zza(this.zzaeh, this.val$name, this.zzard, this.zzare, this.zzafg, this.zzarf, this.zzarg, this.zzaqq);
    }
}
