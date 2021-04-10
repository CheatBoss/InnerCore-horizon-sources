package com.google.android.gms.internal.measurement;

import java.io.*;

public abstract class zzzg
{
    protected volatile int zzcfm;
    
    public zzzg() {
        this.zzcfm = -1;
    }
    
    @Override
    public String toString() {
        return zzzh.zzc(this);
    }
    
    public abstract zzzg zza(final zzyx p0) throws IOException;
    
    public void zza(final zzyy zzyy) throws IOException {
    }
    
    protected int zzf() {
        return 0;
    }
    
    public final int zzvu() {
        return this.zzcfm = this.zzf();
    }
    
    public zzzg zzyu() throws CloneNotSupportedException {
        return (zzzg)super.clone();
    }
    
    public final int zzza() {
        if (this.zzcfm < 0) {
            this.zzvu();
        }
        return this.zzcfm;
    }
}
