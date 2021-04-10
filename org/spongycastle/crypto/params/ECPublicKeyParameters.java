package org.spongycastle.crypto.params;

import org.spongycastle.math.ec.*;

public class ECPublicKeyParameters extends ECKeyParameters
{
    private final ECPoint Q;
    
    public ECPublicKeyParameters(final ECPoint ecPoint, final ECDomainParameters ecDomainParameters) {
        super(false, ecDomainParameters);
        this.Q = this.validate(ecPoint);
    }
    
    private ECPoint validate(ECPoint normalize) {
        if (normalize == null) {
            throw new IllegalArgumentException("point has null value");
        }
        if (normalize.isInfinity()) {
            throw new IllegalArgumentException("point at infinity");
        }
        normalize = normalize.normalize();
        if (normalize.isValid()) {
            return normalize;
        }
        throw new IllegalArgumentException("point not on curve");
    }
    
    public ECPoint getQ() {
        return this.Q;
    }
}
