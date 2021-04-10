package com.google.android.gms.internal.measurement;

import java.io.*;
import java.util.logging.*;
import java.nio.*;

public abstract class zzut extends zzuc
{
    private static final Logger logger;
    private static final boolean zzbuv;
    zzuv zzbuw;
    
    static {
        logger = Logger.getLogger(zzut.class.getName());
        zzbuv = zzyh.zzyi();
    }
    
    private zzut() {
    }
    
    public static int zza(int zzbb, final zzwa zzwa) {
        zzbb = zzbb(zzbb);
        final int zzvu = zzwa.zzvu();
        return zzbb + (zzbd(zzvu) + zzvu);
    }
    
    public static int zza(final zzwa zzwa) {
        final int zzvu = zzwa.zzvu();
        return zzbd(zzvu) + zzvu;
    }
    
    public static zzut zza(final ByteBuffer byteBuffer) {
        if (byteBuffer.hasArray()) {
            return new zzb(byteBuffer);
        }
        if (!byteBuffer.isDirect() || byteBuffer.isReadOnly()) {
            throw new IllegalArgumentException("ByteBuffer is read-only");
        }
        if (zzyh.zzyj()) {
            return new zze(byteBuffer);
        }
        return new zzd(byteBuffer);
    }
    
    public static int zzay(final long n) {
        return zzaz(n);
    }
    
    public static int zzaz(long n) {
        if ((n & 0xFFFFFFFFFFFFFF80L) == 0x0L) {
            return 1;
        }
        if (n < 0L) {
            return 10;
        }
        int n2;
        if ((n & 0xFFFFFFF800000000L) != 0x0L) {
            n2 = 6;
            n >>>= 28;
        }
        else {
            n2 = 2;
        }
        int n3 = n2;
        long n4 = n;
        if ((n & 0xFFFFFFFFFFE00000L) != 0x0L) {
            n3 = n2 + 2;
            n4 = n >>> 14;
        }
        int n5 = n3;
        if ((n4 & 0xFFFFFFFFFFFFC000L) != 0x0L) {
            n5 = n3 + 1;
        }
        return n5;
    }
    
    public static int zzb(final float n) {
        return 4;
    }
    
    public static int zzb(final int n, final double n2) {
        return zzbb(n) + 8;
    }
    
    public static int zzb(final int n, final float n2) {
        return zzbb(n) + 4;
    }
    
    public static int zzb(final int n, final zzwa zzwa) {
        return (zzbb(1) << 1) + zzi(2, n) + zza(3, zzwa);
    }
    
    static int zzb(final int n, final zzwt zzwt, final zzxj zzxj) {
        return zzbb(n) + zzb(zzwt, zzxj);
    }
    
    public static int zzb(final zzud zzud) {
        final int size = zzud.size();
        return zzbd(size) + size;
    }
    
    static int zzb(final zzwt zzwt, final zzxj zzxj) {
        final zztw zztw = (zztw)zzwt;
        int n;
        if ((n = zztw.zztu()) == -1) {
            n = zzxj.zzae(zztw);
            zztw.zzah(n);
        }
        return zzbd(n) + n;
    }
    
    public static int zzba(final long n) {
        return zzaz(zzbd(n));
    }
    
    public static int zzbb(final int n) {
        return zzbd(n << 3);
    }
    
    public static int zzbb(final long n) {
        return 8;
    }
    
    public static int zzbc(final int n) {
        if (n >= 0) {
            return zzbd(n);
        }
        return 10;
    }
    
    public static int zzbc(final long n) {
        return 8;
    }
    
    public static int zzbd(final int n) {
        if ((n & 0xFFFFFF80) == 0x0) {
            return 1;
        }
        if ((n & 0xFFFFC000) == 0x0) {
            return 2;
        }
        if ((0xFFE00000 & n) == 0x0) {
            return 3;
        }
        if ((n & 0xF0000000) == 0x0) {
            return 4;
        }
        return 5;
    }
    
    private static long zzbd(final long n) {
        return n >> 63 ^ n << 1;
    }
    
    public static int zzbe(final int n) {
        return zzbd(zzbi(n));
    }
    
    public static int zzbf(final int n) {
        return 4;
    }
    
    public static int zzbg(final int n) {
        return 4;
    }
    
    public static int zzbh(final int n) {
        return zzbc(n);
    }
    
    private static int zzbi(final int n) {
        return n << 1 ^ n >> 31;
    }
    
    @Deprecated
    public static int zzbj(final int n) {
        return zzbd(n);
    }
    
    public static int zzc(final double n) {
        return 8;
    }
    
    public static int zzc(int zzbb, final zzud zzud) {
        zzbb = zzbb(zzbb);
        final int size = zzud.size();
        return zzbb + (zzbd(size) + size);
    }
    
    public static int zzc(final int n, final zzwt zzwt) {
        return zzbb(n) + zzc(zzwt);
    }
    
