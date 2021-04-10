package org.spongycastle.math.raw;

import java.math.*;
import org.spongycastle.util.*;

public abstract class Nat576
{
    public static void copy64(final long[] array, final long[] array2) {
        array2[0] = array[0];
        array2[1] = array[1];
        array2[2] = array[2];
        array2[3] = array[3];
        array2[4] = array[4];
        array2[5] = array[5];
        array2[6] = array[6];
        array2[7] = array[7];
        array2[8] = array[8];
    }
    
    public static long[] create64() {
        return new long[9];
    }
    
    public static long[] createExt64() {
        return new long[18];
    }
    
    public static boolean eq64(final long[] array, final long[] array2) {
        for (int i = 8; i >= 0; --i) {
            if (array[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }
    
    public static long[] fromBigInteger64(BigInteger shiftRight) {
        if (shiftRight.signum() >= 0 && shiftRight.bitLength() <= 576) {
            final long[] create64 = create64();
            for (int n = 0; shiftRight.signum() != 0; shiftRight = shiftRight.shiftRight(64), ++n) {
                create64[n] = shiftRight.longValue();
            }
            return create64;
        }
        throw new IllegalArgumentException();
    }
    
    public static boolean isOne64(final long[] array) {
        if (array[0] != 1L) {
            return false;
        }
        for (int i = 1; i < 9; ++i) {
            if (array[i] != 0L) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isZero64(final long[] array) {
        for (int i = 0; i < 9; ++i) {
            if (array[i] != 0L) {
                return false;
            }
        }
        return true;
    }
    
    public static BigInteger toBigInteger64(final long[] array) {
        final byte[] array2 = new byte[72];
        for (int i = 0; i < 9; ++i) {
            final long n = array[i];
            if (n != 0L) {
                Pack.longToBigEndian(n, array2, 8 - i << 3);
            }
        }
        return new BigInteger(1, array2);
    }
}
