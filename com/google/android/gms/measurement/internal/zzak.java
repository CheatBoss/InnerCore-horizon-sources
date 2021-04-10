package com.google.android.gms.measurement.internal;

import android.os.*;

public final class zzak
{
    public final String origin;
    public final long zzadt;
    public final long zzadu;
    public final boolean zzadv;
    public final String zzadw;
    public final String zzadx;
    public final Bundle zzady;
    
    zzak(final long zzadt, final long zzadu, final boolean zzadv, final String zzadw, final String origin, final String zzadx, final Bundle zzady) {
        this.zzadt = zzadt;
        this.zzadu = zzadu;
        this.zzadv = zzadv;
        this.zzadw = zzadw;
        this.origin = origin;
        this.zzadx = zzadx;
        this.zzady = zzady;
    }
    
    public static final zzak zzc(final Bundle bundle) {
        return new zzak(0L, 0L, true, null, null, null, bundle);
    }
}