    @Deprecated
    static int zzc(int n, final zzwt zzwt, final zzxj zzxj) {
        final int zzbb = zzbb(n);
        final zztw zztw = (zztw)zzwt;
        if ((n = zztw.zztu()) == -1) {
            n = zzxj.zzae(zztw);
            zztw.zzah(n);
        }
        return (zzbb << 1) + n;
    }
    
    public static int zzc(final int n, final String s) {
        return zzbb(n) + zzfx(s);
    }
    
    public static int zzc(final int n, final boolean b) {
        return zzbb(n) + 1;
    }
    
    public static int zzc(final zzwt zzwt) {
        final int zzvu = zzwt.zzvu();
        return zzbd(zzvu) + zzvu;
    }
    
    public static int zzd(final int n, final long n2) {
        return zzbb(n) + zzaz(n2);
    }
    
    public static int zzd(final int n, final zzud zzud) {
        return (zzbb(1) << 1) + zzi(2, n) + zzc(3, zzud);
    }
    
    public static int zzd(final int n, final zzwt zzwt) {
        return (zzbb(1) << 1) + zzi(2, n) + zzc(3, zzwt);
    }
    
    @Deprecated
    public static int zzd(final zzwt zzwt) {
        return zzwt.zzvu();
    }
    
    public static int zze(final int n, final long n2) {
        return zzbb(n) + zzaz(n2);
    }
    
    public static int zzf(final int n, final long n2) {
        return zzbb(n) + zzaz(zzbd(n2));
    }
    
    public static int zzfx(final String s) {
        int n;
        try {
            n = zzyj.zza(s);
        }
        catch (zzyn zzyn) {
            n = s.getBytes(zzvo.UTF_8).length;
        }
        return zzbd(n) + n;
    }
    
    public static int zzg(final int n, final long n2) {
        return zzbb(n) + 8;
    }
    
    public static int zzh(final int n, final int n2) {
        return zzbb(n) + zzbc(n2);
    }
    
    public static int zzh(final int n, final long n2) {
        return zzbb(n) + 8;
    }
    
    public static int zzi(final int n, final int n2) {
        return zzbb(n) + zzbd(n2);
    }
    
    public static int zzj(final int n, final int n2) {
        return zzbb(n) + zzbd(zzbi(n2));
    }
    
    public static zzut zzj(final byte[] array) {
        return new zza(array, 0, array.length);
    }
    
    public static int zzk(final int n, final int n2) {
        return zzbb(n) + 4;
    }
    
    public static int zzk(final byte[] array) {
        final int length = array.length;
        return zzbd(length) + length;
    }
    
    public static int zzl(final int n, final int n2) {
        return zzbb(n) + 4;
    }
    
    public static int zzm(final int n, final int n2) {
        return zzbb(n) + zzbc(n2);
    }
    
    public static int zzv(final boolean b) {
        return 1;
    }
    
    public abstract void flush() throws IOException;
    
    public abstract void write(final byte[] p0, final int p1, final int p2) throws IOException;
    
    public final void zza(final float n) throws IOException {
        this.zzba(Float.floatToRawIntBits(n));
    }
    
    public final void zza(final int n, final double n2) throws IOException {
        this.zzc(n, Double.doubleToRawLongBits(n2));
    }
    
    public final void zza(final int n, final float n2) throws IOException {
        this.zzg(n, Float.floatToRawIntBits(n2));
    }
    
    public abstract void zza(final int p0, final long p1) throws IOException;
    
    public abstract void zza(final int p0, final zzud p1) throws IOException;
    
    public abstract void zza(final int p0, final zzwt p1) throws IOException;
    
    abstract void zza(final int p0, final zzwt p1, final zzxj p2) throws IOException;
    
    public abstract void zza(final zzud p0) throws IOException;
    
    abstract void zza(final zzwt p0, final zzxj p1) throws IOException;
    
    final void zza(final String s, final zzyn zzyn) throws IOException {
        zzut.logger.logp(Level.WARNING, "com.google.protobuf.CodedOutputStream", "inefficientWriteStringNoTag", "Converting ill-formed UTF-16. Your Protocol Buffer will not round trip correctly!", zzyn);
        final byte[] bytes = s.getBytes(zzvo.UTF_8);
        try {
            this.zzay(bytes.length);
            this.zza(bytes, 0, bytes.length);
        }
        catch (zzc zzc) {
            throw zzc;
        }
        catch (IndexOutOfBoundsException ex) {
            throw new zzc(ex);
        }
    }
    
    public abstract void zzav(final long p0) throws IOException;
    
    public final void zzaw(final long n) throws IOException {
        this.zzav(zzbd(n));
    }
    
    public abstract void zzax(final int p0) throws IOException;
    
    public abstract void zzax(final long p0) throws IOException;
    
    public abstract void zzay(final int p0) throws IOException;
    
    public final void zzaz(final int n) throws IOException {
        this.zzay(zzbi(n));
    }
    
    public final void zzb(final double n) throws IOException {
        this.zzax(Double.doubleToRawLongBits(n));
    }
    
