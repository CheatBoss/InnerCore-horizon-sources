package org.spongycastle.pqc.math.linearalgebra;

import java.math.*;

public final class BigIntUtils
{
    private BigIntUtils() {
    }
    
    public static boolean equals(final BigInteger[] array, final BigInteger[] array2) {
        final int length = array.length;
        final int length2 = array2.length;
        boolean b = false;
        if (length != length2) {
            return false;
        }
        int i = 0;
        int n = 0;
        while (i < array.length) {
            n |= array[i].compareTo(array2[i]);
            ++i;
        }
        if (n == 0) {
            b = true;
        }
        return b;
    }
    
    public static void fill(final BigInteger[] array, final BigInteger bigInteger) {
        int length = array.length;
        while (true) {
            --length;
            if (length < 0) {
                break;
            }
            array[length] = bigInteger;
        }
    }
    
    public static BigInteger[] subArray(final BigInteger[] array, final int n, int n2) {
        n2 -= n;
        final BigInteger[] array2 = new BigInteger[n2];
        System.arraycopy(array, n, array2, 0, n2);
        return array2;
    }
    
    public static int[] toIntArray(final BigInteger[] array) {
        final int[] array2 = new int[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = array[i].intValue();
        }
        return array2;
    }
    
    public static int[] toIntArrayModQ(int i, final BigInteger[] array) {
        final BigInteger value = BigInteger.valueOf(i);
        final int[] array2 = new int[array.length];
        for (i = 0; i < array.length; ++i) {
            array2[i] = array[i].mod(value).intValue();
        }
        return array2;
    }
    
    public static byte[] toMinimalByteArray(final BigInteger bigInteger) {
        final byte[] byteArray = bigInteger.toByteArray();
        if (byteArray.length == 1) {
            return byteArray;
        }
        if ((bigInteger.bitLength() & 0x7) != 0x0) {
            return byteArray;
        }
        final int n = bigInteger.bitLength() >> 3;
        final byte[] array = new byte[n];
        System.arraycopy(byteArray, 1, array, 0, n);
        return array;
    }
}
