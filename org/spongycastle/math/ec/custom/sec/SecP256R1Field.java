package org.spongycastle.math.ec.custom.sec;

import org.spongycastle.math.raw.*;
import java.math.*;

public class SecP256R1Field
{
    private static final long M = 4294967295L;
    static final int[] P;
    private static final int P7 = -1;
    static final int[] PExt;
    private static final int PExt15s1 = Integer.MAX_VALUE;
    
    static {
        P = new int[] { -1, -1, -1, 0, 0, 0, 1, -1 };
        PExt = new int[] { 1, 0, 0, -2, -1, -1, -2, 1, -2, 1, -2, 1, 1, -2, 2, -2 };
    }
    
    public static void add(final int[] array, final int[] array2, final int[] array3) {
        if (Nat256.add(array, array2, array3) != 0 || (array3[7] == -1 && Nat256.gte(array3, SecP256R1Field.P))) {
            addPInvTo(array3);
        }
    }
    
    public static void addExt(final int[] array, final int[] array2, final int[] array3) {
        if (Nat.add(16, array, array2, array3) != 0 || (array3[15] >>> 1 >= Integer.MAX_VALUE && Nat.gte(16, array3, SecP256R1Field.PExt))) {
            Nat.subFrom(16, SecP256R1Field.PExt, array3);
        }
    }
    
