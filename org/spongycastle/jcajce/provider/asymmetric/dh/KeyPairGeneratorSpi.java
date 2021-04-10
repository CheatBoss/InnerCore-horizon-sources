package org.spongycastle.jcajce.provider.asymmetric.dh;

import java.util.*;
import org.spongycastle.util.*;
import org.spongycastle.jce.provider.*;
import java.math.*;
import org.spongycastle.crypto.generators.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import org.spongycastle.crypto.params.*;
import javax.crypto.spec.*;
import org.spongycastle.crypto.*;
import java.security.spec.*;
import java.security.*;

public class KeyPairGeneratorSpi extends KeyPairGenerator
{
    private static Object lock;
    private static Hashtable params;
    DHBasicKeyPairGenerator engine;
    boolean initialised;
    DHKeyGenerationParameters param;
    SecureRandom random;
    int strength;
    
    static {
        KeyPairGeneratorSpi.params = new Hashtable();
        KeyPairGeneratorSpi.lock = new Object();
    }
    
    public KeyPairGeneratorSpi() {
        super("DH");
        this.engine = new DHBasicKeyPairGenerator();
        this.strength = 2048;
        this.random = new SecureRandom();
        this.initialised = false;
    }
    
    @Override
    public KeyPair generateKeyPair() {
        if (!this.initialised) {
            final Integer value = Integers.valueOf(this.strength);
            Label_0188: {
                Label_0096: {
                    DHKeyGenerationParameters param;
                    if (KeyPairGeneratorSpi.params.containsKey(value)) {
                        param = KeyPairGeneratorSpi.params.get(value);
                    }
                    else {
                        final DHParameterSpec dhDefaultParameters = BouncyCastleProvider.CONFIGURATION.getDHDefaultParameters(this.strength);
                        if (dhDefaultParameters == null) {
                            break Label_0096;
                        }
                        param = new DHKeyGenerationParameters(this.random, new DHParameters(dhDefaultParameters.getP(), dhDefaultParameters.getG(), null, dhDefaultParameters.getL()));
                    }
                    this.param = param;
                    break Label_0188;
                }
                synchronized (KeyPairGeneratorSpi.lock) {
                    if (KeyPairGeneratorSpi.params.containsKey(value)) {
                        this.param = (DHKeyGenerationParameters)KeyPairGeneratorSpi.params.get(value);
                    }
                    else {
                        final DHParametersGenerator dhParametersGenerator = new DHParametersGenerator();
                        dhParametersGenerator.init(this.strength, PrimeCertaintyCalculator.getDefaultCertainty(this.strength), this.random);
                        final DHKeyGenerationParameters param2 = new DHKeyGenerationParameters(this.random, dhParametersGenerator.generateParameters());
                        this.param = param2;
                        KeyPairGeneratorSpi.params.put(value, param2);
                    }
                    // monitorexit(KeyPairGeneratorSpi.lock)
                    this.engine.init(this.param);
                    this.initialised = true;
                }
            }
        }
        final AsymmetricCipherKeyPair generateKeyPair = this.engine.generateKeyPair();
        return new KeyPair(new BCDHPublicKey((DHPublicKeyParameters)generateKeyPair.getPublic()), new BCDHPrivateKey((DHPrivateKeyParameters)generateKeyPair.getPrivate()));
    }
    
    @Override
    public void initialize(final int strength, final SecureRandom random) {
        this.strength = strength;
        this.random = random;
        this.initialised = false;
    }
    
    @Override
    public void initialize(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        if (algorithmParameterSpec instanceof DHParameterSpec) {
            final DHParameterSpec dhParameterSpec = (DHParameterSpec)algorithmParameterSpec;
            final DHKeyGenerationParameters param = new DHKeyGenerationParameters(secureRandom, new DHParameters(dhParameterSpec.getP(), dhParameterSpec.getG(), null, dhParameterSpec.getL()));
            this.param = param;
            this.engine.init(param);
            this.initialised = true;
            return;
        }
        throw new InvalidAlgorithmParameterException("parameter object not a DHParameterSpec");
    }
}
