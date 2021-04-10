package org.spongycastle.math.ec.custom.sec;

import java.math.*;
import org.spongycastle.math.raw.*;

public class SecT409Field
{
    private static final long M25 = 33554431L;
    private static final long M59 = 576460752303423487L;
    
    public static void add(final long[] array, final long[] array2, final long[] array3) {
        array3[0] = (array[0] ^ array2[0]);
        array3[1] = (array[1] ^ array2[1]);
        array3[2] = (array[2] ^ array2[2]);
        array3[3] = (array[3] ^ array2[3]);
        array3[4] = (array[4] ^ array2[4]);
        array3[5] = (array[5] ^ array2[5]);
        array3[6] = (array2[6] ^ array[6]);
    }
    
    public static void addExt(final long[] array, final long[] array2, final long[] array3) {
        for (int i = 0; i < 13; ++i) {
            array3[i] = (array[i] ^ array2[i]);
        }
    }
    
    public static void addOne(final long[] array, final long[] array2) {
        array2[0] = (array[0] ^ 0x1L);
        array2[1] = array[1];
        array2[2] = array[2];
        array2[3] = array[3];
        array2[4] = array[4];
        array2[5] = array[5];
        array2[6] = array[6];
    }
    
    public static long[] fromBigInteger(final BigInteger bigInteger) {
        final long[] fromBigInteger64 = Nat448.fromBigInteger64(bigInteger);
        reduce39(fromBigInteger64, 0);
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
        final long n9 = array[8];
        final long n10 = array[9];
        final long n11 = array[10];
        final long n12 = array[11];
        final long n13 = array[12];
        final long n14 = array[13];
        array[0] = (n ^ n2 << 59);
        array[1] = (n2 >>> 5 ^ n3 << 54);
        array[2] = (n3 >>> 10 ^ n4 << 49);
        array[3] = (n4 >>> 15 ^ n5 << 44);
        array[4] = (n5 >>> 20 ^ n6 << 39);
        array[5] = (n6 >>> 25 ^ n7 << 34);
        array[6] = (n7 >>> 30 ^ n8 << 29);
        array[7] = (n8 >>> 35 ^ n9 << 24);
        array[8] = (n9 >>> 40 ^ n10 << 19);
        array[9] = (n10 >>> 45 ^ n11 << 14);
        array[10] = (n11 >>> 50 ^ n12 << 9);
        array[11] = (n12 >>> 55 ^ n13 << 4 ^ n14 << 63);
        array[12] = (n13 >>> 60 ^ n14 >>> 1);
        array[13] = 0L;
    }
    
    protected static void implExpand(final long[] array, final long[] array2) {
        final long n = array[0];
        final long n2 = array[1];
        final long n3 = array[2];
        final long n4 = array[3];
        final long n5 = array[4];
        final long n6 = array[5];
        final long n7 = array[6];
        array2[0] = (n & 0x7FFFFFFFFFFFFFFL);
        array2[1] = ((n >>> 59 ^ n2 << 5) & 0x7FFFFFFFFFFFFFFL);
        array2[2] = ((n2 >>> 54 ^ n3 << 10) & 0x7FFFFFFFFFFFFFFL);
        array2[3] = ((n3 >>> 49 ^ n4 << 15) & 0x7FFFFFFFFFFFFFFL);
        array2[4] = ((n4 >>> 44 ^ n5 << 20) & 0x7FFFFFFFFFFFFFFL);
        array2[5] = ((n5 >>> 39 ^ n6 << 25) & 0x7FFFFFFFFFFFFFFL);
        array2[6] = (n6 >>> 34 ^ n7 << 30);
    }
    
    protected static void implMultiply(final long[] array, final long[] array2, final long[] array3) {
        final long[] array4 = new long[7];
        final long[] array5 = new long[7];
        implExpand(array, array4);
        implExpand(array2, array5);
        for (int i = 0; i < 7; ++i) {
            implMulwAcc(array4, array5[i], array3, i);
        }
        implCompactExt(array3);
    }
    
    protected static void implMulwAcc(final long[] array, long n, final long[] array2, final int n2) {
        final long[] array3 = new long[8];
        array3[1] = n;
        array3[2] = array3[1] << 1;
        array3[3] = (array3[2] ^ n);
        array3[4] = array3[2] << 1;
        array3[5] = (array3[4] ^ n);
        array3[6] = array3[3] << 1;
        array3[7] = (array3[6] ^ n);
        for (int i = 0; i < 7; ++i) {
            final long n3 = array[i];
            final int n4 = (int)n3;
            n = 0L;
            long n5 = array3[n4 & 0x7] ^ array3[n4 >>> 3 & 0x7] << 3;
            int n6 = 54;
            do {
                final int n7 = (int)(n3 >>> n6);
                final long n8 = array3[n7 >>> 3 & 0x7] << 3 ^ array3[n7 & 0x7];
                n5 ^= n8 << n6;
                n ^= n8 >>> -n6;
                n6 -= 6;
            } while (n6 > 0);
            final int n9 = n2 + i;
            array2[n9] ^= (n5 & 0x7FFFFFFFFFFFFFFL);
            final int n10 = n9 + 1;
            array2[n10] ^= (n << 5 ^ n5 >>> 59);
        }
    }
    
