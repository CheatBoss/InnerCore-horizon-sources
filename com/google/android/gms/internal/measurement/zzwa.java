package com.google.android.gms.internal.measurement;

public class zzwa
{
    private static final zzuz zzbtt;
    private zzud zzcad;
    private volatile zzwt zzcae;
    private volatile zzud zzcaf;
    
    static {
        zzbtt = zzuz.zzvo();
    }
    
    private final zzwt zzh(final zzwt zzwt) {
        if (this.zzcae == null) {
            synchronized (this) {
                if (this.zzcae == null) {
                    try {
                        this.zzcae = zzwt;
                        this.zzcaf = zzud.zzbtz;
                    }
                    catch (zzvt zzvt) {
                        this.zzcae = zzwt;
                        this.zzcaf = zzud.zzbtz;
                    }
                }
            }
        }
        return this.zzcae;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof zzwa)) {
            return false;
        }
        final zzwa zzwa = (zzwa)o;
        final zzwt zzcae = this.zzcae;
        final zzwt zzcae2 = zzwa.zzcae;
        if (zzcae == null && zzcae2 == null) {
            return this.zztt().equals(zzwa.zztt());
        }
        if (zzcae != null && zzcae2 != null) {
            return zzcae.equals(zzcae2);
        }
        if (zzcae != null) {
            return zzcae.equals(zzwa.zzh(zzcae.zzwf()));
        }
        return this.zzh(zzcae2.zzwf()).equals(zzcae2);
    }
    
    @Override
    public int hashCode() {
        return 1;
    }
    
    public final zzwt zzi(final zzwt zzcae) {
        final zzwt zzcae2 = this.zzcae;
        this.zzcad = null;
        this.zzcaf = null;
        this.zzcae = zzcae;
        return zzcae2;
    }
    
    public final zzud zztt() {
        if (this.zzcaf != null) {
            return this.zzcaf;
        }
        synchronized (this) {
            if (this.zzcaf != null) {
                return this.zzcaf;
            }
            zzud zzcaf;
            if (this.zzcae == null) {
                zzcaf = zzud.zzbtz;
            }
            else {
                zzcaf = this.zzcae.zztt();
            }
            return this.zzcaf = zzcaf;
        }
    }
    
    public final int zzvu() {
        if (this.zzcaf != null) {
            return this.zzcaf.size();
        }
        if (this.zzcae != null) {
            return this.zzcae.zzvu();
        }
        return 0;
    }
}
