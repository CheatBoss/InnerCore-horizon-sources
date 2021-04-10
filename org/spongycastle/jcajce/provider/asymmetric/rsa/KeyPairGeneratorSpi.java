package org.spongycastle.jcajce.provider.asymmetric.rsa;

import java.math.*;
import org.spongycastle.crypto.generators.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;
import java.security.spec.*;
import java.security.*;

public class KeyPairGeneratorSpi extends KeyPairGenerator
{
    static final BigInteger defaultPublicExponent;
    RSAKeyPairGenerator engine;
    RSAKeyGenerationParameters param;
    
    static {
        defaultPublicExponent = BigInteger.valueOf(65537L);
    }
    
    public KeyPairGeneratorSpi() {
        super("RSA");
        this.engine = new RSAKeyPairGenerator();
        final RSAKeyGenerationParameters param = new RSAKeyGenerationParameters(KeyPairGeneratorSpi.defaultPublicExponent, new SecureRandom(), 2048, PrimeCertaintyCalculator.getDefaultCertainty(2048));
        this.param = param;
        this.engine.init(param);
    }
    
    public KeyPairGeneratorSpi(final String s) {
        super(s);
    }
    
    @Override
    public KeyPair generateKeyPair() {
        final AsymmetricCipherKeyPair generateKeyPair = this.engine.generateKeyPair();
        return new KeyPair(new BCRSAPublicKey((RSAKeyParameters)generateKeyPair.getPublic()), new BCRSAPrivateCrtKey((RSAPrivateCrtKeyParameters)generateKeyPair.getPrivate()));
    }
    
    @Override
    public void initialize(final int n, final SecureRandom secureRandom) {
        final RSAKeyGenerationParameters param = new RSAKeyGenerationParameters(KeyPairGeneratorSpi.defaultPublicExponent, secureRandom, n, PrimeCertaintyCalculator.getDefaultCertainty(n));
        this.param = param;
        this.engine.init(param);
    }
    
    @Override
    public void initialize(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        if (algorithmParameterSpec instanceof RSAKeyGenParameterSpec) {
            final RSAKeyGenParameterSpec rsaKeyGenParameterSpec = (RSAKeyGenParameterSpec)algorithmParameterSpec;
            final RSAKeyGenerationParameters param = new RSAKeyGenerationParameters(rsaKeyGenParameterSpec.getPublicExponent(), secureRandom, rsaKeyGenParameterSpec.getKeysize(), PrimeCertaintyCalculator.getDefaultCertainty(2048));
            this.param = param;
            this.engine.init(param);
            return;
        }
        throw new InvalidAlgorithmParameterException("parameter object not a RSAKeyGenParameterSpec");
    }
}
