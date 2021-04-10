package org.spongycastle.crypto.params;

import java.math.*;

public class GOST3410PrivateKeyParameters extends GOST3410KeyParameters
{
    private BigInteger x;
    
    public GOST3410PrivateKeyParameters(final BigInteger x, final GOST3410Parameters gost3410Parameters) {
        super(true, gost3410Parameters);
        this.x = x;
    }
    
    public BigInteger getX() {
        return this.x;
    }
}
