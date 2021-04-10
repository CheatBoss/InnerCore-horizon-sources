package org.spongycastle.crypto.generators;

import java.math.*;
import org.spongycastle.crypto.params.*;
import java.security.*;
import org.spongycastle.math.ec.*;
import java.util.*;
import org.spongycastle.util.*;

class DHKeyGeneratorHelper
{
    static final DHKeyGeneratorHelper INSTANCE;
    private static final BigInteger ONE;
    private static final BigInteger TWO;
    
    static {
        INSTANCE = new DHKeyGeneratorHelper();
        ONE = BigInteger.valueOf(1L);
        TWO = BigInteger.valueOf(2L);
    }
    
    private DHKeyGeneratorHelper() {
    }
    
    BigInteger calculatePrivate(final DHParameters dhParameters, final SecureRandom secureRandom) {
        final int l = dhParameters.getL();
        if (l != 0) {
            BigInteger setBit;
            do {
                setBit = new BigInteger(l, secureRandom).setBit(l - 1);
            } while (WNafUtil.getNafWeight(setBit) < l >>> 2);
            return setBit;
        }
        BigInteger bigInteger = DHKeyGeneratorHelper.TWO;
        final int m = dhParameters.getM();
        if (m != 0) {
            bigInteger = DHKeyGeneratorHelper.ONE.shiftLeft(m - 1);
        }
        BigInteger bigInteger2;
        if ((bigInteger2 = dhParameters.getQ()) == null) {
            bigInteger2 = dhParameters.getP();
        }
        final BigInteger subtract = bigInteger2.subtract(DHKeyGeneratorHelper.TWO);
        BigInteger randomInRange;
        do {
            randomInRange = BigIntegers.createRandomInRange(bigInteger, subtract, secureRandom);
        } while (WNafUtil.getNafWeight(randomInRange) < subtract.bitLength() >>> 2);
        return randomInRange;
    }
    
    BigInteger calculatePublic(final DHParameters dhParameters, final BigInteger bigInteger) {
        return dhParameters.getG().modPow(bigInteger, dhParameters.getP());
    }
}
