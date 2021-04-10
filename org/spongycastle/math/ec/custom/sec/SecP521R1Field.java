package org.spongycastle.math.ec.custom.sec;

import java.math.*;
import org.spongycastle.math.raw.*;

public class SecP521R1Field
{
    static final int[] P;
    private static final int P16 = 511;
    
    static {
        P = new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 511 };
    }
    
    public static void add(final int[] array, final int[] array2, final int[] array3) {
        final int n = Nat.add(16, array, array2, array3) + array[16] + array2[16];
        int n2 = 0;
        Label_0068: {
            if (n <= 511) {
                if ((n2 = n) != 511) {
                    break Label_0068;
                }
                n2 = n;
                if (!Nat.eq(16, array3, SecP521R1Field.P)) {
                    break Label_0068;
                }
            }
            n2 = (n + Nat.inc(16, array3) & 0x1FF);
        }
        array3[16] = n2;
    }
    
    public static void addOne(final int[] array, final int[] array2) {
        final int n = Nat.inc(16, array, array2) + array[16];
        int n2 = 0;
        Label_0056: {
            if (n <= 511) {
                if ((n2 = n) != 511) {
                    break Label_0056;
                }
                n2 = n;
                if (!Nat.eq(16, array2, SecP521R1Field.P)) {
                    break Label_0056;
                }
            }
            n2 = (n + Nat.inc(16, array2) & 0x1FF);
        }
        array2[16] = n2;
    }
    
    public static int[] fromBigInteger(final BigInteger bigInteger) {
        final int[] fromBigInteger = Nat.fromBigInteger(521, bigInteger);
        if (Nat.eq(17, fromBigInteger, SecP521R1Field.P)) {
            Nat.zero(17, fromBigInteger);
        }
        return fromBigInteger;
    }
    
    public static void half(final int[] array, final int[] array2) {
        final int n = array[16];
        array2[16] = (Nat.shiftDownBit(16, array, n, array2) >>> 23 | n >>> 1);
    }
    
    protected static void implMultiply(final int[] array, final int[] array2, final int[] array3) {
        Nat512.mul(array, array2, array3);
        final int n = array[16];
        final int n2 = array2[16];
        array3[32] = Nat.mul31BothAdd(16, n, array2, n2, array, array3, 16) + n * n2;
    }
    
    protected static void implSquare(final int[] array, final int[] array2) {
        Nat512.square(array, array2);
        final int n = array[16];
        array2[32] = Nat.mulWordAddTo(16, n << 1, array, 0, array2, 16) + n * n;
    }
    
    public static void multiply(final int[] array, final int[] array2, final int[] array3) {
        final int[] create = Nat.create(33);
        implMultiply(array, array2, create);
        reduce(create, array3);
    }
    
    public static void negate(final int[] array, final int[] array2) {
        if (Nat.isZero(17, array)) {
            Nat.zero(17, array2);
            return;
        }
        Nat.sub(17, SecP521R1Field.P, array, array2);
    }
    
    public static void reduce(final int[] array, final int[] array2) {
        final int n = array[32];
        final int n2 = (Nat.shiftDownBits(16, array, 16, 9, n, array2, 0) >>> 23) + (n >>> 9) + Nat.addTo(16, array, array2);
        int n3 = 0;
        Label_0078: {
            if (n2 <= 511) {
                if ((n3 = n2) != 511) {
                    break Label_0078;
                }
                n3 = n2;
                if (!Nat.eq(16, array2, SecP521R1Field.P)) {
                    break Label_0078;
                }
            }
            n3 = (n2 + Nat.inc(16, array2) & 0x1FF);
        }
        array2[16] = n3;
    }
    
    public static void reduce23(final int[] array) {
        final int n = array[16];
        final int n2 = Nat.addWordTo(16, n >>> 9, array) + (n & 0x1FF);
        int n3 = 0;
        Label_0065: {
            if (n2 <= 511) {
                if ((n3 = n2) != 511) {
                    break Label_0065;
                }
                n3 = n2;
                if (!Nat.eq(16, array, SecP521R1Field.P)) {
                    break Label_0065;
                }
            }
            n3 = (n2 + Nat.inc(16, array) & 0x1FF);
        }
        array[16] = n3;
    }
    
    public static void square(final int[] array, final int[] array2) {
        final int[] create = Nat.create(33);
        implSquare(array, create);
        reduce(create, array2);
    }
    
    public static void squareN(final int[] array, int n, final int[] array2) {
        final int[] create = Nat.create(33);
        implSquare(array, create);
        while (true) {
            reduce(create, array2);
            --n;
            if (n <= 0) {
                break;
            }
            implSquare(array2, create);
        }
    }
    
    public static void subtract(final int[] array, final int[] array2, final int[] array3) {
        int n2;
        final int n = n2 = Nat.sub(16, array, array2, array3) + array[16] - array2[16];
        if (n < 0) {
            n2 = (n + Nat.dec(16, array3) & 0x1FF);
        }
        array3[16] = n2;
    }
    
    public static void twice(final int[] array, final int[] array2) {
        final int n = array[16];
        array2[16] = ((Nat.shiftUpBit(16, array, n << 23, array2) | n << 1) & 0x1FF);
    }
}
