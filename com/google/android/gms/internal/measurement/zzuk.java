package com.google.android.gms.internal.measurement;

final class zzuk
{
    private final byte[] buffer;
    private final zzut zzbuf;
    
    private zzuk(final int n) {
        final byte[] buffer = new byte[n];
        this.buffer = buffer;
        this.zzbuf = zzut.zzj(buffer);
    }
    
    public final zzud zzue() {
        if (this.zzbuf.zzvg() == 0) {
            return new zzum(this.buffer);
        }
        throw new IllegalStateException("Did not write as much data as expected.");
    }
    
    public final zzut zzuf() {
        return this.zzbuf;
    }
}
