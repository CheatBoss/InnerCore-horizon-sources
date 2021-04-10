package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.*;
import android.text.*;
import android.os.*;
import com.google.android.gms.common.util.*;
import com.google.android.gms.common.*;
import java.util.concurrent.*;
import java.util.*;

public final class zzbv extends zzah
{
    private final zzfa zzamz;
    private Boolean zzaql;
    private String zzaqm;
    
    public zzbv(final zzfa zzfa) {
        this(zzfa, null);
    }
    
    private zzbv(final zzfa zzamz, final String s) {
        Preconditions.checkNotNull(zzamz);
        this.zzamz = zzamz;
        this.zzaqm = null;
    }
    
    private final void zzb(final zzh zzh, final boolean b) {
        Preconditions.checkNotNull(zzh);
        this.zzc(zzh.packageName, false);
        this.zzamz.zzgm().zzt(zzh.zzafx, zzh.zzagk);
    }
    
    private final void zzc(final String zzaqm, final boolean b) {
        while (true) {
            Label_0172: {
                if (TextUtils.isEmpty((CharSequence)zzaqm)) {
                    break Label_0172;
                }
                Label_0089: {
                    if (!b) {
                        break Label_0089;
                    }
                    try {
                        if (this.zzaql == null) {
                            this.zzaql = ("com.google.android.gms".equals(this.zzaqm) || UidVerifier.isGooglePlayServicesUid(this.zzamz.getContext(), Binder.getCallingUid()) || GoogleSignatureVerifier.getInstance(this.zzamz.getContext()).isUidGoogleSigned(Binder.getCallingUid()));
                        }
                        if (this.zzaql) {
                            return;
                        }
                        if (this.zzaqm == null && GooglePlayServicesUtilLight.uidHasPackageName(this.zzamz.getContext(), Binder.getCallingUid(), zzaqm)) {
                            this.zzaqm = zzaqm;
                        }
                        if (zzaqm.equals(this.zzaqm)) {
                            return;
                        }
                        throw new SecurityException(String.format("Unknown calling package name '%s'.", zzaqm));
                        this.zzamz.zzgo().zzjd().zzbx("Measurement Service called without app package");
                        throw new SecurityException("Measurement Service called without app package");
                        this.zzamz.zzgo().zzjd().zzg("Measurement Service called with invalid calling package. appId", zzap.zzbv(zzaqm));
                        throw;
                    }
                    catch (SecurityException ex2) {}
                }
            }
            final SecurityException ex2;
            final SecurityException ex = ex2;
            continue;
        }
    }
    
    private final void zze(final Runnable runnable) {
        Preconditions.checkNotNull(runnable);
        if (zzaf.zzakv.get() && this.zzamz.zzgn().zzkb()) {
            runnable.run();
            return;
        }
        this.zzamz.zzgn().zzc(runnable);
    }
    
    @Override
    public final List<zzfh> zza(final zzh zzh, final boolean b) {
        this.zzb(zzh, false);
        final Future<List<zzfj>> zzb = this.zzamz.zzgn().zzb((Callable<List<zzfj>>)new zzcl(this, zzh));
        try {
            final List<zzfj> list = zzb.get();
            final ArrayList list2 = new ArrayList<zzfh>(list.size());
            for (final zzfj zzfj : list) {
                if (b || !zzfk.zzcv(zzfj.name)) {
                    list2.add(new zzfh(zzfj));
                }
            }
            return (List<zzfh>)list2;
        }
        catch (InterruptedException | ExecutionException ex) {
            final Object o;
            this.zzamz.zzgo().zzjd().zze("Failed to get user attributes. appId", zzap.zzbv(zzh.packageName), o);
            return null;
        }
    }
    
    @Override
    public final List<zzl> zza(final String s, final String s2, final zzh zzh) {
        this.zzb(zzh, false);
        final Future<List<zzl>> zzb = this.zzamz.zzgn().zzb((Callable<List<zzl>>)new zzcd(this, zzh, s, s2));
        try {
            return zzb.get();
        }
        catch (InterruptedException | ExecutionException ex) {
            final Object o;
            this.zzamz.zzgo().zzjd().zzg("Failed to get conditional user properties", o);
            return Collections.emptyList();
        }
    }
    
    @Override
    public final List<zzfh> zza(final String s, final String s2, final String s3, final boolean b) {
        this.zzc(s, true);
        final Future<List<zzfj>> zzb = this.zzamz.zzgn().zzb((Callable<List<zzfj>>)new zzcc(this, s, s2, s3));
        try {
            final List<zzfj> list = zzb.get();
            final ArrayList list2 = new ArrayList<zzfh>(list.size());
            for (final zzfj zzfj : list) {
                if (b || !zzfk.zzcv(zzfj.name)) {
                    list2.add(new zzfh(zzfj));
                }
            }
            return (List<zzfh>)list2;
        }
        catch (InterruptedException | ExecutionException ex) {
            final Object o;
            this.zzamz.zzgo().zzjd().zze("Failed to get user attributes. appId", zzap.zzbv(s), o);
            return Collections.emptyList();
        }
    }
    
    @Override
    public final List<zzfh> zza(final String s, final String s2, final boolean b, final zzh zzh) {
        this.zzb(zzh, false);
        final Future<List<zzfj>> zzb = this.zzamz.zzgn().zzb((Callable<List<zzfj>>)new zzcb(this, zzh, s, s2));
        try {
            final List<zzfj> list = zzb.get();
            final ArrayList list2 = new ArrayList<zzfh>(list.size());
            for (final zzfj zzfj : list) {
                if (b || !zzfk.zzcv(zzfj.name)) {
                    list2.add(new zzfh(zzfj));
                }
            }
            return (List<zzfh>)list2;
        }
        catch (InterruptedException | ExecutionException ex) {
            final Object o;
            this.zzamz.zzgo().zzjd().zze("Failed to get user attributes. appId", zzap.zzbv(zzh.packageName), o);
            return Collections.emptyList();
        }
    }
    
