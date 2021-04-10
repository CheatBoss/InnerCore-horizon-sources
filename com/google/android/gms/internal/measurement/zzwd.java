package com.google.android.gms.internal.measurement;

import java.util.*;

abstract class zzwd
{
    private static final zzwd zzcaj;
    private static final zzwd zzcak;
    
    static {
        zzcaj = new zzwf(null);
        zzcak = new zzwg(null);
    }
    
    private zzwd() {
    }
    
    static zzwd zzwx() {
        return zzwd.zzcaj;
    }
    
    static zzwd zzwy() {
        return zzwd.zzcak;
    }
    
    abstract <L> List<L> zza(final Object p0, final long p1);
    
    abstract <L> void zza(final Object p0, final Object p1, final long p2);
    
    abstract void zzb(final Object p0, final long p1);
}
