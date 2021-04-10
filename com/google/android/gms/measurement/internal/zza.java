package com.google.android.gms.measurement.internal;

import android.support.v4.util.*;
import android.os.*;
import com.google.android.gms.common.internal.*;
import java.util.*;
import android.content.*;
import com.google.android.gms.common.util.*;

public final class zza extends zze
{
    private final Map<String, Long> zzafq;
    private final Map<String, Integer> zzafr;
    private long zzafs;
    
    public zza(final zzbt zzbt) {
        super(zzbt);
        this.zzafr = new ArrayMap<String, Integer>();
        this.zzafq = new ArrayMap<String, Long>();
    }
    
    private final void zza(final long n, final zzdn zzdn) {
        if (zzdn == null) {
            this.zzgo().zzjl().zzbx("Not logging ad exposure. No active activity");
            return;
        }
        if (n < 1000L) {
            this.zzgo().zzjl().zzg("Not logging ad exposure. Less than 1000 ms. exposure", n);
            return;
        }
        final Bundle bundle = new Bundle();
        bundle.putLong("_xt", n);
        zzdo.zza(zzdn, bundle, true);
        this.zzge().logEvent("am", "_xa", bundle);
    }
    
    private final void zza(final String s, final long zzafs) {
        this.zzgb();
        this.zzaf();
        Preconditions.checkNotEmpty(s);
        if (this.zzafr.isEmpty()) {
            this.zzafs = zzafs;
        }
        final Integer n = this.zzafr.get(s);
        if (n != null) {
            this.zzafr.put(s, n + 1);
            return;
        }
        if (this.zzafr.size() >= 100) {
            this.zzgo().zzjg().zzbx("Too many ads visible");
            return;
        }
        this.zzafr.put(s, 1);
        this.zzafq.put(s, zzafs);
    }
    
    private final void zza(final String s, final long n, final zzdn zzdn) {
        if (zzdn == null) {
            this.zzgo().zzjl().zzbx("Not logging ad unit exposure. No active activity");
            return;
        }
        if (n < 1000L) {
            this.zzgo().zzjl().zzg("Not logging ad unit exposure. Less than 1000 ms. exposure", n);
            return;
        }
        final Bundle bundle = new Bundle();
        bundle.putString("_ai", s);
        bundle.putLong("_xt", n);
        zzdo.zza(zzdn, bundle, true);
        this.zzge().logEvent("am", "_xu", bundle);
    }
    
    private final void zzb(final String s, final long n) {
        this.zzgb();
        this.zzaf();
        Preconditions.checkNotEmpty(s);
        final Integer n2 = this.zzafr.get(s);
        if (n2 == null) {
            this.zzgo().zzjd().zzg("Call to endAdUnitExposure for unknown ad unit id", s);
            return;
        }
        final zzdn zzla = this.zzgh().zzla();
        final int n3 = n2 - 1;
        if (n3 == 0) {
            this.zzafr.remove(s);
            final Long n4 = this.zzafq.get(s);
            if (n4 == null) {
                this.zzgo().zzjd().zzbx("First ad unit exposure time was never set");
            }
            else {
                final long longValue = n4;
                this.zzafq.remove(s);
                this.zza(s, n - longValue, zzla);
            }
            if (this.zzafr.isEmpty()) {
                final long zzafs = this.zzafs;
                if (zzafs == 0L) {
                    this.zzgo().zzjd().zzbx("First ad exposure time was never set");
                    return;
                }
                this.zza(n - zzafs, zzla);
                this.zzafs = 0L;
            }
            return;
        }
        this.zzafr.put(s, n3);
    }
    
    private final void zzr(final long zzafs) {
        final Iterator<String> iterator = this.zzafq.keySet().iterator();
        while (iterator.hasNext()) {
            this.zzafq.put(iterator.next(), zzafs);
        }
        if (!this.zzafq.isEmpty()) {
            this.zzafs = zzafs;
        }
    }
    
    public final void beginAdUnitExposure(final String s, final long n) {
        if (s != null && s.length() != 0) {
            this.zzgn().zzc(new zzb(this, s, n));
            return;
        }
        this.zzgo().zzjd().zzbx("Ad unit id must be a non-empty string");
    }
    
    public final void endAdUnitExposure(final String s, final long n) {
        if (s != null && s.length() != 0) {
            this.zzgn().zzc(new zzc(this, s, n));
            return;
        }
        this.zzgo().zzjd().zzbx("Ad unit id must be a non-empty string");
    }
    
    public final void zzq(final long n) {
        final zzdn zzla = this.zzgh().zzla();
        for (final String s : this.zzafq.keySet()) {
            this.zza(s, n - this.zzafq.get(s), zzla);
        }
        if (!this.zzafq.isEmpty()) {
            this.zza(n - this.zzafs, zzla);
        }
        this.zzr(n);
    }
}
