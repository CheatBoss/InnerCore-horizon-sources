package com.google.android.gms.internal.measurement;

import java.io.*;

public final class zzgd extends zzza<zzgd>
{
    private static volatile zzgd[] zzawl;
    public Integer zzauy;
    public zzgj zzawm;
    public zzgj zzawn;
    public Boolean zzawo;
    
    public zzgd() {
        this.zzauy = null;
        this.zzawm = null;
        this.zzawn = null;
        this.zzawo = null;
        this.zzcfc = null;
        this.zzcfm = -1;
    }
    
    public static zzgd[] zzmo() {
        if (zzgd.zzawl == null) {
            synchronized (zzze.zzcfl) {
                if (zzgd.zzawl == null) {
                    zzgd.zzawl = new zzgd[0];
                }
            }
        }
        return zzgd.zzawl;
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zzgd)) {
            return false;
        }
        final zzgd zzgd = (zzgd)o;
        final Integer zzauy = this.zzauy;
        if (zzauy == null) {
            if (zzgd.zzauy != null) {
                return false;
            }
        }
        else if (!zzauy.equals(zzgd.zzauy)) {
            return false;
        }
        final zzgj zzawm = this.zzawm;
        if (zzawm == null) {
            if (zzgd.zzawm != null) {
                return false;
            }
        }
        else if (!zzawm.equals(zzgd.zzawm)) {
            return false;
        }
        final zzgj zzawn = this.zzawn;
        if (zzawn == null) {
            if (zzgd.zzawn != null) {
                return false;
            }
        }
        else if (!zzawn.equals(zzgd.zzawn)) {
            return false;
        }
        final Boolean zzawo = this.zzawo;
        if (zzawo == null) {
            if (zzgd.zzawo != null) {
                return false;
            }
        }
        else if (!zzawo.equals(zzgd.zzawo)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(zzgd.zzcfc);
        }
        return zzgd.zzcfc == null || zzgd.zzcfc.isEmpty();
    }
    
    @Override
    public final int hashCode() {
        final int hashCode = this.getClass().getName().hashCode();
        final Integer zzauy = this.zzauy;
        final boolean b = false;
        int hashCode2;
        if (zzauy == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = zzauy.hashCode();
        }
        final zzgj zzawm = this.zzawm;
        int hashCode3;
        if (zzawm == null) {
            hashCode3 = 0;
        }
        else {
            hashCode3 = zzawm.hashCode();
        }
        final zzgj zzawn = this.zzawn;
        int hashCode4;
        if (zzawn == null) {
            hashCode4 = 0;
        }
        else {
            hashCode4 = zzawn.hashCode();
        }
        final Boolean zzawo = this.zzawo;
        int hashCode5;
        if (zzawo == null) {
            hashCode5 = 0;
        }
        else {
            hashCode5 = zzawo.hashCode();
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
        final Integer zzauy = this.zzauy;
        if (zzauy != null) {
            zzyy.zzd(1, zzauy);
        }
        final zzgj zzawm = this.zzawm;
        if (zzawm != null) {
            zzyy.zza(2, zzawm);
        }
        final zzgj zzawn = this.zzawn;
        if (zzawn != null) {
            zzyy.zza(3, zzawn);
        }
        final Boolean zzawo = this.zzawo;
        if (zzawo != null) {
            zzyy.zzb(4, zzawo);
        }
        super.zza(zzyy);
    }
    
    @Override
    protected final int zzf() {
        final int zzf = super.zzf();
        final Integer zzauy = this.zzauy;
        int n = zzf;
        if (zzauy != null) {
            n = zzf + zzyy.zzh(1, zzauy);
        }
        final zzgj zzawm = this.zzawm;
        int n2 = n;
        if (zzawm != null) {
            n2 = n + zzyy.zzb(2, zzawm);
        }
        final zzgj zzawn = this.zzawn;
        int n3 = n2;
        if (zzawn != null) {
            n3 = n2 + zzyy.zzb(3, zzawn);
        }
        final Boolean zzawo = this.zzawo;
        int n4 = n3;
        if (zzawo != null) {
            zzawo;
            n4 = n3 + (zzyy.zzbb(4) + 1);
        }
        return n4;
    }
}
