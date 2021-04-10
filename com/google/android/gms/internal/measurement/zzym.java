package com.google.android.gms.internal.measurement;

import java.nio.*;

final class zzym extends zzyl
{
    @Override
    final int zzb(int i, final byte[] array, int n, final int n2) {
        while (n < n2 && array[n] >= 0) {
            ++n;
        }
        if ((i = n) >= n2) {
            return 0;
        }
        while (i < n2) {
            n = i + 1;
            final byte b = array[i];
            i = n;
            if (b < 0) {
                if (b < -32) {
                    if (n >= n2) {
                        return b;
                    }
                    if (b >= -62) {
                        i = n + 1;
                        if (array[n] <= -65) {
                            continue;
                        }
                    }
                    return -1;
                }
                else if (b < -16) {
                    if (n >= n2 - 1) {
                        return zzg(array, n, n2);
                    }
                    final int n3 = n + 1;
                    i = array[n];
                    if (i <= -65 && (b != -32 || i >= -96) && (b != -19 || i < -96)) {
                        i = n3 + 1;
                        if (array[n3] <= -65) {
                            continue;
                        }
                    }
                    return -1;
                }
                else {
                    if (n >= n2 - 2) {
                        return zzg(array, n, n2);
                    }
                    i = n + 1;
                    n = array[n];
                    if (n <= -65 && (b << 28) + (n + 112) >> 30 == 0) {
                        n = i + 1;
                        if (array[i] <= -65) {
                            i = n + 1;
                            if (array[n] <= -65) {
                                continue;
                            }
                        }
                    }
                    return -1;
                }
            }
        }
        return 0;
    }
    
    @Override
    final int zzb(final CharSequence charSequence, final byte[] array, int i, int j) {
        final int length = charSequence.length();
        final int n = j + i;
        int n2;
        char char1;
        for (j = 0; j < length; ++j) {
            n2 = j + i;
            if (n2 >= n) {
                break;
            }
            char1 = charSequence.charAt(j);
            if (char1 >= '\u0080') {
                break;
            }
            array[n2] = (byte)char1;
        }
        if (j == length) {
            return i + length;
        }
        int n3 = i + j;
        char char2;
        int n4;
        int n5;
        char char3;
        int n6;
        int n7;
        int n8;
        int n9;
        StringBuilder sb;
        for (i = j; i < length; ++i, n3 = j) {
            char2 = charSequence.charAt(i);
            if (char2 < '\u0080' && n3 < n) {
                j = n3 + 1;
                array[n3] = (byte)char2;
            }
            else if (char2 < '\u0800' && n3 <= n - 2) {
                n4 = n3 + 1;
                array[n3] = (byte)(char2 >>> 6 | 0x3C0);
                j = n4 + 1;
                array[n4] = (byte)((char2 & '?') | 0x80);
            }
            else if ((char2 < '\ud800' || '\udfff' < char2) && n3 <= n - 3) {
                j = n3 + 1;
                array[n3] = (byte)(char2 >>> 12 | 0x1E0);
                n5 = j + 1;
                array[j] = (byte)((char2 >>> 6 & 0x3F) | 0x80);
                j = n5 + 1;
                array[n5] = (byte)((char2 & '?') | 0x80);
            }
            else {
                if (n3 <= n - 4) {
                    j = i + 1;
                    if (j != charSequence.length()) {
                        char3 = charSequence.charAt(j);
                        if (Character.isSurrogatePair(char2, char3)) {
                            i = Character.toCodePoint(char2, char3);
                            n6 = n3 + 1;
                            array[n3] = (byte)(i >>> 18 | 0xF0);
                            n7 = n6 + 1;
                            array[n6] = (byte)((i >>> 12 & 0x3F) | 0x80);
                            n8 = n7 + 1;
                            array[n7] = (byte)((i >>> 6 & 0x3F) | 0x80);
                            n9 = n8 + 1;
                            array[n8] = (byte)((i & 0x3F) | 0x80);
                            i = j;
                            j = n9;
                            continue;
                        }
                        i = j;
                    }
                    throw new zzyn(i - 1, length);
                }
                if ('\ud800' <= char2 && char2 <= '\udfff') {
                    j = i + 1;
                    if (j == charSequence.length() || !Character.isSurrogatePair(char2, charSequence.charAt(j))) {
                        throw new zzyn(i, length);
                    }
                }
                sb = new StringBuilder(37);
                sb.append("Failed writing ");
                sb.append(char2);
                sb.append(" at index ");
                sb.append(n3);
                throw new ArrayIndexOutOfBoundsException(sb.toString());
            }
        }
        return n3;
    }
    
    @Override
    final void zzb(final CharSequence charSequence, final ByteBuffer byteBuffer) {
        zzyl.zzc(charSequence, byteBuffer);
    }
    
    @Override
    final String zzh(final byte[] array, int i, int j) throws zzvt {
        if ((i | j | array.length - i - j) >= 0) {
            final int n = i + j;
            final char[] array2 = new char[j];
            j = 0;
            while (i < n) {
                final byte b = array[i];
                if (!zzd(b)) {
                    break;
                }
                ++i;
                zza(b, array2, j);
                ++j;
            }
            final int n2 = j;
            j = i;
            i = n2;
            while (j < n) {
                int n3 = j + 1;
                final byte b2 = array[j];
                if (zzd(b2)) {
                    zza(b2, array2, i);
                    int n4 = i + 1;
                    while (true) {
                        i = n4;
                        j = n3;
                        if (n3 >= n) {
                            break;
                        }
                        final byte b3 = array[n3];
                        i = n4;
                        j = n3;
                        if (!zzd(b3)) {
                            break;
                        }
                        ++n3;
                        zza(b3, array2, n4);
                        ++n4;
                    }
                }
                else {
                    if (zze(b2)) {
                        if (n3 >= n) {
                            throw zzvt.zzwr();
                        }
                        zza(b2, array[n3], array2, i);
                        j = n3 + 1;
                    }
                    else if (zzf(b2)) {
                        if (n3 >= n - 1) {
                            throw zzvt.zzwr();
                        }
                        j = n3 + 1;
                        zza(b2, array[n3], array[j], array2, i);
                        ++j;
                    }
                    else {
                        if (n3 < n - 2) {
                            j = n3 + 1;
                            final byte b4 = array[n3];
                            final int n5 = j + 1;
                            zza(b2, b4, array[j], array[n5], array2, i);
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
