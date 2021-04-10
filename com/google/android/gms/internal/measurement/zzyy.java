package com.google.android.gms.internal.measurement;

import java.io.*;
import java.nio.*;

public final class zzyy
{
    private final ByteBuffer zzbva;
    private zzut zzcfa;
    private int zzcfb;
    
    private zzyy(final ByteBuffer zzbva) {
        (this.zzbva = zzbva).order(ByteOrder.LITTLE_ENDIAN);
    }
    
    private zzyy(final byte[] array, final int n, final int n2) {
        this(ByteBuffer.wrap(array, n, n2));
    }
    
    private static int zza(final CharSequence charSequence) {
        final int length = charSequence.length();
        final int n = 0;
        int n2;
        for (n2 = 0; n2 < length && charSequence.charAt(n2) < '\u0080'; ++n2) {}
        int i = n2;
        int n3 = length;
        int n4;
        while (true) {
            n4 = n3;
            if (i >= length) {
                break;
            }
            final char char1 = charSequence.charAt(i);
            if (char1 >= '\u0800') {
                final int length2 = charSequence.length();
                int n5 = n;
                while (i < length2) {
                    final char char2 = charSequence.charAt(i);
                    int n6;
                    if (char2 < '\u0800') {
                        n5 += '\u007f' - char2 >>> 31;
                        n6 = i;
                    }
                    else {
                        final int n7 = n5 += 2;
                        n6 = i;
                        if ('\ud800' <= char2) {
                            n5 = n7;
                            n6 = i;
                            if (char2 <= '\udfff') {
                                if (Character.codePointAt(charSequence, i) < 65536) {
                                    final StringBuilder sb = new StringBuilder(39);
                                    sb.append("Unpaired surrogate at index ");
                                    sb.append(i);
                                    throw new IllegalArgumentException(sb.toString());
                                }
                                n6 = i + 1;
                                n5 = n7;
                            }
                        }
                    }
                    i = n6 + 1;
                }
                n4 = n3 + n5;
                break;
            }
            n3 += '\u007f' - char1 >>> 31;
            ++i;
        }
        if (n4 >= length) {
            return n4;
        }
        final long n8 = n4;
        final StringBuilder sb2 = new StringBuilder(54);
        sb2.append("UTF-8 length does not fit in int: ");
        sb2.append(n8 + 4294967296L);
        throw new IllegalArgumentException(sb2.toString());
    }
    
    public static int zzb(int zzbb, final zzzg zzzg) {
        zzbb = zzbb(zzbb);
        final int zzvu = zzzg.zzvu();
        return zzbb + (zzbj(zzvu) + zzvu);
    }
    
    public static int zzbb(final int n) {
        return zzbj(n << 3);
    }
    
    public static int zzbc(final int n) {
        if (n >= 0) {
            return zzbj(n);
        }
        return 10;
    }
    
    private final void zzbh(long n) throws IOException {
        while ((n & 0xFFFFFFFFFFFFFF80L) != 0x0L) {
            this.zzbz(((int)n & 0x7F) | 0x80);
            n >>>= 7;
        }
        this.zzbz((int)n);
    }
    
    public static int zzbi(final long n) {
        if ((n & 0xFFFFFFFFFFFFFF80L) == 0x0L) {
            return 1;
        }
        if ((n & 0xFFFFFFFFFFFFC000L) == 0x0L) {
            return 2;
        }
        if ((n & 0xFFFFFFFFFFE00000L) == 0x0L) {
            return 3;
        }
        if ((n & 0xFFFFFFFFF0000000L) == 0x0L) {
            return 4;
        }
        if ((n & 0xFFFFFFF800000000L) == 0x0L) {
            return 5;
        }
        if ((n & 0xFFFFFC0000000000L) == 0x0L) {
            return 6;
        }
        if ((n & 0xFFFE000000000000L) == 0x0L) {
            return 7;
        }
        if ((n & 0xFF00000000000000L) == 0x0L) {
            return 8;
        }
        if ((n & Long.MIN_VALUE) == 0x0L) {
            return 9;
        }
        return 10;
    }
    
