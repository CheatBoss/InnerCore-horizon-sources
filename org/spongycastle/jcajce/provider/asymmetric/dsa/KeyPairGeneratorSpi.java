package org.spongycastle.jcajce.provider.asymmetric.dsa;

import java.util.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import org.spongycastle.crypto.generators.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.digests.*;
import org.spongycastle.crypto.*;
import java.security.spec.*;
import org.spongycastle.crypto.params.*;
import java.security.*;

public class KeyPairGeneratorSpi extends KeyPairGenerator
{
    private static Object lock;
    private static Hashtable params;
    DSAKeyPairGenerator engine;
    boolean initialised;
    DSAKeyGenerationParameters param;
    SecureRandom random;
    int strength;
    
    static {
        KeyPairGeneratorSpi.params = new Hashtable();
        KeyPairGeneratorSpi.lock = new Object();
    }
    
    public KeyPairGeneratorSpi() {
        super("DSA");
        this.engine = new DSAKeyPairGenerator();
        this.strength = 2048;
        this.random = new SecureRandom();
        this.initialised = false;
    }
    
    @Override
    public KeyPair generateKeyPair() {
        if (!this.initialised) {
            final Integer value = Integers.valueOf(this.strength);
            Label_0274: {
                if (KeyPairGeneratorSpi.params.containsKey(value)) {
                    this.param = (DSAKeyGenerationParameters)KeyPairGeneratorSpi.params.get(value);
                    break Label_0274;
                }
                synchronized (KeyPairGeneratorSpi.lock) {
                    if (KeyPairGeneratorSpi.params.containsKey(value)) {
                        this.param = (DSAKeyGenerationParameters)KeyPairGeneratorSpi.params.get(value);
                    }
                    else {
                        final int defaultCertainty = PrimeCertaintyCalculator.getDefaultCertainty(this.strength);
                        DSAParametersGenerator dsaParametersGenerator = null;
                        Label_0240: {
                            int n;
                            SecureRandom secureRandom;
                            if (this.strength == 1024) {
                                dsaParametersGenerator = new DSAParametersGenerator();
                                if (!Properties.isOverrideSet("org.spongycastle.dsa.FIPS186-2for1024bits")) {
                                    dsaParametersGenerator.init(new DSAParameterGenerationParameters(1024, 160, defaultCertainty, this.random));
                                    break Label_0240;
                                }
                                n = this.strength;
                                secureRandom = this.random;
                            }
                            else {
                                if (this.strength > 1024) {
                                    final DSAParameterGenerationParameters dsaParameterGenerationParameters = new DSAParameterGenerationParameters(this.strength, 256, defaultCertainty, this.random);
                                    dsaParametersGenerator = new DSAParametersGenerator(new SHA256Digest());
                                    dsaParametersGenerator.init(dsaParameterGenerationParameters);
                                    break Label_0240;
                                }
                                dsaParametersGenerator = new DSAParametersGenerator();
                                n = this.strength;
                                secureRandom = this.random;
                            }
                            dsaParametersGenerator.init(n, defaultCertainty, secureRandom);
                        }
                        final DSAKeyGenerationParameters param = new DSAKeyGenerationParameters(this.random, dsaParametersGenerator.generateParameters());
                        this.param = param;
                        KeyPairGeneratorSpi.params.put(value, param);
                    }
                    // monitorexit(KeyPairGeneratorSpi.lock)
                    this.engine.init(this.param);
                    this.initialised = true;
                }
            }
        }
        final AsymmetricCipherKeyPair generateKeyPair = this.engine.generateKeyPair();
        return new KeyPair(new BCDSAPublicKey((DSAPublicKeyParameters)generateKeyPair.getPublic()), new BCDSAPrivateKey((DSAPrivateKeyParameters)generateKeyPair.getPrivate()));
    }
    
    @Override
    public void initialize(final int strength, final SecureRandom random) {
        if (strength >= 512 && strength <= 4096 && (strength >= 1024 || strength % 64 == 0) && (strength < 1024 || strength % 1024 == 0)) {
            this.strength = strength;
            this.random = random;
            this.initialised = false;
            return;
        }
        throw new InvalidParameterException("strength must be from 512 - 4096 and a multiple of 1024 above 1024");
    }
    
    @Override
    public void initialize(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        if (algorithmParameterSpec instanceof DSAParameterSpec) {
            final DSAParameterSpec dsaParameterSpec = (DSAParameterSpec)algorithmParameterSpec;
            final DSAKeyGenerationParameters param = new DSAKeyGenerationParameters(secureRandom, new DSAParameters(dsaParameterSpec.getP(), dsaParameterSpec.getQ(), dsaParameterSpec.getG()));
            this.param = param;
            this.engine.init(param);
            this.initialised = true;
            return;
        }
        throw new InvalidAlgorithmParameterException("parameter object not a DSAParameterSpec");
    }
}
