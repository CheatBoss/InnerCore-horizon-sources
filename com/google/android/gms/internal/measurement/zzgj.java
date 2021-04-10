package com.google.android.gms.internal.measurement;

import java.io.*;

public final class zzgj extends zzza<zzgj>
{
    public long[] zzaye;
    public long[] zzayf;
    public zzge[] zzayg;
    public zzgk[] zzayh;
    
    public zzgj() {
        this.zzaye = zzzj.zzcfr;
        this.zzayf = zzzj.zzcfr;
        this.zzayg = zzge.zzmp();
        this.zzayh = zzgk.zzmt();
        this.zzcfc = null;
        this.zzcfm = -1;
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zzgj)) {
            return false;
        }
        final zzgj zzgj = (zzgj)o;
        if (!zzze.equals(this.zzaye, zzgj.zzaye)) {
            return false;
        }
        if (!zzze.equals(this.zzayf, zzgj.zzayf)) {
            return false;
        }
        if (!zzze.equals(this.zzayg, zzgj.zzayg)) {
            return false;
        }
        if (!zzze.equals(this.zzayh, zzgj.zzayh)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(zzgj.zzcfc);
        }
        return zzgj.zzcfc == null || zzgj.zzcfc.isEmpty();
    }
    
    @Override
    public final int hashCode() {
        final int hashCode = this.getClass().getName().hashCode();
        final int hashCode2 = zzze.hashCode(this.zzaye);
        final int hashCode3 = zzze.hashCode(this.zzayf);
        final int hashCode4 = zzze.hashCode(this.zzayg);
        final int hashCode5 = zzze.hashCode(this.zzayh);
        int hashCode6;
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            hashCode6 = this.zzcfc.hashCode();
        }
        else {
            hashCode6 = 0;
        }
        return (((((hashCode + 527) * 31 + hashCode2) * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode5) * 31 + hashCode6;
    }
    
    @Override
    public final void zza(final zzyy zzyy) throws IOException {
        final long[] zzaye = this.zzaye;
        final int n = 0;
        if (zzaye != null && zzaye.length > 0) {
            int n2 = 0;
            while (true) {
                final long[] zzaye2 = this.zzaye;
                if (n2 >= zzaye2.length) {
                    break;
                }
                zzyy.zza(1, zzaye2[n2]);
                ++n2;
            }
        }
        final long[] zzayf = this.zzayf;
        if (zzayf != null && zzayf.length > 0) {
            int n3 = 0;
            while (true) {
                final long[] zzayf2 = this.zzayf;
                if (n3 >= zzayf2.length) {
                    break;
                }
                zzyy.zza(2, zzayf2[n3]);
                ++n3;
            }
        }
        final zzge[] zzayg = this.zzayg;
        if (zzayg != null && zzayg.length > 0) {
            int n4 = 0;
            while (true) {
                final zzge[] zzayg2 = this.zzayg;
                if (n4 >= zzayg2.length) {
                    break;
                }
                final zzge zzge = zzayg2[n4];
                if (zzge != null) {
                    zzyy.zza(3, zzge);
                }
                ++n4;
            }
        }
        final zzgk[] zzayh = this.zzayh;
        if (zzayh != null && zzayh.length > 0) {
            int n5 = n;
            while (true) {
                final zzgk[] zzayh2 = this.zzayh;
                if (n5 >= zzayh2.length) {
                    break;
                }
                final zzgk zzgk = zzayh2[n5];
                if (zzgk != null) {
                    zzyy.zza(4, zzgk);
                }
                ++n5;
            }
        }
        super.zza(zzyy);
    }
    
    @Override
    protected final int zzf() {
        final int zzf = super.zzf();
        final long[] zzaye = this.zzaye;
        final int n = 0;
        int n2 = zzf;
        if (zzaye != null) {
            n2 = zzf;
            if (zzaye.length > 0) {
                int n3 = 0;
                int n4 = 0;
                long[] zzaye2;
                while (true) {
                    zzaye2 = this.zzaye;
                    if (n3 >= zzaye2.length) {
                        break;
                    }
                    n4 += zzyy.zzbi(zzaye2[n3]);
                    ++n3;
                }
                n2 = zzf + n4 + zzaye2.length * 1;
            }
        }
        final long[] zzayf = this.zzayf;
        int n5 = n2;
        if (zzayf != null) {
            n5 = n2;
            if (zzayf.length > 0) {
                int n6 = 0;
                int n7 = 0;
                long[] zzayf2;
                while (true) {
                    zzayf2 = this.zzayf;
                    if (n6 >= zzayf2.length) {
                        break;
                    }
                    n7 += zzyy.zzbi(zzayf2[n6]);
                    ++n6;
                }
                n5 = n2 + n7 + zzayf2.length * 1;
            }
        }
        final zzge[] zzayg = this.zzayg;
        int n8 = n5;
        if (zzayg != null) {
            n8 = n5;
            if (zzayg.length > 0) {
                int n9 = 0;
                while (true) {
                    final zzge[] zzayg2 = this.zzayg;
                    n8 = n5;
                    if (n9 >= zzayg2.length) {
                        break;
                    }
                    final zzge zzge = zzayg2[n9];
                    int n10 = n5;
                    if (zzge != null) {
                        n10 = n5 + zzyy.zzb(3, zzge);
                    }
                    ++n9;
                    n5 = n10;
                }
            }
        }
        final zzgk[] zzayh = this.zzayh;
        int n11 = n8;
        if (zzayh != null) {
            n11 = n8;
            if (zzayh.length > 0) {
                int n12 = n;
                while (true) {
                    final zzgk[] zzayh2 = this.zzayh;
                    n11 = n8;
                    if (n12 >= zzayh2.length) {
                        break;
                    }
                    final zzgk zzgk = zzayh2[n12];
                    int n13 = n8;
                    if (zzgk != null) {
                        n13 = n8 + zzyy.zzb(4, zzgk);
                    }
                    ++n12;
                    n8 = n13;
                }
            }
        }
        return n11;
    }
}
