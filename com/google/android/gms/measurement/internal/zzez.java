package com.google.android.gms.measurement.internal;

abstract class zzez extends zzey
{
    private boolean zzvz;
    
    zzez(final zzfa zzfa) {
        super(zzfa);
        this.zzamz.zzb(this);
    }
    
    final boolean isInitialized() {
        return this.zzvz;
    }
    
    protected final void zzcl() {
        if (this.isInitialized()) {
            return;
        }
        throw new IllegalStateException("Not initialized");
    }
    
    protected abstract boolean zzgt();
    
    public final void zzq() {
        if (!this.zzvz) {
            this.zzgt();
            this.zzamz.zzma();
            this.zzvz = true;
            return;
        }
        throw new IllegalStateException("Can't initialize twice");
    }
}
