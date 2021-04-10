package com.google.android.gms.internal.measurement;

import java.io.*;

public final class zzga extends zzza<zzga>
{
    private static volatile zzga[] zzawa;
    public String name;
    public Boolean zzawb;
    public Boolean zzawc;
    public Integer zzawd;
    
    public zzga() {
        this.name = null;
        this.zzawb = null;
        this.zzawc = null;
        this.zzawd = null;
        this.zzcfc = null;
        this.zzcfm = -1;
    }
    
    public static zzga[] zzmm() {
        if (zzga.zzawa == null) {
            synchronized (zzze.zzcfl) {
                if (zzga.zzawa == null) {
                    zzga.zzawa = new zzga[0];
                }
            }
        }
        return zzga.zzawa;
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zzga)) {
            return false;
        }
        final zzga zzga = (zzga)o;
        final String name = this.name;
        if (name == null) {
            if (zzga.name != null) {
                return false;
            }
        }
        else if (!name.equals(zzga.name)) {
            return false;
        }
        final Boolean zzawb = this.zzawb;
        if (zzawb == null) {
            if (zzga.zzawb != null) {
                return false;
            }
        }
        else if (!zzawb.equals(zzga.zzawb)) {
            return false;
        }
        final Boolean zzawc = this.zzawc;
        if (zzawc == null) {
            if (zzga.zzawc != null) {
                return false;
            }
        }
        else if (!zzawc.equals(zzga.zzawc)) {
            return false;
        }
        final Integer zzawd = this.zzawd;
        if (zzawd == null) {
            if (zzga.zzawd != null) {
                return false;
            }
        }
        else if (!zzawd.equals(zzga.zzawd)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(zzga.zzcfc);
        }
        return zzga.zzcfc == null || zzga.zzcfc.isEmpty();
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
        final Boolean zzawb = this.zzawb;
        int hashCode3;
        if (zzawb == null) {
            hashCode3 = 0;
        }
        else {
            hashCode3 = zzawb.hashCode();
        }
        final Boolean zzawc = this.zzawc;
        int hashCode4;
        if (zzawc == null) {
            hashCode4 = 0;
        }
        else {
            hashCode4 = zzawc.hashCode();
        }
        final Integer zzawd = this.zzawd;
        int hashCode5;
        if (zzawd == null) {
            hashCode5 = 0;
        }
        else {
            hashCode5 = zzawd.hashCode();
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
        final String name = this.name;
        if (name != null) {
            zzyy.zzb(1, name);
        }
        final Boolean zzawb = this.zzawb;
        if (zzawb != null) {
            zzyy.zzb(2, zzawb);
        }
        final Boolean zzawc = this.zzawc;
        if (zzawc != null) {
            zzyy.zzb(3, zzawc);
        }
        final Integer zzawd = this.zzawd;
        if (zzawd != null) {
            zzyy.zzd(4, zzawd);
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
        final Boolean zzawb = this.zzawb;
        int n2 = n;
        if (zzawb != null) {
            zzawb;
            n2 = n + (zzyy.zzbb(2) + 1);
        }
        final Boolean zzawc = this.zzawc;
        int n3 = n2;
        if (zzawc != null) {
            zzawc;
            n3 = n2 + (zzyy.zzbb(3) + 1);
        }
        final Integer zzawd = this.zzawd;
        int n4 = n3;
        if (zzawd != null) {
            n4 = n3 + zzyy.zzh(4, zzawd);
        }
        return n4;
    }
}
