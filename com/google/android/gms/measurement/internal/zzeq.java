package com.google.android.gms.measurement.internal;

import android.os.*;
import com.google.android.gms.internal.measurement.*;
import android.content.*;
import com.google.android.gms.common.util.*;

public final class zzeq extends zzf
{
    private Handler handler;
    private long zzasw;
    private final zzv zzasx;
    private final zzv zzasy;
    
    zzeq(final zzbt zzbt) {
        super(zzbt);
        this.zzasx = new zzer(this, this.zzadj);
        this.zzasy = new zzes(this, this.zzadj);
        this.zzasw = this.zzbx().elapsedRealtime();
    }
    
    private final void zzal(final long zzasw) {
        this.zzaf();
        this.zzli();
        this.zzgo().zzjl().zzg("Activity resumed, time", zzasw);
        this.zzasw = zzasw;
        if (this.zzgq().zzbj(this.zzgf().zzal())) {
            this.zzam(this.zzbx().currentTimeMillis());
            return;
        }
        this.zzasx.cancel();
        this.zzasy.cancel();
        if (this.zzbx().currentTimeMillis() - this.zzgp().zzanq.get() > this.zzgp().zzant.get()) {
            this.zzgp().zzanr.set(true);
            this.zzgp().zzanu.set(0L);
        }
        if (this.zzgp().zzanr.get()) {
            this.zzasx.zzh(Math.max(0L, this.zzgp().zzanp.get() - this.zzgp().zzanu.get()));
            return;
        }
        this.zzasy.zzh(Math.max(0L, 3600000L - this.zzgp().zzanu.get()));
    }
    
    private final void zzan(final long n) {
        this.zzaf();
        this.zzli();
        this.zzasx.cancel();
        this.zzasy.cancel();
        this.zzgo().zzjl().zzg("Activity paused, time", n);
        if (this.zzasw != 0L) {
            this.zzgp().zzanu.set(this.zzgp().zzanu.get() + (n - this.zzasw));
        }
    }
    
    private final void zzao(final long n) {
        this.zzaf();
        this.zzgo().zzjl().zzg("Session started, time", this.zzbx().elapsedRealtime());
        zzcs zzcs;
        Long value;
        if (this.zzgq().zzbi(this.zzgf().zzal())) {
            final long n2 = n / 1000L;
            zzcs = this.zzge();
            value = n2;
        }
        else {
            zzcs = this.zzge();
            value = null;
        }
        zzcs.zza("auto", "_sid", value, n);
        this.zzgp().zzanr.set(false);
        final Bundle bundle = new Bundle();
        if (this.zzgq().zzbi(this.zzgf().zzal())) {
            bundle.putLong("_sid", n / 1000L);
        }
        this.zzge().zza("auto", "_s", n, bundle);
        this.zzgp().zzant.set(n);
    }
    
    private final void zzli() {
        synchronized (this) {
            if (this.handler == null) {
                this.handler = new zzdx(Looper.getMainLooper());
            }
        }
    }
    
    private final void zzll() {
        this.zzaf();
        this.zzn(false);
        this.zzgd().zzq(this.zzbx().elapsedRealtime());
    }
    
    final void zzam(final long n) {
        this.zzaf();
        this.zzli();
        this.zzasx.cancel();
        this.zzasy.cancel();
        if (n - this.zzgp().zzanq.get() > this.zzgp().zzant.get()) {
            this.zzgp().zzanr.set(true);
            this.zzgp().zzanu.set(0L);
        }
        if (this.zzgp().zzanr.get()) {
            this.zzao(n);
            return;
        }
        this.zzasy.zzh(Math.max(0L, 3600000L - this.zzgp().zzanu.get()));
    }
    
    @Override
    protected final boolean zzgt() {
        return false;
    }
    
    protected final void zzlk() {
        this.zzaf();
        this.zzao(this.zzbx().currentTimeMillis());
    }
    
    public final boolean zzn(final boolean b) {
        this.zzaf();
        this.zzcl();
        final long elapsedRealtime = this.zzbx().elapsedRealtime();
        this.zzgp().zzant.set(this.zzbx().currentTimeMillis());
        final long n = elapsedRealtime - this.zzasw;
        if (!b && n < 1000L) {
            this.zzgo().zzjl().zzg("Screen exposed for less than 1000 ms. Event not sent. time", n);
            return false;
        }
        this.zzgp().zzanu.set(n);
        this.zzgo().zzjl().zzg("Recording user engagement, ms", n);
        final Bundle bundle = new Bundle();
        bundle.putLong("_et", n);
        zzdo.zza(this.zzgh().zzla(), bundle, true);
        this.zzge().logEvent("auto", "_e", bundle);
        this.zzasw = elapsedRealtime;
        this.zzasy.cancel();
        this.zzasy.zzh(Math.max(0L, 3600000L - this.zzgp().zzanu.get()));
        return true;
    }
}
