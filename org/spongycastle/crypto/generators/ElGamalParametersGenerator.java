package org.spongycastle.crypto.generators;

import java.security.*;
import org.spongycastle.crypto.params.*;
import java.math.*;

public class ElGamalParametersGenerator
{
    private int certainty;
    private SecureRandom random;
    private int size;
    
    public ElGamalParameters generateParameters() {
        final BigInteger[] generateSafePrimes = DHParametersHelper.generateSafePrimes(this.size, this.certainty, this.random);
        final BigInteger bigInteger = generateSafePrimes[0];
        return new ElGamalParameters(bigInteger, DHParametersHelper.selectGenerator(bigInteger, generateSafePrimes[1], this.random));
    }
    
    public void init(final int size, final int certainty, final SecureRandom random) {
        this.size = size;
        this.certainty = certainty;
        this.random = random;
    }
}
