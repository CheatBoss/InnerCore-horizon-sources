package org.spongycastle.crypto.params;

import java.math.*;

public class ElGamalPublicKeyParameters extends ElGamalKeyParameters
{
    private BigInteger y;
    
    public ElGamalPublicKeyParameters(final BigInteger y, final ElGamalParameters elGamalParameters) {
        super(false, elGamalParameters);
        this.y = y;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof ElGamalPublicKeyParameters;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        boolean b3 = b2;
        if (((ElGamalPublicKeyParameters)o).getY().equals(this.y)) {
            b3 = b2;
            if (super.equals(o)) {
                b3 = true;
            }
        }
        return b3;
    }
    
    public BigInteger getY() {
        return this.y;
    }
    
    @Override
    public int hashCode() {
        return this.y.hashCode() ^ super.hashCode();
    }
}
