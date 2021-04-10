package org.spongycastle.jce.spec;

import java.math.*;

public class ElGamalPrivateKeySpec extends ElGamalKeySpec
{
    private BigInteger x;
    
    public ElGamalPrivateKeySpec(final BigInteger x, final ElGamalParameterSpec elGamalParameterSpec) {
        super(elGamalParameterSpec);
        this.x = x;
    }
    
    public BigInteger getX() {
        return this.x;
    }
}
