package org.spongycastle.crypto.generators;

import java.math.*;
import java.security.*;
import org.spongycastle.math.ec.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class DSAKeyPairGenerator implements AsymmetricCipherKeyPairGenerator
{
    private static final BigInteger ONE;
    private DSAKeyGenerationParameters param;
    
    static {
        ONE = BigInteger.valueOf(1L);
    }
    
    private static BigInteger calculatePublicKey(final BigInteger bigInteger, final BigInteger bigInteger2, final BigInteger bigInteger3) {
        return bigInteger2.modPow(bigInteger3, bigInteger);
    }
    
    private static BigInteger generatePrivateKey(final BigInteger bigInteger, final SecureRandom secureRandom) {
        BigInteger randomInRange;
        do {
            final BigInteger one = DSAKeyPairGenerator.ONE;
            randomInRange = BigIntegers.createRandomInRange(one, bigInteger.subtract(one), secureRandom);
        } while (WNafUtil.getNafWeight(randomInRange) < bigInteger.bitLength() >>> 2);
        return randomInRange;
    }
    
    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        final DSAParameters parameters = this.param.getParameters();
        final BigInteger generatePrivateKey = generatePrivateKey(parameters.getQ(), this.param.getRandom());
        return new AsymmetricCipherKeyPair(new DSAPublicKeyParameters(calculatePublicKey(parameters.getP(), parameters.getG(), generatePrivateKey), parameters), new DSAPrivateKeyParameters(generatePrivateKey, parameters));
    }
    
    @Override
    public void init(final KeyGenerationParameters keyGenerationParameters) {
        this.param = (DSAKeyGenerationParameters)keyGenerationParameters;
    }
}
