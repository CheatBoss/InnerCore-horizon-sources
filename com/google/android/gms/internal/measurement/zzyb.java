package com.google.android.gms.internal.measurement;

import java.io.*;

abstract class zzyb<T, B>
{
    abstract void zza(final B p0, final int p1, final long p2);
    
    abstract void zza(final B p0, final int p1, final zzud p2);
    
    abstract void zza(final B p0, final int p1, final T p2);
    
    abstract void zza(final T p0, final zzyw p1) throws IOException;
    
    abstract boolean zza(final zzxi p0);
    
    final boolean zza(final B b, final zzxi zzxi) throws IOException {
        final int tag = zzxi.getTag();
        final int n = tag >>> 3;
        final int n2 = tag & 0x7;
        if (n2 == 0) {
            this.zza(b, n, zzxi.zzui());
            return true;
        }
        if (n2 == 1) {
            this.zzb(b, n, zzxi.zzuk());
            return true;
        }
        if (n2 == 2) {
            this.zza(b, n, zzxi.zzuo());
            return true;
        }
        if (n2 != 3) {
            if (n2 == 4) {
                return false;
            }
            if (n2 == 5) {
                this.zzc(b, n, zzxi.zzul());
                return true;
            }
            throw zzvt.zzwo();
        }
        else {
            final B zzye = this.zzye();
            while (zzxi.zzve() != Integer.MAX_VALUE && this.zza(zzye, zzxi)) {}
            if ((0x4 | n << 3) == zzxi.getTag()) {
                this.zza(b, n, this.zzab(zzye));
                return true;
            }
            throw zzvt.zzwn();
        }
    }
    
    abstract T zzab(final B p0);
    
    abstract int zzae(final T p0);
    
    abstract T zzah(final Object p0);
    
    abstract B zzai(final Object p0);
    
    abstract int zzaj(final T p0);
    
    abstract void zzb(final B p0, final int p1, final long p2);
    
    abstract void zzc(final B p0, final int p1, final int p2);
    
    abstract void zzc(final T p0, final zzyw p1) throws IOException;
    
    abstract void zzf(final Object p0, final T p1);
    
    abstract void zzg(final Object p0, final B p1);
    
    abstract T zzh(final T p0, final T p1);
    
    abstract void zzu(final Object p0);
    
    abstract B zzye();
}
