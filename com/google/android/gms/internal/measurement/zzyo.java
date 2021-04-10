package com.google.android.gms.internal.measurement;

import java.nio.*;

final class zzyo extends zzyl
{
    private static int zza(final byte[] array, final int n, final long n2, final int n3) {
        if (n3 == 0) {
            return zzbw(n);
        }
        if (n3 == 1) {
            return zzq(n, zzyh.zza(array, n2));
        }
        if (n3 == 2) {
            return zzc(n, zzyh.zza(array, n2), zzyh.zza(array, n2 + 1L));
        }
        throw new AssertionError();
    }
    
    @Override
    final int zzb(int i, final byte[] array, int zza, int n) {
        if ((zza | n | array.length - n) >= 0) {
            final long n2 = zza;
            zza = (int)(n - n2);
            Label_0074: {
                if (zza < 16) {
                    i = 0;
                }
                else {
                    long n3;
                    for (n3 = n2, i = 0; i < zza; ++i, ++n3) {
                        if (zzyh.zza(array, n3) < 0) {
                            break Label_0074;
                        }
                    }
                    i = zza;
                }
            }
            zza -= i;
            long n4 = n2 + i;
            i = zza;
            while (true) {
                zza = 0;
                long n5;
                while (true) {
                    n5 = n4;
                    if (i <= 0) {
                        break;
                    }
                    n5 = n4 + 1L;
                    zza = zzyh.zza(array, n4);
                    if (zza < 0) {
                        break;
                    }
                    --i;
                    n4 = n5;
                }
                if (i == 0) {
                    return 0;
                }
                --i;
                if (zza < -32) {
                    if (i == 0) {
                        return zza;
                    }
                    --i;
                    if (zza < -62) {
                        return -1;
                    }
                    if (zzyh.zza(array, n5) > -65) {
                        return -1;
                    }
                    n4 = n5 + 1L;
                }
                else if (zza < -16) {
                    if (i < 2) {
                        return zza(array, zza, n5, i);
                    }
                    i -= 2;
                    final long n6 = n5 + 1L;
                    n = zzyh.zza(array, n5);
                    if (n > -65 || (zza == -32 && n < -96) || (zza == -19 && n >= -96)) {
                        break;
                    }
                    n4 = n6 + 1L;
                    if (zzyh.zza(array, n6) > -65) {
                        break;
                    }
                    continue;
                }
                else {
                    if (i < 3) {
                        return zza(array, zza, n5, i);
                    }
                    i -= 3;
                    final long n7 = n5 + 1L;
                    n = zzyh.zza(array, n5);
                    if (n > -65 || (zza << 28) + (n + 112) >> 30 != 0) {
                        return -1;
                    }
                    final long n8 = n7 + 1L;
                    if (zzyh.zza(array, n7) > -65) {
                        return -1;
                    }
                    n4 = n8 + 1L;
                    if (zzyh.zza(array, n8) > -65) {
                        return -1;
                    }
                    continue;
                }
            }
            return -1;
        }
        throw new ArrayIndexOutOfBoundsException(String.format("Array length=%d, index=%d, limit=%d", array.length, zza, n));
    }
    
