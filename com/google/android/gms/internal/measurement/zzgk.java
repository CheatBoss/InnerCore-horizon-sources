package com.google.android.gms.internal.measurement;

import java.io.*;

public final class zzgk extends zzza<zzgk>
{
    private static volatile zzgk[] zzayi;
    public Integer zzawq;
    public long[] zzayj;
    
    public zzgk() {
        this.zzawq = null;
        this.zzayj = zzzj.zzcfr;
        this.zzcfc = null;
        this.zzcfm = -1;
    }
    
    public static zzgk[] zzmt() {
        if (zzgk.zzayi == null) {
            synchronized (zzze.zzcfl) {
                if (zzgk.zzayi == null) {
                    zzgk.zzayi = new zzgk[0];
                }
            }
        }
        return zzgk.zzayi;
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zzgk)) {
            return false;
        }
        final zzgk zzgk = (zzgk)o;
        final Integer zzawq = this.zzawq;
        if (zzawq == null) {
            if (zzgk.zzawq != null) {
                return false;
            }
        }
        else if (!zzawq.equals(zzgk.zzawq)) {
            return false;
        }
        if (!zzze.equals(this.zzayj, zzgk.zzayj)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(zzgk.zzcfc);
        }
        return zzgk.zzcfc == null || zzgk.zzcfc.isEmpty();
    }
    
    @Override
    public final int hashCode() {
        final int hashCode = this.getClass().getName().hashCode();
        final Integer zzawq = this.zzawq;
        final boolean b = false;
        int hashCode2;
        if (zzawq == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = zzawq.hashCode();
        }
        final int hashCode3 = zzze.hashCode(this.zzayj);
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
        final Integer zzawq = this.zzawq;
        if (zzawq != null) {
            zzyy.zzd(1, zzawq);
        }
        final long[] zzayj = this.zzayj;
        if (zzayj != null && zzayj.length > 0) {
            int n = 0;
            while (true) {
                final long[] zzayj2 = this.zzayj;
                if (n >= zzayj2.length) {
                    break;
                }
                zzyy.zzi(2, zzayj2[n]);
                ++n;
            }
        }
        super.zza(zzyy);
    }
    
    @Override
    protected final int zzf() {
        final int zzf = super.zzf();
        final Integer zzawq = this.zzawq;
        int n = zzf;
        if (zzawq != null) {
            n = zzf + zzyy.zzh(1, zzawq);
        }
        final long[] zzayj = this.zzayj;
        int n2 = n;
        if (zzayj != null) {
            n2 = n;
            if (zzayj.length > 0) {
                int n3 = 0;
                int n4 = 0;
                long[] zzayj2;
                while (true) {
                    zzayj2 = this.zzayj;
                    if (n3 >= zzayj2.length) {
                        break;
                    }
                    n4 += zzyy.zzbi(zzayj2[n3]);
                    ++n3;
                }
                n2 = n + n4 + zzayj2.length * 1;
            }
        }
        return n2;
    }
}
