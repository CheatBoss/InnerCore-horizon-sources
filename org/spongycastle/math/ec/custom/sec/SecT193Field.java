package org.spongycastle.math.ec.custom.sec;

import java.math.*;
import org.spongycastle.math.raw.*;

public class SecT193Field
{
    private static final long M01 = 1L;
    private static final long M49 = 562949953421311L;
    
    public static void add(final long[] array, final long[] array2, final long[] array3) {
        array3[0] = (array[0] ^ array2[0]);
        array3[1] = (array[1] ^ array2[1]);
        array3[2] = (array[2] ^ array2[2]);
        array3[3] = (array2[3] ^ array[3]);
    }
    
    public static void addExt(final long[] array, final long[] array2, final long[] array3) {
        array3[0] = (array[0] ^ array2[0]);
        array3[1] = (array[1] ^ array2[1]);
        array3[2] = (array[2] ^ array2[2]);
        array3[3] = (array[3] ^ array2[3]);
        array3[4] = (array[4] ^ array2[4]);
        array3[5] = (array[5] ^ array2[5]);
        array3[6] = (array2[6] ^ array[6]);
    }
    
    public static void addOne(final long[] array, final long[] array2) {
        array2[0] = (array[0] ^ 0x1L);
        array2[1] = array[1];
        array2[2] = array[2];
        array2[3] = array[3];
    }
    
    public static long[] fromBigInteger(final BigInteger bigInteger) {
        final long[] fromBigInteger64 = Nat256.fromBigInteger64(bigInteger);
        reduce63(fromBigInteger64, 0);
        return fromBigInteger64;
    }
    
    protected static void implCompactExt(final long[] array) {
        final long n = array[0];
        final long n2 = array[1];
        final long n3 = array[2];
        final long n4 = array[3];
        final long n5 = array[4];
        final long n6 = array[5];
        final long n7 = array[6];
        final long n8 = array[7];
        array[0] = (n ^ n2 << 49);
        array[1] = (n2 >>> 15 ^ n3 << 34);
        array[2] = (n3 >>> 30 ^ n4 << 19);
        array[3] = (n4 >>> 45 ^ n5 << 4 ^ n6 << 53);
        array[4] = (n5 >>> 60 ^ n7 << 38 ^ n6 >>> 11);
        array[5] = (n7 >>> 26 ^ n8 << 23);
        array[6] = n8 >>> 41;
        array[7] = 0L;
    }
    
    protected static void implExpand(final long[] array, final long[] array2) {
        final long n = array[0];
        final long n2 = array[1];
        final long n3 = array[2];
        final long n4 = array[3];
        array2[0] = (n & 0x1FFFFFFFFFFFFL);
        array2[1] = ((n >>> 49 ^ n2 << 15) & 0x1FFFFFFFFFFFFL);
        array2[2] = ((n2 >>> 34 ^ n3 << 30) & 0x1FFFFFFFFFFFFL);
        array2[3] = (n3 >>> 19 ^ n4 << 45);
    }
    
    protected static void implMultiply(long[] array, final long[] array2, final long[] array3) {
        final long[] array4 = new long[4];
        final long[] array5 = new long[4];
        implExpand(array, array4);
        implExpand(array2, array5);
        implMulwAcc(array4[0], array5[0], array3, 0);
        implMulwAcc(array4[1], array5[1], array3, 1);
        implMulwAcc(array4[2], array5[2], array3, 2);
        implMulwAcc(array4[3], array5[3], array3, 3);
        int n2;
        for (int i = 5; i > 0; i = n2) {
            final long n = array3[i];
            n2 = i - 1;
            array3[i] = (n ^ array3[n2]);
        }
        implMulwAcc(array4[0] ^ array4[1], array5[0] ^ array5[1], array3, 1);
        implMulwAcc(array4[2] ^ array4[3], array5[2] ^ array5[3], array3, 3);
        for (int j = 7; j > 1; --j) {
            array3[j] ^= array3[j - 2];
        }
        final long n3 = array4[0] ^ array4[2];
        final long n4 = array4[1] ^ array4[3];
        final long n5 = array5[0] ^ array5[2];
        final long n6 = array5[1] ^ array5[3];
        implMulwAcc(n3 ^ n4, n5 ^ n6, array3, 3);
        array = new long[3];
        implMulwAcc(n3, n5, array, 0);
        implMulwAcc(n4, n6, array, 1);
        final long n7 = array[0];
        final long n8 = array[1];
        final long n9 = array[2];
        array3[2] ^= n7;
        array3[3] ^= (n7 ^ n8);
        array3[4] ^= (n8 ^ n9);
        array3[5] ^= n9;
        implCompactExt(array3);
    }
    
