package org.spongycastle.crypto.agreement;

import org.spongycastle.crypto.*;
import java.math.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.math.ec.*;

public class ECDHBasicAgreement implements BasicAgreement
{
    private ECPrivateKeyParameters key;
    
    @Override
    public BigInteger calculateAgreement(final CipherParameters cipherParameters) {
        final ECPublicKeyParameters ecPublicKeyParameters = (ECPublicKeyParameters)cipherParameters;
        if (!ecPublicKeyParameters.getParameters().equals(this.key.getParameters())) {
            throw new IllegalStateException("ECDH public key has wrong domain parameters");
        }
        final ECPoint normalize = ecPublicKeyParameters.getQ().multiply(this.key.getD()).normalize();
        if (!normalize.isInfinity()) {
            return normalize.getAffineXCoord().toBigInteger();
        }
        throw new IllegalStateException("Infinity is not a valid agreement value for ECDH");
    }
    
    @Override
    public int getFieldSize() {
        return (this.key.getParameters().getCurve().getFieldSize() + 7) / 8;
    }
    
    @Override
    public void init(final CipherParameters cipherParameters) {
        this.key = (ECPrivateKeyParameters)cipherParameters;
    }
}
