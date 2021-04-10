package com.google.android.gms.internal.measurement;

import java.io.*;

public final class zzfz extends zzza<zzfz>
{
    public Integer zzavw;
    public String zzavx;
    public Boolean zzavy;
    public String[] zzavz;
    
    public zzfz() {
        this.zzavw = null;
        this.zzavx = null;
        this.zzavy = null;
        this.zzavz = zzzj.zzcfv;
        this.zzcfc = null;
        this.zzcfm = -1;
    }
    
    private final zzfz zzd(final zzyx zzyx) throws IOException {
        while (true) {
            final int zzug = zzyx.zzug();
            if (zzug == 0) {
                break;
            }
            if (zzug == 8) {
                final int position = zzyx.getPosition();
                try {
                    final int zzuy = zzyx.zzuy();
                    if (zzuy >= 0 && zzuy <= 6) {
                        this.zzavw = zzuy;
                        continue;
                    }
                    final StringBuilder sb = new StringBuilder(41);
                    sb.append(zzuy);
                    sb.append(" is not a valid enum MatchType");
                    throw new IllegalArgumentException(sb.toString());
                }
                catch (IllegalArgumentException ex) {
                    zzyx.zzby(position);
                    this.zza(zzyx, zzug);
                    continue;
                }
                break;
            }
            if (zzug != 18) {
                if (zzug != 24) {
                    if (zzug != 34) {
                        if (!super.zza(zzyx, zzug)) {
                            return this;
                        }
                        continue;
                    }
                    else {
                        final int zzb = zzzj.zzb(zzyx, 34);
                        final String[] zzavz = this.zzavz;
                        int length;
                        if (zzavz == null) {
                            length = 0;
                        }
                        else {
                            length = zzavz.length;
                        }
                        final int n = zzb + length;
                        final String[] zzavz2 = new String[n];
                        int i = length;
                        if (length != 0) {
                            System.arraycopy(this.zzavz, 0, zzavz2, 0, length);
                            i = length;
                        }
                        while (i < n - 1) {
                            zzavz2[i] = zzyx.readString();
                            zzyx.zzug();
                            ++i;
                        }
                        zzavz2[i] = zzyx.readString();
                        this.zzavz = zzavz2;
                    }
                }
                else {
                    this.zzavy = zzyx.zzum();
                }
            }
            else {
                this.zzavx = zzyx.readString();
            }
        }
        return this;
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zzfz)) {
            return false;
        }
        final zzfz zzfz = (zzfz)o;
        final Integer zzavw = this.zzavw;
        if (zzavw == null) {
            if (zzfz.zzavw != null) {
                return false;
            }
        }
        else if (!zzavw.equals(zzfz.zzavw)) {
            return false;
        }
        final String zzavx = this.zzavx;
        if (zzavx == null) {
            if (zzfz.zzavx != null) {
                return false;
            }
        }
        else if (!zzavx.equals(zzfz.zzavx)) {
            return false;
        }
        final Boolean zzavy = this.zzavy;
        if (zzavy == null) {
            if (zzfz.zzavy != null) {
                return false;
            }
        }
        else if (!zzavy.equals(zzfz.zzavy)) {
            return false;
        }
        if (!zzze.equals(this.zzavz, zzfz.zzavz)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(zzfz.zzcfc);
        }
        return zzfz.zzcfc == null || zzfz.zzcfc.isEmpty();
    }
    
    @Override
    public final int hashCode() {
        final int hashCode = this.getClass().getName().hashCode();
        final Integer zzavw = this.zzavw;
        final boolean b = false;
        int intValue;
        if (zzavw == null) {
            intValue = 0;
        }
        else {
            intValue = zzavw;
        }
        final String zzavx = this.zzavx;
        int hashCode2;
        if (zzavx == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = zzavx.hashCode();
        }
        final Boolean zzavy = this.zzavy;
        int hashCode3;
        if (zzavy == null) {
            hashCode3 = 0;
        }
        else {
            hashCode3 = zzavy.hashCode();
        }
        final int hashCode4 = zzze.hashCode(this.zzavz);
        int hashCode5 = b ? 1 : 0;
        if (this.zzcfc != null) {
            if (this.zzcfc.isEmpty()) {
                hashCode5 = (b ? 1 : 0);
            }
            else {
                hashCode5 = this.zzcfc.hashCode();
            }
        }
        return (((((hashCode + 527) * 31 + intValue) * 31 + hashCode2) * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode5;
    }
    
    @Override
    public final void zza(final zzyy zzyy) throws IOException {
        final Integer zzavw = this.zzavw;
        if (zzavw != null) {
            zzyy.zzd(1, zzavw);
        }
        final String zzavx = this.zzavx;
        if (zzavx != null) {
            zzyy.zzb(2, zzavx);
        }
        final Boolean zzavy = this.zzavy;
        if (zzavy != null) {
            zzyy.zzb(3, zzavy);
        }
        final String[] zzavz = this.zzavz;
        if (zzavz != null && zzavz.length > 0) {
            int n = 0;
            while (true) {
                final String[] zzavz2 = this.zzavz;
                if (n >= zzavz2.length) {
                    break;
                }
                final String s = zzavz2[n];
                if (s != null) {
                    zzyy.zzb(4, s);
                }
                ++n;
            }
        }
        super.zza(zzyy);
    }
    
    @Override
    protected final int zzf() {
        final int zzf = super.zzf();
        final Integer zzavw = this.zzavw;
        int n = zzf;
        if (zzavw != null) {
            n = zzf + zzyy.zzh(1, zzavw);
        }
        final String zzavx = this.zzavx;
        int n2 = n;
        if (zzavx != null) {
            n2 = n + zzyy.zzc(2, zzavx);
        }
        final Boolean zzavy = this.zzavy;
        int n3 = n2;
        if (zzavy != null) {
            zzavy;
            n3 = n2 + (zzyy.zzbb(3) + 1);
        }
        final String[] zzavz = this.zzavz;
        int n4 = n3;
        if (zzavz != null) {
            n4 = n3;
            if (zzavz.length > 0) {
                int n5 = 0;
                int n6 = 0;
                int n7 = 0;
                while (true) {
                    final String[] zzavz2 = this.zzavz;
                    if (n5 >= zzavz2.length) {
                        break;
                    }
                    final String s = zzavz2[n5];
                    int n8 = n6;
                    int n9 = n7;
                    if (s != null) {
                        n9 = n7 + 1;
                        n8 = n6 + zzyy.zzfx(s);
                    }
                    ++n5;
                    n6 = n8;
                    n7 = n9;
                }
                n4 = n3 + n6 + n7 * 1;
            }
        }
        return n4;
    }
}
