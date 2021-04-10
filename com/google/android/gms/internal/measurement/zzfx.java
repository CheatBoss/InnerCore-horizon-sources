package com.google.android.gms.internal.measurement;

import java.io.*;

public final class zzfx extends zzza<zzfx>
{
    public Integer zzavo;
    public Boolean zzavp;
    public String zzavq;
    public String zzavr;
    public String zzavs;
    
    public zzfx() {
        this.zzavo = null;
        this.zzavp = null;
        this.zzavq = null;
        this.zzavr = null;
        this.zzavs = null;
        this.zzcfc = null;
        this.zzcfm = -1;
    }
    
    private final zzfx zzc(final zzyx zzyx) throws IOException {
        while (true) {
            final int zzug = zzyx.zzug();
            if (zzug == 0) {
                break;
            }
            if (zzug == 8) {
                final int position = zzyx.getPosition();
                try {
                    final int zzuy = zzyx.zzuy();
                    if (zzuy >= 0 && zzuy <= 4) {
                        this.zzavo = zzuy;
                        continue;
                    }
                    final StringBuilder sb = new StringBuilder(46);
                    sb.append(zzuy);
                    sb.append(" is not a valid enum ComparisonType");
                    throw new IllegalArgumentException(sb.toString());
                }
                catch (IllegalArgumentException ex) {
                    zzyx.zzby(position);
                    this.zza(zzyx, zzug);
                    continue;
                }
                break;
            }
            if (zzug != 16) {
                if (zzug != 26) {
                    if (zzug != 34) {
                        if (zzug != 42) {
                            if (!super.zza(zzyx, zzug)) {
                                return this;
                            }
                            continue;
                        }
                        else {
                            this.zzavs = zzyx.readString();
                        }
                    }
                    else {
                        this.zzavr = zzyx.readString();
                    }
                }
                else {
                    this.zzavq = zzyx.readString();
                }
            }
            else {
                this.zzavp = zzyx.zzum();
            }
        }
        return this;
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zzfx)) {
            return false;
        }
        final zzfx zzfx = (zzfx)o;
        final Integer zzavo = this.zzavo;
        if (zzavo == null) {
            if (zzfx.zzavo != null) {
                return false;
            }
        }
        else if (!zzavo.equals(zzfx.zzavo)) {
            return false;
        }
        final Boolean zzavp = this.zzavp;
        if (zzavp == null) {
            if (zzfx.zzavp != null) {
                return false;
            }
        }
        else if (!zzavp.equals(zzfx.zzavp)) {
            return false;
        }
        final String zzavq = this.zzavq;
        if (zzavq == null) {
            if (zzfx.zzavq != null) {
                return false;
            }
        }
        else if (!zzavq.equals(zzfx.zzavq)) {
            return false;
        }
        final String zzavr = this.zzavr;
        if (zzavr == null) {
            if (zzfx.zzavr != null) {
                return false;
            }
        }
        else if (!zzavr.equals(zzfx.zzavr)) {
            return false;
        }
        final String zzavs = this.zzavs;
        if (zzavs == null) {
            if (zzfx.zzavs != null) {
                return false;
            }
        }
        else if (!zzavs.equals(zzfx.zzavs)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(zzfx.zzcfc);
        }
        return zzfx.zzcfc == null || zzfx.zzcfc.isEmpty();
    }
    
    @Override
    public final int hashCode() {
        final int hashCode = this.getClass().getName().hashCode();
        final Integer zzavo = this.zzavo;
        final boolean b = false;
        int intValue;
        if (zzavo == null) {
            intValue = 0;
        }
        else {
            intValue = zzavo;
        }
        final Boolean zzavp = this.zzavp;
        int hashCode2;
        if (zzavp == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = zzavp.hashCode();
        }
        final String zzavq = this.zzavq;
        int hashCode3;
        if (zzavq == null) {
            hashCode3 = 0;
        }
        else {
            hashCode3 = zzavq.hashCode();
        }
        final String zzavr = this.zzavr;
        int hashCode4;
        if (zzavr == null) {
            hashCode4 = 0;
        }
        else {
            hashCode4 = zzavr.hashCode();
        }
        final String zzavs = this.zzavs;
        int hashCode5;
        if (zzavs == null) {
            hashCode5 = 0;
        }
        else {
            hashCode5 = zzavs.hashCode();
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
        return ((((((hashCode + 527) * 31 + intValue) * 31 + hashCode2) * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode5) * 31 + hashCode6;
    }
    
    @Override
    public final void zza(final zzyy zzyy) throws IOException {
        final Integer zzavo = this.zzavo;
        if (zzavo != null) {
            zzyy.zzd(1, zzavo);
        }
        final Boolean zzavp = this.zzavp;
        if (zzavp != null) {
            zzyy.zzb(2, zzavp);
        }
        final String zzavq = this.zzavq;
        if (zzavq != null) {
            zzyy.zzb(3, zzavq);
        }
        final String zzavr = this.zzavr;
        if (zzavr != null) {
            zzyy.zzb(4, zzavr);
        }
        final String zzavs = this.zzavs;
        if (zzavs != null) {
            zzyy.zzb(5, zzavs);
        }
        super.zza(zzyy);
    }
    
    @Override
    protected final int zzf() {
        final int zzf = super.zzf();
        final Integer zzavo = this.zzavo;
        int n = zzf;
        if (zzavo != null) {
            n = zzf + zzyy.zzh(1, zzavo);
        }
        final Boolean zzavp = this.zzavp;
        int n2 = n;
        if (zzavp != null) {
            zzavp;
            n2 = n + (zzyy.zzbb(2) + 1);
        }
        final String zzavq = this.zzavq;
        int n3 = n2;
        if (zzavq != null) {
            n3 = n2 + zzyy.zzc(3, zzavq);
        }
        final String zzavr = this.zzavr;
        int n4 = n3;
        if (zzavr != null) {
            n4 = n3 + zzyy.zzc(4, zzavr);
        }
        final String zzavs = this.zzavs;
        int n5 = n4;
        if (zzavs != null) {
            n5 = n4 + zzyy.zzc(5, zzavs);
        }
        return n5;
    }
}
