package com.google.android.gms.internal.measurement;

import java.io.*;

public final class zzfv extends zzza<zzfv>
{
    private static volatile zzfv[] zzavd;
    public Boolean zzavb;
    public Boolean zzavc;
    public Integer zzave;
    public String zzavf;
    public zzfw[] zzavg;
    private Boolean zzavh;
    public zzfx zzavi;
    
    public zzfv() {
        this.zzave = null;
        this.zzavf = null;
        this.zzavg = zzfw.zzmk();
        this.zzavh = null;
        this.zzavi = null;
        this.zzavb = null;
        this.zzavc = null;
        this.zzcfc = null;
        this.zzcfm = -1;
    }
    
    public static zzfv[] zzmj() {
        if (zzfv.zzavd == null) {
            synchronized (zzze.zzcfl) {
                if (zzfv.zzavd == null) {
                    zzfv.zzavd = new zzfv[0];
                }
            }
        }
        return zzfv.zzavd;
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zzfv)) {
            return false;
        }
        final zzfv zzfv = (zzfv)o;
        final Integer zzave = this.zzave;
        if (zzave == null) {
            if (zzfv.zzave != null) {
                return false;
            }
        }
        else if (!zzave.equals(zzfv.zzave)) {
            return false;
        }
        final String zzavf = this.zzavf;
        if (zzavf == null) {
            if (zzfv.zzavf != null) {
                return false;
            }
        }
        else if (!zzavf.equals(zzfv.zzavf)) {
            return false;
        }
        if (!zzze.equals(this.zzavg, zzfv.zzavg)) {
            return false;
        }
        final Boolean zzavh = this.zzavh;
        if (zzavh == null) {
            if (zzfv.zzavh != null) {
                return false;
            }
        }
        else if (!zzavh.equals(zzfv.zzavh)) {
            return false;
        }
        final zzfx zzavi = this.zzavi;
        if (zzavi == null) {
            if (zzfv.zzavi != null) {
                return false;
            }
        }
        else if (!zzavi.equals(zzfv.zzavi)) {
            return false;
        }
        final Boolean zzavb = this.zzavb;
        if (zzavb == null) {
            if (zzfv.zzavb != null) {
                return false;
            }
        }
        else if (!zzavb.equals(zzfv.zzavb)) {
            return false;
        }
        final Boolean zzavc = this.zzavc;
        if (zzavc == null) {
            if (zzfv.zzavc != null) {
                return false;
            }
        }
        else if (!zzavc.equals(zzfv.zzavc)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(zzfv.zzcfc);
        }
        return zzfv.zzcfc == null || zzfv.zzcfc.isEmpty();
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
        final String zzavf = this.zzavf;
        int hashCode3;
        if (zzavf == null) {
            hashCode3 = 0;
        }
        else {
            hashCode3 = zzavf.hashCode();
        }
        final int hashCode4 = zzze.hashCode(this.zzavg);
        final Boolean zzavh = this.zzavh;
        int hashCode5;
        if (zzavh == null) {
            hashCode5 = 0;
        }
        else {
            hashCode5 = zzavh.hashCode();
        }
        final zzfx zzavi = this.zzavi;
        int hashCode6;
        if (zzavi == null) {
            hashCode6 = 0;
        }
        else {
            hashCode6 = zzavi.hashCode();
        }
        final Boolean zzavb = this.zzavb;
        int hashCode7;
        if (zzavb == null) {
            hashCode7 = 0;
        }
        else {
            hashCode7 = zzavb.hashCode();
        }
        final Boolean zzavc = this.zzavc;
        int hashCode8;
        if (zzavc == null) {
            hashCode8 = 0;
        }
        else {
            hashCode8 = zzavc.hashCode();
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
        final Integer zzave = this.zzave;
        if (zzave != null) {
            zzyy.zzd(1, zzave);
        }
        final String zzavf = this.zzavf;
        if (zzavf != null) {
            zzyy.zzb(2, zzavf);
        }
        final zzfw[] zzavg = this.zzavg;
        if (zzavg != null && zzavg.length > 0) {
            int n = 0;
            while (true) {
                final zzfw[] zzavg2 = this.zzavg;
                if (n >= zzavg2.length) {
                    break;
                }
                final zzfw zzfw = zzavg2[n];
                if (zzfw != null) {
                    zzyy.zza(3, zzfw);
                }
                ++n;
            }
        }
        final Boolean zzavh = this.zzavh;
        if (zzavh != null) {
            zzyy.zzb(4, zzavh);
        }
        final zzfx zzavi = this.zzavi;
        if (zzavi != null) {
            zzyy.zza(5, zzavi);
        }
        final Boolean zzavb = this.zzavb;
        if (zzavb != null) {
            zzyy.zzb(6, zzavb);
        }
        final Boolean zzavc = this.zzavc;
        if (zzavc != null) {
            zzyy.zzb(7, zzavc);
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
        final String zzavf = this.zzavf;
        int n2 = n;
        if (zzavf != null) {
            n2 = n + zzyy.zzc(2, zzavf);
        }
        final zzfw[] zzavg = this.zzavg;
        int n3 = n2;
        if (zzavg != null) {
            n3 = n2;
            if (zzavg.length > 0) {
                int n4 = 0;
                while (true) {
                    final zzfw[] zzavg2 = this.zzavg;
                    n3 = n2;
                    if (n4 >= zzavg2.length) {
                        break;
                    }
                    final zzfw zzfw = zzavg2[n4];
                    int n5 = n2;
                    if (zzfw != null) {
                        n5 = n2 + zzyy.zzb(3, zzfw);
                    }
                    ++n4;
                    n2 = n5;
                }
            }
        }
        final Boolean zzavh = this.zzavh;
        int n6 = n3;
        if (zzavh != null) {
            zzavh;
            n6 = n3 + (zzyy.zzbb(4) + 1);
        }
        final zzfx zzavi = this.zzavi;
        int n7 = n6;
        if (zzavi != null) {
            n7 = n6 + zzyy.zzb(5, zzavi);
        }
        final Boolean zzavb = this.zzavb;
        int n8 = n7;
        if (zzavb != null) {
            zzavb;
            n8 = n7 + (zzyy.zzbb(6) + 1);
        }
        final Boolean zzavc = this.zzavc;
        int n9 = n8;
        if (zzavc != null) {
            zzavc;
            n9 = n8 + (zzyy.zzbb(7) + 1);
        }
        return n9;
    }
}
