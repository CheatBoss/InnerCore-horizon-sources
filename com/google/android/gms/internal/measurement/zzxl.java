package com.google.android.gms.internal.measurement;

import java.util.*;
import java.io.*;

final class zzxl
{
    private static final Class<?> zzcbw;
    private static final zzyb<?, ?> zzcbx;
    private static final zzyb<?, ?> zzcby;
    private static final zzyb<?, ?> zzcbz;
    
    static {
        zzcbw = zzxu();
        zzcbx = zzx(false);
        zzcby = zzx(true);
        zzcbz = new zzyd();
    }
    
    static <UT, UB> UB zza(final int n, final int n2, final UB ub, final zzyb<UT, UB> zzyb) {
        UB zzye = ub;
        if (ub == null) {
            zzye = zzyb.zzye();
        }
        zzyb.zza(zzye, n, n2);
        return zzye;
    }
    
    static <UT, UB> UB zza(final int n, final List<Integer> list, final zzvr zzvr, UB ub, final zzyb<UT, UB> zzyb) {
        if (zzvr == null) {
            return ub;
        }
        UB ub2;
        if (list instanceof RandomAccess) {
            final int size = list.size();
            int i = 0;
            int n2 = 0;
            while (i < size) {
                final int intValue = list.get(i);
                if (zzvr.zzb(intValue)) {
                    if (i != n2) {
                        list.set(n2, intValue);
                    }
                    ++n2;
                }
                else {
                    ub = zza(n, intValue, ub, zzyb);
                }
                ++i;
            }
            ub2 = ub;
            if (n2 != size) {
                list.subList(n2, size).clear();
                return ub;
            }
        }
        else {
            final Iterator<Integer> iterator = list.iterator();
            while (true) {
                ub2 = ub;
                if (!iterator.hasNext()) {
                    break;
                }
                final int intValue2 = iterator.next();
                if (zzvr.zzb(intValue2)) {
                    continue;
                }
                ub = zza(n, intValue2, ub, zzyb);
                iterator.remove();
            }
        }
        return ub2;
    }
    
