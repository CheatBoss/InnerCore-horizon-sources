package com.google.android.gms.internal.measurement;

import java.io.*;

public abstract class zzuo
{
    int zzbuh;
    int zzbui;
    private int zzbuj;
    zzur zzbuk;
    private boolean zzbul;
    
    private zzuo() {
        this.zzbui = 100;
        this.zzbuj = Integer.MAX_VALUE;
        this.zzbul = false;
    }
    
    static zzuo zza(final byte[] array, final int n, final int n2, final boolean b) {
        final zzuq zzuq = new zzuq(array, n, n2, false, null);
        try {
            zzuq.zzaq(n2);
            return zzuq;
        }
        catch (zzvt zzvt) {
            throw new IllegalArgumentException(zzvt);
        }
    }
    
    public static zzuo zzd(final byte[] array, final int n, final int n2) {
        return zza(array, n, n2, false);
    }
    
    public abstract double readDouble() throws IOException;
    
    public abstract float readFloat() throws IOException;
    
    public abstract String readString() throws IOException;
    
    public abstract <T extends zzwt> T zza(final zzxd<T> p0, final zzuz p1) throws IOException;
    
    public abstract void zzan(final int p0) throws zzvt;
    
    public abstract boolean zzao(final int p0) throws IOException;
    
    public final int zzap(final int zzbui) {
        if (zzbui >= 0) {
            final int zzbui2 = this.zzbui;
            this.zzbui = zzbui;
            return zzbui2;
        }
        final StringBuilder sb = new StringBuilder(47);
        sb.append("Recursion limit cannot be negative: ");
        sb.append(zzbui);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public abstract int zzaq(final int p0) throws zzvt;
    
    public abstract void zzar(final int p0);
    
    public abstract void zzas(final int p0) throws IOException;
    
    public abstract int zzug() throws IOException;
    
    public abstract long zzuh() throws IOException;
    
    public abstract long zzui() throws IOException;
    
    public abstract int zzuj() throws IOException;
    
    public abstract long zzuk() throws IOException;
    
    public abstract int zzul() throws IOException;
    
    public abstract boolean zzum() throws IOException;
    
    public abstract String zzun() throws IOException;
    
    public abstract zzud zzuo() throws IOException;
    
    public abstract int zzup() throws IOException;
    
    public abstract int zzuq() throws IOException;
    
    public abstract int zzur() throws IOException;
    
    public abstract long zzus() throws IOException;
    
    public abstract int zzut() throws IOException;
    
    public abstract long zzuu() throws IOException;
    
    abstract long zzuv() throws IOException;
    
    public abstract boolean zzuw() throws IOException;
    
    public abstract int zzux();
}
