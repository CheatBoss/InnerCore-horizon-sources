package com.google.android.gms.internal.measurement;

import java.io.*;

public final class zzgc extends zzza<zzgc>
{
    private static volatile zzgc[] zzawk;
    public String value;
    public String zzoj;
    
    public zzgc() {
        this.zzoj = null;
        this.value = null;
        this.zzcfc = null;
        this.zzcfm = -1;
    }
    
    public static zzgc[] zzmn() {
        if (zzgc.zzawk == null) {
            synchronized (zzze.zzcfl) {
                if (zzgc.zzawk == null) {
                    zzgc.zzawk = new zzgc[0];
                }
            }
        }
        return zzgc.zzawk;
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zzgc)) {
            return false;
        }
        final zzgc zzgc = (zzgc)o;
        final String zzoj = this.zzoj;
        if (zzoj == null) {
            if (zzgc.zzoj != null) {
                return false;
            }
        }
        else if (!zzoj.equals(zzgc.zzoj)) {
            return false;
        }
        final String value = this.value;
        if (value == null) {
            if (zzgc.value != null) {
                return false;
            }
        }
        else if (!value.equals(zzgc.value)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(zzgc.zzcfc);
        }
        return zzgc.zzcfc == null || zzgc.zzcfc.isEmpty();
    }
    
    @Override
    public final int hashCode() {
        final int hashCode = this.getClass().getName().hashCode();
        final String zzoj = this.zzoj;
        final boolean b = false;
        int hashCode2;
        if (zzoj == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = zzoj.hashCode();
        }
        final String value = this.value;
        int hashCode3;
        if (value == null) {
            hashCode3 = 0;
        }
        else {
            hashCode3 = value.hashCode();
        }
        int hashCode4 = b ? 1 : 0;
        if (this.zzcfc != null) {
            if (this.zzcfc.isEmpty()) {
                hashCode4 = (b ? 1 : 0);
            }
            else {
                hashCode4 = this.zzcfc.hashCode();
            }
        }
        return (((hashCode + 527) * 31 + hashCode2) * 31 + hashCode3) * 31 + hashCode4;
    }
    
    @Override
    public final void zza(final zzyy zzyy) throws IOException {
        final String zzoj = this.zzoj;
        if (zzoj != null) {
            zzyy.zzb(1, zzoj);
        }
        final String value = this.value;
        if (value != null) {
            zzyy.zzb(2, value);
        }
        super.zza(zzyy);
    }
    
    @Override
    protected final int zzf() {
        final int zzf = super.zzf();
        final String zzoj = this.zzoj;
        int n = zzf;
        if (zzoj != null) {
            n = zzf + zzyy.zzc(1, zzoj);
        }
        final String value = this.value;
        int n2 = n;
        if (value != null) {
            n2 = n + zzyy.zzc(2, value);
        }
        return n2;
    }
}
