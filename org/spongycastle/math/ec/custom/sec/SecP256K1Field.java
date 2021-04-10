package org.spongycastle.math.ec.custom.sec;

import org.spongycastle.math.raw.*;
import java.math.*;

public class SecP256K1Field
{
    static final int[] P;
    private static final int P7 = -1;
    static final int[] PExt;
    private static final int PExt15 = -1;
    private static final int[] PExtInv;
    private static final int PInv33 = 977;
    
    static {
        P = new int[] { -977, -2, -1, -1, -1, -1, -1, -1 };
        PExt = new int[] { 954529, 1954, 1, 0, 0, 0, 0, 0, -1954, -3, -1, -1, -1, -1, -1, -1 };
        PExtInv = new int[] { -954529, -1955, -2, -1, -1, -1, -1, -1, 1953, 2 };
    }
    
    public static void add(final int[] array, final int[] array2, final int[] array3) {
        if (Nat256.add(array, array2, array3) != 0 || (array3[7] == -1 && Nat256.gte(array3, SecP256K1Field.P))) {
            Nat.add33To(8, 977, array3);
        }
    }
    
    public static void addExt(int[] pExtInv, final int[] array, final int[] array2) {
        if (Nat.add(16, pExtInv, array, array2) != 0 || (array2[15] == -1 && Nat.gte(16, array2, SecP256K1Field.PExt))) {
            pExtInv = SecP256K1Field.PExtInv;
            if (Nat.addTo(pExtInv.length, pExtInv, array2) != 0) {
                Nat.incAt(16, array2, SecP256K1Field.PExtInv.length);
            }
        }
    }
    
    public static void addOne(final int[] array, final int[] array2) {
        if (Nat.inc(8, array, array2) != 0 || (array2[7] == -1 && Nat256.gte(array2, SecP256K1Field.P))) {
            Nat.add33To(8, 977, array2);
        }
    }
    
    public static int[] fromBigInteger(final BigInteger bigInteger) {
        final int[] fromBigInteger = Nat256.fromBigInteger(bigInteger);
        if (fromBigInteger[7] == -1 && Nat256.gte(fromBigInteger, SecP256K1Field.P)) {
            Nat256.subFrom(SecP256K1Field.P, fromBigInteger);
        }
        return fromBigInteger;
    }
    
    public static void half(final int[] array, final int[] array2) {
        if ((array[0] & 0x1) == 0x0) {
            Nat.shiftDownBit(8, array, 0, array2);
            return;
        }
        Nat.shiftDownBit(8, array2, Nat256.add(array, SecP256K1Field.P, array2));
    }
    
    public static void multiply(final int[] array, final int[] array2, final int[] array3) {
        final int[] ext = Nat256.createExt();
        Nat256.mul(array, array2, ext);
        reduce(ext, array3);
    }
    
    public static void multiplyAddToExt(int[] pExtInv, final int[] array, final int[] array2) {
        if (Nat256.mulAddTo(pExtInv, array, array2) != 0 || (array2[15] == -1 && Nat.gte(16, array2, SecP256K1Field.PExt))) {
            pExtInv = SecP256K1Field.PExtInv;
            if (Nat.addTo(pExtInv.length, pExtInv, array2) != 0) {
                Nat.incAt(16, array2, SecP256K1Field.PExtInv.length);
            }
        }
    }
    
    public static void negate(final int[] array, final int[] array2) {
        if (Nat256.isZero(array)) {
            Nat256.zero(array2);
            return;
        }
        Nat256.sub(SecP256K1Field.P, array, array2);
    }
    
    public static void reduce(final int[] array, final int[] array2) {
        if (Nat256.mul33DWordAdd(977, Nat256.mul33Add(977, array, 8, array, 0, array2, 0), array2, 0) != 0 || (array2[7] == -1 && Nat256.gte(array2, SecP256K1Field.P))) {
            Nat.add33To(8, 977, array2);
        }
    }
    
    public static void reduce32(final int n, final int[] array) {
        if ((n != 0 && Nat256.mul33WordAdd(977, n, array, 0) != 0) || (array[7] == -1 && Nat256.gte(array, SecP256K1Field.P))) {
            Nat.add33To(8, 977, array);
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
    
    public static void subtract(final int[] array, final int[] array2, final int[] array3) {
        if (Nat256.sub(array, array2, array3) != 0) {
            Nat.sub33From(8, 977, array3);
        }
    }
    
    public static void subtractExt(int[] pExtInv, final int[] array, final int[] array2) {
        if (Nat.sub(16, pExtInv, array, array2) != 0) {
            pExtInv = SecP256K1Field.PExtInv;
            if (Nat.subFrom(pExtInv.length, pExtInv, array2) != 0) {
                Nat.decAt(16, array2, SecP256K1Field.PExtInv.length);
            }
        }
    }
    
    public static void twice(final int[] array, final int[] array2) {
        if (Nat.shiftUpBit(8, array, 0, array2) != 0 || (array2[7] == -1 && Nat256.gte(array2, SecP256K1Field.P))) {
            Nat.add33To(8, 977, array2);
        }
    }
}
