package org.spongycastle.pqc.crypto.mceliece;

import java.math.*;
import org.spongycastle.pqc.math.linearalgebra.*;

final class Conversions
{
    private static final BigInteger ONE;
    private static final BigInteger ZERO;
    
    static {
        ZERO = BigInteger.valueOf(0L);
        ONE = BigInteger.valueOf(1L);
    }
    
    private Conversions() {
    }
    
    public static byte[] decode(final int n, int i, final GF2Vector gf2Vector) {
        if (gf2Vector.getLength() == n && gf2Vector.getHammingWeight() == i) {
            final int[] vecArray = gf2Vector.getVecArray();
            BigInteger bigInteger = IntegerFunctions.binomial(n, i);
            BigInteger zero = Conversions.ZERO;
            final int n2 = 0;
            int n3 = i;
            int n4 = n;
            BigInteger divide;
            int n5;
            int n6;
            BigInteger add;
            for (i = n2; i < n; ++i, n3 = n6, zero = add, n4 = n5) {
                divide = bigInteger.multiply(BigInteger.valueOf(n4 - n3)).divide(BigInteger.valueOf(n4));
                n5 = n4 - 1;
                bigInteger = divide;
                n6 = n3;
                add = zero;
                if ((1 << (i & 0x1F) & vecArray[i >> 5]) != 0x0) {
                    add = zero.add(divide);
                    n6 = n3 - 1;
                    if (n5 == n6) {
                        bigInteger = Conversions.ONE;
                    }
                    else {
                        bigInteger = divide.multiply(BigInteger.valueOf(n6 + 1)).divide(BigInteger.valueOf(n5 - n6));
                    }
                }
            }
            return BigIntUtils.toMinimalByteArray(zero);
        }
        throw new IllegalArgumentException("vector has wrong length or hamming weight");
    }
    
    public static GF2Vector encode(final int n, int i, final byte[] array) {
        if (n < i) {
            throw new IllegalArgumentException("n < t");
        }
        final BigInteger binomial = IntegerFunctions.binomial(n, i);
        BigInteger bigInteger = new BigInteger(1, array);
        if (bigInteger.compareTo(binomial) < 0) {
            final GF2Vector gf2Vector = new GF2Vector(n);
            final int n2 = 0;
            int n3 = i;
            int n4 = n;
            i = n2;
            BigInteger bigInteger2 = binomial;
            while (i < n) {
                final BigInteger divide = bigInteger2.multiply(BigInteger.valueOf(n4 - n3)).divide(BigInteger.valueOf(n4));
                final int n5 = n4 - 1;
                bigInteger2 = divide;
                int n6 = n3;
                BigInteger subtract = bigInteger;
                if (divide.compareTo(bigInteger) <= 0) {
                    gf2Vector.setBit(i);
                    subtract = bigInteger.subtract(divide);
                    n6 = n3 - 1;
                    if (n5 == n6) {
                        bigInteger2 = Conversions.ONE;
                    }
                    else {
                        bigInteger2 = divide.multiply(BigInteger.valueOf(n6 + 1)).divide(BigInteger.valueOf(n5 - n6));
                    }
                }
                ++i;
                n3 = n6;
                bigInteger = subtract;
                n4 = n5;
            }
            return gf2Vector;
        }
        throw new IllegalArgumentException("Encoded number too large.");
    }
    
    public static byte[] signConversion(int i, int j, byte[] array) {
        if (i >= j) {
            final BigInteger binomial = IntegerFunctions.binomial(i, j);
            final int n = binomial.bitLength() - 1;
            final int n2 = n >> 3;
            final int n3 = n & 0x7;
            int n4 = 8;
            int n5 = n3;
            int n6 = n2;
            if (n3 == 0) {
                n6 = n2 - 1;
                n5 = 8;
            }
            int n7 = i >> 3;
            final int n8 = i & 0x7;
            if (n8 == 0) {
                --n7;
            }
            else {
                n4 = n8;
            }
            final int n9 = n7 + 1;
            final byte[] array2 = new byte[n9];
            if (array.length < n9) {
                System.arraycopy(array, 0, array2, 0, array.length);
                for (int k = array.length; k < n9; ++k) {
                    array2[k] = 0;
                }
            }
            else {
                System.arraycopy(array, 0, array2, 0, n7);
                array2[n7] = (byte)(array[n7] & (1 << n4) - 1);
            }
            BigInteger zero = Conversions.ZERO;
            int n10 = j;
            j = 0;
            int n11 = i;
            BigInteger bigInteger = binomial;
            while (j < i) {
                final BigInteger divide = bigInteger.multiply(new BigInteger(Integer.toString(n11 - n10))).divide(new BigInteger(Integer.toString(n11)));
                final int n12 = n11 - 1;
                bigInteger = divide;
                int n13 = n10;
                BigInteger add = zero;
                if ((byte)(array2[j >>> 3] & 1 << (j & 0x7)) != 0) {
                    add = zero.add(divide);
                    n13 = n10 - 1;
                    if (n12 == n13) {
                        bigInteger = Conversions.ONE;
                    }
                    else {
                        bigInteger = divide.multiply(new BigInteger(Integer.toString(n13 + 1))).divide(new BigInteger(Integer.toString(n12 - n13)));
                    }
                }
                ++j;
                n10 = n13;
                zero = add;
                n11 = n12;
            }
            j = n6 + 1;
            array = new byte[j];
            final byte[] byteArray = zero.toByteArray();
            if (byteArray.length < j) {
                System.arraycopy(byteArray, 0, array, 0, byteArray.length);
                for (i = byteArray.length; i < j; ++i) {
                    array[i] = 0;
                }
            }
            else {
                System.arraycopy(byteArray, 0, array, 0, n6);
                array[n6] = (byte)((1 << n5) - 1 & byteArray[n6]);
            }
            return array;
        }
        throw new IllegalArgumentException("n < t");
    }
}
