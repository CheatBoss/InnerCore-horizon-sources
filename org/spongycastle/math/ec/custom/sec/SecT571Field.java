package org.spongycastle.math.ec.custom.sec;

import java.math.*;
import org.spongycastle.math.raw.*;

public class SecT571Field
{
    private static final long M59 = 576460752303423487L;
    private static final long RM = -1190112520884487202L;
    private static final long[] ROOT_Z;
    
    static {
        ROOT_Z = new long[] { 3161836309350906777L, -7642453882179322845L, -3821226941089661423L, 7312758566309945096L, -556661012383879292L, 8945041530681231562L, -4750851271514160027L, 6847946401097695794L, 541669439031730457L };
    }
    
    private static void add(final long[] array, final int n, final long[] array2, final int n2, final long[] array3, final int n3) {
        for (int i = 0; i < 9; ++i) {
            array3[n3 + i] = (array[n + i] ^ array2[n2 + i]);
        }
    }
    
    public static void add(final long[] array, final long[] array2, final long[] array3) {
        for (int i = 0; i < 9; ++i) {
            array3[i] = (array[i] ^ array2[i]);
        }
    }
    
    private static void addBothTo(final long[] array, final int n, final long[] array2, final int n2, final long[] array3, final int n3) {
        for (int i = 0; i < 9; ++i) {
            final int n4 = n3 + i;
            array3[n4] ^= (array[n + i] ^ array2[n2 + i]);
        }
    }
    
    public static void addBothTo(final long[] array, final long[] array2, final long[] array3) {
        for (int i = 0; i < 9; ++i) {
            array3[i] ^= (array[i] ^ array2[i]);
        }
    }
    
    public static void addExt(final long[] array, final long[] array2, final long[] array3) {
        for (int i = 0; i < 18; ++i) {
            array3[i] = (array[i] ^ array2[i]);
        }
    }
    
    public static void addOne(final long[] array, final long[] array2) {
        array2[0] = (array[0] ^ 0x1L);
        for (int i = 1; i < 9; ++i) {
            array2[i] = array[i];
        }
    }
    
    public static long[] fromBigInteger(final BigInteger bigInteger) {
        final long[] fromBigInteger64 = Nat576.fromBigInteger64(bigInteger);
        reduce5(fromBigInteger64, 0);
        return fromBigInteger64;
    }
    
    protected static void implMultiply(final long[] array, final long[] array2, final long[] array3) {
        implMultiplyPrecomp(array, precompMultiplicand(array2), array3);
    }
    
    protected static void implMultiplyPrecomp(final long[] array, final long[] array2, final long[] array3) {
        final int n = 56;
        int n2 = 56;
        int i;
        while (true) {
            i = n;
            if (n2 < 0) {
                break;
            }
            for (int j = 1; j < 9; j += 2) {
                final int n3 = (int)(array[j] >>> n2);
                addBothTo(array2, (n3 & 0xF) * 9, array2, ((n3 >>> 4 & 0xF) + 16) * 9, array3, j - 1);
            }
            Nat.shiftUpBits64(16, array3, 0, 8, 0L);
            n2 -= 8;
        }
        while (i >= 0) {
            for (int k = 0; k < 9; k += 2) {
                final int n4 = (int)(array[k] >>> i);
                addBothTo(array2, (n4 & 0xF) * 9, array2, ((n4 >>> 4 & 0xF) + 16) * 9, array3, k);
            }
            if (i > 0) {
                Nat.shiftUpBits64(18, array3, 0, 8, 0L);
            }
            i -= 8;
        }
    }
    
    protected static void implMulwAcc(final long[] array, final long n, final long[] array2, int n2) {
        final long[] array3 = new long[32];
        array3[1] = n;
        for (int i = 2; i < 32; i += 2) {
            array3[i] = array3[i >>> 1] << 1;
            array3[i + 1] = (array3[i] ^ n);
        }
        int j = 0;
        long n3 = 0L;
        while (j < 9) {
            final long n4 = array[j];
            long n5 = n3 ^ array3[(int)n4 & 0x1F];
            int n6 = 60;
            n3 = 0L;
            long n8;
            while (true) {
                final long n7 = array3[(int)(n4 >>> n6) & 0x1F];
                n8 = (n5 ^ n7 << n6);
                n3 ^= n7 >>> -n6;
                n6 -= 5;
                if (n6 <= 0) {
                    break;
                }
                n5 = n8;
            }
            int k = 0;
            long n9 = n4;
            while (k < 4) {
                n9 = (n9 & 0xEF7BDEF7BDEF7BDEL) >>> 1;
                final int n10 = k + 1;
                n3 ^= (n << k >> 63 & n9);
                k = n10;
            }
            final int n11 = n2 + j;
            array2[n11] ^= n8;
            ++j;
        }
        n2 += 9;
        array2[n2] ^= n3;
    }
    
    protected static void implSquare(final long[] array, final long[] array2) {
        for (int i = 0; i < 9; ++i) {
            Interleave.expand64To128(array[i], array2, i << 1);
        }
    }
    
    public static void invert(final long[] array, final long[] array2) {
        if (!Nat576.isZero64(array)) {
            final long[] create64 = Nat576.create64();
            final long[] create65 = Nat576.create64();
            final long[] create66 = Nat576.create64();
            square(array, create66);
            square(create66, create64);
            square(create64, create65);
            multiply(create64, create65, create64);
            squareN(create64, 2, create65);
            multiply(create64, create65, create64);
            multiply(create64, create66, create64);
            squareN(create64, 5, create65);
            multiply(create64, create65, create64);
            squareN(create65, 5, create65);
            multiply(create64, create65, create64);
            squareN(create64, 15, create65);
            multiply(create64, create65, create66);
            squareN(create66, 30, create64);
            squareN(create64, 30, create65);
            multiply(create64, create65, create64);
            squareN(create64, 60, create65);
            multiply(create64, create65, create64);
            squareN(create65, 60, create65);
            multiply(create64, create65, create64);
            squareN(create64, 180, create65);
            multiply(create64, create65, create64);
            squareN(create65, 180, create65);
            multiply(create64, create65, create64);
            multiply(create64, create66, array2);
            return;
        }
        throw new IllegalStateException();
    }
    
