package com.google.android.gms.internal.measurement;

import java.io.*;
import java.util.*;

final class zzuv implements zzyw
{
    private final zzut zzbuf;
    
    private zzuv(zzut zzbuf) {
        zzbuf = zzvo.zza(zzbuf, "output");
        this.zzbuf = zzbuf;
        zzbuf.zzbuw = this;
    }
    
    public static zzuv zza(final zzut zzut) {
        if (zzut.zzbuw != null) {
            return zzut.zzbuw;
        }
        return new zzuv(zzut);
    }
    
    @Override
    public final void zza(final int n, final double n2) throws IOException {
        this.zzbuf.zza(n, n2);
    }
    
    @Override
    public final void zza(final int n, final float n2) throws IOException {
        this.zzbuf.zza(n, n2);
    }
    
    @Override
    public final void zza(final int n, final long n2) throws IOException {
        this.zzbuf.zza(n, n2);
    }
    
    @Override
    public final void zza(final int n, final zzud zzud) throws IOException {
        this.zzbuf.zza(n, zzud);
    }
    
    @Override
    public final <K, V> void zza(final int n, final zzwm<K, V> zzwm, final Map<K, V> map) throws IOException {
        for (final Map.Entry<K, V> entry : map.entrySet()) {
            this.zzbuf.zzc(n, 2);
            this.zzbuf.zzay(zzwl.zza(zzwm, entry.getKey(), entry.getValue()));
            zzwl.zza(this.zzbuf, (zzwm<K, V>)zzwm, (K)entry.getKey(), (V)entry.getValue());
        }
    }
    
    @Override
    public final void zza(final int n, final Object o) throws IOException {
        if (o instanceof zzud) {
            this.zzbuf.zzb(n, (zzud)o);
            return;
        }
        this.zzbuf.zzb(n, (zzwt)o);
    }
    
    @Override
    public final void zza(final int n, final Object o, final zzxj zzxj) throws IOException {
        this.zzbuf.zza(n, (zzwt)o, zzxj);
    }
    
    @Override
    public final void zza(final int n, final List<String> list) throws IOException {
        final boolean b = list instanceof zzwc;
        int i = 0;
        final int n2 = 0;
        if (b) {
            final zzwc zzwc = (zzwc)list;
            for (int j = n2; j < list.size(); ++j) {
                final Object raw = zzwc.getRaw(j);
                if (raw instanceof String) {
                    this.zzbuf.zzb(n, (String)raw);
                }
                else {
                    this.zzbuf.zza(n, (zzud)raw);
                }
            }
            return;
        }
        while (i < list.size()) {
            this.zzbuf.zzb(n, list.get(i));
            ++i;
        }
    }
    
    @Override
    public final void zza(final int n, final List<?> list, final zzxj zzxj) throws IOException {
        for (int i = 0; i < list.size(); ++i) {
            this.zza(n, list.get(i), zzxj);
        }
    }
    
    @Override
    public final void zza(int i, final List<Integer> list, final boolean b) throws IOException {
        int j = 0;
        final int n = 0;
        if (b) {
            this.zzbuf.zzc(i, 2);
            i = 0;
            int n2 = 0;
            while (i < list.size()) {
                n2 += zzut.zzbc(list.get(i));
                ++i;
            }
            this.zzbuf.zzay(n2);
            for (i = n; i < list.size(); ++i) {
                this.zzbuf.zzax(list.get(i));
            }
            return;
        }
        while (j < list.size()) {
            this.zzbuf.zzd(i, list.get(j));
            ++j;
        }
    }
    
    @Override
    public final void zzb(final int n, final long n2) throws IOException {
        this.zzbuf.zzb(n, n2);
    }
    
    @Override
    public final void zzb(final int n, final Object o, final zzxj zzxj) throws IOException {
        final zzut zzbuf = this.zzbuf;
        final zzwt zzwt = (zzwt)o;
        zzbuf.zzc(n, 3);
        zzxj.zza(zzwt, zzbuf.zzbuw);
        zzbuf.zzc(n, 4);
    }
    
    @Override
    public final void zzb(final int n, final String s) throws IOException {
        this.zzbuf.zzb(n, s);
    }
    
    @Override
    public final void zzb(final int n, final List<zzud> list) throws IOException {
        for (int i = 0; i < list.size(); ++i) {
            this.zzbuf.zza(n, list.get(i));
        }
    }
    
    @Override
    public final void zzb(final int n, final List<?> list, final zzxj zzxj) throws IOException {
        for (int i = 0; i < list.size(); ++i) {
            this.zzb(n, list.get(i), zzxj);
        }
    }
    
