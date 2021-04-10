package org.spongycastle.crypto.params;

import java.math.*;

public class DSAPrivateKeyParameters extends DSAKeyParameters
{
    private BigInteger x;
    
    public DSAPrivateKeyParameters(final BigInteger x, final DSAParameters dsaParameters) {
        super(true, dsaParameters);
        this.x = x;
    }
    
    public BigInteger getX() {
        return this.x;
    }
}
