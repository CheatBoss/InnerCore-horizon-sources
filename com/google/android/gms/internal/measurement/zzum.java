package com.google.android.gms.internal.measurement;

import java.nio.charset.*;
import java.io.*;

class zzum extends zzul
{
    protected final byte[] zzbug;
    
    zzum(final byte[] zzbug) {
        if (zzbug != null) {
            this.zzbug = zzbug;
            return;
        }
        throw null;
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zzud)) {
            return false;
        }
        if (this.size() != ((zzud)o).size()) {
            return false;
        }
        if (this.size() == 0) {
            return true;
        }
        if (o instanceof zzum) {
            final zzum zzum = (zzum)o;
            final int zzuc = this.zzuc();
            final int zzuc2 = zzum.zzuc();
            return (zzuc == 0 || zzuc2 == 0 || zzuc == zzuc2) && this.zza(zzum, 0, this.size());
        }
        return o.equals(this);
    }
    
    @Override
    public int size() {
        return this.zzbug.length;
    }
    
    @Override
    protected final int zza(final int n, final int n2, final int n3) {
        return zzvo.zza(n, this.zzbug, this.zzud(), n3);
    }
    
    @Override
    protected final String zza(final Charset charset) {
        return new String(this.zzbug, this.zzud(), this.size(), charset);
    }
    
    @Override
    final void zza(final zzuc zzuc) throws IOException {
        zzuc.zza(this.zzbug, this.zzud(), this.size());
    }
    
    @Override
    final boolean zza(final zzud zzud, int n, final int n2) {
        if (n2 > zzud.size()) {
            n = this.size();
            final StringBuilder sb = new StringBuilder(40);
            sb.append("Length too large: ");
            sb.append(n2);
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
        if (n2 > zzud.size()) {
            n = zzud.size();
            final StringBuilder sb2 = new StringBuilder(59);
            sb2.append("Ran off end of other: 0, ");
            sb2.append(n2);
            sb2.append(", ");
            sb2.append(n);
            throw new IllegalArgumentException(sb2.toString());
        }
        if (zzud instanceof zzum) {
            final zzum zzum = (zzum)zzud;
            final byte[] zzbug = this.zzbug;
            final byte[] zzbug2 = zzum.zzbug;
            int zzud2;
            int i;
            for (zzud2 = this.zzud(), i = this.zzud(), n = zzum.zzud(); i < zzud2 + n2; ++i, ++n) {
                if (zzbug[i] != zzbug2[n]) {
                    return false;
                }
            }
            return true;
        }
        return zzud.zzb(0, n2).equals(this.zzb(0, n2));
    }
    
    @Override
    public byte zzal(final int n) {
        return this.zzbug[n];
    }
    
    @Override
    public final zzud zzb(int zzb, final int n) {
        zzb = zzud.zzb(0, n, this.size());
        if (zzb == 0) {
            return zzud.zzbtz;
        }
        return new zzuh(this.zzbug, this.zzud(), zzb);
    }
    
    @Override
    public final boolean zzub() {
        final int zzud = this.zzud();
        return zzyj.zzf(this.zzbug, zzud, this.size() + zzud);
    }
    
    protected int zzud() {
        return 0;
    }
}
