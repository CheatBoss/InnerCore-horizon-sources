package org.spongycastle.pqc.math.ntru.util;

import java.io.*;
import java.math.*;
import org.spongycastle.util.*;

public class ArrayEncoder
{
    private static final int[] BIT1_TABLE;
    private static final int[] BIT2_TABLE;
    private static final int[] BIT3_TABLE;
    private static final int[] COEFF1_TABLE;
    private static final int[] COEFF2_TABLE;
    
    static {
        COEFF1_TABLE = new int[] { 0, 0, 0, 1, 1, 1, -1, -1 };
        COEFF2_TABLE = new int[] { 0, 1, -1, 0, 1, -1, 0, 1 };
        BIT1_TABLE = new int[] { 1, 1, 1, 0, 0, 0, 1, 0, 1 };
        BIT2_TABLE = new int[] { 1, 1, 1, 1, 0, 0, 0, 1, 0 };
        BIT3_TABLE = new int[] { 1, 0, 1, 0, 0, 1, 1, 1, 0 };
    }
    
    public static int[] decodeMod3Sves(final byte[] array, final int n) {
        final int[] array2 = new int[n];
        int i = 0;
        int n2 = 0;
        while (i < array.length * 8) {
            final int n3 = i + 1;
            final int bit = getBit(array, i);
            final int n4 = n3 + 1;
            final int n5 = bit * 4 + getBit(array, n3) * 2 + getBit(array, n4);
            final int n6 = n2 + 1;
            array2[n2] = ArrayEncoder.COEFF1_TABLE[n5];
            n2 = n6 + 1;
            array2[n6] = ArrayEncoder.COEFF2_TABLE[n5];
            if (n2 > n - 2) {
                return array2;
            }
            i = n4 + 1;
        }
        return array2;
    }
    
    public static int[] decodeMod3Tight(final InputStream inputStream, final int n) throws IOException {
        final double n2 = n;
        final double log = Math.log(3.0);
        Double.isNaN(n2);
        return decodeMod3Tight(Util.readFullLength(inputStream, (int)Math.ceil(n2 * log / Math.log(2.0) / 8.0)), n);
    }
    
    public static int[] decodeMod3Tight(final byte[] array, final int n) {
        BigInteger divide = new BigInteger(1, array);
        final int[] array2 = new int[n];
        for (int i = 0; i < n; ++i) {
            array2[i] = divide.mod(BigInteger.valueOf(3L)).intValue() - 1;
            if (array2[i] > 1) {
                array2[i] -= 3;
            }
            divide = divide.divide(BigInteger.valueOf(3L));
        }
        return array2;
    }
    
    public static int[] decodeModQ(final InputStream inputStream, final int n, final int n2) throws IOException {
        return decodeModQ(Util.readFullLength(inputStream, ((31 - Integer.numberOfLeadingZeros(n2)) * n + 7) / 8), n, n2);
    }
    
    public static int[] decodeModQ(final byte[] array, final int n, int i) {
        final int[] array2 = new int[n];
        final int n2 = 31 - Integer.numberOfLeadingZeros(i);
        i = 0;
        int n3 = 0;
        while (i < n * n2) {
            int n4 = n3;
            if (i > 0) {
                n4 = n3;
                if (i % n2 == 0) {
                    n4 = n3 + 1;
                }
            }
            array2[n4] += getBit(array, i) << i % n2;
            ++i;
            n3 = n4;
        }
        return array2;
    }
    
    public static byte[] encodeMod3Sves(final int[] array) {
        final byte[] array2 = new byte[((array.length * 3 + 1) / 2 + 7) / 8];
        int i = 0;
        int n = 0;
        int n2 = 0;
        while (i < array.length / 2 * 2) {
            final int n3 = i + 1;
            final int n4 = array[i] + 1;
            final int n5 = array[n3] + 1;
            if (n4 == 0 && n5 == 0) {
                throw new IllegalStateException("Illegal encoding!");
            }
            final int n6 = n4 * 3 + n5;
            final int n7 = ArrayEncoder.BIT1_TABLE[n6];
            final int n8 = ArrayEncoder.BIT2_TABLE[n6];
            final int n9 = ArrayEncoder.BIT3_TABLE[n6];
            for (int j = 0; j < 3; ++j) {
                array2[n] |= (byte)((new int[] { n7, n8, n9 })[j] << n2);
                if (n2 == 7) {
                    ++n;
                    n2 = 0;
                }
                else {
                    ++n2;
                }
            }
            i = n3 + 1;
        }
        return array2;
    }
    
    public static byte[] encodeMod3Tight(final int[] array) {
        BigInteger bigInteger = BigInteger.ZERO;
        for (int i = array.length - 1; i >= 0; --i) {
            bigInteger = bigInteger.multiply(BigInteger.valueOf(3L)).add(BigInteger.valueOf(array[i] + 1));
        }
        final int n = (BigInteger.valueOf(3L).pow(array.length).bitLength() + 7) / 8;
        final byte[] byteArray = bigInteger.toByteArray();
        if (byteArray.length < n) {
            final byte[] array2 = new byte[n];
            System.arraycopy(byteArray, 0, array2, n - byteArray.length, byteArray.length);
            return array2;
        }
        byte[] copyOfRange = byteArray;
        if (byteArray.length > n) {
            copyOfRange = Arrays.copyOfRange(byteArray, 1, byteArray.length);
        }
        return copyOfRange;
    }
    
    public static byte[] encodeModQ(final int[] array, int n) {
        final int n2 = 31 - Integer.numberOfLeadingZeros(n);
        final byte[] array2 = new byte[(array.length * n2 + 7) / 8];
        int i = 0;
        n = 0;
        int n3 = 0;
        while (i < array.length) {
            for (int j = 0; j < n2; ++j) {
                array2[n] |= (byte)((array[i] >> j & 0x1) << n3);
                if (n3 == 7) {
                    ++n;
                    n3 = 0;
                }
                else {
                    ++n3;
                }
            }
            ++i;
        }
        return array2;
    }
    
    private static int getBit(final byte[] array, final int n) {
        return (array[n / 8] & 0xFF) >> n % 8 & 0x1;
    }
}