    @Override
    public final void zzb(int i, final List<Integer> list, final boolean b) throws IOException {
        int j = 0;
        final int n = 0;
        if (b) {
            this.zzbuf.zzc(i, 2);
            i = 0;
            int n2 = 0;
            while (i < list.size()) {
                n2 += zzut.zzbf(list.get(i));
                ++i;
            }
            this.zzbuf.zzay(n2);
            for (i = n; i < list.size(); ++i) {
                this.zzbuf.zzba(list.get(i));
            }
            return;
        }
        while (j < list.size()) {
            this.zzbuf.zzg(i, list.get(j));
            ++j;
        }
    }
    
    @Override
    public final void zzb(final int n, final boolean b) throws IOException {
        this.zzbuf.zzb(n, b);
    }
    
    @Override
    public final void zzbk(final int n) throws IOException {
        this.zzbuf.zzc(n, 3);
    }
    
    @Override
    public final void zzbl(final int n) throws IOException {
        this.zzbuf.zzc(n, 4);
    }
    
    @Override
    public final void zzc(final int n, final long n2) throws IOException {
        this.zzbuf.zzc(n, n2);
    }
    
    @Override
    public final void zzc(int i, final List<Long> list, final boolean b) throws IOException {
        int j = 0;
        final int n = 0;
        if (b) {
            this.zzbuf.zzc(i, 2);
            i = 0;
            int n2 = 0;
            while (i < list.size()) {
                n2 += zzut.zzay(list.get(i));
                ++i;
            }
            this.zzbuf.zzay(n2);
            for (i = n; i < list.size(); ++i) {
                this.zzbuf.zzav(list.get(i));
            }
            return;
        }
        while (j < list.size()) {
            this.zzbuf.zza(i, list.get(j));
            ++j;
        }
    }
    
    @Override
    public final void zzd(final int n, final int n2) throws IOException {
        this.zzbuf.zzd(n, n2);
    }
    
    @Override
    public final void zzd(int i, final List<Long> list, final boolean b) throws IOException {
        int j = 0;
        final int n = 0;
        if (b) {
            this.zzbuf.zzc(i, 2);
            i = 0;
            int n2 = 0;
            while (i < list.size()) {
                n2 += zzut.zzaz(list.get(i));
                ++i;
            }
            this.zzbuf.zzay(n2);
            for (i = n; i < list.size(); ++i) {
                this.zzbuf.zzav(list.get(i));
            }
            return;
        }
        while (j < list.size()) {
            this.zzbuf.zza(i, list.get(j));
            ++j;
        }
    }
    
    @Override
    public final void zze(final int n, final int n2) throws IOException {
        this.zzbuf.zze(n, n2);
    }
    
    @Override
    public final void zze(int i, final List<Long> list, final boolean b) throws IOException {
        int j = 0;
        final int n = 0;
        if (b) {
            this.zzbuf.zzc(i, 2);
            i = 0;
            int n2 = 0;
            while (i < list.size()) {
                n2 += zzut.zzbb(list.get(i));
                ++i;
            }
            this.zzbuf.zzay(n2);
            for (i = n; i < list.size(); ++i) {
                this.zzbuf.zzax(list.get(i));
            }
            return;
        }
        while (j < list.size()) {
            this.zzbuf.zzc(i, list.get(j));
            ++j;
        }
    }
    
    @Override
    public final void zzf(final int n, final int n2) throws IOException {
        this.zzbuf.zzf(n, n2);
    }
    
    @Override
    public final void zzf(int i, final List<Float> list, final boolean b) throws IOException {
        int j = 0;
        final int n = 0;
        if (b) {
            this.zzbuf.zzc(i, 2);
            i = 0;
            int n2 = 0;
            while (i < list.size()) {
                n2 += zzut.zzb(list.get(i));
                ++i;
            }
            this.zzbuf.zzay(n2);
            for (i = n; i < list.size(); ++i) {
                this.zzbuf.zza(list.get(i));
            }
            return;
        }
        while (j < list.size()) {
            this.zzbuf.zza(i, list.get(j));
            ++j;
        }
    }
    
    @Override
    public final void zzg(final int n, final int n2) throws IOException {
        this.zzbuf.zzg(n, n2);
    }
    
    @Override
    public final void zzg(int i, final List<Double> list, final boolean b) throws IOException {
        int j = 0;
        final int n = 0;
        if (b) {
            this.zzbuf.zzc(i, 2);
            i = 0;
            int n2 = 0;
            while (i < list.size()) {
                n2 += zzut.zzc(list.get(i));
                ++i;
            }
            this.zzbuf.zzay(n2);
            for (i = n; i < list.size(); ++i) {
                this.zzbuf.zzb(list.get(i));
            }
            return;
        }
        while (j < list.size()) {
            this.zzbuf.zza(i, list.get(j));
            ++j;
        }
    }
    