    public final void zzb(final int n, final long n2) throws IOException {
        this.zza(n, zzbd(n2));
    }
    
    public abstract void zzb(final int p0, final zzud p1) throws IOException;
    
    public abstract void zzb(final int p0, final zzwt p1) throws IOException;
    
    public abstract void zzb(final int p0, final String p1) throws IOException;
    
    public abstract void zzb(final int p0, final boolean p1) throws IOException;
    
    public abstract void zzb(final zzwt p0) throws IOException;
    
    public abstract void zzba(final int p0) throws IOException;
    
    public abstract void zzc(final byte p0) throws IOException;
    
    public abstract void zzc(final int p0, final int p1) throws IOException;
    
    public abstract void zzc(final int p0, final long p1) throws IOException;
    
    public abstract void zzd(final int p0, final int p1) throws IOException;
    
    public abstract void zze(final int p0, final int p1) throws IOException;
    
    abstract void zze(final byte[] p0, final int p1, final int p2) throws IOException;
    
    public final void zzf(final int n, final int n2) throws IOException {
        this.zze(n, zzbi(n2));
    }
    
    public abstract void zzfw(final String p0) throws IOException;
    
    public abstract void zzg(final int p0, final int p1) throws IOException;
    
    public final void zzu(final boolean b) throws IOException {
        this.zzc((byte)(b ? 1 : 0));
    }
    
    public abstract int zzvg();
    
    static class zza extends zzut
    {
        private final byte[] buffer;
        private final int limit;
        private final int offset;
        private int position;
        
        zza(final byte[] buffer, final int n, final int n2) {
            super(null);
            if (buffer == null) {
                throw new NullPointerException("buffer");
            }
            final int length = buffer.length;
            final int limit = n + n2;
            if ((length - limit | (n | n2)) >= 0) {
                this.buffer = buffer;
                this.offset = n;
                this.position = n;
                this.limit = limit;
                return;
            }
            throw new IllegalArgumentException(String.format("Array range is invalid. Buffer.length=%d, offset=%d, length=%d", buffer.length, n, n2));
        }
        
        @Override
        public void flush() {
        }
        
        @Override
        public final void write(final byte[] array, final int n, final int n2) throws IOException {
            try {
                System.arraycopy(array, n, this.buffer, this.position, n2);
                this.position += n2;
            }
            catch (IndexOutOfBoundsException ex) {
                throw new zzc(String.format("Pos: %d, limit: %d, len: %d", this.position, this.limit, n2), ex);
            }
        }
        
        @Override
        public final void zza(final int n, final long n2) throws IOException {
            this.zzc(n, 0);
            this.zzav(n2);
        }
        
        @Override
        public final void zza(final int n, final zzud zzud) throws IOException {
            this.zzc(n, 2);
            this.zza(zzud);
        }
        
        @Override
        public final void zza(final int n, final zzwt zzwt) throws IOException {
            this.zzc(n, 2);
            this.zzb(zzwt);
        }
        
        @Override
        final void zza(int n, final zzwt zzwt, final zzxj zzxj) throws IOException {
            this.zzc(n, 2);
            final zztw zztw = (zztw)zzwt;
            if ((n = zztw.zztu()) == -1) {
                n = zzxj.zzae(zztw);
                zztw.zzah(n);
            }
            this.zzay(n);
            zzxj.zza(zzwt, this.zzbuw);
        }
        
        @Override
        public final void zza(final zzud zzud) throws IOException {
            this.zzay(zzud.size());
            zzud.zza(this);
        }
        
        @Override
        final void zza(final zzwt zzwt, final zzxj zzxj) throws IOException {
            final zztw zztw = (zztw)zzwt;
            int n;
            if ((n = zztw.zztu()) == -1) {
                n = zzxj.zzae(zztw);
                zztw.zzah(n);
            }
            this.zzay(n);
            zzxj.zza(zzwt, this.zzbuw);
        }
        
        @Override
        public final void zza(final byte[] array, final int n, final int n2) throws IOException {
            this.write(array, n, n2);
        }
        
        @Override
        public final void zzav(long n) throws IOException {
            long n2 = n;
            if (zzut.zzbuv) {
                n2 = n;
                if (this.zzvg() >= 10) {
                    while ((n & 0xFFFFFFFFFFFFFF80L) != 0x0L) {
                        zzyh.zza(this.buffer, this.position++, (byte)(((int)n & 0x7F) | 0x80));
                        n >>>= 7;
                    }
                    zzyh.zza(this.buffer, this.position++, (byte)n);
                    return;
                }
            }
            while (true) {
                if ((n2 & 0xFFFFFFFFFFFFFF80L) == 0x0L) {
                    try {
                        this.buffer[this.position++] = (byte)n2;
                        return;
                    }
                    catch (IndexOutOfBoundsException ex) {
                        break;
                    }
                }
                this.buffer[this.position++] = (byte)(((int)n2 & 0x7F) | 0x80);
                n2 >>>= 7;
            }
            final IndexOutOfBoundsException ex;
            throw new zzc(String.format("Pos: %d, limit: %d, len: %d", this.position, this.limit, 1), ex);
        }
        
