package org.spongycastle.math.ec.custom.sec;

import org.spongycastle.math.raw.*;
import java.math.*;

public class SecP128R1Field
{
    private static final long M = 4294967295L;
    static final int[] P;
    private static final int P3s1 = 2147483646;
    static final int[] PExt;
    private static final int PExt7s1 = 2147483646;
    private static final int[] PExtInv;
    
    static {
        P = new int[] { -1, -1, -1, -3 };
        PExt = new int[] { 1, 0, 0, 4, -2, -1, 3, -4 };
        PExtInv = new int[] { -1, -1, -1, -5, 1, 0, -4, 3 };
    }
    
    public static void add(final int[] array, final int[] array2, final int[] array3) {
        if (Nat128.add(array, array2, array3) != 0 || (array3[3] >>> 1 >= 2147483646 && Nat128.gte(array3, SecP128R1Field.P))) {
            addPInvTo(array3);
        }
    }
    
    public static void addExt(int[] pExtInv, final int[] array, final int[] array2) {
        if (Nat256.add(pExtInv, array, array2) != 0 || (array2[7] >>> 1 >= 2147483646 && Nat256.gte(array2, SecP128R1Field.PExt))) {
            pExtInv = SecP128R1Field.PExtInv;
            Nat.addTo(pExtInv.length, pExtInv, array2);
        }
    }
    
    public static void addOne(final int[] array, final int[] array2) {
        if (Nat.inc(4, array, array2) != 0 || (array2[3] >>> 1 >= 2147483646 && Nat128.gte(array2, SecP128R1Field.P))) {
            addPInvTo(array2);
        }
    }
    
    private static void addPInvTo(final int[] array) {
        final long n = ((long)array[0] & 0xFFFFFFFFL) + 1L;
        array[0] = (int)n;
        long n3;
        final long n2 = n3 = n >> 32;
        if (n2 != 0L) {
            final long n4 = n2 + ((long)array[1] & 0xFFFFFFFFL);
            array[1] = (int)n4;
            final long n5 = (n4 >> 32) + ((long)array[2] & 0xFFFFFFFFL);
            array[2] = (int)n5;
            n3 = n5 >> 32;
        }
        array[3] = (int)(n3 + (((long)array[3] & 0xFFFFFFFFL) + 2L));
    }
    
    public static int[] fromBigInteger(final BigInteger bigInteger) {
        final int[] fromBigInteger = Nat128.fromBigInteger(bigInteger);
        if (fromBigInteger[3] >>> 1 >= 2147483646 && Nat128.gte(fromBigInteger, SecP128R1Field.P)) {
            Nat128.subFrom(SecP128R1Field.P, fromBigInteger);
        }
        return fromBigInteger;
    }
    
    public static void half(final int[] array, final int[] array2) {
        if ((array[0] & 0x1) == 0x0) {
            Nat.shiftDownBit(4, array, 0, array2);
            return;
        }
        Nat.shiftDownBit(4, array2, Nat128.add(array, SecP128R1Field.P, array2));
    }
    
    public static void multiply(final int[] array, final int[] array2, final int[] array3) {
        final int[] ext = Nat128.createExt();
        Nat128.mul(array, array2, ext);
        reduce(ext, array3);
    }
    
    public static void multiplyAddToExt(int[] pExtInv, final int[] array, final int[] array2) {
        if (Nat128.mulAddTo(pExtInv, array, array2) != 0 || (array2[7] >>> 1 >= 2147483646 && Nat256.gte(array2, SecP128R1Field.PExt))) {
            pExtInv = SecP128R1Field.PExtInv;
            Nat.addTo(pExtInv.length, pExtInv, array2);
        }
    }
    
    public static void negate(final int[] array, final int[] array2) {
        if (Nat128.isZero(array)) {
            Nat128.zero(array2);
            return;
        }
        Nat128.sub(SecP128R1Field.P, array, array2);
    }
    
