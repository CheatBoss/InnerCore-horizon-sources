package org.spongycastle.math.ec.custom.sec;

import java.math.*;
import org.spongycastle.math.raw.*;

public class SecT113Field
{
    private static final long M49 = 562949953421311L;
    private static final long M57 = 144115188075855871L;
    
    public static void add(final long[] array, final long[] array2, final long[] array3) {
        array3[0] = (array[0] ^ array2[0]);
        array3[1] = (array2[1] ^ array[1]);
    }
    
    public static void addExt(final long[] array, final long[] array2, final long[] array3) {
        array3[0] = (array[0] ^ array2[0]);
        array3[1] = (array[1] ^ array2[1]);
        array3[2] = (array[2] ^ array2[2]);
        array3[3] = (array2[3] ^ array[3]);
    }
    
    public static void addOne(final long[] array, final long[] array2) {
        array2[0] = (array[0] ^ 0x1L);
        array2[1] = array[1];
    }
    
    public static long[] fromBigInteger(final BigInteger bigInteger) {
        final long[] fromBigInteger64 = Nat128.fromBigInteger64(bigInteger);
        reduce15(fromBigInteger64, 0);
        return fromBigInteger64;
    }
    
    protected static void implMultiply(long[] array, final long[] array2, final long[] array3) {
        final long n = array[0];
        final long n2 = (array[1] << 7 ^ n >>> 57) & 0x1FFFFFFFFFFFFFFL;
        final long n3 = n & 0x1FFFFFFFFFFFFFFL;
        final long n4 = array2[0];
        final long n5 = (array2[1] << 7 ^ n4 >>> 57) & 0x1FFFFFFFFFFFFFFL;
        final long n6 = n4 & 0x1FFFFFFFFFFFFFFL;
        array = new long[6];
        implMulw(n3, n6, array, 0);
        implMulw(n2, n5, array, 2);
        implMulw(n3 ^ n2, n6 ^ n5, array, 4);
        final long n7 = array[1] ^ array[2];
        final long n8 = array[0];
        final long n9 = array[3];
        final long n10 = array[4] ^ n8 ^ n7;
        final long n11 = n7 ^ (array[5] ^ n9);
        array3[0] = (n8 ^ n10 << 57);
        array3[1] = (n10 >>> 7 ^ n11 << 50);
        array3[2] = (n11 >>> 14 ^ n9 << 43);
        array3[3] = n9 >>> 21;
    }
    
    protected static void implMulw(final long n, final long n2, final long[] array, final int n3) {
        final long[] array2 = new long[8];
        array2[1] = n2;
        array2[2] = array2[1] << 1;
        array2[3] = (array2[2] ^ n2);
        array2[4] = array2[2] << 1;
        array2[5] = (array2[4] ^ n2);
        array2[6] = array2[3] << 1;
        array2[7] = (array2[6] ^ n2);
        long n4 = array2[(int)n & 0x7];
        long n5 = 0L;
        int n6 = 48;
        do {
            final int n7 = (int)(n >>> n6);
            final long n8 = array2[n7 >>> 6 & 0x7] << 6 ^ (array2[n7 & 0x7] ^ array2[n7 >>> 3 & 0x7] << 3);
            n4 ^= n8 << n6;
            n5 ^= n8 >>> -n6;
            n6 -= 9;
        } while (n6 > 0);
        array[n3] = (n4 & 0x1FFFFFFFFFFFFFFL);
        array[n3 + 1] = (((n & 0x100804020100800L & n2 << 7 >> 63) >>> 8 ^ n5) << 7 ^ n4 >>> 57);
    }
    
    protected static void implSquare(final long[] array, final long[] array2) {
        Interleave.expand64To128(array[0], array2, 0);
        Interleave.expand64To128(array[1], array2, 2);
    }
    
    public static void invert(final long[] array, final long[] array2) {
        if (!Nat128.isZero64(array)) {
            final long[] create64 = Nat128.create64();
            final long[] create65 = Nat128.create64();
            square(array, create64);
            multiply(create64, array, create64);
            square(create64, create64);
            multiply(create64, array, create64);
            squareN(create64, 3, create65);
            multiply(create65, create64, create65);
            square(create65, create65);
            multiply(create65, array, create65);
            squareN(create65, 7, create64);
            multiply(create64, create65, create64);
            squareN(create64, 14, create65);
            multiply(create65, create64, create65);
            squareN(create65, 28, create64);
            multiply(create64, create65, create64);
            squareN(create64, 56, create65);
            multiply(create65, create64, create65);
            square(create65, array2);
            return;
        }
        throw new IllegalStateException();
    }
    
    public static void multiply(final long[] array, final long[] array2, final long[] array3) {
        final long[] ext64 = Nat128.createExt64();
        implMultiply(array, array2, ext64);
        reduce(ext64, array3);
    }
    
    public static void multiplyAddToExt(final long[] array, final long[] array2, final long[] array3) {
        final long[] ext64 = Nat128.createExt64();
        implMultiply(array, array2, ext64);
        addExt(array3, ext64, array3);
    }
    
    public static void reduce(final long[] array, final long[] array2) {
        final long n = array[0];
        final long n2 = array[1];
        final long n3 = array[2];
        final long n4 = array[3];
        final long n5 = n3 ^ (n4 >>> 40 ^ n4 >>> 49);
        final long n6 = n2 ^ (n4 << 15 ^ n4 << 24) ^ (n5 >>> 40 ^ n5 >>> 49);
        final long n7 = n6 >>> 49;
        array2[0] = (n ^ (n5 << 15 ^ n5 << 24) ^ n7 ^ n7 << 9);
        array2[1] = (n6 & 0x1FFFFFFFFFFFFL);
    }
    
    public static void reduce15(final long[] array, final int n) {
        final int n2 = n + 1;
        final long n3 = array[n2];
        final long n4 = n3 >>> 49;
        array[n] ^= (n4 ^ n4 << 9);
        array[n2] = (n3 & 0x1FFFFFFFFFFFFL);
    }
    
    public static void sqrt(final long[] array, final long[] array2) {
        final long unshuffle = Interleave.unshuffle(array[0]);
        final long unshuffle2 = Interleave.unshuffle(array[1]);
        final long n = unshuffle >>> 32 | (unshuffle2 & 0xFFFFFFFF00000000L);
        array2[0] = (n << 57 ^ ((unshuffle & 0xFFFFFFFFL) | unshuffle2 << 32) ^ n << 5);
        array2[1] = (n >>> 59 ^ n >>> 7);
    }
    
    public static void square(final long[] array, final long[] array2) {
        final long[] ext64 = Nat128.createExt64();
        implSquare(array, ext64);
        reduce(ext64, array2);
    }
    
    public static void squareAddToExt(final long[] array, final long[] array2) {
        final long[] ext64 = Nat128.createExt64();
        implSquare(array, ext64);
        addExt(array2, ext64, array2);
    }
    
    public static void squareN(final long[] array, int n, final long[] array2) {
        final long[] ext64 = Nat128.createExt64();
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
