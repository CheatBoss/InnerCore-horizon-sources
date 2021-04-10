package org.spongycastle.pqc.math.linearalgebra;

import java.math.*;
import java.security.*;
import java.io.*;
import java.util.*;

public final class IntegerFunctions
{
    private static final BigInteger FOUR;
    private static final BigInteger ONE;
    private static final int[] SMALL_PRIMES;
    private static final long SMALL_PRIME_PRODUCT = 152125131763605L;
    private static final BigInteger TWO;
    private static final BigInteger ZERO;
    private static final int[] jacobiTable;
    private static SecureRandom sr;
    
    static {
        ZERO = BigInteger.valueOf(0L);
        ONE = BigInteger.valueOf(1L);
        TWO = BigInteger.valueOf(2L);
        FOUR = BigInteger.valueOf(4L);
        SMALL_PRIMES = new int[] { 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41 };
        IntegerFunctions.sr = null;
        jacobiTable = new int[] { 0, 1, 0, -1, 0, -1, 0, 1 };
    }
    
    private IntegerFunctions() {
    }
    
    public static BigInteger binomial(final int n, int i) {
        BigInteger bigInteger = IntegerFunctions.ONE;
        if (n != 0) {
            int n2;
            if ((n2 = i) > n >>> 1) {
                n2 = n - i;
            }
            for (i = 1; i <= n2; ++i) {
                bigInteger = bigInteger.multiply(BigInteger.valueOf(n - (i - 1))).divide(BigInteger.valueOf(i));
            }
            return bigInteger;
        }
        if (i == 0) {
            return bigInteger;
        }
        return IntegerFunctions.ZERO;
    }
    
    public static int bitCount(int i) {
        int n = 0;
        while (i != 0) {
            n += (i & 0x1);
            i >>>= 1;
        }
        return n;
    }
    
    public static int ceilLog(final int n) {
        int i;
        int n2;
        for (i = 1, n2 = 0; i < n; i <<= 1, ++n2) {}
        return n2;
    }
    
    public static int ceilLog(final BigInteger bigInteger) {
        BigInteger bigInteger2 = IntegerFunctions.ONE;
        int n = 0;
        while (bigInteger2.compareTo(bigInteger) < 0) {
            ++n;
            bigInteger2 = bigInteger2.shiftLeft(1);
        }
        return n;
    }
    
    public static int ceilLog256(int n) {
        if (n == 0) {
            return 1;
        }
        int i;
        if ((i = n) < 0) {
            i = -n;
        }
        n = 0;
        while (i > 0) {
            ++n;
            i >>>= 8;
        }
        return n;
    }
    
    public static int ceilLog256(final long n) {
        if (n == 0L) {
            return 1;
        }
        long n2 = n;
        if (n < 0L) {
            n2 = -n;
        }
        int n3 = 0;
        while (n2 > 0L) {
            ++n3;
            n2 >>>= 8;
        }
        return n3;
    }
    
    public static BigInteger divideAndRound(final BigInteger bigInteger, final BigInteger bigInteger2) {
        if (bigInteger.signum() < 0) {
            return divideAndRound(bigInteger.negate(), bigInteger2).negate();
        }
        if (bigInteger2.signum() < 0) {
            return divideAndRound(bigInteger, bigInteger2.negate()).negate();
        }
        return bigInteger.shiftLeft(1).add(bigInteger2).divide(bigInteger2.shiftLeft(1));
    }
    
    public static BigInteger[] divideAndRound(final BigInteger[] array, final BigInteger bigInteger) {
        final BigInteger[] array2 = new BigInteger[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = divideAndRound(array[i], bigInteger);
        }
        return array2;
    }
    
    public static int[] extGCD(final int n, final int n2) {
        final BigInteger[] extgcd = extgcd(BigInteger.valueOf(n), BigInteger.valueOf(n2));
        return new int[] { extgcd[0].intValue(), extgcd[1].intValue(), extgcd[2].intValue() };
    }
    
