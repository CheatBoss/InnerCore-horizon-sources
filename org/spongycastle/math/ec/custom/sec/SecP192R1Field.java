package org.spongycastle.math.ec.custom.sec;

import org.spongycastle.math.raw.*;
import java.math.*;

public class SecP192R1Field
{
    private static final long M = 4294967295L;
    static final int[] P;
    private static final int P5 = -1;
    static final int[] PExt;
    private static final int PExt11 = -1;
    private static final int[] PExtInv;
    
    static {
        P = new int[] { -1, -1, -2, -1, -1, -1 };
        PExt = new int[] { 1, 0, 2, 0, 1, 0, -2, -1, -3, -1, -1, -1 };
        PExtInv = new int[] { -1, -1, -3, -1, -2, -1, 1, 0, 2 };
    }
    
    public static void add(final int[] array, final int[] array2, final int[] array3) {
        if (Nat192.add(array, array2, array3) != 0 || (array3[5] == -1 && Nat192.gte(array3, SecP192R1Field.P))) {
            addPInvTo(array3);
        }
    }
    
    public static void addExt(int[] pExtInv, final int[] array, final int[] array2) {
        if (Nat.add(12, pExtInv, array, array2) != 0 || (array2[11] == -1 && Nat.gte(12, array2, SecP192R1Field.PExt))) {
            pExtInv = SecP192R1Field.PExtInv;
            if (Nat.addTo(pExtInv.length, pExtInv, array2) != 0) {
                Nat.incAt(12, array2, SecP192R1Field.PExtInv.length);
            }
        }
    }
    
    public static void addOne(final int[] array, final int[] array2) {
        if (Nat.inc(6, array, array2) != 0 || (array2[5] == -1 && Nat192.gte(array2, SecP192R1Field.P))) {
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
            n3 = n4 >> 32;
        }
        final long n5 = n3 + (((long)array[2] & 0xFFFFFFFFL) + 1L);
        array[2] = (int)n5;
        if (n5 >> 32 != 0L) {
            Nat.incAt(6, array, 3);
        }
    }
    
    public static int[] fromBigInteger(final BigInteger bigInteger) {
        final int[] fromBigInteger = Nat192.fromBigInteger(bigInteger);
        if (fromBigInteger[5] == -1 && Nat192.gte(fromBigInteger, SecP192R1Field.P)) {
            Nat192.subFrom(SecP192R1Field.P, fromBigInteger);
        }
        return fromBigInteger;
    }
    
    public static void half(final int[] array, final int[] array2) {
        if ((array[0] & 0x1) == 0x0) {
            Nat.shiftDownBit(6, array, 0, array2);
            return;
        }
        Nat.shiftDownBit(6, array2, Nat192.add(array, SecP192R1Field.P, array2));
    }
    
    public static void multiply(final int[] array, final int[] array2, final int[] array3) {
        final int[] ext = Nat192.createExt();
        Nat192.mul(array, array2, ext);
        reduce(ext, array3);
    }
    
    public static void multiplyAddToExt(int[] pExtInv, final int[] array, final int[] array2) {
        if (Nat192.mulAddTo(pExtInv, array, array2) != 0 || (array2[11] == -1 && Nat.gte(12, array2, SecP192R1Field.PExt))) {
            pExtInv = SecP192R1Field.PExtInv;
            if (Nat.addTo(pExtInv.length, pExtInv, array2) != 0) {
                Nat.incAt(12, array2, SecP192R1Field.PExtInv.length);
            }
        }
    }
    
    public static void negate(final int[] array, final int[] array2) {
        if (Nat192.isZero(array)) {
            Nat192.zero(array2);
            return;
        }
        Nat192.sub(SecP192R1Field.P, array, array2);
    }
    
