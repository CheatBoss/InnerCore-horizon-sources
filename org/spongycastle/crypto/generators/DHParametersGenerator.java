package org.spongycastle.crypto.generators;

import java.math.*;
import java.security.*;
import org.spongycastle.crypto.params.*;

public class DHParametersGenerator
{
    private static final BigInteger TWO;
    private int certainty;
    private SecureRandom random;
    private int size;
    
    static {
        TWO = BigInteger.valueOf(2L);
    }
    
    public DHParameters generateParameters() {
        final BigInteger[] generateSafePrimes = DHParametersHelper.generateSafePrimes(this.size, this.certainty, this.random);
        final BigInteger bigInteger = generateSafePrimes[0];
        final BigInteger bigInteger2 = generateSafePrimes[1];
        return new DHParameters(bigInteger, DHParametersHelper.selectGenerator(bigInteger, bigInteger2, this.random), bigInteger2, DHParametersGenerator.TWO, null);
    }
    
    public void init(final int size, final int certainty, final SecureRandom random) {
        this.size = size;
        this.certainty = certainty;
        this.random = random;
    }
}
