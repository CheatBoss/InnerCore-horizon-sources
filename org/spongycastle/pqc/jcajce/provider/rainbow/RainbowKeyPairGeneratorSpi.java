package org.spongycastle.pqc.jcajce.provider.rainbow;

import org.spongycastle.pqc.jcajce.spec.*;
import org.spongycastle.pqc.crypto.rainbow.*;
import org.spongycastle.crypto.*;
import java.security.spec.*;
import java.security.*;

public class RainbowKeyPairGeneratorSpi extends KeyPairGenerator
{
    RainbowKeyPairGenerator engine;
    boolean initialised;
    RainbowKeyGenerationParameters param;
    SecureRandom random;
    int strength;
    
    public RainbowKeyPairGeneratorSpi() {
        super("Rainbow");
        this.engine = new RainbowKeyPairGenerator();
        this.strength = 1024;
        this.random = new SecureRandom();
        this.initialised = false;
    }
    
    @Override
    public KeyPair generateKeyPair() {
        if (!this.initialised) {
            final RainbowKeyGenerationParameters param = new RainbowKeyGenerationParameters(this.random, new RainbowParameters(new RainbowParameterSpec().getVi()));
            this.param = param;
            this.engine.init(param);
            this.initialised = true;
        }
        final AsymmetricCipherKeyPair generateKeyPair = this.engine.generateKeyPair();
        return new KeyPair(new BCRainbowPublicKey((RainbowPublicKeyParameters)generateKeyPair.getPublic()), new BCRainbowPrivateKey((RainbowPrivateKeyParameters)generateKeyPair.getPrivate()));
    }
    
    @Override
    public void initialize(final int strength, final SecureRandom random) {
        this.strength = strength;
        this.random = random;
    }
    
    @Override
    public void initialize(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        if (algorithmParameterSpec instanceof RainbowParameterSpec) {
            final RainbowKeyGenerationParameters param = new RainbowKeyGenerationParameters(secureRandom, new RainbowParameters(((RainbowParameterSpec)algorithmParameterSpec).getVi()));
            this.param = param;
            this.engine.init(param);
            this.initialised = true;
            return;
        }
        throw new InvalidAlgorithmParameterException("parameter object not a RainbowParameterSpec");
    }
}
