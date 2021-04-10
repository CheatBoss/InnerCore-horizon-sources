package org.spongycastle.crypto.generators;

import java.math.*;
import java.util.*;
import org.spongycastle.math.ec.*;
import org.spongycastle.crypto.params.*;
import java.security.*;
import org.spongycastle.crypto.*;

public class GOST3410KeyPairGenerator implements AsymmetricCipherKeyPairGenerator
{
    private GOST3410KeyGenerationParameters param;
    
    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        final GOST3410Parameters parameters = this.param.getParameters();
        final SecureRandom random = this.param.getRandom();
        final BigInteger q = parameters.getQ();
        final BigInteger p = parameters.getP();
        final BigInteger a = parameters.getA();
        BigInteger bigInteger;
        while (true) {
            bigInteger = new BigInteger(256, random);
            if (bigInteger.signum() >= 1) {
                if (bigInteger.compareTo(q) >= 0) {
                    continue;
                }
                if (WNafUtil.getNafWeight(bigInteger) < 64) {
                    continue;
                }
                break;
            }
        }
        return new AsymmetricCipherKeyPair(new GOST3410PublicKeyParameters(a.modPow(bigInteger, p), parameters), new GOST3410PrivateKeyParameters(bigInteger, parameters));
    }
    
    @Override
    public void init(final KeyGenerationParameters keyGenerationParameters) {
        this.param = (GOST3410KeyGenerationParameters)keyGenerationParameters;
    }
}
