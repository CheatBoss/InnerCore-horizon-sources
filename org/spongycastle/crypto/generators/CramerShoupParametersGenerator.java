package org.spongycastle.crypto.generators;

import java.math.*;
import java.security.*;
import org.spongycastle.crypto.digests.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;
import java.util.*;
import org.spongycastle.util.*;

public class CramerShoupParametersGenerator
{
    private static final BigInteger ONE;
    private int certainty;
    private SecureRandom random;
    private int size;
    
    static {
        ONE = BigInteger.valueOf(1L);
    }
    
    public CramerShoupParameters generateParameters() {
        final BigInteger bigInteger = ParametersHelper.generateSafePrimes(this.size, this.certainty, this.random)[1];
        final BigInteger selectGenerator = ParametersHelper.selectGenerator(bigInteger, this.random);
        BigInteger selectGenerator2;
        do {
            selectGenerator2 = ParametersHelper.selectGenerator(bigInteger, this.random);
        } while (selectGenerator.equals(selectGenerator2));
        return new CramerShoupParameters(bigInteger, selectGenerator, selectGenerator2, new SHA256Digest());
    }
    
    public CramerShoupParameters generateParameters(final DHParameters dhParameters) {
        final BigInteger p = dhParameters.getP();
        final BigInteger g = dhParameters.getG();
        BigInteger selectGenerator;
        do {
            selectGenerator = ParametersHelper.selectGenerator(p, this.random);
        } while (g.equals(selectGenerator));
        return new CramerShoupParameters(p, g, selectGenerator, new SHA256Digest());
    }
    
    public void init(final int size, final int certainty, final SecureRandom random) {
        this.size = size;
        this.certainty = certainty;
        this.random = random;
    }
    
    private static class ParametersHelper
    {
        private static final BigInteger TWO;
        
        static {
            TWO = BigInteger.valueOf(2L);
        }
        
        static BigInteger[] generateSafePrimes(final int n, final int n2, final SecureRandom secureRandom) {
            BigInteger add;
            BigInteger bigInteger;
            do {
                bigInteger = new BigInteger(n - 1, 2, secureRandom);
                add = bigInteger.shiftLeft(1).add(CramerShoupParametersGenerator.ONE);
            } while (!add.isProbablePrime(n2) || (n2 > 2 && !bigInteger.isProbablePrime(n2)));
            return new BigInteger[] { add, bigInteger };
        }
        
        static BigInteger selectGenerator(final BigInteger bigInteger, final SecureRandom secureRandom) {
            final BigInteger subtract = bigInteger.subtract(ParametersHelper.TWO);
            BigInteger modPow;
            do {
                modPow = BigIntegers.createRandomInRange(ParametersHelper.TWO, subtract, secureRandom).modPow(ParametersHelper.TWO, bigInteger);
            } while (modPow.equals(CramerShoupParametersGenerator.ONE));
            return modPow;
        }
    }
}
