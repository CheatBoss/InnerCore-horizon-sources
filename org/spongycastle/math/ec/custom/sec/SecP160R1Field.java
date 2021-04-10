package org.spongycastle.math.ec.custom.sec;

import org.spongycastle.math.raw.*;
import java.math.*;

public class SecP160R1Field
{
    private static final long M = 4294967295L;
    static final int[] P;
    private static final int P4 = -1;
    static final int[] PExt;
    private static final int PExt9 = -1;
    private static final int[] PExtInv;
    private static final int PInv = -2147483647;
    
    static {
        P = new int[] { Integer.MAX_VALUE, -1, -1, -1, -1 };
        PExt = new int[] { 1, 1073741825, 0, 0, 0, -2, -2, -1, -1, -1 };
        PExtInv = new int[] { -1, -1073741826, -1, -1, -1, 1, 1 };
    }
    
    public static void add(final int[] array, final int[] array2, final int[] array3) {
        if (Nat160.add(array, array2, array3) != 0 || (array3[4] == -1 && Nat160.gte(array3, SecP160R1Field.P))) {
            Nat.addWordTo(5, -2147483647, array3);
        }
    }
    
    public static void addExt(int[] pExtInv, final int[] array, final int[] array2) {
        if (Nat.add(10, pExtInv, array, array2) != 0 || (array2[9] == -1 && Nat.gte(10, array2, SecP160R1Field.PExt))) {
            pExtInv = SecP160R1Field.PExtInv;
            if (Nat.addTo(pExtInv.length, pExtInv, array2) != 0) {
                Nat.incAt(10, array2, SecP160R1Field.PExtInv.length);
            }
        }
    }
    
    public static void addOne(final int[] array, final int[] array2) {
        if (Nat.inc(5, array, array2) != 0 || (array2[4] == -1 && Nat160.gte(array2, SecP160R1Field.P))) {
            Nat.addWordTo(5, -2147483647, array2);
        }
    }
    
    public static int[] fromBigInteger(final BigInteger bigInteger) {
        final int[] fromBigInteger = Nat160.fromBigInteger(bigInteger);
        if (fromBigInteger[4] == -1 && Nat160.gte(fromBigInteger, SecP160R1Field.P)) {
            Nat160.subFrom(SecP160R1Field.P, fromBigInteger);
        }
        return fromBigInteger;
    }
    
    public static void half(final int[] array, final int[] array2) {
        if ((array[0] & 0x1) == 0x0) {
            Nat.shiftDownBit(5, array, 0, array2);
            return;
        }
        Nat.shiftDownBit(5, array2, Nat160.add(array, SecP160R1Field.P, array2));
    }
    
    public static void multiply(final int[] array, final int[] array2, final int[] array3) {
        final int[] ext = Nat160.createExt();
        Nat160.mul(array, array2, ext);
        reduce(ext, array3);
    }
    
    public static void multiplyAddToExt(int[] pExtInv, final int[] array, final int[] array2) {
        if (Nat160.mulAddTo(pExtInv, array, array2) != 0 || (array2[9] == -1 && Nat.gte(10, array2, SecP160R1Field.PExt))) {
            pExtInv = SecP160R1Field.PExtInv;
            if (Nat.addTo(pExtInv.length, pExtInv, array2) != 0) {
                Nat.incAt(10, array2, SecP160R1Field.PExtInv.length);
            }
        }
    }
    
    public static void negate(final int[] array, final int[] array2) {
        if (Nat160.isZero(array)) {
            Nat160.zero(array2);
            return;
        }
        Nat160.sub(SecP160R1Field.P, array, array2);
    }
    
    public static void reduce(final int[] array, final int[] array2) {
        final long n = (long)array[5] & 0xFFFFFFFFL;
        final long n2 = (long)array[6] & 0xFFFFFFFFL;
        final long n3 = (long)array[7] & 0xFFFFFFFFL;
        final long n4 = (long)array[8] & 0xFFFFFFFFL;
        final long n5 = (long)array[9] & 0xFFFFFFFFL;
        final long n6 = ((long)array[0] & 0xFFFFFFFFL) + n + (n << 31) + 0L;
        array2[0] = (int)n6;
        final long n7 = (n6 >>> 32) + (((long)array[1] & 0xFFFFFFFFL) + n2 + (n2 << 31));
        array2[1] = (int)n7;
        final long n8 = (n7 >>> 32) + (((long)array[2] & 0xFFFFFFFFL) + n3 + (n3 << 31));
        array2[2] = (int)n8;
        final long n9 = (n8 >>> 32) + (((long)array[3] & 0xFFFFFFFFL) + n4 + (n4 << 31));
        array2[3] = (int)n9;
        final long n10 = (n9 >>> 32) + (((long)array[4] & 0xFFFFFFFFL) + n5 + (n5 << 31));
        array2[4] = (int)n10;
        reduce32((int)(n10 >>> 32), array2);
    }
    
    public static void reduce32(final int n, final int[] array) {
        if ((n != 0 && Nat160.mulWordsAdd(-2147483647, n, array, 0) != 0) || (array[4] == -1 && Nat160.gte(array, SecP160R1Field.P))) {
            Nat.addWordTo(5, -2147483647, array);
        }
    }
    
    public static void square(final int[] array, final int[] array2) {
        final int[] ext = Nat160.createExt();
        Nat160.square(array, ext);
        reduce(ext, array2);
    }
    
    public static void squareN(final int[] array, int n, final int[] array2) {
        final int[] ext = Nat160.createExt();
        Nat160.square(array, ext);
        while (true) {
            reduce(ext, array2);
            --n;
            if (n <= 0) {
                break;
            }
            Nat160.square(array2, ext);
        }
    }
    
    public static void subtract(final int[] array, final int[] array2, final int[] array3) {
        if (Nat160.sub(array, array2, array3) != 0) {
            Nat.subWordFrom(5, -2147483647, array3);
        }
    }
    
    public static void subtractExt(int[] pExtInv, final int[] array, final int[] array2) {
        if (Nat.sub(10, pExtInv, array, array2) != 0) {
            pExtInv = SecP160R1Field.PExtInv;
            if (Nat.subFrom(pExtInv.length, pExtInv, array2) != 0) {
                Nat.decAt(10, array2, SecP160R1Field.PExtInv.length);
            }
        }
    }
    
    public static void twice(final int[] array, final int[] array2) {
        if (Nat.shiftUpBit(5, array, 0, array2) != 0 || (array2[4] == -1 && Nat160.gte(array2, SecP160R1Field.P))) {
            Nat.addWordTo(5, -2147483647, array2);
        }
    }
}
