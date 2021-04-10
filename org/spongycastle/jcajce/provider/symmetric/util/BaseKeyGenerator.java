package org.spongycastle.jcajce.provider.symmetric.util;

import javax.crypto.*;
import org.spongycastle.crypto.*;
import javax.crypto.spec.*;
import java.security.spec.*;
import java.security.*;

public class BaseKeyGenerator extends KeyGeneratorSpi
{
    protected String algName;
    protected int defaultKeySize;
    protected CipherKeyGenerator engine;
    protected int keySize;
    protected boolean uninitialised;
    
    protected BaseKeyGenerator(final String algName, final int n, final CipherKeyGenerator engine) {
        this.uninitialised = true;
        this.algName = algName;
        this.defaultKeySize = n;
        this.keySize = n;
        this.engine = engine;
    }
    
    @Override
    protected SecretKey engineGenerateKey() {
        if (this.uninitialised) {
            this.engine.init(new KeyGenerationParameters(new SecureRandom(), this.defaultKeySize));
            this.uninitialised = false;
        }
        return new SecretKeySpec(this.engine.generateKey(), this.algName);
    }
    
    @Override
    protected void engineInit(final int n, final SecureRandom secureRandom) {
        SecureRandom secureRandom2 = secureRandom;
        Label_0017: {
            if (secureRandom != null) {
                break Label_0017;
            }
            try {
                secureRandom2 = new SecureRandom();
                this.engine.init(new KeyGenerationParameters(secureRandom2, n));
                this.uninitialised = false;
            }
            catch (IllegalArgumentException ex) {
                throw new InvalidParameterException(ex.getMessage());
            }
        }
    }
    
    @Override
    protected void engineInit(final SecureRandom secureRandom) {
        if (secureRandom != null) {
            this.engine.init(new KeyGenerationParameters(secureRandom, this.defaultKeySize));
            this.uninitialised = false;
        }
    }
    
    @Override
    protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        throw new InvalidAlgorithmParameterException("Not Implemented");
    }
}