    @Override
    final int zzb(final CharSequence charSequence, final byte[] array, int i, int j) {
        long n = i;
        final long n2 = j + n;
        final int length = charSequence.length();
        if (length > j || array.length - j < i) {
            final char char1 = charSequence.charAt(length - 1);
            final StringBuilder sb = new StringBuilder(37);
            sb.append("Failed writing ");
            sb.append(char1);
            sb.append(" at index ");
            sb.append(i + j);
            throw new ArrayIndexOutOfBoundsException(sb.toString());
        }
        for (j = 0; j < length; ++j, ++n) {
            i = charSequence.charAt(j);
            if (i >= 128) {
                break;
            }
            zzyh.zza(array, n, (byte)i);
        }
        i = j;
        long n3 = n;
        if (j == length) {
            return (int)n;
        }
        while (i < length) {
            final char char2 = charSequence.charAt(i);
            Label_0461: {
                if (char2 < '\u0080' && n3 < n2) {
                    j = char2;
                }
                else {
                    if (char2 < '\u0800' && n3 <= n2 - 2L) {
                        final long n4 = n3 + 1L;
                        zzyh.zza(array, n3, (byte)(char2 >>> 6 | 0x3C0));
                        zzyh.zza(array, n4, (byte)((char2 & '?') | 0x80));
                        n3 = n4 + 1L;
                        break Label_0461;
                    }
                    if ((char2 < '\ud800' || '\udfff' < char2) && n3 <= n2 - 3L) {
                        final long n5 = n3 + 1L;
                        zzyh.zza(array, n3, (byte)(char2 >>> 12 | 0x1E0));
                        n3 = n5 + 1L;
                        zzyh.zza(array, n5, (byte)((char2 >>> 6 & 0x3F) | 0x80));
                        j = ((char2 & '?') | 0x80);
                    }
                    else {
                        if (n3 <= n2 - 4L) {
                            j = i + 1;
                            if (j != length) {
                                final char char3 = charSequence.charAt(j);
                                i = j;
                                if (Character.isSurrogatePair(char2, char3)) {
                                    i = Character.toCodePoint(char2, char3);
                                    final long n6 = n3 + 1L;
                                    zzyh.zza(array, n3, (byte)(i >>> 18 | 0xF0));
                                    final long n7 = n6 + 1L;
                                    zzyh.zza(array, n6, (byte)((i >>> 12 & 0x3F) | 0x80));
                                    final long n8 = n7 + 1L;
                                    zzyh.zza(array, n7, (byte)((i >>> 6 & 0x3F) | 0x80));
                                    n3 = n8 + 1L;
                                    zzyh.zza(array, n8, (byte)((i & 0x3F) | 0x80));
                                    i = j;
                                    break Label_0461;
                                }
                            }
                            throw new zzyn(i - 1, length);
                        }
                        if ('\ud800' <= char2 && char2 <= '\udfff') {
                            j = i + 1;
                            if (j == length || !Character.isSurrogatePair(char2, charSequence.charAt(j))) {
                                throw new zzyn(i, length);
                            }
                        }
                        final StringBuilder sb2 = new StringBuilder(46);
                        sb2.append("Failed writing ");
                        sb2.append(char2);
                        sb2.append(" at index ");
                        sb2.append(n3);
                        throw new ArrayIndexOutOfBoundsException(sb2.toString());
                    }
                }
                zzyh.zza(array, n3, (byte)j);
                ++n3;
            }
            ++i;
        }
        return (int)n3;
    }
    
