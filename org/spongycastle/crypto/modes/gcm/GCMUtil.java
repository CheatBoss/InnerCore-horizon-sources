package org.spongycastle.crypto.modes.gcm;

import org.spongycastle.util.*;

public abstract class GCMUtil
{
    private static final int E1 = -520093696;
    private static final long E1L = -2233785415175766016L;
    private static final int[] LOOKUP;
    
    static {
        LOOKUP = generateLookup();
    }
    
    public static void asBytes(final int[] array, final byte[] array2) {
        Pack.intToBigEndian(array, array2, 0);
    }
    
    public static void asBytes(final long[] array, final byte[] array2) {
        Pack.longToBigEndian(array, array2, 0);
    }
    
    public static byte[] asBytes(final int[] array) {
        final byte[] array2 = new byte[16];
        Pack.intToBigEndian(array, array2, 0);
        return array2;
    }
    
    public static byte[] asBytes(final long[] array) {
        final byte[] array2 = new byte[16];
        Pack.longToBigEndian(array, array2, 0);
        return array2;
    }
    
    public static void asInts(final byte[] array, final int[] array2) {
        Pack.bigEndianToInt(array, 0, array2);
    }
    
    public static int[] asInts(final byte[] array) {
        final int[] array2 = new int[4];
        Pack.bigEndianToInt(array, 0, array2);
        return array2;
    }
    
    public static void asLongs(final byte[] array, final long[] array2) {
        Pack.bigEndianToLong(array, 0, array2);
    }
    
    public static long[] asLongs(final byte[] array) {
        final long[] array2 = new long[2];
        Pack.bigEndianToLong(array, 0, array2);
        return array2;
    }
    
    private static int[] generateLookup() {
        final int[] array = new int[256];
        for (int i = 0; i < 256; ++i) {
            int j = 7;
            int n = 0;
            while (j >= 0) {
                int n2 = n;
                if ((1 << j & i) != 0x0) {
                    n2 = (n ^ -520093696 >>> 7 - j);
                }
                --j;
                n = n2;
            }
            array[i] = n;
        }
        return array;
    }
    
    public static void multiply(final byte[] array, final byte[] array2) {
        final int[] ints = asInts(array);
        multiply(ints, asInts(array2));
        asBytes(ints, array);
    }
    
    public static void multiply(final int[] array, final int[] array2) {
        int n = array[0];
        int n2 = array[1];
        int n3 = array[2];
        int n4 = array[3];
        int i = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        while (i < 4) {
            int n9 = array2[i];
            final int n10 = n6;
            final int n11 = n5;
            final int n12 = 0;
            int n13 = n4;
            int n14 = n3;
            int n15 = n8;
            int n16 = n7;
            int n17 = n10;
            int n18 = n11;
            int n19 = n12;
            int n20;
            while (true) {
                n20 = n14;
                if (n19 >= 32) {
                    break;
                }
                final int n21 = n9 >> 31;
                n9 <<= 1;
                n18 ^= (n & n21);
                n17 ^= (n2 & n21);
                n16 ^= (n20 & n21);
                n15 ^= (n21 & n13);
                n14 = (n20 >>> 1 | n2 << 31);
                n2 = (n2 >>> 1 | n << 31);
                n = (n >>> 1 ^ (n13 << 31 >> 8 & 0xE1000000));
                ++n19;
                n13 = (n13 >>> 1 | n20 << 31);
            }
            ++i;
            final int n22 = n16;
            final int n23 = n13;
            n5 = n18;
            n6 = n17;
            n7 = n22;
            n8 = n15;
            n4 = n23;
            n3 = n20;
        }
        array[0] = n5;
        array[1] = n6;
        array[2] = n7;
        array[3] = n8;
    }
    
    public static void multiply(final long[] array, final long[] array2) {
        long n = array[0];
        long n2 = array[1];
        long n3 = 0L;
        long n4 = 0L;
        long n8;
        long n10;
        long n11;
        for (int i = 0; i < 2; ++i, n2 = n8, n4 = n10, n = n11) {
            long n5 = array2[i];
            final long n6 = n4;
            int n7 = 0;
            n8 = n2;
            long n9 = n;
            n10 = n6;
            while (true) {
                n11 = n9;
                if (n7 >= 64) {
                    break;
                }
                final long n12 = n5 >> 63;
                n5 <<= 1;
                n9 = (n11 >>> 1 ^ (n8 << 63 >> 8 & 0xE100000000000000L));
                ++n7;
                n10 ^= (n12 & n8);
                n8 = (n8 >>> 1 | n11 << 63);
                n3 ^= (n11 & n12);
            }
        }
        array[0] = n3;
        array[1] = n4;
    }
    
    public static void multiplyP(final int[] array) {
        array[0] ^= (shiftRight(array) >> 8 & 0xE1000000);
    }
    
    public static void multiplyP(final int[] array, final int[] array2) {
        array2[0] ^= (shiftRight(array, array2) >> 8 & 0xE1000000);
    }
    