        @Override
        public final void zzax(final int n) throws IOException {
            if (n >= 0) {
                this.zzay(n);
                return;
            }
            this.zzav(n);
        }
        
        @Override
        public final void zzax(final long n) throws IOException {
            try {
                final byte[] buffer = this.buffer;
                final int position = this.position;
                final int position2 = position + 1;
                this.position = position2;
                buffer[position] = (byte)n;
                final byte[] buffer2 = this.buffer;
                final int position3 = position2 + 1;
                this.position = position3;
                buffer2[position2] = (byte)(n >> 8);
                final byte[] buffer3 = this.buffer;
                final int position4 = position3 + 1;
                this.position = position4;
                buffer3[position3] = (byte)(n >> 16);
                final byte[] buffer4 = this.buffer;
                final int position5 = position4 + 1;
                this.position = position5;
                buffer4[position4] = (byte)(n >> 24);
                final byte[] buffer5 = this.buffer;
                final int position6 = position5 + 1;
                this.position = position6;
                buffer5[position5] = (byte)(n >> 32);
                final byte[] buffer6 = this.buffer;
                final int position7 = position6 + 1;
                this.position = position7;
                buffer6[position6] = (byte)(n >> 40);
                final byte[] buffer7 = this.buffer;
                final int position8 = position7 + 1;
                this.position = position8;
                buffer7[position7] = (byte)(n >> 48);
                final byte[] buffer8 = this.buffer;
                this.position = position8 + 1;
                buffer8[position8] = (byte)(n >> 56);
            }
            catch (IndexOutOfBoundsException ex) {
                throw new zzc(String.format("Pos: %d, limit: %d, len: %d", this.position, this.limit, 1), ex);
            }
        }
        
        @Override
        public final void zzay(int n) throws IOException {
            int n2 = n;
            if (zzut.zzbuv) {
                n2 = n;
                if (this.zzvg() >= 10) {
                    while ((n & 0xFFFFFF80) != 0x0) {
                        zzyh.zza(this.buffer, this.position++, (byte)((n & 0x7F) | 0x80));
                        n >>>= 7;
                    }
                    zzyh.zza(this.buffer, this.position++, (byte)n);
                    return;
                }
            }
            while (true) {
                if ((n2 & 0xFFFFFF80) == 0x0) {
                    try {
                        final byte[] buffer = this.buffer;
                        n = this.position++;
                        buffer[n] = (byte)n2;
                        return;
                    }
                    catch (IndexOutOfBoundsException ex) {
                        break;
                    }
                }
                final byte[] buffer2 = this.buffer;
                n = this.position++;
                buffer2[n] = (byte)((n2 & 0x7F) | 0x80);
                n2 >>>= 7;
            }
            final IndexOutOfBoundsException ex;
            throw new zzc(String.format("Pos: %d, limit: %d, len: %d", this.position, this.limit, 1), ex);
        }
        
        @Override
        public final void zzb(final int n, final zzud zzud) throws IOException {
            this.zzc(1, 3);
            this.zze(2, n);
            this.zza(3, zzud);
            this.zzc(1, 4);
        }
        
        @Override
        public final void zzb(final int n, final zzwt zzwt) throws IOException {
            this.zzc(1, 3);
            this.zze(2, n);
            this.zza(3, zzwt);
            this.zzc(1, 4);
        }
        
        @Override
        public final void zzb(final int n, final String s) throws IOException {
            this.zzc(n, 2);
            this.zzfw(s);
        }
        
        @Override
        public final void zzb(final int n, final boolean b) throws IOException {
            this.zzc(n, 0);
            this.zzc((byte)(b ? 1 : 0));
        }
        
        @Override
        public final void zzb(final zzwt zzwt) throws IOException {
            this.zzay(zzwt.zzvu());
            zzwt.zzb(this);
        }
        
        @Override
        public final void zzba(final int n) throws IOException {
            try {
                final byte[] buffer = this.buffer;
                final int position = this.position;
                final int position2 = position + 1;
                this.position = position2;
                buffer[position] = (byte)n;
                final byte[] buffer2 = this.buffer;
                final int position3 = position2 + 1;
                this.position = position3;
                buffer2[position2] = (byte)(n >> 8);
                final byte[] buffer3 = this.buffer;
                final int position4 = position3 + 1;
                this.position = position4;
                buffer3[position3] = (byte)(n >> 16);
                final byte[] buffer4 = this.buffer;
                this.position = position4 + 1;
                buffer4[position4] = (byte)(n >> 24);
            }
            catch (IndexOutOfBoundsException ex) {
                throw new zzc(String.format("Pos: %d, limit: %d, len: %d", this.position, this.limit, 1), ex);
            }
        }
        
