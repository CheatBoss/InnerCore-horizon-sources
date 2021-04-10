package com.google.android.gms.internal.measurement;

import java.util.*;

final class zzwg extends zzwd
{
    private zzwg() {
        super(null);
    }
    
    private static <E> zzvs<E> zzd(final Object o, final long n) {
        return (zzvs<E>)zzyh.zzp(o, n);
    }
    
    @Override
    final <L> List<L> zza(final Object o, final long n) {
        zzvs<L> zzvs2;
        final zzvs<L> zzvs = zzvs2 = zzd(o, n);
        if (!zzvs.zztw()) {
            final int size = zzvs.size();
            int n2;
            if (size == 0) {
                n2 = 10;
            }
            else {
                n2 = size << 1;
            }
            zzvs2 = zzvs.zzak(n2);
            zzyh.zza(o, n, zzvs2);
        }
        return zzvs2;
    }
    
    @Override
    final <E> void zza(final Object o, final Object o2, final long n) {
        final zzvs<Object> zzd = zzd(o, n);
        final zzvs<Object> zzd2 = zzd(o2, n);
        final int size = zzd.size();
        final int size2 = zzd2.size();
        zzvs<Object> zzak = zzd;
        if (size > 0) {
            zzak = zzd;
            if (size2 > 0) {
                zzak = zzd;
                if (!zzd.zztw()) {
                    zzak = zzd.zzak(size2 + size);
                }
                zzak.addAll(zzd2);
            }
        }
        zzvs<Object> zzvs = zzd2;
        if (size > 0) {
            zzvs = zzak;
        }
        zzyh.zza(o, n, zzvs);
    }
    
    @Override
    final void zzb(final Object o, final long n) {
        zzd(o, n).zzsm();
    }
}
