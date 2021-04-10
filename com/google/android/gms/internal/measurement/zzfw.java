package com.google.android.gms.internal.measurement;

import java.io.*;

public final class zzfw extends zzza<zzfw>
{
    private static volatile zzfw[] zzavj;
    public zzfz zzavk;
    public zzfx zzavl;
    public Boolean zzavm;
    public String zzavn;
    
    public zzfw() {
        this.zzavk = null;
        this.zzavl = null;
        this.zzavm = null;
        this.zzavn = null;
        this.zzcfc = null;
        this.zzcfm = -1;
    }
    
    public static zzfw[] zzmk() {
        if (zzfw.zzavj == null) {
            synchronized (zzze.zzcfl) {
                if (zzfw.zzavj == null) {
                    zzfw.zzavj = new zzfw[0];
                }
            }
        }
        return zzfw.zzavj;
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zzfw)) {
            return false;
        }
        final zzfw zzfw = (zzfw)o;
        final zzfz zzavk = this.zzavk;
        if (zzavk == null) {
            if (zzfw.zzavk != null) {
                return false;
            }
        }
        else if (!zzavk.equals(zzfw.zzavk)) {
            return false;
        }
        final zzfx zzavl = this.zzavl;
        if (zzavl == null) {
            if (zzfw.zzavl != null) {
                return false;
            }
        }
        else if (!zzavl.equals(zzfw.zzavl)) {
            return false;
        }
        final Boolean zzavm = this.zzavm;
        if (zzavm == null) {
            if (zzfw.zzavm != null) {
                return false;
            }
        }
        else if (!zzavm.equals(zzfw.zzavm)) {
            return false;
        }
        final String zzavn = this.zzavn;
        if (zzavn == null) {
            if (zzfw.zzavn != null) {
                return false;
            }
        }
        else if (!zzavn.equals(zzfw.zzavn)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(zzfw.zzcfc);
        }
        return zzfw.zzcfc == null || zzfw.zzcfc.isEmpty();
    }
    
    @Override
    public final int hashCode() {
        final int hashCode = this.getClass().getName().hashCode();
        final zzfz zzavk = this.zzavk;
        final boolean b = false;
        int hashCode2;
        if (zzavk == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = zzavk.hashCode();
        }
        final zzfx zzavl = this.zzavl;
        int hashCode3;
        if (zzavl == null) {
            hashCode3 = 0;
        }
        else {
            hashCode3 = zzavl.hashCode();
        }
        final Boolean zzavm = this.zzavm;
        int hashCode4;
        if (zzavm == null) {
            hashCode4 = 0;
        }
        else {
            hashCode4 = zzavm.hashCode();
        }
        final String zzavn = this.zzavn;
        int hashCode5;
        if (zzavn == null) {
            hashCode5 = 0;
        }
        else {
            hashCode5 = zzavn.hashCode();
        }
        int hashCode6 = b ? 1 : 0;
        if (this.zzcfc != null) {
            if (this.zzcfc.isEmpty()) {
                hashCode6 = (b ? 1 : 0);
            }
            else {
                hashCode6 = this.zzcfc.hashCode();
            }
        }
        return (((((hashCode + 527) * 31 + hashCode2) * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode5) * 31 + hashCode6;
    }
    
    @Override
    public final void zza(final zzyy zzyy) throws IOException {
        final zzfz zzavk = this.zzavk;
        if (zzavk != null) {
            zzyy.zza(1, zzavk);
        }
        final zzfx zzavl = this.zzavl;
        if (zzavl != null) {
            zzyy.zza(2, zzavl);
        }
        final Boolean zzavm = this.zzavm;
        if (zzavm != null) {
            zzyy.zzb(3, zzavm);
        }
        final String zzavn = this.zzavn;
        if (zzavn != null) {
            zzyy.zzb(4, zzavn);
        }
        super.zza(zzyy);
    }
    
    @Override
    protected final int zzf() {
        final int zzf = super.zzf();
        final zzfz zzavk = this.zzavk;
        int n = zzf;
        if (zzavk != null) {
            n = zzf + zzyy.zzb(1, zzavk);
        }
        final zzfx zzavl = this.zzavl;
        int n2 = n;
        if (zzavl != null) {
            n2 = n + zzyy.zzb(2, zzavl);
        }
        final Boolean zzavm = this.zzavm;
        int n3 = n2;
        if (zzavm != null) {
            zzavm;
            n3 = n2 + (zzyy.zzbb(3) + 1);
        }
        final String zzavn = this.zzavn;
        int n4 = n3;
        if (zzavn != null) {
            n4 = n3 + zzyy.zzc(4, zzavn);
        }
        return n4;
    }
}
