package org.spongycastle.math.ec.custom.sec;

import java.math.*;
import org.spongycastle.math.raw.*;

public class SecT131Field
{
    private static final long M03 = 7L;
    private static final long M44 = 17592186044415L;
    private static final long[] ROOT_Z;
    
    static {
        ROOT_Z = new long[] { 2791191049453778211L, 2791191049453778402L, 6L };
    }
    
    public static void add(final long[] array, final long[] array2, final long[] array3) {
        array3[0] = (array[0] ^ array2[0]);
        array3[1] = (array[1] ^ array2[1]);
        array3[2] = (array2[2] ^ array[2]);
    }
    
    public static void addExt(final long[] array, final long[] array2, final long[] array3) {
        array3[0] = (array[0] ^ array2[0]);
        array3[1] = (array[1] ^ array2[1]);
        array3[2] = (array[2] ^ array2[2]);
        array3[3] = (array[3] ^ array2[3]);
        array3[4] = (array2[4] ^ array[4]);
    }
    
    public static void addOne(final long[] array, final long[] array2) {
        array2[0] = (array[0] ^ 0x1L);
        array2[1] = array[1];
        array2[2] = array[2];
    }
    
    public static long[] fromBigInteger(final BigInteger bigInteger) {
        final long[] fromBigInteger64 = Nat192.fromBigInteger64(bigInteger);
        reduce61(fromBigInteger64, 0);
        return fromBigInteger64;
    }
    
    protected static void implCompactExt(final long[] array) {
        final long n = array[0];
        final long n2 = array[1];
        final long n3 = array[2];
        final long n4 = array[3];
        final long n5 = array[4];
        final long n6 = array[5];
        array[0] = (n ^ n2 << 44);
        array[1] = (n2 >>> 20 ^ n3 << 24);
        array[2] = (n3 >>> 40 ^ n4 << 4 ^ n5 << 48);
        array[3] = (n4 >>> 60 ^ n6 << 28 ^ n5 >>> 16);
        array[4] = n6 >>> 36;
        array[5] = 0L;
    }
    
    protected static void implMultiply(long[] array, final long[] array2, final long[] array3) {
        final long n = array[0];
        final long n2 = array[1];
        final long n3 = (array[2] << 40 ^ n2 >>> 24) & 0xFFFFFFFFFFFL;
        final long n4 = (n2 << 20 ^ n >>> 44) & 0xFFFFFFFFFFFL;
        final long n5 = n & 0xFFFFFFFFFFFL;
        final long n6 = array2[0];
        final long n7 = array2[1];
        final long n8 = (n7 >>> 24 ^ array2[2] << 40) & 0xFFFFFFFFFFFL;
        final long n9 = (n6 >>> 44 ^ n7 << 20) & 0xFFFFFFFFFFFL;
        final long n10 = n6 & 0xFFFFFFFFFFFL;
        array = new long[10];
        implMulw(n5, n10, array, 0);
        implMulw(n3, n8, array, 2);
        final long n11 = n5 ^ n4 ^ n3;
        final long n12 = n10 ^ n9 ^ n8;
        implMulw(n11, n12, array, 4);
        final long n13 = n4 << 1 ^ n3 << 2;
        final long n14 = n9 << 1 ^ n8 << 2;
        implMulw(n5 ^ n13, n10 ^ n14, array, 6);
        implMulw(n11 ^ n13, n12 ^ n14, array, 8);
        final long n15 = array[6] ^ array[8];
        final long n16 = array[7] ^ array[9];
        final long n17 = array[6];
        final long n18 = array[7];
        final long n19 = array[0];
        final long n20 = array[1] ^ array[0] ^ array[4];
        final long n21 = array[1] ^ array[5];
        final long n22 = n15 << 1 ^ n17 ^ n19 ^ array[2] << 4 ^ array[2] << 1;
        final long n23 = n20 ^ (n15 ^ n16 << 1 ^ n18) ^ array[3] << 4 ^ array[3] << 1 ^ n22 >>> 44;
        final long n24 = n21 ^ n16 ^ n23 >>> 44;
        final long n25 = n23 & 0xFFFFFFFFFFFL;
        final long n26 = (n22 & 0xFFFFFFFFFFFL) >>> 1 ^ (n25 & 0x1L) << 43;
        final long n27 = n26 ^ n26 << 1;
        final long n28 = n27 ^ n27 << 2;
        final long n29 = n28 ^ n28 << 4;
        final long n30 = n29 ^ n29 << 8;
        final long n31 = n30 ^ n30 << 16;
        final long n32 = (n31 ^ n31 << 32) & 0xFFFFFFFFFFFL;
        final long n33 = n25 >>> 1 ^ (n24 & 0x1L) << 43 ^ n32 >>> 43;
        final long n34 = n33 ^ n33 << 1;
        final long n35 = n34 ^ n34 << 2;
        final long n36 = n35 ^ n35 << 4;
        final long n37 = n36 ^ n36 << 8;
        final long n38 = n37 ^ n37 << 16;
        final long n39 = (n38 ^ n38 << 32) & 0xFFFFFFFFFFFL;
        final long n40 = n39 >>> 43 ^ n24 >>> 1;
        final long n41 = n40 ^ n40 << 1;
        final long n42 = n41 ^ n41 << 2;
        final long n43 = n42 ^ n42 << 4;
        final long n44 = n43 ^ n43 << 8;
        final long n45 = n44 ^ n44 << 16;
        final long n46 = n45 ^ n45 << 32;
        array3[0] = n19;
        array3[1] = (n20 ^ n32 ^ array[2]);
        array3[2] = (n21 ^ n39 ^ n32 ^ array[3]);
        array3[3] = (n46 ^ n39);
        array3[4] = (array[2] ^ n46);
        array3[5] = array[3];
        implCompactExt(array3);
    }
    
