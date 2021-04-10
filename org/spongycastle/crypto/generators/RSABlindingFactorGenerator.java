package org.spongycastle.crypto.generators;

import java.math.*;
import java.security.*;
import java.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class RSABlindingFactorGenerator
{
    private static BigInteger ONE;
    private static BigInteger ZERO;
    private RSAKeyParameters key;
    private SecureRandom random;
    
    static {
        RSABlindingFactorGenerator.ZERO = BigInteger.valueOf(0L);
        RSABlindingFactorGenerator.ONE = BigInteger.valueOf(1L);
    }
    
    public BigInteger generateBlindingFactor() {
        final RSAKeyParameters key = this.key;
        if (key != null) {
            final BigInteger modulus = key.getModulus();
            final int bitLength = modulus.bitLength();
            BigInteger bigInteger;
            BigInteger gcd;
            do {
                bigInteger = new BigInteger(bitLength - 1, this.random);
                gcd = bigInteger.gcd(modulus);
            } while (bigInteger.equals(RSABlindingFactorGenerator.ZERO) || bigInteger.equals(RSABlindingFactorGenerator.ONE) || !gcd.equals(RSABlindingFactorGenerator.ONE));
            return bigInteger;
        }
        throw new IllegalStateException("generator not initialised");
    }
    
    public void init(final CipherParameters cipherParameters) {
        SecureRandom random;
        if (cipherParameters instanceof ParametersWithRandom) {
            final ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            this.key = (RSAKeyParameters)parametersWithRandom.getParameters();
            random = parametersWithRandom.getRandom();
        }
        else {
            this.key = (RSAKeyParameters)cipherParameters;
            random = new SecureRandom();
        }
        this.random = random;
        if (!(this.key instanceof RSAPrivateCrtKeyParameters)) {
            return;
        }
        throw new IllegalArgumentException("generator requires RSA public key");
    }
}
