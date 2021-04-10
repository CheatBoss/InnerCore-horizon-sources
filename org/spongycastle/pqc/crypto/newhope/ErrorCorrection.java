package org.spongycastle.pqc.crypto.newhope;

import org.spongycastle.util.*;

class ErrorCorrection
{
    static short LDDecode(final int n, final int n2, final int n3, final int n4) {
        return (short)(g(n) + g(n2) + g(n3) + g(n4) - 98312 >>> 31);
    }
    
    static int abs(final int n) {
        final int n2 = n >> 31;
        return (n ^ n2) - n2;
    }
    
    static int f(final int[] array, final int n, final int n2, final int n3) {
        final int n4 = n3 * 2730 >> 25;
        final int n5 = n4 - (12288 - (n3 - n4 * 12289) >> 31);
        array[n] = (n5 >> 1) + (n5 & 0x1);
        final int n6 = n5 - 1;
        array[n2] = (n6 >> 1) + (n6 & 0x1);
        return abs(n3 - array[n] * 2 * 12289);
    }
    
    static int g(final int n) {
        final int n2 = n * 2730 >> 27;
        final int n3 = n2 - (49155 - (n - 49156 * n2) >> 31);
        return abs(((n3 >> 1) + (n3 & 0x1)) * 98312 - n);
    }
    
    static void helpRec(final short[] array, final short[] array2, final byte[] array3, final byte b) {
        final byte[] array4 = new byte[8];
        array4[0] = b;
        final byte[] array5 = new byte[32];
        ChaCha20.process(array3, array4, array5, 0, 32);
        final int[] array6 = new int[8];
        final int[] array7 = new int[4];
        for (int i = 0; i < 256; ++i) {
            final byte b2 = array5[i >>> 3];
            final int n = i + 0;
            final short n2 = array2[n];
            final int n3 = (b2 >>> (i & 0x7) & 0x1) * 4;
            final int f = f(array6, 0, 4, n2 * 8 + n3);
            final int n4 = i + 256;
            final int f2 = f(array6, 1, 5, array2[n4] * 8 + n3);
            final int n5 = i + 512;
            final int f3 = f(array6, 2, 6, array2[n5] * 8 + n3);
            final int n6 = i + 768;
            final int n7 = 24577 - (f + f2 + f3 + f(array6, 3, 7, array2[n6] * 8 + n3)) >> 31;
            final int n8 = ~n7;
            array7[0] = ((n8 & array6[0]) ^ (n7 & array6[4]));
            array7[1] = ((n8 & array6[1]) ^ (array6[5] & n7));
            array7[2] = ((array6[2] & n8) ^ (array6[6] & n7));
            array7[3] = ((array6[7] & n7) ^ (n8 & array6[3]));
            array[n] = (short)(array7[0] - array7[3] & 0x3);
            array[n4] = (short)(array7[1] - array7[3] & 0x3);
            array[n5] = (short)(array7[2] - array7[3] & 0x3);
            array[n6] = (short)(-n7 + array7[3] * 2 & 0x3);
        }
    }
    
    static void rec(final byte[] array, final short[] array2, final short[] array3) {
        Arrays.fill(array, (byte)0);
        final int[] array4 = new int[4];
        for (int i = 0; i < 256; ++i) {
            final int n = i + 0;
            final short n2 = array2[n];
            final short n3 = array3[n];
            final int n4 = i + 768;
            array4[0] = n2 * 8 + 196624 - (n3 * 2 + array3[n4]) * 12289;
            final int n5 = i + 256;
            array4[1] = array2[n5] * 8 + 196624 - (array3[n5] * 2 + array3[n4]) * 12289;
            final int n6 = i + 512;
            array4[2] = array2[n6] * 8 + 196624 - (array3[n6] * 2 + array3[n4]) * 12289;
            array4[3] = array2[n4] * 8 + 196624 - array3[n4] * 12289;
            final int n7 = i >>> 3;
            array[n7] |= (byte)(LDDecode(array4[0], array4[1], array4[2], array4[3]) << (i & 0x7));
        }
    }
}
