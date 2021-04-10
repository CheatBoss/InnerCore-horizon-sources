package com.google.android.gms.internal.measurement;

import java.io.*;

public final class zzgb extends zzza<zzgb>
{
    public String zzafx;
    public Long zzawe;
    private Integer zzawf;
    public zzgc[] zzawg;
    public zzga[] zzawh;
    public zzfu[] zzawi;
    private String zzawj;
    
    public zzgb() {
        this.zzawe = null;
        this.zzafx = null;
        this.zzawf = null;
        this.zzawg = zzgc.zzmn();
        this.zzawh = zzga.zzmm();
        this.zzawi = zzfu.zzmi();
        this.zzawj = null;
        this.zzcfc = null;
        this.zzcfm = -1;
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zzgb)) {
            return false;
        }
        final zzgb zzgb = (zzgb)o;
        final Long zzawe = this.zzawe;
        if (zzawe == null) {
            if (zzgb.zzawe != null) {
                return false;
            }
        }
        else if (!zzawe.equals(zzgb.zzawe)) {
            return false;
        }
        final String zzafx = this.zzafx;
        if (zzafx == null) {
            if (zzgb.zzafx != null) {
                return false;
            }
        }
        else if (!zzafx.equals(zzgb.zzafx)) {
            return false;
        }
        final Integer zzawf = this.zzawf;
        if (zzawf == null) {
            if (zzgb.zzawf != null) {
                return false;
            }
        }
        else if (!zzawf.equals(zzgb.zzawf)) {
            return false;
        }
        if (!zzze.equals(this.zzawg, zzgb.zzawg)) {
            return false;
        }
        if (!zzze.equals(this.zzawh, zzgb.zzawh)) {
            return false;
        }
        if (!zzze.equals(this.zzawi, zzgb.zzawi)) {
            return false;
        }
        final String zzawj = this.zzawj;
        if (zzawj == null) {
            if (zzgb.zzawj != null) {
                return false;
            }
        }
        else if (!zzawj.equals(zzgb.zzawj)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(zzgb.zzcfc);
        }
        return zzgb.zzcfc == null || zzgb.zzcfc.isEmpty();
    }
    
    @Override
    public final int hashCode() {
        final int hashCode = this.getClass().getName().hashCode();
        final Long zzawe = this.zzawe;
        final boolean b = false;
        int hashCode2;
        if (zzawe == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = zzawe.hashCode();
        }
        final String zzafx = this.zzafx;
        int hashCode3;
        if (zzafx == null) {
            hashCode3 = 0;
        }
        else {
            hashCode3 = zzafx.hashCode();
        }
        final Integer zzawf = this.zzawf;
        int hashCode4;
        if (zzawf == null) {
            hashCode4 = 0;
        }
        else {
            hashCode4 = zzawf.hashCode();
        }
        final int hashCode5 = zzze.hashCode(this.zzawg);
        final int hashCode6 = zzze.hashCode(this.zzawh);
        final int hashCode7 = zzze.hashCode(this.zzawi);
        final String zzawj = this.zzawj;
        int hashCode8;
        if (zzawj == null) {
            hashCode8 = 0;
        }
        else {
            hashCode8 = zzawj.hashCode();
        }
        int hashCode9 = b ? 1 : 0;
        if (this.zzcfc != null) {
            if (this.zzcfc.isEmpty()) {
                hashCode9 = (b ? 1 : 0);
            }
            else {
                hashCode9 = this.zzcfc.hashCode();
            }
        }
        return ((((((((hashCode + 527) * 31 + hashCode2) * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode5) * 31 + hashCode6) * 31 + hashCode7) * 31 + hashCode8) * 31 + hashCode9;
    }
    
    @Override
    public final void zza(final zzyy zzyy) throws IOException {
        final Long zzawe = this.zzawe;
        if (zzawe != null) {
            zzyy.zzi(1, zzawe);
        }
        final String zzafx = this.zzafx;
        if (zzafx != null) {
            zzyy.zzb(2, zzafx);
        }
        final Integer zzawf = this.zzawf;
        if (zzawf != null) {
            zzyy.zzd(3, zzawf);
        }
        final zzgc[] zzawg = this.zzawg;
        final int n = 0;
        if (zzawg != null && zzawg.length > 0) {
            int n2 = 0;
            while (true) {
                final zzgc[] zzawg2 = this.zzawg;
                if (n2 >= zzawg2.length) {
                    break;
                }
                final zzgc zzgc = zzawg2[n2];
                if (zzgc != null) {
                    zzyy.zza(4, zzgc);
                }
                ++n2;
            }
        }
        final zzga[] zzawh = this.zzawh;
        if (zzawh != null && zzawh.length > 0) {
            int n3 = 0;
            while (true) {
                final zzga[] zzawh2 = this.zzawh;
                if (n3 >= zzawh2.length) {
                    break;
                }
                final zzga zzga = zzawh2[n3];
                if (zzga != null) {
                    zzyy.zza(5, zzga);
                }
                ++n3;
            }
        }
        final zzfu[] zzawi = this.zzawi;
        if (zzawi != null && zzawi.length > 0) {
            int n4 = n;
            while (true) {
                final zzfu[] zzawi2 = this.zzawi;
                if (n4 >= zzawi2.length) {
                    break;
                }
                final zzfu zzfu = zzawi2[n4];
                if (zzfu != null) {
                    zzyy.zza(6, zzfu);
                }
                ++n4;
            }
        }
        final String zzawj = this.zzawj;
        if (zzawj != null) {
            zzyy.zzb(7, zzawj);
        }
        super.zza(zzyy);
    }
    
    @Override
    protected final int zzf() {
        final int zzf = super.zzf();
        final Long zzawe = this.zzawe;
        int n = zzf;
        if (zzawe != null) {
            n = zzf + zzyy.zzd(1, zzawe);
        }
        final String zzafx = this.zzafx;
        int n2 = n;
        if (zzafx != null) {
            n2 = n + zzyy.zzc(2, zzafx);
        }
        final Integer zzawf = this.zzawf;
        int n3 = n2;
        if (zzawf != null) {
            n3 = n2 + zzyy.zzh(3, zzawf);
        }
        final zzgc[] zzawg = this.zzawg;
        final int n4 = 0;
        int n5 = n3;
        if (zzawg != null) {
            n5 = n3;
            if (zzawg.length > 0) {
                int n6 = 0;
                while (true) {
                    final zzgc[] zzawg2 = this.zzawg;
                    n5 = n3;
                    if (n6 >= zzawg2.length) {
                        break;
                    }
                    final zzgc zzgc = zzawg2[n6];
                    int n7 = n3;
                    if (zzgc != null) {
                        n7 = n3 + zzyy.zzb(4, zzgc);
                    }
                    ++n6;
                    n3 = n7;
                }
            }
        }
        final zzga[] zzawh = this.zzawh;
        int n8 = n5;
        if (zzawh != null) {
            n8 = n5;
            if (zzawh.length > 0) {
                int n9 = 0;
                while (true) {
                    final zzga[] zzawh2 = this.zzawh;
                    n8 = n5;
                    if (n9 >= zzawh2.length) {
                        break;
                    }
                    final zzga zzga = zzawh2[n9];
                    int n10 = n5;
                    if (zzga != null) {
                        n10 = n5 + zzyy.zzb(5, zzga);
                    }
                    ++n9;
                    n5 = n10;
                }
            }
        }
        final zzfu[] zzawi = this.zzawi;
        int n11 = n8;
        if (zzawi != null) {
            n11 = n8;
            if (zzawi.length > 0) {
                int n12 = n4;
                while (true) {
                    final zzfu[] zzawi2 = this.zzawi;
                    n11 = n8;
                    if (n12 >= zzawi2.length) {
                        break;
                    }
                    final zzfu zzfu = zzawi2[n12];
                    int n13 = n8;
                    if (zzfu != null) {
                        n13 = n8 + zzyy.zzb(6, zzfu);
                    }
                    ++n12;
                    n8 = n13;
                }
            }
        }
        final String zzawj = this.zzawj;
        int n14 = n11;
        if (zzawj != null) {
            n14 = n11 + zzyy.zzc(7, zzawj);
        }
        return n14;
    }
}
