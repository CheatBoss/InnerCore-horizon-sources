package org.spongycastle.pqc.math.linearalgebra;

public final class BigEndianConversions
{
    private BigEndianConversions() {
    }
    
    public static void I2OSP(final int n, final byte[] array, int n2) {
        final int n3 = n2 + 1;
        array[n2] = (byte)(n >>> 24);
        n2 = n3 + 1;
        array[n3] = (byte)(n >>> 16);
        array[n2] = (byte)(n >>> 8);
        array[n2 + 1] = (byte)n;
    }
    
    public static void I2OSP(final int n, final byte[] array, final int n2, int i) {
        for (int n3 = --i; i >= 0; --i) {
            array[n2 + i] = (byte)(n >>> (n3 - i) * 8);
        }
    }
    
    public static void I2OSP(final long n, final byte[] array, int n2) {
        final int n3 = n2 + 1;
        array[n2] = (byte)(n >>> 56);
        n2 = n3 + 1;
        array[n3] = (byte)(n >>> 48);
        final int n4 = n2 + 1;
        array[n2] = (byte)(n >>> 40);
        n2 = n4 + 1;
        array[n4] = (byte)(n >>> 32);
        final int n5 = n2 + 1;
        array[n2] = (byte)(n >>> 24);
        n2 = n5 + 1;
        array[n5] = (byte)(n >>> 16);
        array[n2] = (byte)(n >>> 8);
        array[n2 + 1] = (byte)n;
    }
    
    public static byte[] I2OSP(final int n) {
        return new byte[] { (byte)(n >>> 24), (byte)(n >>> 16), (byte)(n >>> 8), (byte)n };
    }
    
    public static byte[] I2OSP(final int n, final int n2) throws ArithmeticException {
        if (n < 0) {
            return null;
        }
        final int ceilLog256 = IntegerFunctions.ceilLog256(n);
        if (ceilLog256 <= n2) {
            final byte[] array = new byte[n2];
            int i;
            for (int n3 = i = n2 - 1; i >= n2 - ceilLog256; --i) {
                array[i] = (byte)(n >>> (n3 - i) * 8);
            }
            return array;
        }
        throw new ArithmeticException("Cannot encode given integer into specified number of octets.");
    }
    
    public static byte[] I2OSP(final long n) {
        return new byte[] { (byte)(n >>> 56), (byte)(n >>> 48), (byte)(n >>> 40), (byte)(n >>> 32), (byte)(n >>> 24), (byte)(n >>> 16), (byte)(n >>> 8), (byte)n };
    }
    
    public static int OS2IP(final byte[] array) {
        if (array.length > 4) {
            throw new ArithmeticException("invalid input length");
        }
        final int length = array.length;
        int i = 0;
        if (length == 0) {
            return 0;
        }
        int n = 0;
        while (i < array.length) {
            n |= (array[i] & 0xFF) << (array.length - 1 - i) * 8;
            ++i;
        }
        return n;
    }
    
    public static int OS2IP(final byte[] array, int n) {
        final int n2 = n + 1;
        n = array[n];
        final int n3 = n2 + 1;
        return (array[n3 + 1] & 0xFF) | ((n & 0xFF) << 24 | (array[n2] & 0xFF) << 16 | (array[n3] & 0xFF) << 8);
    }
    
    public static int OS2IP(final byte[] array, final int n, final int n2) {
        final int length = array.length;
        int i = 0;
        if (length == 0) {
            return 0;
        }
        if (array.length < n + n2 - 1) {
            return 0;
        }
        int n3 = 0;
        while (i < n2) {
            n3 |= (array[n + i] & 0xFF) << (n2 - i - 1) * 8;
            ++i;
        }
        return n3;
    }
    
    public static long OS2LIP(final byte[] array, int n) {
        final int n2 = n + 1;
        final long n3 = array[n];
        n = n2 + 1;
        final long n4 = array[n2];
        final int n5 = n + 1;
        final long n6 = array[n];
        n = n5 + 1;
        final long n7 = array[n5];
        final int n8 = n + 1;
        final long n9 = array[n];
        n = n8 + 1;
        return (long)(array[n + 1] & 0xFF) | ((n3 & 0xFFL) << 56 | (n4 & 0xFFL) << 48 | (n6 & 0xFFL) << 40 | (n7 & 0xFFL) << 32 | (n9 & 0xFFL) << 24 | (long)((array[n8] & 0xFF) << 16) | (long)((array[n] & 0xFF) << 8));
    }
    
    public static byte[] toByteArray(final int[] array) {
        final byte[] array2 = new byte[array.length << 2];
        for (int i = 0; i < array.length; ++i) {
            I2OSP(array[i], array2, i << 2);
        }
        return array2;
    }
    
    public static byte[] toByteArray(final int[] array, final int n) {
        final int length = array.length;
        final byte[] array2 = new byte[n];
        int i;
        int n2;
        for (i = 0, n2 = 0; i <= length - 2; ++i, n2 += 4) {
            I2OSP(array[i], array2, n2);
        }
        I2OSP(array[length - 1], array2, n2, n - n2);
        return array2;
    }
    
    public static int[] toIntArray(final byte[] array) {
        final int n = (array.length + 3) / 4;
        final int n2 = array.length & 0x3;
        final int[] array2 = new int[n];
        int i;
        int n3;
        for (i = 0, n3 = 0; i <= n - 2; ++i, n3 += 4) {
            array2[i] = OS2IP(array, n3);
        }
        if (n2 != 0) {
            array2[n - 1] = OS2IP(array, n3, n2);
            return array2;
        }
        array2[n - 1] = OS2IP(array, n3);
        return array2;
    }
}