    @Override
    public final void zza(final long n, final String s, final String s2, final String s3) {
        this.zze(new zzcn(this, s2, s3, s, n));
    }
    
    @Override
    public final void zza(final zzad zzad, final zzh zzh) {
        Preconditions.checkNotNull(zzad);
        this.zzb(zzh, false);
        this.zze(new zzcg(this, zzad, zzh));
    }
    
    @Override
    public final void zza(final zzad zzad, final String s, final String s2) {
        Preconditions.checkNotNull(zzad);
        Preconditions.checkNotEmpty(s);
        this.zzc(s, true);
        this.zze(new zzch(this, zzad, s));
    }
    
    @Override
    public final void zza(final zzfh zzfh, final zzh zzh) {
        Preconditions.checkNotNull(zzfh);
        this.zzb(zzh, false);
        Runnable runnable;
        if (zzfh.getValue() == null) {
            runnable = new zzcj(this, zzfh, zzh);
        }
        else {
            runnable = new zzck(this, zzfh, zzh);
        }
        this.zze(runnable);
    }
    
    @Override
    public final void zza(final zzh zzh) {
        this.zzb(zzh, false);
        this.zze(new zzcm(this, zzh));
    }
    
    @Override
    public final void zza(final zzl zzl, final zzh zzh) {
        Preconditions.checkNotNull(zzl);
        Preconditions.checkNotNull(zzl.zzahb);
        this.zzb(zzh, false);
        final zzl zzl2 = new zzl(zzl);
        zzl2.packageName = zzh.packageName;
        Runnable runnable;
        if (zzl.zzahb.getValue() == null) {
            runnable = new zzbx(this, zzl2, zzh);
        }
        else {
            runnable = new zzby(this, zzl2, zzh);
        }
        this.zze(runnable);
    }
    
    @Override
    public final byte[] zza(final zzad zzad, final String s) {
        Preconditions.checkNotEmpty(s);
        Preconditions.checkNotNull(zzad);
        this.zzc(s, true);
        this.zzamz.zzgo().zzjk().zzg("Log and bundle. event", this.zzamz.zzgl().zzbs(zzad.name));
        final long n = this.zzamz.zzbx().nanoTime() / 1000000L;
        final Future<byte[]> zzc = this.zzamz.zzgn().zzc((Callable<byte[]>)new zzci(this, zzad, s));
        try {
            byte[] array;
            if ((array = zzc.get()) == null) {
                this.zzamz.zzgo().zzjd().zzg("Log and bundle returned null. appId", zzap.zzbv(s));
                array = new byte[0];
            }
            this.zzamz.zzgo().zzjk().zzd("Log and bundle processed. event, size, time_ms", this.zzamz.zzgl().zzbs(zzad.name), array.length, this.zzamz.zzbx().nanoTime() / 1000000L - n);
            return array;
        }
        catch (InterruptedException | ExecutionException ex) {
            final Object o;
            this.zzamz.zzgo().zzjd().zzd("Failed to log and bundle. appId, event, error", zzap.zzbv(s), this.zzamz.zzgl().zzbs(zzad.name), o);
            return null;
        }
    }
    
    final zzad zzb(final zzad zzad, final zzh zzh) {
        boolean b = false;
        Label_0099: {
            if ("_cmp".equals(zzad.name) && zzad.zzaid != null) {
                if (zzad.zzaid.size() != 0) {
                    final String string = zzad.zzaid.getString("_cis");
                    if (!TextUtils.isEmpty((CharSequence)string) && ("referrer broadcast".equals(string) || "referrer API".equals(string)) && this.zzamz.zzgq().zzbg(zzh.packageName)) {
                        b = true;
                        break Label_0099;
                    }
                }
            }
            b = false;
        }
        if (b) {
            this.zzamz.zzgo().zzjj().zzg("Event has been filtered ", zzad.toString());
            return new zzad("_cmpx", zzad.zzaid, zzad.origin, zzad.zzaip);
        }
        return zzad;
    }
    
    @Override
    public final void zzb(final zzh zzh) {
        this.zzb(zzh, false);
        this.zze(new zzbw(this, zzh));
    }
    
    @Override
    public final void zzb(final zzl zzl) {
        Preconditions.checkNotNull(zzl);
        Preconditions.checkNotNull(zzl.zzahb);
        this.zzc(zzl.packageName, true);
        final zzl zzl2 = new zzl(zzl);
        Runnable runnable;
        if (zzl.zzahb.getValue() == null) {
            runnable = new zzbz(this, zzl2);
        }
        else {
            runnable = new zzca(this, zzl2);
        }
        this.zze(runnable);
    }
    
    @Override
    public final String zzc(final zzh zzh) {
        this.zzb(zzh, false);
        return this.zzamz.zzh(zzh);
    }
    
    @Override
    public final void zzd(final zzh zzh) {
        this.zzc(zzh.packageName, false);
        this.zze(new zzcf(this, zzh));
    }
    
    @Override
    public final List<zzl> zze(final String s, final String s2, final String s3) {
        this.zzc(s, true);
        final Future<List<zzl>> zzb = this.zzamz.zzgn().zzb((Callable<List<zzl>>)new zzce(this, s, s2, s3));
        try {
            return zzb.get();
        }
        catch (InterruptedException | ExecutionException ex) {
            final Object o;
            this.zzamz.zzgo().zzjd().zzg("Failed to get conditional user properties", o);
            return Collections.emptyList();
        }
    }
}
