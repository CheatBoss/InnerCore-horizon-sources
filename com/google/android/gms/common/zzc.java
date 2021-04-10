package com.google.android.gms.common;

import java.lang.ref.*;

abstract class zzc extends CertData
{
    private static final WeakReference<byte[]> zzbf;
    private WeakReference<byte[]> zzbe;
    
    static {
        zzbf = new WeakReference<byte[]>(null);
    }
    
    zzc(final byte[] array) {
        super(array);
        this.zzbe = zzc.zzbf;
    }
    
    @Override
    final byte[] getBytes() {
        synchronized (this) {
            byte[] zzf;
            if ((zzf = this.zzbe.get()) == null) {
                zzf = this.zzf();
                this.zzbe = new WeakReference<byte[]>(zzf);
            }
            return zzf;
        }
    }
    
    protected abstract byte[] zzf();
}
