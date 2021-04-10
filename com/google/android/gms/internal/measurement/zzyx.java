package com.google.android.gms.internal.measurement;

import java.io.*;

public final class zzyx
{
    private final byte[] buffer;
    private int zzbuh;
    private int zzbui;
    private int zzbuj;
    private int zzbun;
    private int zzbup;
    private int zzbuq;
    private final int zzcev;
    private final int zzcew;
    private int zzcex;
    private int zzcey;
    private zzuo zzcez;
    
    private zzyx(final byte[] buffer, final int n, int n2) {
        this.zzbuq = Integer.MAX_VALUE;
        this.zzbui = 64;
        this.zzbuj = 67108864;
        this.buffer = buffer;
        this.zzcev = n;
        n2 += n;
        this.zzcex = n2;
        this.zzcew = n2;
        this.zzcey = n;
    }
    
    private final void zzas(final int n) throws IOException {
        if (n < 0) {
            throw zzzf.zzyx();
        }
        final int zzcey = this.zzcey;
        final int zzbuq = this.zzbuq;
        final int zzcey2 = zzcey + n;
        if (zzcey2 > zzbuq) {
            this.zzas(zzbuq - zzcey);
            throw zzzf.zzyw();
        }
        if (n <= this.zzcex - zzcey) {
            this.zzcey = zzcey2;
            return;
        }
        throw zzzf.zzyw();
    }
    
    public static zzyx zzj(final byte[] array, final int n, final int n2) {
        return new zzyx(array, 0, n2);
    }
    
    public static zzyx zzn(final byte[] array) {
        return zzj(array, 0, array.length);
    }
    
    private final void zzvc() {
        final int zzcex = this.zzcex + this.zzbun;
        this.zzcex = zzcex;
        final int zzbuq = this.zzbuq;
        if (zzcex > zzbuq) {
            final int zzbun = zzcex - zzbuq;
            this.zzbun = zzbun;
            this.zzcex = zzcex - zzbun;
            return;
        }
        this.zzbun = 0;
    }
    
    private final byte zzvd() throws IOException {
        final int zzcey = this.zzcey;
        if (zzcey != this.zzcex) {
            final byte[] buffer = this.buffer;
            this.zzcey = zzcey + 1;
            return buffer[zzcey];
        }
        throw zzzf.zzyw();
    }
    
    public final int getPosition() {
        return this.zzcey - this.zzcev;
    }
    
    public final String readString() throws IOException {
        final int zzuy = this.zzuy();
        if (zzuy < 0) {
            throw zzzf.zzyx();
        }
        if (zzuy <= this.zzcex - this.zzcey) {
            final String s = new String(this.buffer, this.zzcey, zzuy, zzze.UTF_8);
            this.zzcey += zzuy;
            return s;
        }
        throw zzzf.zzyw();
    }
    
    public final <T extends zzvm<T, ?>> T zza(final zzxd<T> zzxd) throws IOException {
        try {
            if (this.zzcez == null) {
                this.zzcez = zzuo.zzd(this.buffer, this.zzcev, this.zzcew);
            }
            final int zzux = this.zzcez.zzux();
            final int n = this.zzcey - this.zzcev;
            if (zzux <= n) {
                this.zzcez.zzas(n - zzux);
                this.zzcez.zzap(this.zzbui - this.zzbuh);
                final zzvm<T, ?> zzvm = this.zzcez.zza(zzxd, zzuz.zzvp());
                this.zzao(this.zzbup);
                return (T)zzvm;
            }
            throw new IOException(String.format("CodedInputStream read ahead of CodedInputByteBufferNano: %s > %s", zzux, n));
        }
        catch (zzvt zzvt) {
            throw new zzzf("", zzvt);
        }
    }
    
    public final void zza(final zzzg zzzg) throws IOException {
        final int zzuy = this.zzuy();
        if (this.zzbuh < this.zzbui) {
            final int zzaq = this.zzaq(zzuy);
            ++this.zzbuh;
            zzzg.zza(this);
            this.zzan(0);
            --this.zzbuh;
            this.zzar(zzaq);
            return;
        }
        throw zzzf.zzyz();
    }
    
    public final void zza(final zzzg zzzg, final int n) throws IOException {
        final int zzbuh = this.zzbuh;
        if (zzbuh < this.zzbui) {
            this.zzbuh = zzbuh + 1;
            zzzg.zza(this);
            this.zzan(n << 3 | 0x4);
            --this.zzbuh;
            return;
        }
        throw zzzf.zzyz();
    }
    
    public final void zzan(final int n) throws zzzf {
        if (this.zzbup == n) {
            return;
        }
        throw new zzzf("Protocol message end-group tag did not match expected tag.");
    }
    
