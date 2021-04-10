package org.spongycastle.math.ec.custom.sec;

import org.spongycastle.math.raw.*;
import java.math.*;

public class SecP224R1Field
{
    private static final long M = 4294967295L;
    static final int[] P;
    private static final int P6 = -1;
    static final int[] PExt;
    private static final int PExt13 = -1;
    private static final int[] PExtInv;
    
    static {
        P = new int[] { 1, 0, 0, -1, -1, -1, -1 };
        PExt = new int[] { 1, 0, 0, -2, -1, -1, 0, 2, 0, 0, -2, -1, -1, -1 };
        PExtInv = new int[] { -1, -1, -1, 1, 0, 0, -1, -3, -1, -1, 1 };
    }
    
    public static void add(final int[] array, final int[] array2, final int[] array3) {
        if (Nat224.add(array, array2, array3) != 0 || (array3[6] == -1 && Nat224.gte(array3, SecP224R1Field.P))) {
            addPInvTo(array3);
        }
    }
    
    public static void addExt(int[] pExtInv, final int[] array, final int[] array2) {
        if (Nat.add(14, pExtInv, array, array2) != 0 || (array2[13] == -1 && Nat.gte(14, array2, SecP224R1Field.PExt))) {
            pExtInv = SecP224R1Field.PExtInv;
            if (Nat.addTo(pExtInv.length, pExtInv, array2) != 0) {
                Nat.incAt(14, array2, SecP224R1Field.PExtInv.length);
            }
        }
    }
    
    public static void addOne(final int[] array, final int[] array2) {
        if (Nat.inc(7, array, array2) != 0 || (array2[6] == -1 && Nat224.gte(array2, SecP224R1Field.P))) {
            addPInvTo(array2);
        }
    }
    
    private static void addPInvTo(final int[] array) {
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
        final long n6 = n3 + (((long)array[3] & 0xFFFFFFFFL) + 1L);
        array[3] = (int)n6;
        if (n6 >> 32 != 0L) {
            Nat.incAt(7, array, 4);
        }
    }
    
    public static int[] fromBigInteger(final BigInteger bigInteger) {
        final int[] fromBigInteger = Nat224.fromBigInteger(bigInteger);
        if (fromBigInteger[6] == -1 && Nat224.gte(fromBigInteger, SecP224R1Field.P)) {
            Nat224.subFrom(SecP224R1Field.P, fromBigInteger);
        }
        return fromBigInteger;
    }
    
    public static void half(final int[] array, final int[] array2) {
        if ((array[0] & 0x1) == 0x0) {
            Nat.shiftDownBit(7, array, 0, array2);
            return;
        }
        Nat.shiftDownBit(7, array2, Nat224.add(array, SecP224R1Field.P, array2));
    }
    
    public static void multiply(final int[] array, final int[] array2, final int[] array3) {
        final int[] ext = Nat224.createExt();
        Nat224.mul(array, array2, ext);
        reduce(ext, array3);
    }
    
    public static void multiplyAddToExt(int[] pExtInv, final int[] array, final int[] array2) {
        if (Nat224.mulAddTo(pExtInv, array, array2) != 0 || (array2[13] == -1 && Nat.gte(14, array2, SecP224R1Field.PExt))) {
            pExtInv = SecP224R1Field.PExtInv;
            if (Nat.addTo(pExtInv.length, pExtInv, array2) != 0) {
                Nat.incAt(14, array2, SecP224R1Field.PExtInv.length);
            }
        }
    }
    
    public static void negate(final int[] array, final int[] array2) {
        if (Nat224.isZero(array)) {
            Nat224.zero(array2);
            return;
        }
        Nat224.sub(SecP224R1Field.P, array, array2);
    }
    