    public static BigInteger[] extgcd(BigInteger bigInteger, final BigInteger bigInteger2) {
        final BigInteger one = IntegerFunctions.ONE;
        BigInteger bigInteger3 = IntegerFunctions.ZERO;
        BigInteger bigInteger4 = one;
        BigInteger bigInteger5 = bigInteger;
        if (bigInteger2.signum() != 0) {
            BigInteger zero = IntegerFunctions.ZERO;
            BigInteger bigInteger6 = one;
            BigInteger bigInteger7 = bigInteger;
            BigInteger bigInteger10;
            BigInteger subtract;
            BigInteger bigInteger11;
            for (BigInteger bigInteger8 = bigInteger2; bigInteger8.signum() != 0; bigInteger8 = bigInteger10, bigInteger11 = subtract, bigInteger6 = zero, zero = bigInteger11) {
                final BigInteger[] divideAndRemainder = bigInteger7.divideAndRemainder(bigInteger8);
                final BigInteger bigInteger9 = divideAndRemainder[0];
                bigInteger10 = divideAndRemainder[1];
                subtract = bigInteger6.subtract(bigInteger9.multiply(zero));
                bigInteger7 = bigInteger8;
            }
            bigInteger3 = bigInteger7.subtract(bigInteger.multiply(bigInteger6)).divide(bigInteger2);
            bigInteger = bigInteger6;
            bigInteger5 = bigInteger7;
            bigInteger4 = bigInteger;
        }
        return new BigInteger[] { bigInteger5, bigInteger4, bigInteger3 };
    }
    
    public static float floatPow(final float n, int i) {
        float n2 = 1.0f;
        while (i > 0) {
            n2 *= n;
            --i;
        }
        return n2;
    }
    
    public static int floorLog(int i) {
        if (i <= 0) {
            return -1;
        }
        i >>>= 1;
        int n = 0;
        while (i > 0) {
            ++n;
            i >>>= 1;
        }
        return n;
    }
    
    public static int floorLog(final BigInteger bigInteger) {
        BigInteger bigInteger2 = IntegerFunctions.ONE;
        int n = -1;
        while (bigInteger2.compareTo(bigInteger) <= 0) {
            ++n;
            bigInteger2 = bigInteger2.shiftLeft(1);
        }
        return n;
    }
    
    public static int gcd(final int n, final int n2) {
        return BigInteger.valueOf(n).gcd(BigInteger.valueOf(n2)).intValue();
    }
    
    public static float intRoot(final int n, final int n2) {
        float n3 = (float)(n / n2);
        float n5;
        for (float n4 = 0.0f; Math.abs(n4 - n3) > 1.0E-4; n4 = n3, n3 -= n5) {
            float floatPow;
            while (true) {
                floatPow = floatPow(n3, n2);
                if (!Float.isInfinite(floatPow)) {
                    break;
                }
                n3 = (n3 + n4) / 2.0f;
            }
            n5 = (floatPow - n) / (n2 * floatPow(n3, n2 - 1));
        }
        return n3;
    }
    
    public static byte[] integerToOctets(final BigInteger bigInteger) {
        final byte[] byteArray = bigInteger.abs().toByteArray();
        if ((bigInteger.bitLength() & 0x7) != 0x0) {
            return byteArray;
        }
        final int n = bigInteger.bitLength() >> 3;
        final byte[] array = new byte[n];
        System.arraycopy(byteArray, 1, array, 0, n);
        return array;
    }
    
    public static boolean isIncreasing(final int[] array) {
        for (int i = 1; i < array.length; ++i) {
            final int n = i - 1;
            if (array[n] >= array[i]) {
                final PrintStream out = System.out;
                final StringBuilder sb = new StringBuilder();
                sb.append("a[");
                sb.append(n);
                sb.append("] = ");
                sb.append(array[n]);
                sb.append(" >= ");
                sb.append(array[i]);
                sb.append(" = a[");
                sb.append(i);
                sb.append("]");
                out.println(sb.toString());
                return false;
            }
        }
        return true;
    }
    
    public static int isPower(int n, final int n2) {
        if (n <= 0) {
            return -1;
        }
        final int n3 = 0;
        int i;
        for (i = n, n = n3; i > 1; i /= n2, ++n) {
            if (i % n2 != 0) {
                return -1;
            }
        }
        return n;
    }
    
    public static boolean isPrime(final int n) {
        if (n < 2) {
            return false;
        }
        if (n == 2) {
            return true;
        }
        if ((n & 0x1) == 0x0) {
            return false;
        }
        if (n < 42) {
            int n2 = 0;
            while (true) {
                final int[] small_PRIMES = IntegerFunctions.SMALL_PRIMES;
                if (n2 >= small_PRIMES.length) {
                    break;
                }
                if (n == small_PRIMES[n2]) {
                    return true;
                }
                ++n2;
            }
        }
        return n % 3 != 0 && n % 5 != 0 && n % 7 != 0 && n % 11 != 0 && n % 13 != 0 && n % 17 != 0 && n % 19 != 0 && n % 23 != 0 && n % 29 != 0 && n % 31 != 0 && n % 37 != 0 && n % 41 != 0 && BigInteger.valueOf(n).isProbablePrime(20);
    }
    
