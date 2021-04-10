package org.spongycastle.util;

import java.math.*;
import java.security.*;
import java.util.*;

public final class BigIntegers
{
    private static final int MAX_ITERATIONS = 1000;
    private static final BigInteger ZERO;
    
    static {
        ZERO = BigInteger.valueOf(0L);
    }
    
    public static byte[] asUnsignedByteArray(final int n, final BigInteger bigInteger) {
        final byte[] byteArray = bigInteger.toByteArray();
        if (byteArray.length == n) {
            return byteArray;
        }
        int n2 = 0;
        if (byteArray[0] == 0) {
            n2 = 1;
        }
        final int n3 = byteArray.length - n2;
        if (n3 <= n) {
            final byte[] array = new byte[n];
            System.arraycopy(byteArray, n2, array, n - n3, n3);
            return array;
        }
        throw new IllegalArgumentException("standard length exceeded for value");
    }
    
    public static byte[] asUnsignedByteArray(final BigInteger bigInteger) {
        final byte[] byteArray = bigInteger.toByteArray();
        if (byteArray[0] == 0) {
            final int n = byteArray.length - 1;
            final byte[] array = new byte[n];
            System.arraycopy(byteArray, 1, array, 0, n);
            return array;
        }
        return byteArray;
    }
    
    public static BigInteger createRandomInRange(final BigInteger bigInteger, final BigInteger bigInteger2, final SecureRandom secureRandom) {
        final int compareTo = bigInteger.compareTo(bigInteger2);
        if (compareTo >= 0) {
            if (compareTo <= 0) {
                return bigInteger;
            }
            throw new IllegalArgumentException("'min' may not be greater than 'max'");
        }
        else {
            if (bigInteger.bitLength() > bigInteger2.bitLength() / 2) {
                return createRandomInRange(BigIntegers.ZERO, bigInteger2.subtract(bigInteger), secureRandom).add(bigInteger);
            }
            for (int i = 0; i < 1000; ++i) {
                final BigInteger bigInteger3 = new BigInteger(bigInteger2.bitLength(), secureRandom);
                if (bigInteger3.compareTo(bigInteger) >= 0 && bigInteger3.compareTo(bigInteger2) <= 0) {
                    return bigInteger3;
                }
            }
            return new BigInteger(bigInteger2.subtract(bigInteger).bitLength() - 1, secureRandom).add(bigInteger);
        }
    }
    
    public static BigInteger fromUnsignedByteArray(final byte[] array) {
        return new BigInteger(1, array);
    }
    
    public static BigInteger fromUnsignedByteArray(final byte[] array, final int n, final int n2) {
        if (n == 0) {
            final byte[] array2 = array;
            if (n2 == array.length) {
                return new BigInteger(1, array2);
            }
        }
        final byte[] array2 = new byte[n2];
        System.arraycopy(array, n, array2, 0, n2);
        return new BigInteger(1, array2);
    }
}
