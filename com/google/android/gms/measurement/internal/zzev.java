package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.util.*;
import com.google.android.gms.common.internal.*;

final class zzev
{
    private long startTime;
    private final Clock zzrz;
    
    public zzev(final Clock zzrz) {
        Preconditions.checkNotNull(zzrz);
        this.zzrz = zzrz;
    }
    
    public final void clear() {
        this.startTime = 0L;
    }
    
    public final void start() {
        this.startTime = this.zzrz.elapsedRealtime();
    }
    
    public final boolean zzj(final long n) {
        return this.startTime == 0L || this.zzrz.elapsedRealtime() - this.startTime >= 3600000L;
    }
}
