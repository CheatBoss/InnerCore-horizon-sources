package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;

public class MQVPublicParameters implements CipherParameters
{
    private ECPublicKeyParameters ephemeralPublicKey;
    private ECPublicKeyParameters staticPublicKey;
    
    public MQVPublicParameters(final ECPublicKeyParameters staticPublicKey, final ECPublicKeyParameters ephemeralPublicKey) {
        if (staticPublicKey == null) {
            throw new NullPointerException("staticPublicKey cannot be null");
        }
        if (ephemeralPublicKey == null) {
            throw new NullPointerException("ephemeralPublicKey cannot be null");
        }
        if (staticPublicKey.getParameters().equals(ephemeralPublicKey.getParameters())) {
            this.staticPublicKey = staticPublicKey;
            this.ephemeralPublicKey = ephemeralPublicKey;
            return;
        }
        throw new IllegalArgumentException("Static and ephemeral public keys have different domain parameters");
    }
    
    public ECPublicKeyParameters getEphemeralPublicKey() {
        return this.ephemeralPublicKey;
    }
    
    public ECPublicKeyParameters getStaticPublicKey() {
        return this.staticPublicKey;
    }
}
