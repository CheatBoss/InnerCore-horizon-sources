package org.spongycastle.jcajce.provider.asymmetric.elgamal;

import org.spongycastle.jce.provider.*;
import org.spongycastle.crypto.generators.*;
import org.spongycastle.crypto.params.*;
import javax.crypto.spec.*;
import org.spongycastle.crypto.*;
import java.security.spec.*;
import org.spongycastle.jce.spec.*;
import java.security.*;

public class KeyPairGeneratorSpi extends KeyPairGenerator
{
    int certainty;
    ElGamalKeyPairGenerator engine;
    boolean initialised;
    ElGamalKeyGenerationParameters param;
    SecureRandom random;
    int strength;
    
    public KeyPairGeneratorSpi() {
        super("ElGamal");
        this.engine = new ElGamalKeyPairGenerator();
        this.strength = 1024;
        this.certainty = 20;
        this.random = new SecureRandom();
        this.initialised = false;
    }
    
    @Override
    public KeyPair generateKeyPair() {
        if (!this.initialised) {
            final DHParameterSpec dhDefaultParameters = BouncyCastleProvider.CONFIGURATION.getDHDefaultParameters(this.strength);
            ElGamalKeyGenerationParameters param;
            if (dhDefaultParameters != null) {
                param = new ElGamalKeyGenerationParameters(this.random, new ElGamalParameters(dhDefaultParameters.getP(), dhDefaultParameters.getG(), dhDefaultParameters.getL()));
            }
            else {
                final ElGamalParametersGenerator elGamalParametersGenerator = new ElGamalParametersGenerator();
                elGamalParametersGenerator.init(this.strength, this.certainty, this.random);
                param = new ElGamalKeyGenerationParameters(this.random, elGamalParametersGenerator.generateParameters());
            }
            this.param = param;
            this.engine.init(this.param);
            this.initialised = true;
        }
        final AsymmetricCipherKeyPair generateKeyPair = this.engine.generateKeyPair();
        return new KeyPair(new BCElGamalPublicKey((ElGamalPublicKeyParameters)generateKeyPair.getPublic()), new BCElGamalPrivateKey((ElGamalPrivateKeyParameters)generateKeyPair.getPrivate()));
    }
    
    @Override
    public void initialize(final int strength, final SecureRandom random) {
        this.strength = strength;
        this.random = random;
    }
    
    @Override
    public void initialize(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        final boolean b = algorithmParameterSpec instanceof ElGamalParameterSpec;
        if (!b && !(algorithmParameterSpec instanceof DHParameterSpec)) {
            throw new InvalidAlgorithmParameterException("parameter object not a DHParameterSpec or an ElGamalParameterSpec");
        }
        ElGamalKeyGenerationParameters param;
        if (b) {
            final ElGamalParameterSpec elGamalParameterSpec = (ElGamalParameterSpec)algorithmParameterSpec;
            param = new ElGamalKeyGenerationParameters(secureRandom, new ElGamalParameters(elGamalParameterSpec.getP(), elGamalParameterSpec.getG()));
        }
        else {
            final DHParameterSpec dhParameterSpec = (DHParameterSpec)algorithmParameterSpec;
            param = new ElGamalKeyGenerationParameters(secureRandom, new ElGamalParameters(dhParameterSpec.getP(), dhParameterSpec.getG(), dhParameterSpec.getL()));
        }
        this.param = param;
        this.engine.init(this.param);
        this.initialised = true;
    }
}
