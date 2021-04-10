package com.google.android.gms.internal.measurement;

import java.io.*;

public final class zzge extends zzza<zzge>
{
    private static volatile zzge[] zzawp;
    public Integer zzawq;
    public Long zzawr;
    
    public zzge() {
        this.zzawq = null;
        this.zzawr = null;
        this.zzcfc = null;
        this.zzcfm = -1;
    }
    
    public static zzge[] zzmp() {
        if (zzge.zzawp == null) {
            synchronized (zzze.zzcfl) {
                if (zzge.zzawp == null) {
                    zzge.zzawp = new zzge[0];
                }
            }
        }
        return zzge.zzawp;
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zzge)) {
            return false;
        }
        final zzge zzge = (zzge)o;
        final Integer zzawq = this.zzawq;
        if (zzawq == null) {
            if (zzge.zzawq != null) {
                return false;
            }
        }
        else if (!zzawq.equals(zzge.zzawq)) {
            return false;
        }
        final Long zzawr = this.zzawr;
        if (zzawr == null) {
            if (zzge.zzawr != null) {
                return false;
            }
        }
        else if (!zzawr.equals(zzge.zzawr)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(zzge.zzcfc);
        }
        return zzge.zzcfc == null || zzge.zzcfc.isEmpty();
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
        final Long zzawr = this.zzawr;
        int hashCode3;
        if (zzawr == null) {
            hashCode3 = 0;
        }
        else {
            hashCode3 = zzawr.hashCode();
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
        final Integer zzawq = this.zzawq;
        if (zzawq != null) {
            zzyy.zzd(1, zzawq);
        }
        final Long zzawr = this.zzawr;
        if (zzawr != null) {
            zzyy.zzi(2, zzawr);
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
        final Long zzawr = this.zzawr;
        int n2 = n;
        if (zzawr != null) {
            n2 = n + zzyy.zzd(2, zzawr);
        }
        return n2;
    }
}