    public static int zzbj(final int n) {
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
    
    private final void zzbz(final int n) throws IOException {
        final byte b = (byte)n;
        if (this.zzbva.hasRemaining()) {
            this.zzbva.put(b);
            return;
        }
        throw new zzyz(this.zzbva.position(), this.zzbva.limit());
    }
    
    public static int zzc(final int n, final String s) {
        return zzbb(n) + zzfx(s);
    }
    
    public static int zzd(final int n, final long n2) {
        return zzbb(n) + zzbi(n2);
    }
    
    private static void zzd(final CharSequence charSequence, final ByteBuffer byteBuffer) {
        if (!byteBuffer.isReadOnly()) {
            final boolean hasArray = byteBuffer.hasArray();
            final int n = 0;
            int i = 0;
            if (hasArray) {
                try {
                    final byte[] array = byteBuffer.array();
                    final int n2 = byteBuffer.arrayOffset() + byteBuffer.position();
                    final int remaining = byteBuffer.remaining();
                    final int length = charSequence.length();
                    final int n3 = remaining + n2;
                    while (i < length) {
                        final int n4 = i + n2;
                        if (n4 >= n3) {
                            break;
                        }
                        final char char1 = charSequence.charAt(i);
                        if (char1 >= '\u0080') {
                            break;
                        }
                        array[n4] = (byte)char1;
                        ++i;
                    }
                    int n6 = 0;
                    Label_0586: {
                        if (i != length) {
                            int n5 = n2 + i;
                            while (true) {
                                n6 = n5;
                                if (i >= length) {
                                    break Label_0586;
                                }
                                final char char2 = charSequence.charAt(i);
                                if (char2 < '\u0080' && n5 < n3) {
                                    final int n7 = n5 + 1;
                                    array[n5] = (byte)char2;
                                    n5 = n7;
                                }
                                else if (char2 < '\u0800' && n5 <= n3 - 2) {
                                    final int n8 = n5 + 1;
                                    array[n5] = (byte)(char2 >>> 6 | 0x3C0);
                                    n5 = n8 + 1;
                                    array[n8] = (byte)((char2 & '?') | 0x80);
                                }
                                else if ((char2 < '\ud800' || '\udfff' < char2) && n5 <= n3 - 3) {
                                    final int n9 = n5 + 1;
                                    array[n5] = (byte)(char2 >>> 12 | 0x1E0);
                                    final int n10 = n9 + 1;
                                    array[n9] = (byte)((char2 >>> 6 & 0x3F) | 0x80);
                                    n5 = n10 + 1;
                                    array[n10] = (byte)((char2 & '?') | 0x80);
                                }
                                else {
                                    if (n5 > n3 - 4) {
                                        final StringBuilder sb = new StringBuilder(37);
                                        sb.append("Failed writing ");
                                        sb.append(char2);
                                        sb.append(" at index ");
                                        sb.append(n5);
                                        throw new ArrayIndexOutOfBoundsException(sb.toString());
                                    }
                                    final int n11 = i + 1;
                                    if (n11 == charSequence.length()) {
                                        break;
                                    }
                                    final char char3 = charSequence.charAt(n11);
                                    if (!Character.isSurrogatePair(char2, char3)) {
                                        i = n11;
                                        break;
                                    }
                                    final int codePoint = Character.toCodePoint(char2, char3);
                                    final int n12 = n5 + 1;
                                    array[n5] = (byte)(codePoint >>> 18 | 0xF0);
                                    final int n13 = n12 + 1;
                                    array[n12] = (byte)((codePoint >>> 12 & 0x3F) | 0x80);
                                    final int n14 = n13 + 1;
                                    array[n13] = (byte)((codePoint >>> 6 & 0x3F) | 0x80);
                                    n5 = n14 + 1;
                                    array[n14] = (byte)((codePoint & 0x3F) | 0x80);
                                    i = n11;
                                }
                                ++i;
                            }
                            final StringBuilder sb2 = new StringBuilder(39);
                            sb2.append("Unpaired surrogate at index ");
                            sb2.append(i - 1);
                            throw new IllegalArgumentException(sb2.toString());
                        }
                        n6 = n2 + length;
                    }
                    byteBuffer.position(n6 - byteBuffer.arrayOffset());
                    return;
                }
                catch (ArrayIndexOutOfBoundsException ex2) {
                    final BufferOverflowException ex = new BufferOverflowException();
                    ex.initCause(ex2);
                    throw ex;
                }
            }
            for (int length2 = charSequence.length(), j = n; j < length2; ++j) {
                final char char4 = charSequence.charAt(j);
                int n15;
                if (char4 < '\u0080') {
                    n15 = char4;
                }
                else {
                    int n16;
                    if (char4 < '\u0800') {
                        n16 = (char4 >>> 6 | 0x3C0);
                    }
                    else {
                        if (char4 >= '\ud800' && '\udfff' >= char4) {
                            final int n17 = j + 1;
                            if (n17 != charSequence.length()) {
                                final char char5 = charSequence.charAt(n17);
                                if (Character.isSurrogatePair(char4, char5)) {
                                    final int codePoint2 = Character.toCodePoint(char4, char5);
                                    byteBuffer.put((byte)(codePoint2 >>> 18 | 0xF0));
                                    byteBuffer.put((byte)((codePoint2 >>> 12 & 0x3F) | 0x80));
                                    byteBuffer.put((byte)((codePoint2 >>> 6 & 0x3F) | 0x80));
                                    byteBuffer.put((byte)((codePoint2 & 0x3F) | 0x80));
                                    j = n17;
                                    continue;
                                }
                                j = n17;
                            }
                            final StringBuilder sb3 = new StringBuilder(39);
                            sb3.append("Unpaired surrogate at index ");
                            sb3.append(j - 1);
                            throw new IllegalArgumentException(sb3.toString());
                        }
                        byteBuffer.put((byte)(char4 >>> 12 | 0x1E0));
                        n16 = ((char4 >>> 6 & 0x3F) | 0x80);
                    }
                    byteBuffer.put((byte)n16);
                    n15 = ((char4 & '?') | 0x80);
                }
                byteBuffer.put((byte)n15);
            }
            return;
        }
        throw new ReadOnlyBufferException();
    }
    
    public static int zzfx(final String s) {
        final int zza = zza(s);
        return zzbj(zza) + zza;
    }
    
    public static int zzh(final int n, final int n2) {
        return zzbb(n) + zzbc(n2);
    }
    
    public static zzyy zzk(final byte[] array, final int n, final int n2) {
        return new zzyy(array, 0, n2);
    }
    
    public static zzyy zzo(final byte[] array) {
        return zzk(array, 0, array.length);
    }
    
    private final zzut zzys() throws IOException {
        if (this.zzcfa == null) {
            this.zzcfa = zzut.zza(this.zzbva);
        }
        else {
            if (this.zzcfb == this.zzbva.position()) {
                return this.zzcfa;
            }
            this.zzcfa.write(this.zzbva.array(), this.zzcfb, this.zzbva.position() - this.zzcfb);
        }
        this.zzcfb = this.zzbva.position();
        return this.zzcfa;
    }
    
    public final void zza(final int n, final double n2) throws IOException {
        this.zzc(n, 1);
        final long doubleToLongBits = Double.doubleToLongBits(n2);
        if (this.zzbva.remaining() >= 8) {
            this.zzbva.putLong(doubleToLongBits);
            return;
        }
        throw new zzyz(this.zzbva.position(), this.zzbva.limit());
    }
    
    public final void zza(int floatToIntBits, final float n) throws IOException {
        this.zzc(floatToIntBits, 5);
        floatToIntBits = Float.floatToIntBits(n);
        if (this.zzbva.remaining() >= 4) {
            this.zzbva.putInt(floatToIntBits);
            return;
        }
        throw new zzyz(this.zzbva.position(), this.zzbva.limit());
    }
    
    public final void zza(final int n, final long n2) throws IOException {
        this.zzc(n, 0);
        this.zzbh(n2);
    }
    
    public final void zza(final int n, final zzzg zzzg) throws IOException {
        this.zzc(n, 2);
        this.zzb(zzzg);
    }
    
    public final void zzb(int zzbj, final String s) throws IOException {
        this.zzc(zzbj, 2);
        try {
            zzbj = zzbj(s.length());
            if (zzbj != zzbj(s.length() * 3)) {
                this.zzca(zza(s));
                zzd(s, this.zzbva);
                return;
            }
            final int position = this.zzbva.position();
            if (this.zzbva.remaining() >= zzbj) {
                this.zzbva.position(position + zzbj);
                zzd(s, this.zzbva);
                final int position2 = this.zzbva.position();
                this.zzbva.position(position);
                this.zzca(position2 - position - zzbj);
                this.zzbva.position(position2);
                return;
            }
            throw new zzyz(position + zzbj, this.zzbva.limit());
        }
        catch (BufferOverflowException ex) {
            final zzyz zzyz = new zzyz(this.zzbva.position(), this.zzbva.limit());
            zzyz.initCause(ex);
            throw zzyz;
        }
    }
    
    public final void zzb(final int n, final boolean b) throws IOException {
        this.zzc(n, 0);
        final byte b2 = (byte)(b ? 1 : 0);
        if (this.zzbva.hasRemaining()) {
            this.zzbva.put(b2);
            return;
        }
        throw new zzyz(this.zzbva.position(), this.zzbva.limit());
    }
    
    public final void zzb(final zzzg zzzg) throws IOException {
        this.zzca(zzzg.zzza());
        zzzg.zza(this);
    }
    
    public final void zzc(final int n, final int n2) throws IOException {
        this.zzca(n << 3 | n2);
    }
    
    public final void zzca(int n) throws IOException {
        while ((n & 0xFFFFFF80) != 0x0) {
            this.zzbz((n & 0x7F) | 0x80);
            n >>>= 7;
        }
        this.zzbz(n);
    }
    
    public final void zzd(final int n, final int n2) throws IOException {
        this.zzc(n, 0);
        if (n2 >= 0) {
            this.zzca(n2);
            return;
        }
        this.zzbh(n2);
    }
    
    public final void zze(final int n, final zzwt zzwt) throws IOException {
        final zzut zzys = this.zzys();
        zzys.zza(n, zzwt);
        zzys.flush();
        this.zzcfb = this.zzbva.position();
    }
    
    public final void zzi(final int n, final long n2) throws IOException {
        this.zzc(n, 0);
        this.zzbh(n2);
    }
    
    public final void zzp(final byte[] array) throws IOException {
        final int length = array.length;
        if (this.zzbva.remaining() >= length) {
            this.zzbva.put(array, 0, length);
            return;
        }
        throw new zzyz(this.zzbva.position(), this.zzbva.limit());
    }
    
    public final void zzyt() {
        if (this.zzbva.remaining() == 0) {
            return;
        }
        throw new IllegalStateException(String.format("Did not write as much data as expected, %s bytes remaining.", this.zzbva.remaining()));
    }
}
