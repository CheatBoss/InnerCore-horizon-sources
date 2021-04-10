package org.spongycastle.crypto.generators;

import org.spongycastle.crypto.params.*;
import java.math.*;
import org.spongycastle.crypto.*;

public class DHKeyPairGenerator implements AsymmetricCipherKeyPairGenerator
{
    private DHKeyGenerationParameters param;
    
    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        final DHKeyGeneratorHelper instance = DHKeyGeneratorHelper.INSTANCE;
        final DHParameters parameters = this.param.getParameters();
        final BigInteger calculatePrivate = instance.calculatePrivate(parameters, this.param.getRandom());
        return new AsymmetricCipherKeyPair(new DHPublicKeyParameters(instance.calculatePublic(parameters, calculatePrivate), parameters), new DHPrivateKeyParameters(calculatePrivate, parameters));
    }
    
    @Override
    public void init(final KeyGenerationParameters keyGenerationParameters) {
        this.param = (DHKeyGenerationParameters)keyGenerationParameters;
    }
}
