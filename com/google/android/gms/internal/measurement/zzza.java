package com.google.android.gms.internal.measurement;

import java.io.*;

public abstract class zzza<M extends zzza<M>> extends zzzg
{
    protected zzzc zzcfc;
    
    @Override
    public void zza(final zzyy zzyy) throws IOException {
        if (this.zzcfc == null) {
            return;
        }
        for (int i = 0; i < this.zzcfc.size(); ++i) {
            this.zzcfc.zzcc(i).zza(zzyy);
        }
    }
    
    protected final boolean zza(final zzyx zzyx, final int n) throws IOException {
        final int position = zzyx.getPosition();
        if (!zzyx.zzao(n)) {
            return false;
        }
        final int n2 = n >>> 3;
        final zzzi zzzi = new zzzi(n, zzyx.zzs(position, zzyx.getPosition() - position));
        zzzd zzcb = null;
        final zzzc zzcfc = this.zzcfc;
        if (zzcfc == null) {
            this.zzcfc = new zzzc();
        }
        else {
            zzcb = zzcfc.zzcb(n2);
        }
        zzzd zzzd = zzcb;
        if (zzcb == null) {
            zzzd = new zzzd();
            this.zzcfc.zza(n2, zzzd);
        }
        zzzd.zza(zzzi);
        return true;
    }
    
    @Override
    protected int zzf() {
        final zzzc zzcfc = this.zzcfc;
        int n = 0;
        int n3;
        if (zzcfc != null) {
            int n2 = 0;
            while (true) {
                n3 = n2;
                if (n >= this.zzcfc.size()) {
                    break;
                }
                n2 += this.zzcfc.zzcc(n).zzf();
                ++n;
            }
        }
        else {
            n3 = 0;
        }
        return n3;
    }
}