    public static void reduce(final int[] array, final int[] array2) {
        final long n = array[0];
        final long n2 = array[1];
        final long n3 = array[2];
        final long n4 = array[3];
        final long n5 = array[4];
        final long n6 = array[5];
        final long n7 = array[6];
        final long n8 = (long)array[7] & 0xFFFFFFFFL;
        final long n9 = (n7 & 0xFFFFFFFFL) + (n8 << 1);
        final long n10 = (n6 & 0xFFFFFFFFL) + (n9 << 1);
        final long n11 = (n5 & 0xFFFFFFFFL) + (n10 << 1);
        final long n12 = (n & 0xFFFFFFFFL) + n11;
        array2[0] = (int)n12;
        final long n13 = (n2 & 0xFFFFFFFFL) + n10 + (n12 >>> 32);
        array2[1] = (int)n13;
        final long n14 = (n3 & 0xFFFFFFFFL) + n9 + (n13 >>> 32);
        array2[2] = (int)n14;
        final long n15 = (n4 & 0xFFFFFFFFL) + n8 + (n11 << 1) + (n14 >>> 32);
        array2[3] = (int)n15;
        reduce32((int)(n15 >>> 32), array2);
    }
    
    public static void reduce32(int i, final int[] array) {
        while (i != 0) {
            final long n = (long)i & 0xFFFFFFFFL;
            final long n2 = ((long)array[0] & 0xFFFFFFFFL) + n;
            array[0] = (int)n2;
            long n4;
            final long n3 = n4 = n2 >> 32;
            if (n3 != 0L) {
                final long n5 = n3 + ((long)array[1] & 0xFFFFFFFFL);
                array[1] = (int)n5;
                final long n6 = (n5 >> 32) + ((long)array[2] & 0xFFFFFFFFL);
                array[2] = (int)n6;
                n4 = n6 >> 32;
            }
            final long n7 = n4 + (((long)array[3] & 0xFFFFFFFFL) + (n << 1));
            array[3] = (int)n7;
            i = (int)(n7 >> 32);
        }
    }
    
    public static void square(final int[] array, final int[] array2) {
        final int[] ext = Nat128.createExt();
        Nat128.square(array, ext);
        reduce(ext, array2);
    }
    
    public static void squareN(final int[] array, int n, final int[] array2) {
        final int[] ext = Nat128.createExt();
        Nat128.square(array, ext);
        while (true) {
            reduce(ext, array2);
            --n;
            if (n <= 0) {
                break;
            }
            Nat128.square(array2, ext);
        }
    }
    
    private static void subPInvFrom(final int[] array) {
        final long n = ((long)array[0] & 0xFFFFFFFFL) - 1L;
        array[0] = (int)n;
        long n3;
        final long n2 = n3 = n >> 32;
        if (n2 != 0L) {
            final long n4 = n2 + ((long)array[1] & 0xFFFFFFFFL);
            array[1] = (int)n4;
            final long n5 = (n4 >> 32) + ((long)array[2] & 0xFFFFFFFFL);
            array[2] = (int)n5;
            n3 = n5 >> 32;
        }
        array[3] = (int)(n3 + (((long)array[3] & 0xFFFFFFFFL) - 2L));
    }
    
    public static void subtract(final int[] array, final int[] array2, final int[] array3) {
        if (Nat128.sub(array, array2, array3) != 0) {
            subPInvFrom(array3);
        }
    }
    
    public static void subtractExt(int[] pExtInv, final int[] array, final int[] array2) {
        if (Nat.sub(10, pExtInv, array, array2) != 0) {
            pExtInv = SecP128R1Field.PExtInv;
            Nat.subFrom(pExtInv.length, pExtInv, array2);
        }
    }
    
    public static void twice(final int[] array, final int[] array2) {
        if (Nat.shiftUpBit(4, array, 0, array2) != 0 || (array2[3] >>> 1 >= 2147483646 && Nat128.gte(array2, SecP128R1Field.P))) {
            addPInvTo(array2);
        }
    }
}
