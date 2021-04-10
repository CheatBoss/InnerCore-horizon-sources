package com.google.android.gms.measurement.internal;

import java.util.concurrent.atomic.*;
import java.util.*;

final class zzde implements Runnable
{
    private final /* synthetic */ String zzaeh;
    private final /* synthetic */ String zzaeo;
    private final /* synthetic */ boolean zzaev;
    private final /* synthetic */ String zzaqq;
    private final /* synthetic */ AtomicReference zzarb;
    private final /* synthetic */ zzcs zzarc;
    
    zzde(final zzcs zzarc, final AtomicReference zzarb, final String zzaqq, final String zzaeh, final String zzaeo, final boolean zzaev) {
        this.zzarc = zzarc;
        this.zzarb = zzarb;
        this.zzaqq = zzaqq;
        this.zzaeh = zzaeh;
        this.zzaeo = zzaeo;
        this.zzaev = zzaev;
    }
    
    @Override
    public final void run() {
        this.zzarc.zzadj.zzgg().zza(this.zzarb, this.zzaqq, this.zzaeh, this.zzaeo, this.zzaev);
    }
}
