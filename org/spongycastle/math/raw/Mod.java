package org.spongycastle.math.raw;

import java.util.*;

public abstract class Mod
{
    public static void add(final int[] array, final int[] array2, final int[] array3, final int[] array4) {
        final int length = array.length;
        if (Nat.add(length, array2, array3, array4) != 0) {
            Nat.subFrom(length, array, array4);
        }
    }
    
    private static int getTrailingZeroes(int n) {
        final int n2 = 0;
        int n3;
        for (n3 = n, n = n2; (n3 & 0x1) == 0x0; n3 >>>= 1, ++n) {}
        return n;
    }
    
    public static int inverse32(final int n) {
        final int n2 = (2 - n * n) * n;
        final int n3 = n2 * (2 - n * n2);
        final int n4 = n3 * (2 - n * n3);
        return n4 * (2 - n * n4);
    }
    
    private static void inversionResult(final int[] array, final int n, final int[] array2, final int[] array3) {
        if (n < 0) {
            Nat.add(array.length, array2, array, array3);
            return;
        }
        System.arraycopy(array2, 0, array3, 0, array.length);
    }
    
    private static int inversionStep(final int[] array, final int[] array2, int i, final int[] array3, int n) {
        final int length = array.length;
        int n2 = 0;
        while (array2[0] == 0) {
            Nat.shiftDownWord(i, array2, 0);
            n2 += 32;
        }
        final int trailingZeroes = getTrailingZeroes(array2[0]);
        int n3 = n2;
        if (trailingZeroes > 0) {
            Nat.shiftDownBits(i, array2, trailingZeroes, 0);
            n3 = n2 + trailingZeroes;
        }
        int n4;
        int n5;
        for (i = 0; i < n3; ++i, n = n4) {
            n4 = n;
            if ((array3[0] & 0x1) != 0x0) {
                if (n < 0) {
                    n5 = Nat.addTo(length, array, array3);
                }
                else {
                    n5 = Nat.subFrom(length, array, array3);
                }
                n4 = n + n5;
            }
            Nat.shiftDownBit(length, array3, n4);
        }
        return n;
    }
    
    public static void invert(final int[] array, int[] copy, final int[] array2) {
        final int length = array.length;
        if (Nat.isZero(length, copy)) {
            throw new IllegalArgumentException("'x' cannot be 0");
        }
        if (Nat.isOne(length, copy)) {
            System.arraycopy(copy, 0, array2, 0, length);
            return;
        }
        copy = Nat.copy(length, copy);
        final int[] create = Nat.create(length);
        create[0] = 1;
        int n;
        if ((0x1 & copy[0]) == 0x0) {
            n = inversionStep(array, copy, length, create, 0);
        }
        else {
            n = 0;
        }
        if (Nat.isOne(length, copy)) {
            inversionResult(array, n, create, array2);
            return;
        }
        final int[] copy2 = Nat.copy(length, array);
        final int[] create2 = Nat.create(length);
        int n2 = length;
        int inversionStep = 0;
        while (true) {
            final int n3 = n2 - 1;
            if (copy[n3] == 0 && copy2[n3] == 0) {
                n2 = n3;
            }
            else if (Nat.gte(n2, copy, copy2)) {
                Nat.subFrom(n2, copy2, copy);
                final int n4 = n = inversionStep(array, copy, n2, create, n + (Nat.subFrom(length, create2, create) - inversionStep));
                if (Nat.isOne(n2, copy)) {
                    inversionResult(array, n4, create, array2);
                    return;
                }
                continue;
            }
            else {
                Nat.subFrom(n2, copy, copy2);
                final int n5 = inversionStep = inversionStep(array, copy2, n2, create2, inversionStep + (Nat.subFrom(length, create, create2) - n));
                if (Nat.isOne(n2, copy2)) {
                    inversionResult(array, n5, create2, array2);
                    return;
                }
                continue;
            }
        }
    }
    
    public static int[] random(final int[] array) {
        final int length = array.length;
        final Random random = new Random();
        final int[] create = Nat.create(length);
        final int n = length - 1;
        final int n2 = array[n];
        final int n3 = n2 | n2 >>> 1;
        final int n4 = n3 | n3 >>> 2;
        final int n5 = n4 | n4 >>> 4;
        final int n6 = n5 | n5 >>> 8;
        do {
            for (int i = 0; i != length; ++i) {
                create[i] = random.nextInt();
            }
            create[n] &= (n6 >>> 16 | n6);
        } while (Nat.gte(length, create, array));
        return create;
    }
    
    public static void subtract(final int[] array, final int[] array2, final int[] array3, final int[] array4) {
        final int length = array.length;
        if (Nat.sub(length, array2, array3, array4) != 0) {
            Nat.addTo(length, array, array4);
        }
    }
}