    public static void multiply(final long[] array, final long[] array2, final long[] array3) {
        final long[] ext64 = Nat576.createExt64();
        implMultiply(array, array2, ext64);
        reduce(ext64, array3);
    }
    
    public static void multiplyAddToExt(final long[] array, final long[] array2, final long[] array3) {
        final long[] ext64 = Nat576.createExt64();
        implMultiply(array, array2, ext64);
        addExt(array3, ext64, array3);
    }
    
    public static void multiplyPrecomp(final long[] array, final long[] array2, final long[] array3) {
        final long[] ext64 = Nat576.createExt64();
        implMultiplyPrecomp(array, array2, ext64);
        reduce(ext64, array3);
    }
    
    public static void multiplyPrecompAddToExt(final long[] array, final long[] array2, final long[] array3) {
        final long[] ext64 = Nat576.createExt64();
        implMultiplyPrecomp(array, array2, ext64);
        addExt(array3, ext64, array3);
    }
    
    public static long[] precompMultiplicand(final long[] array) {
        final long[] array2 = new long[288];
        int n = 0;
        System.arraycopy(array, 0, array2, 9, 9);
        for (int i = 7; i > 0; --i) {
            n += 18;
            Nat.shiftUpBit64(9, array2, n >>> 1, 0L, array2, n);
            reduce5(array2, n);
            add(array2, 9, array2, n, array2, n + 9);
        }
        Nat.shiftUpBits64(144, array2, 0, 4, 0L, array2, 144);
        return array2;
    }
    
    public static void reduce(final long[] array, final long[] array2) {
        final long n = array[9];
        final long n2 = array[17];
        final long n3 = n ^ n2 >>> 59 ^ n2 >>> 57 ^ n2 >>> 54 ^ n2 >>> 49;
        long n4 = n2 << 15 ^ (array[8] ^ n2 << 5 ^ n2 << 7 ^ n2 << 10);
        long n5;
        long n6;
        for (int i = 16; i >= 10; --i, n4 = (n6 ^ n5 << 5 ^ n5 << 7 ^ n5 << 10 ^ n5 << 15)) {
            n5 = array[i];
            array2[i - 8] = (n4 ^ n5 >>> 59 ^ n5 >>> 57 ^ n5 >>> 54 ^ n5 >>> 49);
            n6 = array[i - 9];
        }
        array2[1] = (n4 ^ n3 >>> 59 ^ n3 >>> 57 ^ n3 >>> 54 ^ n3 >>> 49);
        final long n7 = array[0];
        final long n8 = array2[8];
        final long n9 = n8 >>> 59;
        array2[0] = (n3 << 15 ^ (n7 ^ n3 << 5 ^ n3 << 7 ^ n3 << 10) ^ n9 ^ n9 << 2 ^ n9 << 5 ^ n9 << 10);
        array2[8] = (n8 & 0x7FFFFFFFFFFFFFFL);
    }
    
    public static void reduce5(final long[] array, final int n) {
        final int n2 = n + 8;
        final long n3 = array[n2];
        final long n4 = n3 >>> 59;
        array[n] ^= (n4 << 10 ^ (n4 << 2 ^ n4 ^ n4 << 5));
        array[n2] = (n3 & 0x7FFFFFFFFFFFFFFL);
    }
    
    public static void sqrt(final long[] array, final long[] array2) {
        final long[] create64 = Nat576.create64();
        final long[] create65 = Nat576.create64();
        int i = 0;
        int n = 0;
        while (i < 4) {
            final int n2 = n + 1;
            final long unshuffle = Interleave.unshuffle(array[n]);
            n = n2 + 1;
            final long unshuffle2 = Interleave.unshuffle(array[n2]);
            create64[i] = ((unshuffle & 0xFFFFFFFFL) | unshuffle2 << 32);
            create65[i] = (unshuffle >>> 32 | (unshuffle2 & 0xFFFFFFFF00000000L));
            ++i;
        }
        final long unshuffle3 = Interleave.unshuffle(array[n]);
        create64[4] = (unshuffle3 & 0xFFFFFFFFL);
        create65[4] = unshuffle3 >>> 32;
        multiply(create65, SecT571Field.ROOT_Z, array2);
        add(array2, create64, array2);
    }
    
    public static void square(final long[] array, final long[] array2) {
        final long[] ext64 = Nat576.createExt64();
        implSquare(array, ext64);
        reduce(ext64, array2);
    }
    
    public static void squareAddToExt(final long[] array, final long[] array2) {
        final long[] ext64 = Nat576.createExt64();
        implSquare(array, ext64);
        addExt(array2, ext64, array2);
    }
    
    public static void squareN(final long[] array, int n, final long[] array2) {
        final long[] ext64 = Nat576.createExt64();
        implSquare(array, ext64);
        while (true) {
            reduce(ext64, array2);
            --n;
            if (n <= 0) {
                break;
            }
            implSquare(array2, ext64);
        }
    }
    
    public static int trace(final long[] array) {
        return (int)(array[0] ^ array[8] >>> 49 ^ array[8] >>> 57) & 0x1;
    }
}