    protected static void implMulwAcc(final long n, long n2, final long[] array, int n3) {
        final long[] array2 = new long[8];
        array2[1] = n2;
        array2[2] = array2[1] << 1;
        array2[3] = (array2[2] ^ n2);
        array2[4] = array2[2] << 1;
        array2[5] = (array2[4] ^ n2);
        array2[6] = array2[3] << 1;
        array2[7] = (array2[6] ^ n2);
        final int n4 = (int)n;
        n2 = array2[n4 & 0x7];
        long n5 = array2[n4 >>> 3 & 0x7] << 3 ^ n2;
        n2 = 0L;
        int n6 = 36;
        do {
            final int n7 = (int)(n >>> n6);
            final long n8 = array2[n7 >>> 12 & 0x7] << 12 ^ (array2[n7 & 0x7] ^ array2[n7 >>> 3 & 0x7] << 3 ^ array2[n7 >>> 6 & 0x7] << 6 ^ array2[n7 >>> 9 & 0x7] << 9);
            n5 ^= n8 << n6;
            n2 ^= n8 >>> -n6;
            n6 -= 15;
        } while (n6 > 0);
        array[n3] ^= (n5 & 0x1FFFFFFFFFFFFL);
        ++n3;
        array[n3] ^= (n5 >>> 49 ^ n2 << 15);
    }
    
    protected static void implSquare(final long[] array, final long[] array2) {
        Interleave.expand64To128(array[0], array2, 0);
        Interleave.expand64To128(array[1], array2, 2);
        Interleave.expand64To128(array[2], array2, 4);
        array2[6] = (array[3] & 0x1L);
    }
    
    public static void invert(final long[] array, final long[] array2) {
        if (!Nat256.isZero64(array)) {
            final long[] create64 = Nat256.create64();
            final long[] create65 = Nat256.create64();
            square(array, create64);
            squareN(create64, 1, create65);
            multiply(create64, create65, create64);
            squareN(create65, 1, create65);
            multiply(create64, create65, create64);
            squareN(create64, 3, create65);
            multiply(create64, create65, create64);
            squareN(create64, 6, create65);
            multiply(create64, create65, create64);
            squareN(create64, 12, create65);
            multiply(create64, create65, create64);
            squareN(create64, 24, create65);
            multiply(create64, create65, create64);
            squareN(create64, 48, create65);
            multiply(create64, create65, create64);
            squareN(create64, 96, create65);
            multiply(create64, create65, array2);
            return;
        }
        throw new IllegalStateException();
    }
    
    public static void multiply(final long[] array, final long[] array2, final long[] array3) {
        final long[] ext64 = Nat256.createExt64();
        implMultiply(array, array2, ext64);
        reduce(ext64, array3);
    }
    
    public static void multiplyAddToExt(final long[] array, final long[] array2, final long[] array3) {
        final long[] ext64 = Nat256.createExt64();
        implMultiply(array, array2, ext64);
        addExt(array3, ext64, array3);
    }
    
    public static void reduce(final long[] array, final long[] array2) {
        final long n = array[0];
        final long n2 = array[1];
        final long n3 = array[2];
        final long n4 = array[3];
        final long n5 = array[4];
        final long n6 = array[5];
        final long n7 = array[6];
        final long n8 = n5 ^ n7 >>> 50;
        final long n9 = n4 ^ (n7 >>> 1 ^ n7 << 14) ^ n6 >>> 50;
        final long n10 = n9 >>> 1;
        array2[0] = (n ^ n8 << 63 ^ n10 ^ n10 << 15);
        array2[1] = (n10 >>> 49 ^ (n2 ^ n6 << 63 ^ (n8 >>> 1 ^ n8 << 14)));
        array2[2] = (n3 ^ n7 << 63 ^ (n6 >>> 1 ^ n6 << 14) ^ n8 >>> 50);
        array2[3] = (n9 & 0x1L);
    }
    
    public static void reduce63(final long[] array, int n) {
        final int n2 = n + 3;
        final long n3 = array[n2];
        final long n4 = n3 >>> 1;
        array[n] ^= (n4 << 15 ^ n4);
        ++n;
        array[n] ^= n4 >>> 49;
        array[n2] = (n3 & 0x1L);
    }
    
    public static void sqrt(final long[] array, final long[] array2) {
        final long unshuffle = Interleave.unshuffle(array[0]);
        final long unshuffle2 = Interleave.unshuffle(array[1]);
        final long n = unshuffle >>> 32 | (unshuffle2 & 0xFFFFFFFF00000000L);
        final long unshuffle3 = Interleave.unshuffle(array[2]);
        final long n2 = array[3];
        final long n3 = unshuffle3 >>> 32;
        array2[0] = (((unshuffle & 0xFFFFFFFFL) | unshuffle2 << 32) ^ n << 8);
        array2[1] = ((unshuffle3 & 0xFFFFFFFFL) ^ n2 << 32 ^ n3 << 8 ^ n >>> 56 ^ n << 33);
        array2[2] = (n >>> 31 ^ (n3 >>> 56 ^ n3 << 33));
        array2[3] = n3 >>> 31;
    }
    
    public static void square(final long[] array, final long[] array2) {
        final long[] ext64 = Nat256.createExt64();
        implSquare(array, ext64);
        reduce(ext64, array2);
    }
    
    public static void squareAddToExt(final long[] array, final long[] array2) {
        final long[] ext64 = Nat256.createExt64();
        implSquare(array, ext64);
        addExt(array2, ext64, array2);
    }
    
    public static void squareN(final long[] array, int n, final long[] array2) {
        final long[] ext64 = Nat256.createExt64();
        implSquare(array, ext64);
        while (true) {
            reduce(ext64, array2);
            --n;
            if (n <= 0) {
                break;
            }
            implSquare(array2, ext64);
        }
    }
    
    public static int trace(final long[] array) {
        return (int)array[0] & 0x1;
    }
}