    protected static void implSquare(final long[] array, final long[] array2) {
        for (int i = 0; i < 6; ++i) {
            Interleave.expand64To128(array[i], array2, i << 1);
        }
        array2[12] = Interleave.expand32to64((int)array[6]);
    }
    
    public static void invert(final long[] array, final long[] array2) {
        if (!Nat448.isZero64(array)) {
            final long[] create64 = Nat448.create64();
            final long[] create65 = Nat448.create64();
            final long[] create66 = Nat448.create64();
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
            multiply(create64, create65, create66);
            squareN(create66, 24, create64);
            squareN(create64, 24, create65);
            multiply(create64, create65, create64);
            squareN(create64, 48, create65);
            multiply(create64, create65, create64);
            squareN(create64, 96, create65);
            multiply(create64, create65, create64);
            squareN(create64, 192, create65);
            multiply(create64, create65, create64);
            multiply(create64, create66, array2);
            return;
        }
        throw new IllegalStateException();
    }
    
    public static void multiply(final long[] array, final long[] array2, final long[] array3) {
        final long[] ext64 = Nat448.createExt64();
        implMultiply(array, array2, ext64);
        reduce(ext64, array3);
    }
    
    public static void multiplyAddToExt(final long[] array, final long[] array2, final long[] array3) {
        final long[] ext64 = Nat448.createExt64();
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
        final long n8 = array[7];
        final long n9 = array[12];
        final long n10 = n8 ^ n9 >>> 2;
        final long n11 = array[11];
        final long n12 = n7 ^ (n9 >>> 25 ^ n9 << 62) ^ n11 >>> 2;
        final long n13 = array[10];
        final long n14 = array[9];
        final long n15 = array[8];
        final long n16 = n12 >>> 25;
        array2[0] = (n ^ n10 << 39 ^ n16);
        array2[1] = (n16 << 23 ^ (n2 ^ n15 << 39 ^ (n10 >>> 25 ^ n10 << 62)));
        array2[2] = (n3 ^ n14 << 39 ^ (n15 >>> 25 ^ n15 << 62) ^ n10 >>> 2);
        array2[3] = (n4 ^ n13 << 39 ^ (n14 >>> 25 ^ n14 << 62) ^ n15 >>> 2);
        array2[4] = (n5 ^ n11 << 39 ^ (n13 >>> 25 ^ n13 << 62) ^ n14 >>> 2);
        array2[5] = (n6 ^ n9 << 39 ^ (n11 >>> 25 ^ n11 << 62) ^ n13 >>> 2);
        array2[6] = (n12 & 0x1FFFFFFL);
    }
    
    public static void reduce39(final long[] array, int n) {
        final int n2 = n + 6;
        final long n3 = array[n2];
        final long n4 = n3 >>> 25;
        array[n] ^= n4;
        ++n;
        array[n] ^= n4 << 23;
        array[n2] = (n3 & 0x1FFFFFFL);
    }
    
    public static void sqrt(final long[] array, final long[] array2) {
        final long unshuffle = Interleave.unshuffle(array[0]);
        final long unshuffle2 = Interleave.unshuffle(array[1]);
        final long n = unshuffle >>> 32 | (unshuffle2 & 0xFFFFFFFF00000000L);
        final long unshuffle3 = Interleave.unshuffle(array[2]);
        final long unshuffle4 = Interleave.unshuffle(array[3]);
        final long n2 = unshuffle3 >>> 32 | (unshuffle4 & 0xFFFFFFFF00000000L);
        final long unshuffle5 = Interleave.unshuffle(array[4]);
        final long unshuffle6 = Interleave.unshuffle(array[5]);
        final long n3 = unshuffle5 >>> 32 | (unshuffle6 & 0xFFFFFFFF00000000L);
        final long unshuffle7 = Interleave.unshuffle(array[6]);
        final long n4 = unshuffle7 >>> 32;
        array2[0] = (((unshuffle & 0xFFFFFFFFL) | unshuffle2 << 32) ^ n << 44);
        array2[1] = (((unshuffle3 & 0xFFFFFFFFL) | unshuffle4 << 32) ^ n2 << 44 ^ n >>> 20);
        array2[2] = (((unshuffle5 & 0xFFFFFFFFL) | unshuffle6 << 32) ^ n3 << 44 ^ n2 >>> 20);
        array2[3] = (n4 << 44 ^ (unshuffle7 & 0xFFFFFFFFL) ^ n3 >>> 20 ^ n << 13);
        array2[4] = (n >>> 51 ^ (n4 >>> 20 ^ n2 << 13));
        array2[5] = (n3 << 13 ^ n2 >>> 51);
        array2[6] = (n4 << 13 ^ n3 >>> 51);
    }
    
    public static void square(final long[] array, final long[] array2) {
        final long[] create64 = Nat.create64(13);
        implSquare(array, create64);
        reduce(create64, array2);
    }
    
    public static void squareAddToExt(final long[] array, final long[] array2) {
        final long[] create64 = Nat.create64(13);
        implSquare(array, create64);
        addExt(array2, create64, array2);
    }
    
    public static void squareN(final long[] array, int n, final long[] array2) {
        final long[] create64 = Nat.create64(13);
        implSquare(array, create64);
        while (true) {
            reduce(create64, array2);
            --n;
            if (n <= 0) {
                break;
            }
            implSquare(array2, create64);
        }
    }
    
    public static int trace(final long[] array) {
        return (int)array[0] & 0x1;
    }
}
