package org.spongycastle.math.ec.custom.sec;

import java.math.*;
import org.spongycastle.math.raw.*;

public class SecP384R1Field
{
    private static final long M = 4294967295L;
    static final int[] P;
    private static final int P11 = -1;
    static final int[] PExt;
    private static final int PExt23 = -1;
    private static final int[] PExtInv;
    
    static {
        P = new int[] { -1, 0, 0, -1, -2, -1, -1, -1, -1, -1, -1, -1 };
        PExt = new int[] { 1, -2, 0, 2, 0, -2, 0, 2, 1, 0, 0, 0, -2, 1, 0, -2, -3, -1, -1, -1, -1, -1, -1, -1 };
        PExtInv = new int[] { -1, 1, -1, -3, -1, 1, -1, -3, -2, -1, -1, -1, 1, -2, -1, 1, 2 };
    }
    
    public static void add(final int[] array, final int[] array2, final int[] array3) {
        if (Nat.add(12, array, array2, array3) != 0 || (array3[11] == -1 && Nat.gte(12, array3, SecP384R1Field.P))) {
            addPInvTo(array3);
        }
    }
    
    public static void addExt(int[] pExtInv, final int[] array, final int[] array2) {
        if (Nat.add(24, pExtInv, array, array2) != 0 || (array2[23] == -1 && Nat.gte(24, array2, SecP384R1Field.PExt))) {
            pExtInv = SecP384R1Field.PExtInv;
            if (Nat.addTo(pExtInv.length, pExtInv, array2) != 0) {
                Nat.incAt(24, array2, SecP384R1Field.PExtInv.length);
            }
        }
    }
    
    public static void addOne(final int[] array, final int[] array2) {
        if (Nat.inc(12, array, array2) != 0 || (array2[11] == -1 && Nat.gte(12, array2, SecP384R1Field.P))) {
            addPInvTo(array2);
        }
    }
    
    private static void addPInvTo(final int[] array) {
        final long n = ((long)array[0] & 0xFFFFFFFFL) + 1L;
        array[0] = (int)n;
        final long n2 = (n >> 32) + (((long)array[1] & 0xFFFFFFFFL) - 1L);
        array[1] = (int)n2;
        long n4;
        final long n3 = n4 = n2 >> 32;
        if (n3 != 0L) {
            final long n5 = n3 + ((long)array[2] & 0xFFFFFFFFL);
            array[2] = (int)n5;
            n4 = n5 >> 32;
        }
        final long n6 = n4 + (((long)array[3] & 0xFFFFFFFFL) + 1L);
        array[3] = (int)n6;
        final long n7 = (n6 >> 32) + (((long)array[4] & 0xFFFFFFFFL) + 1L);
        array[4] = (int)n7;
        if (n7 >> 32 != 0L) {
            Nat.incAt(12, array, 5);
        }
    }
    
    public static int[] fromBigInteger(final BigInteger bigInteger) {
        final int[] fromBigInteger = Nat.fromBigInteger(384, bigInteger);
        if (fromBigInteger[11] == -1 && Nat.gte(12, fromBigInteger, SecP384R1Field.P)) {
            Nat.subFrom(12, SecP384R1Field.P, fromBigInteger);
        }
        return fromBigInteger;
    }
    
    public static void half(final int[] array, final int[] array2) {
        if ((array[0] & 0x1) == 0x0) {
            Nat.shiftDownBit(12, array, 0, array2);
            return;
        }
        Nat.shiftDownBit(12, array2, Nat.add(12, array, SecP384R1Field.P, array2));
    }
    
    public static void multiply(final int[] array, final int[] array2, final int[] array3) {
        final int[] create = Nat.create(24);
        Nat384.mul(array, array2, create);
        reduce(create, array3);
    }
    
    public static void negate(final int[] array, final int[] array2) {
        if (Nat.isZero(12, array)) {
            Nat.zero(12, array2);
            return;
        }
        Nat.sub(12, SecP384R1Field.P, array, array2);
    }
    
