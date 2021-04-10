package org.spongycastle.pqc.crypto.newhope;

import org.spongycastle.util.*;
import org.spongycastle.crypto.digests.*;

class Poly
{
    static void add(final short[] array, final short[] array2, final short[] array3) {
        for (int i = 0; i < 1024; ++i) {
            array3[i] = Reduce.barrett((short)(array[i] + array2[i]));
        }
    }
    
    static void fromBytes(final short[] array, final byte[] array2) {
        for (int i = 0; i < 256; ++i) {
            final int n = i * 7;
            final byte b = array2[n + 0];
            final int n2 = array2[n + 1] & 0xFF;
            final byte b2 = array2[n + 2];
            final int n3 = array2[n + 3] & 0xFF;
            final byte b3 = array2[n + 4];
            final int n4 = array2[n + 5] & 0xFF;
            final byte b4 = array2[n + 6];
            final int n5 = i * 4;
            array[n5 + 0] = (short)((b & 0xFF) | (n2 & 0x3F) << 8);
            array[n5 + 1] = (short)(n2 >>> 6 | (b2 & 0xFF) << 2 | (n3 & 0xF) << 10);
            array[n5 + 2] = (short)(n3 >>> 4 | (b3 & 0xFF) << 4 | (n4 & 0x3) << 12);
            array[n5 + 3] = (short)((b4 & 0xFF) << 6 | n4 >>> 2);
        }
    }
    
    static void fromNTT(final short[] array) {
        NTT.bitReverse(array);
        NTT.core(array, Precomp.OMEGAS_INV_MONTGOMERY);
        NTT.mulCoefficients(array, Precomp.PSIS_INV_MONTGOMERY);
    }
    
    static void getNoise(final short[] array, final byte[] array2, final byte b) {
        final byte[] array3 = new byte[8];
        array3[0] = b;
        final byte[] array4 = new byte[4096];
        ChaCha20.process(array2, array3, array4, 0, 4096);
        for (int i = 0; i < 1024; ++i) {
            final int bigEndianToInt = Pack.bigEndianToInt(array4, i * 4);
            int j = 0;
            int n = 0;
            while (j < 8) {
                n += (bigEndianToInt >> j & 0x1010101);
                ++j;
            }
            array[i] = (short)(((n >>> 24) + (n >>> 0) & 0xFF) + 12289 - ((n >>> 16) + (n >>> 8) & 0xFF));
        }
    }
    
    private static short normalize(final short n) {
        final short barrett = Reduce.barrett(n);
        final int n2 = barrett - 12289;
        return (short)(((barrett ^ n2) & n2 >> 31) ^ n2);
    }
    
    static void pointWise(final short[] array, final short[] array2, final short[] array3) {
        for (int i = 0; i < 1024; ++i) {
            array3[i] = Reduce.montgomery((array[i] & 0xFFFF) * (0xFFFF & Reduce.montgomery((array2[i] & 0xFFFF) * 3186)));
        }
    }
    
    static void toBytes(final byte[] array, final short[] array2) {
        for (int i = 0; i < 256; ++i) {
            final int n = i * 4;
            final short normalize = normalize(array2[n + 0]);
            final short normalize2 = normalize(array2[n + 1]);
            final short normalize3 = normalize(array2[n + 2]);
            final short normalize4 = normalize(array2[n + 3]);
            final int n2 = i * 7;
            array[n2 + 0] = (byte)normalize;
            array[n2 + 1] = (byte)(normalize >> 8 | normalize2 << 6);
            array[n2 + 2] = (byte)(normalize2 >> 2);
            array[n2 + 3] = (byte)(normalize2 >> 10 | normalize3 << 4);
            array[n2 + 4] = (byte)(normalize3 >> 4);
            array[n2 + 5] = (byte)(normalize3 >> 12 | normalize4 << 2);
            array[n2 + 6] = (byte)(normalize4 >> 6);
        }
    }
    
    static void toNTT(final short[] array) {
        NTT.mulCoefficients(array, Precomp.PSIS_BITREV_MONTGOMERY);
        NTT.core(array, Precomp.OMEGAS_MONTGOMERY);
    }
    
    static void uniform(final short[] array, byte[] array2) {
        final SHAKEDigest shakeDigest = new SHAKEDigest(128);
        shakeDigest.update(array2, 0, array2.length);
        int n = 0;
    Block_3:
        while (true) {
            array2 = new byte[256];
            shakeDigest.doOutput(array2, 0, 256);
            int n2 = 0;
            int n3 = n;
            while (true) {
                n = n3;
                if (n2 >= 256) {
                    break;
                }
                final int n4 = ((array2[n2] & 0xFF) | (array2[n2 + 1] & 0xFF) << 8) & 0x3FFF;
                int n5 = n3;
                if (n4 < 12289) {
                    n5 = n3 + 1;
                    array[n3] = (short)n4;
                    if (n5 == 1024) {
                        break Block_3;
                    }
                }
                n2 += 2;
                n3 = n5;
            }
        }
    }
}
