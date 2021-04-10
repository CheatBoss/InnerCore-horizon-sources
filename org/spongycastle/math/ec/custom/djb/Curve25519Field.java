package org.spongycastle.math.ec.custom.djb;

import org.spongycastle.math.raw.*;
import java.math.*;

public class Curve25519Field
{
    private static final long M = 4294967295L;
    static final int[] P;
    private static final int P7 = Integer.MAX_VALUE;
    private static final int[] PExt;
    private static final int PInv = 19;
    
    static {
        P = new int[] { -19, -1, -1, -1, -1, -1, -1, Integer.MAX_VALUE };
        PExt = new int[] { 361, 0, 0, 0, 0, 0, 0, 0, -19, -1, -1, -1, -1, -1, -1, 1073741823 };
    }
    
    public static void add(final int[] array, final int[] array2, final int[] array3) {
        Nat256.add(array, array2, array3);
        if (Nat256.gte(array3, Curve25519Field.P)) {
            subPFrom(array3);
        }
    }
    
    public static void addExt(final int[] array, final int[] array2, final int[] array3) {
        Nat.add(16, array, array2, array3);
        if (Nat.gte(16, array3, Curve25519Field.PExt)) {
            subPExtFrom(array3);
        }
    }
    
    public static void addOne(final int[] array, final int[] array2) {
        Nat.inc(8, array, array2);
        if (Nat256.gte(array2, Curve25519Field.P)) {
            subPFrom(array2);
        }
    }
    
    private static int addPExtTo(final int[] array) {
        final long n = ((long)array[0] & 0xFFFFFFFFL) + ((long)Curve25519Field.PExt[0] & 0xFFFFFFFFL);
        array[0] = (int)n;
        long n2;
        if ((n2 = n >> 32) != 0L) {
            n2 = Nat.incAt(8, array, 1);
        }
        final long n3 = n2 + (((long)array[8] & 0xFFFFFFFFL) - 19L);
        array[8] = (int)n3;
        long n4;
        if ((n4 = n3 >> 32) != 0L) {
            n4 = Nat.decAt(15, array, 9);
        }
        final long n5 = n4 + (((long)array[15] & 0xFFFFFFFFL) + ((long)(Curve25519Field.PExt[15] + 1) & 0xFFFFFFFFL));
        array[15] = (int)n5;
        return (int)(n5 >> 32);
    }
    
    private static int addPTo(final int[] array) {
        final long n = ((long)array[0] & 0xFFFFFFFFL) - 19L;
        array[0] = (int)n;
        long n2;
        if ((n2 = n >> 32) != 0L) {
            n2 = Nat.decAt(7, array, 1);
        }
        final long n3 = n2 + (((long)array[7] & 0xFFFFFFFFL) + 2147483648L);
        array[7] = (int)n3;
        return (int)(n3 >> 32);
    }
    
    public static int[] fromBigInteger(final BigInteger bigInteger) {
        final int[] fromBigInteger = Nat256.fromBigInteger(bigInteger);
        while (Nat256.gte(fromBigInteger, Curve25519Field.P)) {
            Nat256.subFrom(Curve25519Field.P, fromBigInteger);
        }
        return fromBigInteger;
    }
    
    public static void half(final int[] array, final int[] array2) {
        if ((array[0] & 0x1) == 0x0) {
            Nat.shiftDownBit(8, array, 0, array2);
            return;
        }
        Nat256.add(array, Curve25519Field.P, array2);
        Nat.shiftDownBit(8, array2, 0);
    }
    
    public static void multiply(final int[] array, final int[] array2, final int[] array3) {
        final int[] ext = Nat256.createExt();
        Nat256.mul(array, array2, ext);
        reduce(ext, array3);
    }
    
    public static void multiplyAddToExt(final int[] array, final int[] array2, final int[] array3) {
        Nat256.mulAddTo(array, array2, array3);
        if (Nat.gte(16, array3, Curve25519Field.PExt)) {
            subPExtFrom(array3);
        }
    }
    
    public static void negate(final int[] array, final int[] array2) {
        if (Nat256.isZero(array)) {
            Nat256.zero(array2);
            return;
        }
        Nat256.sub(Curve25519Field.P, array, array2);
    }
    
    public static void reduce(final int[] array, final int[] array2) {
        final int n = array[7];
        Nat.shiftUpBit(8, array, 8, n, array2, 0);
        final int mulByWordAddTo = Nat256.mulByWordAddTo(19, array, array2);
        final int n2 = array2[7];
        array2[7] = (Integer.MAX_VALUE & n2) + Nat.addWordTo(7, ((mulByWordAddTo << 1) + ((n2 >>> 31) - (n >>> 31))) * 19, array2);
        if (Nat256.gte(array2, Curve25519Field.P)) {
            subPFrom(array2);
        }
    }
    
    public static void reduce27(final int n, final int[] array) {
        final int n2 = array[7];
        array[7] = (Integer.MAX_VALUE & n2) + Nat.addWordTo(7, (n << 1 | n2 >>> 31) * 19, array);
        if (Nat256.gte(array, Curve25519Field.P)) {
            subPFrom(array);
        }
    }
    
    public static void square(final int[] array, final int[] array2) {
        final int[] ext = Nat256.createExt();
        Nat256.square(array, ext);
        reduce(ext, array2);
    }
    
    public static void squareN(final int[] array, int n, final int[] array2) {
        final int[] ext = Nat256.createExt();
        Nat256.square(array, ext);
        while (true) {
            reduce(ext, array2);
            --n;
            if (n <= 0) {
                break;
            }
            Nat256.square(array2, ext);
        }
    }
    
    private static int subPExtFrom(final int[] array) {
        final long n = ((long)array[0] & 0xFFFFFFFFL) - ((long)Curve25519Field.PExt[0] & 0xFFFFFFFFL);
        array[0] = (int)n;
        long n2;
        if ((n2 = n >> 32) != 0L) {
            n2 = Nat.decAt(8, array, 1);
        }
        final long n3 = n2 + (((long)array[8] & 0xFFFFFFFFL) + 19L);
        array[8] = (int)n3;
        long n4;
        if ((n4 = n3 >> 32) != 0L) {
            n4 = Nat.incAt(15, array, 9);
        }
        final long n5 = n4 + (((long)array[15] & 0xFFFFFFFFL) - ((long)(Curve25519Field.PExt[15] + 1) & 0xFFFFFFFFL));
        array[15] = (int)n5;
        return (int)(n5 >> 32);
    }
    
    private static int subPFrom(final int[] array) {
        final long n = ((long)array[0] & 0xFFFFFFFFL) + 19L;
        array[0] = (int)n;
        long n2;
        if ((n2 = n >> 32) != 0L) {
            n2 = Nat.incAt(7, array, 1);
        }
        final long n3 = n2 + (((long)array[7] & 0xFFFFFFFFL) - 2147483648L);
        array[7] = (int)n3;
        return (int)(n3 >> 32);
    }
    
    public static void subtract(final int[] array, final int[] array2, final int[] array3) {
        if (Nat256.sub(array, array2, array3) != 0) {
            addPTo(array3);
        }
    }
    
    public static void subtractExt(final int[] array, final int[] array2, final int[] array3) {
        if (Nat.sub(16, array, array2, array3) != 0) {
            addPExtTo(array3);
        }
    }
    
    public static void twice(final int[] array, final int[] array2) {
        Nat.shiftUpBit(8, array, 0, array2);
        if (Nat256.gte(array2, Curve25519Field.P)) {
            subPFrom(array2);
        }
    }
}
