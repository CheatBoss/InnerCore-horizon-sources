package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;
import java.math.*;

public class RSABlindingParameters implements CipherParameters
{
    private BigInteger blindingFactor;
    private RSAKeyParameters publicKey;
    
    public RSABlindingParameters(final RSAKeyParameters publicKey, final BigInteger blindingFactor) {
        if (!(publicKey instanceof RSAPrivateCrtKeyParameters)) {
            this.publicKey = publicKey;
            this.blindingFactor = blindingFactor;
            return;
        }
        throw new IllegalArgumentException("RSA parameters should be for a public key");
    }
    
    public BigInteger getBlindingFactor() {
        return this.blindingFactor;
    }
    
    public RSAKeyParameters getPublicKey() {
        return this.publicKey;
    }
}
