package com.google.android.gms.internal.measurement;

import java.io.*;
import java.util.*;

final class zzvb extends zzva<Object>
{
    @Override
    final Object zza(final zzuz zzuz, final zzwt zzwt, final int n) {
        return zzuz.zza(zzwt, n);
    }
    
    @Override
    final <UT, UB> UB zza(final zzxi zzxi, final Object o, final zzuz zzuz, final zzvd<Object> zzvd, final UB ub, final zzyb<UT, UB> zzyb) throws IOException {
        throw new NoSuchMethodError();
    }
    
    @Override
    final void zza(final zzud zzud, final Object o, final zzuz zzuz, final zzvd<Object> zzvd) throws IOException {
        throw new NoSuchMethodError();
    }
    
    @Override
    final void zza(final zzxi zzxi, final Object o, final zzuz zzuz, final zzvd<Object> zzvd) throws IOException {
        throw new NoSuchMethodError();
    }
    
    @Override
    final void zza(final zzyw zzyw, final Map.Entry<?, ?> entry) throws IOException {
        entry.getKey();
        throw new NoSuchMethodError();
    }
    
    @Override
    final void zza(final Object o, final zzvd<Object> zzbys) {
        ((zzvm.zzc)o).zzbys = zzbys;
    }
    
    @Override
    final int zzb(final Map.Entry<?, ?> entry) {
        entry.getKey();
        throw new NoSuchMethodError();
    }
    
    @Override
    final boolean zze(final zzwt zzwt) {
        return zzwt instanceof zzvm.zzc;
    }
    
    @Override
    final zzvd<Object> zzs(final Object o) {
        return ((zzvm.zzc)o).zzbys;
    }
    
    @Override
    final zzvd<Object> zzt(final Object o) {
        zzvd<Object> zzs;
        final zzvd<Object> zzvd = zzs = this.zzs(o);
        if (zzvd.isImmutable()) {
            zzs = (zzvd<Object>)zzvd.clone();
            this.zza(o, zzs);
        }
        return zzs;
    }
    
    @Override
    final void zzu(final Object o) {
        this.zzs(o).zzsm();
    }
}