    public final boolean zzao(final int n) throws IOException {
        final int n2 = n & 0x7;
        if (n2 == 0) {
            this.zzuy();
            return true;
        }
        if (n2 == 1) {
            this.zzvb();
            return true;
        }
        if (n2 == 2) {
            this.zzas(this.zzuy());
            return true;
        }
        if (n2 == 3) {
            int zzug;
            do {
                zzug = this.zzug();
            } while (zzug != 0 && this.zzao(zzug));
            this.zzan(n >>> 3 << 3 | 0x4);
            return true;
        }
        if (n2 == 4) {
            return false;
        }
        if (n2 == 5) {
            this.zzva();
            return true;
        }
        throw new zzzf("Protocol message tag had invalid wire type.");
    }
    
    public final int zzaq(int zzbuq) throws zzzf {
        if (zzbuq < 0) {
            throw zzzf.zzyx();
        }
        zzbuq += this.zzcey;
        final int zzbuq2 = this.zzbuq;
        if (zzbuq <= zzbuq2) {
            this.zzbuq = zzbuq;
            this.zzvc();
            return zzbuq2;
        }
        throw zzzf.zzyw();
    }
    
    public final void zzar(final int zzbuq) {
        this.zzbuq = zzbuq;
        this.zzvc();
    }
    
    public final void zzby(final int n) {
        this.zzt(n, this.zzbup);
    }
    
    public final byte[] zzs(final int n, final int n2) {
        if (n2 == 0) {
            return zzzj.zzcfx;
        }
        final byte[] array = new byte[n2];
        System.arraycopy(this.buffer, this.zzcev + n, array, 0, n2);
        return array;
    }
    
    final void zzt(final int n, int zzcey) {
        final int zzcey2 = this.zzcey;
        final int zzcev = this.zzcev;
        if (n > zzcey2 - zzcev) {
            zzcey = this.zzcey;
            final int zzcev2 = this.zzcev;
            final StringBuilder sb = new StringBuilder(50);
            sb.append("Position ");
            sb.append(n);
            sb.append(" is beyond current ");
            sb.append(zzcey - zzcev2);
            throw new IllegalArgumentException(sb.toString());
        }
        if (n >= 0) {
            this.zzcey = zzcev + n;
            this.zzbup = zzcey;
            return;
        }
        final StringBuilder sb2 = new StringBuilder(24);
        sb2.append("Bad position ");
        sb2.append(n);
        throw new IllegalArgumentException(sb2.toString());
    }
    
    public final int zzug() throws IOException {
        if (this.zzcey == this.zzcex) {
            return this.zzbup = 0;
        }
        final int zzuy = this.zzuy();
        if ((this.zzbup = zzuy) != 0) {
            return zzuy;
        }
        throw new zzzf("Protocol message contained an invalid tag (zero).");
    }
    
    public final boolean zzum() throws IOException {
        return this.zzuy() != 0;
    }
    
    public final int zzuy() throws IOException {
        final byte zzvd = this.zzvd();
        if (zzvd >= 0) {
            return zzvd;
        }
        int n = zzvd & 0x7F;
        final byte zzvd2 = this.zzvd();
        int n2;
        if (zzvd2 >= 0) {
            n2 = zzvd2 << 7;
        }
        else {
            n |= (zzvd2 & 0x7F) << 7;
            final byte zzvd3 = this.zzvd();
            if (zzvd3 >= 0) {
                n2 = zzvd3 << 14;
            }
            else {
                n |= (zzvd3 & 0x7F) << 14;
                final byte zzvd4 = this.zzvd();
                if (zzvd4 >= 0) {
                    n2 = zzvd4 << 21;
                }
                else {
                    final byte zzvd5 = this.zzvd();
                    final int n3 = n | (zzvd4 & 0x7F) << 21 | zzvd5 << 28;
                    if (zzvd5 < 0) {
                        for (int i = 0; i < 5; ++i) {
                            if (this.zzvd() >= 0) {
                                return n3;
                            }
                        }
                        throw zzzf.zzyy();
                    }
                    return n3;
                }
            }
        }
        return n | n2;
    }
    
    public final long zzuz() throws IOException {
        int i = 0;
        long n = 0L;
        while (i < 64) {
            final byte zzvd = this.zzvd();
            n |= (long)(zzvd & 0x7F) << i;
            if ((zzvd & 0x80) == 0x0) {
                return n;
            }
            i += 7;
        }
        throw zzzf.zzyy();
    }
    
    public final int zzva() throws IOException {
        return (this.zzvd() & 0xFF) | (this.zzvd() & 0xFF) << 8 | (this.zzvd() & 0xFF) << 16 | (this.zzvd() & 0xFF) << 24;
    }
    
    public final long zzvb() throws IOException {
        return ((long)this.zzvd() & 0xFFL) << 8 | ((long)this.zzvd() & 0xFFL) | ((long)this.zzvd() & 0xFFL) << 16 | ((long)this.zzvd() & 0xFFL) << 24 | ((long)this.zzvd() & 0xFFL) << 32 | ((long)this.zzvd() & 0xFFL) << 40 | ((long)this.zzvd() & 0xFFL) << 48 | ((long)this.zzvd() & 0xFFL) << 56;
    }
    
    public final int zzyr() {
        final int zzbuq = this.zzbuq;
        if (zzbuq == Integer.MAX_VALUE) {
            return -1;
        }
        return zzbuq - this.zzcey;
    }
}
