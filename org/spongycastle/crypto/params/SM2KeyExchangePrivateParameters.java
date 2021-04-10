package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;
import org.spongycastle.math.ec.*;

public class SM2KeyExchangePrivateParameters implements CipherParameters
{
    private final ECPrivateKeyParameters ephemeralPrivateKey;
    private final ECPoint ephemeralPublicPoint;
    private final boolean initiator;
    private final ECPrivateKeyParameters staticPrivateKey;
    private final ECPoint staticPublicPoint;
    
    public SM2KeyExchangePrivateParameters(final boolean initiator, final ECPrivateKeyParameters staticPrivateKey, final ECPrivateKeyParameters ephemeralPrivateKey) {
        if (staticPrivateKey == null) {
            throw new NullPointerException("staticPrivateKey cannot be null");
        }
        if (ephemeralPrivateKey == null) {
            throw new NullPointerException("ephemeralPrivateKey cannot be null");
        }
        final ECDomainParameters parameters = staticPrivateKey.getParameters();
        if (parameters.equals(ephemeralPrivateKey.getParameters())) {
            this.initiator = initiator;
            this.staticPrivateKey = staticPrivateKey;
            this.staticPublicPoint = parameters.getG().multiply(staticPrivateKey.getD()).normalize();
            this.ephemeralPrivateKey = ephemeralPrivateKey;
            this.ephemeralPublicPoint = parameters.getG().multiply(ephemeralPrivateKey.getD()).normalize();
            return;
        }
        throw new IllegalArgumentException("Static and ephemeral private keys have different domain parameters");
    }
    
    public ECPrivateKeyParameters getEphemeralPrivateKey() {
        return this.ephemeralPrivateKey;
    }
    
    public ECPoint getEphemeralPublicPoint() {
        return this.ephemeralPublicPoint;
    }
    
    public ECPrivateKeyParameters getStaticPrivateKey() {
        return this.staticPrivateKey;
    }
    
    public ECPoint getStaticPublicPoint() {
        return this.staticPublicPoint;
    }
    
    public boolean isInitiator() {
        return this.initiator;
    }
}
