package org.spongycastle.crypto.params;

import java.math.*;

public class DHPrivateKeyParameters extends DHKeyParameters
{
    private BigInteger x;
    
    public DHPrivateKeyParameters(final BigInteger x, final DHParameters dhParameters) {
        super(true, dhParameters);
        this.x = x;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof DHPrivateKeyParameters;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        boolean b3 = b2;
        if (((DHPrivateKeyParameters)o).getX().equals(this.x)) {
            b3 = b2;
            if (super.equals(o)) {
                b3 = true;
            }
        }
        return b3;
    }
    
    public BigInteger getX() {
        return this.x;
    }
    
    @Override
    public int hashCode() {
        return this.x.hashCode() ^ super.hashCode();
    }
}
