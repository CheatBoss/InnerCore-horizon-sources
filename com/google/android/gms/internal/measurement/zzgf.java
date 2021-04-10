package com.google.android.gms.internal.measurement;

import java.io.*;

public final class zzgf extends zzza<zzgf>
{
    private static volatile zzgf[] zzaws;
    public Integer count;
    public String name;
    public zzgg[] zzawt;
    public Long zzawu;
    public Long zzawv;
    
    public zzgf() {
        this.zzawt = zzgg.zzmr();
        this.name = null;
        this.zzawu = null;
        this.zzawv = null;
        this.count = null;
        this.zzcfc = null;
        this.zzcfm = -1;
    }
    
    public static zzgf[] zzmq() {
        if (zzgf.zzaws == null) {
            synchronized (zzze.zzcfl) {
                if (zzgf.zzaws == null) {
                    zzgf.zzaws = new zzgf[0];
                }
            }
        }
        return zzgf.zzaws;
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zzgf)) {
            return false;
        }
        final zzgf zzgf = (zzgf)o;
        if (!zzze.equals(this.zzawt, zzgf.zzawt)) {
            return false;
        }
        final String name = this.name;
        if (name == null) {
            if (zzgf.name != null) {
                return false;
            }
        }
        else if (!name.equals(zzgf.name)) {
            return false;
        }
        final Long zzawu = this.zzawu;
        if (zzawu == null) {
            if (zzgf.zzawu != null) {
                return false;
            }
        }
        else if (!zzawu.equals(zzgf.zzawu)) {
            return false;
        }
        final Long zzawv = this.zzawv;
        if (zzawv == null) {
            if (zzgf.zzawv != null) {
                return false;
            }
        }
        else if (!zzawv.equals(zzgf.zzawv)) {
            return false;
        }
        final Integer count = this.count;
        if (count == null) {
            if (zzgf.count != null) {
                return false;
            }
        }
        else if (!count.equals(zzgf.count)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(zzgf.zzcfc);
        }
        return zzgf.zzcfc == null || zzgf.zzcfc.isEmpty();
    }
    
    @Override
    public final int hashCode() {
        final int hashCode = this.getClass().getName().hashCode();
        final int hashCode2 = zzze.hashCode(this.zzawt);
        final String name = this.name;
        final boolean b = false;
        int hashCode3;
        if (name == null) {
            hashCode3 = 0;
        }
        else {
            hashCode3 = name.hashCode();
        }
        final Long zzawu = this.zzawu;
        int hashCode4;
        if (zzawu == null) {
            hashCode4 = 0;
        }
        else {
            hashCode4 = zzawu.hashCode();
        }
        final Long zzawv = this.zzawv;
        int hashCode5;
        if (zzawv == null) {
            hashCode5 = 0;
        }
        else {
            hashCode5 = zzawv.hashCode();
        }
        final Integer count = this.count;
        int hashCode6;
        if (count == null) {
            hashCode6 = 0;
        }
        else {
            hashCode6 = count.hashCode();
        }
        int hashCode7 = b ? 1 : 0;
        if (this.zzcfc != null) {
            if (this.zzcfc.isEmpty()) {
                hashCode7 = (b ? 1 : 0);
            }
            else {
                hashCode7 = this.zzcfc.hashCode();
            }
        }
        return ((((((hashCode + 527) * 31 + hashCode2) * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode5) * 31 + hashCode6) * 31 + hashCode7;
    }
    
    @Override
    public final void zza(final zzyy zzyy) throws IOException {
        final zzgg[] zzawt = this.zzawt;
        if (zzawt != null && zzawt.length > 0) {
            int n = 0;
            while (true) {
                final zzgg[] zzawt2 = this.zzawt;
                if (n >= zzawt2.length) {
                    break;
                }
                final zzgg zzgg = zzawt2[n];
                if (zzgg != null) {
                    zzyy.zza(1, zzgg);
                }
                ++n;
            }
        }
        final String name = this.name;
        if (name != null) {
            zzyy.zzb(2, name);
        }
        final Long zzawu = this.zzawu;
        if (zzawu != null) {
            zzyy.zzi(3, zzawu);
        }
        final Long zzawv = this.zzawv;
        if (zzawv != null) {
            zzyy.zzi(4, zzawv);
        }
        final Integer count = this.count;
        if (count != null) {
            zzyy.zzd(5, count);
        }
        super.zza(zzyy);
    }
    
    @Override
    protected final int zzf() {
        int zzf = super.zzf();
        final zzgg[] zzawt = this.zzawt;
        int n = zzf;
        if (zzawt != null) {
            n = zzf;
            if (zzawt.length > 0) {
                int n2 = 0;
                while (true) {
                    final zzgg[] zzawt2 = this.zzawt;
                    n = zzf;
                    if (n2 >= zzawt2.length) {
                        break;
                    }
                    final zzgg zzgg = zzawt2[n2];
                    int n3 = zzf;
                    if (zzgg != null) {
                        n3 = zzf + zzyy.zzb(1, zzgg);
                    }
                    ++n2;
                    zzf = n3;
                }
            }
        }
        final String name = this.name;
        int n4 = n;
        if (name != null) {
            n4 = n + zzyy.zzc(2, name);
        }
        final Long zzawu = this.zzawu;
        int n5 = n4;
        if (zzawu != null) {
            n5 = n4 + zzyy.zzd(3, zzawu);
        }
        final Long zzawv = this.zzawv;
        int n6 = n5;
        if (zzawv != null) {
            n6 = n5 + zzyy.zzd(4, zzawv);
        }
        final Integer count = this.count;
        int n7 = n6;
        if (count != null) {
            n7 = n6 + zzyy.zzh(5, count);
        }
        return n7;
    }
}
