package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;
import java.math.*;
import java.security.*;

public class RSAKeyGenerationParameters extends KeyGenerationParameters
{
    private int certainty;
    private BigInteger publicExponent;
    
    public RSAKeyGenerationParameters(final BigInteger publicExponent, final SecureRandom secureRandom, final int n, final int certainty) {
        super(secureRandom, n);
        if (n < 12) {
            throw new IllegalArgumentException("key strength too small");
        }
        if (publicExponent.testBit(0)) {
            this.publicExponent = publicExponent;
            this.certainty = certainty;
            return;
        }
        throw new IllegalArgumentException("public exponent cannot be even");
    }
    
    public int getCertainty() {
        return this.certainty;
    }
    
    public BigInteger getPublicExponent() {
        return this.publicExponent;
    }
}
