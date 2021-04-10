package org.spongycastle.crypto.generators;

import java.math.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class ElGamalKeyPairGenerator implements AsymmetricCipherKeyPairGenerator
{
    private ElGamalKeyGenerationParameters param;
    
    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        final DHKeyGeneratorHelper instance = DHKeyGeneratorHelper.INSTANCE;
        final ElGamalParameters parameters = this.param.getParameters();
        final DHParameters dhParameters = new DHParameters(parameters.getP(), parameters.getG(), null, parameters.getL());
        final BigInteger calculatePrivate = instance.calculatePrivate(dhParameters, this.param.getRandom());
        return new AsymmetricCipherKeyPair(new ElGamalPublicKeyParameters(instance.calculatePublic(dhParameters, calculatePrivate), parameters), new ElGamalPrivateKeyParameters(calculatePrivate, parameters));
    }
    
    @Override
    public void init(final KeyGenerationParameters keyGenerationParameters) {
        this.param = (ElGamalKeyGenerationParameters)keyGenerationParameters;
    }
}
