package com.google.android.gms.measurement.internal;

import java.util.*;
import com.google.android.gms.common.internal.*;

final class zzaw implements Runnable
{
    private final String packageName;
    private final int status;
    private final zzav zzamr;
    private final Throwable zzams;
    private final byte[] zzamt;
    private final Map<String, List<String>> zzamu;
    
    private zzaw(final String packageName, final zzav zzamr, final int status, final Throwable zzams, final byte[] zzamt, final Map<String, List<String>> zzamu) {
        Preconditions.checkNotNull(zzamr);
        this.zzamr = zzamr;
        this.status = status;
        this.zzams = zzams;
        this.zzamt = zzamt;
        this.packageName = packageName;
        this.zzamu = zzamu;
    }
    
    @Override
    public final void run() {
        this.zzamr.zza(this.packageName, this.status, this.zzams, this.zzamt, this.zzamu);
    }
}
