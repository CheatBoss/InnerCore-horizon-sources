package org.mozilla.javascript.v8dtoa;

public class FastDtoa
{
    static final int kFastDtoaMaximalLength = 17;
    static final int kTen4 = 10000;
    static final int kTen5 = 100000;
    static final int kTen6 = 1000000;
    static final int kTen7 = 10000000;
    static final int kTen8 = 100000000;
    static final int kTen9 = 1000000000;
    static final int maximal_target_exponent = -32;
    static final int minimal_target_exponent = -60;
    
    static long biggestPowerTen(int n, int n2) {
        switch (n2) {
            default: {
                n = 0;
                n2 = 0;
                break;
            }
            case 30:
            case 31:
            case 32: {
                if (1000000000 <= n) {
                    n = 1000000000;
                    n2 = 9;
                    break;
                }
            }
            case 27:
            case 28:
            case 29: {
                if (100000000 <= n) {
                    n = 100000000;
                    n2 = 8;
                    break;
                }
            }
            case 24:
            case 25:
            case 26: {
                if (10000000 <= n) {
                    n = 10000000;
                    n2 = 7;
                    break;
                }
            }
            case 20:
            case 21:
            case 22:
            case 23: {
                if (1000000 <= n) {
                    n = 1000000;
                    n2 = 6;
                    break;
                }
            }
            case 17:
            case 18:
            case 19: {
                if (100000 <= n) {
                    n = 100000;
                    n2 = 5;
                    break;
                }
            }
            case 14:
            case 15:
            case 16: {
                if (10000 <= n) {
                    n = 10000;
                    n2 = 4;
                    break;
                }
            }
            case 10:
            case 11:
            case 12:
            case 13: {
                if (1000 <= n) {
                    n = 1000;
                    n2 = 3;
                    break;
                }
            }
            case 7:
            case 8:
            case 9: {
                if (100 <= n) {
                    n = 100;
                    n2 = 2;
                    break;
                }
            }
            case 4:
            case 5:
            case 6: {
                if (10 <= n) {
                    n = 10;
                    n2 = 1;
                    break;
                }
            }
            case 1:
            case 2:
            case 3: {
                if (1 <= n) {
                    n = 1;
                    n2 = 0;
                    break;
                }
            }
            case 0: {
                n = 0;
                n2 = -1;
                break;
            }
        }
        return (long)n << 32 | ((long)n2 & 0xFFFFFFFFL);
    }
    
    static boolean digitGen(DiyFp diyFp, final DiyFp diyFp2, DiyFp minus, final FastDtoaBuilder fastDtoaBuilder, final int n) {
        final DiyFp diyFp3 = new DiyFp(diyFp.f() - 1L, diyFp.e());
        diyFp = new DiyFp(minus.f() + 1L, minus.e());
        minus = DiyFp.minus(diyFp, diyFp3);
        final DiyFp diyFp4 = new DiyFp(1L << -diyFp2.e(), diyFp2.e());
        int n2 = (int)(diyFp.f() >>> -diyFp4.e() & 0xFFFFFFFFL);
        long n3 = diyFp.f() & diyFp4.f() - 1L;
        final long biggestPowerTen = biggestPowerTen(n2, 64 - -diyFp4.e());
        long n4 = 1L;
        int n5 = (int)(biggestPowerTen >>> 32 & 0xFFFFFFFFL);
        int i = (int)(biggestPowerTen & 0xFFFFFFFFL) + 1;
        while (i > 0) {
            fastDtoaBuilder.append((char)(n2 / n5 + 48));
            n2 %= n5;
            --i;
            final long n6 = ((long)n2 << -diyFp4.e()) + n3;
            if (n6 < minus.f()) {
                fastDtoaBuilder.point = fastDtoaBuilder.end - n + i;
                return roundWeed(fastDtoaBuilder, DiyFp.minus(diyFp, diyFp2).f(), minus.f(), n6, (long)n5 << -diyFp4.e(), n4);
            }
            n5 /= 10;
        }
        do {
            final long n7 = n3 * 5L;
            n4 *= 5L;
            minus.setF(minus.f() * 5L);
            minus.setE(minus.e() + 1);
            diyFp4.setF(diyFp4.f() >>> 1);
            diyFp4.setE(diyFp4.e() + 1);
            fastDtoaBuilder.append((char)((int)(n7 >>> -diyFp4.e() & 0xFFFFFFFFL) + 48));
            n3 = (n7 & diyFp4.f() - 1L);
            --i;
        } while (n3 >= minus.f());
        fastDtoaBuilder.point = fastDtoaBuilder.end - n + i;
        return roundWeed(fastDtoaBuilder, DiyFp.minus(diyFp, diyFp2).f() * n4, minus.f(), n3, diyFp4.f(), n4);
    }
    
    public static boolean dtoa(final double n, final FastDtoaBuilder fastDtoaBuilder) {
        return grisu3(n, fastDtoaBuilder);
    }
    
    static boolean grisu3(final double n, final FastDtoaBuilder fastDtoaBuilder) {
        final long doubleToLongBits = Double.doubleToLongBits(n);
        final DiyFp normalizedDiyFp = DoubleHelper.asNormalizedDiyFp(doubleToLongBits);
        final DiyFp diyFp = new DiyFp();
        final DiyFp diyFp2 = new DiyFp();
        DoubleHelper.normalizedBoundaries(doubleToLongBits, diyFp, diyFp2);
        final DiyFp diyFp3 = new DiyFp();
        return digitGen(DiyFp.times(diyFp, diyFp3), DiyFp.times(normalizedDiyFp, diyFp3), DiyFp.times(diyFp2, diyFp3), fastDtoaBuilder, CachedPowers.getCachedPower(normalizedDiyFp.e() + 64, -60, -32, diyFp3));
    }
    
    public static String numberToString(final double n) {
        final FastDtoaBuilder fastDtoaBuilder = new FastDtoaBuilder();
        if (numberToString(n, fastDtoaBuilder)) {
            return fastDtoaBuilder.format();
        }
        return null;
    }
    
    public static boolean numberToString(final double n, final FastDtoaBuilder fastDtoaBuilder) {
        fastDtoaBuilder.reset();
        double n2 = n;
        if (n < 0.0) {
            fastDtoaBuilder.append('-');
            n2 = -n;
        }
        return dtoa(n2, fastDtoaBuilder);
    }
    
    static boolean roundWeed(final FastDtoaBuilder fastDtoaBuilder, long n, final long n2, long n3, final long n4, final long n5) {
        final long n6 = n - n5;
        n += n5;
        while (n3 < n6 && n2 - n3 >= n4 && (n3 + n4 < n6 || n6 - n3 >= n3 + n4 - n6)) {
            fastDtoaBuilder.decreaseLast();
            n3 += n4;
        }
        final boolean b = false;
        if (n3 < n && n2 - n3 >= n4 && (n3 + n4 < n || n - n3 > n3 + n4 - n)) {
            return false;
        }
        boolean b2 = b;
        if (2L * n5 <= n3) {
            b2 = b;
            if (n3 <= n2 - 4L * n5) {
                b2 = true;
            }
        }
        return b2;
    }
    
    private static boolean uint64_lte(final long n, final long n2) {
        return n == n2 || (n < n2 ^ n < 0L ^ n2 < 0L);
    }
}
