package org.spongycastle.pqc.jcajce.provider.newhope;

import org.spongycastle.pqc.crypto.newhope.*;
import org.spongycastle.crypto.*;
import java.security.spec.*;
import java.security.*;

public class NHKeyPairGeneratorSpi extends KeyPairGenerator
{
    NHKeyPairGenerator engine;
    boolean initialised;
    SecureRandom random;
    
    public NHKeyPairGeneratorSpi() {
        super("NH");
        this.engine = new NHKeyPairGenerator();
        this.random = new SecureRandom();
        this.initialised = false;
    }
    
    @Override
    public KeyPair generateKeyPair() {
        if (!this.initialised) {
            this.engine.init(new KeyGenerationParameters(this.random, 1024));
            this.initialised = true;
        }
        final AsymmetricCipherKeyPair generateKeyPair = this.engine.generateKeyPair();
        return new KeyPair(new BCNHPublicKey((NHPublicKeyParameters)generateKeyPair.getPublic()), new BCNHPrivateKey((NHPrivateKeyParameters)generateKeyPair.getPrivate()));
    }
    
    @Override
    public void initialize(final int n, final SecureRandom secureRandom) {
        if (n == 1024) {
            this.engine.init(new KeyGenerationParameters(secureRandom, 1024));
            this.initialised = true;
            return;
        }
        throw new IllegalArgumentException("strength must be 1024 bits");
    }
    
    @Override
    public void initialize(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        throw new InvalidAlgorithmParameterException("parameter object not recognised");
    }
}
