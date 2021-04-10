package org.spongycastle.crypto.params;

import java.math.*;

public class ElGamalPrivateKeyParameters extends ElGamalKeyParameters
{
    private BigInteger x;
    
    public ElGamalPrivateKeyParameters(final BigInteger x, final ElGamalParameters elGamalParameters) {
        super(true, elGamalParameters);
        this.x = x;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof ElGamalPrivateKeyParameters && ((ElGamalPrivateKeyParameters)o).getX().equals(this.x) && super.equals(o);
    }
    
    public BigInteger getX() {
        return this.x;
    }
    
    @Override
    public int hashCode() {
        return this.getX().hashCode();
    }
}
