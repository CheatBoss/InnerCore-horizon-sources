package org.mozilla.javascript.v8dtoa;

public class DoubleHelper
{
    private static final int kDenormalExponent = -1074;
    private static final int kExponentBias = 1075;
    static final long kExponentMask = 9218868437227405312L;
    static final long kHiddenBit = 4503599627370496L;
    static final long kSignMask = Long.MIN_VALUE;
    static final long kSignificandMask = 4503599627370495L;
    private static final int kSignificandSize = 52;
    
    static DiyFp asDiyFp(final long n) {
        return new DiyFp(significand(n), exponent(n));
    }
    
    static DiyFp asNormalizedDiyFp(long n) {
        final long significand = significand(n);
        int exponent;
        for (exponent = exponent(n), n = significand; (n & 0x10000000000000L) == 0x0L; n <<= 1, --exponent) {}
        return new DiyFp(n << 11, exponent - 11);
    }
    
    static int exponent(final long n) {
        if (isDenormal(n)) {
            return -1074;
        }
        return (int)((n & 0x7FF0000000000000L) >>> 52 & 0xFFFFFFFFL) - 1075;
    }
    
    static boolean isDenormal(final long n) {
        return (n & 0x7FF0000000000000L) == 0x0L;
    }
    
    static boolean isInfinite(final long n) {
        return (n & 0x7FF0000000000000L) == 0x7FF0000000000000L && (n & 0xFFFFFFFFFFFFFL) == 0x0L;
    }
    
    static boolean isNan(final long n) {
        return (n & 0x7FF0000000000000L) == 0x7FF0000000000000L && (n & 0xFFFFFFFFFFFFFL) != 0x0L;
    }
    
    static boolean isSpecial(final long n) {
        return (n & 0x7FF0000000000000L) == 0x7FF0000000000000L;
    }
    
    static void normalizedBoundaries(final long n, final DiyFp diyFp, final DiyFp diyFp2) {
        final DiyFp diyFp3 = asDiyFp(n);
        final boolean b = diyFp3.f() == 4503599627370496L;
        diyFp2.setF((diyFp3.f() << 1) + 1L);
        diyFp2.setE(diyFp3.e() - 1);
        diyFp2.normalize();
        if (b && diyFp3.e() != -1074) {
            diyFp.setF((diyFp3.f() << 2) - 1L);
            diyFp.setE(diyFp3.e() - 2);
        }
        else {
            diyFp.setF((diyFp3.f() << 1) - 1L);
            diyFp.setE(diyFp3.e() - 1);
        }
        diyFp.setF(diyFp.f() << diyFp.e() - diyFp2.e());
        diyFp.setE(diyFp2.e());
    }
    
    static int sign(final long n) {
        if ((n & Long.MIN_VALUE) == 0x0L) {
            return 1;
        }
        return -1;
    }
    
    static long significand(final long n) {
        final long n2 = n & 0xFFFFFFFFFFFFFL;
        if (!isDenormal(n)) {
            return n2 + 4503599627370496L;
        }
        return n2;
    }
}