    public static void reduce(final int[] array, final int[] array2) {
        final long n = (long)array[10] & 0xFFFFFFFFL;
        final long n2 = (long)array[11] & 0xFFFFFFFFL;
        final long n3 = (long)array[12] & 0xFFFFFFFFL;
        final long n4 = (long)array[13] & 0xFFFFFFFFL;
        final long n5 = ((long)array[7] & 0xFFFFFFFFL) + n2 - 1L;
        final long n6 = ((long)array[8] & 0xFFFFFFFFL) + n3;
        final long n7 = ((long)array[9] & 0xFFFFFFFFL) + n4;
        final long n8 = ((long)array[0] & 0xFFFFFFFFL) - n5 + 0L;
        final long n9 = (n8 >> 32) + (((long)array[1] & 0xFFFFFFFFL) - n6);
        array2[1] = (int)n9;
        final long n10 = (n9 >> 32) + (((long)array[2] & 0xFFFFFFFFL) - n7);
        array2[2] = (int)n10;
        final long n11 = (n10 >> 32) + (((long)array[3] & 0xFFFFFFFFL) + n5 - n);
        final long n12 = (n11 >> 32) + (((long)array[4] & 0xFFFFFFFFL) + n6 - n2);
        array2[4] = (int)n12;
        final long n13 = (n12 >> 32) + (((long)array[5] & 0xFFFFFFFFL) + n7 - n3);
        array2[5] = (int)n13;
        final long n14 = (n13 >> 32) + (((long)array[6] & 0xFFFFFFFFL) + n - n4);
        array2[6] = (int)n14;
        final long n15 = (n14 >> 32) + 1L;
        long n16 = (n11 & 0xFFFFFFFFL) + n15;
        final long n17 = (n8 & 0xFFFFFFFFL) - n15;
        array2[0] = (int)n17;
        final long n18 = n17 >> 32;
        if (n18 != 0L) {
            final long n19 = n18 + ((long)array2[1] & 0xFFFFFFFFL);
            array2[1] = (int)n19;
            final long n20 = (n19 >> 32) + ((long)array2[2] & 0xFFFFFFFFL);
            array2[2] = (int)n20;
            n16 += n20 >> 32;
        }
        array2[3] = (int)n16;
        if ((n16 >> 32 != 0L && Nat.incAt(7, array2, 4) != 0) || (array2[6] == -1 && Nat224.gte(array2, SecP224R1Field.P))) {
            addPInvTo(array2);
        }
    }
    
    public static void reduce32(final int n, final int[] array) {
        long n9;
        if (n != 0) {
            final long n2 = (long)n & 0xFFFFFFFFL;
            final long n3 = ((long)array[0] & 0xFFFFFFFFL) - n2 + 0L;
            array[0] = (int)n3;
            long n5;
            final long n4 = n5 = n3 >> 32;
            if (n4 != 0L) {
                final long n6 = n4 + ((long)array[1] & 0xFFFFFFFFL);
                array[1] = (int)n6;
                final long n7 = (n6 >> 32) + ((long)array[2] & 0xFFFFFFFFL);
                array[2] = (int)n7;
                n5 = n7 >> 32;
            }
            final long n8 = n5 + (((long)array[3] & 0xFFFFFFFFL) + n2);
            array[3] = (int)n8;
            n9 = n8 >> 32;
        }
        else {
            n9 = 0L;
        }
        if ((n9 != 0L && Nat.incAt(7, array, 4) != 0) || (array[6] == -1 && Nat224.gte(array, SecP224R1Field.P))) {
            addPInvTo(array);
        }
    }
    
    public static void square(final int[] array, final int[] array2) {
        final int[] ext = Nat224.createExt();
        Nat224.square(array, ext);
        reduce(ext, array2);
    }
    
    public static void squareN(final int[] array, int n, final int[] array2) {
        final int[] ext = Nat224.createExt();
        Nat224.square(array, ext);
        while (true) {
            reduce(ext, array2);
            --n;
            if (n <= 0) {
                break;
            }
            Nat224.square(array2, ext);
        }
    }
    
    private static void subPInvFrom(final int[] array) {
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
        final long n6 = n3 + (((long)array[3] & 0xFFFFFFFFL) - 1L);
        array[3] = (int)n6;
        if (n6 >> 32 != 0L) {
            Nat.decAt(7, array, 4);
        }
    }
    
    public static void subtract(final int[] array, final int[] array2, final int[] array3) {
        if (Nat224.sub(array, array2, array3) != 0) {
            subPInvFrom(array3);
        }
    }
    
    public static void subtractExt(int[] pExtInv, final int[] array, final int[] array2) {
        if (Nat.sub(14, pExtInv, array, array2) != 0) {
            pExtInv = SecP224R1Field.PExtInv;
            if (Nat.subFrom(pExtInv.length, pExtInv, array2) != 0) {
                Nat.decAt(14, array2, SecP224R1Field.PExtInv.length);
            }
        }
    }
    
    public static void twice(final int[] array, final int[] array2) {
        if (Nat.shiftUpBit(7, array, 0, array2) != 0 || (array2[6] == -1 && Nat224.gte(array2, SecP224R1Field.P))) {
            addPInvTo(array2);
        }
    }
}