        @Override
        public final void zzc(final byte b) throws IOException {
            try {
                this.buffer[this.position++] = b;
            }
            catch (IndexOutOfBoundsException ex) {
                throw new zzc(String.format("Pos: %d, limit: %d, len: %d", this.position, this.limit, 1), ex);
            }
        }
        
        @Override
        public final void zzc(final int n, final int n2) throws IOException {
            this.zzay(n << 3 | n2);
        }
        
        @Override
        public final void zzc(final int n, final long n2) throws IOException {
            this.zzc(n, 1);
            this.zzax(n2);
        }
        
        @Override
        public final void zzd(final int n, final int n2) throws IOException {
            this.zzc(n, 0);
            this.zzax(n2);
        }
        
        @Override
        public final void zze(final int n, final int n2) throws IOException {
            this.zzc(n, 0);
            this.zzay(n2);
        }
        
        public final void zze(final byte[] array, final int n, final int n2) throws IOException {
            this.zzay(n2);
            this.write(array, 0, n2);
        }
        
        @Override
        public final void zzfw(final String s) throws IOException {
            final int position = this.position;
            try {
                final int zzbd = zzut.zzbd(s.length() * 3);
                final int zzbd2 = zzut.zzbd(s.length());
                if (zzbd2 == zzbd) {
                    final int position2 = position + zzbd2;
                    this.position = position2;
                    final int zza = zzyj.zza(s, this.buffer, position2, this.zzvg());
                    this.zzay(zza - (this.position = position) - zzbd2);
                    this.position = zza;
                    return;
                }
                this.zzay(zzyj.zza(s));
                this.position = zzyj.zza(s, this.buffer, this.position, this.zzvg());
            }
            catch (IndexOutOfBoundsException ex) {
                throw new zzc(ex);
            }
            catch (zzyn zzyn) {
                this.position = position;
                this.zza(s, zzyn);
            }
        }
        
        @Override
        public final void zzg(final int n, final int n2) throws IOException {
            this.zzc(n, 5);
            this.zzba(n2);
        }
        
        @Override
        public final int zzvg() {
            return this.limit - this.position;
        }
        
        public final int zzvi() {
            return this.position - this.offset;
        }
    }
    
    static final class zzb extends zza
    {
        private final ByteBuffer zzbux;
        private int zzbuy;
        
        zzb(final ByteBuffer zzbux) {
            super(zzbux.array(), zzbux.arrayOffset() + zzbux.position(), zzbux.remaining());
            this.zzbux = zzbux;
            this.zzbuy = zzbux.position();
        }
        
        @Override
        public final void flush() {
            this.zzbux.position(this.zzbuy + ((zza)this).zzvi());
        }
    }
    
    public static final class zzc extends IOException
    {
        zzc() {
            super("CodedOutputStream was writing to a flat byte array and ran out of space.");
        }
        
        zzc(String s) {
            s = String.valueOf(s);
            if (s.length() != 0) {
                s = "CodedOutputStream was writing to a flat byte array and ran out of space.: ".concat(s);
            }
            else {
                s = new String("CodedOutputStream was writing to a flat byte array and ran out of space.: ");
            }
            super(s);
        }
        
        zzc(String s, final Throwable t) {
            s = String.valueOf(s);
            if (s.length() != 0) {
                s = "CodedOutputStream was writing to a flat byte array and ran out of space.: ".concat(s);
            }
            else {
                s = new String("CodedOutputStream was writing to a flat byte array and ran out of space.: ");
            }
            super(s, t);
        }
        
        zzc(final Throwable t) {
            super("CodedOutputStream was writing to a flat byte array and ran out of space.", t);
        }
    }
    
    static final class zzd extends zzut
    {
        private final int zzbuy;
        private final ByteBuffer zzbuz;
        private final ByteBuffer zzbva;
        
        zzd(final ByteBuffer zzbuz) {
            super(null);
            this.zzbuz = zzbuz;
            this.zzbva = zzbuz.duplicate().order(ByteOrder.LITTLE_ENDIAN);
            this.zzbuy = zzbuz.position();
        }
        
        private final void zzfy(final String s) throws IOException {
            try {
                zzyj.zza(s, this.zzbva);
            }
            catch (IndexOutOfBoundsException ex) {
                throw new zzc(ex);
            }
        }
        
        @Override
        public final void flush() {
            this.zzbuz.position(this.zzbva.position());
        }
        
        @Override
        public final void write(final byte[] array, final int n, final int n2) throws IOException {
            try {
                this.zzbva.put(array, n, n2);
            }
            catch (BufferOverflowException ex) {
                throw new zzc(ex);
            }
            catch (IndexOutOfBoundsException ex2) {
                throw new zzc(ex2);
            }
        }
        
        @Override
        public final void zza(final int n, final long n2) throws IOException {
            this.zzc(n, 0);
            this.zzav(n2);
        }
        
        @Override
        public final void zza(final int n, final zzud zzud) throws IOException {
            this.zzc(n, 2);
            this.zza(zzud);
        }
        
