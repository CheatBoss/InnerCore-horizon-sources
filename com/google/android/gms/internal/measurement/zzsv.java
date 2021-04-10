package com.google.android.gms.internal.measurement;

import android.net.*;

public final class zzsv
{
    private final String zzbrm;
    private final Uri zzbrn;
    private final String zzbro;
    private final String zzbrp;
    private final boolean zzbrq;
    private final boolean zzbrr;
    private final boolean zzbrs;
    
    public zzsv(final Uri uri) {
        this(null, uri, "", "", false, false, false);
    }
    
    private zzsv(final String s, final Uri zzbrn, final String zzbro, final String zzbrp, final boolean b, final boolean b2, final boolean b3) {
        this.zzbrm = null;
        this.zzbrn = zzbrn;
        this.zzbro = zzbro;
        this.zzbrp = zzbrp;
        this.zzbrq = false;
        this.zzbrr = false;
        this.zzbrs = false;
    }
    
    public final zzsl<Double> zzb(final String s, final double n) {
        return zza(this, s, n);
    }
    
    public final zzsl<Integer> zzd(final String s, final int n) {
        return zza(this, s, n);
    }
    
    public final zzsl<Long> zze(final String s, final long n) {
        return zza(this, s, n);
    }
    
    public final zzsl<Boolean> zzf(final String s, final boolean b) {
        return zza(this, s, b);
    }
    
    public final zzsl<String> zzx(final String s, final String s2) {
        return zza(this, s, s2);
    }
}
