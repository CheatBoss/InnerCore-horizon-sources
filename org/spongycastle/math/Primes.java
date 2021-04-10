package org.spongycastle.math;

import java.math.*;
import java.security.*;
import org.spongycastle.crypto.*;
import org.spongycastle.util.*;

public abstract class Primes
{
    private static final BigInteger ONE;
    public static final int SMALL_FACTOR_LIMIT = 211;
    private static final BigInteger THREE;
    private static final BigInteger TWO;
    
    static {
        ONE = BigInteger.valueOf(1L);
        TWO = BigInteger.valueOf(2L);
        THREE = BigInteger.valueOf(3L);
    }
    
    private static void checkCandidate(final BigInteger bigInteger, final String s) {
        if (bigInteger != null && bigInteger.signum() >= 1 && bigInteger.bitLength() >= 2) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("'");
        sb.append(s);
        sb.append("' must be non-null and >= 2");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static MROutput enhancedMRProbablePrimeTest(BigInteger gcd, final SecureRandom secureRandom, final int n) {
        checkCandidate(gcd, "candidate");
        if (secureRandom == null) {
            throw new IllegalArgumentException("'random' cannot be null");
        }
        if (n < 1) {
            throw new IllegalArgumentException("'iterations' must be > 0");
        }
        if (gcd.bitLength() == 2) {
            return probablyPrime();
        }
        if (!gcd.testBit(0)) {
            return provablyCompositeWithFactor(Primes.TWO);
        }
        final BigInteger subtract = gcd.subtract(Primes.ONE);
        final BigInteger subtract2 = gcd.subtract(Primes.TWO);
        final int lowestSetBit = subtract.getLowestSetBit();
        final BigInteger shiftRight = subtract.shiftRight(lowestSetBit);
        BigInteger randomInRange;
        BigInteger gcd2;
        BigInteger bigInteger;
        int j;
        BigInteger modPow;
        boolean b;
        Label_0221_Outer:Label_0301:
        for (int i = 0; i < n; ++i) {
            randomInRange = BigIntegers.createRandomInRange(Primes.TWO, subtract2, secureRandom);
            gcd2 = randomInRange.gcd(gcd);
            if (gcd2.compareTo(Primes.ONE) > 0) {
                return provablyCompositeWithFactor(gcd2);
            }
            bigInteger = randomInRange.modPow(shiftRight, gcd);
            if (!bigInteger.equals(Primes.ONE)) {
                if (!bigInteger.equals(subtract)) {
                    j = 1;
                    while (true) {
                        while (j < lowestSetBit) {
                            modPow = bigInteger.modPow(Primes.TWO, gcd);
                            if (modPow.equals(subtract)) {
                                b = true;
                            }
                            else {
                                if (!modPow.equals(Primes.ONE)) {
                                    ++j;
                                    bigInteger = modPow;
                                    continue Label_0221_Outer;
                                }
                                b = false;
                            }
                            if (b) {
                                continue Label_0301;
                            }
                            if (!modPow.equals(Primes.ONE)) {
                                bigInteger = modPow.modPow(Primes.TWO, gcd);
                                if (bigInteger.equals(Primes.ONE)) {
                                    bigInteger = modPow;
                                }
                            }
                            gcd = bigInteger.subtract(Primes.ONE).gcd(gcd);
                            if (gcd.compareTo(Primes.ONE) > 0) {
                                return provablyCompositeWithFactor(gcd);
                            }
                            return provablyCompositeNotPrimePower();
                        }
                        modPow = bigInteger;
                        continue;
                    }
                }
            }
        }
        return probablyPrime();
    }
    
    private static int extract32(final byte[] array) {
        final int min = Math.min(4, array.length);
        int i = 0;
        int n = 0;
        while (i < min) {
            final int length = array.length;
            final int n2 = i + 1;
            n |= (array[length - n2] & 0xFF) << i * 8;
            i = n2;
        }
        return n;
    }
    
    public static STOutput generateSTRandomPrime(final Digest digest, final int n, final byte[] array) {
        if (digest == null) {
            throw new IllegalArgumentException("'hash' cannot be null");
        }
        if (n < 2) {
            throw new IllegalArgumentException("'length' must be >= 2");
        }
        if (array != null && array.length != 0) {
            return implSTRandomPrime(digest, n, Arrays.clone(array));
        }
        throw new IllegalArgumentException("'inputSeed' cannot be null or empty");
    }
    
    public static boolean hasAnySmallFactors(final BigInteger bigInteger) {
        checkCandidate(bigInteger, "candidate");
        return implHasAnySmallFactors(bigInteger);
    }
    
    private static void hash(final Digest digest, final byte[] array, final byte[] array2, final int n) {
        digest.update(array, 0, array.length);
        digest.doFinal(array2, n);
    }
    
    private static BigInteger hashGen(final Digest digest, final byte[] array, final int n) {
        final int digestSize = digest.getDigestSize();
        int n2 = n * digestSize;
        final byte[] array2 = new byte[n2];
        for (int i = 0; i < n; ++i) {
            n2 -= digestSize;
            hash(digest, array, array2, n2);
            inc(array, 1);
        }
        return new BigInteger(1, array2);
    }
    
    private static boolean implHasAnySmallFactors(final BigInteger bigInteger) {
        final int intValue = bigInteger.mod(BigInteger.valueOf(223092870)).intValue();
        if (intValue % 2 != 0 && intValue % 3 != 0 && intValue % 5 != 0 && intValue % 7 != 0 && intValue % 11 != 0 && intValue % 13 != 0 && intValue % 17 != 0 && intValue % 19 != 0) {
            if (intValue % 23 == 0) {
                return true;
            }
            final int intValue2 = bigInteger.mod(BigInteger.valueOf(58642669)).intValue();
            if (intValue2 % 29 != 0 && intValue2 % 31 != 0 && intValue2 % 37 != 0 && intValue2 % 41 != 0) {
                if (intValue2 % 43 == 0) {
                    return true;
                }
                final int intValue3 = bigInteger.mod(BigInteger.valueOf(600662303)).intValue();
                if (intValue3 % 47 != 0 && intValue3 % 53 != 0 && intValue3 % 59 != 0 && intValue3 % 61 != 0) {
                    if (intValue3 % 67 == 0) {
                        return true;
                    }
                    final int intValue4 = bigInteger.mod(BigInteger.valueOf(33984931)).intValue();
                    if (intValue4 % 71 != 0 && intValue4 % 73 != 0 && intValue4 % 79 != 0) {
                        if (intValue4 % 83 == 0) {
                            return true;
                        }
                        final int intValue5 = bigInteger.mod(BigInteger.valueOf(89809099)).intValue();
                        if (intValue5 % 89 != 0 && intValue5 % 97 != 0 && intValue5 % 101 != 0) {
                            if (intValue5 % 103 == 0) {
                                return true;
                            }
                            final int intValue6 = bigInteger.mod(BigInteger.valueOf(167375713)).intValue();
                            if (intValue6 % 107 != 0 && intValue6 % 109 != 0 && intValue6 % 113 != 0) {
                                if (intValue6 % 127 == 0) {
                                    return true;
                                }
                                final int intValue7 = bigInteger.mod(BigInteger.valueOf(371700317)).intValue();
                                if (intValue7 % 131 != 0 && intValue7 % 137 != 0 && intValue7 % 139 != 0) {
                                    if (intValue7 % 149 == 0) {
                                        return true;
                                    }
                                    final int intValue8 = bigInteger.mod(BigInteger.valueOf(645328247)).intValue();
                                    if (intValue8 % 151 != 0 && intValue8 % 157 != 0 && intValue8 % 163 != 0) {
                                        if (intValue8 % 167 == 0) {
                                            return true;
                                        }
                                        final int intValue9 = bigInteger.mod(BigInteger.valueOf(1070560157)).intValue();
                                        if (intValue9 % 173 != 0 && intValue9 % 179 != 0 && intValue9 % 181 != 0) {
                                            if (intValue9 % 191 == 0) {
                                                return true;
                                            }
                                            final int intValue10 = bigInteger.mod(BigInteger.valueOf(1596463769)).intValue();
                                            if (intValue10 % 193 != 0 && intValue10 % 197 != 0 && intValue10 % 199 != 0) {
                                                return intValue10 % 211 == 0;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    
    private static boolean implMRProbablePrimeToBase(final BigInteger bigInteger, final BigInteger bigInteger2, BigInteger bigInteger3, final int n, final BigInteger bigInteger4) {
        bigInteger3 = bigInteger4.modPow(bigInteger3, bigInteger);
        final boolean equals = bigInteger3.equals(Primes.ONE);
        final boolean b = false;
        boolean b2;
        if (!equals) {
            if (bigInteger3.equals(bigInteger2)) {
                return true;
            }
            int n2 = 1;
            while (true) {
                b2 = b;
                if (n2 >= n) {
                    break;
                }
                bigInteger3 = bigInteger3.modPow(Primes.TWO, bigInteger);
                if (bigInteger3.equals(bigInteger2)) {
                    return true;
                }
                if (bigInteger3.equals(Primes.ONE)) {
                    return false;
                }
                ++n2;
            }
        }
        else {
            b2 = true;
        }
        return b2;
    }
    
    private static STOutput implSTRandomPrime(final Digest digest, final int n, final byte[] array) {
        final int digestSize = digest.getDigestSize();
        if (n < 33) {
            final byte[] array2 = new byte[digestSize];
            final byte[] array3 = new byte[digestSize];
            int n2 = 0;
            while (true) {
                hash(digest, array, array2, 0);
                inc(array, 1);
                hash(digest, array, array3, 0);
                inc(array, 1);
                final int extract32 = extract32(array2);
                final int extract33 = extract32(array3);
                ++n2;
                final long n3 = (long)(((extract32 ^ extract33) & -1 >>> 32 - n) | (1 << n - 1 | 0x1)) & 0xFFFFFFFFL;
                if (isPrime32(n3)) {
                    return new STOutput(BigInteger.valueOf(n3), array, n2);
                }
                if (n2 <= n * 4) {
                    continue;
                }
                throw new IllegalStateException("Too many iterations in Shawe-Taylor Random_Prime Routine");
            }
        }
        else {
            final STOutput implSTRandomPrime = implSTRandomPrime(digest, (n + 3) / 2, array);
            final BigInteger prime = implSTRandomPrime.getPrime();
            final byte[] primeSeed = implSTRandomPrime.getPrimeSeed();
            final int primeGenCounter = implSTRandomPrime.getPrimeGenCounter();
            final int bit = n - 1;
            final int n4 = bit / (digestSize * 8) + 1;
            final BigInteger setBit = hashGen(digest, primeSeed, n4).mod(Primes.ONE.shiftLeft(bit)).setBit(bit);
            final BigInteger shiftLeft = prime.shiftLeft(1);
            BigInteger bigInteger = setBit.subtract(Primes.ONE).divide(shiftLeft).add(Primes.ONE).shiftLeft(1);
            BigInteger bigInteger2 = bigInteger.multiply(prime).add(Primes.ONE);
            int n5 = 0;
            int n6 = primeGenCounter;
            while (true) {
                BigInteger add = bigInteger2;
                if (bigInteger2.bitLength() > n) {
                    bigInteger = Primes.ONE.shiftLeft(bit).subtract(Primes.ONE).divide(shiftLeft).add(Primes.ONE).shiftLeft(1);
                    add = bigInteger.multiply(prime).add(Primes.ONE);
                }
                ++n6;
                if (!implHasAnySmallFactors(add)) {
                    final BigInteger add2 = hashGen(digest, primeSeed, n4).mod(add.subtract(Primes.THREE)).add(Primes.TWO);
                    bigInteger = bigInteger.add(BigInteger.valueOf(n5));
                    final BigInteger modPow = add2.modPow(bigInteger, add);
                    if (add.gcd(modPow.subtract(Primes.ONE)).equals(Primes.ONE) && modPow.modPow(prime, add).equals(Primes.ONE)) {
                        return new STOutput(add, primeSeed, n6);
                    }
                    n5 = 0;
                }
                else {
                    inc(primeSeed, n4);
                }
                if (n6 >= n * 4 + primeGenCounter) {
                    throw new IllegalStateException("Too many iterations in Shawe-Taylor Random_Prime Routine");
                }
                n5 += 2;
                bigInteger2 = add.add(shiftLeft);
            }
        }
    }
    
    private static void inc(final byte[] array, int i) {
        for (int length = array.length; i > 0; i += (array[length] & 0xFF), array[length] = (byte)i, i >>>= 8) {
            --length;
            if (length < 0) {
                break;
            }
        }
    }
    
    public static boolean isMRProbablePrime(final BigInteger bigInteger, final SecureRandom secureRandom, final int n) {
        checkCandidate(bigInteger, "candidate");
        if (secureRandom == null) {
            throw new IllegalArgumentException("'random' cannot be null");
        }
        if (n < 1) {
            throw new IllegalArgumentException("'iterations' must be > 0");
        }
        if (bigInteger.bitLength() == 2) {
            return true;
        }
        if (!bigInteger.testBit(0)) {
            return false;
        }
        final BigInteger subtract = bigInteger.subtract(Primes.ONE);
        final BigInteger subtract2 = bigInteger.subtract(Primes.TWO);
        final int lowestSetBit = subtract.getLowestSetBit();
        final BigInteger shiftRight = subtract.shiftRight(lowestSetBit);
        for (int i = 0; i < n; ++i) {
            if (!implMRProbablePrimeToBase(bigInteger, subtract, shiftRight, lowestSetBit, BigIntegers.createRandomInRange(Primes.TWO, subtract2, secureRandom))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isMRProbablePrimeToBase(final BigInteger bigInteger, final BigInteger bigInteger2) {
        checkCandidate(bigInteger, "candidate");
        checkCandidate(bigInteger2, "base");
        if (bigInteger2.compareTo(bigInteger.subtract(Primes.ONE)) >= 0) {
            throw new IllegalArgumentException("'base' must be < ('candidate' - 1)");
        }
        if (bigInteger.bitLength() == 2) {
            return true;
        }
        final BigInteger subtract = bigInteger.subtract(Primes.ONE);
        final int lowestSetBit = subtract.getLowestSetBit();
        return implMRProbablePrimeToBase(bigInteger, subtract, subtract.shiftRight(lowestSetBit), lowestSetBit, bigInteger2);
    }
    
    private static boolean isPrime32(final long n) {
        if (n >>> 32 != 0L) {
            throw new IllegalArgumentException("Size limit exceeded");
        }
        final boolean b = true;
        if (n <= 5L) {
            boolean b2 = b;
            if (n != 2L) {
                b2 = b;
                if (n != 3L) {
                    if (n == 5L) {
                        return true;
                    }
                    b2 = false;
                }
            }
            return b2;
        }
        if ((n & 0x1L) == 0x0L || n % 3L == 0L) {
            return false;
        }
        if (n % 5L == 0L) {
            return false;
        }
        long n2 = 0L;
        int n3 = 1;
        while (true) {
            if (n3 < 8) {
                if (n % ((new long[] { 1L, 7L, 11L, 13L, 17L, 19L, 23L, 29L })[n3] + n2) == 0L) {
                    return n < 30L;
                }
                ++n3;
            }
            else {
                n2 += 30L;
                if (n2 * n2 >= n) {
                    return true;
                }
                n3 = 0;
            }
        }
    }
    
    public static class MROutput
    {
        private BigInteger factor;
        private boolean provablyComposite;
        
        private MROutput(final boolean provablyComposite, final BigInteger factor) {
            this.provablyComposite = provablyComposite;
            this.factor = factor;
        }
        
        private static MROutput probablyPrime() {
            return new MROutput(false, null);
        }
        
        private static MROutput provablyCompositeNotPrimePower() {
            return new MROutput(true, null);
        }
        
        private static MROutput provablyCompositeWithFactor(final BigInteger bigInteger) {
            return new MROutput(true, bigInteger);
        }
        
        public BigInteger getFactor() {
            return this.factor;
        }
        
        public boolean isNotPrimePower() {
            return this.provablyComposite && this.factor == null;
        }
        
        public boolean isProvablyComposite() {
            return this.provablyComposite;
        }
    }
    
    public static class STOutput
    {
        private BigInteger prime;
        private int primeGenCounter;
        private byte[] primeSeed;
        
        private STOutput(final BigInteger prime, final byte[] primeSeed, final int primeGenCounter) {
            this.prime = prime;
            this.primeSeed = primeSeed;
            this.primeGenCounter = primeGenCounter;
        }
        
        public BigInteger getPrime() {
            return this.prime;
        }
        
        public int getPrimeGenCounter() {
            return this.primeGenCounter;
        }
        
        public byte[] getPrimeSeed() {
            return this.primeSeed;
        }
    }
}
