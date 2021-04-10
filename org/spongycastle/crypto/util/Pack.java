package org.spongycastle.crypto.util;

public abstract class Pack
{
    public static int bigEndianToInt(final byte[] array, int n) {
        final byte b = array[n];
        final int n2 = n + 1;
        n = array[n2];
        final int n3 = n2 + 1;
        return (array[n3 + 1] & 0xFF) | (b << 24 | (n & 0xFF) << 16 | (array[n3] & 0xFF) << 8);
    }
    
    public static void bigEndianToInt(final byte[] array, int i, final int[] array2) {
        final int n = 0;
        int n2 = i;
        for (i = n; i < array2.length; ++i) {
            array2[i] = bigEndianToInt(array, n2);
            n2 += 4;
        }
    }
    
    public static long bigEndianToLong(final byte[] array, int bigEndianToInt) {
        final int bigEndianToInt2 = bigEndianToInt(array, bigEndianToInt);
        bigEndianToInt = bigEndianToInt(array, bigEndianToInt + 4);
        return ((long)bigEndianToInt & 0xFFFFFFFFL) | ((long)bigEndianToInt2 & 0xFFFFFFFFL) << 32;
    }
    
    public static void bigEndianToLong(final byte[] array, int i, final long[] array2) {
        final int n = 0;
        int n2 = i;
        for (i = n; i < array2.length; ++i) {
            array2[i] = bigEndianToLong(array, n2);
            n2 += 8;
        }
    }
    
    public static void intToBigEndian(final int n, final byte[] array, int n2) {
        array[n2] = (byte)(n >>> 24);
        ++n2;
        array[n2] = (byte)(n >>> 16);
        ++n2;
        array[n2] = (byte)(n >>> 8);
        array[n2 + 1] = (byte)n;
    }
    
    public static void intToBigEndian(final int[] array, final byte[] array2, int i) {
        final int n = 0;
        int n2 = i;
        for (i = n; i < array.length; ++i) {
            intToBigEndian(array[i], array2, n2);
            n2 += 4;
        }
    }
    
    public static byte[] intToBigEndian(final int n) {
        final byte[] array = new byte[4];
        intToBigEndian(n, array, 0);
        return array;
    }
    
    public static byte[] intToBigEndian(final int[] array) {
        final byte[] array2 = new byte[array.length * 4];
        intToBigEndian(array, array2, 0);
        return array2;
    }
    
    public static void intToLittleEndian(final int n, final byte[] array, int n2) {
        array[n2] = (byte)n;
        ++n2;
        array[n2] = (byte)(n >>> 8);
        ++n2;
        array[n2] = (byte)(n >>> 16);
        array[n2 + 1] = (byte)(n >>> 24);
    }
    
    public static void intToLittleEndian(final int[] array, final byte[] array2, int i) {
        final int n = 0;
        int n2 = i;
        for (i = n; i < array.length; ++i) {
            intToLittleEndian(array[i], array2, n2);
            n2 += 4;
        }
    }
    
    public static byte[] intToLittleEndian(final int n) {
        final byte[] array = new byte[4];
        intToLittleEndian(n, array, 0);
        return array;
    }
    
    public static byte[] intToLittleEndian(final int[] array) {
        final byte[] array2 = new byte[array.length * 4];
        intToLittleEndian(array, array2, 0);
        return array2;
    }
    
    public static int littleEndianToInt(final byte[] array, int n) {
        final byte b = array[n];
        final int n2 = n + 1;
        n = array[n2];
        final int n3 = n2 + 1;
        return array[n3 + 1] << 24 | ((b & 0xFF) | (n & 0xFF) << 8 | (array[n3] & 0xFF) << 16);
    }
    
    public static void littleEndianToInt(final byte[] array, int i, final int[] array2) {
        final int n = 0;
        int n2 = i;
        for (i = n; i < array2.length; ++i) {
            array2[i] = littleEndianToInt(array, n2);
            n2 += 4;
        }
    }
    
    public static void littleEndianToInt(final byte[] array, int i, final int[] array2, final int n, final int n2) {
        final int n3 = 0;
        int n4 = i;
        for (i = n3; i < n2; ++i) {
            array2[n + i] = littleEndianToInt(array, n4);
            n4 += 4;
        }
    }
    
    public static long littleEndianToLong(final byte[] array, final int n) {
        return ((long)littleEndianToInt(array, n + 4) & 0xFFFFFFFFL) << 32 | ((long)littleEndianToInt(array, n) & 0xFFFFFFFFL);
    }
    
    public static void littleEndianToLong(final byte[] array, int i, final long[] array2) {
        final int n = 0;
        int n2 = i;
        for (i = n; i < array2.length; ++i) {
            array2[i] = littleEndianToLong(array, n2);
            n2 += 8;
        }
    }
    
    public static void longToBigEndian(final long n, final byte[] array, final int n2) {
        intToBigEndian((int)(n >>> 32), array, n2);
        intToBigEndian((int)(n & 0xFFFFFFFFL), array, n2 + 4);
    }
    
    public static void longToBigEndian(final long[] array, final byte[] array2, int i) {
        final int n = 0;
        int n2 = i;
        for (i = n; i < array.length; ++i) {
            longToBigEndian(array[i], array2, n2);
            n2 += 8;
        }
    }
    
    public static byte[] longToBigEndian(final long n) {
        final byte[] array = new byte[8];
        longToBigEndian(n, array, 0);
        return array;
    }
    
    public static byte[] longToBigEndian(final long[] array) {
        final byte[] array2 = new byte[array.length * 8];
        longToBigEndian(array, array2, 0);
        return array2;
    }
    
    public static void longToLittleEndian(final long n, final byte[] array, final int n2) {
        intToLittleEndian((int)(n & 0xFFFFFFFFL), array, n2);
        intToLittleEndian((int)(n >>> 32), array, n2 + 4);
    }
    
    public static void longToLittleEndian(final long[] array, final byte[] array2, int i) {
        final int n = 0;
        int n2 = i;
        for (i = n; i < array.length; ++i) {
            longToLittleEndian(array[i], array2, n2);
            n2 += 8;
        }
    }
    
    public static byte[] longToLittleEndian(final long n) {
        final byte[] array = new byte[8];
        longToLittleEndian(n, array, 0);
        return array;
    }
    
    public static byte[] longToLittleEndian(final long[] array) {
        final byte[] array2 = new byte[array.length * 8];
        longToLittleEndian(array, array2, 0);
        return array2;
    }
}
