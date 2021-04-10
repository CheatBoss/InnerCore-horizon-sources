package com.google.android.gms.internal.measurement;

import java.io.*;
import java.util.*;

final class zzuq extends zzuo
{
    private final byte[] buffer;
    private int limit;
    private int pos;
    private final boolean zzbum;
    private int zzbun;
    private int zzbuo;
    private int zzbup;
    private int zzbuq;
    
    private zzuq(final byte[] buffer, final int n, final int n2, final boolean zzbum) {
        super(null);
        this.zzbuq = Integer.MAX_VALUE;
        this.buffer = buffer;
        this.limit = n2 + n;
        this.pos = n;
        this.zzbuo = n;
        this.zzbum = zzbum;
    }
    
    private final int zzuy() throws IOException {
        final int pos = this.pos;
        final int limit = this.limit;
        if (limit != pos) {
            final byte[] buffer = this.buffer;
            final int pos2 = pos + 1;
            final byte b = buffer[pos];
            if (b >= 0) {
                this.pos = pos2;
                return b;
            }
            if (limit - pos2 >= 9) {
                int pos3 = pos2 + 1;
                final int n = b ^ buffer[pos2] << 7;
                int n2;
                if (n < 0) {
                    n2 = (n ^ 0xFFFFFF80);
                }
                else {
                    final int n3 = pos3 + 1;
                    final int n4 = n ^ buffer[pos3] << 14;
                    if (n4 >= 0) {
                        final int n5 = n4 ^ 0x3F80;
                        pos3 = n3;
                        n2 = n5;
                    }
                    else {
                        pos3 = n3 + 1;
                        final int n6 = n4 ^ buffer[n3] << 21;
                        if (n6 < 0) {
                            n2 = (n6 ^ 0xFFE03F80);
                        }
                        else {
                            final int n7 = pos3 + 1;
                            final byte b2 = buffer[pos3];
                            final int n8 = n2 = (n6 ^ b2 << 28 ^ 0xFE03F80);
                            pos3 = n7;
                            if (b2 < 0) {
                                final int n9 = n7 + 1;
                                n2 = n8;
                                pos3 = n9;
                                if (buffer[n7] < 0) {
                                    final int n10 = n9 + 1;
                                    n2 = n8;
                                    pos3 = n10;
                                    if (buffer[n9] < 0) {
                                        final int n11 = n10 + 1;
                                        n2 = n8;
                                        pos3 = n11;
                                        if (buffer[n10] < 0) {
                                            final int n12 = n11 + 1;
                                            n2 = n8;
                                            pos3 = n12;
                                            if (buffer[n11] < 0) {
                                                pos3 = n12 + 1;
                                                if (buffer[n12] < 0) {
                                                    return (int)this.zzuv();
                                                }
                                                n2 = n8;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                this.pos = pos3;
                return n2;
            }
        }
        return (int)this.zzuv();
    }
    
    private final long zzuz() throws IOException {
        final int pos = this.pos;
        final int limit = this.limit;
        if (limit != pos) {
            final byte[] buffer = this.buffer;
            final int pos2 = pos + 1;
            final byte b = buffer[pos];
            if (b >= 0) {
                this.pos = pos2;
                return b;
            }
            if (limit - pos2 >= 9) {
                int n = pos2 + 1;
                final int n2 = b ^ buffer[pos2] << 7;
                int pos3 = 0;
                long n14 = 0L;
                Label_0340: {
                    int n5 = 0;
                    Label_0135: {
                        int n3;
                        if (n2 < 0) {
                            n3 = (n2 ^ 0xFFFFFF80);
                        }
                        else {
                            pos3 = n + 1;
                            final int n4 = n2 ^ buffer[n] << 14;
                            if (n4 >= 0) {
                                n5 = (n4 ^ 0x3F80);
                                break Label_0135;
                            }
                            n = pos3 + 1;
                            final int n6 = n4 ^ buffer[pos3] << 21;
                            if (n6 >= 0) {
                                final long n7 = n6;
                                pos3 = n + 1;
                                long n8 = n7 ^ (long)buffer[n] << 28;
                                Label_0251: {
                                    long n12 = 0L;
                                    Label_0175: {
                                        if (n8 < 0L) {
                                            final int n9 = pos3 + 1;
                                            long n10 = n8 ^ (long)buffer[pos3] << 35;
                                            long n11;
                                            if (n10 < 0L) {
                                                n11 = -34093383808L;
                                                pos3 = n9;
                                            }
                                            else {
                                                pos3 = n9 + 1;
                                                n8 = (n10 ^ (long)buffer[n9] << 42);
                                                if (n8 >= 0L) {
                                                    n12 = 4363953127296L;
                                                    break Label_0175;
                                                }
                                                final int n13 = pos3 + 1;
                                                n10 = (n8 ^ (long)buffer[pos3] << 49);
                                                if (n10 < 0L) {
                                                    n11 = -558586000294016L;
                                                    pos3 = n13;
                                                }
                                                else {
                                                    pos3 = n13 + 1;
                                                    n14 = (n10 ^ (long)buffer[n13] << 56 ^ 0xFE03F80FE03F80L);
                                                    if (n14 >= 0L) {
                                                        break Label_0340;
                                                    }
                                                    if (buffer[pos3] >= 0L) {
                                                        ++pos3;
                                                        break Label_0340;
                                                    }
                                                    return this.zzuv();
                                                }
                                            }
                                            n14 = (n10 ^ n11);
                                            break Label_0251;
                                        }
                                        n12 = 266354560L;
                                    }
                                    n14 = (n12 ^ n8);
                                }
                                break Label_0340;
                            }
                            n3 = (n6 ^ 0xFFE03F80);
                        }
                        final int n15 = n;
                        n5 = n3;
                        pos3 = n15;
                    }
                    n14 = n5;
                }
                this.pos = pos3;
                return n14;
            }
        }
        return this.zzuv();
    }
    
    private final int zzva() throws IOException {
        final int pos = this.pos;
        if (this.limit - pos >= 4) {
            final byte[] buffer = this.buffer;
            this.pos = pos + 4;
            return (buffer[pos + 3] & 0xFF) << 24 | ((buffer[pos] & 0xFF) | (buffer[pos + 1] & 0xFF) << 8 | (buffer[pos + 2] & 0xFF) << 16);
        }
        throw zzvt.zzwk();
    }
    
    private final long zzvb() throws IOException {
        final int pos = this.pos;
        if (this.limit - pos >= 8) {
            final byte[] buffer = this.buffer;
            this.pos = pos + 8;
            return ((long)buffer[pos + 7] & 0xFFL) << 56 | (((long)buffer[pos] & 0xFFL) | ((long)buffer[pos + 1] & 0xFFL) << 8 | ((long)buffer[pos + 2] & 0xFFL) << 16 | ((long)buffer[pos + 3] & 0xFFL) << 24 | ((long)buffer[pos + 4] & 0xFFL) << 32 | ((long)buffer[pos + 5] & 0xFFL) << 40 | ((long)buffer[pos + 6] & 0xFFL) << 48);
        }
        throw zzvt.zzwk();
    }
    
    private final void zzvc() {
        final int limit = this.limit + this.zzbun;
        this.limit = limit;
        final int n = limit - this.zzbuo;
        final int zzbuq = this.zzbuq;
        if (n > zzbuq) {
            final int zzbun = n - zzbuq;
            this.zzbun = zzbun;
            this.limit = limit - zzbun;
            return;
        }
        this.zzbun = 0;
    }
    
    private final byte zzvd() throws IOException {
        final int pos = this.pos;
        if (pos != this.limit) {
            final byte[] buffer = this.buffer;
            this.pos = pos + 1;
            return buffer[pos];
        }
        throw zzvt.zzwk();
    }
    
    @Override
    public final double readDouble() throws IOException {
        return Double.longBitsToDouble(this.zzvb());
    }
    
    @Override
    public final float readFloat() throws IOException {
        return Float.intBitsToFloat(this.zzva());
    }
    
    @Override
    public final String readString() throws IOException {
        final int zzuy = this.zzuy();
        if (zzuy > 0 && zzuy <= this.limit - this.pos) {
            final String s = new String(this.buffer, this.pos, zzuy, zzvo.UTF_8);
            this.pos += zzuy;
            return s;
        }
        if (zzuy == 0) {
            return "";
        }
        if (zzuy < 0) {
            throw zzvt.zzwl();
        }
        throw zzvt.zzwk();
    }
    
    @Override
    public final <T extends zzwt> T zza(final zzxd<T> zzxd, final zzuz zzuz) throws IOException {
        final int zzuy = this.zzuy();
        if (this.zzbuh < this.zzbui) {
            final int zzaq = this.zzaq(zzuy);
            ++this.zzbuh;
            final zzwt zzwt = zzxd.zza(this, zzuz);
            this.zzan(0);
            --this.zzbuh;
            this.zzar(zzaq);
            return (T)zzwt;
        }
        throw zzvt.zzwp();
    }
    
    @Override
    public final void zzan(final int n) throws zzvt {
        if (this.zzbup == n) {
            return;
        }
        throw zzvt.zzwn();
    }
    
    @Override
    public final boolean zzao(int i) throws IOException {
        final int n = i & 0x7;
        final int n2 = 0;
        final int n3 = 0;
        if (n == 0) {
            i = n2;
            if (this.limit - this.pos >= 10) {
                for (i = n3; i < 10; ++i) {
                    if (this.buffer[this.pos++] >= 0) {
                        return true;
                    }
                }
                throw zzvt.zzwm();
            }
            while (i < 10) {
                if (this.zzvd() >= 0) {
                    return true;
                }
                ++i;
            }
            throw zzvt.zzwm();
        }
        if (n == 1) {
            this.zzas(8);
            return true;
        }
        if (n == 2) {
            this.zzas(this.zzuy());
            return true;
        }
        if (n == 3) {
            int zzug;
            do {
                zzug = this.zzug();
            } while (zzug != 0 && this.zzao(zzug));
            this.zzan(i >>> 3 << 3 | 0x4);
            return true;
        }
        if (n == 4) {
            return false;
        }
        if (n == 5) {
            this.zzas(4);
            return true;
        }
        throw zzvt.zzwo();
    }
    
    @Override
    public final int zzaq(int zzbuq) throws zzvt {
        if (zzbuq < 0) {
            throw zzvt.zzwl();
        }
        zzbuq += this.zzux();
        final int zzbuq2 = this.zzbuq;
        if (zzbuq <= zzbuq2) {
            this.zzbuq = zzbuq;
            this.zzvc();
            return zzbuq2;
        }
        throw zzvt.zzwk();
    }
    
    @Override
    public final void zzar(final int zzbuq) {
        this.zzbuq = zzbuq;
        this.zzvc();
    }
    
    @Override
    public final void zzas(final int n) throws IOException {
        if (n >= 0) {
            final int limit = this.limit;
            final int pos = this.pos;
            if (n <= limit - pos) {
                this.pos = pos + n;
                return;
            }
        }
        if (n < 0) {
            throw zzvt.zzwl();
        }
        throw zzvt.zzwk();
    }
    
    @Override
    public final int zzug() throws IOException {
        if (this.zzuw()) {
            return this.zzbup = 0;
        }
        final int zzuy = this.zzuy();
        this.zzbup = zzuy;
        if (zzuy >>> 3 != 0) {
            return zzuy;
        }
        throw new zzvt("Protocol message contained an invalid tag (zero).");
    }
    
    @Override
    public final long zzuh() throws IOException {
        return this.zzuz();
    }
    
    @Override
    public final long zzui() throws IOException {
        return this.zzuz();
    }
    
    @Override
    public final int zzuj() throws IOException {
        return this.zzuy();
    }
    
    @Override
    public final long zzuk() throws IOException {
        return this.zzvb();
    }
    
    @Override
    public final int zzul() throws IOException {
        return this.zzva();
    }
    
    @Override
    public final boolean zzum() throws IOException {
        return this.zzuz() != 0L;
    }
    
    @Override
    public final String zzun() throws IOException {
        final int zzuy = this.zzuy();
        if (zzuy > 0) {
            final int limit = this.limit;
            final int pos = this.pos;
            if (zzuy <= limit - pos) {
                final String zzh = zzyj.zzh(this.buffer, pos, zzuy);
                this.pos += zzuy;
                return zzh;
            }
        }
        if (zzuy == 0) {
            return "";
        }
        if (zzuy <= 0) {
            throw zzvt.zzwl();
        }
        throw zzvt.zzwk();
    }
    
    @Override
    public final zzud zzuo() throws IOException {
        final int zzuy = this.zzuy();
        if (zzuy > 0) {
            final int limit = this.limit;
            final int pos = this.pos;
            if (zzuy <= limit - pos) {
                final zzud zzb = zzud.zzb(this.buffer, pos, zzuy);
                this.pos += zzuy;
                return zzb;
            }
        }
        if (zzuy == 0) {
            return zzud.zzbtz;
        }
        if (zzuy > 0) {
            final int limit2 = this.limit;
            final int pos2 = this.pos;
            if (zzuy <= limit2 - pos2) {
                final int pos3 = zzuy + pos2;
                this.pos = pos3;
                final byte[] array = Arrays.copyOfRange(this.buffer, pos2, pos3);
                return zzud.zzi(array);
            }
        }
        if (zzuy > 0) {
            throw zzvt.zzwk();
        }
        if (zzuy != 0) {
            throw zzvt.zzwl();
        }
        final byte[] array = zzvo.zzbzj;
        return zzud.zzi(array);
    }
    
    @Override
    public final int zzup() throws IOException {
        return this.zzuy();
    }
    
    @Override
    public final int zzuq() throws IOException {
        return this.zzuy();
    }
    
    @Override
    public final int zzur() throws IOException {
        return this.zzva();
    }
    
    @Override
    public final long zzus() throws IOException {
        return this.zzvb();
    }
    
    @Override
    public final int zzut() throws IOException {
        final int zzuy = this.zzuy();
        return zzuy >>> 1 ^ -(zzuy & 0x1);
    }
    
    @Override
    public final long zzuu() throws IOException {
        final long zzuz = this.zzuz();
        return -(zzuz & 0x1L) ^ zzuz >>> 1;
    }
    
    @Override
    final long zzuv() throws IOException {
        long n = 0L;
        for (int i = 0; i < 64; i += 7) {
            final byte zzvd = this.zzvd();
            n |= (long)(zzvd & 0x7F) << i;
            if ((zzvd & 0x80) == 0x0) {
                return n;
            }
        }
        throw zzvt.zzwm();
    }
    
    @Override
    public final boolean zzuw() throws IOException {
        return this.pos == this.limit;
    }
    
    @Override
    public final int zzux() {
        return this.pos - this.zzbuo;
    }
}
