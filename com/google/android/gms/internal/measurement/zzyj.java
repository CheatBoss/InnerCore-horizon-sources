package com.google.android.gms.internal.measurement;

import java.nio.*;

final class zzyj
{
    private static final zzyl zzcdp;
    
    static {
        zzyl zzcdp2;
        if (zzyh.zzyi() && zzyh.zzyj() && !zzua.zzty()) {
            zzcdp2 = new zzyo();
        }
        else {
            zzcdp2 = new zzym();
        }
        zzcdp = zzcdp2;
    }
    
    static int zza(final CharSequence charSequence) {
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
                                    throw new zzyn(i, length2);
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
        final StringBuilder sb = new StringBuilder(54);
        sb.append("UTF-8 length does not fit in int: ");
        sb.append(n8 + 4294967296L);
        throw new IllegalArgumentException(sb.toString());
    }
    
    static int zza(final CharSequence charSequence, final byte[] array, final int n, final int n2) {
        return zzyj.zzcdp.zzb(charSequence, array, n, n2);
    }
    
    static void zza(final CharSequence charSequence, final ByteBuffer byteBuffer) {
        final zzyl zzcdp = zzyj.zzcdp;
        if (byteBuffer.hasArray()) {
            final int arrayOffset = byteBuffer.arrayOffset();
            byteBuffer.position(zza(charSequence, byteBuffer.array(), byteBuffer.position() + arrayOffset, byteBuffer.remaining()) - arrayOffset);
            return;
        }
        if (byteBuffer.isDirect()) {
            zzcdp.zzb(charSequence, byteBuffer);
            return;
        }
        zzyl.zzc(charSequence, byteBuffer);
    }
    
    private static int zzbw(final int n) {
        int n2 = n;
        if (n > -12) {
            n2 = -1;
        }
        return n2;
    }
    
    private static int zzc(final int n, final int n2, final int n3) {
        if (n <= -12 && n2 <= -65 && n3 <= -65) {
            return n ^ n2 << 8 ^ n3 << 16;
        }
        return -1;
    }
    
    public static boolean zzf(final byte[] array, final int n, final int n2) {
        return zzyj.zzcdp.zzf(array, n, n2);
    }
    
    private static int zzg(final byte[] array, final int n, int n2) {
        final byte b = array[n - 1];
        n2 -= n;
        if (n2 == 0) {
            return zzbw(b);
        }
        if (n2 == 1) {
            return zzq(b, array[n]);
        }
        if (n2 == 2) {
            return zzc(b, array[n], array[n + 1]);
        }
        throw new AssertionError();
    }
    
    static String zzh(final byte[] array, final int n, final int n2) throws zzvt {
        return zzyj.zzcdp.zzh(array, n, n2);
    }
    
    public static boolean zzl(final byte[] array) {
        return zzyj.zzcdp.zzf(array, 0, array.length);
    }
    
    private static int zzq(final int n, final int n2) {
        if (n <= -12 && n2 <= -65) {
            return n ^ n2 << 8;
        }
        return -1;
    }
}
