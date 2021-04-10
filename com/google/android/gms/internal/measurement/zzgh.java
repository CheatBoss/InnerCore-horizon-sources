package com.google.android.gms.internal.measurement;

import java.io.*;

public final class zzgh extends zzza<zzgh>
{
    public zzgi[] zzawy;
    
    public zzgh() {
        this.zzawy = zzgi.zzms();
        this.zzcfc = null;
        this.zzcfm = -1;
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zzgh)) {
            return false;
        }
        final zzgh zzgh = (zzgh)o;
        if (!zzze.equals(this.zzawy, zzgh.zzawy)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(zzgh.zzcfc);
        }
        return zzgh.zzcfc == null || zzgh.zzcfc.isEmpty();
    }
    
    @Override
    public final int hashCode() {
        final int hashCode = this.getClass().getName().hashCode();
        final int hashCode2 = zzze.hashCode(this.zzawy);
        int hashCode3;
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            hashCode3 = this.zzcfc.hashCode();
        }
        else {
            hashCode3 = 0;
        }
        return ((hashCode + 527) * 31 + hashCode2) * 31 + hashCode3;
    }
    
    @Override
    public final void zza(final zzyy zzyy) throws IOException {
        final zzgi[] zzawy = this.zzawy;
        if (zzawy != null && zzawy.length > 0) {
            int n = 0;
            while (true) {
                final zzgi[] zzawy2 = this.zzawy;
                if (n >= zzawy2.length) {
                    break;
                }
                final zzgi zzgi = zzawy2[n];
                if (zzgi != null) {
                    zzyy.zza(1, zzgi);
                }
                ++n;
            }
        }
        super.zza(zzyy);
    }
    
    @Override
    protected final int zzf() {
        int zzf = super.zzf();
        final zzgi[] zzawy = this.zzawy;
        int n = zzf;
        if (zzawy != null) {
            n = zzf;
            if (zzawy.length > 0) {
                int n2 = 0;
                while (true) {
                    final zzgi[] zzawy2 = this.zzawy;
                    n = zzf;
                    if (n2 >= zzawy2.length) {
                        break;
                    }
                    final zzgi zzgi = zzawy2[n2];
                    int n3 = zzf;
                    if (zzgi != null) {
                        n3 = zzf + zzyy.zzb(1, zzgi);
                    }
                    ++n2;
                    zzf = n3;
                }
            }
        }
        return n;
    }
}