    public static void multiplyP8(final int[] array) {
        array[0] ^= GCMUtil.LOOKUP[shiftRightN(array, 8) >>> 24];
    }
    
    public static void multiplyP8(final int[] array, final int[] array2) {
        array2[0] ^= GCMUtil.LOOKUP[shiftRightN(array, 8, array2) >>> 24];
    }
    
    public static byte[] oneAsBytes() {
        final byte[] array = new byte[16];
        array[0] = -128;
        return array;
    }
    
    public static int[] oneAsInts() {
        final int[] array = new int[4];
        array[0] = Integer.MIN_VALUE;
        return array;
    }
    
    public static long[] oneAsLongs() {
        return new long[] { Long.MIN_VALUE, 0L };
    }
    
    static int shiftRight(final int[] array) {
        final int n = array[0];
        array[0] = n >>> 1;
        final int n2 = array[1];
        array[1] = (n << 31 | n2 >>> 1);
        final int n3 = array[2];
        array[2] = (n2 << 31 | n3 >>> 1);
        final int n4 = array[3];
        array[3] = (n3 << 31 | n4 >>> 1);
        return n4 << 31;
    }
    
    static int shiftRight(final int[] array, final int[] array2) {
        final int n = array[0];
        array2[0] = n >>> 1;
        final int n2 = array[1];
        array2[1] = (n << 31 | n2 >>> 1);
        final int n3 = array[2];
        array2[2] = (n2 << 31 | n3 >>> 1);
        final int n4 = array[3];
        array2[3] = (n3 << 31 | n4 >>> 1);
        return n4 << 31;
    }
    
    static long shiftRight(final long[] array) {
        final long n = array[0];
        array[0] = n >>> 1;
        final long n2 = array[1];
        array[1] = (n << 63 | n2 >>> 1);
        return n2 << 63;
    }
    
    static long shiftRight(final long[] array, final long[] array2) {
        final long n = array[0];
        array2[0] = n >>> 1;
        final long n2 = array[1];
        array2[1] = (n << 63 | n2 >>> 1);
        return n2 << 63;
    }
    
    static int shiftRightN(final int[] array, final int n) {
        final int n2 = array[0];
        final int n3 = 32 - n;
        array[0] = n2 >>> n;
        final int n4 = array[1];
        array[1] = (n2 << n3 | n4 >>> n);
        final int n5 = array[2];
        array[2] = (n4 << n3 | n5 >>> n);
        final int n6 = array[3];
        array[3] = (n6 >>> n | n5 << n3);
        return n6 << n3;
    }
    
    static int shiftRightN(final int[] array, final int n, final int[] array2) {
        final int n2 = array[0];
        final int n3 = 32 - n;
        array2[0] = n2 >>> n;
        final int n4 = array[1];
        array2[1] = (n2 << n3 | n4 >>> n);
        final int n5 = array[2];
        array2[2] = (n4 << n3 | n5 >>> n);
        final int n6 = array[3];
        array2[3] = (n6 >>> n | n5 << n3);
        return n6 << n3;
    }
    
    public static void xor(final byte[] array, final byte[] array2) {
        int n = 0;
        int n2;
        do {
            array[n] ^= array2[n];
            final int n3 = n + 1;
            array[n3] ^= array2[n3];
            final int n4 = n3 + 1;
            array[n4] ^= array2[n4];
            n2 = n4 + 1;
            array[n2] ^= array2[n2];
        } while ((n = n2 + 1) < 16);
    }
    
    public static void xor(final byte[] array, final byte[] array2, final int n, int n2) {
        while (true) {
            --n2;
            if (n2 < 0) {
                break;
            }
            array[n2] ^= array2[n + n2];
        }
    }
    
    public static void xor(final byte[] array, final byte[] array2, final byte[] array3) {
        int n = 0;
        int n2;
        do {
            array3[n] = (byte)(array[n] ^ array2[n]);
            final int n3 = n + 1;
            array3[n3] = (byte)(array[n3] ^ array2[n3]);
            final int n4 = n3 + 1;
            array3[n4] = (byte)(array[n4] ^ array2[n4]);
            n2 = n4 + 1;
            array3[n2] = (byte)(array[n2] ^ array2[n2]);
        } while ((n = n2 + 1) < 16);
    }
    
    public static void xor(final int[] array, final int[] array2) {
        array[0] ^= array2[0];
        array[1] ^= array2[1];
        array[2] ^= array2[2];
        array[3] ^= array2[3];
    }
    
    public static void xor(final int[] array, final int[] array2, final int[] array3) {
        array3[0] = (array[0] ^ array2[0]);
        array3[1] = (array[1] ^ array2[1]);
        array3[2] = (array[2] ^ array2[2]);
        array3[3] = (array[3] ^ array2[3]);
    }
    
    public static void xor(final long[] array, final long[] array2) {
        array[0] ^= array2[0];
        array[1] ^= array2[1];
    }
    
    public static void xor(final long[] array, final long[] array2, final long[] array3) {
        array3[0] = (array[0] ^ array2[0]);
        array3[1] = (array2[1] ^ array[1]);
    }
}
