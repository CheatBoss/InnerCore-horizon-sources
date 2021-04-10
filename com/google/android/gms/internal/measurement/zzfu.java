package com.google.android.gms.internal.measurement;

import java.io.*;

public final class zzfu extends zzza<zzfu>
{
    private static volatile zzfu[] zzaux;
    public Integer zzauy;
    public zzfy[] zzauz;
    public zzfv[] zzava;
    private Boolean zzavb;
    private Boolean zzavc;
    
    public zzfu() {
        this.zzauy = null;
        this.zzauz = zzfy.zzml();
        this.zzava = zzfv.zzmj();
        this.zzavb = null;
        this.zzavc = null;
        this.zzcfc = null;
        this.zzcfm = -1;
    }
    
    public static zzfu[] zzmi() {
        if (zzfu.zzaux == null) {
            synchronized (zzze.zzcfl) {
                if (zzfu.zzaux == null) {
                    zzfu.zzaux = new zzfu[0];
                }
            }
        }
        return zzfu.zzaux;
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zzfu)) {
            return false;
        }
        final zzfu zzfu = (zzfu)o;
        final Integer zzauy = this.zzauy;
        if (zzauy == null) {
            if (zzfu.zzauy != null) {
                return false;
            }
        }
        else if (!zzauy.equals(zzfu.zzauy)) {
            return false;
        }
        if (!zzze.equals(this.zzauz, zzfu.zzauz)) {
            return false;
        }
        if (!zzze.equals(this.zzava, zzfu.zzava)) {
            return false;
        }
        final Boolean zzavb = this.zzavb;
        if (zzavb == null) {
            if (zzfu.zzavb != null) {
                return false;
            }
        }
        else if (!zzavb.equals(zzfu.zzavb)) {
            return false;
        }
        final Boolean zzavc = this.zzavc;
        if (zzavc == null) {
            if (zzfu.zzavc != null) {
                return false;
            }
        }
        else if (!zzavc.equals(zzfu.zzavc)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(zzfu.zzcfc);
        }
        return zzfu.zzcfc == null || zzfu.zzcfc.isEmpty();
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
        final int hashCode3 = zzze.hashCode(this.zzauz);
        final int hashCode4 = zzze.hashCode(this.zzava);
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
        final Integer zzauy = this.zzauy;
        if (zzauy != null) {
            zzyy.zzd(1, zzauy);
        }
        final zzfy[] zzauz = this.zzauz;
        final int n = 0;
        if (zzauz != null && zzauz.length > 0) {
            int n2 = 0;
            while (true) {
                final zzfy[] zzauz2 = this.zzauz;
                if (n2 >= zzauz2.length) {
                    break;
                }
                final zzfy zzfy = zzauz2[n2];
                if (zzfy != null) {
                    zzyy.zza(2, zzfy);
                }
                ++n2;
            }
        }
        final zzfv[] zzava = this.zzava;
        if (zzava != null && zzava.length > 0) {
            int n3 = n;
            while (true) {
                final zzfv[] zzava2 = this.zzava;
                if (n3 >= zzava2.length) {
                    break;
                }
                final zzfv zzfv = zzava2[n3];
                if (zzfv != null) {
                    zzyy.zza(3, zzfv);
                }
                ++n3;
            }
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
        final Integer zzauy = this.zzauy;
        int n = zzf;
        if (zzauy != null) {
            n = zzf + zzyy.zzh(1, zzauy);
        }
        final zzfy[] zzauz = this.zzauz;
        final int n2 = 0;
        int n3 = n;
        if (zzauz != null) {
            n3 = n;
            if (zzauz.length > 0) {
                int n4 = 0;
                while (true) {
                    final zzfy[] zzauz2 = this.zzauz;
                    n3 = n;
                    if (n4 >= zzauz2.length) {
                        break;
                    }
                    final zzfy zzfy = zzauz2[n4];
                    int n5 = n;
                    if (zzfy != null) {
                        n5 = n + zzyy.zzb(2, zzfy);
                    }
                    ++n4;
                    n = n5;
                }
            }
        }
        final zzfv[] zzava = this.zzava;
        int n6 = n3;
        if (zzava != null) {
            n6 = n3;
            if (zzava.length > 0) {
                int n7 = n2;
                while (true) {
                    final zzfv[] zzava2 = this.zzava;
                    n6 = n3;
                    if (n7 >= zzava2.length) {
                        break;
                    }
                    final zzfv zzfv = zzava2[n7];
                    int n8 = n3;
                    if (zzfv != null) {
                        n8 = n3 + zzyy.zzb(3, zzfv);
                    }
                    ++n7;
                    n3 = n8;
                }
            }
        }
        final Boolean zzavb = this.zzavb;
        int n9 = n6;
        if (zzavb != null) {
            zzavb;
            n9 = n6 + (zzyy.zzbb(4) + 1);
        }
        final Boolean zzavc = this.zzavc;
        int n10 = n9;
        if (zzavc != null) {
            zzavc;
            n10 = n9 + (zzyy.zzbb(5) + 1);
        }
        return n10;
    }
}
