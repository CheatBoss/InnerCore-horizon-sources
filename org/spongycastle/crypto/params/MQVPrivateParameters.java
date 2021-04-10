package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;

public class MQVPrivateParameters implements CipherParameters
{
    private ECPrivateKeyParameters ephemeralPrivateKey;
    private ECPublicKeyParameters ephemeralPublicKey;
    private ECPrivateKeyParameters staticPrivateKey;
    
    public MQVPrivateParameters(final ECPrivateKeyParameters ecPrivateKeyParameters, final ECPrivateKeyParameters ecPrivateKeyParameters2) {
        this(ecPrivateKeyParameters, ecPrivateKeyParameters2, null);
    }
    
    public MQVPrivateParameters(final ECPrivateKeyParameters staticPrivateKey, final ECPrivateKeyParameters ephemeralPrivateKey, ECPublicKeyParameters ephemeralPublicKey) {
        if (staticPrivateKey == null) {
            throw new NullPointerException("staticPrivateKey cannot be null");
        }
        if (ephemeralPrivateKey == null) {
            throw new NullPointerException("ephemeralPrivateKey cannot be null");
        }
        final ECDomainParameters parameters = staticPrivateKey.getParameters();
        if (parameters.equals(ephemeralPrivateKey.getParameters())) {
            if (ephemeralPublicKey == null) {
                ephemeralPublicKey = new ECPublicKeyParameters(parameters.getG().multiply(ephemeralPrivateKey.getD()), parameters);
            }
            else if (!parameters.equals(ephemeralPublicKey.getParameters())) {
                throw new IllegalArgumentException("Ephemeral public key has different domain parameters");
            }
            this.staticPrivateKey = staticPrivateKey;
            this.ephemeralPrivateKey = ephemeralPrivateKey;
            this.ephemeralPublicKey = ephemeralPublicKey;
            return;
        }
        throw new IllegalArgumentException("Static and ephemeral private keys have different domain parameters");
    }
    
    public ECPrivateKeyParameters getEphemeralPrivateKey() {
        return this.ephemeralPrivateKey;
    }
    
    public ECPublicKeyParameters getEphemeralPublicKey() {
        return this.ephemeralPublicKey;
    }
    
    public ECPrivateKeyParameters getStaticPrivateKey() {
        return this.staticPrivateKey;
    }
}