        @Override
        public final void zza(final int n, final zzwt zzwt) throws IOException {
            this.zzc(n, 2);
            this.zzb(zzwt);
        }
        
        @Override
        final void zza(final int n, final zzwt zzwt, final zzxj zzxj) throws IOException {
            this.zzc(n, 2);
            this.zza(zzwt, zzxj);
        }
        
        @Override
        public final void zza(final zzud zzud) throws IOException {
            this.zzay(zzud.size());
            zzud.zza(this);
        }
        
        @Override
        final void zza(final zzwt zzwt, final zzxj zzxj) throws IOException {
            final zztw zztw = (zztw)zzwt;
            int n;
            if ((n = zztw.zztu()) == -1) {
                n = zzxj.zzae(zztw);
                zztw.zzah(n);
            }
            this.zzay(n);
            zzxj.zza(zzwt, this.zzbuw);
        }
        
        @Override
        public final void zza(final byte[] array, final int n, final int n2) throws IOException {
            this.write(array, n, n2);
        }
        
        @Override
        public final void zzav(long n) throws IOException {
            while (true) {
                Label_0022: {
                    if ((n & 0xFFFFFFFFFFFFFF80L) != 0x0L) {
                        break Label_0022;
                    }
                    try {
                        this.zzbva.put((byte)n);
                        return;
                        this.zzbva.put((byte)(((int)n & 0x7F) | 0x80));
                        n >>>= 7;
                        continue;
                    }
                    catch (BufferOverflowException ex) {
                        throw new zzc(ex);
                    }
                }
            }
        }
        
        @Override
        public final void zzax(final int n) throws IOException {
            if (n >= 0) {
                this.zzay(n);
                return;
            }
            this.zzav(n);
        }
        
        @Override
        public final void zzax(final long n) throws IOException {
            try {
                this.zzbva.putLong(n);
            }
            catch (BufferOverflowException ex) {
                throw new zzc(ex);
            }
        }
        
        @Override
        public final void zzay(int n) throws IOException {
            while (true) {
                Label_0018: {
                    if ((n & 0xFFFFFF80) != 0x0) {
                        break Label_0018;
                    }
                    try {
                        this.zzbva.put((byte)n);
                        return;
                        this.zzbva.put((byte)((n & 0x7F) | 0x80));
                        n >>>= 7;
                        continue;
                    }
                    catch (BufferOverflowException ex) {
                        throw new zzc(ex);
                    }
                }
            }
        }
        
        @Override
        public final void zzb(final int n, final zzud zzud) throws IOException {
            this.zzc(1, 3);
            this.zze(2, n);
            this.zza(3, zzud);
            this.zzc(1, 4);
        }
        
        @Override
        public final void zzb(final int n, final zzwt zzwt) throws IOException {
            this.zzc(1, 3);
            this.zze(2, n);
            this.zza(3, zzwt);
            this.zzc(1, 4);
        }
        
        @Override
        public final void zzb(final int n, final String s) throws IOException {
            this.zzc(n, 2);
            this.zzfw(s);
        }
        
        @Override
        public final void zzb(final int n, final boolean b) throws IOException {
            this.zzc(n, 0);
            this.zzc((byte)(b ? 1 : 0));
        }
        
        @Override
        public final void zzb(final zzwt zzwt) throws IOException {
            this.zzay(zzwt.zzvu());
            zzwt.zzb(this);
        }
        
        @Override
        public final void zzba(final int n) throws IOException {
            try {
                this.zzbva.putInt(n);
            }
            catch (BufferOverflowException ex) {
                throw new zzc(ex);
            }
        }
        
        @Override
        public final void zzc(final byte b) throws IOException {
            try {
                this.zzbva.put(b);
            }
            catch (BufferOverflowException ex) {
                throw new zzc(ex);
            }
        }
        
        @Override
        public final void zzc(final int n, final int n2) throws IOException {
            this.zzay(n << 3 | n2);
        }
        
        @Override
        public final void zzc(final int n, final long n2) throws IOException {
            this.zzc(n, 1);
            this.zzax(n2);
        }
        
        @Override
        public final void zzd(final int n, final int n2) throws IOException {
            this.zzc(n, 0);
            this.zzax(n2);
        }
        
        @Override
        public final void zze(final int n, final int n2) throws IOException {
            this.zzc(n, 0);
            this.zzay(n2);
        }
        
        public final void zze(final byte[] array, final int n, final int n2) throws IOException {
            this.zzay(n2);
            this.write(array, 0, n2);
        }
        
