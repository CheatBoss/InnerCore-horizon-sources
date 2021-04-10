package org.spongycastle.jce.spec;

import java.math.*;

public class ElGamalPublicKeySpec extends ElGamalKeySpec
{
    private BigInteger y;
    
    public ElGamalPublicKeySpec(final BigInteger y, final ElGamalParameterSpec elGamalParameterSpec) {
        super(elGamalParameterSpec);
        this.y = y;
    }
    
    public BigInteger getY() {
        return this.y;
    }
}
