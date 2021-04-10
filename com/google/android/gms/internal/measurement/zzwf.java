package com.google.android.gms.internal.measurement;

import java.util.*;

final class zzwf extends zzwd
{
    private static final Class<?> zzcal;
    
    static {
        zzcal = Collections.unmodifiableList(Collections.emptyList()).getClass();
    }
    
    private zzwf() {
        super(null);
    }
    
    private static <L> List<L> zza(final Object o, final long n, final int n2) {
        final List<Object> zzc = zzc(o, n);
        if (zzc.isEmpty()) {
            Object zzak;
            if (zzc instanceof zzwc) {
                zzak = new zzwb(n2);
            }
            else if (zzc instanceof zzxe && zzc instanceof zzvs) {
                zzak = ((zzvs<L>)zzc).zzak(n2);
            }
            else {
                zzak = new ArrayList<L>(n2);
            }
            zzyh.zza(o, n, zzak);
            return (List<L>)zzak;
        }
        RandomAccess randomAccess;
        if (zzwf.zzcal.isAssignableFrom(((zzvs<Object>)zzc).getClass())) {
            randomAccess = new ArrayList<Object>(zzc.size() + n2);
            ((ArrayList)randomAccess).addAll(zzc);
        }
        else {
            if (!(zzc instanceof zzye)) {
                Object zzak2 = zzc;
                if (zzc instanceof zzxe) {
                    zzak2 = zzc;
                    if (zzc instanceof zzvs) {
                        final zzvs<L> zzvs = (zzvs<L>)zzc;
                        zzak2 = zzc;
                        if (!zzvs.zztw()) {
                            zzak2 = zzvs.zzak(zzc.size() + n2);
                            zzyh.zza(o, n, zzak2);
                        }
                    }
                }
                return (List<L>)zzak2;
            }
            randomAccess = new zzwb(zzc.size() + n2);
            ((zztz)randomAccess).addAll(zzc);
        }
        zzyh.zza(o, n, randomAccess);
        return (List<L>)randomAccess;
    }
    
    private static <E> List<E> zzc(final Object o, final long n) {
        return (List<E>)zzyh.zzp(o, n);
    }
    
    @Override
    final <L> List<L> zza(final Object o, final long n) {
        return zza(o, n, 10);
    }
    
    @Override
    final <E> void zza(final Object o, final Object o2, final long n) {
        List<?> zzc = zzc(o2, n);
        final List<Object> zza = zza(o, n, zzc.size());
        final int size = zza.size();
        final int size2 = zzc.size();
        if (size > 0 && size2 > 0) {
            zza.addAll(zzc);
        }
        if (size > 0) {
            zzc = zza;
        }
        zzyh.zza(o, n, zzc);
    }
    
    @Override
    final void zzb(final Object o, final long n) {
        final List list = (List)zzyh.zzp(o, n);
        List<Object> list2;
        if (list instanceof zzwc) {
            list2 = (List<Object>)((zzwc)list).zzww();
        }
        else {
            if (zzwf.zzcal.isAssignableFrom(((zzvs<? extends T>)list).getClass())) {
                return;
            }
            if (list instanceof zzxe && list instanceof zzvs) {
                final zzvs<? extends T> zzvs = (zzvs<? extends T>)list;
                if (zzvs.zztw()) {
                    zzvs.zzsm();
                }
                return;
            }
            list2 = Collections.unmodifiableList((List<?>)list);
        }
        zzyh.zza(o, n, list2);
    }
}