    protected static void implMulw(final long n, long n2, final long[] array, final int n3) {
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
        long n5 = array2[n4 >>> 6 & 0x7] << 6 ^ (n2 ^ array2[n4 >>> 3 & 0x7] << 3);
        n2 = 0L;
        int n6 = 33;
        do {
            final int n7 = (int)(n >>> n6);
            final long n8 = array2[n7 >>> 9 & 0x7] << 9 ^ (array2[n7 & 0x7] ^ array2[n7 >>> 3 & 0x7] << 3 ^ array2[n7 >>> 6 & 0x7] << 6);
            n5 ^= n8 << n6;
            n2 ^= n8 >>> -n6;
            n6 -= 12;
        } while (n6 > 0);
        array[n3] = (n5 & 0xFFFFFFFFFFFL);
        array[n3 + 1] = (n5 >>> 44 ^ n2 << 20);
    }
    
    protected static void implSquare(final long[] array, final long[] array2) {
        Interleave.expand64To128(array[0], array2, 0);
        Interleave.expand64To128(array[1], array2, 2);
        array2[4] = ((long)Interleave.expand8to16((int)array[2]) & 0xFFFFFFFFL);
    }
    
    public static void invert(final long[] array, final long[] array2) {
        if (!Nat192.isZero64(array)) {
            final long[] create64 = Nat192.create64();
            final long[] create65 = Nat192.create64();
            square(array, create64);
            multiply(create64, array, create64);
            squareN(create64, 2, create65);
            multiply(create65, create64, create65);
            squareN(create65, 4, create64);
            multiply(create64, create65, create64);
            squareN(create64, 8, create65);
            multiply(create65, create64, create65);
            squareN(create65, 16, create64);
            multiply(create64, create65, create64);
            squareN(create64, 32, create65);
            multiply(create65, create64, create65);
            square(create65, create65);
            multiply(create65, array, create65);
            squareN(create65, 65, create64);
            multiply(create64, create65, create64);
            square(create64, array2);
            return;
        }
        throw new IllegalStateException();
    }
    
    public static void multiply(final long[] array, final long[] array2, final long[] array3) {
        final long[] ext64 = Nat192.createExt64();
        implMultiply(array, array2, ext64);
        reduce(ext64, array3);
    }
    
    public static void multiplyAddToExt(final long[] array, final long[] array2, final long[] array3) {
        final long[] ext64 = Nat192.createExt64();
        implMultiply(array, array2, ext64);
        addExt(array3, ext64, array3);
    }
    
    public static void reduce(final long[] array, final long[] array2) {
        final long n = array[0];
        final long n2 = array[1];
        final long n3 = array[2];
        final long n4 = array[3];
        final long n5 = array[4];
        final long n6 = n4 ^ n5 >>> 59;
        final long n7 = n3 ^ (n5 >>> 3 ^ n5 >>> 1 ^ n5 ^ n5 << 5) ^ n6 >>> 59;
        final long n8 = n7 >>> 3;
        array2[0] = (n ^ (n6 << 61 ^ n6 << 63) ^ n8 ^ n8 << 2 ^ n8 << 3 ^ n8 << 8);
        array2[1] = (n8 >>> 56 ^ (n2 ^ (n5 << 61 ^ n5 << 63) ^ (n6 >>> 3 ^ n6 >>> 1 ^ n6 ^ n6 << 5)));
        array2[2] = (n7 & 0x7L);
    }
    
    public static void reduce61(final long[] array, int n) {
        final int n2 = n + 2;
        final long n3 = array[n2];
        final long n4 = n3 >>> 3;
        array[n] ^= (n4 << 2 ^ n4 ^ n4 << 3 ^ n4 << 8);
        ++n;
        array[n] ^= n4 >>> 56;
        array[n2] = (n3 & 0x7L);
    }
    
    public static void sqrt(final long[] array, final long[] array2) {
        final long[] create64 = Nat192.create64();
        final long unshuffle = Interleave.unshuffle(array[0]);
        final long unshuffle2 = Interleave.unshuffle(array[1]);
        create64[0] = (unshuffle >>> 32 | (unshuffle2 & 0xFFFFFFFF00000000L));
        final long unshuffle3 = Interleave.unshuffle(array[2]);
        create64[1] = unshuffle3 >>> 32;
        multiply(create64, SecT131Field.ROOT_Z, array2);
        array2[0] ^= ((unshuffle & 0xFFFFFFFFL) | unshuffle2 << 32);
        array2[1] ^= (unshuffle3 & 0xFFFFFFFFL);
    }
    
    public static void square(final long[] array, final long[] array2) {
        final long[] create64 = Nat.create64(5);
        implSquare(array, create64);
        reduce(create64, array2);
    }
    
    public static void squareAddToExt(final long[] array, final long[] array2) {
        final long[] create64 = Nat.create64(5);
        implSquare(array, create64);
        addExt(array2, create64, array2);
    }
    
    public static void squareN(final long[] array, int n, final long[] array2) {
        final long[] create64 = Nat.create64(5);
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
        return (int)(array[0] ^ array[1] >>> 59 ^ array[2] >>> 1) & 0x1;
    }
}
