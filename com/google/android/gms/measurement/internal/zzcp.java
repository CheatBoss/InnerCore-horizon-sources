package com.google.android.gms.measurement.internal;

abstract class zzcp extends zzco
{
    private boolean zzvz;
    
    zzcp(final zzbt zzbt) {
        super(zzbt);
        this.zzadj.zzb(this);
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
    
    public final void zzgs() {
        if (!this.zzvz) {
            this.zzgu();
            this.zzadj.zzkq();
            this.zzvz = true;
            return;
        }
        throw new IllegalStateException("Can't initialize twice");
    }
    
    protected abstract boolean zzgt();
    
    protected void zzgu() {
    }
    
    public final void zzq() {
        if (!this.zzvz) {
            if (!this.zzgt()) {
                this.zzadj.zzkq();
                this.zzvz = true;
            }
            return;
        }
        throw new IllegalStateException("Can't initialize twice");
    }
}
