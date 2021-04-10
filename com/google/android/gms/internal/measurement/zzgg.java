package com.google.android.gms.internal.measurement;

import java.io.*;

public final class zzgg extends zzza<zzgg>
{
    private static volatile zzgg[] zzaww;
    public String name;
    public String zzamp;
    private Float zzaug;
    public Double zzauh;
    public Long zzawx;
    
    public zzgg() {
        this.name = null;
        this.zzamp = null;
        this.zzawx = null;
        this.zzaug = null;
        this.zzauh = null;
        this.zzcfc = null;
        this.zzcfm = -1;
    }
    
    public static zzgg[] zzmr() {
        if (zzgg.zzaww == null) {
            synchronized (zzze.zzcfl) {
                if (zzgg.zzaww == null) {
                    zzgg.zzaww = new zzgg[0];
                }
            }
        }
        return zzgg.zzaww;
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zzgg)) {
            return false;
        }
        final zzgg zzgg = (zzgg)o;
        final String name = this.name;
        if (name == null) {
            if (zzgg.name != null) {
                return false;
            }
        }
        else if (!name.equals(zzgg.name)) {
            return false;
        }
        final String zzamp = this.zzamp;
        if (zzamp == null) {
            if (zzgg.zzamp != null) {
                return false;
            }
        }
        else if (!zzamp.equals(zzgg.zzamp)) {
            return false;
        }
        final Long zzawx = this.zzawx;
        if (zzawx == null) {
            if (zzgg.zzawx != null) {
                return false;
            }
        }
        else if (!zzawx.equals(zzgg.zzawx)) {
            return false;
        }
        final Float zzaug = this.zzaug;
        if (zzaug == null) {
            if (zzgg.zzaug != null) {
                return false;
            }
        }
        else if (!zzaug.equals(zzgg.zzaug)) {
            return false;
        }
        final Double zzauh = this.zzauh;
        if (zzauh == null) {
            if (zzgg.zzauh != null) {
                return false;
            }
        }
        else if (!zzauh.equals(zzgg.zzauh)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(zzgg.zzcfc);
        }
        return zzgg.zzcfc == null || zzgg.zzcfc.isEmpty();
    }
    
    @Override
    public final int hashCode() {
        final int hashCode = this.getClass().getName().hashCode();
        final String name = this.name;
        final boolean b = false;
        int hashCode2;
        if (name == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = name.hashCode();
        }
        final String zzamp = this.zzamp;
        int hashCode3;
        if (zzamp == null) {
            hashCode3 = 0;
        }
        else {
            hashCode3 = zzamp.hashCode();
        }
        final Long zzawx = this.zzawx;
        int hashCode4;
        if (zzawx == null) {
            hashCode4 = 0;
        }
        else {
            hashCode4 = zzawx.hashCode();
        }
        final Float zzaug = this.zzaug;
        int hashCode5;
        if (zzaug == null) {
            hashCode5 = 0;
        }
        else {
            hashCode5 = zzaug.hashCode();
        }
        final Double zzauh = this.zzauh;
        int hashCode6;
        if (zzauh == null) {
            hashCode6 = 0;
        }
        else {
            hashCode6 = zzauh.hashCode();
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
        final String name = this.name;
        if (name != null) {
            zzyy.zzb(1, name);
        }
        final String zzamp = this.zzamp;
        if (zzamp != null) {
            zzyy.zzb(2, zzamp);
        }
        final Long zzawx = this.zzawx;
        if (zzawx != null) {
            zzyy.zzi(3, zzawx);
        }
        final Float zzaug = this.zzaug;
        if (zzaug != null) {
            zzyy.zza(4, zzaug);
        }
        final Double zzauh = this.zzauh;
        if (zzauh != null) {
            zzyy.zza(5, zzauh);
        }
        super.zza(zzyy);
    }
    
    @Override
    protected final int zzf() {
        final int zzf = super.zzf();
        final String name = this.name;
        int n = zzf;
        if (name != null) {
            n = zzf + zzyy.zzc(1, name);
        }
        final String zzamp = this.zzamp;
        int n2 = n;
        if (zzamp != null) {
            n2 = n + zzyy.zzc(2, zzamp);
        }
        final Long zzawx = this.zzawx;
        int n3 = n2;
        if (zzawx != null) {
            n3 = n2 + zzyy.zzd(3, zzawx);
        }
        final Float zzaug = this.zzaug;
        int n4 = n3;
        if (zzaug != null) {
            zzaug;
            n4 = n3 + (zzyy.zzbb(4) + 4);
        }
        final Double zzauh = this.zzauh;
        int n5 = n4;
        if (zzauh != null) {
            zzauh;
            n5 = n4 + (zzyy.zzbb(5) + 8);
        }
        return n5;
    }
}