        @Override
        public final void zzfw(final String s) throws IOException {
            final int position = this.zzbva.position();
            try {
                final int zzbd = zzut.zzbd(s.length() * 3);
                final int zzbd2 = zzut.zzbd(s.length());
                if (zzbd2 == zzbd) {
                    final int n = this.zzbva.position() + zzbd2;
                    this.zzbva.position(n);
                    this.zzfy(s);
                    final int position2 = this.zzbva.position();
                    this.zzbva.position(position);
                    this.zzay(position2 - n);
                    this.zzbva.position(position2);
                    return;
                }
                this.zzay(zzyj.zza(s));
                this.zzfy(s);
            }
            catch (IllegalArgumentException ex) {
                throw new zzc(ex);
            }
            catch (zzyn zzyn) {
                this.zzbva.position(position);
                this.zza(s, zzyn);
            }
        }
        
        @Override
        public final void zzg(final int n, final int n2) throws IOException {
            this.zzc(n, 5);
            this.zzba(n2);
        }
        
        @Override
        public final int zzvg() {
            return this.zzbva.remaining();
        }
    }
    
    static final class zze extends zzut
    {
        private final ByteBuffer zzbuz;
        private final ByteBuffer zzbva;
        private final long zzbvb;
        private final long zzbvc;
        private final long zzbvd;
        private final long zzbve;
        private long zzbvf;
        
        zze(final ByteBuffer zzbuz) {
            super(null);
            this.zzbuz = zzbuz;
            this.zzbva = zzbuz.duplicate().order(ByteOrder.LITTLE_ENDIAN);
            final long zzb = zzyh.zzb(zzbuz);
            this.zzbvb = zzb;
            this.zzbvc = zzb + zzbuz.position();
            final long zzbvd = this.zzbvb + zzbuz.limit();
            this.zzbvd = zzbvd;
            this.zzbve = zzbvd - 10L;
            this.zzbvf = this.zzbvc;
        }
        
        private final void zzbe(final long n) {
            this.zzbva.position((int)(n - this.zzbvb));
        }
        
        @Override
        public final void flush() {
            this.zzbuz.position((int)(this.zzbvf - this.zzbvb));
        }
        
        @Override
        public final void write(final byte[] array, final int n, final int n2) throws IOException {
            if (array != null && n >= 0 && n2 >= 0 && array.length - n2 >= n) {
                final long zzbvd = this.zzbvd;
                final long n3 = n2;
                final long zzbvf = this.zzbvf;
                if (zzbvd - n3 >= zzbvf) {
                    zzyh.zza(array, n, zzbvf, n3);
                    this.zzbvf += n3;
                    return;
                }
            }
            if (array == null) {
                throw new NullPointerException("value");
            }
            throw new zzc(String.format("Pos: %d, limit: %d, len: %d", this.zzbvf, this.zzbvd, n2));
        }
        
        @Override
        public final void zza(final int n, final long n2) throws IOException {
            this.zzc(n, 0);
            this.zzav(n2);
        }
        
        @Override
        public final void zza(final int n, final zzud zzud) throws IOException {
            this.zzc(n, 2);
            this.zza(zzud);
        }
        
        @Override
        public final void zza(final int n, final zzwt zzwt) throws IOException {
            this.zzc(n, 2);
            this.zzb(zzwt);
        }
        
        @Override
        final void zza(final int n, final zzwt zzwt, final zzxj zzxj) throws IOException {
            this.zzc(n, 2);
            this.zza(zzwt, zzxj);
        }
        
        @Override
        public final void zza(final zzud zzud) throws IOException {
            this.zzay(zzud.size());
            zzud.zza(this);
        }
        
        @Override
        final void zza(final zzwt zzwt, final zzxj zzxj) throws IOException {
            final zztw zztw = (zztw)zzwt;
            int n;
            if ((n = zztw.zztu()) == -1) {
                n = zzxj.zzae(zztw);
                zztw.zzah(n);
            }
            this.zzay(n);
            zzxj.zza(zzwt, this.zzbuw);
        }
        
        @Override
        public final void zza(final byte[] array, final int n, final int n2) throws IOException {
            this.write(array, n, n2);
        }
        
        @Override
        public final void zzav(long zzbvf) throws IOException {
            long n = zzbvf;
            if (this.zzbvf <= this.zzbve) {
                while ((zzbvf & 0xFFFFFFFFFFFFFF80L) != 0x0L) {
                    zzyh.zza(this.zzbvf++, (byte)(((int)zzbvf & 0x7F) | 0x80));
                    zzbvf >>>= 7;
                }
                zzyh.zza(this.zzbvf++, (byte)zzbvf);
                return;
            }
            while (true) {
                zzbvf = this.zzbvf;
                if (zzbvf >= this.zzbvd) {
                    throw new zzc(String.format("Pos: %d, limit: %d, len: %d", this.zzbvf, this.zzbvd, 1));
                }
                if ((n & 0xFFFFFFFFFFFFFF80L) == 0x0L) {
                    this.zzbvf = zzbvf + 1L;
                    zzyh.zza(zzbvf, (byte)n);
                    return;
                }
                this.zzbvf = zzbvf + 1L;
                zzyh.zza(zzbvf, (byte)(((int)n & 0x7F) | 0x80));
                n >>>= 7;
            }
        }
        
