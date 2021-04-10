package com.google.android.gms.internal.measurement;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

final class zzzd implements Cloneable
{
    private Object value;
    private zzzb<?, ?> zzcfj;
    private List<zzzi> zzcfk;
    
    zzzd() {
        this.zzcfk = new ArrayList<zzzi>();
    }
    
    private final byte[] toByteArray() throws IOException {
        final byte[] array = new byte[this.zzf()];
        this.zza(zzyy.zzo(array));
        return array;
    }
    
    private final zzzd zzyv() {
        final zzzd zzzd = new zzzd();
        try {
            zzzd.zzcfj = this.zzcfj;
            if (this.zzcfk == null) {
                zzzd.zzcfk = null;
            }
            else {
                zzzd.zzcfk.addAll(this.zzcfk);
            }
            if (this.value != null) {
                if (this.value instanceof zzzg) {
                    zzzd.value = ((zzzg)this.value).clone();
                    return zzzd;
                }
                if (this.value instanceof byte[]) {
                    zzzd.value = ((byte[])this.value).clone();
                    return zzzd;
                }
                final boolean b = this.value instanceof byte[][];
                final int n = 0;
                int i = 0;
                if (b) {
                    final byte[][] array = (byte[][])this.value;
                    final byte[][] value = new byte[array.length][];
                    zzzd.value = value;
                    while (i < array.length) {
                        value[i] = array[i].clone();
                        ++i;
                    }
                }
                else {
                    if (this.value instanceof boolean[]) {
                        zzzd.value = ((boolean[])this.value).clone();
                        return zzzd;
                    }
                    if (this.value instanceof int[]) {
                        zzzd.value = ((int[])this.value).clone();
                        return zzzd;
                    }
                    if (this.value instanceof long[]) {
                        zzzd.value = ((long[])this.value).clone();
                        return zzzd;
                    }
                    if (this.value instanceof float[]) {
                        zzzd.value = ((float[])this.value).clone();
                        return zzzd;
                    }
                    if (this.value instanceof double[]) {
                        zzzd.value = ((double[])this.value).clone();
                        return zzzd;
                    }
                    if (this.value instanceof zzzg[]) {
                        final zzzg[] array2 = (zzzg[])this.value;
                        final zzzg[] value2 = new zzzg[array2.length];
                        zzzd.value = value2;
                        for (int j = n; j < array2.length; ++j) {
                            value2[j] = (zzzg)array2[j].clone();
                        }
                    }
                }
            }
            return zzzd;
        }
        catch (CloneNotSupportedException ex) {
            throw new AssertionError((Object)ex);
        }
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zzzd)) {
            return false;
        }
        final zzzd zzzd = (zzzd)o;
        if (this.value != null && zzzd.value != null) {
            final zzzb<?, ?> zzcfj = this.zzcfj;
            if (zzcfj != zzzd.zzcfj) {
                return false;
            }
            if (!zzcfj.zzcfd.isArray()) {
                return this.value.equals(zzzd.value);
            }
            final Object value = this.value;
            if (value instanceof byte[]) {
                return Arrays.equals((byte[])value, (byte[])zzzd.value);
            }
            if (value instanceof int[]) {
                return Arrays.equals((int[])value, (int[])zzzd.value);
            }
            if (value instanceof long[]) {
                return Arrays.equals((long[])value, (long[])zzzd.value);
            }
            if (value instanceof float[]) {
                return Arrays.equals((float[])value, (float[])zzzd.value);
            }
            if (value instanceof double[]) {
                return Arrays.equals((double[])value, (double[])zzzd.value);
            }
            if (value instanceof boolean[]) {
                return Arrays.equals((boolean[])value, (boolean[])zzzd.value);
            }
            return Arrays.deepEquals((Object[])value, (Object[])zzzd.value);
        }
        else {
            final List<zzzi> zzcfk = this.zzcfk;
            if (zzcfk != null) {
                final List<zzzi> zzcfk2 = zzzd.zzcfk;
                if (zzcfk2 != null) {
                    return zzcfk.equals(zzcfk2);
                }
            }
            try {
                return Arrays.equals(this.toByteArray(), zzzd.toByteArray());
            }
            catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
        }
    }
    
    @Override
    public final int hashCode() {
        try {
            return Arrays.hashCode(this.toByteArray()) + 527;
        }
        catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    final void zza(final zzyy zzyy) throws IOException {
        final Object value = this.value;
        if (value == null) {
            for (final zzzi zzzi : this.zzcfk) {
                zzyy.zzca(zzzi.tag);
                zzyy.zzp(zzzi.zzbug);
            }
            return;
        }
        final zzzb<?, ?> zzcfj = this.zzcfj;
        if (zzcfj.zzcfe) {
            for (int length = Array.getLength(value), i = 0; i < length; ++i) {
                final Object value2 = Array.get(value, i);
                if (value2 != null) {
                    zzcfj.zza(value2, zzyy);
                }
            }
            return;
        }
        zzcfj.zza(value, zzyy);
    }
    
    final void zza(final zzzi zzzi) throws IOException {
        final List<zzzi> zzcfk = this.zzcfk;
        if (zzcfk != null) {
            zzcfk.add(zzzi);
            return;
        }
        final Object value = this.value;
        Object value2;
        if (value instanceof zzzg) {
            final byte[] zzbug = zzzi.zzbug;
            final zzyx zzj = zzyx.zzj(zzbug, 0, zzbug.length);
            final int zzuy = zzj.zzuy();
            if (zzuy != zzbug.length - zzyy.zzbc(zzuy)) {
                throw zzzf.zzyw();
            }
            value2 = ((zzzg)this.value).zza(zzj);
        }
        else if (value instanceof zzzg[]) {
            final zzzg[] array = (Object)this.zzcfj.zzah(Collections.singletonList(zzzi));
            final zzzg[] array2 = (zzzg[])this.value;
            value2 = Arrays.copyOf(array2, array2.length + array.length);
            System.arraycopy(array, 0, value2, array2.length, array.length);
        }
        else {
            value2 = this.zzcfj.zzah(Collections.singletonList(zzzi));
        }
        this.zzcfj = this.zzcfj;
        this.value = value2;
        this.zzcfk = null;
    }
    
    final int zzf() {
        final Object value = this.value;
        int n = 0;
        int n3;
        if (value != null) {
            final zzzb<?, ?> zzcfj = this.zzcfj;
            if (!zzcfj.zzcfe) {
                return zzcfj.zzak(value);
            }
            final int length = Array.getLength(value);
            int n2 = 0;
            while (true) {
                n3 = n;
                if (n2 >= length) {
                    break;
                }
                int n4 = n;
                if (Array.get(value, n2) != null) {
                    n4 = n + zzcfj.zzak(Array.get(value, n2));
                }
                ++n2;
                n = n4;
            }
        }
        else {
            final Iterator<zzzi> iterator = this.zzcfk.iterator();
            int n5 = 0;
            while (iterator.hasNext()) {
                final zzzi zzzi = iterator.next();
                n5 += zzyy.zzbj(zzzi.tag) + 0 + zzzi.zzbug.length;
            }
            n3 = n5;
        }
        return n3;
    }
}
