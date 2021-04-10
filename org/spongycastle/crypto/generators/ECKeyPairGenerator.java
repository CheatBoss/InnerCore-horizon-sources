package org.spongycastle.crypto.generators;

import java.security.*;
import java.math.*;
import java.util.*;
import org.spongycastle.math.ec.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class ECKeyPairGenerator implements AsymmetricCipherKeyPairGenerator, ECConstants
{
    ECDomainParameters params;
    SecureRandom random;
    
    protected ECMultiplier createBasePointMultiplier() {
        return new FixedPointCombMultiplier();
    }
    
    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        final BigInteger n = this.params.getN();
        final int bitLength = n.bitLength();
        BigInteger bigInteger;
        while (true) {
            bigInteger = new BigInteger(bitLength, this.random);
            if (bigInteger.compareTo(ECKeyPairGenerator.TWO) >= 0) {
                if (bigInteger.compareTo(n) >= 0) {
                    continue;
                }
                if (WNafUtil.getNafWeight(bigInteger) < bitLength >>> 2) {
                    continue;
                }
                break;
            }
        }
        return new AsymmetricCipherKeyPair(new ECPublicKeyParameters(this.createBasePointMultiplier().multiply(this.params.getG(), bigInteger), this.params), new ECPrivateKeyParameters(bigInteger, this.params));
    }
    
    @Override
    public void init(final KeyGenerationParameters keyGenerationParameters) {
        final ECKeyGenerationParameters ecKeyGenerationParameters = (ECKeyGenerationParameters)keyGenerationParameters;
        this.random = ecKeyGenerationParameters.getRandom();
        this.params = ecKeyGenerationParameters.getDomainParameters();
        if (this.random == null) {
            this.random = new SecureRandom();
        }
    }
}
