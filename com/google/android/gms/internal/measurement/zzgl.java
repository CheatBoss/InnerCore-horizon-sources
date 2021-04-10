package com.google.android.gms.internal.measurement;

import java.io.*;

public final class zzgl extends zzza<zzgl>
{
    private static volatile zzgl[] zzayk;
    public String name;
    public String zzamp;
    private Float zzaug;
    public Double zzauh;
    public Long zzawx;
    public Long zzayl;
    
    public zzgl() {
        this.zzayl = null;
        this.name = null;
        this.zzamp = null;
        this.zzawx = null;
        this.zzaug = null;
        this.zzauh = null;
        this.zzcfc = null;
        this.zzcfm = -1;
    }
    
    public static zzgl[] zzmu() {
        if (zzgl.zzayk == null) {
            synchronized (zzze.zzcfl) {
                if (zzgl.zzayk == null) {
                    zzgl.zzayk = new zzgl[0];
                }
            }
        }
        return zzgl.zzayk;
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zzgl)) {
            return false;
        }
        final zzgl zzgl = (zzgl)o;
        final Long zzayl = this.zzayl;
        if (zzayl == null) {
            if (zzgl.zzayl != null) {
                return false;
            }
        }
        else if (!zzayl.equals(zzgl.zzayl)) {
            return false;
        }
        final String name = this.name;
        if (name == null) {
            if (zzgl.name != null) {
                return false;
            }
        }
        else if (!name.equals(zzgl.name)) {
            return false;
        }
        final String zzamp = this.zzamp;
        if (zzamp == null) {
            if (zzgl.zzamp != null) {
                return false;
            }
        }
        else if (!zzamp.equals(zzgl.zzamp)) {
            return false;
        }
        final Long zzawx = this.zzawx;
        if (zzawx == null) {
            if (zzgl.zzawx != null) {
                return false;
            }
        }
        else if (!zzawx.equals(zzgl.zzawx)) {
            return false;
        }
        final Float zzaug = this.zzaug;
        if (zzaug == null) {
            if (zzgl.zzaug != null) {
                return false;
            }
        }
        else if (!zzaug.equals(zzgl.zzaug)) {
            return false;
        }
        final Double zzauh = this.zzauh;
        if (zzauh == null) {
            if (zzgl.zzauh != null) {
                return false;
            }
        }
        else if (!zzauh.equals(zzgl.zzauh)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(zzgl.zzcfc);
        }
        return zzgl.zzcfc == null || zzgl.zzcfc.isEmpty();
    }
    
    @Override
    public final int hashCode() {
        final int hashCode = this.getClass().getName().hashCode();
        final Long zzayl = this.zzayl;
        final boolean b = false;
        int hashCode2;
        if (zzayl == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = zzayl.hashCode();
        }
        final String name = this.name;
        int hashCode3;
        if (name == null) {
            hashCode3 = 0;
        }
        else {
            hashCode3 = name.hashCode();
        }
        final String zzamp = this.zzamp;
        int hashCode4;
        if (zzamp == null) {
            hashCode4 = 0;
        }
        else {
            hashCode4 = zzamp.hashCode();
        }
        final Long zzawx = this.zzawx;
        int hashCode5;
        if (zzawx == null) {
            hashCode5 = 0;
        }
        else {
            hashCode5 = zzawx.hashCode();
        }
        final Float zzaug = this.zzaug;
        int hashCode6;
        if (zzaug == null) {
            hashCode6 = 0;
        }
        else {
            hashCode6 = zzaug.hashCode();
        }
        final Double zzauh = this.zzauh;
        int hashCode7;
        if (zzauh == null) {
            hashCode7 = 0;
        }
        else {
            hashCode7 = zzauh.hashCode();
        }
        int hashCode8 = b ? 1 : 0;
        if (this.zzcfc != null) {
            if (this.zzcfc.isEmpty()) {
                hashCode8 = (b ? 1 : 0);
            }
            else {
                hashCode8 = this.zzcfc.hashCode();
            }
        }
        return (((((((hashCode + 527) * 31 + hashCode2) * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode5) * 31 + hashCode6) * 31 + hashCode7) * 31 + hashCode8;
    }
    
    @Override
    public final void zza(final zzyy zzyy) throws IOException {
        final Long zzayl = this.zzayl;
        if (zzayl != null) {
            zzyy.zzi(1, zzayl);
        }
        final String name = this.name;
        if (name != null) {
            zzyy.zzb(2, name);
        }
        final String zzamp = this.zzamp;
        if (zzamp != null) {
            zzyy.zzb(3, zzamp);
        }
        final Long zzawx = this.zzawx;
        if (zzawx != null) {
            zzyy.zzi(4, zzawx);
        }
        final Float zzaug = this.zzaug;
        if (zzaug != null) {
            zzyy.zza(5, zzaug);
        }
        final Double zzauh = this.zzauh;
        if (zzauh != null) {
            zzyy.zza(6, zzauh);
        }
        super.zza(zzyy);
    }
    
    @Override
    protected final int zzf() {
        final int zzf = super.zzf();
        final Long zzayl = this.zzayl;
        int n = zzf;
        if (zzayl != null) {
            n = zzf + zzyy.zzd(1, zzayl);
        }
        final String name = this.name;
        int n2 = n;
        if (name != null) {
            n2 = n + zzyy.zzc(2, name);
        }
        final String zzamp = this.zzamp;
        int n3 = n2;
        if (zzamp != null) {
            n3 = n2 + zzyy.zzc(3, zzamp);
        }
        final Long zzawx = this.zzawx;
        int n4 = n3;
        if (zzawx != null) {
            n4 = n3 + zzyy.zzd(4, zzawx);
        }
        final Float zzaug = this.zzaug;
        int n5 = n4;
        if (zzaug != null) {
            zzaug;
            n5 = n4 + (zzyy.zzbb(5) + 4);
        }
        final Double zzauh = this.zzauh;
        int n6 = n5;
        if (zzauh != null) {
            zzauh;
            n6 = n5 + (zzyy.zzbb(6) + 8);
        }
        return n6;
    }
}
