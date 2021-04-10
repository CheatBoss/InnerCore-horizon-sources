package org.spongycastle.pqc.math.linearalgebra;

public final class LittleEndianConversions
{
    private LittleEndianConversions() {
    }
    
    public static void I2OSP(final int n, final byte[] array, int n2) {
        final int n3 = n2 + 1;
        array[n2] = (byte)n;
        n2 = n3 + 1;
        array[n3] = (byte)(n >>> 8);
        array[n2] = (byte)(n >>> 16);
        array[n2 + 1] = (byte)(n >>> 24);
    }
    
    public static void I2OSP(final int n, final byte[] array, final int n2, int n3) {
        while (true) {
            --n3;
            if (n3 < 0) {
                break;
            }
            array[n2 + n3] = (byte)(n >>> n3 * 8);
        }
    }
    
    public static void I2OSP(final long n, final byte[] array, int n2) {
        final int n3 = n2 + 1;
        array[n2] = (byte)n;
        n2 = n3 + 1;
        array[n3] = (byte)(n >>> 8);
        final int n4 = n2 + 1;
        array[n2] = (byte)(n >>> 16);
        n2 = n4 + 1;
        array[n4] = (byte)(n >>> 24);
        final int n5 = n2 + 1;
        array[n2] = (byte)(n >>> 32);
        n2 = n5 + 1;
        array[n5] = (byte)(n >>> 40);
        array[n2] = (byte)(n >>> 48);
        array[n2 + 1] = (byte)(n >>> 56);
    }
    
    public static byte[] I2OSP(final int n) {
        return new byte[] { (byte)n, (byte)(n >>> 8), (byte)(n >>> 16), (byte)(n >>> 24) };
    }
    
    public static byte[] I2OSP(final long n) {
        return new byte[] { (byte)n, (byte)(n >>> 8), (byte)(n >>> 16), (byte)(n >>> 24), (byte)(n >>> 32), (byte)(n >>> 40), (byte)(n >>> 48), (byte)(n >>> 56) };
    }
    
    public static int OS2IP(final byte[] array) {
        return (array[3] & 0xFF) << 24 | ((array[0] & 0xFF) | (array[1] & 0xFF) << 8 | (array[2] & 0xFF) << 16);
    }
    
    public static int OS2IP(final byte[] array, int n) {
        final int n2 = n + 1;
        n = array[n];
        final int n3 = n2 + 1;
        return (array[n3 + 1] & 0xFF) << 24 | ((n & 0xFF) | (array[n2] & 0xFF) << 8 | (array[n3] & 0xFF) << 16);
    }
    
    public static int OS2IP(final byte[] array, final int n, int i) {
        --i;
        int n2 = 0;
        while (i >= 0) {
            n2 |= (array[n + i] & 0xFF) << i * 8;
            --i;
        }
        return n2;
    }
    
    public static long OS2LIP(final byte[] array, int n) {
        final int n2 = n + 1;
        final long n3 = array[n] & 0xFF;
        n = n2 + 1;
        final long n4 = (array[n2] & 0xFF) << 8;
        final int n5 = n + 1;
        final long n6 = (array[n] & 0xFF) << 16;
        n = n5 + 1;
        final long n7 = array[n5];
        final int n8 = n + 1;
        final long n9 = array[n];
        n = n8 + 1;
        return ((long)array[n + 1] & 0xFFL) << 56 | (n3 | n4 | n6 | (n7 & 0xFFL) << 24 | (n9 & 0xFFL) << 32 | ((long)array[n8] & 0xFFL) << 40 | ((long)array[n] & 0xFFL) << 48);
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