    public static int jacobi(final BigInteger bigInteger, final BigInteger bigInteger2) {
        throw new Runtime("d2j fail translate: java.lang.RuntimeException: \r\n\tat com.googlecode.dex2jar.ir.ts.NewTransformer.transform(NewTransformer.java:134)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:148)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
    }
    
    public static BigInteger leastCommonMultiple(final BigInteger[] array) {
        final int length = array.length;
        BigInteger divide = array[0];
        for (int i = 1; i < length; ++i) {
            divide = divide.multiply(array[i]).divide(divide.gcd(array[i]));
        }
        return divide;
    }
    
    public static int leastDiv(int i) {
        int n = i;
        if (i < 0) {
            n = -i;
        }
        if (n == 0) {
            return 1;
        }
        if ((n & 0x1) == 0x0) {
            return 2;
        }
        for (i = 3; i <= n / i; i += 2) {
            if (n % i == 0) {
                return i;
            }
        }
        return n;
    }
    
    public static double log(double logBKM) {
        if (logBKM > 0.0 && logBKM < 1.0) {
            return -log(1.0 / logBKM);
        }
        int n = 0;
        double n2 = 1.0;
        for (double n3 = logBKM; n3 > 2.0; n3 /= 2.0, ++n, n2 *= 2.0) {}
        logBKM = logBKM(logBKM / n2);
        final double n4 = n;
        Double.isNaN(n4);
        return n4 + logBKM;
    }
    
    public static double log(final long n) {
        final int floorLog = floorLog(BigInteger.valueOf(n));
        final long n2 = 1 << floorLog;
        final double n3 = (double)n;
        final double n4 = (double)n2;
        Double.isNaN(n3);
        Double.isNaN(n4);
        final double logBKM = logBKM(n3 / n4);
        final double n5 = floorLog;
        Double.isNaN(n5);
        return n5 + logBKM;
    }
    
    private static double logBKM(final double n) {
        double n2 = 1.0;
        int i = 0;
        double n3 = 0.0;
        double n4 = 1.0;
        while (i < 53) {
            final double n5 = n2 * n4 + n2;
            double n6 = n3;
            if (n5 <= n) {
                n6 = n3 + (new double[] { 1.0, 0.5849625007211562, 0.32192809488736235, 0.16992500144231237, 0.0874628412503394, 0.044394119358453436, 0.02236781302845451, 0.01122725542325412, 0.005624549193878107, 0.0028150156070540383, 0.0014081943928083889, 7.042690112466433E-4, 3.5217748030102726E-4, 1.7609948644250602E-4, 8.80524301221769E-5, 4.4026886827316716E-5, 2.2013611360340496E-5, 1.1006847667481442E-5, 5.503434330648604E-6, 2.751719789561283E-6, 1.375860550841138E-6, 6.879304394358497E-7, 3.4396526072176454E-7, 1.7198264061184464E-7, 8.599132286866321E-8, 4.299566207501687E-8, 2.1497831197679756E-8, 1.0748915638882709E-8, 5.374457829452062E-9, 2.687228917228708E-9, 1.3436144592400231E-9, 6.718072297764289E-10, 3.3590361492731876E-10, 1.6795180747343547E-10, 8.397590373916176E-11, 4.1987951870191886E-11, 2.0993975935248694E-11, 1.0496987967662534E-11, 5.2484939838408146E-12, 2.624246991922794E-12, 1.3121234959619935E-12, 6.56061747981146E-13, 3.2803087399061026E-13, 1.6401543699531447E-13, 8.200771849765956E-14, 4.1003859248830365E-14, 2.0501929624415328E-14, 1.02509648122077E-14, 5.1254824061038595E-15, 2.5627412030519317E-15, 1.2813706015259665E-15, 6.406853007629834E-16, 3.203426503814917E-16, 1.6017132519074588E-16, 8.008566259537294E-17, 4.004283129768647E-17, 2.0021415648843235E-17, 1.0010707824421618E-17, 5.005353912210809E-18, 2.5026769561054044E-18, 1.2513384780527022E-18, 6.256692390263511E-19, 3.1283461951317555E-19, 1.5641730975658778E-19, 7.820865487829389E-20, 3.9104327439146944E-20, 1.9552163719573472E-20, 9.776081859786736E-21, 4.888040929893368E-21, 2.444020464946684E-21, 1.222010232473342E-21, 6.11005116236671E-22, 3.055025581183355E-22, 1.5275127905916775E-22, 7.637563952958387E-23, 3.818781976479194E-23, 1.909390988239597E-23, 9.546954941197984E-24, 4.773477470598992E-24, 2.386738735299496E-24, 1.193369367649748E-24, 5.96684683824874E-25, 2.98342341912437E-25, 1.491711709562185E-25, 7.458558547810925E-26, 3.7292792739054626E-26, 1.8646396369527313E-26, 9.323198184763657E-27, 4.661599092381828E-27, 2.330799546190914E-27, 1.165399773095457E-27, 5.826998865477285E-28, 2.9134994327386427E-28, 1.4567497163693213E-28, 7.283748581846607E-29, 3.6418742909233034E-29, 1.8209371454616517E-29, 9.104685727308258E-30, 4.552342863654129E-30, 2.2761714318270646E-30 })[i];
                n2 = n5;
            }
            n4 *= 0.5;
            ++i;
            n3 = n6;
        }
        return n3;
    }
    
    public static int maxPower(final int n) {
        int n2 = 0;
        int n3 = 0;
        if (n != 0) {
            int n4 = 1;
            while (true) {
                n2 = n3;
                if ((n & n4) != 0x0) {
                    break;
                }
                ++n3;
                n4 <<= 1;
            }
        }
        return n2;
    }
    
    public static long mod(long n, final long n2) {
        final long n3 = n %= n2;
        if (n3 < 0L) {
            n = n3 + n2;
        }
        return n;
    }
    
    public static int modInverse(final int n, final int n2) {
        return BigInteger.valueOf(n).modInverse(BigInteger.valueOf(n2)).intValue();
    }
    
    public static long modInverse(final long n, final long n2) {
        return BigInteger.valueOf(n).modInverse(BigInteger.valueOf(n2)).longValue();
    }
    
    public static int modPow(int n, int i, final int n2) {
        if (n2 > 0 && n2 * n2 <= Integer.MAX_VALUE && i >= 0) {
            n = (n % n2 + n2) % n2;
            int n3 = 1;
            while (i > 0) {
                int n4 = n3;
                if ((i & 0x1) == 0x1) {
                    n4 = n3 * n % n2;
                }
                n = n * n % n2;
                i >>>= 1;
                n3 = n4;
            }
            return n3;
        }
        return 0;
    }
    
    public static BigInteger nextPrime(final long n) {
        if (n <= 1L) {
            return BigInteger.valueOf(2L);
        }
        if (n == 2L) {
            return BigInteger.valueOf(3L);
        }
        long n2 = n + 1L + (n & 0x1L);
        int n3 = 0;
        long n4 = 0L;
        while (n2 <= n << 1 && n3 == 0) {
            for (long n5 = 3L; n5 <= n2 >> 1 && n3 == 0; n5 += 2L) {
                if (n2 % n5 == 0L) {
                    n3 = 1;
                }
            }
            if (n3 == 0) {
                n4 = n2;
            }
            n3 ^= 0x1;
            n2 += 2L;
        }
        return BigInteger.valueOf(n4);
    }
    
    public static BigInteger nextProbablePrime(final BigInteger bigInteger) {
        return nextProbablePrime(bigInteger, 20);
    }
    
    public static BigInteger nextProbablePrime(BigInteger bigInteger, final int n) {
        if (bigInteger.signum() < 0 || bigInteger.signum() == 0 || bigInteger.equals(IntegerFunctions.ONE)) {
            return IntegerFunctions.TWO;
        }
        final BigInteger bigInteger2 = bigInteger = bigInteger.add(IntegerFunctions.ONE);
        while (true) {
        Label_0218:
            while (true) {
                Label_0067: {
                    if (bigInteger2.testBit(0)) {
                        break Label_0067;
                    }
                    final BigInteger one = IntegerFunctions.ONE;
                    bigInteger = bigInteger2;
                    final BigInteger two = one;
                    bigInteger = bigInteger.add(two);
                }
                if (bigInteger.bitLength() <= 6) {
                    break Label_0218;
                }
                final long longValue = bigInteger.remainder(BigInteger.valueOf(152125131763605L)).longValue();
                if (longValue % 3L != 0L && longValue % 5L != 0L && longValue % 7L != 0L && longValue % 11L != 0L && longValue % 13L != 0L && longValue % 17L != 0L && longValue % 19L != 0L && longValue % 23L != 0L && longValue % 29L != 0L && longValue % 31L != 0L && longValue % 37L != 0L && longValue % 41L != 0L) {
                    break Label_0218;
                }
                final BigInteger two = IntegerFunctions.TWO;
                continue;
            }
            if (bigInteger.bitLength() < 4) {
                return bigInteger;
            }
            if (bigInteger.isProbablePrime(n)) {
                return bigInteger;
            }
            continue;
        }
    }
    
    public static int nextSmallerPrime(int n) {
        if (n <= 2) {
            return 1;
        }
        if (n == 3) {
            return 2;
        }
        int n2 = n;
        while (true) {
            Label_0033: {
                if ((n & 0x1) == 0x0) {
                    --n;
                    break Label_0033;
                }
                n = n2 - 2;
            }
            if (n > 3 & (isPrime(n) ^ true)) {
                n2 = n;
                continue;
            }
            break;
        }
        return n;
    }
    
    public static BigInteger octetsToInteger(final byte[] array) {
        return octetsToInteger(array, 0, array.length);
    }
    
    public static BigInteger octetsToInteger(final byte[] array, final int n, final int n2) {
        final byte[] array2 = new byte[n2 + 1];
        array2[0] = 0;
        System.arraycopy(array, n, array2, 1, n2);
        return new BigInteger(array2);
    }
    
    public static int order(final int n, final int n2) {
        int i = n % n2;
        if (i != 0) {
            int n3 = 1;
            while (i != 1) {
                final int n4 = i * n % n2;
                if ((i = n4) < 0) {
                    i = n4 + n2;
                }
                ++n3;
            }
            return n3;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(n);
        sb.append(" is not an element of Z/(");
        sb.append(n2);
        sb.append("Z)^*; it is not meaningful to compute its order.");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static boolean passesSmallPrimeTest(final BigInteger bigInteger) {
        for (int i = 0; i < 239; ++i) {
            if (bigInteger.mod(BigInteger.valueOf((new int[] { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499, 503, 509, 521, 523, 541, 547, 557, 563, 569, 571, 577, 587, 593, 599, 601, 607, 613, 617, 619, 631, 641, 643, 647, 653, 659, 661, 673, 677, 683, 691, 701, 709, 719, 727, 733, 739, 743, 751, 757, 761, 769, 773, 787, 797, 809, 811, 821, 823, 827, 829, 839, 853, 857, 859, 863, 877, 881, 883, 887, 907, 911, 919, 929, 937, 941, 947, 953, 967, 971, 977, 983, 991, 997, 1009, 1013, 1019, 1021, 1031, 1033, 1039, 1049, 1051, 1061, 1063, 1069, 1087, 1091, 1093, 1097, 1103, 1109, 1117, 1123, 1129, 1151, 1153, 1163, 1171, 1181, 1187, 1193, 1201, 1213, 1217, 1223, 1229, 1231, 1237, 1249, 1259, 1277, 1279, 1283, 1289, 1291, 1297, 1301, 1303, 1307, 1319, 1321, 1327, 1361, 1367, 1373, 1381, 1399, 1409, 1423, 1427, 1429, 1433, 1439, 1447, 1451, 1453, 1459, 1471, 1481, 1483, 1487, 1489, 1493, 1499 })[i])).equals(IntegerFunctions.ZERO)) {
                return false;
            }
        }
        return true;
    }
    
    public static int pow(int n, int i) {
        int n2 = 1;
        while (i > 0) {
            int n3 = n2;
            if ((i & 0x1) == 0x1) {
                n3 = n2 * n;
            }
            n *= n;
            i >>>= 1;
            n2 = n3;
        }
        return n2;
    }
    
    public static long pow(long n, int i) {
        long n2 = 1L;
        while (i > 0) {
            long n3 = n2;
            if ((i & 0x1) == 0x1) {
                n3 = n2 * n;
            }
            n *= n;
            i >>>= 1;
            n2 = n3;
        }
        return n2;
    }
    
    public static BigInteger randomize(final BigInteger bigInteger) {
        if (IntegerFunctions.sr == null) {
            IntegerFunctions.sr = new SecureRandom();
        }
        return randomize(bigInteger, IntegerFunctions.sr);
    }
    
    public static BigInteger randomize(final BigInteger bigInteger, final SecureRandom secureRandom) {
        final int bitLength = bigInteger.bitLength();
        final BigInteger value = BigInteger.valueOf(0L);
        SecureRandom sr = secureRandom;
        if (secureRandom == null) {
            sr = IntegerFunctions.sr;
            if (sr == null) {
                sr = new SecureRandom();
            }
        }
        int i = 0;
        BigInteger bigInteger2 = value;
        while (i < 20) {
            bigInteger2 = new BigInteger(bitLength, sr);
            if (bigInteger2.compareTo(bigInteger) < 0) {
                return bigInteger2;
            }
            ++i;
        }
        return bigInteger2.mod(bigInteger);
    }
    
    public static BigInteger reduceInto(final BigInteger bigInteger, final BigInteger bigInteger2, final BigInteger bigInteger3) {
        return bigInteger.subtract(bigInteger2).mod(bigInteger3.subtract(bigInteger2)).add(bigInteger2);
    }
    
    public static BigInteger ressol(BigInteger bigInteger, final BigInteger bigInteger2) throws IllegalArgumentException {
        BigInteger add;
        if (bigInteger.compareTo(IntegerFunctions.ZERO) < 0) {
            add = bigInteger.add(bigInteger2);
        }
        else {
            add = bigInteger;
        }
        if (add.equals(IntegerFunctions.ZERO)) {
            return IntegerFunctions.ZERO;
        }
        if (bigInteger2.equals(IntegerFunctions.TWO)) {
            return add;
        }
        if (bigInteger2.testBit(0) && bigInteger2.testBit(1)) {
            if (jacobi(add, bigInteger2) == 1) {
                return add.modPow(bigInteger2.add(IntegerFunctions.ONE).shiftRight(2), bigInteger2);
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("No quadratic residue: ");
            sb.append(add);
            sb.append(", ");
            sb.append(bigInteger2);
            throw new IllegalArgumentException(sb.toString());
        }
        else {
            long n;
            for (bigInteger = bigInteger2.subtract(IntegerFunctions.ONE), n = 0L; !bigInteger.testBit(0); bigInteger = bigInteger.shiftRight(1), ++n) {}
            final BigInteger shiftRight = bigInteger.subtract(IntegerFunctions.ONE).shiftRight(1);
            bigInteger = add.modPow(shiftRight, bigInteger2);
            final BigInteger remainder = bigInteger.multiply(bigInteger).remainder(bigInteger2).multiply(add).remainder(bigInteger2);
            final BigInteger remainder2 = bigInteger.multiply(add).remainder(bigInteger2);
            if (remainder.equals(IntegerFunctions.ONE)) {
                return remainder2;
            }
            for (bigInteger = IntegerFunctions.TWO; jacobi(bigInteger, bigInteger2) == 1; bigInteger = bigInteger.add(IntegerFunctions.ONE)) {}
            final BigInteger modPow = bigInteger.modPow(shiftRight.multiply(IntegerFunctions.TWO).add(IntegerFunctions.ONE), bigInteger2);
            bigInteger = remainder;
            long n2 = n;
            BigInteger remainder3 = remainder2;
            long n3;
            for (BigInteger remainder4 = modPow; bigInteger.compareTo(IntegerFunctions.ONE) == 1; bigInteger = bigInteger.multiply(remainder4).mod(bigInteger2), n2 = n3) {
                n3 = 0L;
                for (BigInteger mod = bigInteger; !mod.equals(IntegerFunctions.ONE); mod = mod.multiply(mod).mod(bigInteger2), ++n3) {}
                final long n4 = n2 - n3;
                if (n4 == 0L) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("No quadratic residue: ");
                    sb2.append(add);
                    sb2.append(", ");
                    sb2.append(bigInteger2);
                    throw new IllegalArgumentException(sb2.toString());
                }
                BigInteger bigInteger3 = IntegerFunctions.ONE;
                for (long n5 = 0L; n5 < n4 - 1L; ++n5) {
                    bigInteger3 = bigInteger3.shiftLeft(1);
                }
                final BigInteger modPow2 = remainder4.modPow(bigInteger3, bigInteger2);
                remainder3 = remainder3.multiply(modPow2).remainder(bigInteger2);
                remainder4 = modPow2.multiply(modPow2).remainder(bigInteger2);
            }
            return remainder3;
        }
    }
    
    public static BigInteger squareRoot(final BigInteger bigInteger) {
        throw new Runtime("d2j fail translate: java.lang.RuntimeException: \r\n\tat com.googlecode.dex2jar.ir.ts.NewTransformer.transform(NewTransformer.java:134)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:148)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
    }
}