    public static void zza(final int n, final List<String> list, final zzyw zzyw) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzyw.zza(n, list);
        }
    }
    
    public static void zza(final int n, final List<?> list, final zzyw zzyw, final zzxj zzxj) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzyw.zza(n, list, zzxj);
        }
    }
    
    public static void zza(final int n, final List<Double> list, final zzyw zzyw, final boolean b) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzyw.zzg(n, list, b);
        }
    }
    
    static <T, FT extends zzvf<FT>> void zza(final zzva<FT> zzva, final T t, final T t2) {
        final zzvd<FT> zzs = zzva.zzs(t2);
        if (!zzs.isEmpty()) {
            zzva.zzt(t).zza(zzs);
        }
    }
    
    static <T> void zza(final zzwo zzwo, final T t, final T t2, final long n) {
        zzyh.zza(t, n, zzwo.zzc(zzyh.zzp(t, n), zzyh.zzp(t2, n)));
    }
    
    static <T, UT, UB> void zza(final zzyb<UT, UB> zzyb, final T t, final T t2) {
        zzyb.zzf(t, zzyb.zzh(zzyb.zzah(t), zzyb.zzah(t2)));
    }
    
    static int zzaa(final List<Integer> list) {
        final int size = list.size();
        final int n = 0;
        int n2 = 0;
        if (size == 0) {
            return 0;
        }
        int n4;
        if (list instanceof zzvn) {
            final zzvn zzvn = (zzvn)list;
            int n3 = 0;
            while (true) {
                n4 = n2;
                if (n3 >= size) {
                    break;
                }
                n2 += zzut.zzbh(zzvn.getInt(n3));
                ++n3;
            }
        }
        else {
            int n5 = 0;
            int n6 = n;
            while (true) {
                n4 = n6;
                if (n5 >= size) {
                    break;
                }
                n6 += zzut.zzbh(list.get(n5));
                ++n5;
            }
        }
        return n4;
    }
    
    static int zzab(final List<Integer> list) {
        final int size = list.size();
        final int n = 0;
        int n2 = 0;
        if (size == 0) {
            return 0;
        }
        int n4;
        if (list instanceof zzvn) {
            final zzvn zzvn = (zzvn)list;
            int n3 = 0;
            while (true) {
                n4 = n2;
                if (n3 >= size) {
                    break;
                }
                n2 += zzut.zzbc(zzvn.getInt(n3));
                ++n3;
            }
        }
        else {
            int n5 = 0;
            int n6 = n;
            while (true) {
                n4 = n6;
                if (n5 >= size) {
                    break;
                }
                n6 += zzut.zzbc(list.get(n5));
                ++n5;
            }
        }
        return n4;
    }
    
    static int zzac(final List<Integer> list) {
        final int size = list.size();
        final int n = 0;
        int n2 = 0;
        if (size == 0) {
            return 0;
        }
        int n4;
        if (list instanceof zzvn) {
            final zzvn zzvn = (zzvn)list;
            int n3 = 0;
            while (true) {
                n4 = n2;
                if (n3 >= size) {
                    break;
                }
                n2 += zzut.zzbd(zzvn.getInt(n3));
                ++n3;
            }
        }
        else {
            int n5 = 0;
            int n6 = n;
            while (true) {
                n4 = n6;
                if (n5 >= size) {
                    break;
                }
                n6 += zzut.zzbd(list.get(n5));
                ++n5;
            }
        }
        return n4;
    }
    
    static int zzad(final List<Integer> list) {
        final int size = list.size();
        final int n = 0;
        int n2 = 0;
        if (size == 0) {
            return 0;
        }
        int n4;
        if (list instanceof zzvn) {
            final zzvn zzvn = (zzvn)list;
            int n3 = 0;
            while (true) {
                n4 = n2;
                if (n3 >= size) {
                    break;
                }
                n2 += zzut.zzbe(zzvn.getInt(n3));
                ++n3;
            }
        }
        else {
            int n5 = 0;
            int n6 = n;
            while (true) {
                n4 = n6;
                if (n5 >= size) {
                    break;
                }
                n6 += zzut.zzbe(list.get(n5));
                ++n5;
            }
        }
        return n4;
    }
    
    static int zzae(final List<?> list) {
        return list.size() << 2;
    }
    
    static int zzaf(final List<?> list) {
        return list.size() << 3;
    }
    
    static int zzag(final List<?> list) {
        return list.size();
    }
    
    public static void zzb(final int n, final List<zzud> list, final zzyw zzyw) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzyw.zzb(n, list);
        }
    }
    
    public static void zzb(final int n, final List<?> list, final zzyw zzyw, final zzxj zzxj) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzyw.zzb(n, list, zzxj);
        }
    }
    
    public static void zzb(final int n, final List<Float> list, final zzyw zzyw, final boolean b) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzyw.zzf(n, list, b);
        }
    }
    
    static int zzc(final int n, final Object o, final zzxj zzxj) {
        if (o instanceof zzwa) {
            return zzut.zza(n, (zzwa)o);
        }
        return zzut.zzb(n, (zzwt)o, zzxj);
    }
    
    static int zzc(int n, final List<?> list) {
        final int size = list.size();
        int n2 = 0;
        final int n3 = 0;
        if (size == 0) {
            return 0;
        }
        final int n4 = n = zzut.zzbb(n) * size;
        int n6;
        if (list instanceof zzwc) {
            final zzwc zzwc = (zzwc)list;
            n = n4;
            int n5 = n3;
            while (true) {
                n6 = n;
                if (n5 >= size) {
                    break;
                }
                final Object raw = zzwc.getRaw(n5);
                int n7;
                if (raw instanceof zzud) {
                    n7 = zzut.zzb((zzud)raw);
                }
                else {
                    n7 = zzut.zzfx((String)raw);
                }
                n += n7;
                ++n5;
            }
        }
        else {
            while (true) {
                n6 = n;
                if (n2 >= size) {
                    break;
                }
                final zzud value = list.get(n2);
                int n8;
                if (value instanceof zzud) {
                    n8 = zzut.zzb(value);
                }
                else {
                    n8 = zzut.zzfx((String)value);
                }
                n += n8;
                ++n2;
            }
        }
        return n6;
    }
    
    static int zzc(int i, final List<?> list, final zzxj zzxj) {
        final int size = list.size();
        final int n = 0;
        if (size == 0) {
            return 0;
        }
        int n2 = zzut.zzbb(i) * size;
        Object value;
        int n3;
        for (i = n; i < size; ++i) {
            value = list.get(i);
            if (value instanceof zzwa) {
                n3 = zzut.zza((zzwa)value);
            }
            else {
                n3 = zzut.zzb((zzwt)value, zzxj);
            }
            n2 += n3;
        }
        return n2;
    }
    
    public static void zzc(final int n, final List<Long> list, final zzyw zzyw, final boolean b) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzyw.zzc(n, list, b);
        }
    }
    
    static int zzd(int i, final List<zzud> list) {
        final int size = list.size();
        final int n = 0;
        if (size == 0) {
            return 0;
        }
        final int n2 = size * zzut.zzbb(i);
        i = n;
        int n3 = n2;
        while (i < list.size()) {
            n3 += zzut.zzb(list.get(i));
            ++i;
        }
        return n3;
    }
    
    static int zzd(final int n, final List<zzwt> list, final zzxj zzxj) {
        final int size = list.size();
        int i = 0;
        if (size == 0) {
            return 0;
        }
        int n2 = 0;
        while (i < size) {
            n2 += zzut.zzc(n, list.get(i), zzxj);
            ++i;
        }
        return n2;
    }
    
    public static void zzd(final int n, final List<Long> list, final zzyw zzyw, final boolean b) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzyw.zzd(n, list, b);
        }
    }
    
    public static void zze(final int n, final List<Long> list, final zzyw zzyw, final boolean b) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzyw.zzn(n, list, b);
        }
    }
    
    static boolean zze(final Object o, final Object o2) {
        return o == o2 || (o != null && o.equals(o2));
    }
    
    public static void zzf(final int n, final List<Long> list, final zzyw zzyw, final boolean b) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzyw.zze(n, list, b);
        }
    }
    
    public static void zzg(final int n, final List<Long> list, final zzyw zzyw, final boolean b) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzyw.zzl(n, list, b);
        }
    }
    
    public static void zzh(final int n, final List<Integer> list, final zzyw zzyw, final boolean b) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzyw.zza(n, list, b);
        }
    }
    
    public static void zzi(final int n, final List<Integer> list, final zzyw zzyw, final boolean b) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzyw.zzj(n, list, b);
        }
    }
    
    public static void zzj(final int n, final List<Integer> list, final zzyw zzyw, final boolean b) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzyw.zzm(n, list, b);
        }
    }
    
    public static void zzj(final Class<?> clazz) {
        if (!zzvm.class.isAssignableFrom(clazz)) {
            final Class<?> zzcbw = zzxl.zzcbw;
            if (zzcbw != null) {
                if (zzcbw.isAssignableFrom(clazz)) {
                    return;
                }
                throw new IllegalArgumentException("Message classes must extend GeneratedMessage or GeneratedMessageLite");
            }
        }
    }
    
    public static void zzk(final int n, final List<Integer> list, final zzyw zzyw, final boolean b) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzyw.zzb(n, list, b);
        }
    }
    
    public static void zzl(final int n, final List<Integer> list, final zzyw zzyw, final boolean b) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzyw.zzk(n, list, b);
        }
    }
    
    public static void zzm(final int n, final List<Integer> list, final zzyw zzyw, final boolean b) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzyw.zzh(n, list, b);
        }
    }
    
    public static void zzn(final int n, final List<Boolean> list, final zzyw zzyw, final boolean b) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzyw.zzi(n, list, b);
        }
    }
    
    static int zzo(final int n, final List<Long> list, final boolean b) {
        if (list.size() == 0) {
            return 0;
        }
        return zzx(list) + list.size() * zzut.zzbb(n);
    }
    
    static int zzp(final int n, final List<Long> list, final boolean b) {
        final int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zzy(list) + size * zzut.zzbb(n);
    }
    
    static int zzq(final int n, final List<Long> list, final boolean b) {
        final int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zzz(list) + size * zzut.zzbb(n);
    }
    
    static int zzr(final int n, final List<Integer> list, final boolean b) {
        final int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zzaa(list) + size * zzut.zzbb(n);
    }
    
    static int zzs(final int n, final List<Integer> list, final boolean b) {
        final int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zzab(list) + size * zzut.zzbb(n);
    }
    
    static int zzt(final int n, final List<Integer> list, final boolean b) {
        final int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zzac(list) + size * zzut.zzbb(n);
    }
    
    static int zzu(final int n, final List<Integer> list, final boolean b) {
        final int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zzad(list) + size * zzut.zzbb(n);
    }
    
    static int zzv(final int n, final List<?> list, final boolean b) {
        final int size = list.size();
        if (size == 0) {
            return 0;
        }
        return size * zzut.zzk(n, 0);
    }
    
    static int zzw(final int n, final List<?> list, final boolean b) {
        final int size = list.size();
        if (size == 0) {
            return 0;
        }
        return size * zzut.zzg(n, 0L);
    }
    
    static int zzx(final int n, final List<?> list, final boolean b) {
        final int size = list.size();
        if (size == 0) {
            return 0;
        }
        return size * zzut.zzc(n, true);
    }
    
    static int zzx(final List<Long> list) {
        final int size = list.size();
        final int n = 0;
        int n2 = 0;
        if (size == 0) {
            return 0;
        }
        int n4;
        if (list instanceof zzwh) {
            final zzwh zzwh = (zzwh)list;
            int n3 = 0;
            while (true) {
                n4 = n2;
                if (n3 >= size) {
                    break;
                }
                n2 += zzut.zzay(zzwh.getLong(n3));
                ++n3;
            }
        }
        else {
            int n5 = 0;
            int n6 = n;
            while (true) {
                n4 = n6;
                if (n5 >= size) {
                    break;
                }
                n6 += zzut.zzay(list.get(n5));
                ++n5;
            }
        }
        return n4;
    }
    
    private static zzyb<?, ?> zzx(final boolean b) {
        try {
            final Class<?> zzxv = zzxv();
            if (zzxv == null) {}
            return (zzyb<?, ?>)zzxv.getConstructor(Boolean.TYPE).newInstance(b);
        }
        finally {
            return null;
        }
    }
    
    public static zzyb<?, ?> zzxr() {
        return zzxl.zzcbx;
    }
    
    public static zzyb<?, ?> zzxs() {
        return zzxl.zzcby;
    }
    
    public static zzyb<?, ?> zzxt() {
        return zzxl.zzcbz;
    }
    
    private static Class<?> zzxu() {
        try {
            return Class.forName("com.google.protobuf.GeneratedMessage");
        }
        finally {
            return null;
        }
    }
    
    private static Class<?> zzxv() {
        try {
            return Class.forName("com.google.protobuf.UnknownFieldSetSchema");
        }
        finally {
            return null;
        }
    }
    
    static int zzy(final List<Long> list) {
        final int size = list.size();
        final int n = 0;
        int n2 = 0;
        if (size == 0) {
            return 0;
        }
        int n4;
        if (list instanceof zzwh) {
            final zzwh zzwh = (zzwh)list;
            int n3 = 0;
            while (true) {
                n4 = n2;
                if (n3 >= size) {
                    break;
                }
                n2 += zzut.zzaz(zzwh.getLong(n3));
                ++n3;
            }
        }
        else {
            int n5 = 0;
            int n6 = n;
            while (true) {
                n4 = n6;
                if (n5 >= size) {
                    break;
                }
                n6 += zzut.zzaz(list.get(n5));
                ++n5;
            }
        }
        return n4;
    }
    
    static int zzz(final List<Long> list) {
        final int size = list.size();
        final int n = 0;
        int n2 = 0;
        if (size == 0) {
            return 0;
        }
        int n4;
        if (list instanceof zzwh) {
            final zzwh zzwh = (zzwh)list;
            int n3 = 0;
            while (true) {
                n4 = n2;
                if (n3 >= size) {
                    break;
                }
                n2 += zzut.zzba(zzwh.getLong(n3));
                ++n3;
            }
        }
        else {
            int n5 = 0;
            int n6 = n;
            while (true) {
                n4 = n6;
                if (n5 >= size) {
                    break;
                }
                n6 += zzut.zzba(list.get(n5));
                ++n5;
            }
        }
        return n4;
    }
}
