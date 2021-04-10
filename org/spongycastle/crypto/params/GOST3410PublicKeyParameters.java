package org.spongycastle.crypto.params;

import java.math.*;

public class GOST3410PublicKeyParameters extends GOST3410KeyParameters
{
    private BigInteger y;
    
    public GOST3410PublicKeyParameters(final BigInteger y, final GOST3410Parameters gost3410Parameters) {
        super(false, gost3410Parameters);
        this.y = y;
    }
    
    public BigInteger getY() {
        return this.y;
    }
}