    public static void reduce(final int[] array, final int[] array2) {
        final long n = (long)array[6] & 0xFFFFFFFFL;
        final long n2 = (long)array[7] & 0xFFFFFFFFL;
        final long n3 = array[8];
        final long n4 = array[9];
        final long n5 = array[10];
        final long n6 = array[11];
        final long n7 = (n5 & 0xFFFFFFFFL) + n;
        final long n8 = (n6 & 0xFFFFFFFFL) + n2;
        final long n9 = ((long)array[0] & 0xFFFFFFFFL) + n7 + 0L;
        final int n10 = (int)n9;
        final long n11 = (n9 >> 32) + (((long)array[1] & 0xFFFFFFFFL) + n8);
        array2[1] = (int)n11;
        final long n12 = n7 + (n3 & 0xFFFFFFFFL);
        final long n13 = n8 + (n4 & 0xFFFFFFFFL);
        final long n14 = (n11 >> 32) + (((long)array[2] & 0xFFFFFFFFL) + n12);
        final long n15 = (n14 >> 32) + (((long)array[3] & 0xFFFFFFFFL) + n13);
        array2[3] = (int)n15;
        final long n16 = (n15 >> 32) + (((long)array[4] & 0xFFFFFFFFL) + (n12 - n));
        array2[4] = (int)n16;
        final long n17 = (n16 >> 32) + (((long)array[5] & 0xFFFFFFFFL) + (n13 - n2));
        array2[5] = (int)n17;
        final long n18 = n17 >> 32;
        long n19 = (n14 & 0xFFFFFFFFL) + n18;
        final long n20 = n18 + ((long)n10 & 0xFFFFFFFFL);
        array2[0] = (int)n20;
        final long n21 = n20 >> 32;
        if (n21 != 0L) {
            final long n22 = n21 + ((long)array2[1] & 0xFFFFFFFFL);
            array2[1] = (int)n22;
            n19 += n22 >> 32;
        }
        array2[2] = (int)n19;
        if ((n19 >> 32 != 0L && Nat.incAt(6, array2, 3) != 0) || (array2[5] == -1 && Nat192.gte(array2, SecP192R1Field.P))) {
            addPInvTo(array2);
        }
    }
    
    public static void reduce32(final int n, final int[] array) {
        long n8;
        if (n != 0) {
            final long n2 = (long)n & 0xFFFFFFFFL;
            final long n3 = ((long)array[0] & 0xFFFFFFFFL) + n2 + 0L;
            array[0] = (int)n3;
            long n5;
            final long n4 = n5 = n3 >> 32;
            if (n4 != 0L) {
                final long n6 = n4 + ((long)array[1] & 0xFFFFFFFFL);
                array[1] = (int)n6;
                n5 = n6 >> 32;
            }
            final long n7 = n5 + (((long)array[2] & 0xFFFFFFFFL) + n2);
            array[2] = (int)n7;
            n8 = n7 >> 32;
        }
        else {
            n8 = 0L;
        }
        if ((n8 != 0L && Nat.incAt(6, array, 3) != 0) || (array[5] == -1 && Nat192.gte(array, SecP192R1Field.P))) {
            addPInvTo(array);
        }
    }
    
    public static void square(final int[] array, final int[] array2) {
        final int[] ext = Nat192.createExt();
        Nat192.square(array, ext);
        reduce(ext, array2);
    }
    
    public static void squareN(final int[] array, int n, final int[] array2) {
        final int[] ext = Nat192.createExt();
        Nat192.square(array, ext);
        while (true) {
            reduce(ext, array2);
            --n;
            if (n <= 0) {
                break;
            }
            Nat192.square(array2, ext);
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
            n3 = n4 >> 32;
        }
        final long n5 = n3 + (((long)array[2] & 0xFFFFFFFFL) - 1L);
        array[2] = (int)n5;
        if (n5 >> 32 != 0L) {
            Nat.decAt(6, array, 3);
        }
    }
    
    public static void subtract(final int[] array, final int[] array2, final int[] array3) {
        if (Nat192.sub(array, array2, array3) != 0) {
            subPInvFrom(array3);
        }
    }
    
    public static void subtractExt(int[] pExtInv, final int[] array, final int[] array2) {
        if (Nat.sub(12, pExtInv, array, array2) != 0) {
            pExtInv = SecP192R1Field.PExtInv;
            if (Nat.subFrom(pExtInv.length, pExtInv, array2) != 0) {
                Nat.decAt(12, array2, SecP192R1Field.PExtInv.length);
            }
        }
    }
    
    public static void twice(final int[] array, final int[] array2) {
        if (Nat.shiftUpBit(6, array, 0, array2) != 0 || (array2[5] == -1 && Nat192.gte(array2, SecP192R1Field.P))) {
            addPInvTo(array2);
        }
    }
}