    public static void addOne(final int[] array, final int[] array2) {
        if (Nat.inc(8, array, array2) != 0 || (array2[7] == -1 && Nat256.gte(array2, SecP256R1Field.P))) {
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
        final long n6 = n3 + (((long)array[3] & 0xFFFFFFFFL) - 1L);
        array[3] = (int)n6;
        long n8;
        final long n7 = n8 = n6 >> 32;
        if (n7 != 0L) {
            final long n9 = n7 + ((long)array[4] & 0xFFFFFFFFL);
            array[4] = (int)n9;
            final long n10 = (n9 >> 32) + ((long)array[5] & 0xFFFFFFFFL);
            array[5] = (int)n10;
            n8 = n10 >> 32;
        }
        final long n11 = n8 + (((long)array[6] & 0xFFFFFFFFL) - 1L);
        array[6] = (int)n11;
        array[7] = (int)((n11 >> 32) + (((long)array[7] & 0xFFFFFFFFL) + 1L));
    }
    
    public static int[] fromBigInteger(final BigInteger bigInteger) {
        final int[] fromBigInteger = Nat256.fromBigInteger(bigInteger);
        if (fromBigInteger[7] == -1 && Nat256.gte(fromBigInteger, SecP256R1Field.P)) {
            Nat256.subFrom(SecP256R1Field.P, fromBigInteger);
        }
        return fromBigInteger;
    }
    
    public static void half(final int[] array, final int[] array2) {
        if ((array[0] & 0x1) == 0x0) {
            Nat.shiftDownBit(8, array, 0, array2);
            return;
        }
        Nat.shiftDownBit(8, array2, Nat256.add(array, SecP256R1Field.P, array2));
    }
    
    public static void multiply(final int[] array, final int[] array2, final int[] array3) {
        final int[] ext = Nat256.createExt();
        Nat256.mul(array, array2, ext);
        reduce(ext, array3);
    }
    
    public static void multiplyAddToExt(final int[] array, final int[] array2, final int[] array3) {
        if (Nat256.mulAddTo(array, array2, array3) != 0 || (array3[15] >>> 1 >= Integer.MAX_VALUE && Nat.gte(16, array3, SecP256R1Field.PExt))) {
            Nat.subFrom(16, SecP256R1Field.PExt, array3);
        }
    }
    
    public static void negate(final int[] array, final int[] array2) {
        if (Nat256.isZero(array)) {
            Nat256.zero(array2);
            return;
        }
        Nat256.sub(SecP256R1Field.P, array, array2);
    }
    
    public static void reduce(final int[] array, final int[] array2) {
        final long n = array[8];
        final long n2 = (long)array[9] & 0xFFFFFFFFL;
        final long n3 = (long)array[10] & 0xFFFFFFFFL;
        final long n4 = (long)array[11] & 0xFFFFFFFFL;
        final long n5 = (long)array[12] & 0xFFFFFFFFL;
        final long n6 = (long)array[13] & 0xFFFFFFFFL;
        final long n7 = (long)array[14] & 0xFFFFFFFFL;
        final long n8 = (long)array[15] & 0xFFFFFFFFL;
        final long n9 = (n & 0xFFFFFFFFL) - 6L;
        final long n10 = n2 + n3;
        final long n11 = n3 + n4 - n8;
        final long n12 = n4 + n5;
        final long n13 = n5 + n6;
        final long n14 = n6 + n7;
        final long n15 = n7 + n8;
        final long n16 = n14 - (n9 + n2);
        final long n17 = ((long)array[0] & 0xFFFFFFFFL) - n12 - n16 + 0L;
        array2[0] = (int)n17;
        final long n18 = (n17 >> 32) + (((long)array[1] & 0xFFFFFFFFL) + n10 - n13 - n15);
        array2[1] = (int)n18;
        final long n19 = (n18 >> 32) + (((long)array[2] & 0xFFFFFFFFL) + n11 - n14);
        array2[2] = (int)n19;
        final long n20 = (n19 >> 32) + (((long)array[3] & 0xFFFFFFFFL) + (n12 << 1) + n16 - n15);
        array2[3] = (int)n20;
        final long n21 = (n20 >> 32) + (((long)array[4] & 0xFFFFFFFFL) + (n13 << 1) + n7 - n10);
        array2[4] = (int)n21;
        final long n22 = (n21 >> 32) + (((long)array[5] & 0xFFFFFFFFL) + (n14 << 1) - n11);
        array2[5] = (int)n22;
        final long n23 = (n22 >> 32) + (((long)array[6] & 0xFFFFFFFFL) + (n15 << 1) + n16);
        array2[6] = (int)n23;
        final long n24 = (n23 >> 32) + (((long)array[7] & 0xFFFFFFFFL) + (n8 << 1) + n9 - n11 - n13);
        array2[7] = (int)n24;
        reduce32((int)((n24 >> 32) + 6L), array2);
    }
    
    public static void reduce32(final int n, final int[] array) {
        long n15;
        if (n != 0) {
            final long n2 = (long)n & 0xFFFFFFFFL;
            final long n3 = ((long)array[0] & 0xFFFFFFFFL) + n2 + 0L;
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
            final long n8 = n5 + (((long)array[3] & 0xFFFFFFFFL) - n2);
            array[3] = (int)n8;
            long n10;
            final long n9 = n10 = n8 >> 32;
            if (n9 != 0L) {
                final long n11 = n9 + ((long)array[4] & 0xFFFFFFFFL);
                array[4] = (int)n11;
                final long n12 = (n11 >> 32) + ((long)array[5] & 0xFFFFFFFFL);
                array[5] = (int)n12;
                n10 = n12 >> 32;
            }
            final long n13 = n10 + (((long)array[6] & 0xFFFFFFFFL) - n2);
            array[6] = (int)n13;
            final long n14 = (n13 >> 32) + (((long)array[7] & 0xFFFFFFFFL) + n2);
            array[7] = (int)n14;
            n15 = n14 >> 32;
        }
        else {
            n15 = 0L;
        }
        if (n15 != 0L || (array[7] == -1 && Nat256.gte(array, SecP256R1Field.P))) {
            addPInvTo(array);
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
        final long n6 = n3 + (((long)array[3] & 0xFFFFFFFFL) + 1L);
        array[3] = (int)n6;
        long n8;
        final long n7 = n8 = n6 >> 32;
        if (n7 != 0L) {
            final long n9 = n7 + ((long)array[4] & 0xFFFFFFFFL);
            array[4] = (int)n9;
            final long n10 = (n9 >> 32) + ((long)array[5] & 0xFFFFFFFFL);
            array[5] = (int)n10;
            n8 = n10 >> 32;
        }
        final long n11 = n8 + (((long)array[6] & 0xFFFFFFFFL) + 1L);
        array[6] = (int)n11;
        array[7] = (int)((n11 >> 32) + (((long)array[7] & 0xFFFFFFFFL) - 1L));
    }
    
    public static void subtract(final int[] array, final int[] array2, final int[] array3) {
        if (Nat256.sub(array, array2, array3) != 0) {
            subPInvFrom(array3);
        }
    }
    
    public static void subtractExt(final int[] array, final int[] array2, final int[] array3) {
        if (Nat.sub(16, array, array2, array3) != 0) {
            Nat.addTo(16, SecP256R1Field.PExt, array3);
        }
    }
    
    public static void twice(final int[] array, final int[] array2) {
        if (Nat.shiftUpBit(8, array, 0, array2) != 0 || (array2[7] == -1 && Nat256.gte(array2, SecP256R1Field.P))) {
            addPInvTo(array2);
        }
    }
}
