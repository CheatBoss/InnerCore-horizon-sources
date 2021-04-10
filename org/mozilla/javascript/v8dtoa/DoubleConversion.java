package org.mozilla.javascript.v8dtoa;

public final class DoubleConversion
{
    private static final int kDenormalExponent = -1074;
    private static final int kExponentBias = 1075;
    private static final long kExponentMask = 9218868437227405312L;
    private static final long kHiddenBit = 4503599627370496L;
    private static final int kPhysicalSignificandSize = 52;
    private static final long kSignMask = Long.MIN_VALUE;
    private static final long kSignificandMask = 4503599627370495L;
    private static final int kSignificandSize = 53;
    
    private DoubleConversion() {
    }
    
    public static int doubleToInt32(final double n) {
        final int n2 = (int)n;
        if (n2 == n) {
            return n2;
        }
        final long doubleToLongBits = Double.doubleToLongBits(n);
        final int exponent = exponent(doubleToLongBits);
        if (exponent > -53 && exponent <= 31) {
            final long significand = significand(doubleToLongBits);
            final int sign = sign(doubleToLongBits);
            long n3;
            if (exponent < 0) {
                n3 = significand >> -exponent;
            }
            else {
                n3 = significand << exponent;
            }
            return sign * (int)n3;
        }
        return 0;
    }
    
    private static int exponent(final long n) {
        if (isDenormal(n)) {
            return -1074;
        }
        return (int)((n & 0x7FF0000000000000L) >> 52) - 1075;
    }
    
    private static boolean isDenormal(final long n) {
        return (n & 0x7FF0000000000000L) == 0x0L;
    }
    
    private static int sign(final long n) {
        if ((n & Long.MIN_VALUE) == 0x0L) {
            return 1;
        }
        return -1;
    }
    
    private static long significand(final long n) {
        final long n2 = n & 0xFFFFFFFFFFFFFL;
        if (!isDenormal(n)) {
            return n2 + 4503599627370496L;
        }
        return n2;
    }
}
