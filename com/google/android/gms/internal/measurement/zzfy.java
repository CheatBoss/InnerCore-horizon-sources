package com.google.android.gms.internal.measurement;

import java.io.*;

public final class zzfy extends zzza<zzfy>
{
    private static volatile zzfy[] zzavt;
    public Boolean zzavb;
    public Boolean zzavc;
    public Integer zzave;
    public String zzavu;
    public zzfw zzavv;
    
    public zzfy() {
        this.zzave = null;
        this.zzavu = null;
        this.zzavv = null;
        this.zzavb = null;
        this.zzavc = null;
        this.zzcfc = null;
        this.zzcfm = -1;
    }
    
    public static zzfy[] zzml() {
        if (zzfy.zzavt == null) {
            synchronized (zzze.zzcfl) {
                if (zzfy.zzavt == null) {
                    zzfy.zzavt = new zzfy[0];
                }
            }
        }
        return zzfy.zzavt;
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zzfy)) {
            return false;
        }
        final zzfy zzfy = (zzfy)o;
        final Integer zzave = this.zzave;
        if (zzave == null) {
            if (zzfy.zzave != null) {
                return false;
            }
        }
        else if (!zzave.equals(zzfy.zzave)) {
            return false;
        }
        final String zzavu = this.zzavu;
        if (zzavu == null) {
            if (zzfy.zzavu != null) {
                return false;
            }
        }
        else if (!zzavu.equals(zzfy.zzavu)) {
            return false;
        }
        final zzfw zzavv = this.zzavv;
        if (zzavv == null) {
            if (zzfy.zzavv != null) {
                return false;
            }
        }
        else if (!zzavv.equals(zzfy.zzavv)) {
            return false;
        }
        final Boolean zzavb = this.zzavb;
        if (zzavb == null) {
            if (zzfy.zzavb != null) {
                return false;
            }
        }
        else if (!zzavb.equals(zzfy.zzavb)) {
            return false;
        }
        final Boolean zzavc = this.zzavc;
        if (zzavc == null) {
            if (zzfy.zzavc != null) {
                return false;
            }
        }
        else if (!zzavc.equals(zzfy.zzavc)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(zzfy.zzcfc);
        }
        return zzfy.zzcfc == null || zzfy.zzcfc.isEmpty();
    }
    
    @Override
    public final int hashCode() {
        final int hashCode = this.getClass().getName().hashCode();
        final Integer zzave = this.zzave;
        final boolean b = false;
        int hashCode2;
        if (zzave == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = zzave.hashCode();
        }
        final String zzavu = this.zzavu;
        int hashCode3;
        if (zzavu == null) {
            hashCode3 = 0;
        }
        else {
            hashCode3 = zzavu.hashCode();
        }
        final zzfw zzavv = this.zzavv;
        int hashCode4;
        if (zzavv == null) {
            hashCode4 = 0;
        }
        else {
            hashCode4 = zzavv.hashCode();
        }
        final Boolean zzavb = this.zzavb;
        int hashCode5;
        if (zzavb == null) {
            hashCode5 = 0;
        }
        else {
            hashCode5 = zzavb.hashCode();
        }
        final Boolean zzavc = this.zzavc;
        int hashCode6;
        if (zzavc == null) {
            hashCode6 = 0;
        }
        else {
            hashCode6 = zzavc.hashCode();
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
        final Integer zzave = this.zzave;
        if (zzave != null) {
            zzyy.zzd(1, zzave);
        }
        final String zzavu = this.zzavu;
        if (zzavu != null) {
            zzyy.zzb(2, zzavu);
        }
        final zzfw zzavv = this.zzavv;
        if (zzavv != null) {
            zzyy.zza(3, zzavv);
        }
        final Boolean zzavb = this.zzavb;
        if (zzavb != null) {
            zzyy.zzb(4, zzavb);
        }
        final Boolean zzavc = this.zzavc;
        if (zzavc != null) {
            zzyy.zzb(5, zzavc);
        }
        super.zza(zzyy);
    }
    
    @Override
    protected final int zzf() {
        final int zzf = super.zzf();
        final Integer zzave = this.zzave;
        int n = zzf;
        if (zzave != null) {
            n = zzf + zzyy.zzh(1, zzave);
        }
        final String zzavu = this.zzavu;
        int n2 = n;
        if (zzavu != null) {
            n2 = n + zzyy.zzc(2, zzavu);
        }
        final zzfw zzavv = this.zzavv;
        int n3 = n2;
        if (zzavv != null) {
            n3 = n2 + zzyy.zzb(3, zzavv);
        }
        final Boolean zzavb = this.zzavb;
        int n4 = n3;
        if (zzavb != null) {
            zzavb;
            n4 = n3 + (zzyy.zzbb(4) + 1);
        }
        final Boolean zzavc = this.zzavc;
        int n5 = n4;
        if (zzavc != null) {
            zzavc;
            n5 = n4 + (zzyy.zzbb(5) + 1);
        }
        return n5;
    }
}