        @Override
        public final void zzax(final int n) throws IOException {
            if (n >= 0) {
                this.zzay(n);
                return;
            }
            this.zzav(n);
        }
        
        @Override
        public final void zzax(final long n) throws IOException {
            this.zzbva.putLong((int)(this.zzbvf - this.zzbvb), n);
            this.zzbvf += 8L;
        }
        
        @Override
        public final void zzay(int n) throws IOException {
            int n2 = n;
            long n3;
            if (this.zzbvf <= this.zzbve) {
                while ((n & 0xFFFFFF80) != 0x0) {
                    zzyh.zza(this.zzbvf++, (byte)((n & 0x7F) | 0x80));
                    n >>>= 7;
                }
                n3 = this.zzbvf;
            }
            else {
                while (true) {
                    n3 = this.zzbvf;
                    if (n3 >= this.zzbvd) {
                        throw new zzc(String.format("Pos: %d, limit: %d, len: %d", this.zzbvf, this.zzbvd, 1));
                    }
                    if ((n2 & 0xFFFFFF80) == 0x0) {
                        n = n2;
                        break;
                    }
                    this.zzbvf = n3 + 1L;
                    zzyh.zza(n3, (byte)((n2 & 0x7F) | 0x80));
                    n2 >>>= 7;
                }
            }
            this.zzbvf = n3 + 1L;
            zzyh.zza(n3, (byte)n);
        }
        
        @Override
        public final void zzb(final int n, final zzud zzud) throws IOException {
            this.zzc(1, 3);
            this.zze(2, n);
            this.zza(3, zzud);
            this.zzc(1, 4);
        }
        
        @Override
        public final void zzb(final int n, final zzwt zzwt) throws IOException {
            this.zzc(1, 3);
            this.zze(2, n);
            this.zza(3, zzwt);
            this.zzc(1, 4);
        }
        
        @Override
        public final void zzb(final int n, final String s) throws IOException {
            this.zzc(n, 2);
            this.zzfw(s);
        }
        
        @Override
        public final void zzb(final int n, final boolean b) throws IOException {
            this.zzc(n, 0);
            this.zzc((byte)(b ? 1 : 0));
        }
        
        @Override
        public final void zzb(final zzwt zzwt) throws IOException {
            this.zzay(zzwt.zzvu());
            zzwt.zzb(this);
        }
        
        @Override
        public final void zzba(final int n) throws IOException {
            this.zzbva.putInt((int)(this.zzbvf - this.zzbvb), n);
            this.zzbvf += 4L;
        }
        
        @Override
        public final void zzc(final byte b) throws IOException {
            final long zzbvf = this.zzbvf;
            if (zzbvf < this.zzbvd) {
                this.zzbvf = zzbvf + 1L;
                zzyh.zza(zzbvf, b);
                return;
            }
            throw new zzc(String.format("Pos: %d, limit: %d, len: %d", this.zzbvf, this.zzbvd, 1));
        }
        
        @Override
        public final void zzc(final int n, final int n2) throws IOException {
            this.zzay(n << 3 | n2);
        }
        
        @Override
        public final void zzc(final int n, final long n2) throws IOException {
            this.zzc(n, 1);
            this.zzax(n2);
        }
        
        @Override
        public final void zzd(final int n, final int n2) throws IOException {
            this.zzc(n, 0);
            this.zzax(n2);
        }
        
        @Override
        public final void zze(final int n, final int n2) throws IOException {
            this.zzc(n, 0);
            this.zzay(n2);
        }
        
        public final void zze(final byte[] array, final int n, final int n2) throws IOException {
            this.zzay(n2);
            this.write(array, 0, n2);
        }
        
        @Override
        public final void zzfw(final String s) throws IOException {
            final long zzbvf = this.zzbvf;
            try {
                final int zzbd = zzut.zzbd(s.length() * 3);
                final int zzbd2 = zzut.zzbd(s.length());
                if (zzbd2 == zzbd) {
                    final int n = (int)(this.zzbvf - this.zzbvb) + zzbd2;
                    this.zzbva.position(n);
                    zzyj.zza(s, this.zzbva);
                    final int n2 = this.zzbva.position() - n;
                    this.zzay(n2);
                    this.zzbvf += n2;
                    return;
                }
                final int zza = zzyj.zza(s);
                this.zzay(zza);
                this.zzbe(this.zzbvf);
                zzyj.zza(s, this.zzbva);
                this.zzbvf += zza;
            }
            catch (IndexOutOfBoundsException ex) {
                throw new zzc(ex);
            }
            catch (IllegalArgumentException ex2) {
                throw new zzc(ex2);
            }
            catch (zzyn zzyn) {
                this.zzbe(this.zzbvf = zzbvf);
                this.zza(s, zzyn);
            }
        }
        
        @Override
        public final void zzg(final int n, final int n2) throws IOException {
            this.zzc(n, 5);
            this.zzba(n2);
        }
        
        @Override
        public final int zzvg() {
            return (int)(this.zzbvd - this.zzbvf);
        }
    }
}
