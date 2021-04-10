package org.spongycastle.math.ec.custom.sec;

import org.spongycastle.math.raw.*;
import java.math.*;

public class SecP160R2Field
{
    static final int[] P;
    private static final int P4 = -1;
    static final int[] PExt;
    private static final int PExt9 = -1;
    private static final int[] PExtInv;
    private static final int PInv33 = 21389;
    
    static {
        P = new int[] { -21389, -2, -1, -1, -1 };
        PExt = new int[] { 457489321, 42778, 1, 0, 0, -42778, -3, -1, -1, -1 };
        PExtInv = new int[] { -457489321, -42779, -2, -1, -1, 42777, 2 };
    }
    
    public static void add(final int[] array, final int[] array2, final int[] array3) {
        if (Nat160.add(array, array2, array3) != 0 || (array3[4] == -1 && Nat160.gte(array3, SecP160R2Field.P))) {
            Nat.add33To(5, 21389, array3);
        }
    }
    
    public static void addExt(int[] pExtInv, final int[] array, final int[] array2) {
        if (Nat.add(10, pExtInv, array, array2) != 0 || (array2[9] == -1 && Nat.gte(10, array2, SecP160R2Field.PExt))) {
            pExtInv = SecP160R2Field.PExtInv;
            if (Nat.addTo(pExtInv.length, pExtInv, array2) != 0) {
                Nat.incAt(10, array2, SecP160R2Field.PExtInv.length);
            }
        }
    }
    
    public static void addOne(final int[] array, final int[] array2) {
        if (Nat.inc(5, array, array2) != 0 || (array2[4] == -1 && Nat160.gte(array2, SecP160R2Field.P))) {
            Nat.add33To(5, 21389, array2);
        }
    }
    
    public static int[] fromBigInteger(final BigInteger bigInteger) {
        final int[] fromBigInteger = Nat160.fromBigInteger(bigInteger);
        if (fromBigInteger[4] == -1 && Nat160.gte(fromBigInteger, SecP160R2Field.P)) {
            Nat160.subFrom(SecP160R2Field.P, fromBigInteger);
        }
        return fromBigInteger;
    }
    
    public static void half(final int[] array, final int[] array2) {
        if ((array[0] & 0x1) == 0x0) {
            Nat.shiftDownBit(5, array, 0, array2);
            return;
        }
        Nat.shiftDownBit(5, array2, Nat160.add(array, SecP160R2Field.P, array2));
    }
    
    public static void multiply(final int[] array, final int[] array2, final int[] array3) {
        final int[] ext = Nat160.createExt();
        Nat160.mul(array, array2, ext);
        reduce(ext, array3);
    }
    
    public static void multiplyAddToExt(int[] pExtInv, final int[] array, final int[] array2) {
        if (Nat160.mulAddTo(pExtInv, array, array2) != 0 || (array2[9] == -1 && Nat.gte(10, array2, SecP160R2Field.PExt))) {
            pExtInv = SecP160R2Field.PExtInv;
            if (Nat.addTo(pExtInv.length, pExtInv, array2) != 0) {
                Nat.incAt(10, array2, SecP160R2Field.PExtInv.length);
            }
        }
    }
    
    public static void negate(final int[] array, final int[] array2) {
        if (Nat160.isZero(array)) {
            Nat160.zero(array2);
            return;
        }
        Nat160.sub(SecP160R2Field.P, array, array2);
    }
    
    public static void reduce(final int[] array, final int[] array2) {
        if (Nat160.mul33DWordAdd(21389, Nat160.mul33Add(21389, array, 5, array, 0, array2, 0), array2, 0) != 0 || (array2[4] == -1 && Nat160.gte(array2, SecP160R2Field.P))) {
            Nat.add33To(5, 21389, array2);
        }
    }
    
    public static void reduce32(final int n, final int[] array) {
        if ((n != 0 && Nat160.mul33WordAdd(21389, n, array, 0) != 0) || (array[4] == -1 && Nat160.gte(array, SecP160R2Field.P))) {
            Nat.add33To(5, 21389, array);
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
            Nat.sub33From(5, 21389, array3);
        }
    }
    
    public static void subtractExt(int[] pExtInv, final int[] array, final int[] array2) {
        if (Nat.sub(10, pExtInv, array, array2) != 0) {
            pExtInv = SecP160R2Field.PExtInv;
            if (Nat.subFrom(pExtInv.length, pExtInv, array2) != 0) {
                Nat.decAt(10, array2, SecP160R2Field.PExtInv.length);
            }
        }
    }
    
    public static void twice(final int[] array, final int[] array2) {
        if (Nat.shiftUpBit(5, array, 0, array2) != 0 || (array2[4] == -1 && Nat160.gte(array2, SecP160R2Field.P))) {
            Nat.add33To(5, 21389, array2);
        }
    }
}
