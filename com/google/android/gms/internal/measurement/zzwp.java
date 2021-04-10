package com.google.android.gms.internal.measurement;

import java.util.*;

final class zzwp implements zzwo
{
    @Override
    public final boolean zzaa(final Object o) {
        return !((zzwn)o).isMutable();
    }
    
    @Override
    public final Object zzab(final Object o) {
        ((zzwn)o).zzsm();
        return o;
    }
    
    @Override
    public final Object zzac(final Object o) {
        return zzwn.zzxa().zzxb();
    }
    
    @Override
    public final zzwm<?, ?> zzad(final Object o) {
        throw new NoSuchMethodError();
    }
    
    @Override
    public final int zzb(final int n, final Object o, final Object o2) {
        final zzwn zzwn = (zzwn)o;
        if (zzwn.isEmpty()) {
            return 0;
        }
        final Iterator iterator = zzwn.entrySet().iterator();
        if (!iterator.hasNext()) {
            return 0;
        }
        final Map.Entry entry = iterator.next();
        entry.getKey();
        entry.getValue();
        throw new NoSuchMethodError();
    }
    
    @Override
    public final Object zzc(final Object o, final Object o2) {
        final zzwn zzwn = (zzwn)o;
        final zzwn zzwn2 = (zzwn)o2;
        zzwn zzxb = zzwn;
        if (!zzwn2.isEmpty()) {
            zzxb = zzwn;
            if (!zzwn.isMutable()) {
                zzxb = zzwn.zzxb();
            }
            zzxb.zza(zzwn2);
        }
        return zzxb;
    }
    
    @Override
    public final Map<?, ?> zzy(final Object o) {
        return (Map<?, ?>)o;
    }
    
    @Override
    public final Map<?, ?> zzz(final Object o) {
        return (Map<?, ?>)o;
    }
}