    @Override
    public final void zzh(int i, final List<Integer> list, final boolean b) throws IOException {
        int j = 0;
        final int n = 0;
        if (b) {
            this.zzbuf.zzc(i, 2);
            i = 0;
            int n2 = 0;
            while (i < list.size()) {
                n2 += zzut.zzbh(list.get(i));
                ++i;
            }
            this.zzbuf.zzay(n2);
            for (i = n; i < list.size(); ++i) {
                this.zzbuf.zzax(list.get(i));
            }
            return;
        }
        while (j < list.size()) {
            this.zzbuf.zzd(i, list.get(j));
            ++j;
        }
    }
    
    @Override
    public final void zzi(final int n, final long n2) throws IOException {
        this.zzbuf.zza(n, n2);
    }
    
    @Override
    public final void zzi(int i, final List<Boolean> list, final boolean b) throws IOException {
        int j = 0;
        final int n = 0;
        if (b) {
            this.zzbuf.zzc(i, 2);
            i = 0;
            int n2 = 0;
            while (i < list.size()) {
                n2 += zzut.zzv(list.get(i));
                ++i;
            }
            this.zzbuf.zzay(n2);
            for (i = n; i < list.size(); ++i) {
                this.zzbuf.zzu(list.get(i));
            }
            return;
        }
        while (j < list.size()) {
            this.zzbuf.zzb(i, list.get(j));
            ++j;
        }
    }
    
    @Override
    public final void zzj(final int n, final long n2) throws IOException {
        this.zzbuf.zzc(n, n2);
    }
    
    @Override
    public final void zzj(int i, final List<Integer> list, final boolean b) throws IOException {
        int j = 0;
        final int n = 0;
        if (b) {
            this.zzbuf.zzc(i, 2);
            i = 0;
            int n2 = 0;
            while (i < list.size()) {
                n2 += zzut.zzbd(list.get(i));
                ++i;
            }
            this.zzbuf.zzay(n2);
            for (i = n; i < list.size(); ++i) {
                this.zzbuf.zzay(list.get(i));
            }
            return;
        }
        while (j < list.size()) {
            this.zzbuf.zze(i, list.get(j));
            ++j;
        }
    }
    
    @Override
    public final void zzk(int i, final List<Integer> list, final boolean b) throws IOException {
        int j = 0;
        final int n = 0;
        if (b) {
            this.zzbuf.zzc(i, 2);
            i = 0;
            int n2 = 0;
            while (i < list.size()) {
                n2 += zzut.zzbg(list.get(i));
                ++i;
            }
            this.zzbuf.zzay(n2);
            for (i = n; i < list.size(); ++i) {
                this.zzbuf.zzba(list.get(i));
            }
            return;
        }
        while (j < list.size()) {
            this.zzbuf.zzg(i, list.get(j));
            ++j;
        }
    }
    
    @Override
    public final void zzl(int i, final List<Long> list, final boolean b) throws IOException {
        int j = 0;
        final int n = 0;
        if (b) {
            this.zzbuf.zzc(i, 2);
            i = 0;
            int n2 = 0;
            while (i < list.size()) {
                n2 += zzut.zzbc(list.get(i));
                ++i;
            }
            this.zzbuf.zzay(n2);
            for (i = n; i < list.size(); ++i) {
                this.zzbuf.zzax(list.get(i));
            }
            return;
        }
        while (j < list.size()) {
            this.zzbuf.zzc(i, list.get(j));
            ++j;
        }
    }
    
    @Override
    public final void zzm(int i, final List<Integer> list, final boolean b) throws IOException {
        int j = 0;
        final int n = 0;
        if (b) {
            this.zzbuf.zzc(i, 2);
            i = 0;
            int n2 = 0;
            while (i < list.size()) {
                n2 += zzut.zzbe(list.get(i));
                ++i;
            }
            this.zzbuf.zzay(n2);
            for (i = n; i < list.size(); ++i) {
                this.zzbuf.zzaz(list.get(i));
            }
            return;
        }
        while (j < list.size()) {
            this.zzbuf.zzf(i, list.get(j));
            ++j;
        }
    }
    
    @Override
    public final void zzn(final int n, final int n2) throws IOException {
        this.zzbuf.zzg(n, n2);
    }
    
    @Override
    public final void zzn(int i, final List<Long> list, final boolean b) throws IOException {
        int j = 0;
        final int n = 0;
        if (b) {
            this.zzbuf.zzc(i, 2);
            i = 0;
            int n2 = 0;
            while (i < list.size()) {
                n2 += zzut.zzba(list.get(i));
                ++i;
            }
            this.zzbuf.zzay(n2);
            for (i = n; i < list.size(); ++i) {
                this.zzbuf.zzaw(list.get(i));
            }
            return;
        }
        while (j < list.size()) {
            this.zzbuf.zzb(i, list.get(j));
            ++j;
        }
    }
    
    @Override
    public final void zzo(final int n, final int n2) throws IOException {
        this.zzbuf.zzd(n, n2);
    }
    
    @Override
    public final int zzvj() {
        return zzvm.zze.zzbze;
    }
}