    public static void reduce(final int[] array, final int[] array2) {
        final long n = (long)array[16] & 0xFFFFFFFFL;
        final long n2 = (long)array[17] & 0xFFFFFFFFL;
        final long n3 = (long)array[18] & 0xFFFFFFFFL;
        final long n4 = (long)array[19] & 0xFFFFFFFFL;
        final long n5 = (long)array[20] & 0xFFFFFFFFL;
        final long n6 = (long)array[21] & 0xFFFFFFFFL;
        final long n7 = (long)array[22] & 0xFFFFFFFFL;
        final long n8 = (long)array[23] & 0xFFFFFFFFL;
        final long n9 = ((long)array[12] & 0xFFFFFFFFL) + n5 - 1L;
        final long n10 = ((long)array[13] & 0xFFFFFFFFL) + n7;
        final long n11 = ((long)array[14] & 0xFFFFFFFFL) + n7 + n8;
        final long n12 = ((long)array[15] & 0xFFFFFFFFL) + n8;
        final long n13 = n2 + n6;
        final long n14 = n6 - n8;
        final long n15 = n7 - n8;
        final long n16 = n9 + n14;
        final long n17 = ((long)array[0] & 0xFFFFFFFFL) + n16 + 0L;
        array2[0] = (int)n17;
        final long n18 = (n17 >> 32) + (((long)array[1] & 0xFFFFFFFFL) + n8 - n9 + n10);
        array2[1] = (int)n18;
        final long n19 = (n18 >> 32) + (((long)array[2] & 0xFFFFFFFFL) - n6 - n10 + n11);
        array2[2] = (int)n19;
        final long n20 = (n19 >> 32) + (((long)array[3] & 0xFFFFFFFFL) - n11 + n12 + n16);
        array2[3] = (int)n20;
        final long n21 = (n20 >> 32) + (((long)array[4] & 0xFFFFFFFFL) + n + n6 + n10 - n12 + n16);
        array2[4] = (int)n21;
        final long n22 = (n21 >> 32) + (((long)array[5] & 0xFFFFFFFFL) - n + n10 + n11 + n13);
        array2[5] = (int)n22;
        final long n23 = (n22 >> 32) + (((long)array[6] & 0xFFFFFFFFL) + n3 - n2 + n11 + n12);
        array2[6] = (int)n23;
        final long n24 = (n23 >> 32) + (((long)array[7] & 0xFFFFFFFFL) + n + n4 - n3 + n12);
        array2[7] = (int)n24;
        final long n25 = (n24 >> 32) + (((long)array[8] & 0xFFFFFFFFL) + n + n2 + n5 - n4);
        array2[8] = (int)n25;
        final long n26 = (n25 >> 32) + (((long)array[9] & 0xFFFFFFFFL) + n3 - n5 + n13);
        array2[9] = (int)n26;
        final long n27 = (n26 >> 32) + (((long)array[10] & 0xFFFFFFFFL) + n3 + n4 - n14 + n15);
        array2[10] = (int)n27;
        final long n28 = (n27 >> 32) + (((long)array[11] & 0xFFFFFFFFL) + n4 + n5 - n15);
        array2[11] = (int)n28;
        reduce32((int)((n28 >> 32) + 1L), array2);
    }
    
    public static void reduce32(final int n, final int[] array) {
        long n10;
        if (n != 0) {
            final long n2 = (long)n & 0xFFFFFFFFL;
            final long n3 = ((long)array[0] & 0xFFFFFFFFL) + n2 + 0L;
            array[0] = (int)n3;
            final long n4 = (n3 >> 32) + (((long)array[1] & 0xFFFFFFFFL) - n2);
            array[1] = (int)n4;
            long n6;
            final long n5 = n6 = n4 >> 32;
            if (n5 != 0L) {
                final long n7 = n5 + ((long)array[2] & 0xFFFFFFFFL);
                array[2] = (int)n7;
                n6 = n7 >> 32;
            }
            final long n8 = n6 + (((long)array[3] & 0xFFFFFFFFL) + n2);
            array[3] = (int)n8;
            final long n9 = (n8 >> 32) + (((long)array[4] & 0xFFFFFFFFL) + n2);
            array[4] = (int)n9;
            n10 = n9 >> 32;
        }
        else {
            n10 = 0L;
        }
        if ((n10 != 0L && Nat.incAt(12, array, 5) != 0) || (array[11] == -1 && Nat.gte(12, array, SecP384R1Field.P))) {
            addPInvTo(array);
        }
    }
    
    public static void square(final int[] array, final int[] array2) {
        final int[] create = Nat.create(24);
        Nat384.square(array, create);
        reduce(create, array2);
    }
    
    public static void squareN(final int[] array, int n, final int[] array2) {
        final int[] create = Nat.create(24);
        Nat384.square(array, create);
        while (true) {
            reduce(create, array2);
            --n;
            if (n <= 0) {
                break;
            }
            Nat384.square(array2, create);
        }
    }
    
    private static void subPInvFrom(final int[] array) {
        final long n = ((long)array[0] & 0xFFFFFFFFL) - 1L;
        array[0] = (int)n;
        final long n2 = (n >> 32) + (((long)array[1] & 0xFFFFFFFFL) + 1L);
        array[1] = (int)n2;
        long n4;
        final long n3 = n4 = n2 >> 32;
        if (n3 != 0L) {
            final long n5 = n3 + ((long)array[2] & 0xFFFFFFFFL);
            array[2] = (int)n5;
            n4 = n5 >> 32;
        }
        final long n6 = n4 + (((long)array[3] & 0xFFFFFFFFL) - 1L);
        array[3] = (int)n6;
        final long n7 = (n6 >> 32) + (((long)array[4] & 0xFFFFFFFFL) - 1L);
        array[4] = (int)n7;
        if (n7 >> 32 != 0L) {
            Nat.decAt(12, array, 5);
        }
    }
    
    public static void subtract(final int[] array, final int[] array2, final int[] array3) {
        if (Nat.sub(12, array, array2, array3) != 0) {
            subPInvFrom(array3);
        }
    }
    
    public static void subtractExt(int[] pExtInv, final int[] array, final int[] array2) {
        if (Nat.sub(24, pExtInv, array, array2) != 0) {
            pExtInv = SecP384R1Field.PExtInv;
            if (Nat.subFrom(pExtInv.length, pExtInv, array2) != 0) {
                Nat.decAt(24, array2, SecP384R1Field.PExtInv.length);
            }
        }
    }
    
    public static void twice(final int[] array, final int[] array2) {
        if (Nat.shiftUpBit(12, array, 0, array2) != 0 || (array2[11] == -1 && Nat.gte(12, array2, SecP384R1Field.P))) {
            addPInvTo(array2);
        }
    }
}
