package com.google.android.gms.measurement.internal;

import java.util.concurrent.atomic.*;
import java.util.*;

final class zzdc implements Runnable
{
    private final /* synthetic */ String zzaeh;
    private final /* synthetic */ String zzaeo;
    private final /* synthetic */ String zzaqq;
    private final /* synthetic */ AtomicReference zzarb;
    private final /* synthetic */ zzcs zzarc;
    
    zzdc(final zzcs zzarc, final AtomicReference zzarb, final String zzaqq, final String zzaeh, final String zzaeo) {
        this.zzarc = zzarc;
        this.zzarb = zzarb;
        this.zzaqq = zzaqq;
        this.zzaeh = zzaeh;
        this.zzaeo = zzaeo;
    }
    
    @Override
    public final void run() {
        this.zzarc.zzadj.zzgg().zza(this.zzarb, this.zzaqq, this.zzaeh, this.zzaeo);
    }
}