    @Override
    final void zzb(final CharSequence charSequence, final ByteBuffer byteBuffer) {
        final long zzb = zzyh.zzb(byteBuffer);
        long n = byteBuffer.position() + zzb;
        final long n2 = byteBuffer.limit() + zzb;
        final int length = charSequence.length();
        if (length <= n2 - n) {
            int n3 = 0;
            long n4;
            while (true) {
                n4 = 1L;
                if (n3 >= length) {
                    break;
                }
                final char char1 = charSequence.charAt(n3);
                if (char1 >= '\u0080') {
                    break;
                }
                zzyh.zza(n, (byte)char1);
                ++n3;
                ++n;
            }
            int i = n3;
            long n5 = n;
            if (n3 != length) {
                while (i < length) {
                    final char char2 = charSequence.charAt(i);
                    long n7 = 0L;
                    long n8 = 0L;
                    Label_0488: {
                        if (char2 < '\u0080' && n5 < n2) {
                            zzyh.zza(n5, (byte)char2);
                            final long n6 = n4;
                            n7 = n5 + n4;
                            n8 = n6;
                        }
                        else {
                            if (char2 < '\u0800' && n5 <= n2 - 2L) {
                                final long n9 = n5 + n4;
                                zzyh.zza(n5, (byte)(char2 >>> 6 | 0x3C0));
                                zzyh.zza(n9, (byte)((char2 & '?') | 0x80));
                                n7 = n9 + 1L;
                            }
                            else if ((char2 < '\ud800' || '\udfff' < char2) && n5 <= n2 - 3L) {
                                final long n10 = n5 + 1L;
                                zzyh.zza(n5, (byte)(char2 >>> 12 | 0x1E0));
                                final long n11 = n10 + 1L;
                                zzyh.zza(n10, (byte)((char2 >>> 6 & 0x3F) | 0x80));
                                zzyh.zza(n11, (byte)((char2 & '?') | 0x80));
                                n7 = n11 + 1L;
                            }
                            else {
                                if (n5 <= n2 - 4L) {
                                    final int n12 = i + 1;
                                    if (n12 != length) {
                                        final char char3 = charSequence.charAt(n12);
                                        if (Character.isSurrogatePair(char2, char3)) {
                                            final int codePoint = Character.toCodePoint(char2, char3);
                                            final long n13 = n5 + 1L;
                                            zzyh.zza(n5, (byte)(codePoint >>> 18 | 0xF0));
                                            final long n14 = n13 + 1L;
                                            zzyh.zza(n13, (byte)((codePoint >>> 12 & 0x3F) | 0x80));
                                            final long n15 = n14 + 1L;
                                            zzyh.zza(n14, (byte)((codePoint >>> 6 & 0x3F) | 0x80));
                                            zzyh.zza(n15, (byte)((codePoint & 0x3F) | 0x80));
                                            n8 = 1L;
                                            n7 = n15 + 1L;
                                            i = n12;
                                            break Label_0488;
                                        }
                                        i = n12;
                                    }
                                    throw new zzyn(i - 1, length);
                                }
                                if ('\ud800' <= char2 && char2 <= '\udfff') {
                                    final int n16 = i + 1;
                                    if (n16 == length || !Character.isSurrogatePair(char2, charSequence.charAt(n16))) {
                                        throw new zzyn(i, length);
                                    }
                                }
                                final StringBuilder sb = new StringBuilder(46);
                                sb.append("Failed writing ");
                                sb.append(char2);
                                sb.append(" at index ");
                                sb.append(n5);
                                throw new ArrayIndexOutOfBoundsException(sb.toString());
                            }
                            n8 = 1L;
                        }
                    }
                    ++i;
                    n4 = n8;
                    n5 = n7;
                }
                n = n5;
            }
            byteBuffer.position((int)(n - zzb));
            return;
        }
        final char char4 = charSequence.charAt(length - 1);
        final int limit = byteBuffer.limit();
        final StringBuilder sb2 = new StringBuilder(37);
        sb2.append("Failed writing ");
        sb2.append(char4);
        sb2.append(" at index ");
        sb2.append(limit);
        throw new ArrayIndexOutOfBoundsException(sb2.toString());
    }
    
    @Override
    final String zzh(final byte[] array, int i, int j) throws zzvt {
        if ((i | j | array.length - i - j) >= 0) {
            final int n = i + j;
            final char[] array2 = new char[j];
            j = 0;
            while (i < n) {
                final byte zza = zzyh.zza(array, i);
                if (!zzd(zza)) {
                    break;
                }
                ++i;
                zza(zza, array2, j);
                ++j;
            }
            final int n2 = j;
            j = i;
            i = n2;
            while (j < n) {
                int n3 = j + 1;
                final byte zza2 = zzyh.zza(array, j);
                if (zzd(zza2)) {
                    zza(zza2, array2, i);
                    int n4 = i + 1;
                    while (true) {
                        i = n4;
                        j = n3;
                        if (n3 >= n) {
                            break;
                        }
                        final byte zza3 = zzyh.zza(array, n3);
                        i = n4;
                        j = n3;
                        if (!zzd(zza3)) {
                            break;
                        }
                        ++n3;
                        zza(zza3, array2, n4);
                        ++n4;
                    }
                }
                else {
                    if (zze(zza2)) {
                        if (n3 >= n) {
                            throw zzvt.zzwr();
                        }
                        zza(zza2, zzyh.zza(array, n3), array2, i);
                        j = n3 + 1;
                    }
                    else if (zzf(zza2)) {
                        if (n3 >= n - 1) {
                            throw zzvt.zzwr();
                        }
                        j = n3 + 1;
                        zza(zza2, zzyh.zza(array, n3), zzyh.zza(array, j), array2, i);
                        ++j;
                    }
                    else {
                        if (n3 < n - 2) {
                            j = n3 + 1;
                            final byte zza4 = zzyh.zza(array, n3);
                            final int n5 = j + 1;
                            zza(zza2, zza4, zzyh.zza(array, j), zzyh.zza(array, n5), array2, i);
                            j = n5 + 1;
                            i = i + 1 + 1;
                            continue;
                        }
                        throw zzvt.zzwr();
                    }
                    ++i;
                }
            }
            return new String(array2, 0, i);
        }
        throw new ArrayIndexOutOfBoundsException(String.format("buffer length=%d, index=%d, size=%d", array.length, i, j));
    }
}
