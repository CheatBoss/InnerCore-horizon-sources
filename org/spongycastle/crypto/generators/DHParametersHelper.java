package org.spongycastle.crypto.generators;

import java.math.*;
import java.security.*;
import java.util.*;
import org.spongycastle.math.ec.*;
import org.spongycastle.util.*;

class DHParametersHelper
{
    private static final BigInteger ONE;
    private static final BigInteger TWO;
    
    static {
        ONE = BigInteger.valueOf(1L);
        TWO = BigInteger.valueOf(2L);
    }
    
    static BigInteger[] generateSafePrimes(final int n, final int n2, final SecureRandom secureRandom) {
        BigInteger bigInteger;
        BigInteger add;
        while (true) {
            bigInteger = new BigInteger(n - 1, 2, secureRandom);
            add = bigInteger.shiftLeft(1).add(DHParametersHelper.ONE);
            if (!add.isProbablePrime(n2)) {
                continue;
            }
            if (n2 > 2 && !bigInteger.isProbablePrime(n2 - 2)) {
                continue;
            }
            if (WNafUtil.getNafWeight(add) < n >>> 2) {
                continue;
            }
            break;
        }
        return new BigInteger[] { add, bigInteger };
    }
    
    static BigInteger selectGenerator(final BigInteger bigInteger, BigInteger subtract, final SecureRandom secureRandom) {
        subtract = bigInteger.subtract(DHParametersHelper.TWO);
        BigInteger modPow;
        do {
            modPow = BigIntegers.createRandomInRange(DHParametersHelper.TWO, subtract, secureRandom).modPow(DHParametersHelper.TWO, bigInteger);
        } while (modPow.equals(DHParametersHelper.ONE));
        return modPow;
    }
}
